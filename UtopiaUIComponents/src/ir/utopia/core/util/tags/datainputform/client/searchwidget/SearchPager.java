package ir.utopia.core.util.tags.datainputform.client.searchwidget;

import ir.utopia.core.util.tags.datainputform.client.ImageHyperlink;
import ir.utopia.core.util.tags.datainputform.client.UtopiaGWTUtil;
import ir.utopia.core.util.tags.datainputform.client.UtopiaImages;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageConstants;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageData;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;

public class SearchPager extends HorizontalPanel{
	NavigateClickListener firtListener,nextListener,previousListener,lastListener;
	protected Label fromNLabel,toNLabel,totalNLabel;
	HorizontalPanel navigatPanel;
	UtopiaImages images = (UtopiaImages) GWT.create(UtopiaImages.class);
	SearchPageConstants constants =(SearchPageConstants)GWT.create(SearchPageConstants.class);
	int loadsize=SearchPageData.DEFAULT_PAGE_SIZE;
	private static transient final int MAX_NAVIGATION_LINK_COUNT=10;
	
	public interface SeachPageListener{
		public void pagerPositionChanged( int from, int resultPerPage);
	}
	
	private List<SeachPageListener> listeners=new ArrayList<SearchPager.SeachPageListener>();
	
	public void addListener(SeachPageListener listener){
		listeners.add(listener);
	}
	ImageHyperlink first;
    ImageHyperlink prev;
	HorizontalPanel linkPanel;
	ImageHyperlink next;
	ImageHyperlink last;
	NumberField pageSteptext;
	public SearchPager(){
		  navigatPanel=new HorizontalPanel();
		  navigatPanel.setSpacing(10);
		  first=new ImageHyperlink(images.btn_First().createImage());
		  first.setTitle(constants.first());
		  linkPanel=new HorizontalPanel();
		  prev=new ImageHyperlink(images.btn_Prev().createImage());
		  prev.setTitle(constants.previous());
		  next=new ImageHyperlink(images.btn_Next().createImage());
		  next.setTitle(constants.next());
		  last=new ImageHyperlink(images.btn_Last().createImage());
		  last.setTitle(constants.last());
		  pageSteptext=new NumberField();
		  pageSteptext.setValue(SearchPageData.DEFAULT_PAGE_SIZE);
		  pageSteptext.setWidth(30);
		  pageSteptext.setDecimalPrecision(0);
		  pageSteptext.setMinValue(SearchPageData.DEFAULT_PAGE_SIZE);
		  pageSteptext.setMaxValue(200); 
		  pageSteptext.addListener(new FieldListenerAdapter(){
			  public void onSpecialKey(Field field, EventObject e) {
				  if(e.getKey()==EventObject.ENTER){
					  int newStep=((NumberField)field).getValue().intValue();
					  if(loadsize!=newStep&&newStep>0&&newStep<=200){
						  loadsize=newStep;
						  firePagerPositionChanged(0);
					  }
				  }
			    }
		  });
		  navigatPanel.add(first);
		  navigatPanel.add(prev);
		  navigatPanel.add(linkPanel);
		  navigatPanel.add(next);
		  navigatPanel.add(last);
		  Label l=new Label(constants.resultCountPerPage());
		  l.setStylePrimaryName("clsLabel");
		  navigatPanel.add(l);
		  navigatPanel.add(pageSteptext);
		  navigatPanel.getElement().setDir("LTR");
		  
		  add(navigatPanel);
		  add(getFromToPanel(0, SearchPageData.DEFAULT_PAGE_SIZE, 0));
	}
//************************************************************************************************************************
	  private HorizontalPanel getFromToPanel(int from,int step,int totalResultCount){
		  HorizontalPanel result=new HorizontalPanel();
		  result.setSpacing(10);
		  from=totalResultCount>0?from+1:0;
		  fromNLabel=new Label(UtopiaGWTUtil.getLocaleNumber(String.valueOf(from),LocaleInfo.getCurrentLocale().getLocaleName()));
		  fromNLabel.setStylePrimaryName("clsLabel");
		  result.add(fromNLabel);
		  
		  Label toLabel=new Label(constants.to());
		  toLabel.setStylePrimaryName("clsLabel");
		  result.add(toLabel);
		  
		  int to=totalResultCount>0?((from+step)>totalResultCount?totalResultCount:from+step-1):0;
		  toNLabel=new Label(UtopiaGWTUtil.getLocaleNumber(String.valueOf(to),LocaleInfo.getCurrentLocale().getLocaleName()));
		  toNLabel.setStylePrimaryName("clsLabel");
		  result.add(toNLabel);
		  
		  Label fromLabel=new Label(constants.from());
		  fromLabel.setStylePrimaryName("clsLabel");
		  result.add(fromLabel);

		  totalNLabel=new Label(UtopiaGWTUtil.getLocaleNumber(String.valueOf(totalResultCount),LocaleInfo.getCurrentLocale().getLocaleName()));
		  totalNLabel.setStylePrimaryName("clsLabel");
		  result.add(totalNLabel);

		  return result;
	  }
//************************************************************************************************************************
	  public void updateNavigation(final int from,final int totalResultCount){
		  int pageCount=totalResultCount/loadsize;
		  if(totalResultCount%loadsize>0){
			  pageCount++;
		  }
		  initNavigationFor( pageCount, from,totalResultCount);
	  }
//************************************************************************************************************************
	  private void initNavigationFor(int pageCount,int from,int totalResultCount){
			  
			  if(firtListener!=null){
				  first.removeClickListener(firtListener);
			  }
			  if(previousListener!=null){
				  prev.removeClickListener(previousListener);
			  }
			  if(lastListener!=null){
				  last.removeClickListener(lastListener);
			  }
			  if(nextListener!=null){
				  next.removeClickListener(nextListener);
			  }
			  int currentpage=(from/loadsize)+1;
			  if(pageCount>=1){
				  if(from>=1){
					  previousListener=new NavigateClickListener( from-loadsize);
					  prev.addClickListener(previousListener);
					  firtListener=new NavigateClickListener(0);
					  first.addClickListener(firtListener);
				  }
				  if(currentpage<pageCount){
					  nextListener=new NavigateClickListener(from+loadsize);
					  next.addClickListener(nextListener);
					  lastListener=new NavigateClickListener((pageCount-1)*loadsize);
					  last.addClickListener(lastListener);
				  }	
				  linkPanel.setSpacing(5);
				  from =totalResultCount>0?from+1:0;
				  fromNLabel.setText(UtopiaGWTUtil.getLocaleNumber(String.valueOf(from),LocaleInfo.getCurrentLocale().getLocaleName()));
				  toNLabel.setText(UtopiaGWTUtil.getLocaleNumber(String.valueOf(((from+loadsize)>totalResultCount?totalResultCount:from+loadsize)),LocaleInfo.getCurrentLocale().getLocaleName()));
				  totalNLabel.setText(UtopiaGWTUtil.getLocaleNumber(String.valueOf(totalResultCount),LocaleInfo.getCurrentLocale().getLocaleName()));
				  initLinkPanel( pageCount, currentpage, loadsize);
			  }else{
				  fromNLabel.setText("0");
				  toNLabel.setText("0");
				  totalNLabel.setText("0");
				  initLinkPanel( pageCount, currentpage, loadsize);
			  }
	  }
//************************************************************************************************************************
	 private void initLinkPanel(int pageCount,int currentpage,int step){
		  for(int i=linkPanel.getWidgetCount();i>0;i--)
			  linkPanel.remove(0);
		  int midel=MAX_NAVIGATION_LINK_COUNT/2;
		  boolean isSequenced=false;
		  int start=0;
		  int end=pageCount>MAX_NAVIGATION_LINK_COUNT?MAX_NAVIGATION_LINK_COUNT:pageCount;
		  if(pageCount>MAX_NAVIGATION_LINK_COUNT&&currentpage>midel){
			  start=currentpage-midel;
			  end=currentpage+midel;
			  end=end>pageCount?pageCount:end;
			  Label l=new Label("...");
			  l.setStylePrimaryName("clsLabel");
			  linkPanel.add(l);
		  }
		  isSequenced=pageCount>MAX_NAVIGATION_LINK_COUNT&end<pageCount;
		  for(int i=start;i<end;i++){
			  int index=i+1;
				String ltext=String.valueOf(index);
				ltext=UtopiaGWTUtil.getLocaleNumber(ltext,LocaleInfo.getCurrentLocale().getLocaleName());
				Widget link=(index==currentpage)?new Label(ltext): new Hyperlink(ltext,"");
					if(index!=currentpage){
						((Hyperlink)link).addClickHandler(new NavigateClickListener(i*step));
						link.setStylePrimaryName("clsLabel");
					}else{
						((Label)link).setStylePrimaryName("clsSearchCurrentPage");
					}
				linkPanel.add(link);
			}  
		  if(isSequenced){
			  Label l=new Label("...");
			  l.setStylePrimaryName("clsLabel");
			  linkPanel.add(l);
		  }
	  }
//************************************************************************************************************************
	  private class NavigateClickListener implements ClickHandler,ClickListener{
		int from;
		public NavigateClickListener(int from) {
			super();
			this.from = from;
		}

		public void onClick(Widget sender) {
			firePagerPositionChanged(from);
		}

		@Override
		public void onClick(ClickEvent event) {
			firePagerPositionChanged(from);
		}
		  
	  }
//************************************************************************************************************************
	  protected void firePagerPositionChanged(int position){
		  for(SeachPageListener listener:listeners){
			  listener.pagerPositionChanged(position,loadsize);
		  }
	  }
//************************************************************************************************************************

}
