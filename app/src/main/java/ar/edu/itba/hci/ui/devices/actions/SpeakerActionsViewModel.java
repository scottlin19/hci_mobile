package ar.edu.itba.hci.ui.devices.actions;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import ar.edu.itba.hci.api.models.devices.states.SpeakerSong;

public class SpeakerActionsViewModel extends ViewModel {

    MutableLiveData<SpeakerSong> song;
    MutableLiveData<String> progress;


}
