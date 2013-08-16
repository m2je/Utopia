package ir.utopia.core.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpSession;

public class SaveTokenController {
	private static String SESSION_TOKENS_KEY="__sessionToken__";

	
	public static String getToken(Map<String,Object>session,Long usecaseActionId){
		if(session==null)return null;
		@SuppressWarnings("unchecked")
		HashMap<Long,List<Token>>saveTokens=initHashMap((HashMap<Long,List<Token>>) session.get(SESSION_TOKENS_KEY),usecaseActionId);
		session.put(SESSION_TOKENS_KEY, saveTokens);
		return getNextToken(saveTokens, usecaseActionId);
	}
//****************************************************************************
	private static HashMap<Long,List<Token>> initHashMap(HashMap<Long,List<Token>>saveTokens,Long usecaseActionId){
		if(saveTokens==null){
			saveTokens=new HashMap<Long,List<Token>>();
		}
		List<Token>tokens=saveTokens.get(usecaseActionId);
		if(tokens==null){
			tokens=new ArrayList<Token>();
			saveTokens.put(usecaseActionId,tokens);
		}
		return saveTokens;
	}
//****************************************************************************
	private static String getNextToken(HashMap<Long,List<Token>>saveTokens,Long usecaseActionId){
		String newKey= "token_"+(new Random(System.currentTimeMillis())).nextLong();
		List<Token>tokens=saveTokens.get(usecaseActionId);
		Token token=new Token(newKey);
		tokens.add(token);
		return newKey;
	}
//****************************************************************************
	public static String getToken(HttpSession session,Long usecaseActionId){
		if(session==null)return null;
		@SuppressWarnings("unchecked")
		HashMap<Long,List<Token>>saveTokens=initHashMap((HashMap<Long,List<Token>>) session.getAttribute(SESSION_TOKENS_KEY),usecaseActionId);
		session.setAttribute(SESSION_TOKENS_KEY, saveTokens);
		return getNextToken(saveTokens, usecaseActionId);
	}
//****************************************************************************
	public static void consumeToken(String tokenKey,Map<String,Object>session,Long usecaseActionId){
		if(session==null || tokenKey==null)return ;
		@SuppressWarnings("unchecked")
		HashMap<Long,List<Token>>sessionTokens=initHashMap((HashMap<Long,List<Token>>) session.get(SESSION_TOKENS_KEY),usecaseActionId);
		Token token=findToken(tokenKey, sessionTokens,usecaseActionId);
		if(token!=null)
			token.setConsumed(true);
	}
//****************************************************************************
	public static void consumeToken(String tokenKey,HttpSession session,Long usecaseActionId){
		if(session==null||usecaseActionId==null||tokenKey==null)return ;
		@SuppressWarnings("unchecked")
		HashMap<Long,List<Token>>sessionTokens=(HashMap<Long,List<Token>>) session.getAttribute(SESSION_TOKENS_KEY);
		Token token=findToken(tokenKey, sessionTokens,usecaseActionId);
		if(token!=null)
			token.setConsumed(true);

	}
//****************************************************************************
	private static Token findToken(String tokenKey,HashMap<Long,List<Token>>sessionTokens,Long usecaseActionId){
		if(sessionTokens!=null){
			List<Token>tokens=sessionTokens.get(usecaseActionId);
			if(tokens!=null){
				for(Token token:tokens){
					if(tokenKey.equals(token.getToken())){
						return token;
					}
				}
			}
		}
		return null;
	}
//****************************************************************************
	@SuppressWarnings("unchecked")
	public static boolean isConsumed(String tokenKey,HttpSession session,Long usecaseActionId){
		HashMap<Long,List<Token>>sessionTokens=(HashMap<Long,List<Token>>) session.getAttribute(SESSION_TOKENS_KEY);
		Token token=findToken(tokenKey, sessionTokens,usecaseActionId);
		return token==null||token.isConsumed();
	}
//****************************************************************************
	@SuppressWarnings("unchecked")
	public static boolean isConsumed(String tokenKey,Map<String,Object> session,Long usecaseActionId){
		HashMap<Long,List<Token>>sessionTokens=(HashMap<Long,List<Token>>) session.get(SESSION_TOKENS_KEY);
		Token token=findToken(tokenKey, sessionTokens,usecaseActionId);
		return token==null||token.isConsumed();
	}
//****************************************************************************
	public static String getLastToken(HttpSession session,Long usecaseActionId){
		if(session.getAttribute(SESSION_TOKENS_KEY)!=null){
			@SuppressWarnings("unchecked")
			HashMap<Long,List<Token>>sessionTokens=(HashMap<Long,List<Token>>) session.getAttribute(SESSION_TOKENS_KEY);
			List<Token>tokens= sessionTokens.get(usecaseActionId);
			if(tokens!=null&&tokens.size()>0){
				return tokens.get(tokens.size()-1).getToken();
			}
		}
		return null;
	}
//****************************************************************************
		public static String getLastToken(Map<String,Object> session,Long usecaseActionId){
			if(session.get(SESSION_TOKENS_KEY)!=null){
				@SuppressWarnings("unchecked")
				HashMap<Long,List<Token>>sessionTokens=(HashMap<Long,List<Token>>) session.get(SESSION_TOKENS_KEY);
				List<Token>tokens= sessionTokens.get(usecaseActionId);
				if(tokens!=null&&tokens.size()>0){
					return tokens.get(tokens.size()-1).getToken();
				}
			}
			return null;
		}

}

