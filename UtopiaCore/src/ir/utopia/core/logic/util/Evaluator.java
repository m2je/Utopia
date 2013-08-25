package ir.utopia.core.logic.util;

import ir.utopia.core.logic.util.LogicParser.MapParameterHandler;

import java.math.BigDecimal;
import java.util.Map;
import java.util.logging.Logger;



 
/**
 * Expression Evaluator
 * 
 * @author 
 * @version $Id: Evaluator.java,v 1.2 
 */
public class Evaluator {
	
private static final Logger logger=Logger.getLogger(Evaluator.class.getName());
	
	
	public static final int LOGIC_EVALUATION_TYPE_BASIC=1;
	public static final int LOGIC_EVALUATION_TYPE_GROOVY=2;
	public static final int LOGIC_EVALUATION_TYPE_COMBINATIONAL=3;	
	
	public static boolean evaluateLogic(String logic,Map<String,Object>map) {
		if(logic != null){
			int logicType=getEvaluationType(logic);
			if(logicType==LOGIC_EVALUATION_TYPE_COMBINATIONAL){
				String []logics= LogicParser.splitCombinationalLogic(logic);
				MapParameterHandler paramHandler=new MapParameterHandler(map);
				for(String l:logics){
					if( getEvaluationType(l)==LOGIC_EVALUATION_TYPE_GROOVY){
						logic=logic.replace("{"+l+"}", String.valueOf(GroovyLogicEvaluator.evaluateLogic(l.substring("Groovy:".length()), map)));
					}else{
						logic=logic.replace("{"+l+"}", String.valueOf(ClassicEvaluator.evaluateLogic(l, paramHandler)));
					}
					return ClassicEvaluator.evaluateLogic(logic, paramHandler);
				}
			}else if(logicType==LOGIC_EVALUATION_TYPE_GROOVY){
				return GroovyLogicEvaluator.evaluateLogic(logic.substring("Groovy:".length()), map);
			}
			else{
				return ClassicEvaluator.evaluateLogic( logic,new MapParameterHandler(map));
			}			
		}
		return false;
	}
//*************************************************************************************************
//*************************************************************************************************
	public static int getEvaluationType(String logic){
		if(logic != null){
			if(logic.indexOf("{")>=0){
				return LOGIC_EVALUATION_TYPE_COMBINATIONAL;
			}
			if(logic.toLowerCase().startsWith("groovy:")){
				return LOGIC_EVALUATION_TYPE_GROOVY;
				}
		}
		return LOGIC_EVALUATION_TYPE_BASIC;
	}
//*************************************************************************************************
	public static String getFlatExpression(String expression,ParameterHandler source){
	     return LogicParser.getFlatExpression(expression,source);	
	}
//	*************************************************************************************************
	public static int getValueOfMathExpression(String expression){
		return getValueOfMathExpression(expression,null);
	}
//	*************************************************************************************************
	public static int getValueOfMathExpression(String expression,ParameterHandler source){
		if(source==null)return MathematicsEvaluator.getValueOfExpresion(expression);
		return  MathematicsEvaluator.getValueOfExpresion(getFlatExpression(expression,source));
		
	}
	public static BigDecimal getValueOfExpresionAsBigDecimal(String expression,ParameterHandler source){
		if(source==null)return MathematicsEvaluator.getValueOfExpresionAsBigDecimal(expression);
		return  MathematicsEvaluator.getValueOfExpresionAsBigDecimal(getFlatExpression(expression,source));
		
	}

} //	Evaluator
