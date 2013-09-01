package ir.utopia.core.util;


import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PersistentConfigLoader {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(PersistentConfigLoader.class.getName());
	}
	private static final String CONFIG_FOLDER_NAME="utopiaConfig";
	private static final String ROOT_FOLDER=getRootFolder();
	private static final String CONFIG_ROOT_FOLDER=ROOT_FOLDER+File.separator+CONFIG_FOLDER_NAME;
	private static final HashMap<String, Properties>CONFIG_MAP=new HashMap<String, Properties>();
//*************************************************************************	
	private static String getRootFolder(){
		String cur=PersistentConfigLoader.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		while(cur!=null){
			  String parent= (new File(cur)).getParent();
			  if(parent==null){
				  return cur;
			  }
			  cur=parent;
			}
		return null;
	}	
//*************************************************************************
	public static String getConfigurationRootFolder(){
		return CONFIG_ROOT_FOLDER;
	}
//*************************************************************************
	public static Properties loadConfiguration(String configFile){
		if(configFile==null||configFile.trim().length()==0){
			throw new IllegalArgumentException("invalid configfile-->"+configFile);
		}
		if(!CONFIG_MAP.containsKey(configFile)){
			Properties props = new Properties();
			String confFullPath=CONFIG_ROOT_FOLDER+File.separator+configFile;
		    try {
		    	
		    	logger.info("loading utopia  configuration from -->"+confFullPath);
		    	props.load(new FileInputStream(confFullPath));
		    	logger.info(props.toString());
		    	CONFIG_MAP.put(configFile, props);
		    } catch (Exception e) {
		    	logger.log(Level.WARNING,"",e);
		    	logger.info("loading utopia  configuration from -->"+confFullPath+" failed");
		    	CONFIG_MAP.put(configFile,null);
		    }
		}
		return CONFIG_MAP.get(configFile);
	}
}
