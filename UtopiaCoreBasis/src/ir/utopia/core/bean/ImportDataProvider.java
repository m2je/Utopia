package ir.utopia.core.bean;

import ir.utopia.core.persistent.UtopiaBasicPersistent;

import java.io.Serializable;
import java.util.Map;

public interface ImportDataProvider extends Serializable{
	 
	public UtopiaBasicPersistent[] getPersistents(int from,int to,Map<String,Object>context)throws Exception;
	
	public int getSize(Map<String,Object>context)throws Exception;

	public Long getMaximumPK();
	
	public String getCommand(Map<String,Object>context);
	
	public void clear();
}
