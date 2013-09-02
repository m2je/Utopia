package ir.utopia.core.importer.model;

import ir.utopia.core.ContextUtil;
import ir.utopia.core.bean.BeanUtil;
import ir.utopia.core.bean.ImportDataProvider;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.importer.FieldSetup;
import ir.utopia.core.importer.FieldSetup.Types;
import ir.utopia.core.importer.ImportSetup;
import ir.utopia.core.messagehandler.MessageHandler;
import ir.utopia.core.persistent.UtopiaBasicPersistent;
import ir.utopia.core.persistent.UtopiaPersistent;
import ir.utopia.core.struts.FormAndPersistentConverter;
import ir.utopia.core.struts.UtopiaFormMetaData;
import ir.utopia.core.struts.UtopiaFormMethodMetaData;
import ir.utopia.core.util.DateUtil;
import ir.utopia.core.util.EnumUtil;
import ir.utopia.core.util.tags.process.client.model.ProcessExecutionResult;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public  class DefaultImportDataProvider implements ImportDataProvider {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(DefaultImportDataProvider.class.getName());
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -7274869159211040319L;

	ImportDataHandler dataHandler;
	ImportSetup importSetup;
	Long maxPk=null;
	public DefaultImportDataProvider(ImportDataHandler dataHandler,ImportSetup importSetup ){
		this.dataHandler=dataHandler;
		this.importSetup=importSetup;
	}
	@Override
	public UtopiaBasicPersistent[] getPersistents(int from, int to,Map<String,Object>context)throws Exception {
		try {
			from=(from==0&&importSetup.isFirstLineTitle())?from+1:from;
			Map<FieldSetup,Vector<Object>>fieldSetupValue=getFormatedValue(from, to,context, new ProcessExecutionResult());
			int size;
			try {
				size=fieldSetupValue.values().iterator().next().size();
			} catch (Exception e) {
				logger.log(Level.WARNING,"", e);
				return new UtopiaPersistent[0]; 
			}
			for(FieldSetup field:fieldSetupValue.keySet()){
				if(field.isLookup()){
					Vector<Object> value=fieldSetupValue.get(field);
					value=getLookupValue(importSetup, field, value, context);
					fieldSetupValue.put(field,value );
				}
			}
			UtopiaBasicPersistent []persistents=new UtopiaBasicPersistent[size];
			for(int i=0;i<persistents.length;i++){
				Map<UtopiaFormMethodMetaData,Object>valMap=new HashMap<UtopiaFormMethodMetaData,Object>();
				for(FieldSetup fieldSetup:fieldSetupValue.keySet()){
					Vector<Object>dataVector=fieldSetupValue.get(fieldSetup);
					Object value= dataVector.get(i);
					if(value==null)continue;
					if(Types.REFERENCE_PRIMARY_KEY.equals(fieldSetup.getType())){
						if(value instanceof Long){
							Long l=(Long)value;
							maxPk=(maxPk==null)?l:(maxPk.longValue()<l.longValue()?l:maxPk);
						}
					}
					valMap.put(fieldSetup,value);
				}
				persistents[i]=FormAndPersistentConverter.converFormToPersistent(importSetup, valMap, null,importSetup.getLocale()) ;
			}
			
			return persistents;
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
			throw e;
		}
		
	}
//*********************************************************************************************
	@Override
	public int getSize(Map<String,Object>context) throws Exception{
		return dataHandler.getRecordCount(context);
	}
//*********************************************************************************************
	public Map<FieldSetup,Vector<Object>> getFormatedValue(int from,int to,Map<String,Object>context,ProcessExecutionResult result)throws Exception{
		String language=ContextUtil.getLoginLanguage(context);
		Vector<Map<Integer, String>> values=dataHandler.fetchRecords( from, to,context);
		Map<FieldSetup,Vector<Object>>fieldSetupValue=new HashMap<FieldSetup,Vector<Object>>();
		if(values==null)return fieldSetupValue;
		
	for(FieldSetup col:importSetup.getFieldsSetup()){
		int index= col.getIndex();
		Vector<Object>colData=new Vector<Object>();
			if(col.isLookup()){
				if(index>0){
					if(col.isMandatory()){
						if(result!=null){
							String message= MessageHandler.getMessage("invalidConfiguration", 
									"ir.utopia.core.struts.ImportAction", language);
								message=message.replaceAll("@column@",importSetup.getHeader(col, language));
							result.addErrorMessage(message);
							result.setSuccess(false);
							continue;
						}
						
					}else{
						
						for(int i=0;i<values.size();i++){
							colData.add(null);
						}
						fieldSetupValue.put(col, colData);
						continue;
					}
				}
			}
			for(Map<Integer, String> row:values){
				colData=colData==null?new Vector<Object>():colData;
				colData.add(getRowValue(col, row, index, language, result));
			}
			
			if(col.hasDefaultValue()){
				Object value= col.getDefaultValue();
				value=(value instanceof String)?getRealFieldValue(col,(String)value):value;
				for(int i=0;i<colData.size();i++){
					if(colData.get(i)==null)
					   colData.set(i, value);
				}
			}
		
		fieldSetupValue.put(col, colData);
		}
	return fieldSetupValue;
	}
//********************************************************************************************************
	private Object getRowValue(FieldSetup col,Map<Integer, String> row,Integer index,String language,ProcessExecutionResult result){
		Object value=null;
		if(col.isLookup()){
			if(!row.containsKey(index)){
				if(result!=null){
					String message= MessageHandler.getMessage("invalidConfiguration", 
							"ir.utopia.core.struts.ImportAction", language);
						message=message.replaceAll("@column@",importSetup.getHeader(col, language));
					result.addErrorMessage(message);
					result.setSuccess(false);
					return null;
				}
				
			}else{
				value=row.get(index);
			}
		}
		else{
				value = getRealFieldValue(col, row);	
		}
		return value;
	}
//********************************************************************************************************
	private Object getRealFieldValue(FieldSetup fieldSetup, Map<Integer, String> values) {
		Integer index = fieldSetup.getIndex();
		if(index==null||index.intValue()==0)return null;
		return getRealFieldValue(fieldSetup, values.get(index));
	}

//********************************************************************************************************
	private Object getRealFieldValue(FieldSetup fieldSetup,String value){
		try {
			if(fieldSetup.getType().isNumberValue()){
				return  getNumericValue(fieldSetup.getReturnType(), value);
			}
			else if("active".equalsIgnoreCase(fieldSetup.getFieldName())||
					"isactive".equalsIgnoreCase(fieldSetup.getFieldName())){
				try {
					return Boolean.parseBoolean(value);
				} catch (Exception e) {
					return false;
				}
			}
			else if(fieldSetup.getType().isEnumValue()){
				int v = Integer.parseInt(value);
				return fieldSetup.getFieldClass().getEnumConstants()[v];
			}
			else if(fieldSetup.getType().isGerDateValue()){ //YYYY/MM/DD  such as 2008/5/22
				return (value!=null&&value.trim().length()>0)?DateUtil.gerToDate(value,"yyyy/MM/dd"):null;
			}
			else if(fieldSetup.getType().isSolarDateValue()){ //YYYY/MM/DD such as 1387/8/27
				return (value!=null&&value.trim().length()>0)?DateUtil.solarToDate(value):null;
			}
			else if(fieldSetup.getType().isTextValue()){
				return value;
			}
			return value;
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
		}
		return null;
	}
//********************************************************************************************************
		private Object getNumericValue(Class<?>clazz,String value){
			if(Integer.class.equals(clazz)){
				return new Integer(value);
			}else if(int.class.equals(clazz)){
				return Integer.parseInt(value);
			}
			else if(Long.class.equals(clazz)){
				return new Long(value);
			}else if(long.class.equals(clazz)){
				return Long.parseLong(value);
			}
			else if(Double.class.equals(clazz)){
				return new Double(value);
			}else if(double.class.equals(clazz)){
				return Double.parseDouble(value);
			}
			else if(Float.class.equals(clazz)){
				return new Float(value);
			}else if(float.class.equals(clazz)){
				return Float.parseFloat(value);
			}
			if(Byte.class.equals(clazz)){
				return new Byte(value);
			}else if(byte.class.equals(clazz)){
				return Byte.parseByte(value);
			}
			return new BigDecimal (value);
		}
//******************************************************************************************************
	private  Vector<Object> getLookupValue(UtopiaFormMetaData formMeta, FieldSetup setup,Vector<Object>datas,Map<String,Object>context){
		Vector<Object> result=new Vector<Object>();
		Object[] distenctValues=distinct(datas);
		Map<Object,Object>map=new HashMap<Object, Object>();
		for(Object obj: distenctValues){
			map.put(obj, getLookupValue(formMeta, setup, obj, context,null,null));
		}
		for(int i=0;i<datas.size();i++){
			result.add(map.get(datas.get(i)));
		}
		return result;
	}
//******************************************************************************************************
	public static Object[] distinct(Vector<Object>datas){
		HashSet<Object>distinctData=new HashSet<Object>();
		for(Object data:datas){
			distinctData.add(data);
		}
		return distinctData.toArray(new Object[distinctData.size()]);
	}
//******************************************************************************************************
	@SuppressWarnings("unchecked")
	public static Object getLookupValue(UtopiaFormMetaData formMeta,FieldSetup setup,Object value,Map<String,Object> context,ProcessExecutionResult result,String language){
			
			
			if(Constants.DisplayTypes.list.equals(setup.getDisplayType())){
				if(value==null)return null;
				if(Enum.class.isInstance(value))return (Enum)value;
				if(Integer.class.isInstance(value))return EnumUtil.getEnumValue((Class<? extends Enum>)setup.getReturnType(),(Integer)value);
				if(String.class.isInstance(value)){
					try {
						return  EnumUtil.getEnumValue((Class<? extends Enum>)setup.getReturnType(),Integer.parseInt(value.toString().trim()));
					} catch (Exception e) {
						return null;
					}
				}
				return null;
			}
			else{
				try {
					String propName = null;
					Class<? extends UtopiaPersistent> lookupClass=formMeta.getLookupPersistentClass(setup);
					if(setup.hasDefaultValue()){
						propName=BeanUtil.getPrimaryKeyColumn(lookupClass);
					}else{
							if(setup.getType().isCode())
								propName = UtopiaPersistent.CODE_FIELD_NAME;
							if(setup.getType().isName())
								propName = UtopiaPersistent.NAME_FIELD_NAME;
					}
					
					UtopiaBasicUsecaseBean<UtopiaPersistent, ?> remoteBean = (UtopiaBasicUsecaseBean<UtopiaPersistent, ?>)
					BeanUtil.lookupEntityFacade((Class<UtopiaPersistent>)lookupClass);
					List<UtopiaPersistent> p = remoteBean.findByProperty(propName, value);
					return p==null||p.size()==0?-1l: p.get(0).getRecordId();
				} catch (Exception e) {
					if(result!=null){
						String message=MessageHandler.getMessage("InvalidDataType", "ir.utopia.core.importer.model.ImportDataHandler", language);
						message= message.replace("@column@", formMeta.getHeader(setup, language));
						result.addErrorMessage(message);
					}
					logger.log(Level.WARNING, "",e);
					return null;
				}
			}
		
	}
	@Override
	public Long getMaximumPK() {
		return maxPk;
	}
	
public void clear(){
	dataHandler.clear();
	System.gc();
}
@Override
public String getCommand(Map<String,Object>context) {
	return dataHandler.getCommand(context);
}
}
