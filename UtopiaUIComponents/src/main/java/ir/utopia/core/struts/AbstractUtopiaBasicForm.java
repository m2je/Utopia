package ir.utopia.core.struts;

import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.MappedSuperForm;
import ir.utopia.core.logic.util.AnnotationUtil;
import ir.utopia.core.persistent.UtopiaBasicPersistent;
import ir.utopia.core.process.ExecutionResult;
import ir.utopia.core.security.exception.NotAuthenticatedException;
import ir.utopia.core.struts.FormAndPersistentConverter.MethodAndParams;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.Subject;


@MappedSuperForm
public abstract class AbstractUtopiaBasicForm<P extends UtopiaBasicPersistent> implements UtopiaBasicForm<P> {
	private static final Logger logger;
	private static final Object [] O=new Object[0];
	static {
		logger = Logger.getLogger(AbstractUtopiaBasicForm.class.getName());
	}
	private int windowNo;
	private static final Class<?> []c=new Class<?>[]{};
	private static final Object []o=new Object[]{};
	private String locale; 
	private boolean updateable=true;
	private boolean deleteable=true;
	protected Subject subject;
	protected File[] attachments;
	protected String[] attachmentNames;
	protected String[] removedAttachmentIds; 
	protected ExecutionResult executionResult;
	protected String saveToken;
	protected String revisionDescription;
	protected boolean deleted=false;
//*******************************************************************************************
	public Long getRecordId(){
		String idMethodName= getMetaData().getIdMethodName(); 
		try {
			Object result= getClass().getMethod(idMethodName, c).invoke(this, o);
			if(result==null)return null;
			if(Long.class.isInstance(result)){
				return ((Long)result).longValue();
			}
		} catch (Exception e) {
			logger.log(Level.ALL,"fail to invoke method "+idMethodName,  e);
		}
		return -1l;
	}
//*******************************************************************************************
	public void setRecordId(long recordId) {
		String idMethodName= getMetaData().getIdMethodName(); 
		idMethodName=AnnotationUtil.getSetterMethodName(idMethodName);
		try {
			getClass().getMethod(idMethodName, long.class).invoke(this, recordId);
		} catch (Exception e) {
			logger.log(Level.ALL,"fail to invoke method "+idMethodName,  e);
		}
	}

//*******************************************************************************************
	public void setLocale(String locale){
		this.locale=locale;
	}
//*******************************************************************************************

	public String getLocale(){
		if(locale==null){
			try {
				Subject subject =getSubject();
				if(subject==null){
					locale=Locale.getDefault().getLanguage();
					return locale;
					}
				locale=ServiceFactory.getSecurityProvider().getLocaleOf(subject).getLanguage();
			} catch (NotAuthenticatedException e) {
				logger.log(Level.WARNING,"could not find locale of subject :"+subject );
			}
	}
			return locale;
		
	}
//******************************************************************************************
	protected Subject getSubject(){
		if(subject==null){
				 subject=ContextUtil.getUser();
		}
		 return subject;
	} 
//*******************************************************************************************
	public UtopiaBasicPersistent convertToPersistent() {
		try {
			UtopiaFormMetaData metaData= getMetaData();
			UtopiaFormMethodMetaData []metas= metaData.getMethodMetaData();
			Map<UtopiaFormMethodMetaData,Object>valueModel =new HashMap<UtopiaFormMethodMetaData, Object>();
			for(UtopiaFormMethodMetaData meta:metas){
				 if(meta.isPersistentMethod()){
					 MethodAndParams method=FormAndPersistentConverter.findMethod(this.getClass(), meta.getMethodName(), null);
					 Object value=method.method.invoke(this, o);
					 valueModel.put(meta, value);
				 }
			}
			
			
			return  FormAndPersistentConverter.converFormToPersistent(metaData, valueModel, getAttachments(),getAttachmentNames(),getRemovedAttachmentIds(),getRecordId(), locale);
		} catch (Exception e) {
			logger.log(Level.ALL," unable to instantiate or initialize class "+getClass().getName()+" with default constructor ",  e); 
		}
		return null;
	}

//*******************************************************************************************
	public void importPersistent(UtopiaBasicPersistent persistent) {
			UtopiaFormMetaData meta=getMetaData();
			UtopiaFormMethodMetaData []metaDatas=meta.getMethodMetaData();
				for(UtopiaFormMethodMetaData methodMeta: metaDatas){
					if(methodMeta.getPersistentMethodName()!=null){
							Object invokationResult=FormAndPersistentConverter.invokeObjectMethod(persistent,methodMeta.getPersistentMethodName(),null,null,getLocale());
							String methodName=AnnotationUtil.getSetterMethodName(methodMeta.getMethodName());
							Object []result=FormAndPersistentConverter.getFormToPersistentMethodParamClass( methodMeta, invokationResult,locale);
							if(result!=null&&result[0]!=null)
								FormAndPersistentConverter.invokeObjectMethod(this,methodName,result[0] ,(Class<?>)result[1],getLocale());
					}
					}
				}
//*******************************************************************************************	
	@SuppressWarnings("unchecked")
	public UtopiaFormMetaData getMetaData(){
		return FormUtil.getMetaData((Class<? extends UtopiaBasicForm<?>>)getClass());
	}

//**************************************************************************************************
		public UtopiaFormMethodMetaData getMethodMetaData(String fieldName){
			return getMetaData().getMethodMetaData(fieldName);
		}
//**************************************************************************************************
		@Override
		public boolean isDeleteable() {
			return deleteable;
		}
//**************************************************************************************************
		@Override
		public boolean isUpdateable() {
			return updateable;
		}
//**************************************************************************************************
		public void setUpdateable(boolean updateable){
			this.updateable=updateable;
		}
//**************************************************************************************************
		public void setDeleteable(boolean deleteable){
			this.deleteable=deleteable;
		}
//**************************************************************************************************	
		@Override
		public int getWindowNo() {
			return windowNo;
		}
//**************************************************************************************************
		@Override
		public void setWindowNo(int windowNo) {
			this.windowNo=windowNo;
		}
//**************************************************************************************************
		@Override
		public File[] getAttachments() {
			return attachments;
		}
//**************************************************************************************************
		@Override
		public void setAttachments(File[] attachments) {
			this.attachments=attachments;
		}
//**************************************************************************************************
		public String[] getAttachmentNames() {
			return attachmentNames;
		}
//**************************************************************************************************
		public void setAttachmentNames(String[] attachmentNames) {
			this.attachmentNames = attachmentNames;
		}
//**************************************************************************************************	
		public String[] getRemovedAttachmentIds() {
			return removedAttachmentIds;
		}
//**************************************************************************************************		
		public void setRemovedAttachmentIds(String[] removedAttachmentIds) {
			this.removedAttachmentIds = removedAttachmentIds;
		}
//**************************************************************************************************
		public ExecutionResult getExecutionResult() {
			return executionResult;
		}
//**************************************************************************************************
		public void setExecutionResult(ExecutionResult executionResult) {
			this.executionResult = executionResult;
		}
//**************************************************************************************************
		public String getSaveToken() {
			return saveToken;
		}
//**************************************************************************************************
		public void setSaveToken(String saveToken) {
			this.saveToken = saveToken;
		}
//**************************************************************************************************
		@Override
		public void setRevisionDescription(String revisionDescription) {
			this.revisionDescription=revisionDescription;
			
		}
//**************************************************************************************************
		@Override
		@FormPersistentAttribute
		public String getRevisionDescription() {
			return revisionDescription;
		}
//**************************************************************************************************
		public boolean isDeleted() {
			return deleted;
		}
//**************************************************************************************************
		public void setDeleted(boolean deleted) {
			this.deleted = deleted;
		}
//**************************************************************************************************
		
		
}
