package deposit;

import java.util.Calendar;
import java.util.Date;

import order.Order;
import order.OrderItem;
import order.ProductType;
import order.ShipmentType;

/**
 * Class for calculating total amount deposited
 * to a seller's account after selling a given Order.
 * 
 * @author Dan Tavrov
 *
 */
public class TotalDeposit {
	//the order being sold
	private Order order;
	
	private static final Calendar calendar = Calendar.getInstance();
	
	//dates of the sales
	private final Date jewelrySalesStartDate;
	private final Date jewelrySalesEndDate;
	
	//dates for referral fees
	private final Date watchReferralStartDate;
	private final Date watchReferralEndDate;
	
	//some constants for watch referral fee
	private final double WATCH_REFERRAL_LIMIT1 = 200.0;
	private final double WATCH_REFERRAL_LIMIT2 = 800.0;
	private final double WATCH_REFERRAL_RATE1 = 0.16;
	private final double WATCH_REFERRAL_RATE2 = 0.15;
	private final double WATCH_REFERRAL_RATE3 = 0.12;
	
	//per-item fee
	private final double PER_ITEM_FEE = 0.99;
	
	//limits for free shipping
	private final double FREE_SHIPPING_BOOK_LIMIT = 25.0;
	private final double FREE_SHIPPING_ELECTRONICS_LIMIT = 99.0;
	private final double FREE_SHIPPING_LIMIT = 49.0;
	
	//some constants for jewelry sales
	private final double JEWELRY_SALES_LIMIT = 500.0;
	private final double JEWELRY_SALES_RATE = 0.9;
	
	public TotalDeposit(Order order){
		this.order = new Order(order);
		
		//set dates
		calendar.set(2017, 12, 16);
		jewelrySalesStartDate = calendar.getTime();
		
		calendar.set(2018, 3, 30);
		jewelrySalesEndDate = calendar.getTime();
		
		calendar.set(2018, 1, 16);
		watchReferralStartDate = calendar.getTime();
		
		calendar.set(2018, 9, 30);
		watchReferralEndDate = calendar.getTime();
	}
	
	public Order getOrder(){
		return order;
	}
	
	public void setOrder(Order order){
		this.order = new Order(order);
	}
	
	/**
	 * Method for calculating the total amount deposited
	 * to a seller's account when the order is sold.
	 * 
	 * It calculates the total amount as the sum of the
	 * following quantities:
	 * 1) + item price (possibly adjusted for any sales standing);
	 * 2) + shipment charge (if the order is eligible for free
	 * shipping, the charge is not collected);
	 * 3) + gift wrap charge;
	 * 4) - referral fees;
	 * 5) - variable closure fees;
	 * 6) - per-item fee of $0.99. 
	 * 
	 * @return Total amount deposited to a seller's
	 * account.
	 */
	public double getTotalDeposit(){
		boolean jewelrySalesApplicable = isJewelrySalesApplicable();
		boolean freeShipping = isEligibleForFreeShipping();
		
		double totalDeposit = 0.0;
		double totalWatchPrice = 0.0;
		
		for (OrderItem orderItem:order.getOrderItems()){
			if (orderItem.getProductType() == ProductType.WATCHES
					&&
				order.getDate().after(watchReferralStartDate)
					&&
				order.getDate().before(watchReferralEndDate)){
				//since in this date range referral fees for watches depend
				//on the total price, we first calculate it,
				//and then we will analyze it
				totalWatchPrice += orderItem.getTotalPrice();
			}
			else{
				//first calculate the item price,
				//including the sales and referral fees
				if (orderItem.getPrice()
						* getReferralFeeRate(orderItem.getProductType())
						> getReferralFeeMinimum(orderItem.getProductType())){
					if (jewelrySalesApplicable && 
							orderItem.getProductType() == ProductType.JEWELRY){
							totalDeposit += orderItem.getTotalPrice() *
									(JEWELRY_SALES_RATE - getReferralFeeRate(ProductType.JEWELRY));
					}
					else{
						totalDeposit += orderItem.getTotalPrice() *
								(1.0 - getReferralFeeRate(orderItem.getProductType()));
					}
				}
				else{
					if (jewelrySalesApplicable &&
						    orderItem.getProductType() == ProductType.JEWELRY){
							totalDeposit += (orderItem.getTotalPrice() * JEWELRY_SALES_RATE)
									- (orderItem.getQuantity()
									* getReferralFeeMinimum(orderItem.getProductType()));
					}
					else{
						totalDeposit += orderItem.getTotalPrice()
								- (orderItem.getQuantity()
								* getReferralFeeMinimum(orderItem.getProductType()));
					}
				}
			}
		
			//add shipment charges
			if (!freeShipping){
				//unless the order is eligible for free shipping
				totalDeposit += orderItem.getQuantity()
						* getShippingRate(orderItem.getProductType(),
								order.getShipmentType());
			}
			
			//add gift wrap charges
			if (orderItem.isGiftWrap()){
				totalDeposit += orderItem.getQuantity()
						* getGiftWrapRate(orderItem.getProductType());
			}
			
			//subtract variable closing fee
			totalDeposit -= orderItem.getQuantity()
					* getVariableClosingFree(orderItem.getProductType(),
							order.getShipmentType());
			
			//subtract per-item fee
			if (orderItem.getPrice() >= PER_ITEM_FEE){
				totalDeposit -= orderItem.getQuantity() * PER_ITEM_FEE;
			}
		}
		
		//tackle the problem of referral fees for watches
		if (totalWatchPrice > 0.0){
			//if there were some watches in the order,
			//this means that the date of the order
			//falls in the specified range, and we
			//need to calculate appropriate referral fees
			
			if (totalWatchPrice <= WATCH_REFERRAL_LIMIT1){
				//decide, which one to apply---regular or minimum
				if (totalWatchPrice * WATCH_REFERRAL_RATE1
						> getReferralFeeMinimum(ProductType.WATCHES)){
					totalDeposit += totalWatchPrice
							* (1.0 - WATCH_REFERRAL_RATE1);
				}
				else{
					totalDeposit += totalWatchPrice
							- getReferralFeeMinimum(ProductType.WATCHES);
				}
			}
			else if (totalWatchPrice <= WATCH_REFERRAL_LIMIT2){
				totalDeposit +=
						WATCH_REFERRAL_LIMIT1
							* (1.0 - WATCH_REFERRAL_RATE1) +
						(totalWatchPrice - WATCH_REFERRAL_LIMIT1)
							* (1.0 - WATCH_REFERRAL_RATE2);
			}
			else{
				totalDeposit +=
						WATCH_REFERRAL_LIMIT1
							* (1.0 - WATCH_REFERRAL_RATE2) +
						(totalWatchPrice - WATCH_REFERRAL_LIMIT1)
							* (1.0 - WATCH_REFERRAL_RATE1) +
						(totalWatchPrice - WATCH_REFERRAL_LIMIT2)
							* (1.0 - WATCH_REFERRAL_RATE3);
			}
		}
		
		return totalDeposit;
	}
	
	/**
	 * Method for determining if the order is eligible for
	 * free shipping.
	 * 
	 * @return Returns true if the order is eligible for 
	 * free shipping.
	 */
	private boolean isEligibleForFreeShipping(){
		double totalPriceInType = 0.0;
		
		for (ProductType productType:ProductType.values()){
			//calculate the total price in this category
			for (OrderItem orderItem:order.getOrderItems()){
				if (orderItem.getProductType() == productType){
					totalPriceInType += orderItem.getTotalPrice();
				}
			}
			
			//check the eligibility conditions
			if (productType == ProductType.BOOKS){
				if (totalPriceInType > FREE_SHIPPING_BOOK_LIMIT){
					return true;
				}
			}
			else if (productType == ProductType.ELECTRONICS){
				if (totalPriceInType > FREE_SHIPPING_ELECTRONICS_LIMIT){
					return true;
				}
			}
			else{
				if (totalPriceInType >= FREE_SHIPPING_LIMIT){
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Method for determining if jewelry sales are applicable
	 * to this order.
	 * 
	 * @return Returns true if jewelry sales are applicable to this order.
	 */
	private boolean isJewelrySalesApplicable(){
		//first check if the order date belongs to 
		//the interval of the sales dates
		if (order.getDate().after(jewelrySalesStartDate)
				&&
			order.getDate().before(jewelrySalesEndDate)){
			//calculate total price of all jewelry items
			double totalJewelryPrice = 0.0;
			
			//keep track of the number of jewelry items in the order
			int jewelryCount = 0;
			for (OrderItem orderItem:order.getOrderItems()){
				if (orderItem.getProductType() == ProductType.JEWELRY){
					totalJewelryPrice += orderItem.getTotalPrice();
					jewelryCount++;
				}
			}
			
			if (jewelryCount > 1 && totalJewelryPrice > JEWELRY_SALES_LIMIT){
				//if the number of jewelry items is bigger than one,
				//and the total price is bigger than JEWELRY_SALES_LIMIT
				return true;
			}
		}
		
		return false;
	}
	
	private double getShippingRate(ProductType productType,
			ShipmentType shipmentType){
		switch (productType){
			case BOOKS:
				switch (shipmentType){
					case DOMESTIC:
						return 3.99;
					case DOMESTIC_EXPEDITED:
						return 6.99;
					case INTERNATIONAL:
						return 16.95;
					case INTERNATIONAL_EXPEDITED:
						return 46.50;
					default:
						return 0.00;	
				}
			case CLOTHING:
				switch (order.getShipmentType()){
					case DOMESTIC:
						return 3.99;
					case DOMESTIC_EXPEDITED:
						return 6.99;
					case INTERNATIONAL:
						return 16.95;
					case INTERNATIONAL_EXPEDITED:
						return 46.50;
					default:
						return 0.00;
				}
			case MUSIC:
				switch (order.getShipmentType()){
					case DOMESTIC:
						return 3.99;
					case DOMESTIC_EXPEDITED:
						return 6.99;
					case INTERNATIONAL:
						return 14.95;
					case INTERNATIONAL_EXPEDITED:
						return 46.50;
					default:
						return 0.00;
				}
			case VIDEO:
				switch (order.getShipmentType()){
					case DOMESTIC:
						return 3.99;
					case DOMESTIC_EXPEDITED:
						return 6.19;
					case INTERNATIONAL:
						return 14.95;
					case INTERNATIONAL_EXPEDITED:
						return 46.50;
					default:
						return 0.00;
				}
			default:
				switch (order.getShipmentType()){
					case DOMESTIC:
						return 4.49;
					case DOMESTIC_EXPEDITED:
						return 6.49;
					case INTERNATIONAL:
						return 24.95;
					case INTERNATIONAL_EXPEDITED:
						return 66.50;
					default:
						return 0.00;
				}
		}
	}
	
	private double getGiftWrapRate(ProductType productType){
		switch (productType){
			case CLOTHING:
				return 6.99;
			case ELECTRONICS:
				return 10.99;
			case FURNITUREDECOR:
				return 24.99;
			case JEWELRY:
				return 3.79;
			case WATCHES:
				return 2.99;
			default:
				return 0.0;
		}
	}
	
	private double getReferralFeeRate(ProductType productType){
		switch (productType){
			case BOOKS:
				return 0.15;
			case CLOTHING:
				return 0.15;
			case ELECTRONICS:
				return 0.08;
			case FURNITUREDECOR:
				return 0.15;
			case JEWELRY:
				return 0.20;
			case MUSIC:
				return 0.15;
			case VIDEO:
				return 0.15;
			case WATCHES:
				return 0.15;
			default:
				return 0.0;
		}
	}
	
	private double getReferralFeeMinimum(ProductType productType){
		switch (productType){
			case BOOKS:
				return 0.00;
			case CLOTHING:
				return 1.00;
			case ELECTRONICS:
				return 1.00;
			case FURNITUREDECOR:
				return 1.00;
			case JEWELRY:
				return 2.00;
			case MUSIC:
				return 0.00;
			case VIDEO:
				return 0.00;
			case WATCHES:
				return 2.00;
			default:
				return 0.00;
		}
	}
	
	private double getVariableClosingFree(ProductType productType,
			ShipmentType shipmentType){
		switch (productType){
			case BOOKS:
				return 1.35;
			case VIDEO:
				return 1.35;
			default:
				switch (shipmentType){
					case DOMESTIC:
						return 0.45;
					case DOMESTIC_EXPEDITED:
						return 0.65;
					case INTERNATIONAL:
						return 0.95;
					case INTERNATIONAL_EXPEDITED:
						return 1.15;
					default:
						return 0.0;
				}
		}
	}
}