package ar.edu.itba.hci.ui.routines;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Routine;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoutinesViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    MutableLiveData<List<Routine>> routineList;
    public RoutinesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is routine fragment");
        routineList = new MutableLiveData<>();
        loadRoutines();

    }

    public LiveData<String> getText() {
        return mText;
    }

    private void loadRoutines() {

        ApiClient.getInstance().getRoutines(new Callback<Result<List<Routine>>>() {
            @Override
            public void onResponse(@NonNull Call<Result<List<Routine>>> call, @NonNull Response<Result<List<Routine>>> response) {
                if (response.isSuccessful()) {
                    Result<List<Routine>> result = response.body();
                    if (result != null) {
                        List<Routine> aux = new ArrayList<>();
                        result.getResult().forEach(routine -> {
                            Log.println(Log.DEBUG, "Routine", routine.toString());
                            aux.add(routine);
                        });
                        routineList.setValue(aux);
                    }
                } else {
                    Log.d("ERROR", "error");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<List<Routine>>> call, @NonNull Throwable t) {

            }
        });
    }
    public LiveData<List<Routine>> getRoutines(){ return routineList; }
}