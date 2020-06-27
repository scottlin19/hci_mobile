package ar.edu.itba.hci.api.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.bluetooth.BluetoothClass;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NavUtils;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ar.edu.itba.hci.MainActivity;
import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.DeviceDeserializer;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.DeviceType;
import ar.edu.itba.hci.api.models.IconAdapter;
import ar.edu.itba.hci.api.models.devices.AcDevice;
import ar.edu.itba.hci.api.models.devices.AlarmDevice;
import ar.edu.itba.hci.api.models.devices.BlindsDevice;
import ar.edu.itba.hci.api.models.devices.DoorDevice;
import ar.edu.itba.hci.api.models.devices.FaucetDevice;
import ar.edu.itba.hci.api.models.devices.FridgeDevice;
import ar.edu.itba.hci.api.models.devices.LampDevice;
import ar.edu.itba.hci.api.models.devices.OvenDevice;
import ar.edu.itba.hci.api.models.devices.SpeakerDevice;
import ar.edu.itba.hci.api.models.devices.VacuumDevice;
import ar.edu.itba.hci.api.models.devices.states.DeviceState;

import ar.edu.itba.hci.ui.devices.DeviceDetailsActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationBroadcastReceiver extends BroadcastReceiver {

    public static final String CHANNEL_ID = "General notifications";
    public static final String DEVICE_NOTIFICATION = "ar.edu.itba.hci.DEVICE_NOTIFICATION";
    public static String PACKAGE_NAME;

    private NotificationManager manager;
    private NotificationCompat.Builder builder;
    private Resources resources;


    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("//////////////////////////////////////////////////// BROADCAST RECEIVER /////////////////////////////////////////////////");
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PACKAGE_NAME = intent.getPackage();

        // Register the channel with the system (required by Android 8.0 - Oreo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String description = "Device notification channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, importance);
            channel.setDescription(description);
            manager.createNotificationChannel(channel);
        }
        resources = context.getResources();
        notificationsDispatcher(context);
        //manager.notify(SPEAKER_NOTIFICATION_ID, builder.build());
    }


    private Device getDeviceFromJson(String json) {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(json);
        Gson gson = new Gson();
        final JsonObject jsonObject = jsonElement.getAsJsonObject();

        final JsonElement jsonElementType = jsonObject.get("type");

        DeviceType deviceType = gson.fromJson(jsonElementType, DeviceType.class);

        if (deviceType.getId().equals("c89b94e8581855bc")) {
            return gson.fromJson(jsonObject, new TypeToken<SpeakerDevice>() {
            }.getType());
        } else if (deviceType.getId().equals("dbrlsh7o5sn8ur4i")) {
            return gson.fromJson(jsonObject, new TypeToken<FaucetDevice>() {
            }.getType());

        } else if (deviceType.getId().equals("eu0v2xgprrhhg41g")) {
            return gson.fromJson(jsonObject, new TypeToken<BlindsDevice>() {
            }.getType());
        } else if (deviceType.getId().equals("go46xmbqeomjrsjr")) {
            return gson.fromJson(jsonObject, new TypeToken<LampDevice>() {
            }.getType());
        } else if (deviceType.getId().equals("im77xxyulpegfmv8")) {
            return gson.fromJson(jsonObject, new TypeToken<OvenDevice>() {
            }.getType());
        } else if (deviceType.getId().equals("li6cbv5sdlatti0j")) {
            return gson.fromJson(jsonObject, new TypeToken<AcDevice>() {
            }.getType());
        } else if (deviceType.getId().equals("lsf78ly0eqrjbz91")) {
            return gson.fromJson(jsonObject, new TypeToken<DoorDevice>() {
            }.getType());
        } else if (deviceType.getId().equals("mxztsyjzsrq7iaqc")) {
            return gson.fromJson(jsonObject, new TypeToken<AlarmDevice>() {
            }.getType());
        } else if (deviceType.getId().equals("ofglvd9gqx8yfl3l")) {
            return gson.fromJson(jsonObject, new TypeToken<VacuumDevice>() {
            }.getType());
        } else if (deviceType.getId().equals("rnizejqr2di0okho")) {
            return gson.fromJson(jsonObject, new TypeToken<FridgeDevice>() {
            }.getType());
        }
        return null;
    }

    public void notificationBuilder(Context context,@NonNull DeviceState savedDevice, @NonNull Device fetchedDevice, Class<? extends DeviceState> stateClass) {
        String[] descriptions = stateClass.cast(savedDevice).compare(fetchedDevice.getState(), resources);


        if (descriptions != null && descriptions.length != 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < descriptions.length; i++) {
                sb.append(descriptions[i] + '\n');
            }
            sb.setLength(sb.length() - 1);
            Intent notificationIntent = new Intent(context, DeviceDetailsActivity.class);
            notificationIntent.putExtra("device", fetchedDevice.getId());
            notificationIntent.setAction("dummy_action_" + fetchedDevice.getId());

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(DeviceDetailsActivity.class);
            stackBuilder.addNextIntent(notificationIntent);

            final PendingIntent contentIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            // Set notification channel id (required by Android 8.0 - Oreo)
            builder = new NotificationCompat.Builder(context.getApplicationContext(), CHANNEL_ID)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.appicon))
                    .setContentIntent(contentIntent)
                    .setContentTitle(fetchedDevice.getName())
                    .setSmallIcon(IconAdapter.getIntIcon(fetchedDevice.getMeta().getIcon()))
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(sb));
            manager.notify(fetchedDevice.getId().hashCode(), builder.build());


            Log.v("Notification", String.format("no changes on %s", fetchedDevice.getName()));
        }
    }

    public void notificationsDispatcher(Context context) {

        //TODO: get saved devices from the local database
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Set<String> devicesJson = sharedPreferences.getStringSet("devices", null);
      /*  System.out.println("Devices json: " + devicesJson);*/
        //System.out.println(String.format("Devices json size: %d", devicesJson.size()));
        List<Device> savedDevices = new ArrayList<>();


        Gson gson = new Gson();
        if (devicesJson != null) {
            devicesJson.forEach(d -> {

                Device device = getDeviceFromJson(d);
                System.out.println(device);
                savedDevices.add(device);
            });

            System.out.println("Saved devices: " + savedDevices);


            ApiClient.getInstance().getDevices(new Callback<Result<List<Device>>>() {
                @Override
                public void onResponse(Call<Result<List<Device>>> call, Response<Result<List<Device>>> response) {
                    List<Device> fetchedDevices = response.body().getResult();
                    List<Device> auxList;


                    //                fetchedDevices.stream().filter(d -> d.getMeta().getNotifStatus()).close();
                    // final Class<?> objectsType = state.getClass();

                    for (Device savedDevice : savedDevices) {
                        auxList = fetchedDevices.stream().filter(d -> d.getId().equals(savedDevice.getId())).collect(Collectors.toList());
                        if (auxList.size() != 0) {
                            DeviceState devState = null;
                            Class<? extends DeviceState> stateClass = null;

                            switch (savedDevice.getType().getId()) {
                                case "go46xmbqeomjrsjr":
                                    devState = ((LampDevice) savedDevice).getState();
                                    stateClass = ((LampDevice) savedDevice).getState().getClass();
                                    break;
                                case "c89b94e8581855bc":
                                    devState = ((SpeakerDevice) savedDevice).getState();
                                    stateClass = ((SpeakerDevice) savedDevice).getState().getClass();
                                    break;
                                case "ofglvd9gqx8yfl3l":
                                    devState = ((VacuumDevice) savedDevice).getState();
                                    stateClass = ((VacuumDevice) savedDevice).getState().getClass();
                                    break;
                                case "rnizejqr2di0okho":
                                    devState = ((FridgeDevice) savedDevice).getState();
                                    stateClass = ((FridgeDevice) savedDevice).getState().getClass();
                                    break;
                                case "im77xxyulpegfmv8":
                                    devState = ((OvenDevice) savedDevice).getState();
                                    stateClass = ((OvenDevice) savedDevice).getState().getClass();
                                    break;
                                case "dbrlsh7o5sn8ur4i":
                                    devState = ((FaucetDevice) savedDevice).getState();
                                    stateClass = ((FaucetDevice) savedDevice).getState().getClass();
                                    break;
                                case "eu0v2xgprrhhg41g":
                                    devState = ((BlindsDevice) savedDevice).getState();
                                    stateClass = ((BlindsDevice) savedDevice).getState().getClass();
                                    break;
                                case "li6cbv5sdlatti0j":
                                    devState = ((AcDevice) savedDevice).getState();
                                    stateClass = ((AcDevice) savedDevice).getState().getClass();
                                    break;
                                case "mxztsyjzsrq7iaqc":
                                    devState = ((AlarmDevice) savedDevice).getState();
                                    stateClass = ((AlarmDevice) savedDevice).getState().getClass();
                                    break;
                                case "lsf78ly0eqrjbz91":
                                    devState = ((DoorDevice) savedDevice).getState();
                                    stateClass = ((DoorDevice) savedDevice).getState().getClass();
                                    break;
                            }

                            notificationBuilder(context,devState, auxList.get(0), stateClass);
                        } else
                            Log.e("Notification", "device not found!!");
                    }
                    SharedPreferencesHelper.saveToSharedPreferences(context,fetchedDevices);
                }

                @Override
                public void onFailure(Call<Result<List<Device>>> call, Throwable t) {
                    Log.e("Notification dispatcher", t.getLocalizedMessage());
                }
            });
        }
    }


/*    private void saveToSharedPreferences(Context context,List<Device> deviceList){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        Set<String> devicesSet = new HashSet<>();

        deviceList.forEach(d -> {
            if(d.getMeta().getNotifStatus())
                devicesSet.add(gson.toJson(d));
        });

        editor.putStringSet("devices", devicesSet);
        editor.apply();
    }*/


}
