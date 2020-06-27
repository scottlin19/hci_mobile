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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.Locale;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.devices.states.DoorDeviceState;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoorActions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoorActions extends Fragment {

    private static final String ARG_PARAM = "device";

    private static final String TAG = "SpeechRecognizer";
    private static final int REQUEST_RECORD_AUDIO = 1;
    private SpeechRecognizer speechRecognizer;
    private FloatingActionButton sttButton;

    private Device device;
    private DoorDeviceState state;
    private SwitchMaterial switchStatus, switchLock;
    private TextView statusText;
    private ImageView lockImg;

    public DoorActions() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param device Parameter 1.
     * @return A new instance of fragment VacuumDetailFragment.
     */
    public static DoorActions newInstance(@NonNull Device device) {
        DoorActions fragment = new DoorActions();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, device);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        device = getArguments().getParcelable(ARG_PARAM);
        state = (DoorDeviceState) device.getState();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_door_actions, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        this.speechRecognizer.setRecognitionListener(new DoorRecognitionListener());
        sttButton = getActivity().findViewById(R.id.stt_button);
        if (sttButton != null) {
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
        switchStatus = getActivity().findViewById(R.id.status_switch);
        switchLock = getActivity().findViewById(R.id.lock_switch);
        statusText = getActivity().findViewById(R.id.state_text);
        lockImg = getActivity().findViewById(R.id.lock_img);
        updateDevice();
        switchStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String action;
            if (isChecked) {
                action = "close";
            } else {
                action = "open";

            }

            ApiClient.getInstance().executeAction(device.getId(), action, new Object[0], new Callback<Result<Object>>() {
                @Override
                public void onResponse(@NonNull Call<Result<Object>> call, @NonNull Response<Result<Object>> response) {
                    updateDevice();
                }

                @Override
                public void onFailure(@NonNull Call<Result<Object>> call, @NonNull Throwable t) {
                    Log.e("switch status", t.getLocalizedMessage());
                }
            });
        });

        switchLock.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String action;
            if (isChecked) {
                action = "lock";
            } else {
                action = "unlock";
            }

            ApiClient.getInstance().executeAction(device.getId(), action, new Object[0], new Callback<Result<Object>>() {
                @Override
                public void onResponse(@NonNull Call<Result<Object>> call, @NonNull Response<Result<Object>> response) {
                    updateDevice();
                }

                @Override
                public void onFailure(@NonNull Call<Result<Object>> call, @NonNull Throwable t) {
                    Log.e("switch lock", t.getLocalizedMessage());
                }
            });
        });
    }

    public void viewHandler() {
        if (state == null) return;
        if (state.getStatus().equals("closed")) {
            switchStatus.setChecked(true);
            statusText.setText(getResources().getString(R.string.closed));
        } else {
            switchStatus.setChecked(false);
            statusText.setText(getResources().getString(R.string.opened));
        }

        if (state.getLock().equals("locked")) {
            switchLock.setChecked(true);
            lockImg.setImageResource(R.drawable.ic_outline_lock_24);
        } else {
            switchLock.setChecked(false);
            lockImg.setImageResource(R.drawable.ic_outline_lock_open_24);
        }
    }

    public void updateDevice() {
        ApiClient.getInstance().getDevice(device.getId(), new Callback<Result<Device>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Device>> call, @NonNull Response<Result<Device>> response) {
                device = response.body().getResult();
                state = (DoorDeviceState) device.getState();
                viewHandler();
            }

            @Override
            public void onFailure(@NonNull Call<Result<Device>> call, @NonNull Throwable t) {
                Log.e("update device", t.getLocalizedMessage());
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
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 3000);
        sttButton.setColorFilter(Color.RED);
        speechRecognizer.startListening(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        speechRecognizer.destroy();
    }

    private class DoorRecognitionListener implements RecognitionListener {

        private String[] sstStatus = {
                getResources().getString(R.string.close).toLowerCase(),
                getResources().getString(R.string.open).toLowerCase(),
        };

        private String[] sstLock = {
                getResources().getString(R.string.lock).toLowerCase(),
                getResources().getString(R.string.unlock).toLowerCase(),
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
            sttButton.setColorFilter(ContextCompat.getColor(getContext(), R.color.textColorPrimary));
            Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onResults(Bundle results) {
            Log.d(TAG, "onResults " + results);
            ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            sttButton.setColorFilter(ContextCompat.getColor(getContext(), R.color.textColorPrimary));
            String lower = data.get(0).toLowerCase();
            System.out.println(lower);
            if (!compareSwitch(lower, sstStatus, switchStatus) && !compareSwitch(lower, sstLock, switchLock)) {
                Toast.makeText(getContext(), getResources().getString(R.string.sstNotRecognized), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.sstSuccess), Toast.LENGTH_SHORT).show();

            }

        }

        private boolean compareSwitch(String toFind, String[] sstStrings, SwitchMaterial s) {
            for (int i = 0; i < 2; i++) {
                if (toFind.equals(sstStrings[i])) {
                    if ((!s.isChecked() && i == 0) || (s.isChecked() && i == 1)) {
                        s.performClick();
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