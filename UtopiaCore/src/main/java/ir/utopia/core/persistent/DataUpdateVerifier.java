package ir.utopia.core.persistent;

public interface DataUpdateVerifier {

	public void verify(Object oldValue,Object newValue) throws Exception;
}
