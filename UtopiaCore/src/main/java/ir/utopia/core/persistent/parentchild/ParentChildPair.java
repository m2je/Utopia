package ir.utopia.core.persistent.parentchild;

import ir.utopia.core.bean.EntityPair;
import ir.utopia.core.persistent.lookup.model.NamePair;

public class ParentChildPair extends NamePair{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4282367724307271111L;
	private Long childrenCount;
	public ParentChildPair(String name, Long key,Long childrenCount) {
		super(name, key);
		setChildrenCount(childrenCount);
	}
	public ParentChildPair(EntityPair[]entityPair,Object[]values,String separator,int keyIndex,int descriptionIndex,Long childrenCount){
		super(entityPair,values,separator,keyIndex,descriptionIndex);
		setChildrenCount(childrenCount);
	}
	public ParentChildPair(String name, Long key) {
		super(name, key);
		setChildrenCount(0l);
	}
	public Long getChildrenCount() {
		return childrenCount;
	}
	public void setChildrenCount(Long childrenCount) {
		this.childrenCount = childrenCount;
	}

	
}
