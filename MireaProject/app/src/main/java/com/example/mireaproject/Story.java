package com.example.mireaproject;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

@Entity
public class Story {
    @PrimaryKey(autoGenerate = true)
    @NotNull
    public long id;
    public Date date;
    public String content;
}
