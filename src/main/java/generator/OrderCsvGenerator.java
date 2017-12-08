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

    public static LinkedList<Order> arrayList = new LinkedList<>();


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

    public void generate(String testFile) throws IOException {
        BufferedReader fileReader = new BufferedReader(
                new FileReader(
                        new File(testFile)));

        String line = fileReader.readLine();

        while (line != null) {
            String[] orderStr = line.split(SEPARATOR);
            Order order = getOrder(orderStr);
            line = fileReader.readLine();
            arrayList.add(order);
        }

    }

    private static Order getOrder(String[] line) {
        Order order = new Order();
        LinkedList<OrderItem> linkedList = new LinkedList<>();
        ProductType type = getType(line[0]);
        ShipmentType ship = getShipmentType(line[1]);

        String giftWrap = line[2];
        String quantity = line[3];
        String date = line[4];
        String price = line[5];

        linkedList.addAll(createOrderItems(type,
                Integer.valueOf(quantity),
                Boolean.valueOf(giftWrap),
                Double.valueOf(price)));

        order.setOrderItems(linkedList);
        order.setShipment(ship);

        String[] dateArr = date.trim().split("\\.");

        int year = Integer.valueOf(dateArr[0]);
        int month = Integer.valueOf(dateArr[1])-1;
        int day = Integer.valueOf(dateArr[2]);

        calendar.set(year,month,day);
        order.setDate(calendar.getTime());

        return order;
    }

    private static LinkedList<OrderItem> createOrderItems(
            ProductType product,
            int quantity,
            boolean giftWrapCharge,
            double price) {
        LinkedList<OrderItem> linkedList = new LinkedList<>();
        OrderItem orderItem = new OrderItem(product, quantity, price, giftWrapCharge);
        linkedList.add(orderItem);

        return linkedList;
    }
}



