package ar.edu.itba.hci.ui.devices.actions;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.devices.states.FaucetDeviceState;
import ar.edu.itba.hci.api.models.devices.states.OvenDeviceState;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FaucetActions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FaucetActions extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "device";

    // TODO: Rename and change types of parameters
    private Device<FaucetDeviceState> device;
    private FaucetDeviceState state;
    private SwitchMaterial swi;
    private TextView statusText;
    private String selectedUnit;
    private int amount;
    private Button dispenseButton;
    private Handler stateHandler;
    private LinearLayout dispensingLayout;
    private ProgressBar progressDispense;
    public FaucetActions() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment SpeakerActions.
     */
    // TODO: Rename and change types and number of parameters
    public static FaucetActions newInstance(Device param1) {
        FaucetActions fragment = new FaucetActions();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, param1);
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

    public void onStart(){
        super.onStart();
        state = (FaucetDeviceState) device.getState();
        swi = getActivity().findViewById(R.id.oven_status_switch);
        dispenseButton.setVisibility(View.GONE);
        dispensingLayout = (LinearLayout) getActivity().findViewById(R.id.dispensing_layout);
        progressDispense = (ProgressBar) getActivity().findViewById(R.id.progress_dispense);
        statusText = getActivity().findViewById(R.id.state_text);
        swi.setOnClickListener(v -> {
            String changeState;
            String newStatus;
            if (swi.isChecked()) {
                changeState = "open";
                newStatus = "opened";
            } else {
                changeState = "close";
                newStatus = "closed";
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
    @Override
    public void onPause() {
        super.onPause();
        this.stateHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        Slider slider = (Slider) getView().findViewById(R.id.dispense_slider);
        slider.setValue(amount);
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                amount = (int) value;
            }
        });
    }
    public void viewHandler(){
        if(state.getUnit() != null){
            Log.d("Units: ", state.getUnit());
        }
        if(state.getStatus().equals("opened")){
            swi.setChecked(true);
            statusText.setText(R.string.opened);
            dispenseButton.setVisibility(View.GONE);
            if(state.getUnit() != null) {
                dispensingLayout.setVisibility(View.VISIBLE);
                progressDispense.setProgress((int) ((state.getDispensedQuantity() / (state.getQuantity())) * 100));
            }

        }else{
            swi.setChecked(false);
            statusText.setText(R.string.closed);
            dispenseButton.setVisibility(View.VISIBLE);
            dispensingLayout.setVisibility(View.GONE);

        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_faucet_actions, container, false);

        stateHandler = new Handler();
        int delay = 50;
        stateHandler.post(new Runnable(){
            @Override
            public void run(){
                ApiClient.getInstance().getDevice(device.getId(), new Callback<Result<Device>>(){
                    @Override
                    public void onResponse(Call<Result<Device>> call, Response<Result<Device>> response) {
                        if(response.isSuccessful()){
                            state = (FaucetDeviceState) response.body().getResult().getState();
                            viewHandler();
                        }
                    }

                    @Override
                    public void onFailure(Call<Result<Device>> call, Throwable t) {

                    }
                });
                stateHandler.postDelayed(this, delay);
            }
        });


        Spinner spinner = (Spinner) root.findViewById(R.id.dispense_spinner);
        final String[] units = {"ml","cl","dl","l","dal","hl","kl"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, units);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedUnit = units[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dispenseButton = root.findViewById(R.id.dispense_btn);
        dispenseButton.setOnClickListener(view -> {
            Object[] params  = {amount, selectedUnit};
            ApiClient.getInstance().executeAction(this.device.getId(), "dispense", params, new Callback<Result<Object>>(){
                @Override
                public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response){
                    if(response.isSuccessful()){

                    }
                }
                @Override
                public void onFailure(Call<Result<Object>> call, Throwable t){
                    Log.e("DISPENSE ERROR", "Errror trying to dispense");
                }
            });
        });
        return root;
    }
}
