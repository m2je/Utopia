package ir.utopia.core.form.annotation;

import ir.utopia.core.util.tags.datainputform.client.model.InputItem;

public enum NativeEventType {

	onclick,onMouseOver,onValueChanged,onKeyReleased;
	public  int getDataInputKey(){
		switch (this) {
		case onclick:
			return InputItem.TRIGGER_EVENT_TYPE_ON_CLICK;
		case onMouseOver:
			return InputItem.TRIGGER_EVENT_TYPE_ON_MOUSE_OVER;
		case onValueChanged:
			return InputItem.TRIGGER_EVENT_TYPE_ON_VALUE_CHANGED;
		case onKeyReleased:
			return InputItem.TRIGGER_EVENT_TYPE_ON_KEY_RELEASED;
		}
		return -1;
	}
}
