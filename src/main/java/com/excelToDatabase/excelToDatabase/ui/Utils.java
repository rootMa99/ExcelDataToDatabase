package com.excelToDatabase.excelToDatabase.ui;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class Utils {

    private final Random random= new SecureRandom();
    private final String ALPHABET= "0123456789ABCDEFGHIJKLMNOPQRSTUWVXYZabcdefghijklmnopqrstuwvxyz";
    //private final int ITERATIONS=1000;
    //private final int KEY_LENGTH=256;

    private String generatedRandomString(int length){
        StringBuilder returnValue= new StringBuilder(length);

        for (int i=0; i<length; i++){
            returnValue.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return new String(returnValue);
    }
    public String getFormationId(int length){
        return generatedRandomString(length);
    }
}
