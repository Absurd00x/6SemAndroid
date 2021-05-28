package com.example.mireaproject;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

@Entity(tableName = "contact")
public class Contact {
    public String firstName, lastName;

    @PrimaryKey
    @NotNull
    private String phoneNumber;
    public Date dateCreated;

    @NotNull
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotNull String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
