/**
 * 
 */
package ir.utopia.core;

import ir.utopia.common.systems.subsystem.persistent.CmSubsystem;
import ir.utopia.common.systems.system.bean.CmSystemFacadeRemote;
import ir.utopia.common.systems.system.persistent.CmSystem;
import ir.utopia.core.bean.UtopiaBean;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.exception.CoreException;
import ir.utopia.core.factory.BasicCorePlugin;
import ir.utopia.core.model.SubSystem;
import ir.utopia.core.process.ProcessHandler;
import ir.utopia.core.scheduler.ScheduledBeanFacadeRemote;
import ir.utopia.core.scheduler.model.SchedulerModel;
import ir.utopia.core.security.AfterLoginProcess;
import ir.utopia.core.security.SecurityProvider;
import ir.utopia.core.util.Cache;
import ir.utopia.core.util.language.LanguagePatch;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.beanutils.ConstructorUtils;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;




/**
 * @author Salarkia
 *
 * loads basic\advance feature configuration from configuration xml file 
 * and initialize the mandatory or optional services
 * and introduce the implemented service 
 */
public final class ServiceFactory {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(ServiceFactory.class.getName());
	}
	/**
	 * core configuration file to identify every core element provider 
	 * as security provider , ...  
	 */
	public static final String CORE_CONFIGURATION_FILE_NAME="CoreConf.xml";
	
	public static final String DEFAULT_CONFIGURATION_FILE_NAME="conf.xml";
	/**
	 * security provider tag name
	 */
	public static final String SECURITY_PROVIDER_TAG_NAME="security-provider";
	/**
	 * 
	 */
	public static final String PROCCESS_HANDLER="process-handler";
	
	/**
	 * After user login this class fired!
	 */
	public static final String AFTER_LOGIN_PROCESS_CLASS_NAME = "after-login-process";

	/**
	 * security provider classPath
	 */
	public static final String SECURITY_PROVIDER_CLASS_PATH="class-path";
	/**
	 * system name tag
	 */
	public static final String SYSTEM_TAG_NAME="system";
	/**
	 * subSystem name tag 
	 */
	public static final String SUB_SYSTEM_TAG_NAME="subSystem";
	/**
	 * menu bundle tag name
	 */
	public static final String MENU_BUNDLE_TAG_NAME="menu-bundel";
	/**
	 * useCase bundle tag name 
	 */
	public static final String USECASE_BUNDLE_TAG_NAME="usecase-Bundel";
	/**
	 * global external glossory
	 */
	public static final String GLOBAL_BUNDEL_TAG_NAME="global-Bundel";
	/**
	 * exception bundel path
	 */
	public static final String EXCEPTION_BUNDEL_TAG_NAME="Exeption-Bundel";
	/**
	 * 
	 */
	public static final String DATABSE_CONSTRAINTS_TAG_NAME="DB-Constraint-Bundel";
	/**
	 * 
	 */
	public static final String DATABASE_COLUMN_BUNDEL_TAG_NAME="DB-Column-Bundel";
	/**
	 * ORB initial host for looking up EJB facades
	 */
	public static final String ORB_INITIAL_HOST_TAG="ORB-Host";
	/**
	 * ORB initial port for looking up EJB facades
	 */
	public static final String ORB_INITIAL_PORT_TAG="ORB-Port";
	/**
	 * EJB container configuration tag
	 */
	public static final String EJB_CONTAINER_CONFIGUATION_TAG="EJB-Container-Conf";
	/**
	 * 
	 */
	public static final String EJB_INITIAL_CONTEXT_PROPERTY_TAG="Context-Property";
	/**
	 * 
	 */
	public static final String  EJB_INITIAL_CONTEXT_PROPERTY_ATTRIBUTE="parameterName";
	/**
	 * Schedule task configuration
	 */
	public static final String SCHEDULE_TASK="Schedule";
	/**
	 * 
	 */
	public static final String SCHEDULE_TASK_FACADE="Facade";
	/**
	 * 
	 */
	public static final String SCHEDULE_TASK_START_TIME="StartTime";
	/**
	 * 
	 */
	public static final String SCHEDULE_TASK_INTERVAL="Interval";
	/**
	 * 
	 */
	public static final String SCHEDULE_TASK_PARAMETER="Parameter";
	/**
	 * 
	 */
	public static final String SCHEDULE_TASK_PARAMETER_NAME="Name";
	/**
	 * 
	 */
	public static final String ORGANIZATION_SUPPORT="organization-support";
	/**
	 * 
	 */
	public static final String FISCAL_YEAR_SUPPORT="fiscalYear-support";
	/**
	 * 
	 */
	public static final String LANGUAGE_PATCH_TAG="Language-Patch";
	/**
	 * 
	 */
	public static final String PATCH_IMPL_TAG= "Patch-Impl";
	/**
	 * 
	 */
	public static final String LANGUAGE_TAG="Language";
	/**
	 * default welcome page
	 */
	public static final String WELCOME_URL="../UtopiaCoreUI/pages/welcome.jsp";
	
	private static boolean CONFIGURATION_LOADED=true;
	
	private static Set<String>GLOBAL_GLASSORY_LIST=new HashSet<String>();
	
	private static Set<String>EXCEPTION_BUNDELS=new HashSet<String>();
	
	private static Set<String>DATABSE_CONSTRAINTS_BUNDELS=new HashSet<String>();
	
	private static Set<String>DATABSE_COLUMNS_BUNDELS=new HashSet<String>();
	
	private static ProcessHandler processHandler;
	
	private static boolean supportsOrganization=false;
	
	private static boolean supportsFiscalYear=false;
	
	private static ConcurrentHashMap<String,  LanguagePatch>languagePatchMap=new ConcurrentHashMap<String, LanguagePatch>();
	/**
	 * After login this process run automatically
	 */
	private static List<AfterLoginProcess> afterLoginProcess=new ArrayList<AfterLoginProcess>();
	private static Properties props;
	private static final Cache<String, ir.utopia.core.model.System> SYSTEM_CACHE=new Cache<String, ir.utopia.core.model.System>(); 
	static{
		
			
	    	try {
	    		props=new Properties();
				URL defaultUrl=ServiceFactory.class.getClassLoader().getResource(CORE_CONFIGURATION_FILE_NAME);
				URL confUrl=ServiceFactory.class.getClassLoader().getResource(DEFAULT_CONFIGURATION_FILE_NAME);
				initEJBContainer(defaultUrl);
				initEJBContainer(confUrl);
				initFromXml(defaultUrl);
				try{
					initFromXml(confUrl);
				}catch(Exception e){
					logger.log(Level.WARNING,"", e);
					logger.log(Level.WARNING,DEFAULT_CONFIGURATION_FILE_NAME+"  does not exists using default configuration file" );
				}
			} catch (Throwable e) {
				CONFIGURATION_LOADED=false;
				System.err.println("Fatal Exception loading "+CORE_CONFIGURATION_FILE_NAME+" file ");
				e.printStackTrace();
				System.err.println("System will exist...");
			} 
		
		
	}
//*************************************************************************	
	private static void initEJBContainer(URL url) {
		try {
			SAXBuilder builder = new SAXBuilder();
			InputStream instr=url.openStream();
			Document doc = builder.build(instr);
			Element root= doc.getRootElement();
			List<?>children= root.getChildren();
			
			for(Object cur: children){
				if(!(cur instanceof Element)){
					throw new IllegalArgumentException("invalid argument "+cur+" in "+CORE_CONFIGURATION_FILE_NAME);
				}
				Element curElem= (Element)cur;
				String name= curElem.getName();
				if(EJB_CONTAINER_CONFIGUATION_TAG.equals(name)){
					for(Object conf: curElem.getChildren()){
						initEJBContainerConfiguration((Element)conf);
					}
				}	
			}
		} catch (Exception e){ 
			logger.log(Level.WARNING,"", e);
		}
		
	}
//*************************************************************************	
	private static void initFromXml(URL url)throws Exception{
		SAXBuilder builder = new SAXBuilder();
		InputStream instr=url.openStream();
		Document doc = builder.build(instr);
		Element root= doc.getRootElement();
		List<?>children= root.getChildren();
		
		for(Object cur: children){
			if(!(cur instanceof Element)){
				throw new IllegalArgumentException("invalid argument "+cur+" in "+CORE_CONFIGURATION_FILE_NAME);
			}
		Element curElem= (Element)cur;
		String name= curElem.getName();
		if(SECURITY_PROVIDER_TAG_NAME.equals(name)){
			initSecurityProvider(curElem);
		}
		else if(SYSTEM_TAG_NAME.equals(name)){
			initSystems(curElem);
		}else if(GLOBAL_BUNDEL_TAG_NAME.equals(name)){
			initGlobalGlossory(curElem.getTextNormalize());
		}else if(EXCEPTION_BUNDEL_TAG_NAME.equals(name)){
			if(curElem.getTextNormalize()!=null&&curElem.getTextNormalize().trim().length()>0)
				EXCEPTION_BUNDELS.add(curElem.getTextNormalize());
		}else if(DATABSE_CONSTRAINTS_TAG_NAME.equals(name)){
			if(curElem.getTextNormalize()!=null&&curElem.getTextNormalize().trim().length()>0)
				DATABSE_CONSTRAINTS_BUNDELS.add(curElem.getTextNormalize());
		}else if(DATABASE_COLUMN_BUNDEL_TAG_NAME.equals(name)){
			if(curElem.getTextNormalize()!=null&&curElem.getTextNormalize().trim().length()>0)
				DATABSE_COLUMNS_BUNDELS.add(curElem.getTextNormalize());
		}
		else if(PROCCESS_HANDLER.equals(name)){
			intiProcessHandler(curElem.getTextNormalize());
		}else if(SCHEDULE_TASK.equals(name)){
			initScheduleTasks(curElem);
		}else if(ORGANIZATION_SUPPORT.equals(name)){
			initOrganizationSupport(curElem);
		}else if(LANGUAGE_PATCH_TAG.equals(name)){
			initLanguagePatch(curElem);
		}else if(FISCAL_YEAR_SUPPORT.equals(name)){
			initFiscalYearSupport(curElem);
		}
		}
		instr.close();
		
		
	}
	/**
	 * security provider factory instance
	 */
	private static SecurityProvider securityProvider;
//*************************************************************************	
	/**
	 * 
	 * @return SecurityProvider Implemented class of application
	 */
	public static  synchronized SecurityProvider getSecurityProvider(){
		if(!CONFIGURATION_LOADED){
			throw new InvalidSystemConfigurationException();
		}
		return securityProvider;
	} 
//*************************************************************************	
	private static void intiProcessHandler(String processHandlerClass) {
		if(processHandler==null){
			try {
				processHandler=(ProcessHandler)ConstructorUtils.invokeConstructor(Class.forName(processHandlerClass), new Object[]{}) ;
			} catch (Exception e) {
				logger.log(Level.WARNING,"", e);
				logger.log(Level.WARNING,"System will exit now!!!!");
			}
		}
		
	}
//*************************************************************************	
	/**
	 * application server configuration
	 * @param curElem
	 */
	private static void initEJBContainerConfiguration(Element curElem){
		 if(ORB_INITIAL_HOST_TAG.equals(curElem.getName())){
			if(curElem.getTextNormalize()!=null&&curElem.getTextNormalize().trim().length()>0){
				props.setProperty("org.omg.CORBA.ORBInitialHost", curElem.getTextNormalize());

			}
		}else if(ORB_INITIAL_PORT_TAG.equals(curElem.getName())){
			if(curElem.getTextNormalize()!=null&&curElem.getTextNormalize().trim().length()>0){
			    props.setProperty("org.omg.CORBA.ORBInitialPort", curElem.getTextNormalize());
			}
		}else if(EJB_INITIAL_CONTEXT_PROPERTY_TAG.equals(curElem.getName())){
			String parameterName=curElem.getAttribute(EJB_INITIAL_CONTEXT_PROPERTY_ATTRIBUTE).getValue();
			String value=curElem.getTextNormalize();
			if(value!=null&&parameterName!=null&&parameterName.trim().length()>0&&value.trim().length()>0){
				props.setProperty(parameterName.trim(), value.trim());
			}
		}
	}
//*************************************************************************
	
	public static AfterLoginProcess[] getAfterLoginProcess(){
		return afterLoginProcess.toArray(new AfterLoginProcess[afterLoginProcess.size()]);
	}
//*************************************************************************	
	private static BasicCorePlugin loadClass(String classPath){
		if(classPath==null||classPath.trim().length()==0){
			System.err.println(" classPath is null ");
			return null;
		}
			 try {
                 Class<?> clazz = Class.forName(classPath);
                 BasicCorePlugin factory=(BasicCorePlugin)clazz.getConstructor(new Class[] {}).newInstance(new Object[] {});
                 return factory;
			} catch (Exception e) {
				System.err.println("fail to instantiate class "+classPath);
				e.printStackTrace();
			}
			return null;
	}
//*************************************************************************	
	/**
	 * initialize security provider base on configuration file
	 * @param curElem
	 */
	private static void initSecurityProvider(Element securityTag)throws Exception{
		Attribute classPath= securityTag.getAttribute(SECURITY_PROVIDER_CLASS_PATH);
		if(classPath!=null){
			SecurityProvider newSecurityProvider=(SecurityProvider)loadClass(classPath.getValue());
			if(newSecurityProvider==null&&securityProvider==null){
				throw new CoreException("fail to load manadatory plugin security provider");
			}else{
				securityProvider=newSecurityProvider; 
			}
		}
		
//		Element loginURL=securityTag.getChild(SECURITY_PROVIDER_LOGIN_URL);
		for(Object tag: securityTag.getChildren()){
			if(tag instanceof Element){
				Element e=(Element)tag;
				if(e.getName().equals(AFTER_LOGIN_PROCESS_CLASS_NAME)){
					afterLoginProcess.add((AfterLoginProcess)Class.forName(e.getTextNormalize()).newInstance());
				}
				else{
					securityProvider.setAttributeFromConfiguration(e.getName(), e.getTextNormalize());
				}
			}
		}
	}
//*************************************************************************
	/**
	 * 
	 * @return
	 */
	public static String getWelcomeURL(){
		if(!CONFIGURATION_LOADED){
			throw new InvalidSystemConfigurationException();
		}
		return WELCOME_URL;
	}
//*************************************************************************	
	/**
	 * load and initialize systems 
	 * @param systemTag
	 */
	
	private static void initSystems(Element systemTag){
		String name= systemTag.getAttributeValue("name");
		if(name==null||name.trim().length()==0){
			throw new IllegalArgumentException("invalid system configuration name :"+name);
		}
		if(SYSTEM_CACHE.containsKey(name)){
			throw new IllegalArgumentException("Duplicated system configuration  :"+name);
		}
		loadSystem(systemTag, name);
	}
//*************************************************************************	
	@SuppressWarnings("unchecked")
private static void loadSystem(Element systemTag, String name) {
	try {
		
		CmSystemFacadeRemote subSytem=
			(CmSystemFacadeRemote)lookupFacade(CmSystemFacadeRemote.class.getName());
		CmSystem system= subSytem.loadSystem(name);
		if(system==null){
			logger.log(Level.WARNING,"System not configured in configuration  xml -->"+name);
			return;
		}
		if(Constants.IsActive.disActive.equals(system.getIsactive())){
			logger.log(Level.ALL,"System "+name+" is not active ");
			return ;
		}
		ir.utopia.core.model.System  result=new ir.utopia.core.model.System();
		result.setAbbreviation(system.getAbbreviation());
		result.setName(system.getName());
		result.setSystemId(system.getCmSystemId());
		List<Element>elements =systemTag.getChildren();
		for(Element elem:elements){
			if(SUB_SYSTEM_TAG_NAME.equals(elem.getName())){
					String subSystemName=elem.getAttributeValue("name");
					if(subSystemName==null||subSystemName.trim().length()==0){
						throw new IllegalArgumentException("attribute name not defined for subsystem ");
					}
					for(CmSubsystem subsystem: system.getCmSubsystems()){
						if(subSystemName.equals(subsystem.getName())&&
							Constants.IsActive.active.equals(subsystem.getIsactive())){
							SubSystem newSubSystem=new SubSystem();
							newSubSystem.setName(subsystem.getName());
							newSubSystem.setAbbreviation(subsystem.getAbbreviation());
							newSubSystem.setSubSystemId(subsystem.getCmSubsystemId());
							Element e=elem.getChild(USECASE_BUNDLE_TAG_NAME);
							if(e!=null){
								newSubSystem.setUsecaseBundelName(e.getTextNormalize());
							} 
							result.addSubSystem(newSubSystem);
						}
					}
				}else if(MENU_BUNDLE_TAG_NAME.equals(elem.getName() )){
					result.setMenuBundelName(elem.getTextNormalize());
			}
		}
		SYSTEM_CACHE.put(result.getName(), result);
	} catch (NamingException e) {
		e.printStackTrace();
	}
}
//**********************************************************************************
	/**
	 * returns system and all subSystem configuration
	 */
	public static ir.utopia.core.model.System getSystemConfiguration(long systemId){
		if(!CONFIGURATION_LOADED){
			throw new InvalidSystemConfigurationException();
		}
		for(ir.utopia.core.model.System system: SYSTEM_CACHE.values()){
			if(system.getSystemId()==systemId){
				return system;
			}
		}
		logger.log(Level.ALL,"system with id "+systemId+" not defined in conf.xml ");
		return null;
	}
//**********************************************************************************	
	/**
	 * subSytem configuration base of configuration xml file   
	 * @param subSystemId
	 * @return
	 */
	public static SubSystem getSubsystemConfiguration(long subSystemId){
		if(!CONFIGURATION_LOADED){
			throw new InvalidSystemConfigurationException();
		}
		for(ir.utopia.core.model.System system: SYSTEM_CACHE.values()){
			for(SubSystem subSuyetem: system.getSubSystems()){
				if(subSuyetem.getSubSystemId()==subSystemId){
					return subSuyetem;
				}	
			}
			
		}
		logger.log(Level.ALL,"subSystem with id "+subSystemId+" not defined in configuration xml File");
		return null;
	}
//**********************************************************************************
	public static ProcessHandler getProcessHandler(){
		return processHandler;
	}
//**********************************************************************************
	public static void initGlobalGlossory(String glossory){
		GLOBAL_GLASSORY_LIST.add(glossory);
	} 
//**********************************************************************************
	public static  Set<String> getGlobalGlossory(){
		return GLOBAL_GLASSORY_LIST;
	}
//**********************************************************************************
	public static Set<String> getExceptionBundelPaths(){
		return EXCEPTION_BUNDELS;
	}
//**********************************************************************************
	public static Set<String> getDataBaseConstraintBundels(){
		return DATABSE_CONSTRAINTS_BUNDELS;
	}
//**********************************************************************************
	public static Set<String> getDataBaseColumnBundel(){
		return DATABSE_COLUMNS_BUNDELS;
	}
//**********************************************************************************
	public static LanguagePatch getLanguagePatch(String language){
		return languagePatchMap.get(language);
	}
//**********************************************************************************
	/**
	 * looking up facades base on configuration xml  
	 * @param remoteClass
	 * @return
	 * @throws Exception
	 */
	public static <T>T lookupFacade(Class<T> remoteClass)throws NamingException{
		return (T)lookupFacade(remoteClass.getName());
	}
//************************************************************************************	
	/**
	 * looking up facades base on configuration xml  
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static UtopiaBean lookupFacade(String  name)throws NamingException{
		props.put("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");		
		props.put("java.naming.factory.url.pkgs","com.sun.enterprise.naming");
		props.put("java.naming.factory.state", "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
		props.setProperty("org.omg.CORBA.ORBInitialHost", "localhost");
//	    props.setProperty("org.omg.CORBA.ORBInitialPort", "3700");
		InitialContext ctx=new InitialContext(props);
		UtopiaBean bean=(UtopiaBean)ctx.lookup(name);
		ctx.close();
		return bean;
	}
//*************************************************************************
	@SuppressWarnings("unchecked")
	private static void initScheduleTasks(Element curElem){
		String facade=curElem.getAttributeValue(SCHEDULE_TASK_FACADE);
		String startTime=curElem.getAttributeValue(SCHEDULE_TASK_START_TIME);
		String interval=curElem.getAttributeValue(SCHEDULE_TASK_INTERVAL);
		List<Element>children= curElem.getChildren();
		Properties props=new Properties();
		if(children!=null){
			for(Element param:children){
				if(SCHEDULE_TASK_PARAMETER.equals(param.getName()))
					props.put(param.getAttributeValue(SCHEDULE_TASK_PARAMETER_NAME), param.getTextNormalize());
			}
		}
		 try {
			ScheduledBeanFacadeRemote bean=(ScheduledBeanFacadeRemote) ServiceFactory.lookupFacade(facade);
			Date startDate=new SimpleDateFormat("yyyy-MM-dd").parse(startTime);
			bean.schedule(startDate, Long.parseLong(interval), new SchedulerModel(null,props,new HashMap<String, Object>()));
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
		}
	}
//*************************************************************************
	private static void initOrganizationSupport(Element curElem){
		try {
			supportsOrganization=Boolean.parseBoolean( curElem.getValue());
		} catch (Exception e) {
			logger.log(Level.WARNING, "fail to set Organization support property from configuration",e);
		}
	}
//*************************************************************************
	private static void initLanguagePatch(Element curElem){
		try {
			Element patchClassTage=curElem.getChild(PATCH_IMPL_TAG);
			Element languageTag=curElem.getChild(LANGUAGE_TAG);
			languagePatchMap.put(languageTag.getTextNormalize(), (LanguagePatch)Class.forName(patchClassTage.getTextNormalize()).newInstance());
		} catch (Exception e) {
			logger.log(Level.WARNING, "fail to set language patch ",e);
		}
	}
//*************************************************************************
	private static void initFiscalYearSupport(Element curElem){
		try {
			supportsFiscalYear=Boolean.parseBoolean( curElem.getValue());
		} catch (Exception e) {
			logger.log(Level.WARNING, "fail to set Organization support property from configuration",e);
		}
	}
//*************************************************************************
	public static boolean isSupportOrganization(){
		return supportsOrganization;
	}
//*************************************************************************
	public static boolean isSupportFiscalYear(){
		return supportsFiscalYear;
	}

}

