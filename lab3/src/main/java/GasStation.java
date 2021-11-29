import java.util.LinkedList;
import java.util.List;

public class GasStation {
    private static final int INITIAL_GAS_VOLUME = 50000;
    private static final int INITIAL_WEALTH = 5000;
    public static final int PRICE = 50;
    private int gas_count = INITIAL_GAS_VOLUME;

    public void sendGasToPump(Pump pump, int gas_count) throws OutOfGasException{
        if (this.gas_count >= gas_count)
            this.gas_count-=gas_count;
        else {
            System.out.println("Out of gas!");
        }

    }

    static class Cashier{
        private int money;

       public Cashier(int money) {
           super();
           this.money = money;
       }
       public int getMoney() {
           return money;
       }
       public  void setMoney(int money) {
           this.money = money;
       }
    }

    static class Buyer implements Runnable{
        int gas_count = 0;
        int money = INITIAL_WEALTH;
        private Pump pump;

        public Buyer(Pump pump) {
            super();
            this.pump = pump;
        }

        @Override
        public void run() {

            while(!Thread.currentThread().isInterrupted() && canProceed()) {
                pump.dispenseGas(this.pump, this, gas_amount);
            }

        }

        public void addGas(int i) {
            gas_count += i;
        }

        private boolean canProceed() {
            return !acc.isEmpty()  && seller.getNumOfGoods() > 0;
        }

    }


    static class Pump implements Runnable{
        private int gas_volume;

        public void addGas(Pump pump, Buyer buyer, int gas_amount){
            buyer.addGas(gas_amount);
            pump.
        }

        public void dispenseGas(Pump pump, Buyer buyer, int gas_amount){
            buyer.addGas(gas_amount);
            this.gas_volume-=gas_amount
        }

        @Override
        public void run() {

        }
    }



    List<Pump> pumps = new LinkedList<>();
    List<Buyer> buyers = new LinkedList<>();
}
