package ir.utopia.core.util.tags.datainputform.client.tree.model;

public class CheckBoxTreeData extends TreeData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9151206141404708505L;
	
	public static int CHECKBOX_TREE_VALUES_NON_SELECTED=0;
	public static int CHECKBOX_TREE_VALUES_SELECTED=1;
	public static int CHECKBOX_TREE_VALUES_HALF=2;
	protected int value;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
}
