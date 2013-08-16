package ir.utopia.core.util.tags.controlpanel.client;

public interface ControlPanelTab {

		public boolean discardPanel();
		
		public void reload(boolean ignoreDirty);
		
		public void setWindowNumber(int windowNumber);
}
