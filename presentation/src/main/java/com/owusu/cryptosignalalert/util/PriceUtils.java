package com.owusu.cryptosignalalert.util;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by brightowusu on 27/01/2018.
 */

public class PriceUtils {

    private static Map<String, String> symbolMap;
    private static final String BTC = ConstantsUI.Companion.getBTC();
    private static final String ETH = ConstantsUI.Companion.getETH();
    static
    {
        try {
            Map<String, String> toret = new HashMap<>();
            Locale[] locs = Locale.getAvailableLocales();

            for(Locale loc : locs) {
                try {
                    Currency currency = Currency.getInstance( loc );

                    if ( currency != null ) {
                        toret.put(currency.getCurrencyCode(), currency.getSymbol());
                    }
                } catch(Exception exc)
                {
                    //exc.printStackTrace();
                }
            }


            symbolMap = toret;
            symbolMap.put("%", "%");
            symbolMap.put(BTC, BTC);
            symbolMap.put(ETH, ETH);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String numberFormatter(String currency, String val) {
        String symbol = symbolMap.get(currency);
        if(val == null || val.trim().length() == 0) {
            return symbol+"TBA";
        }


        double number = Double.valueOf(val);
        DecimalFormat decimalFormat = new DecimalFormat("##0.00");
        String numberAsString = decimalFormat.format(number);

        // redo formatting
        if(numberAsString.equals("0.00")) {
            numberAsString = showUpTo6DecimalPlaces(val);
        } else if(numberAsString.endsWith(".00")) {
            numberAsString = numberAsString.substring(0,numberAsString.length() - ".00".length());
        }


        if(symbol.equals("%")) {
            return numberAsString+symbol;
        }

        if(symbol.equals(BTC) || symbol.equals(ETH)) {
            return numberAsString+" "+symbol;
        }

        return symbol+numberAsString;

//        //return symbol+String.valueOf(new BigDecimal(val).setScale(2, RoundingMode.HALF_UP));
//        String result = symbol+String.format ("%,.2f", Double.valueOf(val));
//        String result = symbol+String.format ("%,.2f", Double.valueOf(val));
//        if(result.endsWith(".00")) {
//            result = result.substring(0,result.length() - ".00".length());
//        }
//        return result;
        //return  symbol+NumberFormat.getNumberInstance().format(Double.parseDouble(val));
    }

    private String replaceLast(String string, String from, String to) {
        int lastIndex = string.lastIndexOf(from);
        if (lastIndex < 0) return string;
        String tail = string.substring(lastIndex).replaceFirst(from, to);
        return string.substring(0, lastIndex) + tail;
    }

    private static String showUpTo6DecimalPlaces(String val) {

        String decimalToEnd = val.substring(val.indexOf(".")+1, val.length());
        //System.out.println(decimalToEnd);
        int numberOfDecimalPlaces = decimalToEnd.length();

        if(numberOfDecimalPlaces >5) {
            numberOfDecimalPlaces = 6;
        }

        //System.out.println(String.format ("%,."+numberOfDecimalPlaces+"f", Double.valueOf(val)));

        return String.format ("%,."+numberOfDecimalPlaces+"f", Double.valueOf(val));
    }

}
