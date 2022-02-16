package ru.spbstu.telematics.java;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Assert;

import java.time.Instant;
import java.util.Vector;

import static java.lang.Math.pow;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase
{
    public void testApp()
    {

        Vector<Double> x = new Vector<>();
        for (int i = 0; i < 1000; i++)
            x.add(i * pow(-1, i));
        GradientDescent descentMinimizer = new GradientDescent(2);
        Vector startVector = new Vector(new double[]{2, 2});
        double expectedF = 0.;
        Vector res = new Vector(2);
        descentMinimizer.compute(x);
        Assert.assertEquals(expectedF, f.f(res), 21e-2);
    }
}