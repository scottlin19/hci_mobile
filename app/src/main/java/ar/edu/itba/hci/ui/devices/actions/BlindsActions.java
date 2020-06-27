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
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.devices.states.BlindsDeviceState;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlindsActions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlindsActions extends Fragment {

    private static final String ARG_PARAM = "device";
    private static final int REQUEST_RECORD_AUDIO = 1;
    private SpeechRecognizer speechRecognizer;
    private FloatingActionButton speechButton;
    private Device device;
    private BlindsDeviceState state;
    private SwitchMaterial switchMaterial;
    private Slider slider;
    private TextView currLevel;
    private TextView currStatus;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> fetcherHandler;

    public BlindsActions() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param device Parameter 1.
     * @return A new instance of fragment VacuumDetailFragment.
     */
    public static BlindsActions newInstance(Device device) {
        BlindsActions fragment = new BlindsActions();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, device);
        fragment.setArguments(args);
        return fragment;
    }

    private class BlindsRecognitionListener implements RecognitionListener {

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

            if (lower.equals(getString(R.string.open)) || lower.equals(getString(R.string.close))) {
                ApiClient.getInstance().executeAction(device.getId(), lower, new Object[0], new Callback<Result<Object>>() {
                    @Override
                    public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                        switchMaterial.performClick();
                    }

                    @Override
                    public void onFailure(Call<Result<Object>> call, Throwable t) {
                    }
                });
            } else {
                for (int i = 0; i <= 100; i++) {
                    if (lower.equals(getResources().getString(R.string.setlevelsst, i).toLowerCase())) {
                        slider.setValue(i);
                        slider.callOnClick();
                        Integer[] body = new Integer[1];
                        body[0] = (int) slider.getValue();
                        ApiClient.getInstance().executeAction(device.getId(), "setLevel", body, new Callback<Result<Object>>() {
                            @Override
                            public void onResponse(@NonNull Call<Result<Object>> call,@NonNull Response<Result<Object>> response) {
                                updateDevice();
                            }

                            @Override
                            public void onFailure(@NonNull Call<Result<Object>> call,@NonNull Throwable t) {
                                Log.e("Slider level", t.getLocalizedMessage());
                            }
                        });
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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        device = (Device) getArguments().getParcelable(ARG_PARAM);
        state = (BlindsDeviceState) device.getState();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());

        this.speechRecognizer.setRecognitionListener(new BlindsActions.BlindsRecognitionListener());

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blinds_actions, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        updateDevice();

        switchMaterial = getActivity().findViewById(R.id.switchMaterial);
        slider = getActivity().findViewById(R.id.level_slider);
        currLevel = getActivity().findViewById(R.id.curr_level);
        currStatus = getActivity().findViewById(R.id.curr_status);
        switchMaterial.setOnClickListener(v -> {
            String action;
            if(switchMaterial.isChecked()) action = "open";
            else action = "close";
            ApiClient.getInstance().executeAction(device.getId(), action, new Object[0], new Callback<Result<Object>>() {
                @Override
                public void onResponse(@NonNull Call<Result<Object>> call,@NonNull Response<Result<Object>> response) {
                    updater();
                }

                @Override
                public void onFailure(@NonNull Call<Result<Object>> call,@NonNull Throwable t) {
                    Log.e("blind Action", t.getLocalizedMessage());
                }
            });
        });


        slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                Integer[] body = new Integer[1];
                body[0] = (int) slider.getValue();
                ApiClient.getInstance().executeAction(device.getId(), "setLevel", body, new Callback<Result<Object>>() {
                    @Override
                    public void onResponse(@NonNull Call<Result<Object>> call,@NonNull Response<Result<Object>> response) {
                        updateDevice();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Result<Object>> call,@NonNull Throwable t) {
                        Log.e("Slider level", t.getLocalizedMessage());
                    }
                });
            }
        });
    }

    public void viewHandler() {
        switch (state.getStatus()) {
            case "closed":
                switchMaterial.setChecked(false);
                switchMaterial.setClickable(true);
                switchMaterial.setText("");
                currStatus.setText(R.string.closed);
                break;

            case "opened":
                switchMaterial.setChecked(true);
                switchMaterial.setClickable(true);
                switchMaterial.setText("");
                currStatus.setText(R.string.opened);
                break;

            case "opening":
                switchMaterial.setChecked(true);
                switchMaterial.setClickable(false);
                switchMaterial.setText(R.string.wait_msg);
                currStatus.setText(R.string.opening);
                break;

            case "closing":
                switchMaterial.setChecked(false);
                switchMaterial.setClickable(false);
                switchMaterial.setText(R.string.wait_msg);
                currStatus.setText(R.string.closing);
                break;
        }

        slider.setValue(state.getLevel());
        currLevel.setText(String.valueOf(state.getCurrentLevel()));

    }

    public void updater() {

        if(fetcherHandler == null || fetcherHandler.isCancelled()) {
            Runnable fetcher = this::updateHandler;
            fetcherHandler = scheduler.scheduleAtFixedRate(fetcher, 0, 1, TimeUnit.SECONDS);
        }
    }

    public void updateHandler() {
        ApiClient.getInstance().getDevice(device.getId(), new Callback<Result<Device>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Device>> call, @NonNull Response<Result<Device>> response) {
                device = response.body().getResult();
                state = (BlindsDeviceState) device.getState();
                viewHandler();
                if(state.getStatus().equals("closed") || state.getStatus().equals("opened")) {
                    fetcherHandler.cancel(true);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<Device>> call, @NonNull Throwable t) {
                Log.e("update device", t.getLocalizedMessage());
                if(fetcherHandler != null && !fetcherHandler.isCancelled()) {
                    fetcherHandler.cancel(true);
                }
            }
        });
    }

    public void updateDevice() {
        ApiClient.getInstance().getDevice(device.getId(), new Callback<Result<Device>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Device>> call,@NonNull Response<Result<Device>> response) {
                device = response.body().getResult();
                state = (BlindsDeviceState) device.getState();
                viewHandler();
            }

            @Override
            public void onFailure(@NonNull Call<Result<Device>> call, @NonNull Throwable t) {
                Log.e("update device", t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        speechRecognizer.destroy();
    }
}