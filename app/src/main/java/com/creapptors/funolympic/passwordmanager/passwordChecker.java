package com.creapptors.funolympic.passwordmanager;

public class passwordChecker {

    public passwordChecker() {
    }

    public String lengthChecker(String password){
        int passwordLength = password.length();
        String passwordStrength = "";

        if (passwordLength < 8) {
            passwordStrength = "Weak";
            return passwordStrength;
        } else if (passwordLength < 12) {
            passwordStrength = "Moderate";
            return passwordStrength;
        } else if (passwordLength < 16) {
            passwordStrength = "Strong";
            return passwordStrength;
        } else {
            passwordStrength = "Very strong";
            return passwordStrength;
        }
    }
}
