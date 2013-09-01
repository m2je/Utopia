package ir.utopia.core.customproperty.bean;

import ir.utopia.core.ContextHolder;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.bean.BeanUtil;
import ir.utopia.core.constants.Constants.predefindedActions;
import ir.utopia.core.customproperty.persistent.CoCustomProperty;
import ir.utopia.core.exception.DeleteRecordExeption;
import ir.utopia.core.exception.SaveRecordException;
import ir.utopia.core.persistent.UtopiaBasicPersistent;
import ir.utopia.core.persistent.lookup.model.LookupInfo;
import ir.utopia.core.usecase.UsecaseUtil;
import ir.utopia.core.usecase.actionmodel.UseCase;
import ir.utopia.core.usecase.usecase.persistence.CoUsecase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;
@Stateless
public class CoCustomPropertyFacade extends AbstractBasicUsecaseBean<CoCustomProperty, CoCustomProperty> implements CoCustomPropertyFacadeRemote{

	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(CoCustomPropertyFacade.class.getName());
	}
//*******************************************************************************************	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void handleProperties(UtopiaBasicPersistent persitentObject,
		List<List< String>> customProps, predefindedActions action) throws SaveRecordException{
		if(customProps==null||customProps.size()==0||persitentObject==null)return;
		try {
		Class<?>remoteClass= BeanUtil.findRemoteClassFromPersistent(persitentObject.getClass());
		UseCase usecase= UsecaseUtil.getUseCase(remoteClass.getName());
		LookupInfo usecaseInfo=new LookupInfo(CoUsecase.class,usecase.getUsecaseId());
		if(predefindedActions.save.equals(action)){
				handleNewProperties(persitentObject, customProps, 
						usecaseInfo);
			
		}else if(predefindedActions.update.equals(action)){
			if(customProps==null||customProps.size()==0){
				deleteCustomProperties(usecase.getUsecaseId(), persitentObject.getRecordId());
				
			}else{
				List<CoCustomProperty> currentProps=findByProperties(new String[]{"recordId","coUsecase.coUsecaseId"}, new Object[]{persitentObject.getRecordId(),usecase.getUsecaseId()},  null);
				List<CoCustomProperty>updatedProps=new ArrayList<CoCustomProperty>();
					for(CoCustomProperty prop:currentProps){
						List<String> updatedRow=null;
						for(List<String> row:customProps){
							if(row.get(0).equals(prop.getPropertyName())){
								updatedRow=row;
								prop.setPropertyValue(row.get(1));
								if(row.size()>2)
									prop.setExtendedProps1(row.get(2));
								if(row.size()>3)
									prop.setExtendedProps2(row.get(3));
								if(row.size()>4)
									prop.setExtendedProps3(row.get(4));
								prop.setUpdated(new Date());
								prop.setUpdatedby(ContextUtil.getUserId(ContextHolder.getContext().getContextMap()));
								updatedProps.add(prop);
								entityManager.merge(prop);
								break;
							}
						}
						if(updatedRow!=null)
							customProps.remove(updatedRow);
					}
					if(customProps.size()>0){
						handleNewProperties(persitentObject, customProps,  usecaseInfo);
					}
					currentProps.removeAll(updatedProps);
					if(currentProps.size()>0){
						deleteAll(currentProps.toArray(new CoCustomProperty[currentProps.size()]));
					}
			}
			
		}
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
			throw new SaveRecordException(e);
		}
	}
//****************************************************************************************	
private void handleNewProperties(UtopiaBasicPersistent persitentObject,
		List<List<String>> customProps, 
		LookupInfo usecaseInfo) throws SaveRecordException {
	List<LookupInfo>infos=new ArrayList<LookupInfo>();
	infos.add(usecaseInfo);
	List<CoCustomProperty>props=new ArrayList<CoCustomProperty>();
	for(List<String> row:customProps){
		if(row.size()==0||row.get(0)==null||row.get(0).trim().length()==0){
			logger.log(Level.INFO,"invalid custom property name:"+row);
			continue;
		}
		String value= row.get(1);
		if(value==null||value.trim().length()==0){
			logger.log(Level.INFO,"invalid custom property value:"+value+" for property:"+row);
			continue;
		}
		
		CoCustomProperty p=new CoCustomProperty();
		p.setLookupInfos(infos);
		p.setRecordId(persitentObject.getRecordId());
		p.setPropertyName(row.get(0));
		p.setPropertyValue(value.trim());
		if(row.size()>2)
			p.setExtendedProps1(row.get(2));
		if(row.size()>3)
			p.setExtendedProps2(row.get(3));
		if(row.size()>4)
			p.setExtendedProps3(row.get(4));
		
		props.add(p);
	}
	if(props.size()>0)
		super.saveAll(props.toArray(new CoCustomProperty[props.size()]),  false, null);
}
//*******************************************************************************************
	
//*******************************************************************************************
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void deleteCustomProperties(Long usecaseId, Long recordId
			) throws DeleteRecordExeption{
		if(usecaseId==null||usecaseId<=0){
			logger.log(Level.WARNING,"invalid usecaseId:"+usecaseId);
			return;
		}
		if(recordId==null||recordId<=0){
			logger.log(Level.WARNING,"invalid recordId:"+recordId);
			return;
		}
		
		Query query=entityManager.createNamedQuery(CoCustomProperty.DELETE_USECASE_CUSTOM_PROPERTIES);
		query.setParameter("recordId",recordId);
		query.setParameter("coUsecaseId", usecaseId);
		logger.info("Deleteing All attachment for usecase:"+usecaseId+" ,recordId:"+recordId);
		query.executeUpdate();
	}

}
