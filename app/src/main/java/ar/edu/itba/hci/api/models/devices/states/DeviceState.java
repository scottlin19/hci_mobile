package ar.edu.itba.hci.api.models.devices.states;

import android.content.res.Resources;
import android.os.Parcelable;

import ar.edu.itba.hci.R;

public abstract class DeviceState implements Parcelable {

    public String[] compare(DeviceState deviceState, Resources resources) {
        return null;
    }

}