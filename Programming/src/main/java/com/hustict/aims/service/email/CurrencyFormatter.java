package com.hustict.aims.service.email;

import java.text.DecimalFormat;

public class CurrencyFormatter {
    public static String formatCurrency(int amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount);
    }
}
