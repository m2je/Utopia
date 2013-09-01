package ir.utopia.common.basicinformation.fiscalyear.action;

import ir.utopia.common.basicinformation.fiscalyear.persistent.CmFiscalyear;
import ir.utopia.core.constants.Constants.predefindedActions;
import ir.utopia.core.messagehandler.MessageHandler;
import ir.utopia.core.struts.AbstractUtopiaAction;

public class FiscalYearAction extends AbstractUtopiaAction<FiscalYearForm> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8193214439239146562L;

	protected FiscalYearForm createModelInstance() {
		return new FiscalYearForm();
	}

	@Override
	public void validate() {
		super.validate();
		if(predefindedActions.save.toString().equals(actionName)||
				predefindedActions.update.toString().equals(actionName)){
			CmFiscalyear year=(CmFiscalyear) form.convertToPersistent();
			long from = year.getStartdate().getTime();
			long to = year.getEnddate().getTime();

			// Next subtract the from date from the to date (make sure the
			// result is a double, this is needed in case of Winter and Summer
			// Time (changing the clock one hour ahead or back) the result will
			// then be not exactly rounded on days. If you use long, this slighty
			// different result will be lost.
			double difference = to - from;

			// Next divide the difference by the number of milliseconds in a day
			// (1000 * 60 * 60 * 24). Next round the result, this is needed of the
			// Summer and Winter time. If the period is 5 days and the change from
			// Winter to Summer time is in the period the result will be
			// 5.041666666666667 instead of 5 because of the extra hour. The
			// same will happen from Winter to Summer time, the result will be
			// 4.958333333333333 instead of 5 because of the missing hour. The
			// round method will round both to 5 and everything is OKE....
			long days = Math.round((difference/(1000*60*60*24)));

			// Now we can print the day difference... Try it, it also works with
			// Feb 29...
			if(days<364){
				addFieldError("enddate",MessageHandler.getMessage("EndDateLengthError",FiscalYearForm.class.getName() , getUserLocale().getLanguage()));
			}
		}
		
	}
	
}
