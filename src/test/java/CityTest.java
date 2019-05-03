import org.junit.Test;

import static org.junit.Assert.*;

public class CityTest {

    @Test
    public void getDistanceWithIntegerTest() {
        City c1 = new City(0,0);
        City c2 = new City(1,1);
        assertEquals(Math.sqrt(2),c1.getDistanceWith(c2),3);
    }

    @Test
    public void getDistanceWithFloatNumberTest() {
        City c1 = new City(1.5f,1.5f);
        City c2 = new City(2.5f,2.5f);
        assertEquals(Math.sqrt(2),c1.getDistanceWith(c2),3);
    }
}