package ir.utopia.core.util.tags.importpage.client;

import ir.utopia.core.util.tags.process.client.ProcessConstants;

public interface ImportFormConstants extends ProcessConstants {

	public String fileType();
	
	public String configuration();
	
	public String configurationName();
	
	public String sheetIndex();
	
	public String regexp();
	
	public String firstLineLable();
	
	public String sql();
	
	public String resourceName();
	
	public String schedulStartDate();
	
	public String scheduleEndDate();
	
	public String scheduleTime();
	
	public String fileName();
	
	String equal();
	
	String parseAsGregorianDate();
	
	String parseAsPersianDate();

	public String mapById();

	public String mapByName();

	public String mapByCode();
	
	public String mapByFixedValue();

	public String booleanConversion();
	
	String mapType();

	public String index();

	public String defaultValue();
	
	String fieldName();
	
	String duplicateIndexMessage();
	
	String pleaseEnterConfigurationName();
	
	String importTypeExcel();
	
	String importTypeSQL();
	
	String importType();
	
	String importInstances();
	
	String deleteConfigurationConfirm();
	
	String fileOnServer();
	
	String uploadTheFile();
	
	String fileLocation();
	
	String fromRow();
	
	String toRow();
}
