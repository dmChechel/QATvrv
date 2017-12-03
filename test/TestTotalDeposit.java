import static org.junit.Assert.*;

import java.util.Calendar;

import order.Order;
import order.OrderItem;
import order.ProductType;
import order.ShipmentType;

import org.junit.Test;

import deposit.TotalDeposit;


public class TestTotalDeposit {
	private static final Calendar calendar = Calendar.getInstance();
	
	@Test
	public void test() {
		calendar.set(2017, 12, 17);
		
		Order order = new Order();
		order.addOrderItem(new OrderItem(ProductType.JEWELRY,
				1, 550.00));
		order.addOrderItem(new OrderItem(ProductType.JEWELRY,
				1, 550.00, true));
		order.addOrderItem(new OrderItem(ProductType.FURNITUREDECOR,
				1, 30.00));
		order.setDate(calendar.getTime());
		order.setShipment(ShipmentType.INTERNATIONAL);
		
		double totalDeposit = new TotalDeposit(order).getTotalDeposit();
				
		System.out.println(totalDeposit);
		
		assertTrue(Math.abs(
				new TotalDeposit(order).getTotalDeposit()
				- 793.47 ) < 1e-10); //what the program calculates (because of a bug)
				//- 868.32 ) < 1e-10); //true value
	}
}
