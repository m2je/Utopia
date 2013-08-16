package ir.utopia.core.util.tags.datainputform.server;

import java.io.Serializable;

import ir.utopia.core.struts.UtopiaFormMetaData;
import ir.utopia.core.usecase.actionmodel.UseCase;

public class ReportModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6614904116051347862L;
	String orderbyCol;
	Long []recordIds;
    UtopiaFormMetaData meta;
    UseCase usecase;
	public ReportModel(UseCase usecase,UtopiaFormMetaData meta,Long[] recordIds,String orderbyCol	) {
		super();
		this.usecase=usecase;
		this.orderbyCol = orderbyCol;
		this.recordIds = recordIds;
		this.meta = meta;
	}
	public String getOrderbyCol() {
		return orderbyCol;
	}
	public void setOrderbyCol(String orderbyCol) {
		this.orderbyCol = orderbyCol;
	}
	public Long[] getRecordIds() {
		return recordIds;
	}
	public void setRecordIds(Long[] recordIds) {
		this.recordIds = recordIds;
	}
	public UtopiaFormMetaData getMeta() {
		return meta;
	}
	public void setMeta(UtopiaFormMetaData meta) {
		this.meta = meta;
	}
	public UseCase getUsecase() {
		return usecase;
	}
	public void setUsecase(UseCase usecase) {
		this.usecase = usecase;
	}
    
}
