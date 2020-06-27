package ar.edu.itba.hci.api.models.devices;

import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.devices.states.OvenDeviceState;

public class OvenDevice extends Device<OvenDeviceState> {

    @Override
    public String toString() {
        return super.toString() + "state: " + getState().toString();
    }
}
