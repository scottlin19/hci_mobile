package ar.edu.itba.hci.ui.devices.category;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import ar.edu.itba.hci.database.DeviceRepository;

public class DeviceModel extends AndroidViewModel {

    private DeviceRepository repo;
    public DeviceModel(@NonNull Application application, DeviceRepository repo) {
        super(application);
        this.repo = repo;
    }
}
