import junit.framework.TestCase;
import org.junit.Assert;
import java.util.Vector;

import static java.lang.Math.pow;

public class AppTest extends TestCase
{
    public void testApp()
    {
        Vector<Double> startVector = new Vector<>();
        for (int i = 0; i < GradientDescent.DIMENSIONS; i++)
            startVector.add(10 * i * pow(-1, i)); // -10, 20, -30, 40...
        double expectedF = 0.; // x^2 + y^2 + z^2 + ...
        Vector<Double> res = new Vector<>();
        try {
            res = GradientDescent.compute(startVector);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(expectedF, GradientDescent.f(res), 1e-4);
    }
}