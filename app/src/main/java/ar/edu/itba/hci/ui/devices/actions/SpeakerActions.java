package ar.edu.itba.hci.ui.devices.actions;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.slider.Slider;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.devices.states.DeviceState;
import ar.edu.itba.hci.api.models.devices.states.SpeakerDeviceState;
import ar.edu.itba.hci.api.models.devices.states.SpeakerSong;
import ar.edu.itba.hci.databinding.FragmentSpeakerActionsBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpeakerActions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpeakerActions extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "device";

    // TODO: Rename and change types of parameters
    private Device<SpeakerDeviceState> device;
    private List<String> genreList;
    private List<String> playList;
    private ImageButton playButton,stopButton,nextButton,prevButton;
    private TextView songTitle,songAuthor,songProgress,songDuration;

    private Slider volumeSlider;
    private Handler handler;
    private SpeakerSong song;

    public SpeakerActions() {
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
    public static SpeakerActions newInstance(Device<SpeakerDeviceState> device) {
        SpeakerActions fragment = new SpeakerActions();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, device);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
                    this.device = (Device) getArguments().getParcelable(ARG_PARAM);
                    this.genreList = new ArrayList<>();
                    this.playList = new ArrayList<>();





        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_speaker_actions, container, false);

        songAuthor = root.findViewById(R.id.song_author);
        songTitle = root.findViewById(R.id.song_title);
        songProgress = root.findViewById(R.id.song_progress);
        songDuration = root.findViewById(R.id.song_duration);

        if (this.device.getState().getSong() != null) {
            this.song =this.device.getState().getSong();

        }else{
            this.song = new SpeakerSong();
        }
        songAuthor.setText(song.getArtist());
        songTitle.setText(song.getTitle());
        songDuration.setText(song.getDuration());
        songProgress.setText("00:00");


            playButton = root.findViewById(R.id.speaker_play);
            playButton.setOnClickListener(view -> {
                Object[] params = {};
                String status = this.device.getState().getStatus();
                if(status.equals("playing")){
                    ApiClient.getInstance().executeAction(this.device.getId(), "pause", params, new Callback<Result<Object>>() {
                        @Override
                        public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                            if(response.isSuccessful()){
                                playButton.setBackgroundResource(R.drawable.ic_baseline_play_circle_outline_24);

                                System.out.println("PAUSE BUTTON");
                            }
                        }

                        @Override
                        public void onFailure(Call<Result<Object>> call, Throwable t) {
                            Log.e("PAUSE BUTTON","ERROR: CALL WAS A FAILURE");
                        }
                    });
                }else if(status.equals("stopped")){
                    ApiClient.getInstance().executeAction(this.device.getId(), "play", params, new Callback<Result<Object>>() {
                        @Override
                        public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                            if(response.isSuccessful()){
                                playButton.setBackgroundResource(R.drawable.ic_baseline_pause_circle_outline_24);

                                System.out.println("PLAY BUTTON: "+response.body().getResult());
                            }
                        }

                        @Override
                        public void onFailure(Call<Result<Object>> call, Throwable t) {
                            Log.e("PLAY BUTTON","ERROR: CALL WAS A FAILURE");
                        }
                    });

                }else{
                    ApiClient.getInstance().executeAction(this.device.getId(), "resume", params, new Callback<Result<Object>>() {
                        @Override
                        public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                            if(response.isSuccessful()){
                                playButton.setBackgroundResource(R.drawable.ic_baseline_pause_circle_outline_24);

                                System.out.println("RESUME BUTTON: "+response.body().getResult());
                            }
                        }

                        @Override
                        public void onFailure(Call<Result<Object>> call, Throwable t) {
                            Log.e("RESUME BUTTON","ERROR: CALL WAS A FAILURE");
                        }
                    });

                }

            });
            stopButton = root.findViewById(R.id.speaker_stop);
            stopButton.setOnClickListener(view -> {
                if(!this.device.getState().getStatus().equals("stopped")){
                    Object[] params = {};
                    ApiClient.getInstance().executeAction(this.device.getId(), "stop", params, new Callback<Result<Object>>() {
                        @Override
                        public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                            if(response.isSuccessful()){
                                songProgress.setText("0:00");
                                playButton.setBackgroundResource(R.drawable.ic_baseline_play_circle_outline_24);


                                System.out.println("STOP BUTTON");
                            }
                        }

                        @Override
                        public void onFailure(Call<Result<Object>> call, Throwable t) {
                            Log.e("STOP BUTTON","ERROR: CALL WAS A FAILURE");
                        }
                    });
                }



            });
            prevButton = root.findViewById(R.id.speaker_prev);
            prevButton.setOnClickListener(view -> {
                if(this.device.getState().getStatus().equals("playing")){
                    Object[] params = {};
                    ApiClient.getInstance().executeAction(this.device.getId(), "previousSong", params, new Callback<Result<Object>>() {
                        @Override
                        public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                            if(response.isSuccessful()){

                                System.out.println("PREV BUTTON");

                            }
                        }

                        @Override
                        public void onFailure(Call<Result<Object>> call, Throwable t) {
                            Log.e("PREV BUTTON","ERROR: CALL WAS A FAILURE");
                        }
                    });
                }

            });
            nextButton = root.findViewById(R.id.speaker_next);
            nextButton.setOnClickListener(view -> {
                if(this.device.getState().getStatus().equals("playing")){
                    Object[] params = {};
                    ApiClient.getInstance().executeAction(this.device.getId(), "nextSong", params, new Callback<Result<Object>>() {
                        @Override
                        public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                            if(response.isSuccessful()){

                                System.out.println("NEXT BUTTON");

                            }
                        }

                        @Override
                        public void onFailure(Call<Result<Object>> call, Throwable t) {
                            Log.e("PREV BUTTON","ERROR: CALL WAS A FAILURE");
                        }
                    });                }
            });



            volumeSlider = root.findViewById(R.id.volume_slider);
            volumeSlider.setValue(this.device.getState().getVolume() * 10);
            volumeSlider.addOnChangeListener((slider, value, fromUser) -> {
                Object[] params = {value/10};
                ApiClient.getInstance().executeAction(device.getId(), "setVolume", params, new Callback<Result<Object>>() {
                    @Override
                    public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                        if(response.isSuccessful()){
                            System.out.println("VOLUME CHANGED: "+ response.body().getResult());
                        }
                    }

                    @Override
                    public void onFailure(Call<Result<Object>> call, Throwable t) {
                        Log.e("VOLUME CHANGE","ERROR: CALL WAS A FAILURE");
                    }
                });
            });


            switch(this.device.getState().getStatus()){
                case "playing":
                    playButton.setBackgroundResource(R.drawable.ic_baseline_pause_circle_outline_24);
                    break;
                    default:
                        playButton.setBackgroundResource(R.drawable.ic_baseline_play_circle_outline_24);

            }


            Spinner spinner = (Spinner) root.findViewById(R.id.genre_list_spinner);
            final String[] genres = {"classical",
                    "country",
                    "dance",
                    "latina",
                    "pop",
            };
        Context context  =getContext();
            for(int i = 0; i < genres.length;i++){

                String genre;
                int id = context.getResources().getIdentifier(genres[i], "string", context.getPackageName());
                if(id == 0) genre = "MISSING_NAME";
                else genre = context.getResources().getString(id);
                genreList.add(genre);

            }


            ArrayAdapter<String>adapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_spinner_item,genreList);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            spinner.setSelection(genreList.indexOf( context.getResources().getString(context.getResources().getIdentifier(this.device.getState().getGenre(), "string", context.getPackageName()))));
            System.out.println("SELECTED ITEM " + spinner.getSelectedItem());

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String lastSelected =  context.getResources().getString(context.getResources().getIdentifier(device.getState().getGenre(), "string", context.getPackageName()));
                    String selected = genreList.get(i);
                    if(!lastSelected.equals(selected)){
                        Object[] params = {genreList.get(i).toLowerCase()};
                        System.out.println("SELECTED GENRE: "+genreList.get(i));
                        ApiClient.getInstance().executeAction(device.getId(), "setGenre", params, new Callback<Result<Object>>() {
                            @Override
                            public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                                if(response.isSuccessful()){
                                    songProgress.setText("0:00");
                                    System.out.println("GENRE CHANGED: "+response.body().getResult());

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

            handler = new Handler();
            int delay = 500;

            handler.post(new Runnable() {
                @Override
                public void run() {
                    ApiClient.getInstance().getDevice(device.getId(), new Callback<Result<Device>>() {
                        @Override
                        public void onResponse(Call<Result<Device>> call, Response<Result<Device>> response) {
                            if(response.isSuccessful()){
                                SpeakerSong newSong = ((SpeakerDeviceState) response.body().getResult().getState()).getSong();
                                if(newSong != null) {

                                    if(!newSong.getTitle().equals(song.getTitle())){
                                        songAuthor.setText(newSong.getArtist());
                                        songTitle.setText(newSong.getTitle());
                                        songDuration.setText(newSong.getDuration());
                                        songProgress.setText("0:00");
                                    }
                                    song = newSong;
                                    songProgress.setText(song.getProgress());


                                };


                                device.setState( ((SpeakerDeviceState) response.body().getResult().getState()));

                                //  System.out.println("DEVICE SPEAKER STATE:"+ device.getState().getStatus() + " song: " +song);

                            }
                        }

                        @Override
                        public void onFailure(Call<Result<Device>> call, Throwable t) {
                            Log.e("GET STATE ","ERROR: CALL WAS A FAILURE");
                        }
                    });
                    handler.postDelayed(this,delay);
                }
            });
            return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        this.handler.removeCallbacksAndMessages(null);
    }
}