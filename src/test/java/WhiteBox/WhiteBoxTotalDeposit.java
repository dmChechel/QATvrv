package WhiteBox;

import deposit.TotalDeposit;
import deposit.TotalDepositFixed;
import generator.OrderCsvGenerator;
import javafx.util.Pair;
import order.Order;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;

public class WhiteBoxTotalDeposit {

    private static final String path = "/home/dmitriy/QATvrv/WhiteBoxSuits/";

    @Test
    public void Statement() {
        String file = path + "Statement.csv";
        try {
            runTest(file);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }    }

    @Test
    public void BasicPath() {
        String file = path + "BasicPath.csv";
        try {
            runTest(file);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void Conditional() {
        String file = path + "Conditional.csv";
        try {
            runTest(file);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void runTest(String testFile) throws IOException{

        OrderCsvGenerator generator = new OrderCsvGenerator();
        generator.generate(testFile);
        LinkedList<Order> arrayList = OrderCsvGenerator.arrayList;
        int i = 0;
        int testFail = 0;
        for ( ;i< arrayList.size(); ++i) {
            Order correctOrder = arrayList.get(i);
            double totalDeposit = new TotalDeposit(correctOrder).getTotalDeposit();
            double totalDepositFixed = new TotalDepositFixed(correctOrder).getTotalDeposit();
            if (!(Math.abs(totalDeposit - totalDepositFixed) < 0.0001)) {
                testFail++;
            }

        }
        System.out.println("Tests amount:" + i);
        System.out.println("Tests failed:" + testFail);
    }
}
