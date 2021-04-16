package com.desafio.quality.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private DateUtil() {}

    public static LocalDate convertToLocalDate(String date) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(date, formatter);
    }

    public static boolean validate(LocalDate dateForm, LocalDate dateTo) {
        return dateForm.isBefore(dateTo);
    }

}
