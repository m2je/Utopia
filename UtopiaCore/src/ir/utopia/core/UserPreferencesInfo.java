package ir.utopia.core;

import ir.utopia.core.security.userpreferences.persistent.CoUserPreferences;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserPreferencesInfo implements Serializable{
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(UserPreferencesInfo.class.getName());
	}
	
	public static final String SHOW_ALL_ORGANIZATION="showAllOrganizations";
	public static final String CURRENT_ORGANIZATION_ID="currentOrganizationId";
	public static final String CURRENT_FISCAL_YEAR="currentFiscalYear";
	/**
	 * 
	 */
	private static final long serialVersionUID = 601308634150483236L;
	boolean showAllAccessibleOrganizations=false;
	Long currentOrganizationId;
	Long currentFiscalYearId;
	public boolean isShowAllAccessibleOrganizations() {
		return showAllAccessibleOrganizations;
	}

	public void setShowAllAccessibleOrganizations(
			boolean showAllAccessibleOrganizations) {
		this.showAllAccessibleOrganizations = showAllAccessibleOrganizations;
	}
	public UserPreferencesInfo(){
		
	}
	public UserPreferencesInfo(List<CoUserPreferences> preferences){
		if(preferences!=null){
			for(CoUserPreferences pref:preferences){
				if(SHOW_ALL_ORGANIZATION.equalsIgnoreCase(pref.getKey())){
					try {
						setShowAllAccessibleOrganizations(Boolean.parseBoolean(pref.getValue()));
					} catch (Exception e) {
						if(logger.isLoggable(Level.FINE)){
							logger.log(Level.WARNING,"",e);
						}
					}
				}else if(CURRENT_ORGANIZATION_ID.equals(pref.getKey())){
						setCurrentOrganizationId(Long.parseLong(pref.getValue()));
				}else if(CURRENT_FISCAL_YEAR.equalsIgnoreCase(pref.getKey())){
					setCurrentFiscalYearId(Long.parseLong(pref.getValue()));
				}
			}
		}
	}
	
	public List<CoUserPreferences> exportPreferences(){
		List<CoUserPreferences>result=new ArrayList<CoUserPreferences>();
		CoUserPreferences showAllOrgs=new CoUserPreferences();
		showAllOrgs.setKey(SHOW_ALL_ORGANIZATION);
		showAllOrgs.setValue(String.valueOf(showAllAccessibleOrganizations));
		result.add(showAllOrgs);
		if(currentOrganizationId!=null){
			CoUserPreferences currentOrgId=new CoUserPreferences();
			currentOrgId.setKey(CURRENT_ORGANIZATION_ID);
			currentOrgId.setValue(getCurrentOrganizationId().toString());
			result.add(currentOrgId);
		}
		if(currentFiscalYearId!=null){
			CoUserPreferences currentFiscalYId=new CoUserPreferences();
			currentFiscalYId.setKey(CURRENT_FISCAL_YEAR);
			currentFiscalYId.setValue(currentFiscalYearId.toString());
			result.add(currentFiscalYId);
		}
		
		return result;
	}

	public Long getCurrentOrganizationId() {
		return currentOrganizationId;
	}

	public void setCurrentOrganizationId(Long currentOrganizationId) {
		this.currentOrganizationId = currentOrganizationId;
	}

	public Long getCurrentFiscalYearId() {
		return currentFiscalYearId;
	}

	public void setCurrentFiscalYearId(Long currentFiscalYearId) {
		this.currentFiscalYearId = currentFiscalYearId;
	}
	
	
}
