package ar.edu.itba.hci.ui.devices.actions;

import android.Manifest;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.Slider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
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

    private static final String TAG = "SpeechRecognizer";
    private static final int REQUEST_RECORD_AUDIO = 1;
    private SpeechRecognizer speechRecognizer;
    private FloatingActionButton sttButton;

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
//        updateDevice();

        this.speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        this.speechRecognizer.setRecognitionListener(new FridgeActions.FridgeRecognitionListener());
        sttButton = getActivity().findViewById(R.id.stt_button);
        if(sttButton != null) {
            sttButton.setOnClickListener(v -> {
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

        private void startRecognizer() {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                    Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
            intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,3000);
            sttButton.setColorFilter(Color.RED);
            speechRecognizer.startListening(intent);
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
        btn_unfocus.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.selectedButton));
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
        btn_unfocus.setTextColor(ContextCompat.getColor(getContext(),R.color.textColorPrimary));
        btn_unfocus.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
        btn_focus.setTextColor(Color.BLACK);
        btn_focus.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.selectedButton));
        this.btn_unfocus = btn_focus;
    }

    @Override
    public void onPause() {
        super.onPause();
        speechRecognizer.destroy();
    }

    private class FridgeRecognitionListener implements RecognitionListener{

        private String[] sstModes = {
                getResources().getString(R.string.sstMode,getResources().getString(R.string.defaultMode)).toLowerCase(),
                getResources().getString(R.string.sstMode,getResources().getString(R.string.vacation)).toLowerCase(),
                getResources().getString(R.string.sstMode,getResources().getString(R.string.party)).toLowerCase()
        };

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
            Log.d(TAG, "error " + error);

            String msg;
            switch (error) {
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    msg = getResources().getString(R.string.sstNoInput);
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    msg = getResources().getString(R.string.sstNoPermission);
                    break;
                default:
                    msg = getResources().getString(R.string.sstStopped);
                    break;
            }
            System.out.println(msg);
            sttButton.setColorFilter(ContextCompat.getColor(getContext(),R.color.textColorPrimary));
            Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onResults(Bundle results) {
            Log.d(TAG, "onResults " + results);
            ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            sttButton.setColorFilter(ContextCompat.getColor(getContext(),R.color.textColorPrimary));
            String lower = data.get(0).toLowerCase();
            System.out.println(lower);
            if(!compareSSTButton(lower,sstModes,MODES)
                    && !compareSSTFreezerTemp(lower)
                    && !compareSSTTemp(lower)){
                Toast.makeText(getContext(),getResources().getString(R.string.sstNotRecognized), Toast.LENGTH_SHORT).show();
            }

            else {
                Toast.makeText(getContext(), getResources().getString(R.string.sstSuccess), Toast.LENGTH_SHORT).show();
            }
        }

        private boolean compareSSTFreezerTemp(String toFind) {
            for(int i = -20 ; i <= -8 ; i++){
                if(toFind.equals(getResources().getString(R.string.sstFreezerTemperature,i).toLowerCase()) || toFind.equals(getResources().getString(R.string.sstFreezerTemperatureMinus,-i).toLowerCase())){

                    freezer_slider.setValue(i);
                    freezer_slider.callOnClick();
                    return true;
                }
            }


            return false;
        }

        private boolean compareSSTTemp(String toFind) {
            for(int i = 2 ; i <= 8 ; i++){
                if(toFind.equals(getResources().getString(R.string.sstTemperature,i).toLowerCase())){
                    fridge_slider.setValue(i);
                    fridge_slider.callOnClick();
                    return true;
                }
            }
            return false;
        }

        private boolean compareSSTButton(String toFind, String[] sstStrings, int amount) {
            for (int i = 0; i < amount ; i++){
                if(toFind.equals(sstStrings[i])){
                    btns[i].callOnClick();
                    return true;
                }
            }
            return false;
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    }
}