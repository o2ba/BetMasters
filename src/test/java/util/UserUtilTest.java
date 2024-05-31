package util;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class UserUtilTest {

    void getAge() {
        UserUtil userUtil = new UserUtil();
        assertEquals(24, userUtil.getAge(new Date(1997, 1, 1)));
        assertEquals(23, userUtil.getAge(new Date(2000, 12, 31)));
        assertEquals(21, userUtil.getAge(new Date(2000, 12, 30)));
        assertEquals(21, userUtil.getAge(new Date(2000, 1, 2)));
    }

}