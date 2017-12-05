package WhiteBox;

import deposit.TotalDeposit;
import deposit.TotalDepositFixed;
import order.Order;
import order.OrderItem;
import order.ProductType;
import order.ShipmentType;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertTrue;

public class WhiteBoxTotalDeposit {

    private static final Calendar calendar = Calendar.getInstance();

    /*
    * Тест когда 1 часы стоимостью 15 долларов  в подарочной упаковке
    * с международной доставкой
    * Дата 2018.01.17
    * Ожидаемый результат 38.6
   * */
    @Test
    public void Test1() {
        calendar.set(2018, Calendar.FEBRUARY, 17);

        Order order = new Order();
        order.addOrderItem(new OrderItem(ProductType.WATCHES,
                1, 15.0, true));
        order.setDate(calendar.getTime());
        order.setShipment(ShipmentType.INTERNATIONAL);
        double totalDeposit = new TotalDeposit(order).getTotalDeposit();
        System.out.println(totalDeposit);
        assertTrue(Math.abs(
                new TotalDepositFixed(order).getTotalDeposit()
                        - 38.6) < 1e-10); //true value

    }


    /*
   * Тест когда 1 часы стоимостью 10 долларов в подарочной упаковке
   * Украшения 1 по 500 долларов и без упаковки
   * Украшения 1 по 9  долларов и с упаковки
    *
    *
    * с внутреней доставкой
    * Дата 2018.02.27
    * Ожидаемый результат 371.05
   * */
    @Test
    public void Test2() {
        calendar.set(2018, Calendar.MARCH, 27);

        Order order = new Order();
        order.addOrderItem(new OrderItem(ProductType.WATCHES,
                1, 10.0, true));
        order.addOrderItem(new OrderItem(ProductType.JEWELRY,
                1, 500.0));
        order.addOrderItem(new OrderItem(ProductType.JEWELRY,
                1, 9.0, true));
        order.setDate(calendar.getTime());
        order.setShipment(ShipmentType.DOMESTIC);
        double totalDeposit = new TotalDeposit(order).getTotalDeposit();
        double Correct = new TotalDepositFixed(order).getTotalDeposit();
        System.out.println(totalDeposit);
        System.out.println(Correct);

        assertTrue(Math.abs(
                new TotalDepositFixed(order).getTotalDeposit()
                        - 371.05) < 1e-10); //true value
    }


    /*
   * Тест когда 1 часы стоимостью 250 долларов в подарочной упаковке
   * Книга 1 20 долларов без упаковки
    *
    *
    * с внутреней скоростной доставкой
    * Дата 2018.04.07
    * Ожидаемый результат 213.85
   * */
    @Test
    public void Test3() {
        calendar.set(2018, Calendar.MAY, 7);

        Order order = new Order();
        order.addOrderItem(new OrderItem(ProductType.WATCHES,
                1, 250.0, true));
        order.addOrderItem(new OrderItem(ProductType.BOOKS,
                1, 20.0));
        order.setDate(calendar.getTime());
        order.setShipment(ShipmentType.DOMESTIC_EXPEDITED);
        double totalDeposit = new TotalDeposit(order).getTotalDeposit();
        double Correct = new TotalDepositFixed(order).getTotalDeposit();
        System.out.println(totalDeposit);
        System.out.println(Correct);

        assertTrue(Math.abs(
                new TotalDepositFixed(order).getTotalDeposit()
                        - 235.5) < 1e-10); //true value
    }


    /*
   * Тест когда 1 часы стоимостью 850 долларов в подарочной упаковке
   * Музыка 1 0.5 долларов без упаковки
   *
    *
    * с международная быстрая доставка
    * Дата 2018.08.30
    * Ожидаемый результат 862.275
   * */
    @Test
    public void Test4() {
        calendar.set(2018, Calendar.SEPTEMBER, 30);

        Order order = new Order();
        order.addOrderItem(new OrderItem(ProductType.WATCHES,
                1, 850.0, true));
        order.addOrderItem(new OrderItem(ProductType.MUSIC,
                1, 0.5));
        order.setDate(calendar.getTime());
        order.setShipment(ShipmentType.INTERNATIONAL_EXPEDITED);
        double totalDeposit = new TotalDeposit(order).getTotalDeposit();
        double Correct = new TotalDepositFixed(order).getTotalDeposit();
        System.out.println(totalDeposit);
        System.out.println(Correct);

        assertTrue(Math.abs(
                new TotalDepositFixed(order).getTotalDeposit()
                        - 862.275) < 1e-10); //true value
    }

    /*
   * Тест когда ничего не заказали
   *
    *
    * с международная быстрая доставка
    * Дата 2018.08.30
    * Ожидаемый результат 0
   * */
    @Test
    public void Test5() {
        calendar.set(2018, Calendar.SEPTEMBER, 30);

        Order order = new Order();
        order.setDate(calendar.getTime());
        order.setShipment(ShipmentType.INTERNATIONAL_EXPEDITED);
        double totalDeposit = new TotalDeposit(order).getTotalDeposit();
        double Correct = new TotalDepositFixed(order).getTotalDeposit();
        System.out.println(totalDeposit);
        System.out.println(Correct);

        assertTrue(Math.abs(
                new TotalDepositFixed(order).getTotalDeposit()
                        - 0) < 1e-10); //true value
    }


}
