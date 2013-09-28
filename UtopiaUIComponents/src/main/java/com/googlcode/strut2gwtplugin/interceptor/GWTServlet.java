/*
 * $Id$
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.googlcode.strut2gwtplugin.interceptor;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.servlet.ServletContext;

import org.apache.commons.beanutils.MethodUtils;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * This class is a modified version of GWT's RemoteServiceServlet.java
 */
class GWTServlet extends RemoteServiceServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3963593713648533636L;

	private static final HashMap<String, Method>cachedMethods=new HashMap<String, Method>();
	/** Context for the servlet */
    private ServletContext servletContext;
    
    
    /**
     * Find the invoked method on either the specified interface or any super.
     */
    private static Method findInterfaceMethod(Class<?> intf, String methodName,
        Class<?>[] paramTypes, boolean includeInherited) {
        try {
            return intf.getMethod(methodName, paramTypes);
        } catch(NoSuchMethodException e) {
            if(includeInherited) {
                Class<?>[] superintfs = intf.getInterfaces();
                for(int i = 0; i < superintfs.length; i++) {
                    Method method = findInterfaceMethod(superintfs[i],
                        methodName, paramTypes, true);
                    if(method != null) {
                        return method;
                    }
                }
            }

            return null;
        }
    }

    /**
     * The default constructor.
     */
    public GWTServlet() {
        super();
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.server.rpc.RemoteServiceServlet#processCall(java.lang.String)
     */
    public String processCall(String payload) throws SerializationException {
        
        // default return value - Should this be something else?
        // no known constants to use in this case
        String result = null; 
        
        // get the RPC Request from the request data
        RPCRequest rpcRequest= RPC.decodeRequest(payload);
        
        // get the parameter types for the method look-up
        Method method = findActionMethod(rpcRequest);
        
        // now make the call
        Object callResult = null;
        try {
            callResult = method.invoke(null/*actionInvocation.getAction()*/, 
                    rpcRequest.getParameters());
        } catch (IllegalAccessException iie) {
            // This may need to change this to package up up the cause
            // instead of the thrown exception, not sure if the 
            // chaining will translate
            result = RPC.encodeResponseForFailure(method, iie);
        } catch (InvocationTargetException ite) {
            // This may need to change this to package up up the cause
            // instead of the thrown exception, not sure if the 
            // chaining will translate
            result = RPC.encodeResponseForFailure(method, ite);
        }
        
        // package  up response for GWT
        result = RPC.encodeResponseForSuccess(method, callResult);
        
        // return our response
        return result;
    }

	private Method findActionMethod(RPCRequest rpcRequest) {
		Class<?> actionClass=null;//actionInvocation.getAction().getClass();
		StringBuffer keyBuffer=new StringBuffer(actionClass.getName()).append("|").append( rpcRequest.getMethod().getName());
		Class<?>[] paramTypes = new Class[rpcRequest.getParameters().length];
		Method method;
		for (int i=0; i < paramTypes.length; i++) {
        		Object []params=rpcRequest.getParameters();
        		if(params[i]!=null){
        			paramTypes[i] = params[i].getClass();
        			keyBuffer.append("|").append(params[i].getClass().getName());
				}else{
					paramTypes[i]=String.class;
					keyBuffer.append("|Null");
				}
        }
		String key=keyBuffer.toString();
        if(cachedMethods.containsKey(key)){
        	method=cachedMethods.get(key);
        }else{
        method=MethodUtils.getMatchingAccessibleMethod(actionClass, rpcRequest.getMethod().getName(), paramTypes);
        if(method==null){
        	method=findMethod(actionClass, rpcRequest.getMethod().getName(), paramTypes);
        	if(method==null){
	            // we need to get the action method from Struts
	            method = findInterfaceMethod(
	            		actionClass, 
	                   rpcRequest.getMethod().getName(), 
	                   paramTypes, true);
            }
        }
        cachedMethods.put(key.toString(), method);
        // if the method is null, this may be a hack attempt
        // or we have some other big problem
        if (method == null) {
            // present the params
            StringBuffer params = new StringBuffer();
            for (int i=0; i < paramTypes.length; i++) {
                params.append(paramTypes[i]);
                if (i < paramTypes.length-1) {
                    params.append(", ");
                }
            }
            
            // throw a security exception, could be attempted hack
            //
            // XXX - should we package up the failure like we do below? 
            //
            throw new SecurityException(
                    "Failed to locate method "+ rpcRequest.getMethod().getName()
                    + "("+ params +") on interface "
                    + actionClass.getName()
                    + " requested through interface "
                    + rpcRequest.getClass().getName());
        }
        }
		return method;
	}

    /**
     * Returns the servlet's context
     * 
     * @return a <code>ServletContext</code>
     */
    public ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * Sets the servlet's context
     * 
     * @param servletContext <code>ServletContext</code> to use
     */
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

        
    public Method findMethod(Class<?> clazz,String methodName,Object[] params){
    	Class<?>currentClass=clazz;
    	Method method=null;
    	while (true){
    		Method []methods= currentClass.getDeclaredMethods();
    		if(methods!=null){
    			for(Method cmethod:methods){
    				if(cmethod.getName().equals(methodName)&&cmethod.getParameterTypes().length==params.length){
    					method=cmethod;
    					break;
    				}
    			}
    		}
    		currentClass= currentClass.getSuperclass();
    		if(Object.class.equals(currentClass))break;
    	}
    	return method;
    	
    }
}
