package order;

/**
 * Single items that go in an order.
 * 
 * @author Dan Tavrov
 *
 */
public class OrderItem {
	//product type
	private ProductType productType;
	
	//number of the items of this kind
	private int quantity;
	
	//price of a single item of this kind
	private double price;
	
	//whether it needs to be gift wrapped
	private boolean giftWrap;
	
	public OrderItem(ProductType productType, int quantity,
			double price, boolean giftWrap) {
		this.productType = productType;
		this.quantity = quantity;
		this.price = price;
		this.giftWrap = giftWrap;
	}
	
	public OrderItem(ProductType productType, int quantity,
			double price) {
		this.productType = productType;
		this.quantity = quantity;
		this.price = price;
		this.giftWrap = false;
	}

	public ProductType getProductType() {
		return productType;
	}
	
	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public boolean isGiftWrap() {
		return giftWrap;
	}

	public void setGiftWrap(boolean giftWrap) {
		this.giftWrap = giftWrap;
	}

	public double getTotalPrice(){
		return price * quantity;
	}
	
	public String toString(){
		return new StringBuilder(productType.toString()).append(": ").
				append(quantity).append(" item(s) ").
				append("at price $").append(price).append(", ").
				append("with ").append(
						(!giftWrap)?("no "):("")
						).
						append("giftwrap").toString();		
	}
}
