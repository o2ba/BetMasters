package service.general.__deprecated.user;

import java.time.LocalDate;
import java.util.Calendar;

@Deprecated
public class UserUtil {
    public int getAge(LocalDate dateOfBirth) {
        Calendar today = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();
        birthDate.set(dateOfBirth.getYear(), dateOfBirth.getMonthValue(), dateOfBirth.getDayOfMonth());
        int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }
}
