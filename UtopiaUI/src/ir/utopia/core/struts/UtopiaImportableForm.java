package ir.utopia.core.struts;

import ir.utopia.core.constants.Constants;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.MappedSuperForm;
import ir.utopia.core.persistent.AbstractUtopiaPersistent;
import ir.utopia.core.persistent.UtopiaPersistent;

import java.io.File;
import java.lang.reflect.ParameterizedType;

import com.opensymphony.xwork2.conversion.annotations.TypeConversion;

@MappedSuperForm
public class UtopiaImportableForm<P extends UtopiaPersistent,F extends AbstractUtopiaForm<P>>{
	private File upload;
	private boolean firstLineTitle = false;
	AbstractUtopiaForm<P> form;
	AbstractUtopiaPersistent persistent;
	private String uploadFileName;
	private String uploadContentType;
	private int from;
	private int to;
	private int sheetIndex;
	private Constants.ImportFormat fileType;
	private String regexp;
	private String sqlText;
	private String resourceName;
	@FormId
	@FormPersistentAttribute
	public Long getFormId() {
		return -1L;
	}

	public File getUpload() {
		return upload;
	}
	
	public void setUpload(File upload) {
		this.upload = upload;
	}
	@TypeConversion(converter="ir.utopia.core.struts.UtopiaTypeConverter")
	public boolean isFirstLineTitle() {
		return firstLineTitle;
	}
	
	public void setFirstLineTitle(boolean firstLineTitle) {
		this.firstLineTitle = firstLineTitle;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	
	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public AbstractUtopiaPersistent getPersistent() {
		return persistent;
	}
	
	@SuppressWarnings("unchecked")
	public AbstractUtopiaForm<P> getForm() {
		if(form==null){
			Class<F> formClass = (Class<F>) (((ParameterizedType)getClass().getGenericSuperclass())).getActualTypeArguments()[1];
			try {
				form = formClass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return this.form;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}
	
	@TypeConversion(converter="ir.utopia.core.struts.UtopiaTypeConverter")
	public Constants.ImportFormat getFileType() {
		return fileType;
	}

	public void setFileType(Constants.ImportFormat fileType) {
		this.fileType = fileType;
	}

	public int getSheetIndex() {
		return sheetIndex;
	}

	public void setSheetIndex(int sheetIndex) {
		this.sheetIndex = sheetIndex;
	}

	public String getRegexp() {
		return regexp;
	}

	public void setRegexp(String regexp) {
		this.regexp = regexp;
	}

	public String getSqlText() {
		return sqlText;
	}

	public void setSqlText(String sqlText) {
		this.sqlText = sqlText;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}	
	
}
