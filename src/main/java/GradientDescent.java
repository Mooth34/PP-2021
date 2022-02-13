
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Vector;
import java.util.concurrent.FutureTask;


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
    static final double STOP_EPS = 1e-6;
    static final double MAX_NUMBER_OF_ITERATIONS = 100000;
    static final double THREADS = 2;

    static int iterations;

    static double f(Vector<Double> x) {
        return 1000 * pow(x.get(0), 2) + pow(x.get(1), 2) + pow(x.get(2), 2) + pow(x.get(3), 2); //1000 * x^2 + y^2 + z^2 + k^2
    }

    static Vector<Double> GradientF(Vector<Double> vec) {
        Vector<Double> grad = new Vector<>(vec);
        double delta = GRAD_DELTA;
        //TODO
        for (int i = 0; i < vec.size(); i++) {
            Vector<Double> dVec = new Vector<>(vec);
            dVec.set(i, dVec.get(i) + delta);
            grad.set(i, (f(dVec) - f(vec)) / delta);
        }
        return grad;
    }

    //minimizes N-dimensional function f; x0 - start point
    static Vector<Double> compute(Vector<Double> x0) {
        Vector<Double> old = new Vector<>(), grad, cur_x = x0;
        double gradNorm, lambda = INITIAL_LAMBDA;
        for (iterations = 1; iterations <= MAX_NUMBER_OF_ITERATIONS; iterations++) {
            //save old value
            old.clear();
            old.addAll(cur_x);
            //compute gradient
            grad = GradientF(cur_x);

            gradNorm = 0;

            //TODO
            for (int i = 0; i < cur_x.size(); i++) {
                //вычисляем новое значение
                Double gradElem = grad.get(i);
                cur_x.set(i, cur_x.get(i) - lambda * gradElem);
                //вычисляем квадрат нормы градиента
                gradNorm += pow(gradElem, 2);
            }

            //пересчет длины шага
            while (f(cur_x) > f(old) - EPS * lambda * gradNorm) {
                lambda *= DELTA;
                cur_x.clear();
                cur_x.addAll(old);
                //TODO
                for (int i = 0; i < cur_x.size(); i++)
                    cur_x.set(i, cur_x.get(i) - lambda * grad.get(i));
            }

            //условие останова
            if (abs(f(cur_x) - f(old)) < STOP_EPS)
                break;
        }

        return cur_x;
    }

    public static void main(String[] args) {
        Vector<Double> x = new Vector<>();
        x.add(-30.);
        x.add(30.);
        x.add(0.);
        x.add(1000.5);
        LocalTime start = LocalTime.now();
        Vector<Double> ans = compute(x);
        LocalTime finish = LocalTime.now();
        System.out.printf("Value: %.3f %n", f(ans));
        System.out.println("Point: ");
        for (Double elem : ans)
            System.out.printf("%.3f %n", elem);
        System.out.println("Number of iterations:" + iterations);

        System.out.printf("Working time = %d milliseconds%n", ChronoUnit.MILLIS.between(start, finish));
    }
}
