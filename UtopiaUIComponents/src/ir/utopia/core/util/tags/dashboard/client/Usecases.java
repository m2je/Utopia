
package ir.utopia.core.util.tags.dashboard.client;

import ir.utopia.core.util.tags.dashboard.client.model.DashboardRecord;
import ir.utopia.core.util.tags.dashboard.client.model.DocStatusInfo;
import ir.utopia.core.util.tags.dashboard.client.model.TransitionalUsecaseInfo;
import ir.utopia.core.util.tags.dashboard.client.model.UsecaseSearchCriteria;
import ir.utopia.core.util.tags.dashboard.client.model.UsecaseTransitionTreeNode;
import ir.utopia.core.util.tags.dashboard.client.model.UsecaseTransitionTreeNode.TransitionNodeType;
import ir.utopia.core.util.tags.datainputform.client.MessageUtility;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * A tree displaying a set of email folders.
 */
public class Usecases extends Composite implements DashboardModelListener{
	final DashBoardConstants constants=(DashBoardConstants)GWT.create(DashBoardConstants.class);
	private DashboardModel model;
  /**
   * Specifies the images that will be bundled for this Composite and specify
   * that tree's images should also be included in the same bundle.
   */
  public interface Images extends ClientBundle, Tree.Resources {
	 @ImageOptions(flipRtl=true)
    ImageResource inbox();

    ImageResource processed();
    
    @ImageOptions(flipRtl=true)
    ImageResource underProcess();

    ImageResource trash();
    
    @Source("noimage.png")
    ImageResource treeLeaf();
  }
  final Images images = GWT.create(Images.class);
  private Tree tree;

  /**
   * Constructs a new mailboxes widget with a bundle of images.
   * 
   * @param images a bundle that provides the images for this widget
   */
  public Usecases() {
  
   
    tree = new Tree(images);
    
	tree.addSelectionHandler(new SelectionHandler<TreeItem>() {
		
		@Override
		public void onSelection(SelectionEvent<TreeItem> event) {
			UsecaseTransitionTreeNode item=(UsecaseTransitionTreeNode) event.getSelectedItem();
			model.setCurrent( item.getUsecaseInfo());
			loadUsecaseRecords(item);
		}
	});
    initWidget(tree);
  }

//******************************************************************************************************************
  public void setModel(DashboardModel model){
	  this.model=model;
	  model.addListener(this);
  }
//******************************************************************************************************************
	@Override
  public void modelLoaded(boolean success) {
		if(success){
		List<TransitionalUsecaseInfo>transitions= model.getUsecaseTransitionInfos();
		if(transitions!=null){
			for(TransitionalUsecaseInfo info:transitions){
				TreeItem root = new UsecaseTransitionTreeNode(TransitionNodeType.root,info,null
				      );
				    tree.addItem(root);
				   
				    root.addItem(new UsecaseTransitionTreeNode(TransitionNodeType.proccessed,info,null
						      ));
				    root.addItem(new UsecaseTransitionTreeNode(TransitionNodeType.underProcess,info,null
						      ));
				    for(DocStatusInfo status:info.getDocStatusInfo()){
				    	 root.addItem(new UsecaseTransitionTreeNode(TransitionNodeType.transitionNode,info,status
							      ));
				    }
			}
		}
		}
	}
//******************************************************************************************************************
	public void loadUsecaseRecords(UsecaseTransitionTreeNode item){
		MessageUtility.progress(constants.pleaseWait(), constants.loadingData());
		UsecaseSearchCriteria criteria =new UsecaseSearchCriteria();
		switch (item.getNodeType()) {
		case root:
			
			break;
		case proccessed:
			break;
		case underProcess:
			break;
		case transitionNode:{
			criteria.setFromDocTypeId(item.getTransitionInfo().getCurrentDocStatus());
		}
			break;	
		}
		
		
		criteria.setUsecaseId(item.getUsecaseInfo().getUsecaseId());
		model.setCurrentCriteria(criteria,true);
		
	}
//******************************************************************************************************************

	@Override
	public void dashSearchCriterialChanged(UsecaseSearchCriteria searchCriteria) {
		
	}

	@Override
	public void recordUpdated(DashboardRecord record) {
		// TODO Auto-generated method stub
		
	}
	
}
