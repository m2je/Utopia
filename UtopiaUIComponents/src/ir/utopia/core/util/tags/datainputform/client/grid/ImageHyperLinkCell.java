package ir.utopia.core.util.tags.datainputform.client.grid;

import java.util.HashMap;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeUri;

public class ImageHyperLinkCell extends AbstractCell<ImageResource> {
	
	public static interface ImageHyperLinkCellCallBack{
		public void onClick(int rowIndex,int columnIndex,Object key);
	}
	private static final HashMap<Integer, ImageHyperLinkCellCallBack>callbackMap=new HashMap<Integer, ImageHyperLinkCell.ImageHyperLinkCellCallBack>();
	ImageHyperLinkCellCallBack callBack;
	String caption;
	String imageURL;
	public ImageHyperLinkCell(int columnIndex,String caption,String imageURL, ImageHyperLinkCellCallBack callBack){
		this.callBack=callBack;
		this.caption=caption;
		this.imageURL=imageURL;
		callbackMap.put(columnIndex, callBack);
		if(getNativeFunction("__imageHyperLinkClicked")==null)
			registerNativeFunctions();
	}
	
	private static Template TEMLPATE = GWT.create(Template.class); 
	public interface Template extends SafeHtmlTemplates
	{
	    @SafeHtmlTemplates.Template("<img src=\"{0}\">")
	    SafeHtml content(SafeUri image);
	}

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,
			ImageResource value, SafeHtmlBuilder sb) {
		sb.appendHtmlConstant("<a href=\"#\" title=\""+caption+"\" onclick=javascript:__imageHyperLinkClicked(\""+context.getIndex()+"\",\""+context.getColumn()+"\",\""+context.getKey()+"\")>");
		if(imageURL==null){
			SafeHtml safeHtml= TEMLPATE.content(value.getSafeUri());
			sb.append(safeHtml);
		}else{
			sb.appendHtmlConstant("<img src=\""+imageURL+"\">");
		}
		
		sb.appendHtmlConstant("</a>");
    
	}
	
//***********************************************************************
	public void registerNativeFunctions(){
		addWidgetlookupFunction(this);
	}
//***********************************************************************
	public void linkClicked(String row,String column,String key){
		callbackMap.get(Integer.parseInt(column)).onClick(Integer.parseInt(row), Integer.parseInt(column), key);
	}
//***********************************************************************
	public native void addWidgetlookupFunction(ImageHyperLinkCell x)/*-{

	$wnd.__imageHyperLinkClicked = function ( row,column,key) {

	return x.@ir.utopia.core.util.tags.datainputform.client.grid.ImageHyperLinkCell::linkClicked(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(row,column,key);

	};

	}-*/;
//***********************************************************************
	private static  JavaScriptObject getNativeFunction(String functionName){
		try {
			return getNativeFunctionN(functionName);
		} catch (Exception e) {
			return null;
		}
	}
//***********************************************************************
	private static native JavaScriptObject getNativeFunctionN(String functionName)/*-{
    return  $wnd[functionName];
	}-*/;

}
