package ru.mirea.petrov.simplefragmentapp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    Fragment fragment1, fragment2;
    FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
    }
    public void onClick(View v) {
        fm = getSupportFragmentManager();
        switch (v.getId()) {
            case R.id.btnFragment1:
                fm.beginTransaction().replace(R.id.fragmentContainer, fragment1).commit();
                break;
            case R.id.btnFragment2:
                fm.beginTransaction().replace(R.id.fragmentContainer, fragment2).commit();
                break;
            default:
                break;
        }
    }
}