
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.Vector;


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

    static int iterations;

    static double f(Vector<Double> x) {
        return 1000 * pow(x.get(0), 2) + pow(x.get(1), 2); //1000 * x^2 + y^2
    }

    static Vector<Double> GradientF(Vector<Double> vec) {
        Vector<Double> dVec = new Vector<>(vec), grad = new Vector<>(2);
        double delta = GRAD_DELTA;
        for (int i = 0; i < vec.size(); i++) {
            dVec.set(i, dVec.get(i) + delta);
            grad.add((f(dVec) - f(vec)) / delta);
            dVec.clear();
            dVec.addAll(vec);
        }
        //grad.add(20 * vec.get(0));
        //grad.add(2 * vec.get(1));
        return grad;
    }

    //minimizes N-dimensional function f; x0 - start point
    static Vector<Double> compute(Vector<Double> x0) {
        Vector<Double> old = new Vector<>(), grad, cur_x = x0;
        Iterator<Double> gradIter;
        double s, lambda = INITIAL_LAMBDA;


        for (iterations = 1; iterations <= MAX_NUMBER_OF_ITERATIONS; iterations++) {
            //save old value
            old.clear();
            old.addAll(cur_x);
            //compute gradient
            grad = GradientF(cur_x);

            gradIter = grad.iterator();
            //вычисляем новое значение
            for (int i = 0; i < cur_x.size(); i++)
                cur_x.set(i, cur_x.get(i) - lambda * gradIter.next());


            //вычисляем квадрат нормы градиента
            s = 0;
            for (Double elem : grad)
                s += pow(elem, 2);

            //пересчет длины шага
            while (f(cur_x) > f(old) - EPS * lambda * s) {
                lambda *= DELTA;
                cur_x.clear();
                cur_x.addAll(old);
                gradIter = grad.iterator();
                for (int i = 0; i < cur_x.size(); i++)
                    cur_x.set(i, cur_x.get(i) - lambda * gradIter.next());
            }

            //условие останова
            if (abs(f(cur_x) - f(old)) < STOP_EPS)
                break;
        }

        return cur_x;
    }

    public static void main(String[] args) {
        Vector<Double> x = new Vector<>();
        Vector<Double> ans = new Vector<>();
        LocalTime start = LocalTime.now();
        for (int i = 0; i < 10; i++) {
            x.clear();
            x.add(-30.);
            x.add(30.);
            ans = compute(x);
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
