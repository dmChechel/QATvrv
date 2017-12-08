package BlackBox;

import deposit.TotalDeposit;
import deposit.TotalDepositFixed;
import generator.OrderCsvGenerator;
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
		String[] testFiles = new String[3];
		String testDir = "/home/dmitriy/QATvrv/BlackBoxSuits/";
		testFiles[0] = testDir + "tvrv-output31.csv";
		testFiles[1] = testDir + "tvrv-output32.csv";
		testFiles[2] = testDir + "tvrv-output33.csv";
		for(String testFile:testFiles) {
			OrderCsvGenerator generator = new OrderCsvGenerator();
			generator.generate(testFile);
			LinkedList<Order> arrayList = OrderCsvGenerator.arrayList;
			int i = 0;
			int testFail = 0;
			for ( ;i< arrayList.size(); ++i) {
				Order order = arrayList.get(i);
				double totalDeposit = new TotalDeposit(order).getTotalDeposit();
				double totalDepositFixed = new TotalDepositFixed(order).getTotalDeposit();
				if (!(Math.abs(totalDeposit - totalDepositFixed)<0.0001)) {
					testFail++;
				}

			}
			System.out.println("Tests amount:" + i);
			System.out.println("Tests failed:" + testFail);

		}

	}
}
