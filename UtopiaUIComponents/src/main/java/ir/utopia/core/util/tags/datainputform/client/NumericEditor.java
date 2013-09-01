package ir.utopia.core.util.tags.datainputform.client;

import ir.utopia.core.util.tags.datainputform.client.model.InputItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.TextBox;

public class NumericEditor extends TextBox implements InputVerifier{

	DataInputFormConstants constants=GWT.create(DataInputFormConstants.class);
	InputItem item;
	NumberFormat formatter =NumberFormat.getDecimalFormat();
	public NumericEditor(InputItem item){
		this.item=item;
		init();
	}
	public void init(){
		addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				if(verify()){
					String value=getRawValue();
					if(value!=null&&value.length()>0){
						setValue(formatter.format(Double.parseDouble(makeRaw( value))));
					}
				}
				
			}
		});
	}
//*********************************************************************************************************************************	
	@Override
	public boolean verify() {
		return EditorFactory.validateNumeric(constants, item, getValue());
	}
//*********************************************************************************************************************************	
	public String getValue(){
		String result=super.getValue();
		if(result!=null&&result.length()>0){
			try {
				result=makeRaw(result);
				Double d= formatter.parse(result);
				if(item.getDecimalPrecision()==0){
					return String.valueOf(d.longValue());
				}else{
					return String.valueOf(d );
				}
			} catch (NumberFormatException e) {
				return super.getValue();
			}
		}
		return result;
	}
//*********************************************************************************************************************************
	public String getRawValue(){
		return super.getValue();
	}
//*********************************************************************************************************************************
	public String makeRaw(String result){
		result=EditorFactory.revertRightToLeft( result);
		result=result.replaceAll(",", "").replace("\u066C", "");
		return result;
	}
//*********************************************************************************************************************************
}
