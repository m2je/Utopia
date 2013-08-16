package ir.utopia.core.util.tags.datainputform.client.lov;

public class LOVWindowEvent {

	public static final int WINDOW_STATUS_OPENED=1;
	public static final int WINDOW_STATUS_CLOSED=0;
	
	public int windowStatus;

	public int getWindowStatus() {
		return windowStatus;
	}

	public void setWindowStatus(int windowStatus) {
		this.windowStatus = windowStatus;
	}
	
}
