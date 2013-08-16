package ir.utopia.core.persistent;

import ir.utopia.core.persistent.lookup.model.DetailPersistentValueInfo;
import ir.utopia.core.persistent.lookup.model.LookupInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UtopiaBasicPersistent extends Cloneable {

	/**
	 * 
	 * @return
	 */
	public Long getRecordId();
	/**
	 * 
	 */
	public void setRecordId(Long recordId);
	
	/**
	 * sets lookup information 
	 * @param info
	 */
	public void setLookupInfos(List<LookupInfo> infos);
	/**
	 * 
	 * @return
	 */
	public List<LookupInfo> getLookupInfo();
	/**
	 * 
	 * @param info
	 */
	public void setAttachmentInfos(List<UtopiaAttachmentInfo> infos);
	/**
	 * 
	 */
	public List<UtopiaAttachmentInfo> getAttachmentInfos();
	/**
	 * 
	 * @param customProperties
	 */
	public void setCustomProperties(Map<String,String>customProperties);
	/**
	 * 
	 * @param 
	 */
	public Map<String,String> getCustomProperties();
	public abstract List<List<String>> getCustomPropertyList();
	
	public abstract void setCustomPropertyList(List<List<String>>customProperties);
	
	public void setRevisionDescription(String description);
	
	public String getRevisionDescription();
	
	public void setIncludedPersistentValue(String columnName,Collection<DetailPersistentValueInfo> includedValues );
	
	public Collection<DetailPersistentValueInfo> getIncludedPersistentValue(String columnName);
	
	
}
