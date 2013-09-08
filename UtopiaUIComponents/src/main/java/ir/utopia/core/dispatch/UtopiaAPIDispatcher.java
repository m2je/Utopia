package ir.utopia.core.dispatch;

import ir.utopia.core.dispatch.model.ReSTMethod;
import ir.utopia.core.dispatch.model.ReSTMethod.HttpMethod;
import ir.utopia.core.dispatch.model.ReSTParam;
import ir.utopia.core.process.ExecutionResult;
import ir.utopia.core.util.Cache;
import ir.utopia.core.util.ClassUtility;
import ir.utopia.core.util.ClassUtility.BeanInfo;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.reflections.Reflections;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.uri.UriTemplateParser;

public class UtopiaAPIDispatcher {
	Logger logger = Logger.getLogger(UtopiaAPIDispatcher.class.getSimpleName());
	private static final String PACKAGES_TO_SCAN="packages_to_scan";
	private Class<?>modelClass ;
	private ObjectMapper mapper=new ObjectMapper();
	private static Object [] O=new Object [0];
	private static Cache<Class<?>,BeanInfo>beanInfoCache=new Cache<Class<?>, BeanInfo>();
	private static Cache<String, ReSTMethod>ReSTMethodCache=new Cache<String, ReSTMethod>();
	public void init(ServletConfig config){
		if(configExists(config,PACKAGES_TO_SCAN)){
			String []packages= config.getInitParameter(PACKAGES_TO_SCAN).split(",");
			scanPackages(packages);
		}
	}
//*****************************************************************************************************************************************	
	public void process(HttpServletRequest request,HttpServletResponse response){
		boolean success=false;

			String URI= request.getRequestURI();
			HttpMethod method=HttpMethod.getMethod(request.getMethod());
			String key=getMethodKey(URI, method);
			ReSTMethod restMethod=null;
			if(ReSTMethodCache.containsKey(key)){
				restMethod=ReSTMethodCache.get(key);
			}else {
				 key=getMethodKey(URI, HttpMethod.ALL);
				 if(ReSTMethodCache.containsKey(key)){
					 restMethod=ReSTMethodCache.get(key);
				 }else{
					 restMethod=findReSTMethod(URI,method);	
					}
			}

		
		response(new ExecutionResult(success),response);
	}
//*****************************************************************************************************************************************
	private ReSTMethod findReSTMethod(String URI,HttpMethod method){
		ReSTMethod alternate=null;
		for(String key:ReSTMethodCache.keySet()){
			String []k=key.split("|");
			if(k[0].matches(URI)){
				ReSTMethod cur=ReSTMethodCache.get(key);
				if(cur.getHttpMethod().equals(method)){
					return cur;
				}else if(HttpMethod.ALL.equals(cur.getHttpMethod())){
					alternate=cur;
				}
				
			}
		}
		if(alternate!=null){
			return alternate;
		}
		String []URISpilited=URI.split("//");
		return null;
	}
//*****************************************************************************************************************************************
	protected boolean configExists(ServletConfig config,String configName){
		return config.getInitParameter(configName)!=null&&
				config.getInitParameter(configName).trim().length()>0;
	}
//*****************************************************************************************************************************************
	private void response(Object result,HttpServletResponse response){
		try {
			mapper.writeValue(response.getOutputStream(), result);
		} catch (Throwable e) {
			logger.log(Level.WARNING,"",e);
		}
	}
//*****************************************************************************************************************************************
	protected void scanPackages(String []packages){
		
		for(String packagz :packages){
			Reflections reflections = new Reflections(packagz);
			Set<Method> pathMethods= reflections.getMethodsAnnotatedWith(Path.class);
			if(pathMethods!=null){
				for(Method method:pathMethods){
					try {
						String baseURI=null;
						Path basePath= method.getDeclaringClass().getAnnotation(Path.class);
						if(basePath!=null){
							baseURI=basePath.value();
						}
						
						boolean mapped=false;
						GET get=method.getAnnotation(GET.class);
						if(get!=null){
							mapReSTMethod(method, baseURI, HttpMethod.GET);
							mapped=true;
						}
						POST post =method.getAnnotation(POST.class);
						if(post!=null){
							mapReSTMethod(method, baseURI, HttpMethod.POST);
							mapped=true;
						}
						PUT put=method.getAnnotation(PUT.class);
						if(put!=null){
							mapReSTMethod(method, baseURI, HttpMethod.PUT);
							mapped=true;
						}
						DELETE delete=method.getAnnotation(DELETE.class);
						if(delete!=null){
							mapReSTMethod(method, baseURI, HttpMethod.DELETE);
							mapped=true;
						}
						if(!mapped){
							if(logger.isLoggable(Level.FINEST)){
								logger.log(Level.FINEST,"fail to find http method for method :["+method+"] with path:["+getMethodPath(baseURI, method.getAnnotation(Path.class))+"]");
							}
							mapReSTMethod(method, baseURI, HttpMethod.ALL);
						}
					} catch (Exception e) {
						logger.log(Level.WARNING,"",e);
					}
				}
			}
		}
	}
//*****************************************************************************************************************************************
	private String getMethodKey(String URI,HttpMethod httpMethod){
		return URI+"|"+httpMethod.name();
	}
//*****************************************************************************************************************************************
	private void mapReSTMethod(Method method, String baseURI,HttpMethod httpMethod){
		ReSTMethod rMethod=new ReSTMethod();
		rMethod.setHttpMethod(httpMethod);
		rMethod.setMethod(method);
		Path path= method.getAnnotation(Path.class);
		String methodURI=getMethodPath(baseURI, path);
		rMethod.setURI(methodURI);
		if(path!=null){
			List<ReSTParam> ReSTMethodParams = findMethodParams(method, path);
			rMethod.setParams(ReSTMethodParams);
		}
		ReSTMethodCache.put(getMethodKey(methodURI, httpMethod), rMethod);
	}
//*************************************************************************************************************
	private String getMethodPath(String baseURI,Path path){
		String methodURI=baseURI==null?"/":baseURI.trim();
		if(path!=null){
			String innerPath=path.value();
			if(methodURI.endsWith("/")){
				
				if(innerPath.startsWith("/")&&innerPath.length()>1){
					innerPath=innerPath.substring(1);
				}
			}else{
				if(!innerPath.startsWith("/")){
					innerPath="/"+innerPath;
				}
			}
			methodURI=methodURI+innerPath;
		}
		return methodURI;
		
	}
//*************************************************************************************************************
private List<ReSTParam> findMethodParams(Method method, Path path) {
	List<ReSTParam> ReSTMethodParams=new ArrayList<ReSTParam>();
	UriTemplateParser parser=new UriTemplateParser(path.value());
	Map<String,Pattern>paramMap= parser.getNameToPattern();
	if(paramMap!=null&& paramMap.size()>0){
		Annotation [][]annotations= method.getParameterAnnotations();
		Class<?>[]paramClasses= method.getParameterTypes();
		List<String> parameternames=ClassUtility.getParameterNames(method);
			for(int i=0;i<paramClasses.length;i++){
				String parameterName=parameternames==null||parameternames.size()<i?"param"+i: parameternames.get(i);
				Class<?>clazz=paramClasses[i]; 
				ReSTParam restParam=new ReSTParam();
				restParam.setParamIndex(i);
				restParam.setClazz(clazz);
				if(clazz.isPrimitive()||String.class.isAssignableFrom(clazz)){
							Annotation []paramAnnotations=annotations[i];
							if(paramAnnotations!=null){
								for(Annotation ann: paramAnnotations){
									if(PathParam.class.isInstance(ann)){
										parameterName=((PathParam)ann).value();
										break;
									}
								}
							}
							if(parameterName!=null&&paramMap.containsKey(parameterName)){
								restParam.setPattern(paramMap.get(parameterName));
								restParam.setPathVariableName(parameterName);
							}
							
					}
				restParam.setParamName(parameterName);
				ReSTMethodParams.add(restParam);
			}
			
	}
	return ReSTMethodParams;
}
//*****************************************************************************************************************************************
	protected  Object processRequest(HttpServletRequest request)throws Exception{
		InputStream is=request.getInputStream();
		try {
			Object model= mapper.readValue(is,modelClass);
		} catch (Exception e) {
			if(logger.isLoggable(Level.FINEST)){
				logger.log(Level.FINEST, "fail to process request ",e);
			}
		}
		return null;
	}
//*****************************************************************************************************************************************
}

