package ir.utopia.core.struts;

import ir.utopia.core.form.annotation.LinkPage.linkToPageType;

public class UtopiaPageLinkMetaData {
	linkToPageType[] linkToPages;
	String linkedURL;
	boolean inInternal;
	String actionName;
	boolean perRecord;
	String parameters;
	String icon;
	
	public linkToPageType[] getLinkToPages() {
		return linkToPages;
	}
	public void setLinkToPages(linkToPageType[] linkToPages) {
		this.linkToPages = linkToPages;
	}
	public String getLinkedURL() {
		return linkedURL;
	}
	public void setLinkedURL(String linkedURL) {
		this.linkedURL = linkedURL;
	}
	public boolean isInInternal() {
		return inInternal;
	}
	public void setInInternal(boolean inInternal) {
		this.inInternal = inInternal;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public boolean isPerRecord() {
		return perRecord;
	}
	public void setPerRecord(boolean perRecord) {
		this.perRecord = perRecord;
	}
	public String getParameters() {
		return parameters;
	}
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
}
