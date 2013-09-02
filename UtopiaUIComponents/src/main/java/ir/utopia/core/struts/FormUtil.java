package ir.utopia.core.struts;

import ir.utopia.core.bean.BeanUtil;
import ir.utopia.core.bean.UtopiaBean;
import ir.utopia.core.constants.Constants.predefindedActions;
import ir.utopia.core.form.annotation.ColorLogic;
import ir.utopia.core.form.annotation.CustomButton;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.DisplayFieldsConfiguration;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.IncludedForm;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.LinkPage;
import ir.utopia.core.form.annotation.NativeScript;
import ir.utopia.core.form.annotation.NativeScriptMessage;
import ir.utopia.core.form.annotation.NativeScriptType;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.form.annotation.UsecaseForm;
import ir.utopia.core.logic.util.AnnotationUtil;
import ir.utopia.core.persistent.UtopiaPersistent;
import ir.utopia.core.persistent.annotation.LookupConfiguration;
import ir.utopia.core.revision.annotation.RevisionSupport;
import ir.utopia.core.util.Cache;
import ir.utopia.core.util.tags.datainputform.converter.DefaultUtopiaUIHandler;
import ir.utopia.core.util.tags.datainputform.converter.UtopiaUIHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.beanutils.ConstructorUtils;

import com.google.gwt.dom.client.NativeEvent;

public class FormUtil {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(FormUtil.class.getName());
	}
	public static final String DEFAULT_USECASE_LOAD_METHOD="loadById"; 
	public static final Class<DefaultUtopiaUIHandler> DEFAULT_USECASE_UI_HANDLER= DefaultUtopiaUIHandler.class;
	private static final Cache<String,UtopiaFormMetaData>annotationsCache=new Cache<String, UtopiaFormMetaData>();
	private static Class<?>[]c=new Class<?>[0];
	private static final Cache<String,Class<? extends UtopiaPersistent>>formToEnityClassCache=new Cache<String,Class<? extends UtopiaPersistent>>();
	private static void initialize(Class<? extends UtopiaBasicForm<?> > clazz){
		
		if(!annotationsCache.containsKey(clazz.getName())){//annotationsCache.clear()
			Class<? extends UtopiaPersistent> PClazz=getFormPersistentClass(clazz);
			if(PClazz==null){
				annotationsCache.put(clazz.getName(),null);
				return;
			}
			UsecaseForm usecaseForm= getAnnotation(clazz,UsecaseForm.class);
			DataInputForm dataInputInfo=getAnnotation(clazz,DataInputForm.class);
			UtopiaFormMetaData metaData=getUsecaseMetaData(clazz, PClazz,usecaseForm, dataInputInfo);
			annotationsCache.put(clazz.getName(), metaData);
		}
	}
//*******************************************************************************************
	private static  <T extends Annotation> T getAnnotation(Class<?>clazz, Class<T>annotation){
		if(clazz==null||Object.class.equals(clazz))return null;
		T annot= clazz.getAnnotation(annotation);
		if(annot==null){
			annot= getAnnotation(clazz.getSuperclass(),annotation);
			if(annot==null){
				Class<?>[]interfaces= clazz.getInterfaces();
				if(interfaces!=null){
					for(Class<?> c:interfaces ){
						annot=getAnnotation(c, annotation);
						if(annot!=null){
							return annot;
						}
					}
				}
			}
		}
		return annot;
	}
//*******************************************************************************************
	private static UtopiaFormMetaData getUsecaseMetaData(
			Class<? extends UtopiaBasicForm<?>> clazz,
			Class<? extends UtopiaPersistent> PClazz,UsecaseForm usecaseForm, DataInputForm dataInputInfo) {
		UtopiaFormMetaData metaData=new UtopiaFormMetaData(clazz,PClazz,findIdMethod(clazz));
		metaData.setHandler(getUIHandler(usecaseForm));
		String loadMethodName=usecaseForm==null?DEFAULT_USECASE_LOAD_METHOD:usecaseForm.usecaseLoadMethod();
		loadMethodName=loadMethodName==null||loadMethodName.trim().length()==0?null:loadMethodName.trim();
		Class<? extends UtopiaBean>remoteClass=usecaseForm==null?UtopiaBean.class: usecaseForm.remoteUsecaseClass();
		boolean useSearchObjectForReport=usecaseForm==null?true:usecaseForm.useSearchObjectForReport();
		boolean useSearchObjectForView=usecaseForm==null?true:usecaseForm.useSearchObjectForView();
		if(usecaseForm!=null){
			metaData.setCustomDataInput(usecaseForm.hasCustomDataInputPage());
			metaData.setCustomReport(usecaseForm.hasCustomViewPage());
			metaData.setCustomeSearch(usecaseForm.hasCustomSearchPage());
		}
		remoteClass=remoteClass==null||remoteClass.equals(UtopiaBean.class)?BeanUtil.findRemoteClassFromPersistent(PClazz):remoteClass;
		metaData.setRemoteUsecaseClass(remoteClass);
		metaData.setUsecaseLoadMethod(loadMethodName);
		metaData.setUseSearchPersistentForReport(useSearchObjectForReport);
		metaData.setUseSearchPersistentForView(useSearchObjectForView);
		boolean supportAttachment=usecaseForm!=null?usecaseForm.supportAttachment():false;
		metaData.setSupportAttachment(supportAttachment);
		ArrayList<UtopiaFormMethodMetaData>methodAnnotations=new ArrayList<UtopiaFormMethodMetaData>();
		Method[] methods= clazz.getMethods();
		for(Method method:methods){
			Annotation []annotations= method.getAnnotations();
			String methodName=method.getName();
			annotations=getRelatedAnnotations(annotations,methodName);
			if(annotations.length>0){
				UtopiaFormMethodMetaData methodMeta=new UtopiaFormMethodMetaData(methodName,annotations,method.getReturnType());
				String pmethod= methodMeta.getPersistentMethodName();
				if(pmethod!=null){
					try {
						methodMeta.setPersistentMethodClass(PClazz.getMethod(pmethod, c).getReturnType());
					} catch(NoSuchMethodException e){
						logger.log(Level.WARNING,"fail to find method:\""+pmethod +"\" in class :\""+PClazz.getName()+" \"");
						methodMeta.setPersistentMethodClass(methodMeta.getReturnType());
					}catch (Exception e) {
						logger.log(Level.WARNING,"", e);
					}
				}
				methodAnnotations.add(methodMeta);
			 }
		}	
		if(methodAnnotations.size()>0){
			UtopiaFormMethodMetaData []methodsMeta=methodAnnotations.toArray(new UtopiaFormMethodMetaData[methodAnnotations.size()]);
			metaData.setAnnotations(getRelatedAnnotations(clazz.getAnnotations(),null));
			metaData.setMethodMetaData(methodsMeta);
		}
		IncludedForm []forms;
		if(usecaseForm!=null){
				forms =usecaseForm.includedForms();
		}else{
			forms=new IncludedForm[0];
		}
		if(dataInputInfo!=null){
			IncludedForm []dataInputIncludedForms=dataInputInfo.includedForms();
			if(dataInputIncludedForms!=null&&dataInputIncludedForms.length>0){
				IncludedForm []temp=new IncludedForm[dataInputIncludedForms.length+forms.length];
				System.arraycopy(forms, 0, temp, 0, forms.length);
				System.arraycopy(dataInputIncludedForms, 0, temp, forms.length, dataInputIncludedForms.length);
				forms=temp;
			}
		}
		List<IncludedFormMetaData>includeds=getIncludedForms(forms, metaData);
		metaData.setIncludedForms((IncludedFormMetaData[])includeds.toArray(new IncludedFormMetaData[includeds.size()]));
		NativeScript usecaseNativeScript=usecaseForm!=null?usecaseForm.nativeScripts():null;
		NativeScript dataInpuFormNativeScript=dataInputInfo!=null?dataInputInfo.nativeScript():null;
		String []validationfuctions=dataInputInfo!=null?dataInputInfo.validationFunctions():null;
		metaData.setValidationFunctions(validationfuctions);
		String []onLoadFuctions=dataInpuFormNativeScript!=null?dataInpuFormNativeScript.onLoadMethods():usecaseNativeScript.onLoadMethods();
		String []afterLoadCallBacks=dataInpuFormNativeScript!=null?dataInpuFormNativeScript.afterLoadCallBacks():usecaseNativeScript.afterLoadCallBacks();
		metaData.setOnLoadFunctions(onLoadFuctions);
		metaData.setAfterLoadCallBacks(afterLoadCallBacks);
		initNativeConfigurations(metaData, dataInpuFormNativeScript, usecaseNativeScript);
		metaData.setCustomButtons(getCustomButtons( dataInputInfo));
		
		initTitleMethods(metaData, usecaseForm,dataInputInfo);
		initDescriptionMethods(metaData, usecaseForm,dataInputInfo);
		initIndexMethods(metaData, usecaseForm, dataInputInfo);
		initModifiedMethods(metaData, usecaseForm, dataInputInfo);
		initCSSFiles(metaData, usecaseForm, dataInputInfo);
		initColorLogics(metaData,usecaseForm,dataInputInfo);
		RevisionSupport revision=PClazz.getAnnotation(RevisionSupport.class);
		metaData.setRevisionSupport(revision!=null);
		return metaData;
	}
//*******************************************************************************************
	private static void initTitleMethods(UtopiaFormMetaData metaData, UsecaseForm usecaseForm, DataInputForm dataInputInfo){
		DisplayFieldsConfiguration config=findDisplayConfiguration(usecaseForm==null?null:usecaseForm.title(),dataInputInfo==null?null:dataInputInfo.title());
		metaData.setTitleMethods(new ItemsDisplayConfiguration(findMethodsMeta( metaData,config.displayFields(),"name","subject","title"),config.seperator()));
	}
//*******************************************************************************************
	private static void initDescriptionMethods(UtopiaFormMetaData metaData,  UsecaseForm usecaseForm, DataInputForm dataInputInfo){
		DisplayFieldsConfiguration config=findDisplayConfiguration(usecaseForm==null?null:usecaseForm.description(),dataInputInfo==null?null:dataInputInfo.description());
		metaData.setDescriptionMethods(new ItemsDisplayConfiguration(findMethodsMeta( metaData,config.displayFields(),"description"),config.seperator()));
	}
//*******************************************************************************************
	private static void initIndexMethods(UtopiaFormMetaData metaData,  UsecaseForm usecaseForm, DataInputForm dataInputInfo){
		DisplayFieldsConfiguration config=findDisplayConfiguration(usecaseForm==null?null:usecaseForm.index(),dataInputInfo==null?null:dataInputInfo.index());
		metaData.setIndexMethods(new ItemsDisplayConfiguration(findMethodsMeta( metaData,config.displayFields(),"docNumber","docNo","index","documentNumber"),config.seperator()));
	}
//*******************************************************************************************
	private static void initModifiedMethods(UtopiaFormMetaData metaData,  UsecaseForm usecaseForm, DataInputForm dataInputInfo){
		DisplayFieldsConfiguration config=findDisplayConfiguration(usecaseForm==null?null:usecaseForm.modifiedDate(),dataInputInfo==null?null:dataInputInfo.modifiedDate());
		metaData.setLastModifiedDateMethods(new ItemsDisplayConfiguration(findMethodsMeta( metaData,config.displayFields(),"updated"), config.seperator()) );
	}
//*******************************************************************************************
	private static void initCSSFiles(UtopiaFormMetaData metaData,  UsecaseForm usecaseForm, DataInputForm dataInputInfo){
		String []usCSSFiles=usecaseForm!=null? usecaseForm.CSSFiles():null;
		String []dataInCSSFiles=dataInputInfo!=null?dataInputInfo.CSSFiles():null;
		List<String>cssFiles=null;
		if(usCSSFiles!=null&&usCSSFiles.length>0){
			cssFiles=Arrays.asList(usCSSFiles) ;
		}
		if(dataInCSSFiles!=null&&dataInCSSFiles.length>0){
			if(cssFiles!=null){
				cssFiles.addAll(Arrays.asList(dataInCSSFiles));
			}else{
				cssFiles=Arrays.asList(dataInCSSFiles);
			}
		}
		metaData.setCSSFiles(cssFiles);
	}
//*******************************************************************************************	
	private static void initColorLogics(UtopiaFormMetaData metaData,  UsecaseForm usecaseForm, DataInputForm dataInputInfo){
		ColorLogic[]colorLogics= usecaseForm!=null&&usecaseForm.colorLogics().length>0?usecaseForm.colorLogics(): dataInputInfo.colorLogics();
		if(colorLogics!=null&&colorLogics.length>0){
			ArrayList<FormColorLogic>colorLogicsArray=new ArrayList<FormColorLogic>();
			for(ColorLogic logic:colorLogics){
				FormColorLogic colorLogic=new FormColorLogic();
				colorLogic.setCSSClass(logic.CSSClass());
				colorLogic.setLogic(logic.logic());
				colorLogicsArray.add(colorLogic);
			}
			metaData.setColorLogic(colorLogicsArray);
		}
	}
//*******************************************************************************************
	private static DisplayFieldsConfiguration findDisplayConfiguration(DisplayFieldsConfiguration conf1,DisplayFieldsConfiguration conf2){
		if(conf1==null||conf1.displayFields()==null||conf1.displayFields().length==0){
			return conf2;
		}
		return conf1;
	}
//*******************************************************************************************
	private static final List<UtopiaFormMethodMetaData> findMethodsMeta(UtopiaFormMetaData metaData,String[] methodNames,String ...alternates){
		List<UtopiaFormMethodMetaData>result=new ArrayList<UtopiaFormMethodMetaData>();
		if(methodNames!=null){
			for(String method:methodNames){
				UtopiaFormMethodMetaData methodMeta= metaData.getMethodMetaData(method);
				if(methodMeta!=null){
					result.add(methodMeta);
				}
			}
		}
		if(result.size()==0){
			for(String alternate: alternates){
				UtopiaFormMethodMetaData methodMeta= metaData.getMethodMetaData(alternate);
				if(methodMeta!=null){
					 result.add(methodMeta);
					 break;
				}
			}
		}
		return result;
	}
//*******************************************************************************************
	private static void initNativeConfigurations(UtopiaFormMetaData metaData ,NativeScript dataInpuFormNativeScript,NativeScript usecaseNativeScript){
		if(dataInpuFormNativeScript!=null){
			NativeScriptType[] types= dataInpuFormNativeScript.scriptTypes();
			for(NativeScriptType type:types){
				List<ir.utopia.core.struts.UtopiaFormNativeConfiguration.NativeScriptMessage> nativeMessages = convertNativeScriptMessages(metaData,dataInpuFormNativeScript);
				metaData.putNativeConfiguration(type, new UtopiaFormNativeConfiguration(dataInpuFormNativeScript.script(),dataInpuFormNativeScript.validationFunctions(), nativeMessages));
			}
			 
		}
		if(usecaseNativeScript!=null&&((usecaseNativeScript.validationFunctions()!=null&&
									  usecaseNativeScript.validationFunctions().length>0)
									  ||(usecaseNativeScript.script()!=null&&usecaseNativeScript.script().trim().length()>0))){
			if(dataInpuFormNativeScript!=null){
				logger.log(Level.WARNING,"native configuration for dataInputForm of form class:["+metaData.getClazz()+"] is being override by usecase native configuration ");
			}
			NativeScriptType[] types= usecaseNativeScript.scriptTypes();
			for(NativeScriptType type:types){
				List<ir.utopia.core.struts.UtopiaFormNativeConfiguration.NativeScriptMessage> nativeMessages = convertNativeScriptMessages(metaData,usecaseNativeScript);
				metaData.putNativeConfiguration(type, new UtopiaFormNativeConfiguration(usecaseNativeScript.script(),usecaseNativeScript.validationFunctions(),nativeMessages));
			}
			 
		}
	}
//*******************************************************************************************
	private static List<ir.utopia.core.struts.UtopiaFormNativeConfiguration.NativeScriptMessage> convertNativeScriptMessages(UtopiaFormMetaData metaData,
			NativeScript usecaseNativeScript) {
		if(usecaseNativeScript!=null){
		NativeScriptMessage []messages= usecaseNativeScript.messages();
		List<ir.utopia.core.struts.UtopiaFormNativeConfiguration.NativeScriptMessage>nativeMessages=new ArrayList<ir.utopia.core.struts.UtopiaFormNativeConfiguration.NativeScriptMessage>();
		if(messages!=null){
			for(NativeScriptMessage msg:messages){
				String bundle= msg.bundle();
				if(bundle==null||bundle.trim().length()==0){
					bundle=metaData.getClazz().getName();
				}
				nativeMessages.add(new ir.utopia.core.struts.UtopiaFormNativeConfiguration.NativeScriptMessage(msg.name(), msg.message(),bundle ));
			}
		}
		return nativeMessages;
		}
		return null;
	}
//*******************************************************************************************
	private static List<CustomButtonConfiguration> getCustomButtons(DataInputForm dataInputInfo){
		List<CustomButtonConfiguration> resButtons=new ArrayList<CustomButtonConfiguration>();
		if(dataInputInfo!=null){
		CustomButton []buttons= dataInputInfo.customButtons();
		if(buttons!=null){
			for(CustomButton customButton:buttons){
				CustomButtonConfiguration newButton=new CustomButtonConfiguration(new predefindedActions[]{predefindedActions.save,predefindedActions.update});
				newButton.setCssClass(customButton.cssClass());
				newButton.setReadOnlyLogic(customButton.readOnlyLogic());
				newButton.setDisplayLogic(customButton.displayLogic());
				newButton.setOnClick(customButton.onclick());
				newButton.setText(customButton.text());
				newButton.setId(customButton.Id());
				resButtons.add(newButton);
			}
		}
		}
		return resButtons;
		
	}
//*******************************************************************************************
	private static List<IncludedFormMetaData> getIncludedForms(IncludedForm []forms,UtopiaFormMetaData metaData){
		ArrayList<IncludedFormMetaData>includeds=new ArrayList<IncludedFormMetaData>();
		if(forms!=null){
			for(IncludedForm form:forms){
				String parentLinkMethod= metaData.getIdMethodName();
				String myLinkMethod=form.myLinkMethod();
				myLinkMethod=myLinkMethod==null||myLinkMethod.trim().length()==0?
								parentLinkMethod:myLinkMethod.trim();
				String parentLoadField=form.parentLoadMethod();
				parentLoadField=(parentLoadField!=null&&parentLoadField.trim().length()>0)?
						AnnotationUtil.getPropertyName(parentLoadField):form.name();
				UtopiaFormMethodMetaData parentLoadMethod= metaData.getMethodMetaData(parentLoadField);
				parentLoadMethod.setIncludedLoadMethod(true);
				IncludedFormMetaData includedMeta=new IncludedFormMetaData(form.name(),form.formClass(),parentLoadMethod,form.includedFormDisplayType());
				includedMeta.setMyLinkMethod(myLinkMethod);
				includedMeta.setType(form.includedFormType());
				String formKey=form.includedFormColumnName();
				formKey=formKey==null||formKey.trim().length()==0?form.formClass().getName():formKey.trim();
				includedMeta.setIncludedFormColumnName(formKey);
				includedMeta.setInsertRowAllowed(form.insertRowAllowed());
				includedMeta.setUpdateable(form.updateable());
				includedMeta.setPreferedWith(form.preferedWidth());
				includedMeta.setPreferedHeigth(form.preferedHeigth());
				includedMeta.setAutoFitColumns(form.autofitColumns());
				ColorLogic[]colorLogics= form.colorLogics();
				if(colorLogics!=null&&colorLogics.length>0){
					ArrayList<FormColorLogic>colorLogicsArray=new ArrayList<FormColorLogic>();
					for(ColorLogic logic:colorLogics){
						FormColorLogic colorLogic=new FormColorLogic();
						colorLogic.setCSSClass(logic.CSSClass());
						colorLogic.setLogic(logic.logic());
						colorLogicsArray.add(colorLogic);
					}
					includedMeta.setColorLogic(colorLogicsArray);
				}
				includeds.add(includedMeta);
			}
		}
		return includeds;
	}
//*******************************************************************************************
	private static String findIdMethod(Class<?>clazz){
		Method[] methods= clazz.getMethods();
		String formId=null;
		for(Method method:methods){
			String methodName=method.getName();
			if(methodName.startsWith("get")){
				Annotation annot= method.getAnnotation(FormId.class);
				if(annot!=null){
					if(formId==null){
						formId=methodName;
					}else{
						throw new IllegalArgumentException("Multiple form id defined for form "+clazz.getName());
					}
				}
			}
		}
		if(formId!=null)return formId;
		throw new IllegalArgumentException("form id not defined for class "+clazz);
	}
	
//*******************************************************************************************
	private static UtopiaUIHandler getUIHandler(UsecaseForm usecaseForm){
		try {
			if(usecaseForm==null){
				return new DefaultUtopiaUIHandler();
			}
				return (UtopiaUIHandler)ConstructorUtils.invokeConstructor(usecaseForm.UIHandlerClass(), new Object[]{}) ;
		} catch (Exception e) {
			logger.log(Level.WARNING,"" ,e);
		}
		return new DefaultUtopiaUIHandler();
	}
//*******************************************************************************************
	private static Annotation[] getRelatedAnnotations(Annotation []annotations,String methodName){
			ArrayList<Annotation>result=new ArrayList<Annotation>();
			for(Annotation annotation:annotations){
				if(SearchItem.class.isInstance(annotation)||FormPersistentAttribute.class.isInstance(annotation)||
						PersistedMapForm.class.isInstance(annotation)||FormId.class.isInstance(annotation)||
						InputItem.class.isInstance(annotation)||FormPersistentAttribute.class.isInstance(annotation)||
						DataInputForm.class.isInstance(annotation)||
						IncludedForm.class.isInstance(annotation)||
						LookupConfiguration.class.isInstance(annotation)||
						LinkPage.class.isInstance(annotation)||
						NativeScript.class.isInstance(annotation)||
						NativeEvent.class.isInstance(annotation)){
					result.add(annotation);
				}
			}
			return result.toArray(new Annotation[result.size()]);
		}
//*******************************************************************************************
	public static UtopiaFormMetaData getMetaData(Class<? extends UtopiaBasicForm<?> >clazz){
		initialize(clazz);
		return annotationsCache.get(clazz.getName());
	}
//*******************************************************************************************
	@SuppressWarnings("unchecked")
	public static UtopiaFormMetaData getMetaData(String clazz){
		if(clazz ==null||clazz.trim().length()==0)throw new IllegalArgumentException("invalid calss name"+clazz);
	try {
		Class<?>c= Class.forName(clazz);
		return getMetaData((Class<? extends UtopiaBasicForm<?> >)c);
	} catch (Exception e) {
		e.printStackTrace();
		logger.log( Level.ALL,"",e);
	}
	return null;
	}
//*******************************************************************************************
	@SuppressWarnings("unchecked")
	public static Class<? extends UtopiaPersistent> getFormPersistentClass(Class<? extends  UtopiaBasicForm<?>>clazz){
		String key=clazz.getName();
		 if(!formToEnityClassCache.containsKey(key)){
				Type type=clazz.getGenericSuperclass();
				while((type instanceof Class<?>)&& UtopiaBasicForm.class.isAssignableFrom((Class<?>)type)){
					Class<?>superClass= clazz.getSuperclass();
					if(!UtopiaBasicForm.class.isAssignableFrom(superClass)){
						 formToEnityClassCache.put(key, null);
						 return null;
					}
					type=superClass.getGenericSuperclass();
				}
				if(!(type instanceof ParameterizedType)){
					formToEnityClassCache.put(key, null);
					return null;
				}
				try {
					formToEnityClassCache.put(key,(Class<UtopiaPersistent>) ((ParameterizedType)type).getActualTypeArguments()[0]);
				} catch (Exception e) {
					logger.log(Level.ALL,"", e);
					formToEnityClassCache.put(key, null);
					return null;	
				}
				
			}
		return formToEnityClassCache.get(key);
	}
//*******************************************************************************************
}
