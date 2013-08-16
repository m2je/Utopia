package ir.utopia.core.util.tags.datainputform.client.tree.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TreeNode implements Serializable,IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -699039995708640578L;
	Long nodeId;
	String name;
	String description;
	Long parentId;
	String code;
	TreeNode [] children;
	int level =0 ;
	int	 childrenCount=0;
	String imageIcon;
	
	public TreeNode(Long nodeId, String name, String description,String code, Long parentId) {
		super();
		this.nodeId = nodeId;
		this.name = name;
		this.description = description;
		this.parentId = parentId;
		this.code =code;
	}
	public TreeNode(Long nodeId, String name, String description,String code, Long parentId,int level) {
		super();
		this.nodeId = nodeId;
		this.name = name;
		this.description = description;
		this.parentId = parentId;
		this.code =code;
		this.level =level;
	}
	public TreeNode() {
		// TODO Auto-generated constructor stub
	}

	public Long getNodeId() {
		return nodeId;
	}
	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public TreeNode[] getChildren() {
		return children;
	}
	public void setChildren(TreeNode[] children) {
		this.children = children;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	public int getChildrenCount() {
		return childrenCount;
	}
	public void setChildrenCount(int childrenCount) {
		this.childrenCount = childrenCount;
	}
	public String getImageIcon() {
		return imageIcon;
	}
	public void setImageIcon(String imageIcon) {
		this.imageIcon = imageIcon;
	}
	
	
	
	
}
