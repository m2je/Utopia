package ir.utopia.core.util.tags.datainputform.client;

import ir.utopia.core.util.tags.datainputform.client.grid.IncludedGrid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractDataInputFormContainer extends
		AbstractUtopiaGWTEditor implements DataInputFormContainer {
	protected DataInputFormWidget datainputFormWidget;
//***********************************************************************************	
	@Override
	public void registerNativeFunctions() {
		super.registerNativeFunctions();
		registerGridFunctions(this);
	}
//***********************************************************************************
	protected native void registerGridFunctions(AbstractDataInputFormContainer x)/*-{
		$wnd.addRowToGrid = function ( t,d) {

		return x.@ir.utopia.core.util.tags.datainputform.client.AbstractDataInputFormContainer::addRowToGrid(Ljava/lang/String;Lcom/google/gwt/core/client/JsArrayString;)(t,d);

		};
		$wnd.removeRowFromGrid = function ( t,d) {

		return x.@ir.utopia.core.util.tags.datainputform.client.AbstractDataInputFormContainer::removeRowFromGrid(Ljava/lang/String;Ljava/lang/String;)(t,d);

		};
		$wnd.removeRowsFromGrid = function ( t,d) {

		return x.@ir.utopia.core.util.tags.datainputform.client.AbstractDataInputFormContainer::removeRowsFromGrid(Ljava/lang/String;Lcom/google/gwt/core/client/JsArrayInteger;)(t,d);

		};
		$wnd.resetGrid = function ( t) {

		return x.@ir.utopia.core.util.tags.datainputform.client.AbstractDataInputFormContainer::resetGrid(Ljava/lang/String;)(t);

		};
		$wnd.getRowFromGrid = function ( t,d) {

		return x.@ir.utopia.core.util.tags.datainputform.client.AbstractDataInputFormContainer::getRowFromGrid(Ljava/lang/String;Ljava/lang/String;)(t,d);

		};
		$wnd.setGridButtonEnable = function (id,btId,status) {

		 x.@ir.utopia.core.util.tags.datainputform.client.AbstractDataInputFormContainer::setGridButtonEnable(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(id,btId,status);

		};
		$wnd.setGridHiddenValue = function (id,index,hiddenName,hiddenValue) {

		 x.@ir.utopia.core.util.tags.datainputform.client.AbstractDataInputFormContainer::setGridHiddenValue(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(id,index,hiddenName,hiddenValue);

		};
		$wnd.getGridHiddenValue = function (id,index,hiddenName) {

		return x.@ir.utopia.core.util.tags.datainputform.client.AbstractDataInputFormContainer::getGridHiddenValue(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(id,index,hiddenName);

		};
		
		$wnd.validate = function () {

		return x.@ir.utopia.core.util.tags.datainputform.client.AbstractDataInputFormContainer::validate()();

		};
		
		$wnd.submit= function (withRevision) {

		return x.@ir.utopia.core.util.tags.datainputform.client.AbstractDataInputFormContainer::submit(Ljava/lang/String;)(withRevision);

		};
		
		$wnd.setSubmitURL = function (submitURL) {

			x.@ir.utopia.core.util.tags.datainputform.client.AbstractDataInputFormContainer::setSubmitURL(Ljava/lang/String;)(submitURL);

		};
		
		
		
		}-*/;
//***********************************************************************************
	protected void addRowToGrid(String gridId,JsArrayString data){
		try {
			IncludedGrid grid=findIncludedGrid(gridId);
			if(grid!=null){
				String []row=new String[data.length()];
				for(int i=0;i<row.length;i++){
					row[i]=data.get(i);
				}
				grid.addRow(row);
			}
		} catch (Exception e) {
			GWT.log(e.toString());
			e.printStackTrace();
		}
	}
//***********************************************************************************
	protected void removeRowFromGrid(String gridId,String rowIndex){
		try {
			IncludedGrid grid=findIncludedGrid(gridId);
			if(grid!=null){
				grid.removeRows(Integer.parseInt(rowIndex));
			}
		} catch (Exception e) {
			GWT.log(e.toString());
			e.printStackTrace();
		}
	}
//***********************************************************************************
	protected void removeRowsFromGrid(String gridId,JsArrayInteger rowIndexes){
		try {
			IncludedGrid grid=findIncludedGrid(gridId);
			if(grid!=null){
				int []rowIndexesI=new int[rowIndexes.length()];
				for(int i=0;i<rowIndexesI.length;i++){
					rowIndexesI[i]=rowIndexes.get(i);
				}
				grid.removeRows(rowIndexesI);
			}
		} catch (Exception e) {
			GWT.log(e.toString());
			e.printStackTrace();
		}
	}
//***********************************************************************************
	protected void setGridButtonEnable(String gridId,String buttongId,String enable ){
		try {
			IncludedGrid grid=findIncludedGrid(gridId);
			if(grid!=null){
				if("add".equalsIgnoreCase(buttongId)){
					grid.setAddRecordEnable(Boolean.parseBoolean(enable));
				}else if("remove".equalsIgnoreCase(buttongId)){
					grid.setRemoveRecordEnable(Boolean.parseBoolean(enable));
				}
				
			}
		} catch (Exception e) {
			GWT.log(e.toString());
			e.printStackTrace();
		}
	}
//***********************************************************************************
		protected void setGridHiddenValue(String gridId,String rowIndex,String hiddenName,String value){
			try {
				IncludedGrid grid=findIncludedGrid(gridId);
				if(grid!=null){
					grid.setHiddenValue(Integer.parseInt(rowIndex), hiddenName, value);
				}
			} catch (Exception e) {
				GWT.log(e.toString());
				e.printStackTrace();
			}
		}
//***********************************************************************************
		protected String getGridHiddenValue(String gridId,String rowIndex,String hiddenName){
			try {
				IncludedGrid grid=findIncludedGrid(gridId);
				if(grid!=null){
					return grid.getHiddenValue(Integer.parseInt(rowIndex), hiddenName);
				}
			} catch (Exception e) {
				GWT.log(e.toString());
				e.printStackTrace();
			}
			return null;
		}
		//***********************************************************************************
		protected void resetGrid(String gridId){
				IncludedGrid grid=findIncludedGrid(gridId);
				if(grid!=null){
					grid.reset();
				}
			}
//***********************************************************************************
	/**
	 * returns a native array from grid row , to call use something like:
	 * <B>getRowFromGrid('gridId','0')</B>
	 * @param gridId
	 * @param rowIndexStr
	 * @return
	 */
	protected JsArrayString getRowFromGrid(String gridId,String rowIndexStr){
		try {
			IncludedGrid grid=findIncludedGrid(gridId);
			int rowIndex=Integer.parseInt(rowIndexStr);
			if(grid!=null){
				String []row=grid.getRow(rowIndex);
				JsArrayString result= JavaScriptObject.createArray().cast();
				for(int i=0;i<row.length;i++){
					result.set(i,row[i]);
				}
				return result;
			}
		} catch (Exception e) {
			GWT.log(e.toString());
			e.printStackTrace();
		}
		return null;
	}
//***********************************************************************************
	protected IncludedGrid findIncludedGrid(String gridId){
		Widget w= widgetsMap.get(gridId);
		if(w instanceof IncludedGrid){
			return (IncludedGrid)w;
		}
		GWT.log("invalid id for grid:"+gridId);
		return null;

	}
//***********************************************************************************
	protected boolean validate(){
		return datainputFormWidget.validate();
	}
//***********************************************************************************
	protected void submit(String showRevivision){
		 datainputFormWidget.submit(Boolean.parseBoolean(showRevivision));
	}
//***********************************************************************************
	protected void setSubmitURL(String submitURL){
		datainputFormWidget.setSubmitURL(submitURL);
	}
}
