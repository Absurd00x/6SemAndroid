package com.example.mireaproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface StoryDAO {
    @Insert
    void insert(Story story);
    @Delete
    void delete(Story story);
    @Query("SELECT * FROM story")
    List<Story> getStories();
}
