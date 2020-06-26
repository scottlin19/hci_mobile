package ar.edu.itba.hci.ui.devices;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ar.edu.itba.hci.database.DeviceRepository;

public class DeviceModelFactory implements ViewModelProvider.Factory {

    private Application app;
    private static DeviceModel model;

    public DeviceModelFactory(Application app){
        this.app = app;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(model == null){
            model = new DeviceModel(app,new DeviceRepository(app));
        }
        return (T) model;
    }
}
