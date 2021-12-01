import java.util.LinkedList;
import java.util.List;

public class GasStation {
    private static final int INITIAL_GAS_VOLUME = 50000;
    private static final int INITIAL_WEALTH = 5000;
    public static final int PRICE = 50;
    private int gas_count = INITIAL_GAS_VOLUME;

    public void setGas_count(int gas_count) {
        this.gas_count = gas_count;
    }

    static class Cashier{
       private int money = 0;

       public Cashier() {}

        public int getMoney() {
            return money;
        }

        private Pump sendGasToPump(GasStation gas_station, Buyer buyer, int gas_count) throws Exception {
            if (gas_station.gas_count >= gas_count) {
                gas_station.gas_count -= gas_count;
                //Выбор свободного насоса Pump pump = find();
                pump.prepareGas(buyer, gas_count);
                buyer.attachToPump(pump);
                return pump;
            }
            else
                throw new Exception("Out of gas!");
       }

        public void serveCustomer(GasStation gas_station, Buyer buyer, int gas_count) throws Exception {
           money+=buyer.payForGas(gas_count, this);
           sendGasToPump(gas_station, buyer, gas_count);
        }
    }

    static class Buyer{
        int gas_count = 0;
        int money = INITIAL_WEALTH;
        private Pump pump;

        public Buyer() {}

        public int payForGas(int gas_count, Cashier cashier) throws Exception {
            if (PRICE * gas_count < money) {
                money -= PRICE * gas_count;
                return PRICE * gas_count;
            }
            else throw new Exception("Not enough money!");
        }

        public void obtainGas(int i) {
            gas_count += i;
        }

        public void attachToPump(Pump pump){
            this.pump = pump;
        }

    }


    static class Pump implements Runnable{
        private int gas_volume;
        private Buyer current_buyer;

        public void prepareGas(Buyer buyer, int gas_amount){
            this.gas_volume+=gas_amount;
            this.current_buyer = buyer;
        }

        public void dispenseGas() throws Exception {
            if (gas_volume > 0) {
                this.current_buyer.obtainGas(gas_volume);
                this.gas_volume -= gas_volume;
                this.current_buyer.obtainGas(gas_volume);
            }
            else throw new Exception("No gas in pump!");
        }

        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    this.dispenseGas();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        List<Pump> pumps = new LinkedList<>();
        List<Buyer> buyers = new LinkedList<>();
    }
}
