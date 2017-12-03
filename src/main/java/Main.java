import deposit.TotalDepositCorrect;
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

public class Main {
    private static String PATH_TO_TEST_FILE = "/home/dmitriy/Quality_Assurance/Amazon Test Lab Tavrov-output.csv";
    private static long MARGIN_TO_REAL_DATA = 7L;
    private static int AMOUNT_PRODUCT = 2;
    private static String SEPARATOR = ",";
    private static ShipmentType shipmentType;
    private static final Calendar calendar = Calendar.getInstance();

    public static LinkedList<Pair<Order, Double>> arrayList = new LinkedList<>();


    public static void main(String[] args) throws IOException {
        CalculateRealValueFromFile();

    }

    private static void CalculateRealValueFromFile() throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(new File(PATH_TO_TEST_FILE)));

        String line = fileReader.readLine();
        for (int i = 0; i < MARGIN_TO_REAL_DATA; ++i) {
            line = fileReader.readLine();
        }

        while (line != null) {
            String[] split = line.split(SEPARATOR);
            Order order = mapLineToOrder(split);
            TotalDepositCorrect totalDeposit = new TotalDepositCorrect(order);
            double totalDeposit1 = totalDeposit.getTotalDeposit();
            line = fileReader.readLine();
            arrayList.add(new Pair<Order, Double>(order, totalDeposit1));
        }
    }

    ///Products,Products1,quantity,quantity1,GiftWrapCharges,GiftWrapCharges1,Ship,Date,Price,Price1
    private static Order mapLineToOrder(String[] line) {
        Order order = new Order();
        int nowCursor = 0;
        String[] quantity = new String[AMOUNT_PRODUCT];
        String[] products = new String[AMOUNT_PRODUCT];
        String[] giftWrapCharges = new String[AMOUNT_PRODUCT];
        String[] price = new String[AMOUNT_PRODUCT];

        System.arraycopy(line, nowCursor, products, 0, AMOUNT_PRODUCT);
        nowCursor += AMOUNT_PRODUCT;
        System.arraycopy(line, nowCursor, quantity, 0, AMOUNT_PRODUCT);
        nowCursor += AMOUNT_PRODUCT;
        System.arraycopy(line, nowCursor, giftWrapCharges, 0, AMOUNT_PRODUCT);
        nowCursor += AMOUNT_PRODUCT;
        String ship = line[6];
        String date = line[7];
        nowCursor += AMOUNT_PRODUCT;
        System.arraycopy(line, nowCursor, price, 0, AMOUNT_PRODUCT);
        LinkedList<OrderItem> linkedList = new LinkedList<OrderItem>();
        for (int i = 0; i < AMOUNT_PRODUCT; ++i) {
            linkedList.addAll(createOrderItems(String.valueOf(products[i]),
                    Integer.valueOf(quantity[i]),
                    Boolean.valueOf(giftWrapCharges[i]),
                    Double.valueOf(price[i])));
        }
        order.setOrderItems(linkedList);
        order.setShipment(getShipmentType(ship));
        String[] split = date.trim().split("\\.");
        int year = Integer.valueOf(split[0]);
        int month = Integer.valueOf(split[1])-1;
        int day = Integer.valueOf(split[2]);
        calendar.set(year,month,day);
        order.setDate(calendar.getTime());
        return order;
    }

    private static LinkedList<OrderItem> createOrderItems(String product, int quantity, boolean giftWrapCharge, double price) {
        LinkedList<OrderItem> linkedList = new LinkedList<OrderItem>();


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

    public static ShipmentType getShipmentType(String ship) {
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



