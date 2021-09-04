package com.deadmate.richorbroke.util;

import java.util.regex.Pattern;

public class Constants {

    public static final Pattern VALID_CURRENCY_CODE = Pattern.compile("[A-Z]{3}");
    public static final double EPSILON = 1E-9;

    private Constants() {
        // utility class
    }
}
