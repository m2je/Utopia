package ir.utopia.core.util.tags.controlpanel.client.widgets;

import ir.utopia.core.util.tags.controlpanel.client.ControlPanel;
import ir.utopia.core.util.tags.controlpanel.client.ControlPanelConstants;
import ir.utopia.core.util.tags.controlpanel.client.ControlPanelServerService;
import ir.utopia.core.util.tags.controlpanel.client.ControlPanelTab;
import ir.utopia.core.util.tags.controlpanel.client.model.ControlPanelService;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.datainputform.client.tree.CheckBoxTree;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.FormPanel;

public class RoleOrganizationAccessPanel extends FormPanel implements ControlPanelTab{
	ControlPanelConstants constants ;
	protected  SimpleStore rolesStroe = new SimpleStore(new String []{"key","values"},new String[0][0]);
	
	protected ComboBox rolesBox;
	protected CheckBoxTree treeBox=new CheckBoxTree("root");
	@Override
	public boolean discardPanel() {
		return false;
	}

	@Override
	public void reload(boolean ignoreDirty) {
		
	}

	@Override
	public void setWindowNumber(int windowNumber) {
		
	}
	
	public RoleOrganizationAccessPanel(ControlPanelConstants constants ){
		this.constants=constants;
		create();
	}
	
	public void create(){
		createRoleListBox();
		createOrganizationCheckBoxTree();
		add(rolesBox);
		add(treeBox);
		setMargins(10);
		setPaddings(10);
		updateRolesList();
		reloadOrganizationTree();
	}
	private void createOrganizationCheckBoxTree(){
		
	}
	
	private void reloadOrganizationTree(){
		
//		organizationTree.removeItem(organizationTree.get)
		
	}
	private void updateRolesList() {
		ControlPanel.loadingProgress((ControlPanelConstants)constants);
		ControlPanelServerService.getServer().loadData(ControlPanelService.LIST_TYPE_ROLE, new AsyncCallback<UILookupInfo>() {
			
			public void onFailure(Throwable caught) {
				ControlPanel.hideLoading();
				Window.alert(constants.failToAccessServer());
			}

			public void onSuccess(UILookupInfo result) {
				ControlPanel.hideLoading();
				if(result==null){
					Window.alert(constants.failToAccessServer());
				}else{
					rolesStroe.removeAll();
					String [][]data=result.getData();
					if(data!=null&&data.length>0)
						{
							
							StringFieldDef []defs=new StringFieldDef[]{new StringFieldDef("key"),new StringFieldDef("value")};
							
							RecordDef rdef=new RecordDef(defs);
							
							for(int i=0;i<data.length;i++){
								rolesStroe.add(rdef.createRecord(data[i]));
							}
					}
				}
				
			}
		});
	}
	
	
	private void createRoleListBox(){
		rolesStroe.load();
		rolesBox = new ComboBox();  
		rolesBox.setDisplayField("value");  
		rolesBox.setStore(rolesStroe);
		rolesBox.setMode(ComboBox.LOCAL);  
		rolesBox.setTriggerAction(ComboBox.ALL);  
		rolesBox.setForceSelection(true);  
        rolesBox.setValueField("key");  
        rolesBox.setForceSelection(true);  
        rolesBox.setMinChars(1);  
        rolesBox.setEmptyText(constants.selectRole());  
        rolesBox.setTypeAhead(true);  
        rolesBox.setHideTrigger(false); 
        rolesBox.setName("userId");
        rolesBox.setValueField("key");
        rolesBox.setTitle(constants.role());
        rolesBox.setLabel(constants.role());
	}
}
