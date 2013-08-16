package ir.utopia.core.zeroconfiguration.sequence.action;

import ir.utopia.core.struts.AbstractUtopiaAction;

public class SequenceAction extends AbstractUtopiaAction<SequenceForm> {



	/**
	 * 
	 */
	private static final long serialVersionUID = 8479895728784386849L;

	protected SequenceForm createModelInstance() {
		return new SequenceForm();
	}
}
