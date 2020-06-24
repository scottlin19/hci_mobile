package ar.edu.itba.hci.ui.devices.actions;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.slider.Slider;

import java.util.HashMap;
import java.util.Map;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.devices.FridgeDevice;
import ar.edu.itba.hci.api.models.devices.states.FridgeDeviceState;
import ar.edu.itba.hci.api.models.devices.states.VacuumDeviceState;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FridgeActions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FridgeActions extends Fragment {

    FridgeDeviceState state;
    private final int MODES = 3;
    private Button[] btns = new Button[MODES];
    private int[] btn_id = {R.id.mode_btn1,R.id.mode_btn2,R.id.mode_btn3};
    private Button btn_unfocus;
    Slider freezer_slider;
    Slider fridge_slider;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "device";

    // TODO: Rename and change types of parameters
    private Device<FridgeDeviceState> device;

    public FridgeActions() {
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
    public static FridgeActions newInstance(Device<FridgeDeviceState> device) {
        FridgeActions fragment = new FridgeActions();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fridge_actions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateDevice();
        freezer_slider = (Slider) getView().findViewById(R.id.freezer_slider);
        freezer_slider.setValue(device.getState().getFreezerTemperature());
        freezer_slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                Object[] params = {value};
                registerAction("setFreezerTemperature",params);

            }
        });

        fridge_slider = (Slider) getView().findViewById(R.id.fridge_slider);
        fridge_slider.setValue(device.getState().getTemperature());
        fridge_slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                Object[] params = {value};
                registerAction("setTemperature",params);
            }
        });

        initBtns();
        getInitialMode(device.getState().getMode());

    }

    private void updateDevice() {
        System.out.println("updating");
        ApiClient.getInstance().getDevice(device.getId(), new Callback<Result<Device>>() {
            @Override
            public void onResponse(Call<Result<Device>> call, Response<Result<Device>> response) {
                if(response.isSuccessful()) {
                    device = response.body().getResult();
                    state = (FridgeDeviceState) device.getState();
                    freezer_slider.setValue(device.getState().getFreezerTemperature());
                    fridge_slider.setValue(device.getState().getTemperature());
                }
                else{
                    Toast.makeText(getContext(),getResources().getString(R.string.fetch_device_error),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Result<Device>> call, Throwable t) {
                Log.e("update device", t.getLocalizedMessage());
                Toast.makeText(getContext(),getResources().getString(R.string.unexpected_error),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerAction(String action, Object[] params) {
        ApiClient.getInstance().executeAction(device.getId(), action, params, new Callback<Result<Object>>() {
            @Override
            public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                if(response.isSuccessful()){
                    System.out.println("successful update on" + action);
                    updateView();
                }
            }

            @Override
            public void onFailure(Call<Result<Object>> call, Throwable t){
                Log.e("UPDATE REFRIGERATOR ERROR","error updating refrigerator" + action);
            }
        });
    }

    private void updateView() {
        updateDevice();

    }

    private void getInitialMode(String deviceMode) {
        String aux = deviceMode.concat("Mode");
        System.out.println(aux);
        String wanted = getStringResourceByName(aux);
        System.out.println("wanted: " + wanted);

        int j = 0;

        while(!btns[j].getText().equals(wanted)){
            System.out.println(btns[j].getText());
            j++;
        }

        btn_unfocus = btns[j];
        btn_unfocus.setTextColor(Color.BLACK);
        btn_unfocus.setBackgroundColor(getResources().getColor(R.color.design_default_color_background));
    }

    private String getStringResourceByName(String aString) {
        int resId = getResources().getIdentifier(aString, "string", getContext().getPackageName());
        return getString(resId);
    }

    private void initBtns() {
        Map<String,String> map = new HashMap<String,String>(){
            {
                put("defecto","default");
                put("vacación","vacation");
                put("fiesta","party");
            }
        };
        for(int i = 0; i < MODES; i++){
            btns[i] = (Button) getView().findViewById(btn_id[i]);
            btns[i].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            btns[i].setOnClickListener(v -> {
                Button btn = (Button) getView().findViewById(v.getId());
                changeFocus(btn_unfocus, btn);
                String aux;
                if (map.get(btn.getText().toString()) != null) {
                    aux = map.get(btn.getText().toString());
                } else {
                    aux = btn.getText().toString();
                }
                System.out.println(aux);
                Object[] params = {aux};
                registerAction("setMode",params);
            });
        }
    }

    private void changeFocus(Button btn_unfocus, Button btn_focus){
        btn_unfocus.setTextColor(getResources().getColor(R.color.textColorPrimary));
        btn_unfocus.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        btn_focus.setTextColor(Color.BLACK);
        btn_focus.setBackgroundColor(getResources().getColor(R.color.design_default_color_background));
        this.btn_unfocus = btn_focus;
    }
}