package ir.utopia.core.util.tags.datainputform.client.bubble;

import java.util.ArrayList;
import java.util.List;

import ir.utopia.core.util.tags.datainputform.client.ImageHyperlink;
import ir.utopia.core.util.tags.datainputform.client.UtopiaImages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class BubbleText extends HorizontalPanel{
	 UtopiaImages images = (UtopiaImages) GWT.create(UtopiaImages.class);
	 Long id;
	 String text;
	 List<BubbleTextListener>listeners;
	@SuppressWarnings("deprecation")
	public BubbleText(Long id,String text){
		this.id=id;
		this.text=text;
		setStylePrimaryName("bubble");
		ImageHyperlink l=new ImageHyperlink(images.remove_bubble().createImage());
		add(l);
		add(new Label(text));
		l.addClickListener(new ClickListener() {
			@Override
			public void onClick(Widget sender) {
				fireBubbleClicked();
			}
		});
	}
//**********************************************************************************************
	private void fireBubbleClicked(){
		if(listeners!=null){
			for(BubbleTextListener listner:listeners){
				listner.bubbleDroped(id, text);
			}
		}
	}
//**********************************************************************************************
	public void addListener(BubbleTextListener listener){
		if(listeners==null){
			listeners=new ArrayList<BubbleTextListener>();
		}
		listeners.add(listener);
	}
//**********************************************************************************************
	public void removeListener(BubbleTextListener listener){
		if(listeners!=null){
			listeners.remove(listener);
		}
	}
//**********************************************************************************************
	public String getText(){
		return text;
	}
//**********************************************************************************************
}
