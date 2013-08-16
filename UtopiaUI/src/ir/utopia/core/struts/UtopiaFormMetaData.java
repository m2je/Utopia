/**
 * 
 */
package ir.utopia.core.struts;

import ir.utopia.core.bean.BeanUtil;
import ir.utopia.core.bean.UtopiaBean;
import ir.utopia.core.constants.Constants.predefindedActions;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.DataInputForm.InputPageLayout;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.NativeScriptType;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.form.annotation.SearchItem.SearchType;
import ir.utopia.core.logic.util.AnnotationUtil;
import ir.utopia.core.messagehandler.MessageHandler;
import ir.utopia.core.persistent.UtopiaBasicPersistent;
import ir.utopia.core.persistent.UtopiaPersistent;
import ir.utopia.core.persistent.annotation.LookupConfiguration;
import ir.utopia.core.persistent.lookup.model.LookupConfigurationModel;
import ir.utopia.core.util.tags.datainputform.converter.UtopiaUIHandler;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Enumerated;

import org.apache.commons.beanutils.MethodUtils;

/**
 * @author Salarkia
 *
 */
public class UtopiaFormMetaData implements Serializable {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(UtopiaFormMetaData.class.getName());
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -630235554677691486L;
	public static final Class<?>[]c=new Class[0];
	private UtopiaUIHandler handler;
	private String usecaseLoadMethod;
	private Class<? extends UtopiaBean>remoteUsecaseClass;
	private UtopiaFormMethodMetaData [] methodMetaData;
	private Class<?> clazz;
	private Annotation []annotations;
	private String idMethodName;
	private Class<? extends UtopiaBasicPersistent> persistentClass;
	private IncludedFormMetaData[]includedForms;
	private boolean useSearchPersistentForReport=true;
	private boolean useSearchPersistentForView=true;
	private boolean supportAttachment=false;
	private String[] validationFunctions;
	private String[] onLoadFunctions;
	private String[] afterLoadCallBacks;
	private List<CustomButtonConfiguration>  customButtons;
	private ItemsDisplayConfiguration titleMethods;
	private ItemsDisplayConfiguration descriptionMethods;
	private ItemsDisplayConfiguration indexMethods;
	private ItemsDisplayConfiguration lastModifiedDateMethods;
	private boolean revisionSupport;
	private List<String>CSSFiles;
	private List<FormColorLogic> colorLogic;
	private boolean customeSearch,customDataInput,customReport;
//*************************************************************************************
	public String[] getValidationFunctions() {
		return validationFunctions;
	}
//*************************************************************************************
	public void setValidationFunctions(String[] validationFunctions) {
		this.validationFunctions = validationFunctions;
	}
	private Map<NativeScriptType,UtopiaFormNativeConfiguration> nativeConfigurationMap=new HashMap<NativeScriptType, UtopiaFormNativeConfiguration>();
//*************************************************************************************		
	public UtopiaFormNativeConfiguration getNativeConfiguration(NativeScriptType type){
		UtopiaFormNativeConfiguration config= nativeConfigurationMap.get(type);
		if(config ==null){
			config=nativeConfigurationMap.get(NativeScriptType.all);
		}
		return config;
			}
//*************************************************************************************
	public void putNativeConfiguration(NativeScriptType type,UtopiaFormNativeConfiguration conf){
		nativeConfigurationMap.put(type, conf);
	}
//*************************************************************************************	
 public boolean isSupportAttachment() {
		return supportAttachment;
	}
//*************************************************************************************
public void setSupportAttachment(boolean supportAttachment) {
		this.supportAttachment = supportAttachment;
	}
//*************************************************************************************
	public Class<? extends UtopiaBean> getRemoteUsecaseClass() {
		return remoteUsecaseClass;
	}
//*************************************************************************************
	public void setRemoteUsecaseClass(Class<? extends UtopiaBean> remoteUsecaseClass) {
		this.remoteUsecaseClass = remoteUsecaseClass;
	}
//*************************************************************************************
	public String getUsecaseLoadMethod() {
		return usecaseLoadMethod;
	}
//*************************************************************************************
	public void setUsecaseLoadMethod(String usecaseLoadMethod) {
		this.usecaseLoadMethod = usecaseLoadMethod;
	}
//*************************************************************************************
	public UtopiaUIHandler getHandler() {
		return handler;
	}
//*************************************************************************************
	public void setHandler(UtopiaUIHandler handler) {
		this.handler = handler;
		handler.setMetaData(this);
	}
//*************************************************************************************
	public UtopiaFormMethodMetaData[] getMethodMetaData() {
		return methodMetaData;
	}
//*************************************************************************************	
	public UtopiaFormMethodMetaData getMethodMetaData(String fieldName) {
		for(UtopiaFormMethodMetaData meta:methodMetaData){
			if(meta.getFieldName()!=null&&meta.getFieldName().equals(fieldName)){
				return meta;
			}
		}
		return null;
	}
//*************************************************************************************
	public UtopiaFormMetaData(Class<?> clazz,Class<? extends UtopiaBasicPersistent> persistentClass,String idMethodName ){
		this.clazz=clazz;
		setIdMethodName(idMethodName);
		this.persistentClass=persistentClass;
	}
//*************************************************************************************
	public void setMethodMetaData(UtopiaFormMethodMetaData[] mothodMetaData) {
		this.methodMetaData = mothodMetaData;
		
	}
//*************************************************************************************
	/**
	 * @return owner class name as result
	 */
	public Class<?> getClazz() {
		return clazz;
	}
//*************************************************************************************
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
//*************************************************************************************
	public Annotation[] getAnnotations() {
		return annotations;
	}
//*************************************************************************************
	public void setAnnotations(Annotation[] annotations) {
		this.annotations = annotations;
	}
//*************************************************************************************
	public String getIdMethodName() {
		return idMethodName;
	}
//*************************************************************************************	
	public String getIdFieldName(){
		return AnnotationUtil.getPropertyName(getIdMethodName());
	}
//*************************************************************************************
	public void setIdMethodName(String idMethodName) {
		this.idMethodName = idMethodName;
	}
//*************************************************************************************	
	public Annotation getAnnotation(Class<? extends Annotation>clazz){
		if(annotations==null)return null;
		for(Annotation annot: annotations){
			if(clazz.isInstance(annot)){
				return annot;
			}
		}
		return null;
	}
//*************************************************************************************
	@SuppressWarnings("unchecked")
	public Class<? extends UtopiaPersistent> getLookupPersistentClass(UtopiaFormMethodMetaData meta){
		Class<? extends UtopiaPersistent> defaultPersitentClass=(Class<UtopiaPersistent>)meta.getPersistentMethodClass();
		
		InputItem item= (InputItem)meta.getAnnotation(InputItem.class);
		if(item==null)return defaultPersitentClass;
		Class<? extends UtopiaPersistent> persitentClass=null;
		if(UtopiaPersistent.class.isAssignableFrom( item.lookupEntityClass())){
			persitentClass=(Class<? extends UtopiaPersistent>)item.lookupEntityClass();
		}else {
			FormPersistentAttribute methodPersisMap =(FormPersistentAttribute)meta.getAnnotation(FormPersistentAttribute.class);
			  if(methodPersisMap!=null){
					Class<? extends UtopiaPersistent>sourceClass= FormUtil.getFormPersistentClass((Class<? extends UtopiaBasicForm<?>>) getClazz());
					String methodName= methodPersisMap.method();
					methodName= methodName==null||methodName.trim().length()==0?meta.getMethodName():methodName;
					if(methodName!=null&&methodName.trim().length()>0){
						try {
							Method method= sourceClass.getMethod(methodName, new Class<?>[]{});
							Class<?>re= method.getReturnType();
							if(re!=null){
								if(UtopiaPersistent.class.isAssignableFrom(re))
									persitentClass=(Class<? extends UtopiaPersistent>)re;
								else if(Collection.class.isAssignableFrom(re)){
									persitentClass=(Class<? extends UtopiaPersistent>) ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
								}
							}
						} catch (Exception e) {
							logger.log(Level.ALL,"",  e);
						}
					}
		}
		}
		return persitentClass==null?defaultPersitentClass:persitentClass;
	}
//***************************************************************************************	
	public Class<? extends UtopiaBasicPersistent> getPersistentClass() {
		return persistentClass;
	}
//***************************************************************************************
	public void setPersistentClass(Class<? extends UtopiaBasicPersistent> persistentClass) {
		this.persistentClass = persistentClass;
	}
//***************************************************************************************
	public Method getSetterMethodOf(UtopiaFormMethodMetaData meta){
		String methodName= AnnotationUtil.getSetterMethodName(meta.getMethodName());
		Method[]methods= clazz.getMethods();
		for(Method method :methods){
			if(method.getName().equals(methodName)){
				return method;
			}
		}
		return null;
	}
//***************************************************************************************	
	public Class<?> getEnumClass(UtopiaFormMethodMetaData meta){
		String methodname= meta.getPersistentMethodName();
		Method []methods= getPersistentClass().getMethods();
		for(Method method:methods){
			if(method.getName().equals(methodname)&&method.getAnnotation(Enumerated.class)!=null){
				return method.getReturnType();
			}
		}
		if(Enum.class.isAssignableFrom(meta.getReturnType())){
			return meta.getReturnType();
		}
		return null;
	}
//***************************************************************************************
	public IncludedFormMetaData[] getIncludedForms() {
		return includedForms;
	}
//***************************************************************************************
	public IncludedFormMetaData getIncludedForm(Class<?>formClass){
		if(includedForms==null)return null;
		for(IncludedFormMetaData included:includedForms){
			if(included.getFormClass().isAssignableFrom(formClass))
				return included;
		}
		return null;
	}
//***************************************************************************************
	public IncludedFormMetaData getIncludedForm(String formKey){
		if(includedForms==null)return null;
		for(IncludedFormMetaData included:includedForms){
			if(included.getName().equals(formKey))
				return included;
		}
		return null;
	}
//***************************************************************************************
	public void setIncludedForms(IncludedFormMetaData[] includedForms) {
		this.includedForms = includedForms;
	}
//***************************************************************************************
	public int getColumnCount(){
		for(Annotation annotation: getAnnotations()){
			if(DataInputForm.class.isInstance(annotation)){
				return ((DataInputForm)annotation).columnCount();
			}
		}
		return 3;
	}
//***************************************************************************************
	public InputPageLayout getPageLayout(){
		for(Annotation annotation: getAnnotations()){
			if(DataInputForm.class.isInstance(annotation)){
				return ((DataInputForm)annotation).pageLayout();
			}
		}
		return InputPageLayout.Simple;
	}
//******************************************************************************************			
			public  List<UtopiaFormMethodMetaData>  getInputItems() {
				
				UtopiaFormMethodMetaData []methodMetaDatas= getMethodMetaData();
				List<UtopiaFormMethodMetaData>result=new ArrayList<UtopiaFormMethodMetaData>();
				for(UtopiaFormMethodMetaData methodMetaData:methodMetaDatas){
					if(methodMetaData.getAnnotation(InputItem.class)!=null){
						result.add(methodMetaData);
					}

				}
				
				Collections.sort(result,new Comparator<UtopiaFormMethodMetaData>(){
					public int compare(UtopiaFormMethodMetaData o1, UtopiaFormMethodMetaData o2) {
						InputItem i1=(InputItem)o1.getAnnotation(InputItem.class);
						InputItem i2=(InputItem)o2.getAnnotation(InputItem.class);
						return i1.index()-i2.index();
					}});
				return result;
			}
//******************************************************************************************
			public List<UtopiaFormMethodMetaData> getSearchOnItems(){
				return getSearchItems(true);
			}
//******************************************************************************************
			public List<UtopiaFormMethodMetaData> getSearchResultItems(){
				return getSearchItems(false);
			}
//******************************************************************************************
			private List<UtopiaFormMethodMetaData>getSearchItems(boolean searchOn){
				UtopiaFormMethodMetaData []methodMetaDatas= getMethodMetaData();
				List<UtopiaFormMethodMetaData>result=new ArrayList<UtopiaFormMethodMetaData>();
				final Map<UtopiaFormMethodMetaData,Integer>indexMap=new HashMap<UtopiaFormMethodMetaData, Integer>();
				int index=-1;
				for(UtopiaFormMethodMetaData methodMetaData:methodMetaDatas){
					SearchItem searchItem=(SearchItem)methodMetaData.getAnnotation(SearchItem.class);
					if(searchItem!=null){
						if(searchOn&&!(SearchType.BOTH.equals(searchItem.searchConfiguration())||
								SearchType.SHOW_IN_SEARCH_ITEM.equals(searchItem.searchConfiguration()))){
							continue;
						}else if(!searchOn&&!(SearchType.BOTH.equals(searchItem.searchConfiguration())||
								SearchType.SHOW_IN_SEARCH_GRID.equals(searchItem.searchConfiguration()))){
							continue;
						}
							index=searchOn?searchItem.searchOnIndex():searchItem.searchGridIndex();
							index=index<0?searchItem.index():index;
						if(index<0){ 
							InputItem item=(InputItem)methodMetaData.getAnnotation(InputItem.class);
							index=item!=null?item.index():methodMetaDatas.length;	
						}
						
						indexMap.put(methodMetaData, index);
						result.add(methodMetaData);
					}
				}
				Collections.sort(result,new Comparator<UtopiaFormMethodMetaData>(){

					@Override
					public int compare(UtopiaFormMethodMetaData o1,
							UtopiaFormMethodMetaData o2) {
						return indexMap.get(o1)-indexMap.get(o2);
					}});
				return result;
			}
//******************************************************************************************
			public String getHeader(UtopiaFormMethodMetaData meta, String language){
				InputItem item =(InputItem)meta.getAnnotation(InputItem.class);
				String header=(item==null||item.name()==null||item.name().trim().length()==0)?AnnotationUtil.getPropertyName(meta.getMethodName()):item.name();
				header=MessageHandler.getMessage(header,clazz.getName() ,language);
				return header;
			}
//******************************************************************************************
			public String getHeader(CustomButtonConfiguration button, String language){
				String header=button.getText();
				header=MessageHandler.getMessage(header,clazz.getName() ,language);
				return header;
			}
//******************************************************************************************			
			public String[] getPersitentDetailFieldsToLoad() {
				IncludedFormMetaData []incudedForms= getIncludedForms();
				Set<String> result=null;
				if(incudedForms!=null&&incudedForms.length>0){
					result=new HashSet<String>();
					for(IncludedFormMetaData detail:incudedForms){
						String field= AnnotationUtil.getPropertyName(detail.getParentLoadMethod().getPersistentMethodName());
						result.add(field);
					}
				}
				return result==null?new String[0]:result.toArray(new String[result.size()]);
			}
//******************************************************************************************
			public List<UtopiaFormMethodMetaData> getReportItems(){
				ArrayList<UtopiaFormMethodMetaData>reportItems=new ArrayList<UtopiaFormMethodMetaData>();
				for( UtopiaFormMethodMetaData method:this.methodMetaData){
					if(method.displayOnReport())
						reportItems.add(method);
				}
				return reportItems;
			}

//******************************************************************************************
			public LookupConfigurationModel getLookupConfigurationModel(UtopiaFormMethodMetaData methodMeta,Map<String,Object>context){
				LookupConfigurationModel result=getFormLookupConfigurationModel(methodMeta,context);
				result=result==null?getPersistentLookupConfigurationModel(methodMeta, context):result;
				return result;
				}
//******************************************************************************************
			@SuppressWarnings("unchecked")
			protected LookupConfigurationModel getFormLookupConfigurationModel(UtopiaFormMethodMetaData methodMeta,Map<String,Object>context){
				LookupConfiguration lookupconf= (LookupConfiguration)methodMeta.getAnnotation(LookupConfiguration.class);
				Class<? extends UtopiaPersistent>persistentClass=lookupconf!=null?
						(UtopiaPersistent.class.isAssignableFrom(lookupconf.persistentClass()))?(Class<? extends UtopiaPersistent>)lookupconf.persistentClass():null
							:null;
					persistentClass=persistentClass==null?(Class<? extends UtopiaPersistent>)methodMeta.getPersistentMethodClass():persistentClass;
				if(lookupconf!=null&&persistentClass!=null){
					LookupConfigurationModel model=BeanUtil.getLookupConfigurationModel(context,persistentClass,lookupconf);
					return model;
				}else {
					persistentClass= getLookupPersistentClass(methodMeta);
					if(persistentClass!=null){
						return BeanUtil.getLookupConfigurationModel(context, persistentClass);
					}
				}
				return null;
			} 
//******************************************************************************************
			@SuppressWarnings("unchecked")
			protected LookupConfigurationModel getPersistentLookupConfigurationModel(UtopiaFormMethodMetaData methodMeta,Map<String,Object>context){
				if(methodMeta.isLookup()&&methodMeta.isPersistentMethod()){
				 	Class<?> persitentClass=(Class<?>) methodMeta.getPersistentMethodClass();
					if(UtopiaBasicPersistent.class.isAssignableFrom(persitentClass)){
								String persistentMethodName= methodMeta.getPersistentMethodName();
								if(persistentMethodName!=null&&persistentMethodName.length()>0){
									Method method=MethodUtils.getMatchingAccessibleMethod(getPersistentClass(), persistentMethodName, c);
									if(method!=null){
										LookupConfiguration conf=method.getAnnotation(LookupConfiguration.class);
										LookupConfigurationModel model=BeanUtil.getLookupConfigurationModel(context, (Class<? extends UtopiaBasicPersistent>)persitentClass,conf);
										return model;
									}
						}
					}
				
			}
			return null;
			}
//******************************************************************************************
	public List<UtopiaPageLinkMetaData> getLinkPages(){
		List<UtopiaPageLinkMetaData> result=new ArrayList<UtopiaPageLinkMetaData>();
		for(UtopiaFormMethodMetaData method:methodMetaData){
			UtopiaPageLinkMetaData link=method.getLink();
			if(link!=null){
				result.add(link);
			}
		} 
		return result;
	}
//******************************************************************************************	
	public boolean useSearchPersistentForReport() {
		return useSearchPersistentForReport;
	}
//******************************************************************************************		
	public void setUseSearchPersistentForReport(boolean useViewPersistentForReport) {
		this.useSearchPersistentForReport = useViewPersistentForReport;
	}
//******************************************************************************************	
	public boolean useSearchPersistentForView() {
		return useSearchPersistentForView;
	}
//******************************************************************************************	
	public void setUseSearchPersistentForView(boolean useSearchPersistentForView) {
		this.useSearchPersistentForView = useSearchPersistentForView;
	}
//*************************************************************************************
	public String[] getOnLoadFunctions() {
		return onLoadFunctions;
	}
//*************************************************************************************
	public void setOnLoadFunctions(String[] onLoadFunctions) {
		this.onLoadFunctions = onLoadFunctions;
	}
//*************************************************************************************
	public String[] getAfterLoadCallBacks() {
		return afterLoadCallBacks;
	}
//*************************************************************************************
	public void setAfterLoadCallBacks(String[] afterLoadCallBacks) {
		this.afterLoadCallBacks = afterLoadCallBacks;
	}
//*************************************************************************************
	public List<CustomButtonConfiguration> getCustomButtons(predefindedActions action) {
		if(customButtons!=null){
			ArrayList<CustomButtonConfiguration>customButtons=new ArrayList<CustomButtonConfiguration>();
			for(CustomButtonConfiguration button:this.customButtons){
				if(button.getActions()!=null){
					for(predefindedActions caction:button.actions){
						if(caction.equals(action)){
							customButtons.add(button);
						}
					}
				}
			}
			return customButtons;
		}
		return null;
	}
//*************************************************************************************
	public void setCustomButtons(List<CustomButtonConfiguration> customButtons) {
		this.customButtons = customButtons;
	}
//*************************************************************************************
	public ItemsDisplayConfiguration getTitleMethods() {
		return titleMethods;
	}
//*************************************************************************************
	public void setTitleMethods(ItemsDisplayConfiguration titleMethods) {
		this.titleMethods = titleMethods;
	}
//*************************************************************************************
	public ItemsDisplayConfiguration getDescriptionMethods() {
		return descriptionMethods;
	}
//*************************************************************************************
	public void setDescriptionMethods(
			ItemsDisplayConfiguration descriptionMethods) {
		this.descriptionMethods = descriptionMethods;
	}
//*************************************************************************************
	public boolean isRevisionSupport() {
		return revisionSupport;
	}
//*************************************************************************************
	public void setRevisionSupport(boolean revisionSupport) {
		this.revisionSupport = revisionSupport;
	}
//*************************************************************************************
	public ItemsDisplayConfiguration getIndexMethods() {
		return indexMethods;
	}
//*************************************************************************************
	public void setIndexMethods(ItemsDisplayConfiguration indexMethods) {
		this.indexMethods = indexMethods;
	}
//*************************************************************************************
	public ItemsDisplayConfiguration getLastModifiedDateMethods() {
		return lastModifiedDateMethods;
	}
//*************************************************************************************
	public void setLastModifiedDateMethods(
			ItemsDisplayConfiguration lastModifiedDateMethods) {
		this.lastModifiedDateMethods = lastModifiedDateMethods;
	}
//*************************************************************************************
	public List<String> getCSSFiles() {
		return CSSFiles;
	}
//*************************************************************************************
	public void setCSSFiles(List<String> cSSFiles) {
		CSSFiles = cSSFiles;
	}
//*************************************************************************************
	public List<FormColorLogic> getColorLogic() {
		return colorLogic;
	}
//*************************************************************************************
	public void setColorLogic(List<FormColorLogic> colorLogic) {
		this.colorLogic = colorLogic;
	}
//*************************************************************************************
	public boolean isCustomeSearch() {
		return customeSearch;
	}
//*************************************************************************************
	public void setCustomeSearch(boolean customeSearch) {
		this.customeSearch = customeSearch;
	}
//*************************************************************************************
	public boolean isCustomDataInput() {
		return customDataInput;
	}
//*************************************************************************************
	public void setCustomDataInput(boolean customDataInput) {
		this.customDataInput = customDataInput;
	}
//*************************************************************************************
	public boolean isCustomReport() {
		return customReport;
	}
//*************************************************************************************
	public void setCustomReport(boolean customReport) {
		this.customReport = customReport;
	}

}
