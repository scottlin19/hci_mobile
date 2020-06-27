package ar.edu.itba.hci.ui.devices;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Device;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceDetailsViewModel extends ViewModel implements ViewModelProvider.Factory {
    private MutableLiveData<Device> device;
    private String devId;

    public DeviceDetailsViewModel(@NonNull String devId) {
        this.device = new MutableLiveData<>();
        this.devId = devId;
        loadDevice();
    }

    private void loadDevice() {
        ApiClient.getInstance().getDevice(devId, new Callback<Result<Device>>() {
            @Override
            public void onResponse(Call<Result<Device>> call, Response<Result<Device>> response) {
                if(response.isSuccessful()) {
                    device.setValue(response.body().getResult());
                }
                else {
                    Log.e("Device Activity", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Result<Device>> call, Throwable t) {
                Log.e("Device Activity failure", t.getLocalizedMessage());
            }
        });
    }

    public LiveData<Device> getDevice() { return device; }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DeviceDetailsViewModel(devId);
    }
}
