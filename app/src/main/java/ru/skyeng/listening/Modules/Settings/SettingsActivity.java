package ru.skyeng.listening.Modules.Settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ru.skyeng.listening.R;

public class SettingsActivity extends AppCompatActivity {

    private SettingsFragment mFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
}
