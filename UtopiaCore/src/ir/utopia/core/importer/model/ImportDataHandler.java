package ir.utopia.core.importer.model;

import ir.utopia.core.constants.Constants;
import ir.utopia.core.importer.model.exception.ImportDataException;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

public interface ImportDataHandler extends Serializable {
	
	
	public static final String IMPORT_PROPERTIES_EXCEL_SHEET_INDEX="shitIndex";
	
	public static final String EXCEL_FIRST_ROW_IS_HEADER="firstRowIsHeader";
	
	public static final String TEXT_FETCH_EXPRESSION="fetchExpression";
	
	public static final String SQL="sql";
	
	public static final String RESOURCE_NAME="resourceName";
	
	public static final String LAST_IMPORTED_RECORD_PK_PARAMETER_NAME="lastImportedPK";
	/**
	 * fetch record in the file from :from to :to
	 * @param from
	 * @param to
	 * @return
	 */
	public Vector<Map<Integer, String>> fetchRecords(int from,int to,Map<String,Object>context)throws ImportDataException;
	/**
	 * @return
	 */
	public int getRecordCount(Map<String,Object>context)throws ImportDataException;
	/*
	 * 
	 */
	public String getCommand(Map<String,Object>context);
	/**
	 * 
	 * @param file
	 * @param fileType
	 */
	public void setFile(String file,Constants.ImportFormat fileType);
	/**
	 * 
	 * @param props
	 */
	public void setProperties(Properties props);
	
	public void clear();
}
