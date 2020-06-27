package ar.edu.itba.hci.ui.devices.actions;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.devices.states.AlarmDeviceState;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlarmActions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlarmActions extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "device";

    // TODO: Rename and change types of parameters
    private Device<AlarmDeviceState> device;
    private Spinner spinner;
    private TextInputEditText securityCodeInput,newSecurityCodeInput;
    private ImageButton unlockBtn,showChangeBtn;
    private Button changeBtn;
    private List<String> statusList;
    private LinearLayout new_code_layout,status_layout;
    private TextInputLayout newCodeTi;
    private boolean lock;
    private Map<String,String> statusAdapter;
    private Map<String,String> actionAdapter;
    private String secCode;
    private FloatingActionButton speechButton;
    private SpeechRecognizer speechRecognizer;
    private static final int REQUEST_RECORD_AUDIO = 1;
    public AlarmActions() {
        // Required empty public constructor
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());

        this.speechRecognizer.setRecognitionListener(new AlarmActions.AlarmRecognitionListener());

        speechButton = getActivity().findViewById(R.id.stt_button);
        if (speechButton != null) {
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
    }
    private class AlarmRecognitionListener implements RecognitionListener {

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
            speechButton.setColorFilter(ContextCompat.getColor(getContext(), R.color.textColorPrimary));
            Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onResults(Bundle results) {
            ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            speechButton.setColorFilter(ContextCompat.getColor(getContext(), R.color.textColorPrimary));
            String lower = data.get(0).toLowerCase();
            String action;
            if (status_layout.getVisibility() == View.VISIBLE){
                if(lower.equals(getString(R.string.armStaysst))){
                    action = "armStay";
                }else if(lower.equals(getString(R.string.armAwaysst))){
                    action = "armAway";
                }else if(lower.equals(getString(R.string.disarmsst))){
                    action = "disarm";
                }else{
                    return;
                }
                Object[] params = {securityCodeInput.getText().toString()};
                ApiClient.getInstance().executeAction(device.getId(), action, params, new Callback<Result<Object>>() {
                    @Override
                    public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                        if (response.isSuccessful()) {
                            int selected;
                            if(action.equals("armStay")){
                                selected = 0;
                            }else if(action.equals("armAway")){
                                selected = 1;
                            }else{
                                selected = 2;
                            }
                            spinner.setSelection(selected);
                        }
                    }

                    @Override
                    public void onFailure(Call<Result<Object>> call, Throwable t) {
                        Log.e("GENRE CHANGE","ERROR: CALL WAS A FAILURE");
                    }
                });
            }else{
                Toast.makeText(getContext(), getString(R.string.missingcode), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }

    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param device Parameter 1.
     * @return A new instance of fragment VacuumDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlarmActions newInstance(Device<AlarmDeviceState> device) {
        AlarmActions fragment = new AlarmActions();
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
            statusList = new ArrayList<>();
            statusAdapter = new HashMap<>();
            actionAdapter = new HashMap<>();
            statusAdapter.put("disarmed","disarm");
            statusAdapter.put("armedAway","armAway");
            statusAdapter.put("armedStay","armStay");
            actionAdapter.put("disarm","disarmed");
            actionAdapter.put("armStay","armedStay");
            actionAdapter.put("armAway","armedAway");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =inflater.inflate(R.layout.fragment_alarm_actions, container, false);

        securityCodeInput = root.findViewById(R.id.security_code_input);
        securityCodeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() != 4){
                    disableImageButton(unlockBtn);


                }else{
                    enableImageButton(unlockBtn);

                }
                System.out.println(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        newSecurityCodeInput = root.findViewById(R.id.new_security_code_input);
        newSecurityCodeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() != 4){
                    changeBtn.setEnabled(false);
                    changeBtn.setClickable(false);
                    changeBtn.setFocusable(false);
                    changeBtn.setBackgroundResource(R.drawable.circular_btn_disabled_bg);
                    if(newCodeTi.getError() != null){

                        newCodeTi.setErrorEnabled(false);
                    }

                }else{
                    changeBtn.setEnabled(true);
                    changeBtn.setClickable(true);
                    changeBtn.setFocusable(true);
                    changeBtn.setTextColor(getResources().getColor(R.color.textColorPrimary));
                    changeBtn.setBackgroundResource(R.drawable.circular_btn_enabled_bg);
                    String newCode = newSecurityCodeInput.getText().toString();
                    if(secCode.equals(newCode)){
                        newCodeTi.setErrorEnabled(true);
                        changeBtn.setEnabled(false);
                        changeBtn.setClickable(false);
                        changeBtn.setFocusable(false);
                        changeBtn.setBackgroundResource(R.drawable.circular_btn_disabled_bg);
                        changeBtn.setTextColor(getResources().getColor(R.color.disabledText));
                        newCodeTi.setError("The security code is the same as the old one");
                    }

                }
                System.out.println(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        unlockBtn = root.findViewById(R.id.unlock_btn);
        unlockBtn.setOnClickListener(view -> {
            secCode = securityCodeInput.getText().toString();
            Object[] params = {secCode,secCode};

            ApiClient.getInstance().executeAction(device.getId(), "changeSecurityCode", params, new Callback<Result<Object>>() {
                @Override
                public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                    if(response.isSuccessful()){
                        Boolean res = (Boolean) response.body().getResult();
                        if(res == true){

                            lock = false;
                            showUnlockedLayouts();

                        }else{
                            Toast.makeText(getContext(), getString(R.string.wrongcode), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Result<Object>> call, Throwable t) {
                    Log.e("SECURITY CODE INPUT","ERROR: CALL WAS A FAILURE");
                }
            });
        });

        newCodeTi = root.findViewById(R.id.new_code_ti_layout);
        showChangeBtn = root.findViewById(R.id.show_code_btn);
        showChangeBtn.setOnClickListener(view -> {

            if(this.new_code_layout.getVisibility() == View.VISIBLE){
                this.new_code_layout.setVisibility(View.GONE);
            }else{
                changeBtn.setEnabled(false);
                changeBtn.setClickable(false);
                changeBtn.setFocusable(false);
                changeBtn.setBackgroundResource(R.drawable.circular_btn_disabled_bg);
                changeBtn.setTextColor(getResources().getColor(R.color.disabledText));
                this.new_code_layout.setVisibility(View.VISIBLE);


            }
        });
        changeBtn = root.findViewById(R.id.change_btn);
        changeBtn.setOnClickListener(view -> {
            String newCode = newSecurityCodeInput.getText().toString();
            Object[] params = {secCode,newCode};
            ApiClient.getInstance().executeAction(device.getId(), "changeSecurityCode", params, new Callback<Result<Object>>() {
                @Override
                public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                    if(response.isSuccessful()){
                        Boolean res = (Boolean) response.body().getResult();
                        if(res == true){
                            secCode = newCode;
                            Toast.makeText(getContext(), "Your security code was successfuly changed", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getContext(), "Error changing security code, try again", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Result<Object>> call, Throwable t) {
                    Log.e("SECURITY CODE INPUT","ERROR: CALL WAS A FAILURE");
                }
            });
        });

        new_code_layout = root.findViewById(R.id.new_code_layout);

        status_layout = root.findViewById(R.id.alarm_status_layout
        );


        spinner = (Spinner) root.findViewById(R.id.alarm_state_spinner);
        final String[] statusArray = {"armStay",
                "armAway",
                "disarm",

        };
        Context context  =getContext();
        for(int i = 0; i < statusArray.length;i++){

            String status;
            int id = context.getResources().getIdentifier(statusArray[i], "string", context.getPackageName());
            if(id == 0) status = "MISSING_NAME";
            else status = context.getResources().getString(id);
            statusList.add(status);

        }
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,statusList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setSelection(statusList.indexOf( context.getResources().getString(context.getResources().getIdentifier(statusAdapter.get(this.device.getState().getStatus()), "string", context.getPackageName()))));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String lastSelected = context.getResources().getString(context.getResources().getIdentifier(statusAdapter.get(device.getState().getStatus()), "string", context.getPackageName()));
                String selected = statusList.get(i);
                if (!lastSelected.equals(selected)){
                    Object[] params = {securityCodeInput.getText().toString()};
                    System.out.println("SE MANDA ESTO: "+statusArray[i]);
                    ApiClient.getInstance().executeAction(device.getId(), statusArray[i], params, new Callback<Result<Object>>() {
                        @Override
                        public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                            if (response.isSuccessful()) {

                                device.getState().setStatus(actionAdapter.get(statusArray[i]));

                            }
                        }



                        @Override
                        public void onFailure(Call<Result<Object>> call, Throwable t) {
                            Log.e("GENRE CHANGE","ERROR: CALL WAS A FAILURE");
                        }
                    });
                }
            }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
                           });

        return root;

    }


    private void enableImageButton(ImageButton btn){
        btn.setEnabled(true);
        btn.setClickable(true);
        btn.setFocusable(true);

        btn.setBackgroundResource(R.drawable.circular_btn_enabled_bg);
    btn.setImageResource(R.drawable.ic_lock_enabled);

    }

    private void disableImageButton(ImageButton btn){

        btn.setEnabled(false);
        btn.setClickable(false);
        btn.setFocusable(false);
        btn.setBackgroundResource(R.drawable.circular_btn_disabled_bg);

      btn.setImageResource(lock ? R.drawable.ic_lock_disabled : R.drawable.ic_unlocked);
    }

    private void showUnlockedLayouts(){


        this.showChangeBtn.setVisibility(View.VISIBLE);
        this.status_layout.setVisibility(View.VISIBLE);


        this.securityCodeInput.setFocusable(false);

        disableImageButton(this.unlockBtn);
    }

@Override
    public void onPause() {
        super.onPause();
        speechRecognizer.destroy();

    }
}