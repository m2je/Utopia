package ir.utopia.common.doctype.util;


import ir.utopia.common.dashboard.bean.CmTransitionFacadeRemote;
import ir.utopia.common.doctype.persistent.CmDocStatus;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.logic.util.AnnotationUtil;
import ir.utopia.core.persistent.annotation.DocType;
import ir.utopia.core.usecase.UsecaseUtil;
import ir.utopia.core.usecase.actionmodel.UseCase;
import ir.utopia.core.util.Cache;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DocTypeUtil {
private static final Logger logger;
	private static final Class<?>[] classes= new Class<?>[]{};
	static {
		logger = Logger.getLogger(DocTypeUtil.class.getName());
	}
	
	public static final Cache<String,UsecaseDocInfo>USECASE_DOC_STATUS_COL_CACHE=new Cache<String, UsecaseDocInfo>();
	public static UsecaseDocInfo getUsecaseDocStatusInfo(Long usecaseId,Long fiscalYearId){
		String key=usecaseId+"|"+fiscalYearId;
		initUsecaseDocType(key,usecaseId,fiscalYearId);
		return USECASE_DOC_STATUS_COL_CACHE.get(key);
	}
//******************************************************************************************	
	@SuppressWarnings("unchecked")
	protected static void initUsecaseDocType(String key,Long usecaseId,Long fiscalyearId){
		if(!USECASE_DOC_STATUS_COL_CACHE.containsKey(key)){//USECASE_DOC_STATUS_COL_CACHE.clear();
			try {
				UseCase usecase= UsecaseUtil.getUsecase(usecaseId);
				Class<?> clazz=usecase.getPersistentClass();
				DocType docType=clazz.getAnnotation(DocType.class);
				String doctypeMethodName="getStatus";
				if(docType !=null){
					doctypeMethodName=docType.statusMethod();
				}
				Method method= findStatusMethod(clazz, doctypeMethodName);
				if(method!=null){
					String field= AnnotationUtil.getPropertyName(method.getName());
					UsecaseDocInfo info=new UsecaseDocInfo(field, method.getName(), ((Class<? extends Enum<?>>)method.getReturnType()));
					CmTransitionFacadeRemote transitionFacade=(CmTransitionFacadeRemote)ServiceFactory.lookupFacade(CmTransitionFacadeRemote.class);
					List<CmDocStatus>docStatUsecases= transitionFacade.getUsecaseDocStatues(usecaseId,fiscalyearId);
					List<UsecaseDocStatusInfo>statusesInfo=new ArrayList<UsecaseDocStatusInfo>();
					for(CmDocStatus status:docStatUsecases){
						UsecaseDocStatusInfo statusInfo=new UsecaseDocStatusInfo();
						statusInfo.setStatus(status.getStatus());
						statusInfo.setStatusId(status.getCmDocStatusId());
						statusesInfo.add(statusInfo);
					}
					info.setStatuses(statusesInfo);
					USECASE_DOC_STATUS_COL_CACHE.put(key, info);
				}else{
					USECASE_DOC_STATUS_COL_CACHE.put(key, null);
					logger.log(Level.WARNING,"could not find the status method from class "+clazz.getName());
				}
			} catch (Exception e) {
				logger.log(Level.WARNING,"",e);
				USECASE_DOC_STATUS_COL_CACHE.put(key, null);
			}
		}
		
	}
//******************************************************************************************
	protected static void initUsecaseDocType(String key,Class<?>clazz,Long fiscalyearId){
		
	}
//******************************************************************************************
	private static Method findStatusMethod(Class<?>clazz, String preferedName){
		try {
			return clazz.getMethod(preferedName, classes);
		} catch (Exception e) {
			if(logger.isLoggable(Level.FINE)){
			logger.log(Level.FINE,"fail to find docStatus method with name :"+preferedName,e);
			}
		}
		Method []methods= clazz.getMethods();
		for(Method currentMethod:methods){
				if(Enum.class.isAssignableFrom(currentMethod.getReturnType())&&currentMethod.getName().matches("[get][\\w]*[S|s]tatus[\\w]*")){
					return currentMethod;
				}
			}
			
		return null;
	}
//******************************************************************************************
	
}
