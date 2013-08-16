package ir.utopia.common.basicinformation.branch.persistent;

import ir.utopia.core.persistent.annotation.LookupConfiguration;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "CM_BRANCH", uniqueConstraints = {})
@LookupConfiguration(displayColumns={"code","branchName","cmBank.name"},displayItemSeperator="-")
public class CmBranch extends AbstractCmBranch implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 4384745024789790583L;

	public CmBranch() {
	}

}
