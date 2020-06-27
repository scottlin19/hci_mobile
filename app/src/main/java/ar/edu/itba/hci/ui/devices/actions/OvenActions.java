package ar.edu.itba.hci.ui.devices.actions;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.devices.OvenDevice;
import ar.edu.itba.hci.api.models.devices.states.DoorDeviceState;
import ar.edu.itba.hci.api.models.devices.states.OvenDeviceState;
import ar.edu.itba.hci.api.models.devices.states.VacuumDeviceState;
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
    private static final int REQUEST_RECORD_AUDIO = 1;
    private Slider slider;
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
    Map<String, String> translateMap;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private TextView recognizedText;
    private FloatingActionButton speechButton;
    private SpeechRecognizer speechRecognizer;
    public OvenActions() {
        // Required empty public constructor
    }

    private class OvenRecognitionListener implements RecognitionListener {

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
                    msg = "Wait for speech recognition to end " + error;
                    break;
            }
            System.out.println(msg);
            speechButton.setColorFilter(ContextCompat.getColor(getContext(),R.color.textColorPrimary));
            Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onResults(Bundle results) {
            speechButton.setColorFilter(ContextCompat.getColor(getContext(),R.color.textColorPrimary));
            ArrayList<String> result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String newStatus = null;

            if(result != null && result.size() > 0) {
                String action = result.get(0).toLowerCase();
                if(action.equals(getString(R.string.turnonsst))){
                    action = "turnOn";
                    newStatus = "on";
                }else if(action.equals(getString(R.string.turnoffsst))) {
                    action = "turnOn";
                    newStatus = "off";
                }else if(action.equals(getString(R.string.heatconventionalsst))){
                    heat_btn[0].performClick();
                    return;
                }else if(action.equals(getString(R.string.heatbottomsst))){
                    heat_btn[1].performClick();
                    return;
                }else if(action.equals(getString(R.string.heattopsst))){
                    heat_btn[2].performClick();
                    return;
                }else if(action.equals(getString(R.string.grilllargesst))) {
                    grill_btn[0].performClick();
                    return;
                }else if(action.equals(getString(R.string.grillecosst))) {
                    grill_btn[1].performClick();
                    return;
                }else if(action.equals(getString(R.string.grilloffsst))) {
                    grill_btn[2].performClick();
                    return;
                }else if(action.equals(getString(R.string.convectionnormalsst))) {
                    convec_btn[0].performClick();
                    return;
                }
                else if(action.equals(getString(R.string.convectionecosst))) {
                    convec_btn[1].performClick();
                    return;
                }else if(action.equals(getString(R.string.convectionoffsst))){
                    convec_btn[2].performClick();
                    return;
                }

                if (action == "turnOn" || action == "turnOff") {
                    String finalNewStatus = newStatus;
                    ApiClient.getInstance().executeAction(device.getId(), action, new Object[0], new Callback<Result<Object>>() {
                        @Override
                        public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                            state.setStatus(finalNewStatus);
                            viewHandler();
                        }

                        @Override
                        public void onFailure(Call<Result<Object>> call, Throwable t) {
                        }
                    });
                } else{
                    for (int i = 90; i <= 230; i++) {
                            if (action.equals(getResources().getString(R.string.sstTemperature, i).toLowerCase())) {
                                slider.setValue(i);
                                slider.callOnClick();
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

    private void startRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,3000);
        speechButton.setColorFilter(Color.RED);
        speechRecognizer.startListening(intent);

        System.out.println("funca");
    }

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
        speechButton = getActivity().findViewById(R.id.stt_button);
        if (speechButton != null) {
            speechButton.setOnClickListener((view) -> {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "talk");

                if (intent.resolveActivity(getActivity().getPackageManager()) != null)
                {
                    startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
                }
            });
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
        this.speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());

        this.speechRecognizer.setRecognitionListener(new OvenActions.OvenRecognitionListener());

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
        ApiClient.getInstance().getDevice(device.getId(), new Callback<Result<Device>>() {
            @Override
            public void onResponse(Call<Result<Device>> call, Response<Result<Device>> response) {
                device = (OvenDevice) response.body().getResult();
                state = (OvenDeviceState) device.getState();
                viewHandler();
            }

            @Override
            public void onFailure(Call<Result<Device>> call, Throwable t) {
                Log.e("update device error", t.getLocalizedMessage());
            }
        });
        slider = (Slider) getView().findViewById(R.id.temp_slider);
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
         translateMap = new HashMap<String, String>(){
            {
                put("convencional", "conventional");
                put("abajo", "bottom");
                put("arriba", "top");
                put("grande", "large");
                put("apagado", "off");
                put("eco", "eco");
            }
        };


        for(int i = 0; i < 3; i++){
            btns[i] = (Button) getView().findViewById(btn_ids[i]);
            btns[i].setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
            btns[i].setOnClickListener((View.OnClickListener) v -> {
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
                ApiClient.getInstance().executeAction(device.getId(), action, params, new Callback<Result<Object>>() {
                    @Override
                    public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {

                    }

                    @Override
                    public void onFailure(Call<Result<Object>> call, Throwable t){
                        Log.e("Oven update error","Error updating action " + action);
                    }
                });

            });
        }
        System.out.println("BOTONES DE OVEN" + btns);
    }

    private void changeFocus(int i, Button btn_focus){
        btn_unfocus[i].setTextColor(ContextCompat.getColor(getContext(),R.color.textColorPrimary));
        btn_unfocus[i].setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
        btn_focus.setTextColor(Color.BLACK);
        btn_focus.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.selectedButton));
        this.btn_unfocus[i] = btn_focus;
    }
    private void setInitialFocused(){
        System.out.println(device.getState().getHeat());
        System.out.println(device.getState().getGrill());
        System.out.println(device.getState().getConvection());
        getInitialValue(device.getState().getHeat(), 0, heat_btn);
        getInitialValue(device.getState().getGrill().concat("Mode"), 1, grill_btn);
        getInitialValue(device.getState().getConvection().concat("Mode"), 2, convec_btn);
    }

    private void getInitialValue(String btntext, int i, Button[] btns){
        /*
        btn_unfocus[i] = Arrays.stream(btns).filter(btn -> {
            return btn.getText().equals(btntext);
        }).collect(Collectors.toList()).get(0);
        btn_unfocus[i].setTextColor(Color.BLACK);
        btn_unfocus[i].setBackgroundColor(ContextCompat.getColor(getContext(),R.color.selectedButton));*/
        System.out.println(btntext);
        String wanted = getStringResourceByName(btntext);

        int j = 0;

        while(!btns[j].getText().equals(wanted)){
            j++;
        }

        btn_unfocus[i] = btns[j];
        btn_unfocus[i].setTextColor(Color.BLACK);
        btn_unfocus[i].setBackgroundColor(ContextCompat.getColor(getContext(),R.color.selectedButton));
    }

    private String getStringResourceByName(String aString) {
        int resId = getResources().getIdentifier(aString, "string", getContext().getPackageName());
        return getString(resId);
    }

    @Override
    public void onStart(){
        super.onStart();
        swi = getActivity().findViewById(R.id.oven_status_switch);
        swi.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getActionMasked() == MotionEvent.ACTION_MOVE;
            }
        });
        state = (OvenDeviceState) device.getState();
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