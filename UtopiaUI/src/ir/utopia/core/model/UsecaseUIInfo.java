/**
 * 
 */
package ir.utopia.core.model;

import ir.utopia.core.struts.FormUtil;
import ir.utopia.core.struts.UtopiaFormMetaData;
import ir.utopia.core.usecase.actionmodel.UseCase;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Salarkia
 *
 */
public class UsecaseUIInfo {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(UsecaseUIInfo.class.getName());
	}
	private UseCase usecase;
	private UtopiaFormMetaData meta;
	private String metaDataClass;
	boolean loaded=false;
	public UsecaseUIInfo(UseCase usecase,String metaDataClass) {
		super();
		this.usecase = usecase;
		this.metaDataClass=metaDataClass;
	}
	
	public UseCase getUsecase() {
		return usecase;
	}
	public void setUsecase(UseCase usecase) {
		this.usecase = usecase;
	}
	public UtopiaFormMetaData getMeta() {
		if(!loaded){
			loaded=true;
			try {
				meta=	FormUtil.getMetaData(metaDataClass);
			} catch (Exception e) {
				logger.log(Level.WARNING,"", e);
			}
		}
		return meta;
	}
	public void setMeta(UtopiaFormMetaData meta) {
		
		this.meta = meta;
	}
	
	
	
}
