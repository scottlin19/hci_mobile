package ar.edu.itba.hci.api;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.PastAction;
import ar.edu.itba.hci.api.models.Room;
import ar.edu.itba.hci.api.models.Routine;
import ar.edu.itba.hci.api.models.devices.states.SpeakerDeviceState;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("FieldCanBeLocal")
public class ApiClient {
    private Retrofit retrofit;
    private ApiService service;
    private static ApiClient instance = null;
    // Use IP 10.0.2.2 instead of 127.0.0.1 when running Android emulator in the
    // same computer that runs the API.
    private final String BaseURL = "http://10.0.2.2:8080/api/";

    private ApiClient() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        gsonBuilder.registerTypeAdapter(Device.class,new DeviceDeserializer());
        Gson gson = gsonBuilder.create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.service = retrofit.create(ApiService.class);
    }

    public static synchronized ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }
    public Error getError(ResponseBody response) {
        Converter<ResponseBody, ErrorResult> errorConverter =
                this.retrofit.responseBodyConverter(ErrorResult.class, new Annotation[0]);
        try {
            ErrorResult responseError = errorConverter.convert(response);
            return responseError.getError();
        } catch (IOException e) {
            return null;
        }
    }

    //ROOMS

    public Call<Result<Boolean>> modifyRoom(Room room, Callback<Result<Boolean>> callback) {
        Call<Result<Boolean>> call = this.service.modifyRoom(room.getId(), room);
        call.enqueue(callback);
        return call;
    }


    public Call<Result<Room>> getRoom(String roomId, Callback<Result<Room>> callback) {
        Call<Result<Room>> call = this.service.getRoom(roomId);
        call.enqueue(callback);
        return call;
    }

    public Call<Result<List<Room>>> getRooms(Callback<Result<List<Room>>> callback) {
        Call<Result<List<Room>>> call = this.service.getRooms();
        call.enqueue(callback);
        return call;
    }
    //ROOM DEVICES
    public Call<Result<List<Device>>> getRoomDevices(String roomId,Callback<Result<List<Device>>> callback){
        Call<Result<List<Device>>> call = this.service.getRoomDevices(roomId);
        call.enqueue(callback);
        return call;
    }

    //DEVICES
    public Call<Result<Device>> getDevice(String deviceId, Callback<Result<Device>> callback){
        Call<Result<Device>> call = this.service.getDevice(deviceId);
        call.enqueue(callback);
        return call;
    }

    public Call<Result<List<Device>>> getDevices(Callback<Result<List<Device>>> callback){
        Call<Result<List<Device>>> call = this.service.getDevices();
        call.enqueue(callback);
        return call;
    }

    public Call<Result<Boolean>> modifyDevice(Device device, Callback<Result<Boolean>> callback) {
        Call<Result<Boolean>> call = this.service.modifyDevice(device.getId(), device);
        call.enqueue(callback);
        return call;
    }

    public Call<Result<List<PastAction>>> getLogs(Integer limit, @Nullable Integer offset, Callback<Result<List<PastAction>>> callback) {
        Call<Result<List<PastAction>>> call = this.service.getLogs(limit, offset);
        call.enqueue(callback);
        return call;
    }

    //ROUTINES
    public Call<Result<List<Routine>>> getRoutines(Callback<Result<List<Routine>>> callback){
        Call<Result<List<Routine>>> call = this.service.getRoutines();
        call.enqueue(callback);
        return call;
    }

    public Call<Result<Object>>executeRoutine(String routineId, Callback<Result<Object>> callback){
        Call<Result<Object>> call = this.service.executeRoutine(routineId);
        call.enqueue(callback);
        return call;
    }

    public Call<Result<Object>>executeAction(String deviceId, String actionName, Object[] body, Callback<Result<Object>> callback){
        Call<Result<Object>> call = this.service.executeAction(deviceId,actionName,body);
        call.enqueue(callback);
        return call;
    }
}
