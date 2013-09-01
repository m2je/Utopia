package ir.utopia.core.struts;

import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.persistent.UtopiaBasicPersistent;

public class AbstractUtopiaLanguageSupportForm<P extends UtopiaBasicPersistent> extends AbstractUtopiaBasicForm<P> implements LanguageSupportForm{
	Long language;
	
	@Override
	@InputItem(index=0)
	public void setLanguage(Long language) {
		this.language=language;
	}

	@Override
	public Long getLanguage() {
		return language;
	}

}
