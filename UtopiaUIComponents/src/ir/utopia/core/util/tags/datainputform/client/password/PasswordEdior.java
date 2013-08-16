package ir.utopia.core.util.tags.datainputform.client.password;

import ir.utopia.core.util.tags.datainputform.client.DataInputFormConstants;
import ir.utopia.core.util.tags.datainputform.client.EditorFactory;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;

public class PasswordEdior extends SimplePanel  {

	
	private InputItem item;
	PasswordTextBox first,secound;
	Label firstLabel,secounLabel;
	DataInputFormConstants constants=(DataInputFormConstants)GWT.create(DataInputFormConstants.class);
	public PasswordEdior(InputItem item,String direction) {
		this.item=item;
		creat(direction);
	}
	
	private void creat(String direction){
		HorizontalPanel panel=new HorizontalPanel();
		panel.setSpacing(5);
		firstLabel= EditorFactory.createLable(item, direction,true);
		panel.add(firstLabel) ;
		first= new PasswordTextBox();
		first.setStylePrimaryName("clsText");
		first.setName(item.getColumnName());
		panel.add(first);
		String orginalLable=firstLabel.getText();
		if(orginalLable.endsWith(":")){
			orginalLable=orginalLable.replace(":", "");
		}
		if(orginalLable.endsWith("*")){
			orginalLable=orginalLable.replace("*", "");
		}
		secounLabel=new Label(constants.passwordRepeat()+" "+orginalLable +"*:");
		secounLabel.setStyleName("clsLabel");
		panel.add(secounLabel);
		secound= new PasswordTextBox();
		secound.setStylePrimaryName("clsText");
		secound.setName(item.getColumnName()+"_confirm");
		panel.add(secound);
		add(panel);
	}
	
	public String getPassword(){
		Object value=EditorFactory.getWidgetValue(first);
		return value==null?null:value.toString().trim();
	}
	
	public String getConfirmValue(){
		 Object value=EditorFactory.getWidgetValue(secound);
			return value==null?null:value.toString().trim();
	}
	
	public void markasError(){
		firstLabel.setStyleName("clsErrorLabel");
		secounLabel.setStyleName("clsErrorLabel");
	}
	public void clearError(){
		firstLabel.setStyleName("clsLabel");
		secounLabel.setStyleName("clsLabel");
	}
	
	public void setValue(String value){
		first.setText(value);
		secound.setText(value);
	}

	public PasswordTextBox getFirst() {
		return first;
	}

	public PasswordTextBox getSecound() {
		return secound;
	}

	public Label getFirstLabel() {
		return firstLabel;
	}

	public Label getSecounLabel() {
		return secounLabel;
	}
	
	public boolean validate(){
		String pass=getPassword();
		if(pass==null||pass.trim().length()==0){
			Window.alert( constants.invalidPassword());
			markasError();
			return false;
		}
		String conf=getConfirmValue();
		if(pass==null&&conf==null|| !(pass!=null&&pass.matches(conf)))
			{
			Window.alert(constants.passwordMissMathError()  );
			markasError();
			return false;
			}
		clearError();
		return true;
	}
	
	public String getName(){
		return item.getColumnName();
	}
	
	public String getValue(){
		return first.getValue();
	}
}
