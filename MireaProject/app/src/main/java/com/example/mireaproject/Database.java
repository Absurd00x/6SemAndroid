package com.example.mireaproject;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@androidx.room.Database(entities = {Contact.class, Story.class}, version = 1, exportSchema = false)
@TypeConverters({DateTypeConverter.class})
public abstract class Database extends RoomDatabase {
    public abstract ContactDAO getContactDAO();
    public abstract StoryDAO getStoryDAO();

    private static final String DB_NAME = "database.db";
    private static volatile Database instance;

    static synchronized Database getInstance(Context context) {
        if (instance == null)
            instance = create(context);
        return instance;
    }

    public Database(){};

    private static Database create(final Context context) {
        return Room.databaseBuilder(
                context,
                Database.class,
                DB_NAME)
                .allowMainThreadQueries()
                .build();
    }
}
