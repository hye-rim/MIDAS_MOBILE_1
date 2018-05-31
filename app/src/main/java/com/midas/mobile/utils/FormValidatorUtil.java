package com.midas.mobile.utils;

/**
 * Created by hyerim on 2018. 5. 26....
 */
public class FormValidatorUtil {
    public static boolean validateStringIsEmpty(String str) {
        if (str == null || str.trim().length() < 1) {
            return true;
        }
        return false;
    }
}
