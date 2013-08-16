package ir.utopia.core.util.tags.datainputform.client.tree;

import ir.utopia.core.util.tags.datainputform.client.tree.model.CheckBoxTreeData;

import java.util.HashMap;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

public class CheckBoxTree extends SimplePanel{
	Tree tree = new Tree();
    protected TreeItem root ;
    ScrollPanel treeWrapper = new ScrollPanel(tree);
    protected HashMap<Long,TreeItem>boxMap=new HashMap<Long, TreeItem>();
    public CheckBoxTree(String root){
    	this(root,false);
    }
    public CheckBoxTree(String rootString,boolean rootcheckBox){
    	tree.setAnimationEnabled(true);
	    tree.ensureDebugId("cwTree-staticTree");
	    if(rootcheckBox){
	    	root= tree.addItem("root");
	    }else{
	    	CheckBox box= new CheckBox(rootString);
	    	root= tree.addItem(box);
	    }
	    
		
	    add(treeWrapper);
    }
    
    public void addItem(CheckBoxTreeData node){
    	addItem(node.getText(),node.getId(),node.getParentId());
    }
    
    public void addItem(String text,Long id,Long parentId){
    	 CheckBox ch=new CheckBox(text);
		 TreeItem parent= boxMap.get(parentId);
		 if(parent==null){
			 parent=root;
		 }
		 TreeItem item=  parent.addItem(ch);
		 boxMap.put(id,item);
    }
    
    public TreeItem getTreeItem(Long id){
    	return boxMap.get(id);
    }
    
    public CheckBox getCheckBox(Long id){
    	if( boxMap.get(id)!=null){
    		return (CheckBox)boxMap.get(id).getWidget();
    	}
    	return null;
    }
    
    public int getValueOf(Long id){
    	CheckBox box= getCheckBox(id);
    	if(box!=null){
    		if(box.isEnabled()){
    			
    		}
    	}
    	return 0;
    }
}
