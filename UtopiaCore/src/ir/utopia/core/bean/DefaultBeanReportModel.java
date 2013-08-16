package ir.utopia.core.bean;

import ir.utopia.core.constants.QueryComparsionType;
import ir.utopia.core.persistent.UtopiaBasicPersistent;
import ir.utopia.core.persistent.UtopiaPersistent;
import ir.utopia.core.persistent.lookup.model.LookupConfigurationModel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
public class DefaultBeanReportModel implements BeanReportModel {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(DefaultBeanReportModel.class.getName());
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 8283647120944022055L;

	Class<? extends UtopiaBasicPersistent>masterPersistent;
	EntityPair [] fieldsToLoad;
	SearchConditionItem [] conditions;
	EntityPair []orderby;
	String query,countQuery;
	Map<String,Object>parameterMap=new HashMap<String, Object>();
	List<LookupConfigurationModel>lookupsToLoad;
//********************************************************************************	
	public DefaultBeanReportModel(
			Class<? extends UtopiaPersistent> masterPersistent,
			EntityPair[] fieldsToLoad, SearchConditionItem[] conditions,
			EntityPair[] orderby) {
		super();
		this.masterPersistent = masterPersistent;
		this.fieldsToLoad = fieldsToLoad;
		this.conditions = conditions;
		this.orderby = orderby;
	}
//********************************************************************************	
	public DefaultBeanReportModel(
			Class<? extends UtopiaBasicPersistent> masterPersistent,EntityPair[] fieldsToLoad){
		this.masterPersistent=masterPersistent;
		this.fieldsToLoad = fieldsToLoad;
	}
//********************************************************************************
	public Class<? extends UtopiaBasicPersistent> getMasterPersistent() {
		return masterPersistent;
	}
//********************************************************************************
	public void setMasterPersistent(Class<? extends UtopiaBasicPersistent> masterPersistent) {
		this.masterPersistent = masterPersistent;
	}
//********************************************************************************
	public EntityPair[] getFieldsToLoad() {
		return fieldsToLoad;
	}
//********************************************************************************
	public void setFieldsToLoad(EntityPair[] fieldsToLoad) {
		this.fieldsToLoad = fieldsToLoad;
	}
//********************************************************************************
	public SearchConditionItem[] getConditions() {
		return conditions;
	}
//********************************************************************************
	public void setConditions(SearchConditionItem[] conditions) {
		this.conditions = conditions;
	}
//********************************************************************************
	public EntityPair[] getOrderby() {
		return orderby;
	}
//********************************************************************************
	public void setOrderby(EntityPair[] orderby) {
		this.orderby = orderby;
	}
//********************************************************************************
	@Override
	public Object getParameterValue(String parameter) {
		return parameterMap.get(parameter);
	}
//********************************************************************************
	@Override
	public String[] getQueryParameters() {
		createQuery();
		return parameterMap.keySet().toArray(new String[parameterMap.keySet().size()]);
	}
//********************************************************************************
	@Override
	public String getQueryString() {
		createQuery();
		return query;
	}
//********************************************************************************
	private void createQuery() {
		if(query==null){
			String masterName=masterPersistent.getSimpleName();
			
			StringBuffer queryBuffer=new StringBuffer(" select @fieldsToLoad@ from ").
			append(masterName).append(" ").append(masterName).append(" @joinItems@ ");
			 
			Set<EntityPair>fieldsToLoad=new HashSet<EntityPair>();
			String []fieldToLoad=getFieldsToLoad(fieldsToLoad,masterName);
			
			initConditions(queryBuffer, masterName, fieldsToLoad);
			
			StringBuffer orderByBuffer=new StringBuffer();
			if(orderby!=null){
				orderByBuffer.append(" order by ");
				boolean first=true;
				for(EntityPair order:orderby){
					if(!first){
						orderByBuffer.append(",");
					}
					first=false;
					orderByBuffer.append(masterName).append(".").append(order.getPropertyName());
				}
			}
			String joinToLoadBuff=getJoinClause(fieldsToLoad, masterName);
			query= queryBuffer.append(orderByBuffer).toString().
					replaceAll("@fieldsToLoad@", fieldToLoad[0]).replaceAll("@joinItems@", joinToLoadBuff.toString());
			countQuery= queryBuffer.toString().replaceAll("@fieldsToLoad@", fieldToLoad[1]).replaceAll("@joinItems@", joinToLoadBuff.toString());
		}
		
	}
//********************************************************************************
	@SuppressWarnings("unchecked")
	private String[] getFieldsToLoad(Set<EntityPair>detaildToLoad,String masterName){
		Set<Class<? extends UtopiaPersistent>>countP=new HashSet<Class<? extends UtopiaPersistent>>();
		StringBuffer fieldsToload=new StringBuffer();
		StringBuffer contFieldsToload=new StringBuffer();
		for(int i=0;i<fieldsToLoad.length;i++){
			if(i>0)
				fieldsToload.append(",");
			String persistentName=masterName;
			if(fieldsToLoad[i].getEntityClass()!=masterPersistent){
				detaildToLoad.add(fieldsToLoad[i]);
				persistentName=fieldsToLoad[i].getEntityClass().getSimpleName();
			}
			fieldsToload.append(persistentName).append(".").append(fieldsToLoad[i].getPropertyName());
			if(!countP.contains(fieldsToLoad[i].getEntityClass())&&!fieldsToLoad[i].getEntityClass().equals(masterPersistent)){
				if(countP.size()>0){
					contFieldsToload.append("+");	
				}
				countP.add((Class<? extends UtopiaPersistent>)fieldsToLoad[i].getEntityClass());
				contFieldsToload.append("count(").append(persistentName).append(".").append(fieldsToLoad[i].getPropertyName()).append(")");
			}
		}
		if(lookupsToLoad!=null){
			for(LookupConfigurationModel model:lookupsToLoad){
				if(fieldsToload.length()>0){
					fieldsToload.append(",");
				}
				fieldsToload.append(model.getDisplayColumnsCommaSeperated());
			}
		}
		if(countP.size()==0){
			contFieldsToload.append("count(").append(masterName).append(".").append(fieldsToLoad[0].getPropertyName()).append(")");
		}
		return new String[]{fieldsToload.toString(),contFieldsToload.toString()};
	} 
//********************************************************************************
	@SuppressWarnings("unchecked")
	private String getJoinClause(Set<EntityPair>detaildToLoad,String masterName){
		StringBuffer joinToLoadBuff=new StringBuffer();
		Set<Class<? extends UtopiaPersistent>>joinedP=new HashSet<Class<? extends UtopiaPersistent>>(); 
		for(EntityPair pair:detaildToLoad){
			Class<? extends UtopiaPersistent>currentp= (Class<? extends UtopiaPersistent>)pair.getEntityClass();
			if(!joinedP.contains(currentp)&&!masterPersistent.equals(currentp)){
				String detailPKname= BeanUtil.getPrimaryKeyColumn(currentp);
				joinedP.add(currentp);
				String detailParenkLinkColumn=BeanUtil.getPersistentLinkField(masterPersistent,currentp, detailPKname );
				joinToLoadBuff.append(" left join ").append( masterName).append(".").append(detailParenkLinkColumn).append(" ").
				append(currentp.getSimpleName());
			}
		
		}
		if(lookupsToLoad!=null)
			for(LookupConfigurationModel model:lookupsToLoad){
				if(!(model.getOwnPersitentClass()==masterPersistent||joinedP.contains(model.getOwnPersitentClass()))){
					String linkFiled=BeanUtil.getPersistentLinkField(masterPersistent, model.getOwnPersitentClass(), model.getPrimaryKeyColumn());
					String linkedPersistentName=masterName;
					if(linkFiled==null){
						
						for(Class<? extends UtopiaPersistent>link:joinedP){
							linkFiled=BeanUtil.getPersistentLinkField(link, model.getOwnPersitentClass(), model.getPrimaryKeyColumn());
							if(linkFiled!=null){
								linkedPersistentName=link.getSimpleName();
								break;
							}
						}
					}
					
					if(linkFiled!=null){
						joinToLoadBuff.append(" left join ").append( linkedPersistentName).append(".").append(linkFiled).append(" ").
						append(model.getOwnPersitentClass().getSimpleName());
					}else{
						logger.log(Level.WARNING,"could not locate lookup configuration for class: "+model.getOwnPersitentClass()+" and field :"+model.getPrimaryKeyColumn());
					}
				}
			
		    }
		return joinToLoadBuff.toString();
	}
//********************************************************************************
	private void initConditions(StringBuffer queryBuffer,String masterName,Set<EntityPair>detaildToLoad){
		if(conditions!=null&&conditions.length>0){
			queryBuffer.append(" where ");
			for(SearchConditionItem cond:conditions){
				int paramIndex=parameterMap.size();
				StringBuffer condition=new StringBuffer();
				String persistentName=masterName;
				if(cond.getEntityClass()!=masterPersistent){
					detaildToLoad.add(cond);
					persistentName=cond.getEntityClass().getSimpleName();
				}
				if(QueryComparsionType.in.equals(cond.getEqualityType())&&cond.getValue()!=null){
					Object []in=(Object [])cond.getValue();
					condition.append(persistentName).append(".").append(cond.getPropertyName()).
					append(cond.getEqualityType().getComparisonSign()).append("(");
					for(int k=0;k<in.length;k++){
						if(k>0){
							condition.append(",");
						}
					  condition.append(":P__").append(paramIndex).append(k);
					  parameterMap.put("P__"+paramIndex+k, in[k]);
					  
					}
					condition.append(")");
				}else{
					condition.append(persistentName).append(".").append(cond.getPropertyName()).
					append(cond.getEqualityType().getComparisonSign()).append(":P__").append(paramIndex);
					parameterMap.put("P__"+paramIndex, cond.getValue());
				}
				queryBuffer.append(condition);
				
			}
		}
	}
	@Override
	public String getCountQuery() {
		createQuery();
		return countQuery;
	}
//********************************************************************************
	public int getColumnIndex(EntityPair pair){
		if(fieldsToLoad!=null)
			 for(int i=0;i<fieldsToLoad.length;i++){
				 if(fieldsToLoad[i].equals(pair))return i;
			 }
		
		return -1;
	}
//********************************************************************************
	public int getColumnIndex(LookupConfigurationModel model){
		if(lookupsToLoad!=null&&lookupsToLoad.contains(model)){
			return (fieldsToLoad==null?0:fieldsToLoad.length)+lookupsToLoad.indexOf(model); 
		}
		return -1;
	}
//********************************************************************************
	public List<LookupConfigurationModel> getLookupsToLoad() {
		return lookupsToLoad;
	}
//********************************************************************************
	public void setLookupsToLoad(List<LookupConfigurationModel> lookupsToLoad) {
		this.lookupsToLoad = lookupsToLoad;
	}
	
	
}
