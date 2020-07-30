package com.example.xiaomicompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActionBar;
import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;

public class MainActivity extends TabActivity {

    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        tabHost = getTabHost();
        tabHost.setup();

        TabHost.TabSpec tabSpec1 = tabHost
                .newTabSpec("tab1")
                .setIndicator("指南针")
                .setContent(new Intent(this, CompassActivity.class));

        tabHost.addTab(tabSpec1);

        TabHost.TabSpec tabSpec2 = tabHost
                .newTabSpec("tab2")
                .setIndicator("水平仪")
                .setContent(new Intent(this, SpiritLevelActivit.class));

        tabHost.addTab(tabSpec2);

    }

}
