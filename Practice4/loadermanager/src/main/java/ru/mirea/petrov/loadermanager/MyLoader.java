package ru.mirea.petrov.loadermanager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.loader.content.AsyncTaskLoader;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MyLoader extends AsyncTaskLoader<String> {
    private String firstName;
    public static final String ARG_WORD = "word";
    public MyLoader(@NonNull Context context, Bundle args) {
        super(context);
        if (args != null)
            firstName= args.getString(ARG_WORD);
    }
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        List<String> characters = Arrays.asList(firstName.split(""));
        Collections.shuffle(characters, new Random(System.currentTimeMillis()));
        StringBuilder sb = new StringBuilder(firstName.length());
        for (String character : characters)
            sb.append(character);
        Log.d("loadInBackgroud()", sb.toString());

        return sb.toString();
    }
}
