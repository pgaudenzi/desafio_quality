package com.desafio.quality.utils;

import com.desafio.quality.dtos.PaymentMethodDto;

public class PaymentUtil {

    private PaymentUtil() {}

    /**
     * Aux method to calculate card interest
     */
    public static double calculateInterests(PaymentMethodDto paymentMethod) {

        if (paymentMethod.getType().equalsIgnoreCase("credit")) {
            if (paymentMethod.getDues() < 3) return  1.05;
            if (paymentMethod.getDues() >= 3 && paymentMethod.getDues() <= 6) return 1.10;
            throw new IllegalArgumentException("Max dues allowed is 6");
        }

        if (paymentMethod.getType().equalsIgnoreCase("debit")) {
            if (paymentMethod.getDues() != 0)
                throw new IllegalArgumentException("dues are not allowed with debit card");
            return 0.0;
        }

        throw new IllegalArgumentException("Only credit or debit allowed");
    }

}
