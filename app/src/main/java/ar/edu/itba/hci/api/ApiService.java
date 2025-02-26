package ar.edu.itba.hci.api;

import androidx.annotation.Nullable;

import java.util.List;

import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.PastAction;
import ar.edu.itba.hci.api.models.Room;
import ar.edu.itba.hci.api.models.Routine;
import ar.edu.itba.hci.api.models.devices.states.SpeakerDeviceState;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    //ROOMS
    @PUT("rooms/{roomId}")
    @Headers("Content-Type: application/json")
    Call<Result<Boolean>> modifyRoom(@Path("roomId") String roomId, @Body Room room);

    @GET("rooms/{roomId}")
    Call<Result<Room>> getRoom(@Path("roomId") String roomId);

    @GET("rooms")
    Call<Result<List<Room>>> getRooms();

    //ROOM DEVICES
    @GET("rooms/{roomId}/devices")
    Call<Result<List<Device>>> getRoomDevices(@Path("roomId") String roomId);
    //DEVICES
    @PUT("devices/{deviceId}")
    @Headers("Content-Type: application/json")
    Call<Result<Boolean>> modifyDevice(@Path("deviceId") String deviceId, @Body Device device);

    @GET("devices/{deviceId}")
    Call<Result<Device>> getDevice(@Path("deviceId") String deviceId);

    @GET("devices")
    Call<Result<List<Device>>> getDevices();

    @GET("devices/{deviceId}/state")
    Call<Result<SpeakerDeviceState>> getDeviceState(@Path("deviceId") String deviceId);

    @GET("devices/logs/limit/{limit}/offset/{offset}")
    Call<Result<List<PastAction>>> getLogs(@Path("limit") Integer limit, @Path("offset") Integer offset);

    @GET("devices/logs/limit/null/offset/null")
    Call<Result<List<PastAction>>> getLogs();


    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<Object>> executeAction(@Path("deviceId")String deviceId, @Path("actionName") String actionName, @Body Object[] body);


    //ROUTINES
    @GET("routines")
    Call<Result<List<Routine>>> getRoutines();

    @PUT("routines/{routineId}/execute")
    @Headers("Content-Type: application/json")
    Call<Result<Object>> executeRoutine(@Path("routineId") String routineId);



}
