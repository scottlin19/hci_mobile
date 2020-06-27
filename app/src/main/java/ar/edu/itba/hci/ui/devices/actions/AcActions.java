package ar.edu.itba.hci.ui.devices.actions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainerView;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.devices.states.AcDeviceState;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AcActions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AcActions extends Fragment {

    private static final String TAG = "SpeechRecognizer";
    private static final int REQUEST_RECORD_AUDIO = 1;
    private static final int VSAMOUNT = 5;
    private static final int HSAMOUNT = 6;
    private static final int FSAMOUNT = 5;
    private static final int MODEAMOUNT = 3;
    private static final int BTNTYPES = 4;
    private SpeechRecognizer speechRecognizer;
    private FloatingActionButton sttButton;

    private Button[] vs_btn = new Button[VSAMOUNT];
    private int[] vs_btn_id = {R.id.vs_btn1, R.id.vs_btn2, R.id.vs_btn3, R.id.vs_btn4,R.id.vs_btn5};

    private Button[] hs_btn = new Button[HSAMOUNT];
    private int[] hs_btn_id = {R.id.hs_btn1, R.id.hs_btn2, R.id.hs_btn3, R.id.hs_btn4,R.id.hs_btn5,R.id.hs_btn6};

    private Button[] fs_btn = new Button[FSAMOUNT];
    private int[] fs_btn_id = {R.id.fs_btn1, R.id.fs_btn2, R.id.fs_btn3, R.id.fs_btn4,R.id.fs_btn5};

    private Button[] mode_btn = new Button[MODEAMOUNT];
    private int[] mode_btn_id = {R.id.mode_btn1, R.id.mode_btn2, R.id.mode_btn3};

    private Button[] btn_unfocus = new Button[BTNTYPES];

    SwitchMaterial switchMaterial;

    Slider slider;

    private AcDeviceState state;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "device";

    // TODO: Rename and change types of parameters
    private Device<AcDeviceState> device;

    public AcActions() {
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
    public static AcActions newInstance(Device<AcDeviceState> device) {
        AcActions fragment = new AcActions();
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
       return inflater.inflate(R.layout.fragment_ac_actions, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        updateDevice();

        this.speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        this.speechRecognizer.setRecognitionListener(new AcRecognitionListener());

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

        switchMaterial = getActivity().findViewById(R.id.switchMaterial);
        if(device.getState().getStatus().equals("on")){
            switchMaterial.setChecked(true);
        }
        switchMaterial.setOnClickListener(v -> {
            Object[] params = {};

            if(switchMaterial.isChecked()){
                System.out.printf("on");

                registerAction("turnOn",params);
                switchMaterial.setChecked(true);
//                    device.getState().setStatus("on");
            }
            else {
                System.out.printf("off");
                registerAction("turnOff",params);
                switchMaterial.setChecked(false);
//                    device.getState().setStatus("off");
            }
        });

        TextView textView = (TextView) getView().findViewById(R.id.vertical_swing);
        textView.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_import_export_24,0);

        TextView textView1 = (TextView) getView().findViewById(R.id.horiz_swing);
        textView1.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_swap_horiz_24,0);

        TextView textView2 = (TextView) getView().findViewById(R.id.fan_speed);
        textView2.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_toys_24,0);

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

        initBtns(VSAMOUNT,vs_btn,vs_btn_id,0,"setVerticalSwing");
        initBtns(HSAMOUNT,hs_btn,hs_btn_id,1,"setHorizontalSwing");
        initBtns(FSAMOUNT,fs_btn,fs_btn_id,2,"setFanSpeed");
        initModes(MODEAMOUNT,mode_btn,mode_btn_id,3,"setMode");

        setInitialFocused();
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

    private void initModes(int amount, Button[] btns, int[] btn_ids, int index_focus, String action) {
        Map<String,String> map = new HashMap<String,String>(){
            {
                put("frío","cool");
                put("calor","heat");
                put("ventilador","fan");
            }
        };
        for(int i = 0; i < amount; i++){
            btns[i] = (Button) getView().findViewById(btn_ids[i]);
            btns[i].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            btns[i].setOnClickListener((View.OnClickListener) v -> {
                Button btn = (Button) getView().findViewById(v.getId());
                changeFocus(index_focus, btn);
                String aux;
                if (map.get(btn.getText().toString()) != null) {
                    aux = map.get(btn.getText().toString());
                } else {
                    aux = btn.getText().toString();
                }
                System.out.println(aux);
                Object[] params = {aux};
                registerAction(action, params);
                device.getState().setMode(aux);

            });
        }
    }

    private void initBtns(int amount, Button[] btns, int[] btn_ids,int index_focus,String action) {
        for(int i = 0; i < amount; i++){
            btns[i] = (Button) getView().findViewById(btn_ids[i]);
            btns[i].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            btns[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button btn = (Button) getView().findViewById(v.getId());
                    changeFocus(index_focus, btn);
                    Object[] params = new Object[1];
                    if(!btn.getText().equals("auto")){
                        params[0] = btn.getText().subSequence(0,btn.getText().length() - 1);
                    }
                    else{
                        params[0] = btn.getText();
                    }
                    registerAction(action,params);
                }
            });
        }
    }

    private void setInitialFocused() {
        getInitialValue(device.getState().getVerticalSwing(),0,vs_btn);
        getInitialValue(device.getState().getHorizontalSwing(),1,hs_btn);
        getInitialValue(device.getState().getFanSpeed(),2,fs_btn);
        getInitialMode(device.getState().getMode(),3,mode_btn);
    }

    private void getInitialMode(String btnText, int i, Button[] btns) {
        String wanted = getStringResourceByName(btnText);
        System.out.println("wanted: " + wanted);

        int j = 0;

        while(!btns[j].getText().equals(wanted)){
            System.out.println(btns[j].getText());
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

    private void getInitialValue(String btnText,int i, Button[] btns) {
        btn_unfocus[i] = Arrays.stream(btns).filter(btn ->{
            if(btn.getText().equals("auto")){
                return btn.getText().equals(btnText);
            }
            else{
                return btn.getText().subSequence(0,btn.getText().length() - 1).equals(btnText);
            }
        }).collect(Collectors.toList()).get(0);
        btn_unfocus[i].setTextColor(Color.BLACK);
        btn_unfocus[i].setBackgroundColor(ContextCompat.getColor(getContext(),R.color.selectedButton));
    }

    private void changeFocus(int i, Button btn_focus){
        btn_unfocus[i].setTextColor(ContextCompat.getColor(getContext(),R.color.textColorPrimary));
        btn_unfocus[i].setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
        btn_focus.setTextColor(Color.BLACK);
        btn_focus.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.selectedButton));
        this.btn_unfocus[i] = btn_focus;
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
                Log.e("UPDATE AC ERROR", "error updating A/C on action: " + action);
                Toast.makeText(getContext(), "error updating A/C", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateDevice() {
        ApiClient.getInstance().getDevice(device.getId(), new Callback<Result<Device>>() {
            @Override
            public void onResponse(Call<Result<Device>> call, Response<Result<Device>> response) {
                device = response.body().getResult();
                state = (AcDeviceState) device.getState();
            }

            @Override
            public void onFailure(Call<Result<Device>> call, Throwable t) {
                Log.e("update device", t.getLocalizedMessage());
//                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        speechRecognizer.destroy();
    }

    private class AcRecognitionListener implements RecognitionListener {
        private String[] sstStatus = {
                getResources().getString(R.string.sstTurnOn).toLowerCase(),
                getResources().getString(R.string.sstTurnOff).toLowerCase(),
        };

        private String[] sstModes = {
                getResources().getString(R.string.sstMode,getResources().getString(R.string.cool)).toLowerCase(),
                getResources().getString(R.string.sstMode,getResources().getString(R.string.heat)).toLowerCase(),
                getResources().getString(R.string.sstMode,getResources().getString(R.string.fan)).toLowerCase()
        };
        private String[] sstVSwings = {
                getResources().getString(R.string.sstVertSwingAuto).toLowerCase(),
                getResources().getString(R.string.sstVertSwing,22).toLowerCase(),
                getResources().getString(R.string.sstVertSwing,45).toLowerCase(),
                getResources().getString(R.string.sstVertSwing,67).toLowerCase(),
                getResources().getString(R.string.sstVertSwing,90).toLowerCase(),
        };
        private String[] sstHSwings = {
                getResources().getString(R.string.sstHorizSwingAuto).toLowerCase(),
                getResources().getString(R.string.sstHorizSwingSymbol,-90).toLowerCase(),
                getResources().getString(R.string.sstHorizSwingSymbol,-45).toLowerCase(),
                getResources().getString(R.string.sstHorizSwing,-90).toLowerCase(),
                getResources().getString(R.string.sstHorizSwing,-45).toLowerCase(),
                getResources().getString(R.string.sstHorizSwing,0).toLowerCase(),
                getResources().getString(R.string.sstHorizSwing,45).toLowerCase(),
                getResources().getString(R.string.sstHorizSwing,90).toLowerCase(),
        };
        private String[] sstFSpeeds = {
                getResources().getString(R.string.sstFanSpeedAuto).toLowerCase(),
                getResources().getString(R.string.sstFanSpeed,25).toLowerCase(),
                getResources().getString(R.string.sstFanSpeed,50).toLowerCase(),
                getResources().getString(R.string.sstFanSpeed,75).toLowerCase(),
                getResources().getString(R.string.sstFanSpeed,100).toLowerCase(),
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
                    msg = "No speech input";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    msg = "No recognition result matched";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    msg = "Insufficient permissions";
                    break;
                default:
                    msg = "Unexpected error " + error;
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
            System.out.println("data: "+ data.get(0));
//            Arrays.asList(sstFSpeeds).forEach(v -> System.out.println(v));
            if(!compareSSTStatus(data.get(0).toLowerCase(),sstStatus)
                    && !compareSSTTemp(data.get(0).toLowerCase())
                    && !compareSSTButton(data.get(0).toLowerCase(),sstModes,mode_btn,MODEAMOUNT)
                    && !compareSSTButton(data.get(0).toLowerCase(),sstVSwings,vs_btn,VSAMOUNT)
                    && !compareSSTButton(data.get(0).toLowerCase(),sstHSwings,hs_btn,sstHSwings.length)
                    && !compareSSTButton(data.get(0).toLowerCase(),sstFSpeeds,fs_btn,FSAMOUNT)){
                Toast.makeText(getContext(),getResources().getString(R.string.sstNotRecognized), Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(getContext(),getResources().getString(R.string.sstSuccess),Toast.LENGTH_SHORT).show();
        }

        private boolean compareSSTTemp(String toFind) {
            for(int i = 18; i <= 38; i++){
                if(toFind.equals(getResources().getString(R.string.sstTemperature,i).toLowerCase())){
                    System.out.println(getResources().getString(R.string.sstTemperature,i).toLowerCase());
                    slider.setValue(i);
                    slider.callOnClick();
                    return true;
                }
            }
            return false;
        }

        private boolean compareSSTStatus(String toFind, String[] sstStatus) {
            for(int i = 0 ; i < 2; i ++){
                if(toFind.equals(sstStatus[i])){
                    if((!switchMaterial.isChecked() && i == 0) || (switchMaterial.isChecked() && i == 1)) {
                        switchMaterial.performClick();
                    }
                    return true;
                }
            }
            return false;
        }

        private boolean compareSSTButton(String toFind,String[] sstStrings, Button[] btns, int amount) {
            for(int i = 0; i < amount; i++){
                if(amount == sstHSwings.length) {
                    System.out.println(sstStrings[i]);
                }
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