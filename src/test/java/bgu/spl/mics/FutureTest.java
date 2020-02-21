package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {
    Future<String> future;
    @BeforeEach
    public void setUp(){
        future = new Future<String>();
    }

    /**
     * <p>
     *  testing resolve and get methods
     */
    @Test
    public void testResolveAndGet () {
        String s= "some value";
        future.resolve(s); //changing the future result
        assertEquals(s,future.get()); //future resolve action was updated right.
    }

    /**
     * <p>
     *  testing isDone method retrieves true only after future is resolved.
     */
    @Test
    public void testIsDone () {
        assertFalse(future.isDone()); // future is not done
        future.resolve(""); //changing the future result
        assertTrue(future.isDone()); // future is done
    }
    /**
     * <p>
     *  testing get with wait time
     */
    @Test
    public void testSecondGet () {
        String s= "some value";
        future.resolve(s);
        long start = System.nanoTime();
        assertSame(s,(future.get(100000,TimeUnit.NANOSECONDS)));
        long end = System.nanoTime();
        assertTrue(end-start>=100000);  // testing the method waited at least the amount of time specified
    }

}