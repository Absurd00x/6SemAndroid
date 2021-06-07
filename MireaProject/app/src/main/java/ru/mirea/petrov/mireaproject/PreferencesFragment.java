package ru.mirea.petrov.mireaproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreferencesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreferencesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private final String TAG = PreferencesFragment.class.getSimpleName();
    private final String keyLanguage = "language";
    private final String keyBackgroud = "background";
    private final String keyFont = "font";
    private final String keySize = "size";
    private FragmentActivity fa;
    private TextView text;
    private ConstraintLayout cl;
    private boolean isEnglish;
    private SharedPreferences preferences;
    private final String russianSampleText = "Съешь ещё этих мягких французских булок, да выпей же чаю.";
    private final String englishSampleText = "The quick brown fox jumps over a lazy dog";
    private boolean isDarkmode;
    private int size;
    private int font;

    public PreferencesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PreferencesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PreferencesFragment newInstance(String param1, String param2) {
        PreferencesFragment fragment = new PreferencesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        fa = getActivity();
    }

    private void update() {
        text.setText(isEnglish ? englishSampleText : russianSampleText);
        cl.setBackgroundColor(isDarkmode ? Color.DKGRAY : Color.WHITE);
        text.setTextSize((float)size);
        text.setTypeface(null, font);
    }

    private void save() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(keyLanguage, isEnglish);
        editor.putBoolean(keyBackgroud, isDarkmode);
        editor.putInt(keySize, size);
        editor.putInt(keyFont, font);
        editor.apply();
    }

    @Override
    public void onPause() {
        super.onPause();
        save();
    }

    private void load() {
        isEnglish = preferences.getBoolean(keyLanguage, false);
        isDarkmode = preferences.getBoolean(keyBackgroud, false);
        size = preferences.getInt(keySize, 14);
        font = preferences.getInt(keyFont, Typeface.NORMAL);
    }

    @Override
    public void onStart() {
        super.onStart();
        text = fa.findViewById(R.id.sampleTextView);
        cl = fa.findViewById(R.id.background);
        preferences = fa.getPreferences(Context.MODE_PRIVATE);

        Button colorButton = fa.findViewById(R.id.buttonColor);
        colorButton.setOnClickListener(this::onColorClick);
        Button sizeButton = fa.findViewById(R.id.buttonSize);
        sizeButton.setOnClickListener(this::onSizeClick);
        Button languageButton = fa.findViewById(R.id.buttonLanguage);
        languageButton.setOnClickListener(this::onLanguageClick);
        Button fontButton = fa.findViewById(R.id.buttonFont);
        fontButton.setOnClickListener(this::onFontClick);

        load();
        update();
    }

    public void onLanguageClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(fa);
        builder.setTitle("Выберите язык")
                .setItems(new String[]{"Русский", "English"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isEnglish = (which == 1);
                        update();
                    }
                });
        builder.show();
    }

    public void onColorClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(fa);
        builder.setTitle("Выберите цвет")
               .setItems(new String[]{"Чёрный", "Белый"}, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       isDarkmode = (which == 0);
                       update();
                   }
               });
        builder.show();
    }

    public void onSizeClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(fa);
        String[] sizes = new String[30];
        for(int i = 0; i < 30; ++i)
            sizes[i] = String.valueOf(i + 1);
        builder.setTitle("Выберите размер")
               .setItems(sizes, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       size = which;
                       update();
                   }
               });
        builder.show();
    }

    public void onFontClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(fa);
        builder.setTitle("Выберите Шрифт")
                .setItems(new String[]{"Обычный", "Жирный", "Курсив", "Жирный курсив"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        font = which;
                        update();
                    }
                });
        builder.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preferences, container, false);
    }
}