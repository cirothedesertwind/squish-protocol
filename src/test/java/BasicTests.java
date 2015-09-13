
import com.codingcrucible.squishprotocol.Squish;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BasicTests {

    @Test
    public void testPutLocalDate() {
        LocalDate ld = LocalDate.of(2123, Month.APRIL, 5);
        byte[] b = new byte[8];
        ByteBuffer bb = ByteBuffer.wrap(b);

        Squish.put(bb, ld);
        bb.rewind();

        String s = new String(b, StandardCharsets.US_ASCII);
        assertEquals(s, "21230405");
    }

    @Test
    public void testGetLocalDate() {
        LocalDate target = LocalDate.of(2123, Month.APRIL, 5);

        byte[] b = new byte[8];
        ByteBuffer bb = ByteBuffer.wrap(b);

        bb.put("21230405".getBytes(StandardCharsets.US_ASCII));
        bb.rewind();

        LocalDate ldt = Squish.getLocalDate(bb);

        assertEquals(ldt, target);
    }
    
    @Test
    public void testPutEncodedLocalDate() {
        LocalDate ld = LocalDate.of(2123, Month.APRIL, 5);
        byte[] b = new byte[4];
        ByteBuffer bb = ByteBuffer.wrap(b);

        Squish.putEncodedLocalDate(bb, ld);
        bb.rewind();

        int i = Squish.getInt(bb);
        assertEquals(i, 21230405);
    }

    @Test
    public void testGetEncodedLocalDate() {
        LocalDate target = LocalDate.of(2123, Month.APRIL, 5);

        byte[] b = new byte[4];
        ByteBuffer bb = ByteBuffer.wrap(b);

        bb.putInt(21230405);
        bb.rewind();

        LocalDate ldt = Squish.getEncodedLocalDate(bb);

        assertEquals(ldt, target);
    }
    
        @Test
    public void testPutLocalTime() {
        LocalTime lt = LocalTime.of(6, 7, 8);
        byte[] b = new byte[6];
        ByteBuffer bb = ByteBuffer.wrap(b);

        Squish.put(bb, lt);
        bb.rewind();

        String s = new String(b, StandardCharsets.US_ASCII);
        assertEquals(s, "060708");
    }

    @Test
    public void testGetLocalTime() {
        LocalTime target = LocalTime.of(6, 7, 8);

        byte[] b = new byte[6];
        ByteBuffer bb = ByteBuffer.wrap(b);

        bb.put("060708".getBytes(StandardCharsets.US_ASCII));
        bb.rewind();

        LocalTime lt = Squish.getLocalTime(bb);

        assertEquals(lt, target);
    }
    
    @Test
    public void testPutSimpleLocalDateTime() {
        LocalDateTime ldt = LocalDateTime.of(2123, Month.APRIL, 5, 6, 7, 8, 123456789);
        byte[] b = new byte[14];
        ByteBuffer bb = ByteBuffer.wrap(b);

        Squish.putSimpleLocalDateTime(bb, ldt);
        bb.rewind();

        String s = new String(b, StandardCharsets.US_ASCII);

        assertEquals(s, "21230405060708");

    }

    @Test
    public void testGetSimpleLocalDateTime() {
        LocalDateTime target = LocalDateTime.of(2123, Month.APRIL, 5, 6, 7, 8);

        byte[] b = new byte[14];
        ByteBuffer bb = ByteBuffer.wrap(b);

        bb.put("21230405060708".getBytes(StandardCharsets.US_ASCII));
        bb.rewind();

        LocalDateTime ldt = Squish.getSimpleLocalDateTime(bb);

        System.out.println(ldt.toString());
        System.out.println(target.toString());
        assertEquals(ldt, target);
    }

    @Test
    public void testPutISOLocalDateTime() {
        LocalDateTime ldt = LocalDateTime.of(2123, Month.APRIL, 5, 6, 7, 8, 123456789);
        byte[] b = new byte[30];
        ByteBuffer bb = ByteBuffer.wrap(b);

        Squish.putISOLocalDateTime(bb, ldt);
        bb.rewind();

        String s = new String(b, 1, 29, StandardCharsets.US_ASCII);

        assertEquals(b[0], 29);
        assertEquals(s, "2123-04-05T06:07:08.123456789");

    }

    @Test
    public void testGetISOLocalDateTime() {
        LocalDateTime target = LocalDateTime.of(2123, Month.APRIL, 5, 6, 7, 8, 123456789);

        byte[] b = new byte[30];
        ByteBuffer bb = ByteBuffer.wrap(b);

        bb.put((byte) 29);
        bb.put("2123-04-05T06:07:08.123456789".getBytes(StandardCharsets.US_ASCII));
        bb.rewind();

        LocalDateTime ldt = Squish.getISOLocalDateTime(bb);

        System.out.println(ldt.toString());
        System.out.println(target.toString());
        assertEquals(ldt, target);
    }
}
