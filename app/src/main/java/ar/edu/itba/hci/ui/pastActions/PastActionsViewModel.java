package ar.edu.itba.hci.ui.pastActions;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.PastAction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PastActionsViewModel extends ViewModel {
    private final Integer LIMIT = 10000;
    private final Integer OFFSET = 0;
    MutableLiveData<List<PastAction>> actionsList;
    MutableLiveData<List<Device>> deviceList;


    public PastActionsViewModel() {
        actionsList = new MutableLiveData<>();
        deviceList = new MutableLiveData<>();
        loadActions();
    }

    private void loadActions() {

        ApiClient.getInstance().getLogs(LIMIT, OFFSET, new Callback<Result<List<PastAction>>>() {
            @Override
            public void onResponse(Call<Result<List<PastAction>>> call, Response<Result<List<PastAction>>> response) {
                if (response.isSuccessful()) {
                    List<PastAction> result = response.body().getResult();
                    if (result != null) {
                        System.out.println("PAST ACTIONS: " + result);
                        filterActions(result);
                        System.out.println("FILTERED PAST ACTIONS: " + result);
                    }
                } else {
                    Log.e("Past Actions response", response.message());
                }
            }

            @Override
            public void onFailure(Call<Result<List<PastAction>>> call, Throwable t) {
                Log.e("Past Actions failure", t.getLocalizedMessage());
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void filterActions(List<PastAction> rawActions) {
        System.out.println(String.format("raw size: %d", rawActions.size()));
        ApiClient.getInstance().getDevices(new Callback<Result<List<Device>>>() {

            @Override
            public void onResponse(Call<Result<List<Device>>> call, Response<Result<List<Device>>> response) {
                if (response.isSuccessful()) {
                    List<PastAction> filteredActions = new ArrayList<>();
                    List<Device> devices = response.body().getResult();
                    deviceList.setValue(devices);

                    for (Device d : devices) {
                        filteredActions.addAll(rawActions.stream().filter(pa -> pa.getDeviceId().equals(d.getId())).collect(Collectors.toList()));
                    }
                    filteredActions.sort(Comparator.reverseOrder());
                    actionsList.setValue(filteredActions);
                    System.out.println(String.format("filtered size: %d", filteredActions.size()));

                } else {
                    Log.e("Filter Actions", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Result<List<Device>>> call, Throwable t) {
                Log.e("Filter Actions failure", t.getLocalizedMessage());
            }
        });
    }

    public LiveData<List<PastAction>> getActions() {
        return actionsList;
    }

    public LiveData<List<Device>> getDevices() {
        return deviceList;
    }

}

