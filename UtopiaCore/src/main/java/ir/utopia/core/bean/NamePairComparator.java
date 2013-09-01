package ir.utopia.core.bean;

import ir.utopia.core.persistent.lookup.model.NamePair;

import java.util.Comparator;

public class NamePairComparator implements Comparator< NamePair> {

	@Override
	public int compare(NamePair o1, NamePair o2) {
		String name1=o1.getName().toString();
		String name2=o2.getName().toString();
		if(name1==null)return -1;
		return name1.compareTo(name2);
	}

}
