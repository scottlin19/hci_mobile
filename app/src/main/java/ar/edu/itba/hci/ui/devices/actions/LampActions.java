package ar.edu.itba.hci.ui.devices.actions;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorListener;

import java.util.ArrayList;
import java.util.Locale;

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

    private static final String TAG = "SpeechRecognizer";
    private static final int REQUEST_RECORD_AUDIO = 1;
    private SpeechRecognizer speechRecognizer;
    private FloatingActionButton sttButton;

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
        this.speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        this.speechRecognizer.setRecognitionListener(new LampRecognitionlistener());
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
        if(color.charAt(0) != '#') color.reverse().append('#').reverse();
        if(color.length() > 7) color.setLength(7);


        colorPicker.selectByHsv(Color.parseColor(color.toString()));
        slider.setValue(state.getBrightness());
    }

    @Override
    public void onPause() {
        super.onPause();
        speechRecognizer.destroy();
    }

    private class LampRecognitionlistener implements RecognitionListener{
        private String[][] sstColors = {
                {getResources().getString(R.string.sstColor,getResources().getString(R.string.red)),"#FF0000"},
            {getResources().getString(R.string.sstColor,getResources().getString(R.string.blue)),"#0000FF"},
            {getResources().getString(R.string.sstColor,getResources().getString(R.string.green)),"#00FF00"},
            {getResources().getString(R.string.sstColor,getResources().getString(R.string.yellow)),"#FFFF00"},
        {getResources().getString(R.string.sstColor,getResources().getString(R.string.gamer)),"#7F00FF"},
        {getResources().getString(R.string.sstColor,getResources().getString(R.string.white)),"#FFFFFF"}
        };

        private String[] sstStatus = {
                getResources().getString(R.string.sstTurnOn).toLowerCase(),
                getResources().getString(R.string.sstTurnOff).toLowerCase(),
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
            String lower = data.get(0).toLowerCase();
            System.out.println(lower);
            if(!compareSSTStatus(lower)
                    && !compareSSTColors(lower)
                    && !compareSSTSlider(lower)){
                Toast.makeText(getContext(),getResources().getString(R.string.sstNotRecognized), Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getContext(), getResources().getString(R.string.sstSuccess), Toast.LENGTH_SHORT).show();
            }
        }

        private boolean compareSSTSlider(String toFind) {
            for(int i = 0; i <= 100; i++) {
                if (toFind.equals(getResources().getString(R.string.sstBrightness, i).toLowerCase())) {
                    slider.setValue(i);
                    Integer[] body = {i};
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
                    return true;
                }
            }
            return false;
        }

        private boolean compareSSTColors(String lower) {
            for(int i = 0; i < sstColors.length; i++){
                if(lower.equals(sstColors[i][0])){
                    System.out.println(sstColors[i][1] + " " + Color.parseColor(sstColors[i][1]));
                    colorPicker.selectByHsv(Color.parseColor(sstColors[i][1]));
                    String[] aux = {sstColors[i][1]};
                    ApiClient.getInstance().executeAction(device.getId(), "setColor", aux, new Callback<Result<Object>>() {
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
                    return true;
                }
            }
            return false;
        }

        private boolean compareSSTStatus(String toFind) {
            for(int i = 0 ; i < 2; i ++){
                if(toFind.equals(sstStatus[i])){
                    if((!statusSwitch.isChecked() && i == 0) || (statusSwitch.isChecked() && i == 1)) {
                        statusSwitch.performClick();
                    }
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