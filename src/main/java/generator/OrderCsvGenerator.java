package generator;

import deposit.TotalDepositFixed;
import order.Order;
import order.OrderItem;
import order.ProductType;
import order.ShipmentType;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;

public class OrderCsvGenerator {
    private static final String SEPARATOR = ",";
    private static final Calendar calendar = Calendar.getInstance();

    public static LinkedList<Pair<Order, Double>> arrayList = new LinkedList<>();

    public void generate(String testFile) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(new File(testFile)));

        String line = fileReader.readLine();

        while (line != null) {
            String[] split = line.split(SEPARATOR);
            Order order = mapLineToOrder(split);
            TotalDepositFixed totalDeposit = new TotalDepositFixed(order);
            double totalDeposit1 = totalDeposit.getTotalDeposit();
            line = fileReader.readLine();
            arrayList.add(new Pair<>(order, totalDeposit1));
        }

    }

    ///Products,Products1,quantity,quantity1,GiftWrapCharges,GiftWrapCharges1,Ship,Date,Price,Price1
    private static Order mapLineToOrder(String[] line) {
        Order order = new Order();
        ProductType type = getType(line[0]);
        ShipmentType ship = getShipmentType(line[1]);
        String giftWrap = line[2];
        String quantity = line[3];
        String price = line[4];
        String date = line[5];
        LinkedList<OrderItem> linkedList = new LinkedList<>();
        linkedList.addAll(createOrderItems(String.valueOf(type),
                Integer.valueOf(quantity),
                Boolean.valueOf(giftWrap),
                Double.valueOf(price)));
        order.setOrderItems(linkedList);
        order.setShipment(ship);
        String[] split = date.trim().split("\\.");
        int year = Integer.valueOf(split[0]);
        int month = Integer.valueOf(split[1])-1;
        int day = Integer.valueOf(split[2]);
        calendar.set(year,month,day);
        order.setDate(calendar.getTime());
        return order;
    }

    private static LinkedList<OrderItem> createOrderItems(String product, int quantity, boolean giftWrapCharge, double price) {
        LinkedList<OrderItem> linkedList = new LinkedList<>();


        OrderItem orderItem = new OrderItem(getType(product), quantity, price, giftWrapCharge);
        linkedList.add(orderItem);

        return linkedList;
    }

    private static ProductType getType(String product) {
        switch (product) {
            case "BOOKS":
                return ProductType.BOOKS;
            case "JEWELRY":
                return ProductType.JEWELRY;
            case "MUSIC":
                return ProductType.MUSIC;
            case "VIDEO":
                return ProductType.VIDEO;
            case "WATCHES":
                return ProductType.WATCHES;
            case "CLOTHING":
                return ProductType.CLOTHING;
            case "FURNITUREDECOR":
                return ProductType.FURNITUREDECOR;
            default:
                return ProductType.ELECTRONICS;
        }
    }

    private static ShipmentType getShipmentType(String ship) {
        switch (ship) {
            case "DOMESTIC":
                return ShipmentType.DOMESTIC;
            case "DOMESTIC_EXPEDITED":
                return ShipmentType.DOMESTIC_EXPEDITED;
            case "INTERNATIONAL":
                return ShipmentType.INTERNATIONAL;
            default:
                return ShipmentType.INTERNATIONAL_EXPEDITED;
        }
    }
}



