import org.junit.Test;
import frc.robot.utils.Utils;
import static org.junit.Assert.assertEquals;

public class UtilsTest {
    float DELTA = 0.00001f;
    @Test
    public void testClip() {
        assertEquals(5, Utils.clip(5, -6, 10), DELTA);
        assertEquals(8, Utils.clip(20, -10, 8), DELTA);
        assertEquals(-3, Utils.clip(-100, -3, 8), DELTA);
    }

    @Test
    public void testMap() {
        assertEquals(0.8, Utils.map(8, 0, 10, 0, 1), DELTA);
        assertEquals(0.9, Utils.map(0.8, -1, 1, 0, 1), DELTA);
        assertEquals(11, Utils.map(100, 0, 10, 1, 2), DELTA);
    }

    @Test
    public void testCurve() {
        assertEquals(0.25, Utils.curve(0.5, 2), DELTA);
        assertEquals(-0.001, Utils.curve(-0.1, 3), DELTA);
    }
}