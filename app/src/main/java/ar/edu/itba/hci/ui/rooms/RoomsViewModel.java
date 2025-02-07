package ar.edu.itba.hci.ui.rooms;


import android.util.Log;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.Error;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.Room;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomsViewModel extends ViewModel {
    private  MutableLiveData<List<Room>> rooms = null;

    public LiveData<List<Room>> getRooms(){
        Log.d("GETROOMS","ENTER GETROOMS");
        if(rooms == null){
            rooms = new MutableLiveData<>();
            load();
            Log.d("FETCH","FETCHED ROOMS IN VIEW MODEL");
            Log.d("AUX",rooms.toString());

        }
        return rooms;
    }

    private void load() {
        Log.d("LOAD", "LOADING");

        ApiClient.getInstance().getRooms(new Callback<Result<List<Room>>>() {
            @Override
            public void onResponse(@NonNull Call<Result<List<Room>>> call, @NonNull Response<Result<List<Room>>> response) {
                if (response.isSuccessful()) {
                    Result<List<Room>> result = response.body();
                    if(result != null) {
                      /*  result.getResult().forEach(room -> Log.d("ROOM", room.toString()));*/
                        rooms.setValue(result.getResult());
                    }

                } else {
                    Log.d("ERROR","error");
                    handleError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<List<Room>>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });



    }
    private  <T> void handleError(Response<T> response) {
        Error error = ApiClient.getInstance().getError(response.errorBody());
//        String text = getResources().getString(R.string.error_message, error.getDescription().get(0), error.getCode());
//        Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
    }

    private  void handleUnexpectedError(Throwable t) {
        String LOG_TAG = "edu.itba.example.api";
        Log.e(LOG_TAG, t.toString());
    }



}
