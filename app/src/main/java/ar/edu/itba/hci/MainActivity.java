package ar.edu.itba.hci;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.crypto.spec.DESedeKeySpec;

import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.notifications.NotificationBroadcastReceiver;
import ar.edu.itba.hci.ui.devices.DeviceDetailsActivity;
import ar.edu.itba.hci.ui.routines.RoutinesFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

//    private void createNotificationChannel(){
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            CharSequence name = "Notifications";
//            String description = "Notifications Description";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,name,importance);
//            channel.setDescription(description);
//            channel.enableLights(true);
//            channel.setLightColor(Color.GREEN);
//            channel.enableVibration(true);
//            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
//            channel.setVibrationPattern(new long[]{100,200,300,400,500,400,300,200,400});
//
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//
//        }
//    }

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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor =sharedPreferences.edit();

        ApiClient.getInstance().getDevices(new Callback<Result<List<Device>>>() {
            @Override
            public void onResponse(Call<Result<List<Device>>> call, Response<Result<List<Device>>> response) {
                if(response.isSuccessful()){
                    List<Device> deviceList = response.body().getResult();
                    Set<String> deviceSet = new HashSet<>();
                    Gson gson = new Gson();
                    deviceList.forEach(device ->{
                        deviceSet.add(gson.toJson(device));
                    });
                    editor.putStringSet("devices",deviceSet);
                    editor.apply();
              }
            }

            @Override
            public void onFailure(Call<Result<List<Device>>> call, Throwable t) {

            }
        });
        //Toast.makeText(getApplicationContext(), "On Pause", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        Toast.makeText(getApplicationContext(), "On Stop", Toast.LENGTH_SHORT).show();
    }
}