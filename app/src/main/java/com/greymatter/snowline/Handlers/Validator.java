package com.greymatter.snowline.Handlers;

public class Validator {
    public static boolean validateStopNumber(String stopNumber){
        boolean isValid = true;
        for(int i=0;i<stopNumber.length();i++){
            if(stopNumber.charAt(i)<48 || stopNumber.charAt(i)>57){
                isValid = false;
            }
        }
        return isValid;
    }
}
