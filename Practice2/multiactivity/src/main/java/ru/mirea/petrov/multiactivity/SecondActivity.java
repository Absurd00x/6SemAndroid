package ru.mirea.petrov.multiactivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import ru.mirea.petrov.multiactivity.databinding.ActivitySecondBinding;
import com.google.android.material.snackbar.Snackbar;

public class SecondActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivitySecondBinding binding;
    private String TAG = MainActivity.class.getSimpleName();
    private TextView tw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_second);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    protected void onClickOldActivity(View view) {
        Intent intent = new Intent(SecondActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        tw = findViewById(R.id.textView2);
        Log.i(TAG, "about to get intent");
        Intent intent = getIntent();
        Log.i(TAG, "got intent, extracting message");
        String text = (String)intent.getStringExtra("key");
        Log.i(TAG, "extracted message, trying to set text");
        tw.setText(text);
        Log.i(TAG, "text set, all done");
        Button returnButton = (Button)findViewById(R.id.returnButton);
        returnButton.setOnClickListener(v -> onClickOldActivity(returnButton));
    }

    @Override
    protected void onResume() {
        super.onResume();
        tw.setText("Just resumed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        tw.setText("Just paused");
    }

    @Override
    protected void onStop() {
        super.onStop();
        tw.setText("Just stopped");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_second);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}