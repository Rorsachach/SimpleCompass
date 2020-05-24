package com.example.xiaomicompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActionBar;
import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;

public class MainActivity extends TabActivity {

    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        //checkPermission();

    }

    /**
     * 权限申请部分
     */

    private static final String[] PERMISSIONS ={
            Manifest.permission.CAMERA
    };

    private static final int REQUEST_COUNT = 12345;

    private static final int PERMISSION_COUNT = 1;

    private void checkPermission(){
        for (int i = 0; i < PERMISSION_COUNT; i++) {
            if(ContextCompat.checkSelfPermission(this, PERMISSIONS[i]) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS[i])){

                }else {
                    Toast.makeText(this, "请允许获取此权限", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions( this,PERMISSIONS,i);
                }

            }
        }
    }

}
