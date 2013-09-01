package ir.utopia.core.util.tags.datainputform.client.tree.model;

import java.util.ArrayList;
import java.util.List;

public class TreeModel {
	
	private String name;
	private int id ;
	private TreeModel parent;
	private String code;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public TreeModel getParent() {
		return parent;
	}
	public void setParent(TreeModel parent) {
		this.parent = parent;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public static TreeModel rootTree(){
		TreeModel  tt =new TreeModel();
		tt.setId(1);
		tt.setName("Group");
		tt.setCode("Group Code");
		return tt;
	}
	
	
	public static List<TreeModel> createMokeObject(){
		List<TreeModel> items =new ArrayList<TreeModel>();
		TreeModel  tt =new TreeModel();
		tt.setId(1);
		tt.setName("Group");
		tt.setCode("Group Code");
		//items.add(tt);
		for (int i = 0; i <20; i++) {
			TreeModel  t2 =new TreeModel();
			t2.setId(i);
			t2.setName("col"+i);
			t2.setCode("col Code "+i);
			t2.setParent(tt);
			
			items.add(t2);
		}
		
		
		return items;
	}
	
	
	

}
