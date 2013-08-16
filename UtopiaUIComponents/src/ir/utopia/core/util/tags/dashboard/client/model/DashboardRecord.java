package ir.utopia.core.util.tags.dashboard.client.model;

import ir.utopia.core.util.tags.datainputform.client.grid.model.GridRowData;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DashboardRecord extends GridRowData implements IsSerializable,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -514369161383662399L;

	private static final int INDEX_COLUMN_INDEX=0;
	private static final int DATE_COLUMN_INDEX=1;
	private static final int SUBJECT_COLUMN_INDEX=2;
	private static final int DESCRIPTION_COLUMN_INDEX=3;
	boolean read;
	Date lastModified;
	String updatedBy;
	long docStatusId;
	int status;
	public DashboardRecord(){
		setData(new String[]{"","","",""});
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getSubject() {
		return data[SUBJECT_COLUMN_INDEX];
	}
	public void setSubject(String subject) {
		this.data[SUBJECT_COLUMN_INDEX]= subject;
	}
	public String getDescription() {
		return data[DESCRIPTION_COLUMN_INDEX];
	}
	public void setDescription(String description) {
		data[DESCRIPTION_COLUMN_INDEX]= description;
	}
	public long getDocStatusId() {
		return docStatusId;
	}
	public void setDocStatusId(long docStatusId) {
		this.docStatusId = docStatusId;
	}
	
	public boolean isRead() {
		return read;
	}
	public void setRead(boolean read) {
		this.read = read;
	}
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getIndex() {
		return data[INDEX_COLUMN_INDEX];
	}
	public void setIndex(String index) {
		data[INDEX_COLUMN_INDEX] = index;
	}
	public String getDate() {
		return data[DATE_COLUMN_INDEX];
	}
	public void setDate(String date) {
		data[DATE_COLUMN_INDEX]= date;
	}
	
	
	
}
