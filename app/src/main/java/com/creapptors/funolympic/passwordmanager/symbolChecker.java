package com.creapptors.funolympic.passwordmanager;

import android.widget.Toast;

public class symbolChecker {

    public symbolChecker() {
    }

    public String regexCheck(String password){
        String regex = "^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]+$";
        String regResult = null;
        if (password.matches(regex)) {
            return password;
        } else {
            regResult = "Password must contain " +
                    "at least one digit and one special character (!@#$%^&*)";
            return regResult;
        }
    }
}
