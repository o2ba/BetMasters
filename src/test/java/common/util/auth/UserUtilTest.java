package common.util.auth;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class UserUtilTest {

    @Test
    void getAge() {
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
        LocalDate dateOfBirth2 = LocalDate.of(2002, Month.JUNE, 16);

        UserUtil userUtil = new UserUtil();
        int age = userUtil.getAge(dateOfBirth);
        assertEquals(34, age);
        assertEquals(21, userUtil.getAge(dateOfBirth2));
    }
}