package ir.utopia.core.dispatch;

import ir.utopia.core.dispatch.model.ReSTMethod;
import ir.utopia.core.dispatch.model.ReSTMethod.HttpMethod;
import ir.utopia.core.dispatch.model.ReSTParam;
import ir.utopia.core.process.ExecutionResult;
import ir.utopia.core.util.Cache;
import ir.utopia.core.util.ClassUtility;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.uri.UriTemplateParser;

public class UtopiaAPIDispatcher implements Filter{
	Logger logger = Logger.getLogger(UtopiaAPIDispatcher.class.getSimpleName());
	private static final String PACKAGES_TO_SCAN="packages_to_scan";
	private String contextBase="api";
	private ObjectMapper mapper=new ObjectMapper();
	private static Object [] O=new Object [0];
	private static Cache<String, ReSTMethod>ReSTMethodCache=new Cache<String, ReSTMethod>();
	private static Cache<Class<?>,Object>beanCache=new Cache<Class<?>, Object>();
	
//***************************************************************************************************************************
	public void init(FilterConfig config){
		initContextBase(config);
		initPackagesToScan(config);
	}
//***************************************************************************************************************************	
	private void initPackagesToScan(FilterConfig config) {
		if(configExists(config,PACKAGES_TO_SCAN)){
			String []packages= config.getInitParameter(PACKAGES_TO_SCAN).split(",");
			scanPackages(packages);
			
		}
	}
//***************************************************************************************************************************
	private void initContextBase(FilterConfig config) {
		if(configExists(config, "context_base")){
			contextBase=config.getInitParameter("context_base").trim();
			if(contextBase.startsWith("/")){
				if(contextBase.length()>1){
					contextBase=contextBase.substring(1);
				}else{
					contextBase="";
				}
			}
			if(contextBase.endsWith("/")){
				if(contextBase.length()>1){
					contextBase=contextBase.substring(0,contextBase.length()-1);
				}else{
					contextBase="";
				}
			}
		}
	}
//*****************************************************************************************************************************************	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
			boolean success=true;
			Object result=null;
			try {
				String URI= ((HttpServletRequest) request).getRequestURI();
				String base=((HttpServletRequest) request).getContextPath()+"/"+contextBase;
				if(base.length()>1)
					URI=URI.replaceFirst(base,"");
				
				HttpMethod method=HttpMethod.getMethod(((HttpServletRequest) request).getMethod());
				String key=getMethodKey(URI, method);
				ReSTMethod restMethod=null;
				if(ReSTMethodCache.containsKey(key)){
					restMethod=ReSTMethodCache.get(key);
				}else {
					 key=getMethodKey(URI, HttpMethod.UNKNOWN);
					 if(ReSTMethodCache.containsKey(key)){
						 restMethod=ReSTMethodCache.get(key);
					 }else{
						 restMethod=findReSTMethod(URI,method);	
						}
				}
				if(restMethod!=null){
						result= invokeReSTMethod(((HttpServletRequest) request),(HttpServletResponse)response, restMethod);
						
				}
			} catch (Exception e) {
				success=false;
				logger.log(Level.WARNING,"",e);
			}
				
			if(success&&result!=null){
				response(request, response,result);
			}else{
				response(request,response,new ExecutionResult(success));
				}
			
	}
//*****************************************************************************************************************************************
	private ReSTMethod findReSTMethod(String URI,HttpMethod method){
		ReSTMethod alternate=null;
		for(String key:ReSTMethodCache.keySet()){
			String []k=key.split("[|]");
			if(k[0].matches(URI)||(k[0]+"/").matches(URI)){
				ReSTMethod cur=ReSTMethodCache.get(key);
				if(cur.getHttpMethod().equals(method)){
					return cur;
				}else if(HttpMethod.UNKNOWN.equals(cur.getHttpMethod())){
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
	private Object invokeReSTMethod(HttpServletRequest request,HttpServletResponse response,ReSTMethod rmethod)throws Exception{
		List<ReSTParam> params= rmethod.getParams();
		Object bean;
		Object result=null;
		Method method=rmethod.getMethod();
		if(!beanCache.containsKey(method.getDeclaringClass())){
			bean=getBeanInstance(method.getDeclaringClass());
			beanCache.put(method.getDeclaringClass(), bean);
		}else{
			bean=beanCache.get(method.getDeclaringClass());
		}
		if(bean!=null){
				if(params!=null&&params.size()>0){
					Object []paramValues=new Object[params.size()];
					for(int i=0;i<paramValues.length;i++){
						ReSTParam param=params.get(i);
						Class<?>clazz= param.getClazz();
						if(param.getPathVariableName()!=null){
							
						}else{
							if(HttpServletRequest.class.isAssignableFrom(clazz)){
								paramValues[i]=request;
							}else if(HttpServletResponse.class.isAssignableFrom(clazz)){
								paramValues[i]=response;
							}else{
								paramValues[i]=readRequestBody(request, clazz);
							}
						}
					}
					method.invoke(bean, paramValues);
				}else{
					method.invoke(bean, O);
				}
			
		}
		return result;
	}
//*****************************************************************************************************************************************
	private Object getBeanInstance(Class<?>clazz){
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
		return null;
	}
//*****************************************************************************************************************************************
	protected boolean configExists(FilterConfig config,String configName){
		return config.getInitParameter(configName)!=null&&
				config.getInitParameter(configName).trim().length()>0;
	}
//*****************************************************************************************************************************************
	protected void scanPackages(String []packages){
		HashSet<URL>packagesToScan=new HashSet<URL>();
		for(String packagz :packages){
			packagesToScan.addAll( ClasspathHelper.forPackage(packagz));
		}
		Reflections reflections = new Reflections(new ConfigurationBuilder()
        .setUrls(packagesToScan)
        .setScanners(new MethodAnnotationsScanner()));
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
							mapReSTMethod(method, baseURI, HttpMethod.UNKNOWN);
						}
					} catch (Exception e) {
						logger.log(Level.WARNING,"",e);
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
	
	Class<?>[]paramClasses= method.getParameterTypes();
	if(paramClasses!=null&&paramClasses.length>0){
		Annotation [][]annotations= method.getParameterAnnotations();
		
		List<String> parameternames=ClassUtility.getParameterNames(method);
		for(int i=0;i<paramClasses.length;i++){
			String parameterName=parameternames==null||parameternames.size()<i?"param"+i: parameternames.get(i);
			Class<?>clazz=paramClasses[i]; 
			ReSTParam restParam=new ReSTParam();
			restParam.setParamIndex(i);
			restParam.setClazz(clazz);
			if(isPrimitive(clazz)){
						Annotation []paramAnnotations=annotations[i];
						if(paramAnnotations!=null){
							for(Annotation ann: paramAnnotations){
								if(PathParam.class.isInstance(ann)){
									parameterName=((PathParam)ann).value();
									break;
								}
							}
						}
			if(paramMap!=null&& paramMap.size()>0){			
						if(parameterName!=null&&paramMap.containsKey(parameterName)){
							restParam.setPattern(paramMap.get(parameterName));
							restParam.setPathVariableName(parameterName);
						}
						
				}
			
		}
			restParam.setParamName(parameterName);
			ReSTMethodParams.add(restParam);
		}
	}
	return ReSTMethodParams;
}
//*****************************************************************************************************************************************
	protected  Object readRequestBody(HttpServletRequest request,Class<?> modelClass)throws Exception{
		InputStream is=request.getInputStream();
		try {
			Object model= mapper.readValue(is,modelClass);
			return model;
		} catch (Exception e) {
			if(logger.isLoggable(Level.FINEST)){
				logger.log(Level.FINEST, "fail to process request ",e);
			}
		}
		return null;
	}
//*****************************************************************************************************************************************
	protected boolean isPrimitive(Class<?> clazz){
		
		return clazz.isPrimitive()||String.class.isAssignableFrom(clazz)||
				Integer.class.isAssignableFrom(clazz)||Long.class.isAssignableFrom(clazz)||
				Double.class.isAssignableFrom(clazz)||Short.class.isAssignableFrom(clazz)||
				BigDecimal.class.isAssignableFrom(clazz)||Float.class.isAssignableFrom(clazz);
				
	}
//*****************************************************************************************************************************************
	private void response(ServletRequest request,ServletResponse response,Object result){
		try {
			mapper.writeValue(response.getOutputStream(), result);
			response.flushBuffer();
		} catch (Throwable e) {
			logger.log(Level.WARNING,"",e);
		}
	}
//*****************************************************************************************************************************************
	@Override
	public void destroy() {
		
	}
}

