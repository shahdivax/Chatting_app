package com.divax.chatfree;

public class user {
    String Phone,Name;




    public user(String Phone) {

        this.Phone = Phone;
    }

    public user(){}

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
