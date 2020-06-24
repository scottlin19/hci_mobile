package ar.edu.itba.hci.ui.devices.actions;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorListener;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.devices.states.LampDeviceState;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LampActions#newInstance} factory method to
 * create an instance of this fragment.
 */

public class LampActions extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "device";

    private Device device;
    private LampDeviceState state;
    private SwitchMaterial statusSwitch;
    private ColorPickerView colorPicker;
    private Slider slider;

    public LampActions() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param device Parameter 1.
     * @return A new instance of fragment VacuumDetailFragment.
     */
    public static LampActions newInstance(Device device) {
        LampActions fragment = new LampActions();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, device);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            device = getArguments().getParcelable(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lamp_actions, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        statusSwitch = getActivity().findViewById(R.id.switchMaterial);
        colorPicker = getActivity().findViewById(R.id.color_picker_view);
        slider = getActivity().findViewById(R.id.b_slider);

        updateDevice();

        statusSwitch.setOnClickListener(v -> {
            String action;
            if(statusSwitch.isChecked()) action = "turnOn";
            else action = "turnOff";

            ApiClient.getInstance().executeAction(device.getId(), action, new Object[0], new Callback<Result<Object>>() {
                @Override
                public void onResponse(@NonNull Call<Result<Object>> call, @NonNull Response<Result<Object>> response) {
                    updateDevice();
                }

                @Override
                public void onFailure(@NonNull Call<Result<Object>> call,@NonNull Throwable t) {
                    Log.e("light switch", t.getLocalizedMessage());
                }
            });
        });

        colorPicker.setColorListener((ColorListener) (color, fromUser) -> {
            String[] hexColor = new String[1];
            hexColor[0] = String.format("#%06X", (0xFFFFFF & color));

            if(fromUser) {
                ApiClient.getInstance().executeAction(device.getId(), "setColor", hexColor, new Callback<Result<Object>>() {
                    @Override
                    public void onResponse(@NonNull Call<Result<Object>> call, @NonNull Response<Result<Object>> response) {
                        System.out.println(response.body().getResult());
                        updateDevice();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Result<Object>> call, @NonNull Throwable t) {
                        Log.e("color picker", t.getLocalizedMessage());
                    }
                });
            }
        });

        slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                Integer[] body = new Integer[1];
                body[0] = (int) slider.getValue();
                ApiClient.getInstance().executeAction(device.getId(), "setBrightness", body, new Callback<Result<Object>>() {
                    @Override
                    public void onResponse(@NonNull Call<Result<Object>> call, @NonNull Response<Result<Object>> response) {
                        updateDevice();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Result<Object>> call, @NonNull Throwable t) {
                        Log.e("Slider brightness", t.getLocalizedMessage());
                    }
                });
            }
        });
    }

    public void updateDevice() {
        ApiClient.getInstance().getDevice(device.getId(), new Callback<Result<Device>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Device>> call, @NonNull Response<Result<Device>> response) {
                device = response.body().getResult();
                state = (LampDeviceState) device.getState();
                viewHandler();
            }

            @Override
            public void onFailure(@NonNull Call<Result<Device>> call, @NonNull Throwable t) {
                Log.e("update device", t.getLocalizedMessage());
            }
        });
    }

    public void viewHandler() {
        if(state.getStatus().equals("on")) statusSwitch.setChecked(true);
        else statusSwitch.setChecked(false);
        StringBuilder color = new StringBuilder(state.getColor());
        if(color.length() > 7) color.setLength(7);
        colorPicker.selectByHsv(Color.parseColor(color.toString()));
        slider.setValue(state.getBrightness());
    }
}