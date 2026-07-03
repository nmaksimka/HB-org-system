package com.example.birthday.subscriptionservice.util;

import java.time.*;
import java.time.temporal.ChronoUnit;

public final class BirthdayUtils {
    private BirthdayUtils() {}

    public static LocalDate nextBirthday(LocalDate birthDate, LocalDate today) {
        LocalDate candidate = inYear(birthDate, today.getYear());
        return candidate.isBefore(today) ? inYear(birthDate, today.getYear() + 1) : candidate;
    }

    public static long daysUntil(LocalDate birthDate, LocalDate today) {
        return ChronoUnit.DAYS.between(today, nextBirthday(birthDate, today));
    }

    private static LocalDate inYear(LocalDate birthDate, int year) {
        if (birthDate.getMonth() == Month.FEBRUARY
                && birthDate.getDayOfMonth() == 29 && !Year.isLeap(year)) {
            return LocalDate.of(year, Month.FEBRUARY, 28);
        }
        return birthDate.withYear(year);
    }
}
