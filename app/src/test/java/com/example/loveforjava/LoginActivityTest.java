package com.example.loveforjava;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class LoginActivityTest {
    LoginActivity loginActivity = new LoginActivity();

    @Test
    public void signUpTestProper() {
        String name = "John";
        String email = "john@ualberta.ca";
        int status = loginActivity.signUp(name, email);
        Assert.assertEquals(1, status);
    }

    @Test
    public void signUpTestImproper() {
        String name = "John";
        String email = "johnualberta.ca";
        int status = loginActivity.signUp(name, email);
        Assert.assertEquals(0, status);
    }

    @Test
    public void infoValidationTest() {
        String name = "John";
        String email = "johnualberta.ca";
        ArrayList<Boolean> validation = loginActivity.infoValidation(name, email);
        Assert.assertTrue(validation.get(0));
        Assert.assertFalse(validation.get(1));
    }
}
