package ir.utopia.core.utilities.serialization;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import ir.utopia.core.persistent.UtopiaBasicPersistent;
import ir.utopia.core.revision.annotation.RevisionSupport;

public class JSONSerializationUtil {
	private static final Logger logger=Logger.getLogger(JSONSerializationUtil.class.getName());
	private static  ConcurrentHashMap<String,UtopiaSerializer>serializersMap=new ConcurrentHashMap<String,UtopiaSerializer>();
	
	public static synchronized String serialize(UtopiaBasicPersistent persistent){
		if(persistent==null)return null;
		String key=persistent.getClass().getName();
		if(!serializersMap.containsKey(key)){
			serializersMap.put(key, getSerializerInstance(persistent)) ;
		}
		UtopiaSerializer serialaizer= serializersMap.get(key);
		return serialaizer.serializedToJSON(persistent);
	}
	
	private static UtopiaSerializer getSerializerInstance(UtopiaBasicPersistent persistent){
		if(persistent!=null){
			try {
				RevisionSupport revision= persistent.getClass().getAnnotation(RevisionSupport.class);
				if(revision==null){
					return new DefaultUtopiaSerializer();
				}else{
						return revision.serializer().newInstance();
				}
				} catch (Exception e) {
					logger.log(Level.WARNING,"fail to create serializer for class "+persistent.getClass().getName(),e);
				}
		}
		
		return null;
	}
}
