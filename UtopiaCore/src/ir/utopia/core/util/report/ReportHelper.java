package ir.utopia.core.util.report;

import ir.utopia.core.ContextUtil;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.constants.Constants.ReportType;
import ir.utopia.core.messagehandler.MessageHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;


public  class ReportHelper {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(ReportHelper.class.getName());
	}
    private ReportType outputType ; // Holds output type
    
    private String reportFile; // Holds Relational report template file path
    private String physicalReportPath; // Holds Physical report template file name and path
    public HttpServletRequest request; // Holds an instance of servlet request
    private HttpServletResponse response; // Holds an instance of servlet response
    private OutputStream outStr;
    private Map<Object,Object> reportParameters; // Holds report parameters
    private Vector<Map<String,Object>>dataVector=new Vector<Map<String,Object>>();
    private String  resourceName;
    public ReportHelper( JspWriter out, 
                        HttpServletRequest request, 
                        HttpServletResponse response) {
       
        this.request = request;
        this.response = response;
        try {
			outStr=response.getOutputStream();
		} catch (IOException e) {
			logger.log(Level.WARNING,"",e);
			e.printStackTrace();
		}

    }
//***************************************************************************
    public ReportHelper(OutputStream fio,ReportType reportType){
    	this.outputType=reportType;
    	
    	this.outStr=fio;
    }
 

//***************************************************************************
    public void setReportParameters(Map<Object,Object> reportParameters) {
        this.reportParameters = reportParameters;
    }

    public void setReportPath(String reportFile) {
    	this.physicalReportPath=reportFile;
    	if(physicalReportPath.endsWith(".jrxml")){
    		this.physicalReportPath=physicalReportPath.substring(0,physicalReportPath.lastIndexOf(".jrxml"));
        	this.reportFile = physicalReportPath.substring(physicalReportPath.lastIndexOf(File.separatorChar)+1) ;
    	}else{
    		int index=physicalReportPath.lastIndexOf("/");
    		if(index<0){
    			index=physicalReportPath.lastIndexOf("\\");
    		}
    		this.reportFile = physicalReportPath.substring(index+1) ;
    	}
    	
    }

    public String getReportFile() {
        return reportFile;
    }


    public Map<Object,Object> getReportParameters() {
        return reportParameters;
    }
    
    public void setOutputType(ReportType outputType) {
        this.outputType = outputType;
    }

    public ReportType getOutputType() {
        return outputType;
    }

    public Vector<Map<String, Object>> getDataVector() {
		return dataVector;
	}

	public void setDataVector(Vector<Map<String, Object>> dataVector) {
		this.dataVector = dataVector;
	}
    public String getResourceName() {
			return resourceName;
		}
	public void setResourceName(String resourceName) {
			this.resourceName = resourceName;
		}
	public void RunReport() throws FileNotFoundException, JRException, 
                                   NamingException, SQLException, IOException,IllegalAccessException {
    	InputStream input=null;
		switch(outputType) {
    
        case pdf:
        	if((new File(this.physicalReportPath+".jrxml")).exists()){
        		input = new FileInputStream(new File(this.physicalReportPath+".jrxml"));
        	}
        	break;
            
        case html:
        	if((new File(this.physicalReportPath+"_html.jrxml")).exists()){
        		input = new FileInputStream(new File(this.physicalReportPath+"_html.jrxml"));
        	}
            break;   

        case excel:
        	if((new File(this.physicalReportPath+"_excel.jrxml")).exists()){
        		input = new FileInputStream(new File(this.physicalReportPath+"_excel.jrxml"));
        	}
        	break;
        case text:
        	if((new File(this.physicalReportPath+"_text.jrxml")).exists()){
        		input = new FileInputStream(new File(this.physicalReportPath+"_text.jrxml"));
        	}
        	break;
    	}
        JasperDesign jrDesign;
        
        if(input==null)
        	if((new File(this.physicalReportPath+".jrxml")).exists())
        		input = new FileInputStream(new File(this.physicalReportPath+".jrxml"));
        if(input==null)
        	throw new IllegalAccessException("input cofiguration file can not be null");

        jrDesign = JRXmlLoader.load(input);
        JasperReport jrReport;
        jrReport = JasperCompileManager.compileReport(jrDesign);

        jrReport.setProperty(JRStyledText.PROPERTY_AWT_IGNORE_MISSING_FONT, "true");
        JasperPrint jrPrint;
        Connection conn = null;
        if(resourceName==null){
        	JRDataSource datasource=new JRMapCollectionDataSource(dataVector);
			jrPrint = JasperFillManager.fillReport(jrReport, new HashMap<Object, Object>(), datasource);
        }else{
	        
		    try {
		        InitialContext ic = new InitialContext();
		        javax.sql.DataSource dataSource = (javax.sql.DataSource)ic.lookup(resourceName);
		        
		        conn = dataSource.getConnection();
	
		    } catch (NamingException  e1) {
		    	logger.log(Level.ALL,"DB Drivar not found", e1);
			} catch (SQLException e2) {
				logger.log(Level.ALL,"DB Connection failed",  e2);
			}
			 jrPrint = JasperFillManager.fillReport(jrReport, this.reportParameters, conn);
		}

        
       
        try{
        	generateReport(jrPrint);
        	if(conn!=null)
        		conn.close();
        	}
        catch (Exception e) {
        	logger.log(Level.WARNING,"", e);
        }
        
    }
//************************************************************************************************************************     
    private void generateReport(JasperPrint jrPrint)throws Exception{
    	JRAbstractExporter exporter ;
    	String fileExtension;
    	 switch(outputType) {
         
         
             
         case html:
        	 fileExtension="html";
        	 exporter=new JRHtmlExporter();
        	 exporter.setParameter(JRExporterParameter.JASPER_PRINT, jrPrint);
        	 exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "./jasperImages.jasperImg?image=");
        	 if(request!=null){
        		 request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE,
        				 jrPrint); 
        	 }
        	 
        	 exporter.setParameter(JRExporterParameter.JASPER_PRINT,
        			 jrPrint);
        	 if(response!=null){
        		 exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream()); 
        	 }
             break;    

         case excel:
        	 fileExtension="xls";
        	 if(response!=null)
        		 response.setContentType("application/ms-excel");
 			exporter= new JRXlsExporter();
 			
 			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStr);
 			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
 			exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
 			exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
 			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
 			exporter.setParameter(JRXlsExporterParameter.START_PAGE_INDEX,0);
 			exporter.setParameter(JRXlsExporterParameter.OFFSET_X,1);
 			exporter.setParameter(JRXlsExporterParameter.OFFSET_Y,1);
         	break;
         case text:
        	 fileExtension="text";
        	 if(response!=null)
        		 response.setContentType("application/text");
 			exporter= new JRTextExporter();
 			exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, 5);
 			exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, 20);
 			exporter.setParameter(JRTextExporterParameter.CHARACTER_ENCODING, "UTF-8");
 			
 			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStr);
         	break;
         case pdf:
         	default:{
         		fileExtension="pdf";
           	 if(response!=null)
           		 response.setContentType("application/pdf");
           	 exporter=new JRPdfExporter();
        	 exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,jrPrint);
             exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStr);
       		 break;
         	}
     }
    	 exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,
 				jrPrint);
    	 
    	 String reportFile3 = reportFile.substring(0, reportFile.lastIndexOf("/")+1);
 		String reportFile4[] = reportFile.split(reportFile3);
 		String reportName = reportFile4[1].replace(".jrxml", fileExtension);
 		
 		if(response!=null&&Constants.ReportType.excel.equals(outputType)||Constants.ReportType.text.equals(outputType))
 			response.setHeader("Content-Disposition", "attachment;filename="+reportName+"."+fileExtension);
 	
 		
 		exporter.exportReport();
 		if(response!=null)
 			response.flushBuffer();
 		outStr.close();
    }
//****************************************************************************************************************************************
    public static synchronized void runReport(HttpServletRequest request,HttpServletResponse  response,ServletContext context,JspWriter out,String resourceName,String basePath){
    	runReport(request,response,context,out,new HashMap<Object, Object>(),resourceName,basePath);
    }
//****************************************************************************************************************************************
    public static synchronized void runReport(HttpServletRequest request,HttpServletResponse  response,ServletContext context,JspWriter out,Map<Object,Object> objParameters,Vector<Map<String,Object>>dataVector,String basePath,ReportType reportType){
    	runReport(request, response, context, out, objParameters, null, dataVector, basePath,reportType);
    }
//****************************************************************************************************************************************
    public static synchronized void runReport(HttpServletRequest request,HttpServletResponse  response,ServletContext context,JspWriter out,Map<Object,Object> objParameters,String resourceName,String basePath){
    	 runReport(request,response,context,out,objParameters,resourceName,basePath,ReportType.pdf);
	}
//****************************************************************************************************************************************
    public static synchronized void runReport(HttpServletRequest request,HttpServletResponse  response,ServletContext context,JspWriter out,Map<Object,Object> objParameters,String resourceName,String basePath,ReportType reportType){
    	runReport(request, response, context, out, objParameters, resourceName, null, basePath,reportType);
	}
//****************************************************************************************************************************************
    protected static synchronized void runReport(HttpServletRequest request,HttpServletResponse  response,ServletContext context,JspWriter out,Map<Object,Object> objParameters,String resourceName,Vector<Map<String,Object>>dataVector,String basePath,ReportType reportType){
    	HttpSession session=request.getSession();
    	try {
			ReportHelper objReportHelper=createReportHelper(request, response, context, out, basePath,resourceName,dataVector, objParameters,reportType);
			objReportHelper.RunReport();
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
			try {
				out.print("<p style='color: Red'>"+MessageHandler.getMessage("Error Loading report",ReportHelper.class.getName() ,ContextUtil.getLoginLanguage(ContextUtil.createContext(session)))+"</p>" );
			} catch (IOException e1) {
				logger.log(Level.WARNING,"", e);
			}
		}
    }
//****************************************************************************************************************************************
	private static  ReportHelper createReportHelper(HttpServletRequest request,HttpServletResponse  response,ServletContext context,JspWriter out,String basePath,String resourceName,Vector<Map<String,Object>>dataVector,Map<Object,Object> objParameters,ReportType reportType) throws Exception{
		objParameters.put("BaseDir", context.getRealPath("./"));
		response.setCharacterEncoding("UTF-8");
		out.write("<meta http-equiv=\"content-type\" content=\"text/html;charset=UTF-8\">");
    	ReportHelper objReportHelper= new ReportHelper(out,request, response);
    	objReportHelper.setDataVector(dataVector);
    	objReportHelper.setResourceName(resourceName);
		objReportHelper.setReportPath(context.getRealPath(basePath));
		objReportHelper.setOutputType(reportType);
		objReportHelper.setReportParameters(objParameters);
		return objReportHelper;
    }
}
