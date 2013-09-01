package ir.utopia.common.basicinformation.bank.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "CM_BANK", uniqueConstraints = {})
public class CmBank extends AbstractCmBank implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 3897621041992702095L;

	public CmBank() {
	}

}
