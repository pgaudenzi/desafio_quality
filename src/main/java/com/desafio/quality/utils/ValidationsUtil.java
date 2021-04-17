package com.desafio.quality.utils;

import com.desafio.quality.dtos.BookingDto;
import com.desafio.quality.dtos.HotelDto;
import com.desafio.quality.exceptions.IllegalDateException;

import java.time.LocalDate;
import java.util.Set;
import java.util.regex.Pattern;

public class ValidationsUtil {

    private ValidationsUtil() {}

    /**
     * Method to validate the email format
     * @param email
     */
    public static void validateMailFormat(final String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);

        if (!pat.matcher(email).matches()) throw new IllegalArgumentException("Invalid email format");
    }

    /**
     * Method to validate dates
     * @throws IllegalDateException if dateFrom is not before dateTo
     */
    public static void validateDates(LocalDate dateForm, LocalDate dateTo) throws IllegalDateException {
        if (!dateForm.isBefore(dateTo)) throw new IllegalDateException("'Date from' must be before to 'date to'");
    }

    /**
     * Method to validate if the location exists
     * @param location that needs the validation
     * @param locations set of available locations
     */
    public static void validateLocation(String location, Set<String> locations) {
        if (!locations.contains(location)) throw new IllegalArgumentException("The chosen location does not exist");
    }

    public static void validateRoomType(int amount, String type) {
        switch (type.toLowerCase()) {
            case "single": if (amount != 1) throw new IllegalArgumentException("Single room is for 1 person only");
                            break;
            case "double": if (amount != 2) throw new IllegalArgumentException("Single room is for 2 people");
                            break;
            case "triple": if (amount != 3) throw new IllegalArgumentException("Single room is for 3 people");
                            break;
            case "multiple": if (amount >= 4 && amount <= 10 )
                                throw new IllegalArgumentException("Single room is for 1 person only");
                            break;
            default: throw new IllegalArgumentException("Invalid room type");
        }
    }

    /**
     * Method to validate if the hotel data in the booking are right
     */
    public static void validateHotel(final BookingDto booking, final HotelDto hotel) {
        if (!booking.getDestination().equalsIgnoreCase(hotel.getLocation())
                && !booking.getRoomType().equals(hotel.getRoomType())) {
            throw new IllegalArgumentException("The parameters do not match with the hotel data");
        }
    }

}
