package ir.utopia.core.importer;

import ir.utopia.core.importer.model.DefaultImportDataHandler;
import ir.utopia.core.importer.model.ImportDataHandler;
import ir.utopia.core.struts.UtopiaFormMetaData;

/**
 * Setups info for persistent import.
 * User configure import settings for every persistent he wants to import.
 * This class contains this settings and in run time load a persistent for sample
 * that importer clone this persistent and fill it by excel data and save in DB.
 */
public class ImportSetup extends UtopiaFormMetaData  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7366384132561665087L;
	private boolean firstLineIsTitle;
	private transient Class<? extends ImportDataHandler> dataHandlerClass=DefaultImportDataHandler.class; 
	private int userFrom;
	private int userTo;
	private int sheetIndex;
	private String regExp;
	private String locale;
	private String sql;
	private String resourceName;
	public ImportSetup(UtopiaFormMetaData meta,String locale){
		super(meta.getClass(),meta.getPersistentClass(),meta.getIdMethodName());
		this.setRemoteUsecaseClass(meta.getRemoteUsecaseClass());
		this.setAnnotations(meta.getAnnotations());
		this.setClazz(meta.getClazz());
		this.setIdMethodName(meta.getIdMethodName());
		this.setIncludedForms(meta.getIncludedForms());
		this.setPersistentClass(meta.getPersistentClass());
		this.setUsecaseLoadMethod(meta.getUsecaseLoadMethod());
		this.locale=locale;
	} 
	
	public boolean isFirstLineTitle() {
		return firstLineIsTitle;
	}

	public void setFirstLineTitle(boolean firstLineIsTitle) {
		this.firstLineIsTitle = firstLineIsTitle;
	}

	/**
	 * Gets the field setup.
	 * 
	 * @param fieldName the field name
	 * 
	 * @return the field setup
	 */
	public FieldSetup getFieldSetup(String fieldName){//A user config for a field that identify type of field, map to sheet or a constant value
		
		return (FieldSetup)getMethodMetaData(fieldName);
	}
	
	
	
	/**
	 * Gets the fields setup.
	 * 
	 * @return the fields setup
	 */
	public FieldSetup[] getFieldsSetup() {
		return (FieldSetup[])getMethodMetaData();
	}


	public Class<? extends ImportDataHandler> getDataHandlerClass() {
		return dataHandlerClass;
	}

	public void setDataHandlerClass(Class<? extends ImportDataHandler> dataHandlerClass) {
		this.dataHandlerClass = dataHandlerClass;
	}

	public int getUserFrom() {
		return userFrom;
	}

	public void setUserFrom(int userFrom) {
		this.userFrom = userFrom;
	}

	public int getUserTo() {
		return userTo;
	}

	public void setUserTo(int userTo) {
		this.userTo = userTo;
	}

	public int getSheetIndex() {
		return sheetIndex;
	}

	public void setSheetIndex(int sheetIndex) {
		this.sheetIndex = sheetIndex;
	}

	public String getRegExp() {
		return regExp;
	}

	public void setRegExp(String regExp) {
		this.regExp = regExp;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}


	 
}

