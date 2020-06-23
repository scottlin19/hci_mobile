package ar.edu.itba.hci.ui.devices.actions;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.Room;
import ar.edu.itba.hci.api.models.devices.states.VacuumDeviceState;
import ar.edu.itba.hci.ui.rooms.RecyclerViewRoomsAdapter;
import ar.edu.itba.hci.ui.rooms.RoomsViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VacuumActions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VacuumActions extends Fragment {

    private final int[] colors = {Color.RED,Color.GREEN,Color.YELLOW};
    private final int[] icons = {R.drawable.ic_baseline_battery_alert_24,R.drawable.ic_baseline_battery_full_24,R.drawable.ic_baseline_battery_charging_full_24};
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> fetcherHandler;
    private VacuumDeviceState state;
    TextView batteryLevel;
    TextView errorTextView;
    SwitchMaterial switchMaterial;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "device";

    // TODO: Rename and change types of parameters
    private Device<VacuumDeviceState> device;
    private List<Room> rooms;

    public VacuumActions() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param device Parameter 1.
     * @return A new instance of fragment VacuumDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VacuumActions newInstance(Device<VacuumDeviceState> device) {
        VacuumActions fragment = new VacuumActions();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, device);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            device = (Device) getArguments().getParcelable(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment ;)
        return inflater.inflate(R.layout.fragment_vacuum_actions, container, false);
    }

@Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        updateDevice();
        updater();

        switchMaterial = (SwitchMaterial) getView().findViewById(R.id.switchMaterial);
    if(device.getState().getStatus().equals("on")){
        switchMaterial.setChecked(true);
    }
    switchMaterial.setOnClickListener(v -> {
        Object[] params = {};

        if(switchMaterial.isChecked()){
            System.out.printf("start");
            if(device.getState().getBatteryLevel() > 5) {
                registerAction("start", params);
                switchMaterial.setChecked(true);
            }
//                    device.getState().setStatus("on");
        }
        else {
            System.out.printf("pause");
            registerAction("pause",params);
            switchMaterial.setChecked(false);
//                    device.getState().setStatus("off");
        }
    });
        TextView textView = (TextView) getView().findViewById(R.id.battery_level);
        textView.setText(R.string.battery_level);

        batteryLevel = (TextView) getView().findViewById(R.id.battery_value);
        System.out.println(device.getState().getBatteryLevel());
        batteryLevel.setText(device.getState().getBatteryLevel().toString() + "%");

        errorTextView = (TextView) getView().findViewById(R.id.error_text);
        errorTextView.setVisibility(View.GONE);
        errorTextView.setTextColor(colors[0]);

        updateBatteryLevel();

        Button rechargeBtn = (Button) getView().findViewById(R.id.recharge_btn);
        rechargeBtn.setOnClickListener(v ->{
            Object[] params = {};
            registerAction("dock",params);
//            if(device.getState().getBatteryLevel() <= 5) switchMaterial.setClickable(false);
        });

        Spinner spinner = (Spinner) getView().findViewById(R.id.rooms_spinner);

        RoomsViewModel roomsViewModel = new ViewModelProvider(this).get(RoomsViewModel.class);
        roomsViewModel.getRooms().observe(getViewLifecycleOwner(), rooms -> {
            this.rooms = rooms;
            List<String> roomNames = rooms.stream().map(Room::getName).collect(Collectors.toList());
            roomNames.add("No room selected");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,roomNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            if(device.getState().getLocation() != null) {
                spinner.setSelection(roomNames.indexOf(device.getState().getLocation()));
            }
            else{
                spinner.setSelection(roomNames.size() - 1);
            }
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
    });

        super.onViewCreated(view, savedInstanceState);
    }

    private void registerAction(String action, Object[] params) {
        ApiClient.getInstance().executeAction(device.getId(), action, params, new Callback<Result<Object>>() {
            @Override
            public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                if (response.isSuccessful()) {
                    System.out.println("successful update on " + action);
                }
            }

            @Override
            public void onFailure(Call<Result<Object>> call, Throwable t) {
                Log.e("UPDATE AC ERROR", "error updating vacuum on action: " + action);
                Toast.makeText(getContext(), "error updating vacuum", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateBatteryLevel() {

        if(!device.getState().getStatus().equals("docked")){
            if(device.getState().getBatteryLevel() > 5) {
                batteryLevel.setTextColor(Color.GREEN);
                batteryLevel.setCompoundDrawablesWithIntrinsicBounds(0, 0, icons[1], 0);
                errorTextView.setVisibility(View.GONE);

            }
            else{
                batteryLevel.setTextColor(Color.RED);
                batteryLevel.setCompoundDrawablesWithIntrinsicBounds(0, 0, icons[0], 0);
                errorTextView.setText(getResources().getString(R.string.noBatteryError));
                errorTextView.setVisibility(View.VISIBLE);
            }
        }
        else{
            if(device.getState().getBatteryLevel() <= 5) {
                errorTextView.setText(getResources().getString(R.string.noBatteryError));
                errorTextView.setVisibility(View.VISIBLE);
            }
            else{
                errorTextView.setVisibility(View.GONE);
            }
            batteryLevel.setTextColor(Color.YELLOW);
            batteryLevel.setCompoundDrawablesWithIntrinsicBounds(0,0,icons[2],0);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(fetcherHandler != null && !fetcherHandler.isCancelled()) {
            fetcherHandler.cancel(true);
        }
    }

    public void updater() {
        if(fetcherHandler == null || fetcherHandler.isCancelled()) {
            Runnable fetcher = this::updateDevice;
            fetcherHandler = scheduler.scheduleAtFixedRate(fetcher, 2, 2, TimeUnit.SECONDS);
        }
    }

    public void updateDevice() {
        ApiClient.getInstance().getDevice(device.getId(), new Callback<Result<Device>>() {
            @Override
            public void onResponse(Call<Result<Device>> call, Response<Result<Device>> response) {
                device = response.body().getResult();
                state = (VacuumDeviceState) device.getState();
                updateView();
//                viewHandler();
            }

            @Override
            public void onFailure(Call<Result<Device>> call, Throwable t) {
                Log.e("update device", t.getLocalizedMessage());
                if(fetcherHandler != null && !fetcherHandler.isCancelled()) {
                    fetcherHandler.cancel(true);
                }
            }
        });
    }

    private void updateView() {
        updateBatteryLevel();

    }
}