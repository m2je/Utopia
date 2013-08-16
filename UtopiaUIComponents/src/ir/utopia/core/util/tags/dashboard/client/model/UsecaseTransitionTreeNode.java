package ir.utopia.core.util.tags.dashboard.client.model;

import ir.utopia.core.util.tags.dashboard.client.DashBoardConstants;
import ir.utopia.core.util.tags.dashboard.client.Usecases.Images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.TreeItem;

public class UsecaseTransitionTreeNode extends TreeItem{
	public static final Images images = GWT.create(Images.class);
	public static final DashBoardConstants constants=(DashBoardConstants)GWT.create(DashBoardConstants.class);
	public enum TransitionNodeType{
		root,proccessed,underProcess,transitionNode;
	}
	TransitionNodeType nodeType;
	TransitionalUsecaseInfo usecaseInfo;
	DocStatusInfo transitionInfo;
	
	
	public UsecaseTransitionTreeNode(TransitionNodeType nodeType,
			TransitionalUsecaseInfo usecaseInfo, DocStatusInfo transitionInfo) {
		super();
		switch (nodeType) {
		
		case root:{
			  setHTML(imageItemHTML(images.inbox(), usecaseInfo.getUseCaseHeader()));
		}
			
			break;
		case proccessed:{
			setHTML(imageItemHTML(images.processed(),constants.processed() ));
		}	break;
		case underProcess:{
			setHTML(imageItemHTML(images.underProcess(),constants.underProcessed() ));
		}	break;
		case transitionNode:{
			setHTML(imageItemHTML(images.underProcess(),transitionInfo.getName() ));
			setTitle(transitionInfo.getDesciption());
		}
			break;
		}
		this.nodeType = nodeType;
		this.usecaseInfo = usecaseInfo;
		this.transitionInfo = transitionInfo;
	}
	 
	  private String imageItemHTML(ImageResource imageProto, String title) {
	    return AbstractImagePrototype.create(imageProto).getHTML() + " " + title;
	  }
	public TransitionalUsecaseInfo getUsecaseInfo() {
		return usecaseInfo;
	}
	public void setUsecaseInfo(TransitionalUsecaseInfo usecaseInfo) {
		this.usecaseInfo = usecaseInfo;
	}
	public DocStatusInfo getTransitionInfo() {
		return transitionInfo;
	}
	public void setTransitionInfo(DocStatusInfo transitionInfo) {
		this.transitionInfo = transitionInfo;
	}
	public TransitionNodeType getNodeType() {
		return nodeType;
	}
	public void setNodeType(TransitionNodeType nodeType) {
		this.nodeType = nodeType;
	}
	
}
