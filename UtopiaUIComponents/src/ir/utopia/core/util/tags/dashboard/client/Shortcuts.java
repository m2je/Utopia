
package ir.utopia.core.util.tags.dashboard.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ExternalTextResource;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.StackLayoutPanel;

/**
 * A composite that contains the shortcut stack panel on the left side. The
 * mailbox tree and shortcut lists don't actually do anything, but serve to show
 * how you can construct an interface using
 * {@link com.google.gwt.user.client.ui.StackPanel},
 * {@link com.google.gwt.user.client.ui.Tree}, and other custom widgets.
 */

public class Shortcuts extends ResizeComposite  {

  interface Binder extends UiBinder<StackLayoutPanel, Shortcuts> { }
  private static final Binder binder = GWT.create(Binder.class);
  final DashBoardConstants constants=(DashBoardConstants)GWT.create(DashBoardConstants.class);
  DashboardModel model;
  @UiField Usecases usecases;
  
  
  /**
   * Constructs a new shortcuts widget using the specified images.
   * 
   * @param images a bundle that provides the images for this widget
   */
  public Shortcuts() {
    initWidget(binder.createAndBindUi(this));
  }
  public void setModel(DashboardModel model){
	  this.model=model;
	  usecases.setModel(model);
  }
  
 
}
