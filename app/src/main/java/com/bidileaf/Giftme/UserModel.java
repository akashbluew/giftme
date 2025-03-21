package com.bidileaf.Giftme;

import java.security.SecureRandom;

public class UserModel {
    public String uid;
    public String email;
    public String phone;

    public String address;

    public UserModel() {
        // Default constructor required for Firebase
    }

    public UserModel(String uid, String email, String phone) {
        this.uid = uid;
        this.email = email;
        this.phone = phone;
    }
}

