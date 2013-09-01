package ir.utopia.core.util.tags.datainputform.client.lov;

import java.util.List;
import java.util.Set;

public interface LOVListener  {

	public void valueChanged(LOVWidget source, Long key,String value);
	
	public void valueChanged(LOVWidget source, Set<Long> keys,List<String> values);
	
	public void LovWindowStatusChanged(LOVWindowEvent event);
}
