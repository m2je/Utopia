package ir.utopia.core.struts;

import ir.utopia.core.constants.Constants;
import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.constants.Constants.predefindedActions;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.InputItem.InputItemLogicOnAction;
import ir.utopia.core.form.annotation.LinkPage;
import ir.utopia.core.form.annotation.NativeEvent;
import ir.utopia.core.form.annotation.NativeEventType;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.logic.util.AnnotationUtil;
import ir.utopia.core.persistent.annotation.LOVConfiguration;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

public class UtopiaFormMethodMetaData implements Serializable {
		
		/**
	 * 
	 */
	private static final long serialVersionUID = 3092469180223601895L;
		private String methodName;
		private Map<String,Annotation>annotMap=new HashMap<String, Annotation>();
		private Class<?> returnType;
		private Boolean includedLoadMethod=false;
		private Class<?> persistentClass;
		private String persistentMetodName;
		private DisplayTypes displayType; 
		private FormPersistentAttribute.FormToEntityDataTypeMap formToEntityMapType;
		private FormPersistentAttribute.FormToEntityMapType mapType;
		private UtopiaPageLinkMetaData link;
		private Map<Constants.predefindedActions, List<NativeConfiguration>> nativeConfigurationMap=new HashMap<Constants.predefindedActions, List<NativeConfiguration>>();
		private Map<Constants.predefindedActions, LOVMetaData> LOVConfigurationMap=new HashMap<Constants.predefindedActions, UtopiaFormMethodMetaData.LOVMetaData>();
		private int preferedWidth;
		int index=-1;
//**********************************************************************************************	
		public List<NativeConfiguration> getNativeConfigurations(predefindedActions action) {
		List<NativeConfiguration>result=new ArrayList<UtopiaFormMethodMetaData.NativeConfiguration>();
		if(action==null){
			List<NativeConfiguration> saveNatives=nativeConfigurationMap.get(predefindedActions.save);
			if(saveNatives!=null){
				result.addAll(saveNatives);
			}
			List<NativeConfiguration> searchNatives=nativeConfigurationMap.get(predefindedActions.search);
			if(searchNatives!=null){
				result.addAll(searchNatives);
			}
		}else{
			if(action==predefindedActions.update||action==predefindedActions.updateFromDashboard){
				action=predefindedActions.save;
			}
			result=nativeConfigurationMap.get(action);
		}
		
			return result;
		}
//**********************************************************************************************		
@XmlTransient
public Boolean isIncludedLoadMethod() {
			return includedLoadMethod;
		}
//**********************************************************************************************	
	public void setIncludedLoadMethod(Boolean includedLoadMethod) {
		this.includedLoadMethod = includedLoadMethod;
	}
//**********************************************************************************************	
	public UtopiaFormMethodMetaData(String methodName, Annotation[] annotations,Class<?>returnType) {
			super();
			this.methodName = methodName;
			setAnnotations(annotations);
			this.returnType=returnType;
			initMeta();
		}
//**********************************************************************************************
	@XmlTransient
	public String getMethodName() {
			return methodName;
		}
//**********************************************************************************************
	public void setMethodName(String methodName) {
			this.methodName = methodName;
		}
//**********************************************************************************************
	@XmlTransient
	public Annotation[] getAnnotations() {
		return annotMap.values().toArray(new Annotation[annotMap.size()]);
		}
//**********************************************************************************************
	public void setAnnotations(Annotation[] annotations) {
			for(Annotation annot:annotations){
				annotMap.put(annot.annotationType().getName(), annot);		
			}
		}
//**********************************************************************************************
	public String getFieldName(){
			return AnnotationUtil.getPropertyName(methodName);
		}
//**********************************************************************************************
	public Annotation getAnnotation(Class<?> annotationClass){
			return annotMap.get(annotationClass.getName());
		}
//**********************************************************************************************	
	@XmlTransient
	public String getPersistentMethodName(){
		return persistentMetodName;
	
	}
//**********************************************************************************************
	private void initMeta(){
		initPersistentAttributes();
		initAsInputItem();
		initAsSearchItem();
		initLinks();
		setDisplayTypeFromPersitentMapping();
	}
//**********************************************************************************************
	private void initPersistentAttributes(){
		FormPersistentAttribute annot=(FormPersistentAttribute) getAnnotation(FormPersistentAttribute.class);
		if(annot==null)
			{
			persistentMetodName= null;
			formToEntityMapType=null;
			mapType=null;
			}
		else{
			formToEntityMapType= annot.formToEntityMap();
			mapType=annot.formToEntityMapType();
			persistentMetodName=annot.method();
			persistentMetodName=persistentMetodName==null||persistentMetodName.trim().length()==0?methodName:persistentMetodName;
			
			
			
		}
	}
//**********************************************************************************************
	private void setDisplayTypeFromPersitentMapping(){
		if(displayType==null){
			if(formToEntityMapType!=null){
				switch (formToEntityMapType) {
				case LONG_TO_LOOKUP:
					displayType=DisplayTypes.lookup;
					break;
				
				
				case BOOLEAN_TO_ENUM:
				case ISACTIVE:{
					displayType=DisplayTypes.checkBox;
					break;
				}
				case STRING_TO_DATE:
				case STRING_TO_DATE_TIME:{
					displayType=DisplayTypes.Date;
					break;
				}
				default:
					displayType=DisplayTypes.String;
					break;
				}
			}
		}
	}
//**********************************************************************************************
	private void initAsInputItem(){
		 InputItem item=(InputItem) getAnnotation(InputItem.class);
		 displayType=item==null?null:item.displayType();
		 if(item!=null){
			 NativeEvent[] events= item.nativeEvents();
			 initNativeConfiguration(predefindedActions.save, events);
			 initLOVMetaData(predefindedActions.save,item.LOVconfiguration());
			 setPreferedWidth( item.preferedWidth());
		 }
	}
//**********************************************************************************************
	private void initAsSearchItem(){
		SearchItem item=(SearchItem)getAnnotation(SearchItem.class);
		if(item!=null){
			NativeEvent[] events=item.nativeEvents();
			initNativeConfiguration(predefindedActions.search, events);
			initLOVMetaData(predefindedActions.search,item.LOVconfiguration());
		}
	}
//**********************************************************************************************
	private void initNativeConfiguration(predefindedActions action,NativeEvent[] events){
		if(events!=null&&events.length>0){
			 List<NativeConfiguration>nativeConfigurations=new ArrayList<UtopiaFormMethodMetaData.NativeConfiguration>();
			 for(NativeEvent event:events){
				 NativeConfiguration nativeConf=new NativeConfiguration();
				 nativeConf.setEventType(event.eventType());
				 nativeConf.setFunctionName(event.function());
				 nativeConfigurations.add(nativeConf);
			 }
			 nativeConfigurationMap.put(action, nativeConfigurations);
		 }
	}
//**********************************************************************************************
	private void initLOVMetaData(predefindedActions action,LOVConfiguration configuration){
		if(configuration!=null&&configuration.searchLOVForm()!=null&&!configuration.searchLOVForm().equals(LOVConfiguration.DummyLOVFormClass.class)){
			LOVMetaData LOVMetaData=new LOVMetaData();
			LOVMetaData.setFromClass(configuration.searchLOVForm());
			LOVMetaData.setConditions(configuration.conditions());
			LOVMetaData.setMultiSelect(configuration.isMultiSelect());
			LOVMetaData.setDisplayItemSeperator(configuration.boxDisplaySeperator());
			LOVMetaData.setBoxSelectingColumns(configuration.boxDisplayingColumns());
			LOVConfigurationMap.put(action, LOVMetaData);
		}
	}
//**********************************************************************************************
	private void initLinks(){
		LinkPage link=(LinkPage) getAnnotation(LinkPage.class);
		if(link!=null){
			this.link=new UtopiaPageLinkMetaData();
			this.link.setActionName(link.actionName());
			this.link.setInInternal(link.isInternal());
			this.link.setLinkedURL(link.linkedUrl());
			this.link.setPerRecord(link.perRecord());
			this.link.setLinkToPages(link.linkToPages());
			this.link.setIcon(link.icon());
		}
	}
//**********************************************************************************************
	@XmlTransient
	public Boolean isPersistentMethod(){
		return persistentMetodName!=null&&FormPersistentAttribute.FormToEntityMapType.real.equals(mapType);
	}
//**********************************************************************************************
	@XmlTransient
	public String getPersistentFieldName(){
		if(isPersistentMethod()){
			return AnnotationUtil.getPropertyName(persistentMetodName);
		}
		return null;
	}
//**********************************************************************************************
	@XmlTransient
	public Class<?> getReturnType() {
		return returnType;
	}
//*********************************************************************************************
	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}
//**********************************************************************************************
	@XmlTransient
	public int getInputFormIndex(){
		if(index<0){
			InputItem item=(InputItem) getAnnotation(InputItem.class);
			if(item!=null){
				index= item.index();
			}else{
				index= 0;
			}
		}
		return index;
	}
//**********************************************************************************************
	@XmlTransient
	public int getSearchIndex(){
		SearchItem item=(SearchItem)getAnnotation(SearchItem.class);
		if(item!=null){
			return item.searchOnIndex();
		}
		return 0;
	}
//**********************************************************************************************
	@XmlTransient
	public int getSearchGridIndex(){
		SearchItem item=(SearchItem)getAnnotation(SearchItem.class);
		if(item!=null){
			return item.searchGridIndex();
		}
		return 0;
	}
//**********************************************************************************************
	@XmlTransient
	public int getInputTypeDisplayType(){
		if(Constants.DisplayTypes.Hidden.equals(displayType)){
			return Constants.DisplayTypes.Hidden.ordinal();
		}
		if( !Constants.DisplayTypes.lookup.equals(displayType)&& isNumeric() &&!Constants.DisplayTypes.LOV.equals(displayType)){
			return Constants.DisplayTypes.Integer.ordinal();
		}
		if(displayType!=null){
			return displayType.ordinal();
		}
		return DisplayTypes.String.ordinal();
	}
//**********************************************************************************************
	@XmlTransient
	public DisplayTypes getDisplayType(){
		 return displayType;
	}
//**********************************************************************************************
	private Boolean isNumeric(){
		if(Integer.class.isAssignableFrom(returnType)||int.class.isAssignableFrom(returnType)
		  ||Long.class.isAssignableFrom(returnType)||long.class.isAssignableFrom(returnType)	
		  ||Float.class.isAssignableFrom(returnType)||float.class.isAssignableFrom(returnType)
		  ||BigDecimal.class.isAssignableFrom(returnType)||BigDecimal.class.isAssignableFrom(returnType)
		  ||Double.class.isAssignableFrom(returnType)||double.class.isAssignableFrom(returnType)
		  ){
			return true;
		}
		return false;
	}
//**********************************************************************************************
	@XmlTransient
	public Boolean isBreakLineAfter(){
		InputItem item=(InputItem) getAnnotation(InputItem.class);
		if(item!=null){
			return item.breakLineAfter();
		}
		return false;
	}
//**********************************************************************************************
	@XmlTransient
	public Boolean isMandatory(){
		InputItem item=(InputItem) getAnnotation(InputItem.class);
		if(item!=null){
			return item.isManadatory();
		}
		return false;
	}
//**********************************************************************************************
	@XmlTransient
	public Long getMinValue(){
		InputItem item=(InputItem) getAnnotation(InputItem.class);
		if(item!=null){
			return item.minValue();
		}
		return -1l;
	} 
//**********************************************************************************************
	@XmlTransient
	public Long getMaxValue(){
		InputItem item=(InputItem) getAnnotation(InputItem.class);
		if(item!=null){
			return item.maxValue();
		}
		return -1l;
	}
//**********************************************************************************************
	@XmlTransient
	public String getEndDateItem(){
		InputItem item=(InputItem) getAnnotation(InputItem.class);
		if(item!=null){
			String endDateItem=item.endDateField();
			return endDateItem==null?null:endDateItem.length()==0?null:endDateItem.trim();
		}
		return null;
	}
//**********************************************************************************************
	@XmlTransient
	public String getReadonlyLogic() {
		return getReadonlyLogic(predefindedActions.save);
	}
//**********************************************************************************************
	public String getReadonlyLogic(predefindedActions action) {
		InputItem inpItem=(InputItem)getAnnotation(InputItem.class);
		String readOnlyLogic=inpItem!=null?inpItem.readonlyLogic():null;
		
		Class<? extends Annotation>annot= getAnnotationClass(action);
		if(annot!=null&&annot!=InputItem.class){
			if(SearchItem.class.equals(annot)){
				SearchItem sitem=(SearchItem) getAnnotation(SearchItem.class);
				String searchReadonlyLogic=sitem!=null?sitem.readonlyLogic():null;
				searchReadonlyLogic=searchReadonlyLogic!=null?searchReadonlyLogic.trim():null;
				if(searchReadonlyLogic!=null&&searchReadonlyLogic.length()>0){
					readOnlyLogic=searchReadonlyLogic;
				}
			}
		}
		return readOnlyLogic;
	}
//**********************************************************************************************
	private Class<? extends Annotation> getAnnotationClass(predefindedActions action){
		switch (action) {
		case save:
		case update:
		case importData: return InputItem.class;
		case search: return SearchItem.class;
		default:return null;
		}
	}
//**********************************************************************************************
	@XmlTransient
	public String getDisplayLogic() {
		return getDisplayLogic(predefindedActions.save);	
	}
//**********************************************************************************************
	public String getDisplayLogic(predefindedActions action) {
		InputItem inpItem=(InputItem)getAnnotation(InputItem.class);
		String displayLogic=inpItem!=null?inpItem.displayLogic():null;
		Class<? extends Annotation>annot= getAnnotationClass(action);
		if(annot!=null&&annot!=InputItem.class){
			if(SearchItem.class.equals(annot)){
				SearchItem sitem=(SearchItem) getAnnotation(SearchItem.class);
				String searchDisplayLogic=sitem!=null?sitem.displayLogic():null;
				searchDisplayLogic=searchDisplayLogic!=null?searchDisplayLogic.trim():null;
				if(searchDisplayLogic!=null&&searchDisplayLogic.length()>0){
					displayLogic=searchDisplayLogic;
				}
			}
		}
		return displayLogic; 
	}
//**********************************************************************************************	
	@XmlTransient
	public String getDynamicValidation() {
		InputItem inpItem=(InputItem)getAnnotation(InputItem.class);
		String dynamicValidation= inpItem!=null?inpItem.dynamicValidation():null;
		return dynamicValidation;
	}
//**********************************************************************************************
	@XmlTransient
	public String getDefaultValueLogic() {
		return getDefaultValueLogic(predefindedActions.save);
	}
//**********************************************************************************************	
	public String getDefaultValueLogic(predefindedActions action) {
		InputItem inpItem=(InputItem)getAnnotation(InputItem.class);
		String defaultValueLogic= inpItem!=null?inpItem.defaultValue():null;
		Class<? extends Annotation>annot= getAnnotationClass(action);
		if(annot!=null&&annot!=InputItem.class){
			if(SearchItem.class.equals(annot)){
				SearchItem sitem=(SearchItem) getAnnotation(SearchItem.class);
				String searchDefaultValueLogic=sitem!=null?sitem.defaultValue():null;
				searchDefaultValueLogic=searchDefaultValueLogic!=null?searchDefaultValueLogic.trim():null;
				if(searchDefaultValueLogic!=null&&searchDefaultValueLogic.length()>0){
					defaultValueLogic=searchDefaultValueLogic;
				}
			}
		}
		return defaultValueLogic;
	}
//**********************************************************************************************	
	public Boolean displayOnSave(){
		return displayOn(InputItemLogicOnAction.OnSave);
	}
//**********************************************************************************************
	public Boolean displayOnEdit(){
		return displayOn(InputItemLogicOnAction.OnUpdate);
	}
//**********************************************************************************************
	public Boolean displayOnImport(){
		return displayOn(InputItemLogicOnAction.OnImport);	
	}
//**********************************************************************************************
	public Boolean displayOnReport(){
		return displayOn(InputItemLogicOnAction.OnReport);
	}
//**********************************************************************************************	
	private Boolean displayOn(InputItemLogicOnAction action){
		return isDisplayOnInputItem(action)||isDisplayOnSearchItem(action);
	}
//**********************************************************************************************
	private boolean isDisplayOnInputItem(InputItemLogicOnAction action){
		InputItem inpItem=(InputItem)getAnnotation(InputItem.class);
		if(inpItem==null)return false;
		for(InputItemLogicOnAction disp:inpItem.displayOnAction()){
			if(disp.equals(action)){
				return true;
			}
		}
		return false;
	}
//**********************************************************************************************
	private boolean isReadOnlyInputItem(InputItemLogicOnAction action){
		InputItem inpItem=(InputItem)getAnnotation(InputItem.class);
		if(inpItem==null)return false;
		for(InputItemLogicOnAction readOnly:inpItem.readonlyOnAction()){
			if(readOnly.equals(action)){
				return true;
			}
		}
		return false;
	}
//**********************************************************************************************
	private boolean isDisplayOnSearchItem(InputItemLogicOnAction action){
		SearchItem searchItem=(SearchItem)getAnnotation(SearchItem.class);
		if(searchItem==null)return false;
		for(InputItemLogicOnAction disp:searchItem.displayOnAction()){
			if(disp.equals(action)){
				return true;
			}
		}
		return false;
	}
//**********************************************************************************************
	public Boolean displayOn(predefindedActions action){
		boolean res= displayOn( InputItemLogicOnAction.convert(action));
		
		return res;
	}
//**********************************************************************************************
	public Boolean readOnlyOn(predefindedActions action){
		boolean res= isReadOnlyInputItem( InputItemLogicOnAction.convert(action));
		return res;
	}
//**********************************************************************************************
	@XmlTransient
	public Class<?> getPersistentMethodClass() {
		return persistentClass;
	}
//**********************************************************************************************
	public void setPersistentMethodClass(Class<?> persistentClass) {
		this.persistentClass = persistentClass;
	}
//**********************************************************************************************
	@XmlTransient
	public int getDecimalPercision(){
		InputItem inpItem=(InputItem)getAnnotation(InputItem.class);
		if(inpItem==null)return 0;
		return inpItem.decimalPrecision();
	}
//**********************************************************************************************
	@XmlTransient
	public String getDecimalSeperator(){
		InputItem inpItem=(InputItem)getAnnotation(InputItem.class);
		if(inpItem==null)return ",";
		return inpItem.decimalSeparator();
	}
//**********************************************************************************************	
	@XmlTransient
	public boolean isUseDecimalSeperator(){
		InputItem inpItem=(InputItem)getAnnotation(InputItem.class);
		if(inpItem==null)return false;
		return inpItem.useSeperator();
	}
//**********************************************************************************************
	public Boolean shouldConfirm(){
		InputItem inpItem=(InputItem)getAnnotation(InputItem.class);
		if(inpItem==null)return false;
		return inpItem.shouldConfirm();
	}
//**********************************************************************************************
	@XmlTransient
	public FormPersistentAttribute.FormToEntityDataTypeMap getFormToEntityMapType(){
		return formToEntityMapType;
	}
//**********************************************************************************************
	@XmlTransient
	public FormPersistentAttribute.FormToEntityMapType getMapType(){
		return mapType;
	}
//******************************************************************************************
	@XmlTransient
	public boolean isLookup(){
		return getDisplayType()==null?false:getDisplayType().isLookup();
	}
//******************************************************************************************	
	@XmlTransient
	public UtopiaPageLinkMetaData getLink() {
		return link;
	}
//******************************************************************************************
	public void setLink(UtopiaPageLinkMetaData link) {
		this.link = link;
	}
//******************************************************************************************
	@XmlTransient
	public int getPreferedWidth() {
		return preferedWidth;
	}
//******************************************************************************************
	public void setPreferedWidth(int preferedWidth) {
		this.preferedWidth = preferedWidth;
	}
//******************************************************************************************	
   public LOVMetaData getLOVConfiguration(predefindedActions action) {
	   LOVMetaData result;
	   if(action==null){
		   action=predefindedActions.save;
	   }
	   if(action==predefindedActions.search){
		   result=LOVConfigurationMap.get(action);
		   if(result==null){
			   result=LOVConfigurationMap.get(predefindedActions.save);
		   }
	   }
	   else{
		   result=LOVConfigurationMap.get(predefindedActions.save);
	   }
		return result;
	}
//******************************************************************************************	
	public class NativeConfiguration{
		
		String functionName;
		NativeEventType eventType;
		public String getFunctionName() {
			return functionName;
		}
		public void setFunctionName(String functionName) {
			this.functionName = functionName;
		}
		public NativeEventType getEventType() {
			return eventType;
		}
		public void setEventType(NativeEventType eventType) {
			this.eventType = eventType;
		}
		
	}
//******************************************************************************************	
	public class LOVMetaData{
		Class<?> fromClass;
		String conditions;
		boolean multiSelect;
		String []boxSelectingColumns;
		String displayItemSeperator;
		public Class<?> getFromClass() {
			return fromClass;
		}
		public void setFromClass(Class<?> fromClass) {
			this.fromClass = fromClass;
		}
		public String getConditions() {
			return conditions;
		}
		public void setConditions(String conditions) {
			this.conditions = conditions;
		}
		public boolean isMultiSelect() {
			return multiSelect;
		}
		public void setMultiSelect(boolean multiSelect) {
			this.multiSelect = multiSelect;
		}
		public String[] getBoxSelectingColumns() {
			return boxSelectingColumns;
		}
		public void setBoxSelectingColumns(String... boxSelectingColumns) {
			this.boxSelectingColumns = boxSelectingColumns;
		}
		public String getDisplayItemSeperator() {
			return displayItemSeperator;
		}
		public void setDisplayItemSeperator(String displayItemSeperator) {
			this.displayItemSeperator = displayItemSeperator;
		}
		
	}
}
