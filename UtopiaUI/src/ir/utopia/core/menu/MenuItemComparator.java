package ir.utopia.core.menu;

import ir.utopia.core.basicinformation.menu.MenuItem;
import ir.utopia.core.basicinformation.menu.persistent.CoVSystemMenu;

import java.util.Comparator;

public class MenuItemComparator implements Comparator<Object> {

	@Override
	public int compare(Object o1, Object o2) {
		long ret= 0;
		if(o1 instanceof CoVSystemMenu){
			CoVSystemMenu s1 = (CoVSystemMenu)o1;
			CoVSystemMenu s2 = (CoVSystemMenu)o2;
			 
			ret = s1.getPrecedence() - s2.getPrecedence();
		}else
		if(o1 instanceof MenuItem){
			MenuItem s1 = (MenuItem)o1;
			MenuItem s2 = (MenuItem)o2;
			 
			ret = s1.getPrecedence() - s2.getPrecedence();
		}

		if(ret == 0L)
			return 0;
		return ret>0?1:-1;
	}

}
