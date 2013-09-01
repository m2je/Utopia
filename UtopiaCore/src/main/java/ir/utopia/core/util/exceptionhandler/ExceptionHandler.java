package ir.utopia.core.util.exceptionhandler;

import java.util.Map;

public interface ExceptionHandler {

	public ExceptionResult handel(Throwable e,Map<String,Object>context);
}
