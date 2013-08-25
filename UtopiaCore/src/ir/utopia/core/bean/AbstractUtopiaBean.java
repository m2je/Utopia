/**
 * 
 */
package ir.utopia.core.bean;

import javax.persistence.Query;

//import oracle.toplink.essentials.config.HintValues;
//import oracle.toplink.essentials.config.TopLinkQueryHints;



/**
 * @author salarkia
 *
 */
public abstract class AbstractUtopiaBean implements UtopiaBean {
	
	
	
//************************************************************************************************************
		protected void resetQueryCache(Query query){
//			query.setHint(TopLinkQueryHints.REFRESH, HintValues.TRUE);
//			throw new RuntimeException("reset query");
		}

}
