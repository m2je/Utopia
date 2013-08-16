package ir.utopia.core.util.tags.datainputform.client.tree.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TreeData implements Serializable,IsSerializable{

	protected long id;
	protected long parentId;
	protected String text;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getParentId() {
		return parentId;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	
}
