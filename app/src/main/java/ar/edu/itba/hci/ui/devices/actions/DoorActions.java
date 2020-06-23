package ar.edu.itba.hci.ui.devices.actions;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.devices.states.DoorDeviceState;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoorActions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoorActions extends Fragment {

    private static final String ARG_PARAM = "device";

    private Device device;
    private DoorDeviceState state;
    private SwitchMaterial switchStatus, switchLock;
    private TextView statusText;
    private ImageView lockImg;

    public DoorActions() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param device Parameter 1.
     * @return A new instance of fragment VacuumDetailFragment.
     */
    public static DoorActions newInstance(@NonNull Device device) {
        DoorActions fragment = new DoorActions();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, device);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        device = getArguments().getParcelable(ARG_PARAM);
        state = (DoorDeviceState) device.getState();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_door_actions, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        switchStatus = getActivity().findViewById(R.id.status_switch);
        switchLock = getActivity().findViewById(R.id.lock_switch);
        statusText = getActivity().findViewById(R.id.state_text);
        lockImg = getActivity().findViewById(R.id.lock_img);
        updateDevice();
        switchStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String action;
            if(isChecked) {
                action = "close";
            }
            else {
                action = "open";

            }

            ApiClient.getInstance().executeAction(device.getId(), action, new Object[0], new Callback<Result<Object>>() {
                @Override
                public void onResponse(@NonNull Call<Result<Object>> call,@NonNull Response<Result<Object>> response) {
                    updateDevice();
                }

                @Override
                public void onFailure(@NonNull Call<Result<Object>> call,@NonNull Throwable t) {
                    Log.e("switch status", t.getLocalizedMessage());
                }
            });
        });

        switchLock.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String action;
            if(isChecked) {
                action = "lock";
            }
            else {
                action = "unlock";
            }

            ApiClient.getInstance().executeAction(device.getId(), action, new Object[0], new Callback<Result<Object>>() {
                @Override
                public void onResponse(@NonNull Call<Result<Object>> call,@NonNull Response<Result<Object>> response) {
                    updateDevice();
                }

                @Override
                public void onFailure(@NonNull Call<Result<Object>> call,@NonNull Throwable t) {
                    Log.e("switch lock", t.getLocalizedMessage());
                }
            });
        });
    }

    public void viewHandler() {
        if(state == null) return;
        if(state.getStatus().equals("closed")) {
            switchStatus.setChecked(true);
            statusText.setText(getResources().getString(R.string.closed));
        }
        else {
            switchStatus.setChecked(false);
            statusText.setText(getResources().getString(R.string.opened));
        }

        if(state.getLock().equals("locked")) {
            switchLock.setChecked(true);
            lockImg.setImageResource(R.drawable.ic_outline_lock_24);
        }
        else {
            switchLock.setChecked(false);
            lockImg.setImageResource(R.drawable.ic_outline_lock_open_24);
        }
    }

    public void updateDevice() {
        ApiClient.getInstance().getDevice(device.getId(), new Callback<Result<Device>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Device>> call,@NonNull Response<Result<Device>> response) {
                device = response.body().getResult();
                state = (DoorDeviceState) device.getState();
                viewHandler();
            }

            @Override
            public void onFailure(@NonNull Call<Result<Device>> call, @NonNull Throwable t) {
                Log.e("update device", t.getLocalizedMessage());
            }
        });
    }
}