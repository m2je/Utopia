package ir.utopia.core;

import ir.utopia.core.bean.BeanUtil;
import ir.utopia.core.bean.UtopiaBean;
import ir.utopia.core.model.UsecaseUIInfo;
import ir.utopia.core.persistent.UtopiaBasicPersistent;
import ir.utopia.core.struts.UtopiaControl;
import ir.utopia.core.struts.UtopiaForm;
import ir.utopia.core.usecase.UsecaseUtil;
import ir.utopia.core.usecase.actionmodel.UseCase;
import ir.utopia.core.util.Cache;

import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public abstract class UIServiceFactory {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(UIServiceFactory.class.getName());
	}
	public static final String UI_CONFIG_FILE_NAME="UIConfig.xml";
	public static final String DEFAULT_UI_CONFIG_FILE_NAME="DefaultUIConfig.xml";
	
	private static final String USECASE_CONFIG_TAG_NAME="usecase";
	private static final String USECASE_CONFIG_TAG_NAME_PARAMETER="name";
	private static final String USECASE_CONFIG_TAG_SYSTEM_PARAMETER="system";
	private static final String USECASE_CONFIG_TAG_SUBSYSTEM_PARAMETER="subsystem";
	private static final String USECASE_CONFIG_TAG_FORM_PARAMETER="form";
//	private static final String USECASE_CONFIG_TAG_ACTION_PARAMETER="action";
	
	private static final Cache<Long, UsecaseUIInfo>USECASE_UI_INFO_CACE=new Cache<Long, UsecaseUIInfo>();
	private static final Cache<String, UsecaseUIInfo>USECASE_FORM_INFO_CACE=new Cache<String, UsecaseUIInfo>();
	static {
		intUsecase2ActionMapping();
		try{
			URL defaultXML=UIServiceFactory.class.getClassLoader().getResource(DEFAULT_UI_CONFIG_FILE_NAME);
			parseConfigXML(defaultXML);
			URL projectXML=UIServiceFactory.class.getClassLoader().getResource(UI_CONFIG_FILE_NAME);
			parseConfigXML(projectXML);
		}
		catch (Exception e){
			logger.log(Level.WARNING, "",e);
		}
		
		
	}
//**************************************************************************************	
	private static void intUsecase2ActionMapping(){
		/*
		RuntimeConfiguration configuration= Dispatcher.getInstance().getConfigurationManager().getConfiguration().getRuntimeConfiguration();
		Collection<Map<String,ActionConfig>>configMapCollection= configuration.getActionConfigs().values();
		HashSet<Class<?>>loadedClasses=new HashSet<Class<?>>();
		Map<String,String>form2RemoteMap=new HashMap<String, String>();
		for(Map<String,ActionConfig>configMap:configMapCollection){
			for(String key :configMap.keySet()){
				Object value= configMap.get(key);
				if(ActionConfig.class.isInstance(value)){
					ActionConfig config=(ActionConfig)value;
					try {
						Class<?>clazz= Class.forName(config.getClassName());
						if(loadedClasses.contains(clazz))continue;
						loadedClasses.add(clazz);
						if(UtopiaControl.class.isAssignableFrom(clazz)){
							@SuppressWarnings("unchecked")
							Class<? extends UtopiaForm<?>> formClass=(Class<? extends UtopiaForm<?>>) (((ParameterizedType) clazz.getGenericSuperclass())).getActualTypeArguments()[0];
							if(formClass!=null){
								@SuppressWarnings("unchecked")
								Class<? extends UtopiaBasicPersistent>persistentClass= (Class<? extends UtopiaBasicPersistent>) (((ParameterizedType) formClass.getGenericSuperclass())).getActualTypeArguments()[0];
								if(persistentClass!=null){
									Class<? extends UtopiaBean> remoteClass= BeanUtil.findRemoteClassFromPersistent(persistentClass);
									if(remoteClass!=null){
										form2RemoteMap.put(remoteClass.getName(),formClass.getName());
//										UseCase usecase= UsecaseUtil.getUseCase(remoteClass.getName()) ;
//										if(usecase!=null){
//											UsecaseUIInfo info=new UsecaseUIInfo(usecase, formClass.getName());
//											USECASE_UI_INFO_CACE.put(usecase.getUsecaseId(),info );
//											USECASE_FORM_INFO_CACE.put(formClass.getName(), info);
//										}
									}
								}
								
								
							}
							
							
						}
					} catch (Exception e) {
						logger.log(Level.WARNING, "",e);
					}
				}
				
			}
		}
		try {
			List<UseCase>usecases= UsecaseUtil.getUseCases(form2RemoteMap.keySet());
			for(UseCase usecase:usecases){
				String formClass=form2RemoteMap.get(usecase.getRemoteClassName());
				UsecaseUIInfo info=new UsecaseUIInfo(usecase, formClass);
				USECASE_UI_INFO_CACE.put(usecase.getUsecaseId(),info );
				USECASE_FORM_INFO_CACE.put(formClass, info);
			}
		} catch (Exception e) {
			logger.log(Level.WARNING, "",e);
		}
		*/
	} 
//**************************************************************************************	
	
	private static final void parseConfigXML(URL url) throws Exception{
		SAXBuilder builder = new SAXBuilder();
		InputStream instr=url.openStream();
		Document doc = builder.build(instr);
		Element root= doc.getRootElement();
		List<?>children= root.getChildren();
		
		for(Object cur: children){
			if(!(cur instanceof Element)){
				throw new IllegalArgumentException("invalid argument "+cur+" in "+url.getFile());
			}
		Element curElem= (Element)cur;
		String name= curElem.getName();
		if(USECASE_CONFIG_TAG_NAME.equals(name)){
			try {
				initUsecase(curElem);
			} catch (Exception e) {
				logger.log(Level.WARNING,"", e);
				continue;
			}
		}
		
		}
		instr.close();
	}
//**************************************************************************************
	private static final void initUsecase(Element e)throws Exception{
		String name= e.getAttributeValue(USECASE_CONFIG_TAG_NAME_PARAMETER);
		String system= e.getAttributeValue(USECASE_CONFIG_TAG_SYSTEM_PARAMETER);
		String subSystem= e.getAttributeValue(USECASE_CONFIG_TAG_SUBSYSTEM_PARAMETER);
		String form= e.getAttributeValue(USECASE_CONFIG_TAG_FORM_PARAMETER);
		UseCase usecase= UsecaseUtil.getUseCase(system,subSystem,name);
		if(usecase!=null&&form !=null){
			UsecaseUIInfo info=new UsecaseUIInfo(usecase,form);
			USECASE_UI_INFO_CACE.put(usecase.getUsecaseId(),info );
			USECASE_FORM_INFO_CACE.put(form, info);
		}
	}
//**************************************************************************************
	public static final UsecaseUIInfo getUsecase(Long usecaseId){
		return USECASE_UI_INFO_CACE.get(usecaseId);
	}
//**************************************************************************************
	public static final UsecaseUIInfo getUsecase(Class<?> formClass){
		return getUsecase(formClass.getName());
	}
//**************************************************************************************
	public static final UsecaseUIInfo getUsecase(String formClass){
		return USECASE_FORM_INFO_CACE.get(formClass);
	}
//**************************************************************************************	
}
