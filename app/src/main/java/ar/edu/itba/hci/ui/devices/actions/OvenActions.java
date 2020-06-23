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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.devices.states.DoorDeviceState;
import ar.edu.itba.hci.api.models.devices.states.OvenDeviceState;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OvenActions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OvenActions extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "device";

    // TODO: Rename and change types of parameters
    private Device<OvenDeviceState> device;
    private OvenDeviceState state;
    private SwitchMaterial swi;
    private TextView statusText;
    private Button[] heat_btn = new Button[3];
    private int[] heat_btn_id = {R.id.heat_btn1, R.id.heat_btn2, R.id.heat_btn3};
    private Button[] grill_btn = new Button[3];
    private int[] grill_btn_id = {R.id.grill_btn1, R.id.grill_btn2, R.id.grill_btn3};
    private Button[] convec_btn = new Button[3];
    private int[] convec_btn_id = {R.id.convec_btn1, R.id.convec_btn2, R.id.convec_btn3};
    private Button[] btn_unfocus = new Button[3];
    public OvenActions() {
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
    public static OvenActions newInstance(Device device) {
        OvenActions fragment = new OvenActions();
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

    public void viewHandler(){
        if(state.getStatus().equals("on")){
            swi.setChecked(true);
            statusText.setText(R.string.on);
        }else{
            swi.setChecked(false);
            statusText.setText(R.string.off);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_oven_actions, container, false);

        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        Slider slider = (Slider) getView().findViewById(R.id.temp_slider);
        slider.setValue(device.getState().getTemperature());
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                Object[] params = {value};
                ApiClient.getInstance().executeAction(device.getId(), "setTemperature", params, new Callback<Result<Object>>() {
                    @Override
                    public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                        if(response.isSuccessful()){
                            System.out.println("successful update on setTemperature");
                        }
                    }

                    @Override
                    public void onFailure(Call<Result<Object>> call, Throwable t){
                        Log.e("UPDATE AC ERROR","error updating ac on action: " + "setTemperature");
                    }
                });
            }
        });
        initButtons(heat_btn, heat_btn_id, 0, "setHeat");
        initButtons(grill_btn, grill_btn_id, 1, "setGrill");
        initButtons(convec_btn, convec_btn_id, 2, "setConvection");
        setInitialFocused();
    }

    private void initButtons(Button[] btns, int[] btn_ids, int index_focus, String action) {
        Map<String, String> translateMap = new HashMap<String, String>(){
            {
                put("convencional", "conventional");
                put("abajo", "bottom");
                put("arriba", "top");
                put("grande", "large");
                put("apagado", "off");
            }
        };


        for(int i = 0; i < 3; i++){
            btns[i] = (Button) getView().findViewById(btn_ids[i]);
            btns[i].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            btns[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button btn = (Button) getView().findViewById(v.getId());
                    changeFocus(index_focus, btn);
                    String aux;
                    if(translateMap.get(btn.getText().toString()) != null) {
                        aux = translateMap.get(btn.getText().toString());
                    }
                    else{
                        aux = btn.getText().toString();
                    }
                    Object[] params = {aux};
                    params[0] = btn.getText();
                    ApiClient.getInstance().executeAction(device.getId(), action, params, new Callback<Result<Object>>() {
                        @Override
                        public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {

                        }

                        @Override
                        public void onFailure(Call<Result<Object>> call, Throwable t){
                            Log.e("Oven update error","Error updating action " + action);
                        }
                    });

                }
            });
        }
    }

    private void changeFocus(int i, Button btn_focus){
        btn_unfocus[i].setTextColor(getResources().getColor(R.color.textColorPrimary));
        btn_unfocus[i].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        btn_focus.setTextColor(Color.BLACK);
        btn_focus.setBackgroundColor(getResources().getColor(R.color.design_default_color_background));
        this.btn_unfocus[i] = btn_focus;
    }
    private void setInitialFocused(){
        getInitialValue(device.getState().getHeat(), 0, heat_btn);
        getInitialValue(device.getState().getGrill(), 1, grill_btn);
        getInitialValue(device.getState().getConvection(), 2, convec_btn);
    }
    private void getInitialValue(String btntext, int i, Button[] btns){
        btn_unfocus[i] = Arrays.stream(btns).filter(btn -> {
            return btn.getText().equals(btntext);
        }).collect(Collectors.toList()).get(0);
        btn_unfocus[i].setTextColor(Color.BLACK);
        btn_unfocus[i].setBackgroundColor(getResources().getColor(R.color.design_default_color_background));
    }
    @Override
    public void onStart(){
        super.onStart();
        state = (OvenDeviceState) device.getState();
        swi = getActivity().findViewById(R.id.oven_status_switch);
        statusText = getActivity().findViewById(R.id.state_text);
        viewHandler();
        swi.setOnClickListener(v -> {
            String changeState;
            String newStatus;
            if (swi.isChecked()) {
                changeState = "turnOn";
                newStatus = "on";
            } else {
                changeState = "turnOff";
                newStatus = "off";
            }
            ApiClient.getInstance().executeAction(device.getId(), changeState, new Object[0], new Callback<Result<Object>>() {
                @Override
                public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response){
                    state.setStatus(newStatus);
                    viewHandler();
                }

                @Override
                public void onFailure(Call<Result<Object>> call, Throwable t){
                    Log.e("Switch state err", t.getLocalizedMessage());
                }
            });
        });
    }

}