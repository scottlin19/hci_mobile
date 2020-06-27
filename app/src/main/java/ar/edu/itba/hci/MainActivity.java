package ar.edu.itba.hci;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ar.edu.itba.hci.api.notifications.NotificationBroadcastReceiver;
import ar.edu.itba.hci.api.notifications.SharedPreferencesHelper;

public class MainActivity extends AppCompatActivity {
    NavHostFragment navHostFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpNavigation();

        startAlert();
    }

    public void setUpNavigation(){

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        navHostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.getNavController());
    }

    @Override
    protected void onStart() {
        super.onStart();
        int theme = getSharedPreferences("smarthome.darkmode", MODE_PRIVATE).getInt("smarthome.darkmode", -1);
        if(theme == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }


    private void startAlert() {
        Intent intent = new Intent(NotificationBroadcastReceiver.DEVICE_NOTIFICATION);
        intent.setPackage(getApplicationContext().getPackageName());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 60000, pendingIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferencesHelper.refreshSavedPreferences(getApplicationContext());
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("main activity on result");
        super.onActivityResult(requestCode, resultCode, data);
    }
}