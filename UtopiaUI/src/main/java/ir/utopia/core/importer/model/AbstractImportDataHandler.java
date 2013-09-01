package ir.utopia.core.importer.model;

import ir.utopia.core.constants.Constants;
import ir.utopia.core.importer.model.exception.ImportDataException;
import ir.utopia.core.logic.util.LogicParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.InitialContext;

import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;

public abstract class AbstractImportDataHandler implements ImportDataHandler{
	private static final Logger logger;
	private static SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
	static {
		logger = Logger.getLogger(AbstractImportDataHandler.class.getName());
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 5759809806274408140L;
	Constants.ImportFormat filetype;
	String file;
	Properties props;
	Integer recordCount=null;
	private static final int MAXIMUM_SEQUENTIAL_EMPTY_LINES=20;
	private Long lastFileReadedPosition;
	
	private int lastTo=0;
	 
	private transient Connection conn;
    private transient PreparedStatement ps;
    private transient ResultSet rs;
    private transient ResultSetMetaData meta;
	@Override
	public Vector<Map<Integer, String>> fetchRecords(int from, int to,Map<String,Object>context)throws ImportDataException {
		try {
			if(Constants.ImportFormat.excel.equals(filetype)){
				return getExcelValue(file,getShitIndex(props), from, to,context);
//			}else if(Constants.ImportFormat.txt.equals(filetype)){ //TODO
//				return getTextValue(file, props.getProperty(TEXT_FETCH_EXPRESSION), from, to,context);
			}else if(Constants.ImportFormat.sql.equals(filetype)){
				return getQueryValue(getFormatedSQL(context), props.getProperty(RESOURCE_NAME), from, to,context);
			}
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
			if(e instanceof ImportDataException){
				throw (ImportDataException)e;
			}
			logger.log(Level.WARNING, "",e);	
		}
		return null;
	}
//***********************************************************************************************************
	private Vector<Map<Integer, String>> getQueryValue(String query,String resourceName, int from,int to,Map<String,Object>context)throws Exception{
		
		Vector<Map<Integer, String>>resultValues=new Vector<Map<Integer,String>>();
		int recordCount=getRecordCount(context);
    	int realTo=((to>recordCount)?recordCount:to);
    	if(realTo<=0||realTo<from||from<0){
    		return resultValues;
    	}
		try {
			Long t1=System.currentTimeMillis();
			int size=realTo-from;
    		logger.log(Level.INFO," Datahandler is about to fetch SQL data result with page size: "+size+" records ");
			if(conn==null){
				createSQLObjects(resourceName, query,size, context);
			}
			for(int i=lastTo;i<from;i++){
				if(!rs.next()){
					return resultValues;
				}
			}
	        for(int i=from;i<realTo;i++){
	        	if(rs.next()){
		        	Map<Integer, String> valuesMap = new HashMap<Integer, String>();
		        	for(int j=0;j<meta.getColumnCount();j++){
		    			 Object value=rs.getObject(j+1);
		    			 valuesMap.put(j+1,value!=null?value.toString().trim():null);
		    		 }
		    		 resultValues.add(valuesMap);
	            }
	        	else{
		        	break;
		        }
	        }
	        
	        lastTo=realTo;
	        logger.log(Level.INFO,size+" rows of data successfully loaded in "+(System.currentTimeMillis()-t1)+" ms");
	        return resultValues;	
		    } catch (Exception  e) {
		    	logger.log( Level.ALL,"", e);
			}
		return null;
	}
//***********************************************************************************************************
	private void createSQLObjects(String resourceName,String query,int fetchSize,Map<String,Object>context)throws Exception{
		conn=getConnection(resourceName);
        ps= createStatement(query, conn, context,false);
        rs=ps.executeQuery();
//        rs.setFetchSize(fetchSize);
        meta= rs.getMetaData();
	}
//***********************************************************************************************************
	private PreparedStatement createStatement(String query,Connection conn,Map<String,Object>context,boolean count)throws Exception{
		 List<String>parameters= LogicParser.getParametersInString(query);
	        int paramCounter=0;
	        Map<Integer,Object>paramMap=new HashMap<Integer, Object>();
	        if(parameters!=null&&parameters.size()>0){
	        	for(String param:parameters){
	        		query=query.replaceAll(param, "?");
	        		paramMap.put(paramCounter++,context.get(param));
	        	}
	        }
	        if(count)
	        {	
	        	query=new StringBuffer(" select count(*) from (").append(query).append(" ) a").toString();
	        	
	        }
	        PreparedStatement statement=conn.prepareStatement(query);
	        for(int param:paramMap.keySet()){
	        	statement.setObject(param, paramMap.get(param));
	        }
	        return statement;
	}  
//***********************************************************************************************************
	private Connection getConnection(String resourceName)throws Exception{
        InitialContext ic = new InitialContext();
        javax.sql.DataSource dataSource = (javax.sql.DataSource)ic.lookup(resourceName);
        return dataSource.getConnection();
	}
//***********************************************************************************************************
	/**
	 * 
	 * @param importSetup
	 * @param excelFile
	 * @param from
	 * @param to
	 * @return
	 * @throws Exception
	 */
	private Vector<Map<Integer, String>> getExcelValue(String excelFile,int shitIndex, int from,int to,Map<String,Object>context)throws Exception{
		Workbook workbook = Workbook.getWorkbook(new File(excelFile));
		Sheet sheet = workbook.getSheet(shitIndex);
		Vector<Map<Integer, String>>excelValues=new Vector<Map<Integer,String>>();
		int rowCount=getRecordCount(context);
		for(int i=from;(i<rowCount&&i<to);i++){
			Map<Integer, String> valuesMap = new HashMap<Integer, String>();
			for(int j=0;j<sheet.getColumns();j++){
				String value = sheet.getCell(j, i).getContents();
				if(sheet.getCell(j, i).getType().equals(CellType.DATE))
					value = dateFormat.format((((DateCell)sheet.getCell(j, i)).getDate()));
					valuesMap.put(j+1,value!=null?value.trim():null);
			}
			boolean isNullRow=true;
			for(String val:valuesMap.values()){
				isNullRow=val==null||val.length()==0;
				if(!isNullRow){
					break;
				} 
			}
			if(!isNullRow){
				excelValues.add(valuesMap);
				}
		}
		return excelValues;
	}
//***********************************************************************************************************
	public Vector<Map<Integer, String>> getTextValue(String textFile,String patern, int from,int to,Map<String,Object>context)throws Exception{
		 	int index =0;
		 	RandomAccessFile buf_reader=new RandomAccessFile(textFile, "r");
		 	String line;
		    int count=getRecordCount(context);
		    Vector<Map<Integer, String>>result=new Vector<Map<Integer,String>>(to-from);
			if(from==lastTo){
				buf_reader.seek(lastFileReadedPosition);
				index=lastTo;
		 	}else  {//greater than first line
		 		while (index<from&&index<count) {
			          line = buf_reader.readLine ();
			          index++;
			          }
		 	}
			lastTo=to;
	      Pattern pat = Pattern.compile(patern);
	      while (index<to&&index<count) {
	          line = buf_reader.readLine ();
	          index++;
	         if (line == null) {
	        	 int i;
	        	 for( i=0;i<MAXIMUM_SEQUENTIAL_EMPTY_LINES;i++){
	        		 if(buf_reader.readLine()!=null){
	        			 throw new ImportDataException(index);
	        		 }	 
	        	 }
	        	 break;
	        	 
	        	 }
	         
	         
		      Matcher matcher = pat.matcher(line);
		      Map<Integer, String> valuesMap = new HashMap<Integer, String>();

		      int groupCount=matcher.groupCount();
      		if(matcher.find()){
	        		for(int i=1;i<=groupCount;i++){
	        			int j=0;
	        			matcher.reset();
	        			while(matcher.find()){
	        				String value=matcher.group(i);
	        				if(value!=null)
	        				{
	        					value=value.replaceAll("\"", "").trim();
	        					valuesMap.put(j+1,value);
	        					j++;
	        				}
	        			}
	        		}
      		}	
		      result.add(valuesMap);
	      } 
	      lastFileReadedPosition=buf_reader.getFilePointer();
	      buf_reader.close ();
	    
		return result;
	}
//***********************************************************************************************************
	@Override
	public int getRecordCount(Map<String,Object>context) {
		try {
			if(recordCount==null){
				if(Constants.ImportFormat.excel.equals(filetype)){
					recordCount= getExcelRecorCount(new File(file),getShitIndex(props),isFirstLineHeader(props));
//				}else if(Constants.ImportFormat.txt.equals(filetype)){ //TODO
//					recordCount= getTextRecordCount(new File(file));
				}else if(Constants.ImportFormat.sql.equals(filetype)){
					recordCount= getSQLRecordCount(context);
				}
				else{
					recordCount=-1;
				}
			}
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
			recordCount=-1;
		}
		return recordCount;
	}
//**********************************************************************************************************
	private int getExcelRecorCount(File file,int shitIndex,boolean firstLineHeader)throws Exception{
		Workbook workbook = Workbook.getWorkbook(file);
		Sheet sheet = workbook.getSheet(shitIndex);
		int count= sheet.getRows();
		return count;
	}
//**********************************************************************************************************
	private int getTextRecordCount(File file)throws Exception{
				int result=0;
              FileReader file_reader = new FileReader (file);
		      BufferedReader buf_reader = new BufferedReader (file_reader);
		      do {
		         String line = buf_reader.readLine ();
		         if (line == null) break;
		         result++;
		      } while (true);
		      buf_reader.close ();
		    return result;
	}
//**********************************************************************************************************
	private int getSQLRecordCount(Map<String,Object>context)throws Exception{
		
		Connection conn=getConnection(props.getProperty(RESOURCE_NAME));
		PreparedStatement ps=createStatement(getFormatedSQL(context), conn, context,true);
        ResultSet rs= ps.executeQuery();
        int result=-1;
        if(rs.next())
        	result=rs.getInt(1);
        rs.close();
        ps.close();
        conn.close();
		return result;
	}
//**********************************************************************************************************
	private int getShitIndex(Properties props){
		return props==null?0:(Integer)props.get(IMPORT_PROPERTIES_EXCEL_SHEET_INDEX);
	}
//**********************************************************************************************************
	private boolean isFirstLineHeader(Properties props){
		return props==null?false:(Boolean)props.get(EXCEL_FIRST_ROW_IS_HEADER);
	}
//**********************************************************************************************************
	@Override
	public void setFile(String file, Constants.ImportFormat fileType) {
		this.file=file;
		this.filetype=fileType;
	}
//**********************************************************************************************************
	@Override
	public void setProperties(Properties props) {
		this.props=props;
	}
//**********************************************************************************************************
	private String getFormatedSQL(Map<String,Object>context){
		String query=props.getProperty(SQL);
		if(query!=null){
			Object c=context.get(LAST_IMPORTED_RECORD_PK_PARAMETER_NAME);
			String result=query.replaceAll("@"+LAST_IMPORTED_RECORD_PK_PARAMETER_NAME+"@", c==null?"-1":c.toString());
			logger.log(Level.INFO,"System is about to execute query :"+result);
			return  result;
		}
		return null;
	}
//**********************************************************************************************************
	public void clear(){
		lastFileReadedPosition=null;
		
		try {
			if(conn!=null&&!conn.isClosed()){
				ps.close();
				rs.close();
				conn.close();
				this.conn=null;
				this.rs=null;
				this.ps=null;
				this.meta=null;
			}
		} catch (SQLException e) {
			logger.log(Level.WARNING,"", e);
		}
		System.gc();
	}
//**********************************************************************************************************
	@Override
	public String getCommand(Map<String,Object>context) {
		return getFormatedSQL(context);
		
	}
}
