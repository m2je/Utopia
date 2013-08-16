package ir.utopia.core.log.bean;

// default package

import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.bean.BeanUtil;
import ir.utopia.core.constants.Constants.IsActive;
import ir.utopia.core.exception.SaveRecordException;
import ir.utopia.core.log.persistent.CoCpLogConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;

/**
 * Facade for entity CoCpLogConfig.
 * 
 * @see .CoCpLogConfig
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CoCpLogConfigFacade extends AbstractBasicUsecaseBean<CoCpLogConfig, CoCpLogConfig> implements CoCpLogConfigFacadeRemote {

	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(CoCpLogConfigFacade.class.getName());
	}
	@Override
	public void save(List<CoCpLogConfig> configs,Map<String,Object>context) throws SaveRecordException{
		try {
			for(CoCpLogConfig config: configs){
				CoCpLogConfig conf=findConfig(config, context);
				if( conf==null){
					BeanUtil.initPersistentObject(entityManager, config, false, context);
					BeanUtil.initLookupInfos(entityManager, config);
					config.setIsactive(IsActive.active);
					entityManager.persist(config);
				}else if(conf.getIsactive().equals(IsActive.disActive)){
					BeanUtil.initPersistentObject(entityManager, config, true, context);
					BeanUtil.initLookupInfos(entityManager, config);
					config.setIsactive(IsActive.active);
					entityManager.merge(conf);
				}
			}
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
			throw new SaveRecordException(e);
		}
		
	}

	@Override
	public void delete(List<CoCpLogConfig> configs,Map<String,Object>context) throws SaveRecordException{
		try {
			for(CoCpLogConfig config: configs){
				CoCpLogConfig remove=findConfig(config, context);
				if( remove!=null){
					BeanUtil.initPersistentObject(entityManager, config, true, context);
					config.setIsactive(IsActive.disActive);
					entityManager.persist(config);
				}
			}
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
			throw new SaveRecordException(e);
		}
		
	
	}
	
	private CoCpLogConfig findConfig(CoCpLogConfig config,Map<String,Object>context){
		
		List<String>props=new ArrayList<String>();
		List<Object>values=new ArrayList<Object>();
		if(config.getCmSystem()!=null){
			props.add("cmSystem.cmSystemId");
			values.add(config.getCmSystem().getCmSystemId());
		}
		if(config.getCmSubsystem()!=null){
			props.add("cmSubsystem.cmSubsystemId");
			values.add(config.getCmSubsystem().getCmSubsystemId());
		}
		if(config.getCoUsecase()!=null){
			props.add("coUsecase.coUsecaseId");
			values.add(config.getCoUsecase().getCoUsecaseId());
		}
		if(config.getCoAction()!=null){
			props.add("coAction.coActionId");
			values.add(config.getCoAction().getCoActionId());
		}
		if(config.getCoRole()!=null){
			props.add("coRole.coRoleId");
			values.add(config.getCoRole().getCoRoleId());
		}
		if(config.getCoUser()!=null){
			props.add("coUser.coUserId");
			values.add(config.getCoUser().getCoUserId());
		}
		List<CoCpLogConfig>result= super.findByProperties(props.toArray(new String[props.size()]), values.toArray(), 
				 null);
		
		return (result!=null&&result.size()>0)?result.get(0):null;
	}

}