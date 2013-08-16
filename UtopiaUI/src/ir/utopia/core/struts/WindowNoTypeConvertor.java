package ir.utopia.core.struts;

import ir.utopia.core.ServiceFactory;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WindowNoTypeConvertor extends UtopiaTypeConverter {
private static final Logger logger;
	
	static {
		logger = Logger.getLogger(WindowNoTypeConvertor.class.getName());
	}
	@SuppressWarnings("unchecked")
	public Object convertFromString(Map ognlMap, String[] values, Class clazz) {
		if(int.class.equals(clazz)){
			if(values==null||values.length==0||values[0]==null||values[0].trim().length()==0){
				return 0;
			}
			try {
				try {
					return Integer.parseInt(values[0]);
				} catch (Exception e) {
					return Integer.parseInt(ServiceFactory.getSecurityProvider().decrypt(values[0]));
				}
				
			} catch (Exception e) {
				logger.log(Level.WARNING,"", e);
				return 0;
			}
		}
		return super.convertFromString(ognlMap, values, clazz);
	}
}
