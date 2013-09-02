package ir.utopia.core.util.logic;

import ir.utopia.core.ContextUtil;
import ir.utopia.core.logic.util.Evaluator;
import ir.utopia.core.logic.util.LogicParser;
import ir.utopia.core.util.DateUtil;
import ir.utopia.core.util.WebUtil;
import ir.utopia.core.util.tags.datainputform.client.model.DateInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class LogicEvaluator {

	public static Object evaluateDefaultValueLogic(String logic,Map<String,Object>context,Class<?>preferdType){
		if(logic!=null&&logic.trim().length()>0){
			Map<String,Object>formatedContext=new HashMap<String, Object>(context);
			Date currentDate=(Date)context.get(ContextUtil.CURRENT_DATE_CONTEXT_VARIABLE);	
				if(WebUtil.isUsingSolarDate(context)){
				 formatedContext=new HashMap<String, Object>(context);
				 formatedContext.put(ContextUtil.CURRENT_DATE_CONTEXT_VARIABLE, 
						 WebUtil.write(DateUtil.convertGregorianToSolarString(currentDate), Locale.US));
				}else{
					SimpleDateFormat format=new SimpleDateFormat(DateInfo.GERIGORIAN_DATE_FORMAT);
					formatedContext.put(ContextUtil.CURRENT_DATE_CONTEXT_VARIABLE, 
							format.format(currentDate));
				}
			DateFormat df=new SimpleDateFormat(DateInfo.TIME_FORMAT);
			formatedContext.put(ContextUtil.CURRENT_TIME_CONTEXT_VARIABLE, df.format(currentDate));
			
			String flatLogic= LogicParser.getFlatExpression(logic, formatedContext);
			if(preferdType!=null&&flatLogic!=null&&flatLogic.trim().length()>0){
				try {
					if(long.class.isAssignableFrom(preferdType)|| Long.class.isAssignableFrom(preferdType)){
						return Long.parseLong(flatLogic);
					}else if(boolean.class.isAssignableFrom(preferdType)||Boolean.class.isAssignableFrom(preferdType)){
						return Boolean.parseBoolean(flatLogic);
					}else if(String.class.isAssignableFrom(preferdType)){
						return flatLogic;
					}else if(Enum.class.isAssignableFrom(preferdType)){
						List<String>params=LogicParser.getJavaParameters(logic);
						if(params!=null&&params.size()>0){
							Object result= LogicParser.getJavaParameterValue(params.get(0), formatedContext);
							if(Enum.class.isInstance(result)){
								return ((Enum<?>)result).ordinal();
							}
						}
					}
				} catch (Exception e) {
					return null;
				}
				
			}
		}
		return null;
	}
	
	public static boolean evaluateReadonlyLogic(String logic ,Map<String,Object>context){
		if(logic!=null&&logic.trim().length()>0){
			return Evaluator.evaluateLogic(logic,context);
		}
		return false;
	}
	
	public static boolean evaluateDisplayLogic(String logic,Map<String,Object>context){
		if(logic!=null&&logic.trim().length()>0){
			return Evaluator.evaluateLogic(logic,context);
		}
		return true;
	}
	
	public static boolean evaluateLogic(String logic,Map<String,Object>context){
		if(logic!=null&&logic.trim().length()>0){
			return Evaluator.evaluateLogic(logic,context);
		}
		return true;
	}
}
