package com.divax.chatfree;

public class user {
    String Gender,Phone,Name;




    public user(String Phone, String Gender) {

        this.Phone = Phone;
        this.Gender = Gender;
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

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }
}
