package ar.edu.itba.hci.ui.devices.actions;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.devices.states.FaucetDeviceState;
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
    final String[] units = {"ml", "cl", "dl", "l", "dal", "hl", "kl"};
    private Device<FaucetDeviceState> device;
    private FloatingActionButton speechButton;
    private FaucetDeviceState state;
    private SwitchMaterial swi;
    private TextView statusText;
    private String selectedUnit;
    private static final int REQUEST_RECORD_AUDIO = 1;
    private int amount;
    private Button dispenseButton;
    private Handler stateHandler;
    private LinearLayout dispensingLayout;
    private ProgressBar progressDispense;
    private TextView dispensedQText;
    private TextView unitText;
    private SpeechRecognizer speechRecognizer;
    private TextView qtyText;

    public FaucetActions() {
        // Required empty public constructor
    }


    private void startRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 3000);
        speechButton.setColorFilter(Color.RED);
        speechRecognizer.startListening(intent);

        System.out.println("funca");
    }

    private class FaucetRecognitionListener implements RecognitionListener {

        @Override
        public void onReadyForSpeech(Bundle params) {

        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int error) {
            Log.d("ERROR", "error " + error);

            String msg;
            switch (error) {
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    msg = "No speech input";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    msg = "No recognition result matched";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    msg = "Insufficient permissions";
                    break;
                default:
                    msg = "Wait for speech recognition to end ";
                    break;
            }
            System.out.println(msg);
            speechButton.setColorFilter(ContextCompat.getColor(getContext(), R.color.textColorPrimary));
            Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onResults(Bundle results) {
            speechButton.setColorFilter(ContextCompat.getColor(getContext(), R.color.textColorPrimary));
            ArrayList<String> result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String newStatus = null;
            if (result != null && result.size() > 0) {
                String action = result.get(0).toLowerCase();
                System.out.println(action);
                if (action.equals(getString(R.string.open))) {
                    action = "open";
                    newStatus = "opened";
                } else if (action.equals(getString(R.string.close))) {
                    action = "close";
                    newStatus = "closed";
                }
                if(newStatus != null){
                    String finalNewStatus = newStatus;
                    ApiClient.getInstance().executeAction(device.getId(), action, new Object[0], new Callback<Result<Object>>() {
                        @Override
                        public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response){
                            state.setStatus(finalNewStatus);
                            viewHandler();
                        }

                        @Override
                        public void onFailure(Call<Result<Object>> call, Throwable t){
                            Log.e("Switch state err", t.getLocalizedMessage());
                        }
                    });
                    return;
                }
                Map<String, String> unitMap = new HashMap<String, String>() {
                    {
                        put(getString(R.string.millis), "ml");
                        put(getString(R.string.centis), "cl");
                        put(getString(R.string.decis), "dl");
                        put(getString(R.string.liters), "l");
                        put(getString(R.string.decas), "dal");
                        put(getString(R.string.hectos), "hl");
                        put(getString(R.string.kilos), "kl");
                    }
                };
                String[] unitNames = {getString(R.string.millis), getString(R.string.centis), getString(R.string.decis), getString(R.string.liters), getString(R.string.decas), getString(R.string.hectos), getString(R.string.kilos)};
                for (int i = 0; i <= 100; i++) {
                    for (int j = 0; j < 7; j++) {
                        if (action.equals(getResources().getString(R.string.dispenseamountsst, i, unitNames[j]))) {
                            if(dispenseButton.getVisibility() == View.VISIBLE) {
                                Object[] params = {i, unitMap.get(unitNames[j])};
                                ApiClient.getInstance().executeAction(device.getId(), "dispense", params, new Callback<Result<Object>>() {
                                    @Override
                                    public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                                        if (response.isSuccessful()) {
                                            return;
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Result<Object>> call, Throwable t) {
                                        Log.e("DISPENSE ERROR", "Errror trying to dispense");
                                    }
                                });
                            }else {
                                Toast.makeText(getContext(), "Wrong security code, please try again", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    }




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
        dispensedQText = getActivity().findViewById(R.id.dispensed_quantity);
        qtyText = getActivity().findViewById(R.id.quantity);
        unitText = getActivity().findViewById(R.id.units_text);
        state = (FaucetDeviceState) device.getState();
        swi = getActivity().findViewById(R.id.oven_status_switch);
        swi.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getActionMasked() == MotionEvent.ACTION_MOVE;
            }
        });
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
        speechRecognizer.destroy();
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
        this.speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());

        this.speechRecognizer.setRecognitionListener(new FaucetActions.FaucetRecognitionListener());

        speechButton = getActivity().findViewById(R.id.stt_button);
        if(speechButton != null) {
            speechButton.setOnClickListener(v -> {
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.RECORD_AUDIO},
                            REQUEST_RECORD_AUDIO);
                } else {
                    startRecognizer();
                }
            });
        }
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
                dispensedQText.setText(String.format("%.2f", state.getDispensedQuantity()));
                qtyText.setText(String.format("%.2f", state.getQuantity()));
                unitText.setText(state.getUnit());
                dispensedQText.setVisibility(View.VISIBLE);
                qtyText.setVisibility(View.VISIBLE);
                unitText.setVisibility(View.VISIBLE);
                dispensingLayout.setVisibility(View.VISIBLE);
                progressDispense.setProgress((int) ((state.getDispensedQuantity() / (state.getQuantity())) * 100));
            }

        }else{
            swi.setChecked(false);
            statusText.setText(R.string.closed);
            dispensedQText.setVisibility(View.GONE);
            qtyText.setVisibility(View.GONE);
            unitText.setVisibility(View.GONE);
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
        int delay = 1000;
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
