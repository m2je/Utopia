package ir.utopia.core.util.tags.dashboard.client.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DashboardFilter implements Serializable,IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7393437466252111905L;

	private long dashboardId;
	private int orderbyField;
	private int orderbyDirection;
	public long getDashboardId() {
		return dashboardId;
	}
	public void setDashboardId(long dashboardId) {
		this.dashboardId = dashboardId;
	}
	public int getOrderbyField() {
		return orderbyField;
	}
	public void setOrderbyField(int orderbyField) {
		this.orderbyField = orderbyField;
	}
	public int getOrderbyDirection() {
		return orderbyDirection;
	}
	public void setOrderbyDirection(int orderbyDirection) {
		this.orderbyDirection = orderbyDirection;
	}
	
}
