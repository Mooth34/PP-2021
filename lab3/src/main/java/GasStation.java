import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GasStation {

    public static final int INITIAL_GAS_VOLUME = 5000000;
    public static final int GAS_PRICE = 50;
    public static final int PUMPS_COUNT = 3;
    public static final int BUYERS_COUNT = 10;
    public static final int INITIAL_BUYER_WEALTH = 5000;
    public static int customers_served = 0;

    private int gas_count;
    public List<Pump> pumps = new LinkedList<>();
    public Cashier cashier;

    public GasStation(int gas_count) {
        this.gas_count = gas_count;
        for (int i = 0; i < PUMPS_COUNT; i++) {
            Pump p = new Pump(this);
            pumps.add(p);
        }
    }

    public Pump getVacantPump() /*throws Exception*/ {
        int i = 0;
        for (Pump p : pumps) {
            if (p.is_vacant)
                return pumps.get(i);
            i++;
        }
        return null;
        //throw new Exception("No vacant pumps!");
    }

    static class Cashier implements Runnable {
        final private GasStation gas_station;
        private int money = 0;
        public List<Buyer> buyers = new LinkedList<>();
        final public Object out_of_buyers_monitor;

        public Cashier(GasStation gas_station) {
            this.gas_station = gas_station;
            this.gas_station.cashier = this;
            this.out_of_buyers_monitor = new Object();
        }

        public int getMoney() {
            return money;
        }

        public void serveCustomer(Buyer buyer, Pump pump, int gas_count) throws Exception {
            money += buyer.payForGas(gas_count);
            pump.current_buyer = buyer;
            pump.gas_needed = gas_count;
            pump.is_vacant = false;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if (buyers.isEmpty()) {
                        synchronized (out_of_buyers_monitor) {
                            out_of_buyers_monitor.notify();
                        }
                        System.out.println("Out of buyers!");
                        return;
                    }
                    Random r = new Random();

                    synchronized (gas_station) {
                        if (gas_station.getVacantPump() == null) {
                            System.out.println("Time: " + System.currentTimeMillis() + " " + "Waiting for vacant pumps");
                            gas_station.wait();
                        }
                    }
                    Pump pump;
                    synchronized (gas_station) {
                        pump = gas_station.getVacantPump();
                        serveCustomer(buyers.remove(0), pump, r.nextInt(50));
                    }
                    System.out.println("Time: " + System.currentTimeMillis() + " " + (BUYERS_COUNT - buyers.size()) +
                            "th customer served and sent to pump № " + (gas_station.pumps.indexOf(pump) + 1));
                    synchronized (pump.vacant_monitor) {
                        pump.vacant_monitor.notify();  //Notify that ready
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Buyer {
        volatile private int gas_count = 0;
        private int money = INITIAL_BUYER_WEALTH;
        public Cashier cashier;

        public Buyer() {
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
            private Buyer current_buyer;
            public boolean is_vacant = true;
            private int gas_needed;
            private int customers_served;
            final private Object vacant_monitor;

            public Pump(GasStation gas_station) {
                this.gas_station = gas_station;
                this.customers_served = 0;
                this.vacant_monitor = new Object();
            }

            private void dispenseGas() throws Exception {
                int sleepMs = 100 * gas_needed + new Random().nextInt(1000);
                System.out.println("Time: " + System.currentTimeMillis() + " " + "                        " + Thread.currentThread().getName() + " Sleep = " + sleepMs + " ms");
                Thread.sleep(sleepMs);
                synchronized (gas_station) {
                    if (gas_station.gas_count >= gas_needed) {
                        gas_station.gas_count -= gas_needed;
                        current_buyer.gas_count += gas_needed;
                    } else throw new Exception("Pump isn't ready or not enough gas!");
                }
                current_buyer = null;
                customers_served++;
                gas_needed = 0;
            }

            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        synchronized (vacant_monitor) {
                            synchronized (gas_station) {
                                is_vacant = true;
                                System.out.println("Time: " + System.currentTimeMillis() + " " + "Notify for being vacant " + Thread.currentThread().getName());
                                gas_station.notify();
                            }

                            System.out.println("Time: " + System.currentTimeMillis() + " " + "Waiting for being ready " + Thread.currentThread().getName());
                            vacant_monitor.wait();
                            is_vacant = false;
                        }
                        long startTime = System.currentTimeMillis();
                        System.out.println("Time: " + System.currentTimeMillis() + " " + "                        Began dispensing " + Thread.currentThread().getName());
                        dispenseGas();
                        System.out.println("Time: " + System.currentTimeMillis() + " " + "                        Dispensing ended " + Thread.currentThread().getName() +
                                " Took: " + (System.currentTimeMillis() - startTime) + " ms");

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
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
            Thread generate_buyers = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    for (int i = 0; i < BUYERS_COUNT; i++) {
                        gas_station.cashier.buyers.add(new Buyer());
                        try {
                            Thread.sleep(new Random().nextInt(5));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Thread.currentThread().interrupt();
                }
            });
            generate_buyers.start();
            List<Thread> threads = new LinkedList<>();
            int index = 1;
            for (Pump p : gas_station.pumps) {
                Thread t_pump = new Thread(p, "Pump №" + index++);
                threads.add(t_pump);
                t_pump.start(); //waiting to be ready (activated by Cashier)
            }

            t_cashier.start(); //waiting for vacant pumps


            synchronized (cashier.out_of_buyers_monitor) {
                cashier.out_of_buyers_monitor.wait();
            }

            //waiting for ending of each thread
            boolean exit = false;
            while (!exit) {
                exit = true;
                for (Thread thread : threads)
                    if (thread.getState() != Thread.State.WAITING) {
                        exit = false;
                        break;
                    }
            }


            //terminating threads
            t_cashier.interrupt();
            for (Thread t : threads) {
                t.interrupt();
            }


            System.out.println("Gas station capacity: " + INITIAL_GAS_VOLUME);
            System.out.println("Gas station gas left: " + gas_station.gas_count);
            int total_gas_sold = cashier.getMoney() / GAS_PRICE;
            System.out.println("Total gas sold: " + total_gas_sold);

            int i = 1;
            for (Pump p : gas_station.pumps)
                System.out.println("Pump №" + i++ + " served: " + p.customers_served);

            //tests
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
