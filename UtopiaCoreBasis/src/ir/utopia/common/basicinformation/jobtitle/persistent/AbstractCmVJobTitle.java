package ir.utopia.common.basicinformation.jobtitle.persistent;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


/**
 * AbstractCmVJobTitle entity provides the base persistence definition of the
 * CmVJobTitle entity.
 * 
 * @author Arsalani
 */
@MappedSuperclass
public abstract class AbstractCmVJobTitle extends AbstractUtopiaPersistent implements java.io.Serializable  {

	// Fields
	private static final long serialVersionUID = -4433564164724824211L;
	private Long cmJobTitleId;
	private String code;
	private String name;
	
	
	// Constructors

		/** default constructor */
		public AbstractCmVJobTitle() {
		}


		
		// Property accessors
		@Id
		@Column(name = "CM_JOBTITLE_ID")
		public Long getCmJobTitleId() {
			return cmJobTitleId;
		}
		public void setCmJobTitleId(Long cmJobTitleId) {
			this.cmJobTitleId = cmJobTitleId;
		}


		
		@Column(name = "CODE")
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}


		
		@Column(name = "NAME")
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
}
