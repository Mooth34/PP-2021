import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GasStation {

    public static final int INITIAL_GAS_VOLUME = 50000;
    public static final int GAS_PRICE = 50;
    public static final int PUMPS_COUNT = 3;
    public static final int BUYERS_COUNT = 50;
    public static final int INITIAL_BUYER_WEALTH = 5000;

    private int gas_count;
    public List<Pump> pumps = new LinkedList<>();
    public int last_vacant_pump;


    public GasStation(int gas_count) {
        this.gas_count = gas_count;
        for (int i = 0; i < PUMPS_COUNT; i++) {
            Pump p = new Pump(this, i);
            pumps.add(p);
        }
    }

    static class Cashier implements Runnable {
        final private GasStation gas_station;
        private int money = 0;
        public List<Buyer> buyers = new LinkedList<>();

        public Cashier(GasStation gas_station) {
            this.gas_station = gas_station;
            for (int i = 0; i < BUYERS_COUNT; i++) {
                Buyer b = new Buyer();
                buyers.add(b);
            }
        }

        public int getMoney() {
            return money;
        }

        public void serveCustomer(Buyer buyer, Pump pump, int gas_count) throws Exception {
            money += buyer.payForGas(gas_count);
            pump.current_buyer = buyer;
            pump.is_ready = true;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    synchronized (gas_station) {
                        gas_station.wait();
                    }
                    Random r = new Random();
                    serveCustomer(buyers.remove(0), gas_station.pumps.get(gas_station.last_vacant_pump),
                            r.nextInt(50)); //TODO initialization pumps by first buyers
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Buyer {
        private int gas_count = 0;
        private int money = INITIAL_BUYER_WEALTH;

        public Buyer() {
        }

        public int getGas_count() {
            return gas_count;
        }

        public int payForGas(int gas_count) throws Exception {
            if (GAS_PRICE * gas_count < money) {
                money -= GAS_PRICE * gas_count;
                return GAS_PRICE * gas_count;
            } else throw new Exception("Not enough money!");
        }

    }


    static class Pump implements Runnable {
        final private GasStation gas_station;
        final private int number;
        private Buyer current_buyer;
        private boolean is_ready = false;
        private int gas_needed;

        public Pump(GasStation gas_station, int number) {
            this.gas_station = gas_station;
            this.number = number;
        }

        private void dispenseGas() throws Exception {
            synchronized (gas_station) {
                if (gas_station.gas_count >= gas_needed) {
                    gas_station.gas_count -= gas_needed;
                    current_buyer.gas_count += gas_needed;
                    gas_station.last_vacant_pump = number;
                    gas_station.notify();
                } else throw new Exception("Pump isn't ready or not enough gas!");
            }
            is_ready = false;
            current_buyer = null;
            gas_needed = 0;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted() && this.is_ready) {
                try {
                    this.dispenseGas();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        GasStation gas_station = new GasStation(INITIAL_GAS_VOLUME);

        Cashier cashier = new Cashier(gas_station);
        Thread t_cashier = new Thread(cashier, "Cashier");
        t_cashier.start(); //waiting for vacant pumps

        List<Thread> threads = new LinkedList<>();
        int index = 0;
        for (Pump p : gas_station.pumps) {
            Thread t_pump = new Thread(p, "" + index++);
            threads.add(t_pump);
            t_pump.start(); //waiting to be ready (activated by Cashier)
        }

        for (Thread t: threads) {
            t.join();
        }


        System.out.println("Gas station capacity: " + INITIAL_GAS_VOLUME);
        System.out.println("Gas station gas left: " + gas_station.gas_count);
        int total_gas_sold = 0;
        for (Buyer b: cashier.buyers)
            total_gas_sold+=b.getGas_count();
        System.out.println("Total gas sold: " + total_gas_sold);

        //Tests
        if (gas_station.gas_count + total_gas_sold == INITIAL_GAS_VOLUME)
            System.out.println("Successful!");
        else
            System.out.println("There is any gas leaks!");
        if (cashier.getMoney() / GAS_PRICE == total_gas_sold)
            System.out.println("Successful!");
        else
            System.out.println("There is any money leaks!");
    }
}
