import deposit.TotalDeposit;
import deposit.TotalDepositFixed;
import order.Order;
import order.OrderItem;
import order.ProductType;
import order.ShipmentType;
import javafx.util.Pair;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;

import static org.junit.Assert.assertTrue;


public class TestTotalDeposit {
	private static final Calendar calendar = Calendar.getInstance();
	@Test
	public void test() {
		calendar.set(2017, Calendar.DECEMBER, 17);

		Order order = new Order();
		order.addOrderItem(new OrderItem(ProductType.JEWELRY,
				1, 550.00));
		order.addOrderItem(new OrderItem(ProductType.JEWELRY,
				1, 550.00, true));
		order.addOrderItem(new OrderItem(ProductType.FURNITUREDECOR,
				1, 30.00));
		order.setDate(calendar.getTime());
		order.setShipment(ShipmentType.INTERNATIONAL);

		double totalDeposit = new TotalDepositFixed(order).getTotalDeposit();

		System.out.println(totalDeposit);

		assertTrue(Math.abs(
				new TotalDepositFixed(order).getTotalDeposit()
						- 868.32 ) < 1e-10); //true value
	}
	@Test
	public void blackBox() throws IOException {
		String[] nul = new String[1];
		Main main = new Main();
		Main.main(nul);
		LinkedList<Pair<Order, Double>> arrayList = Main.arrayList;
		int i = 0;
		for ( ;i< arrayList.size(); ++i) {
			Order correctOrder = arrayList.get(i).getKey();
			double correctTotalPrice = arrayList.get(i).getValue();
			double totalDeposit = new TotalDeposit(correctOrder).getTotalDeposit();
			if (!(Math.abs(correctTotalPrice-correctTotalPrice)<0.0000001)) {
				System.out.println("Test number " + i);
				System.out.println("Expected result  " +  correctTotalPrice );
				System.out.println("Received result  " +  totalDeposit );
				System.out.println("Fault Test \n" + correctOrder.toString());
				System.out.println("----------------------------------------");
			}

		}
		System.out.println(i);

	}
}
