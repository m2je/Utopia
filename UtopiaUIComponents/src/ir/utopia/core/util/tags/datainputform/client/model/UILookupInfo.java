package ir.utopia.core.util.tags.datainputform.client.model;

import java.util.Arrays;
import java.util.Comparator;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UILookupInfo implements IsSerializable {

	String columns[];
	String [][]data;
	String emptyName;
	String selectedId;
	String []selectedIds;
	public String getSelectedId() {
		return selectedId;
	}
	public void setSelectedId(String selectedId) {
		this.selectedId = selectedId;
	}
	int displayColumnIndex=1;
	
	public String getEmptyName() {
		return emptyName;
	}
	public void setEmptyName(String emptyName) {
		this.emptyName = emptyName;
	}
	public String[] getColumns() {
		return columns;
	}
	public void setColumns(String ...columns) {
		this.columns = columns;
	}
	public String[][] getData() {
		return data;
	}
	public void setData(String [][]data,boolean sort) {
		if(sort){
			Arrays.sort(data,new Comparator<String[]>(){

				public int compare(String[] o1, String[] o2) {
					if(o1 == null){
						if(o2==null){
							return 0;
						}
						return -1;
					}
					return o1[displayColumnIndex].compareTo(o2[displayColumnIndex]);
				}
				
			});
		}
		this.data = data;

	}
	public void setData(String [][]data) {
		setData(data,true);
	}
	public UILookupInfo(){
		
	}
	public UILookupInfo(String[]columns,String [][]data){
		setData(data);
		this.columns=columns;
	}
	public UILookupInfo(String[]columns,String [][]data,int displayColumnIndex){
		this(columns,data,displayColumnIndex,true);
	}
	public UILookupInfo(String[]columns,String [][]data,int displayColumnIndex,boolean sortData){
		setDisplayColumnIndex(displayColumnIndex);
		setData(data,sortData);
		this.columns=columns;
	}
	public String getKeyColumn(){
		return columns[0];
	}
	public String getDisplayColumn(){
		return columns[displayColumnIndex];
	}
	public int getDisplayColumnIndex() {
		return displayColumnIndex;
	}
	public void setDisplayColumnIndex(int displayColumnIndex) {
		this.displayColumnIndex = displayColumnIndex;
	}
	public String[] getSelectedIds() {
		return selectedIds;
	}
	public void setSelectedIds(String[] selectedIds) {
		this.selectedIds = selectedIds;
	}
	
}
