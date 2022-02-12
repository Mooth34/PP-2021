import java.util.Iterator;
import java.util.Vector;

import static java.lang.Math.abs;
import static java.lang.Math.pow;


public class GradientDescent {

    //длина шага
    static final double INITIAL_LAMBDA = 1;
    //дельта дробления шага
    static final double DELTA = 0.95;
    static final double EPS = 0.1;
    //отклонение для остановки алгоритма
    static final double STOP_EPS = 1e-8;
    static final double MAX_NUMBER_OF_ITERATIONS = 100000;

    static int iterations;

    static double f(Vector<Double> x) {
        return 10 * pow(x.get(0), 2) + pow(x.get(1), 2); //10 * x^2 + y^2
        //return pow(1-x.get(0), 2) + 100 * pow(x.get(1)-pow(x.get(0), 2), 2); // (1-x^2) + 100*(y-x^2)^2
    }

    static Vector<Double> GradientF(Vector<Double> x) {
        Vector<Double> tmp = new Vector<>(2);
        //double delta = 0.0001;
        //for (double elem : x)
        //    tmp.add(elem + delta);
        //tmp.add(f(tmp)-f(x)/delta);
        tmp.add(20 * x.get(0));
        tmp.add(2 * x.get(1));
        //tmp.add(2 * x.get(0) * ( 200 * pow(x.get(0), 2) - 200 * x.get(1) - 1) ); // 2x (200x^2 - 200y - 1)
        //tmp.add(200 * (x.get(1) - pow(x.get(0), 2))); // 200 (y-x^2)
        return tmp;
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
        x.add(-9.);
        x.add(-9.);
        Vector<Double> ans = compute(x);
        System.out.println("Value: " + f(ans));
        System.out.println("Point: ");
        for (Double elem : ans)
            System.out.println(elem + " ");
        System.out.println("Number of iterations:" + iterations);
    }
}
