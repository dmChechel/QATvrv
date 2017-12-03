package order;

import java.util.Date;
import java.util.LinkedList;


/**
 * The whole order consisting of single items.
 * 
 * @author Dan Tavrov
 *
 */
public class Order {
	//list of all items in the order
	private LinkedList<OrderItem> orderItems;
	
	//shipment type
	private ShipmentType shipmentType;
	
	//date when the order was placed
	private Date date;
	
	public Order(){
		this.orderItems = new LinkedList<>();
	}
	
	public Order(LinkedList<OrderItem> orderItems, ShipmentType shipmentType){
		this.orderItems = new LinkedList<>(orderItems);
		this.shipmentType = shipmentType;
		this.date = new Date();
	}

	public Order(LinkedList<OrderItem> orderItems,
			ShipmentType shipmentType, Date date){
		this.orderItems = new LinkedList<>(orderItems);
		this.shipmentType = shipmentType;
		this.date = date;
	}
	
	public Order(Order order){
		this.orderItems = new LinkedList<>(order.getOrderItems());
		this.shipmentType = order.getShipmentType();
		this.date = order.getDate();
	}
	
	public LinkedList<OrderItem> getOrderItems() {
		return orderItems;
	}
	
	public ShipmentType getShipmentType(){
		return shipmentType;
	}

	public Date getDate(){
		return date;
	}
	
	public void setOrderItems(LinkedList<OrderItem> orderItems) {
		this.orderItems = new LinkedList<>(orderItems);
	}
	
	public void setShipment(ShipmentType shipmentType){
		this.shipmentType = shipmentType;
	}
	
	public void setDate(Date date){
		this.date = date;
	}
	
	public void addOrderItem(OrderItem order){
		orderItems.add(order);
	}
}
