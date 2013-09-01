package ir.utopia.core.zeroconfiguration.sequence.action;

import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.struts.AbstractUtopiaForm;
import ir.utopia.core.zeroconfiguration.sequence.persistent.CoSequence;

@PersistedMapForm
@DataInputForm
public class SequenceForm  extends AbstractUtopiaForm<CoSequence>{

	private Long coSequenceId;
	private String tablename;
	private Long cmSubsystemId;
	private Long currentid;
	private String subsysName;

	public SequenceForm(){
		
	}

	@FormId
	@FormPersistentAttribute
	public Long getCoSequenceId() {
		return coSequenceId;
	}

	public void setCoSequenceId(Long coSequenceId) {
		this.coSequenceId = coSequenceId;
	}

	@SearchItem
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=1)
	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_ITEM)
	@FormPersistentAttribute(method="getCmSubsystem")
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=3)
	public Long getCmSubsystemId() {
		return cmSubsystemId;
	}

	public void setCmSubsystemId(Long cmSubsystemId) {
		this.cmSubsystemId = cmSubsystemId;
	}

	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getSubsysName() {
		return subsysName;
	}

	public void setSubsysName(String subsysName) {
		this.subsysName = subsysName;
	}

	@SearchItem
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=2)
	public Long getCurrentid() {
		return currentid;
	}

	public void setCurrentid(Long currentid) {
		this.currentid = currentid;
	}

}
