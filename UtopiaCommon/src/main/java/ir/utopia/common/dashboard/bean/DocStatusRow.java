package ir.utopia.common.dashboard.bean;

import java.io.Serializable;

public class DocStatusRow implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5829112626076260882L;
	Long docTypeId,docStstusFrom,docStatusTo;

	public DocStatusRow(Long docTypeId, Long docStstusFrom, Long docStatusTo) {
		super();
		this.docTypeId = docTypeId;
		this.docStstusFrom = docStstusFrom;
		this.docStatusTo = docStatusTo;
	}

	public Long getDocTypeId() {
		return docTypeId;
	}

	public void setDocTypeId(Long docTypeId) {
		this.docTypeId = docTypeId;
	}

	public Long getDocStstusFrom() {
		return docStstusFrom;
	}

	public void setDocStstusFrom(Long docStstusFrom) {
		this.docStstusFrom = docStstusFrom;
	}

	public Long getDocStatusTo() {
		return docStatusTo;
	}

	public void setDocStatusTo(Long docStatusTo) {
		this.docStatusTo = docStatusTo;
	}
}
