package com.example.birthday.subscriptionservice.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class BirthdayUtilsTest {
    @Test
    void rollsBirthdayToNextYear() {
        assertThat(BirthdayUtils.daysUntil(
                LocalDate.of(2000, 1, 10), LocalDate.of(2026, 1, 11))).isEqualTo(364);
    }

    @Test
    void mapsLeapBirthdayToFebruary28() {
        assertThat(BirthdayUtils.nextBirthday(
                LocalDate.of(2000, 2, 29), LocalDate.of(2025, 2, 1)))
                .isEqualTo(LocalDate.of(2025, 2, 28));
    }
}
