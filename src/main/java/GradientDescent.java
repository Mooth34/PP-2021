
import java.util.Vector;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Math.abs;
import static java.lang.Math.pow;


public class GradientDescent {
    //длина шага
    static final double INITIAL_LAMBDA = 1;
    //дельта дробления шага
    static final double DELTA = 0.95;
    //дельта вычисления производной
    static final double GRAD_DELTA = 1e-6;
    static final double EPS = 0.1;
    //отклонение для остановки алгоритма
    static final double STOP_EPS = 1e-5;
    static final double MAX_NUMBER_OF_ITERATIONS = 100000;
    static final int DIMENSIONS = 1000;
    static final int THREADS = 4;
    static int iterations;

    static double f(Vector<Double> x) {
        double val = 0.;
        for (int i = 0; i < DIMENSIONS; i++) {
            val += pow(x.get(i), 2);    // x^2 + y^2 + z^2 + ...
        }
        return val;
    }

    static Vector<Double> GradientF(Vector<Double> vec) throws InterruptedException {
        Vector<Double> grad = new Vector<>(vec);
        double delta = GRAD_DELTA;
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);
        CountDownLatch countDownLatch = new CountDownLatch(vec.size());
        for (int i = 0; i < vec.size(); i++) {
            final int finalI = i;
            executor.submit(() -> {
                Vector<Double> dVec = new Vector<>(vec);
                dVec.set(finalI, dVec.get(finalI) + delta);
                grad.set(finalI, (f(dVec) - f(vec)) / delta);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executor.shutdown();
        return grad;
    }



    //minimizes N-dimensional function f; x0 - start point
    static Vector<Double> compute(Vector<Double> x0) throws InterruptedException {
        Vector<Double> old = new Vector<>(), cur_x = x0, grad;
        final double[] gradNorm = {0};
        double lambda = INITIAL_LAMBDA;

        for (iterations = 1; iterations <= MAX_NUMBER_OF_ITERATIONS; iterations++) {
            //save old value
            old.clear();
            old.addAll(cur_x);
            //compute gradient
            grad = GradientF(cur_x);
            gradNorm[0] = 0;
            ExecutorService executor = Executors.newFixedThreadPool(THREADS);
            CountDownLatch countDownLatch = new CountDownLatch(cur_x.size());
            for (int i = 0; i < cur_x.size(); i++) {
                final int finalI = i;
                final double finalLambda = lambda;
                Vector<Double> finalGrad = grad;
                executor.submit(() -> {
                    //вычисляем новое значение
                    Double gradElem = finalGrad.get(finalI);
                    cur_x.set(finalI, cur_x.get(finalI) - finalLambda * gradElem);
                    //вычисляем квадрат нормы градиента
                    gradNorm[0] += pow(gradElem, 2);
                    countDownLatch.countDown();
                });
            }
            countDownLatch.await();
            executor.shutdown();
            //пересчет длины шага
            while (f(cur_x) > f(old) - EPS * lambda * gradNorm[0]) {
                lambda *= DELTA;
                cur_x.clear();
                cur_x.addAll(old);
                ExecutorService executor2 = Executors.newFixedThreadPool(THREADS);
                CountDownLatch countDownLatch2 = new CountDownLatch(cur_x.size());
                for (int i = 0; i < cur_x.size(); i++) {
                    int finalI = i;
                    double finalLambda1 = lambda;
                    Vector<Double> finalGrad1 = grad;
                    executor2.submit(() -> {
                    cur_x.set(finalI, cur_x.get(finalI) - finalLambda1 * finalGrad1.get(finalI));
                    countDownLatch2.countDown();
                    });
                }
                countDownLatch2.await();
                executor2.shutdown();
            }

            //условие останова
            if (abs(f(cur_x) - f(old)) < STOP_EPS)
                break;
        }

        return cur_x;
}


    public static void main(String[] args) {
        Vector<Double> x = new Vector<>();
        for (int i = 0; i < DIMENSIONS; i++)
            x.add(i * pow(-1, i));
        LocalTime start = LocalTime.now();
        Vector<Double> ans = null;
        try {
            ans = compute(x);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LocalTime finish = LocalTime.now();
        System.out.printf("Value: %.3f %n", f(ans));
        System.out.println("Point: ");
        for (Double elem : ans)
            System.out.printf("%.3f %n", elem);
        System.out.println("Number of iterations:" + iterations);

        System.out.printf("Working time = %d milliseconds%n", ChronoUnit.MILLIS.between(start, finish));
    }
}
