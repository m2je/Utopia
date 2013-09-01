package ir.utopia.core.util.tags.utilitypanel.client;

public interface InnerPanel {
	
	public boolean validate();
	
	public void reset();
	
	public boolean isModified();
	
	public void reload();
	
	public void setValue(Object value);
}
