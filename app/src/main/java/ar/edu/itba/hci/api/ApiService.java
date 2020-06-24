package ar.edu.itba.hci.api;
import java.util.List;
import java.util.Objects;

import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.Room;
import ar.edu.itba.hci.api.models.Routine;
import ar.edu.itba.hci.api.models.devices.states.DeviceState;
import ar.edu.itba.hci.api.models.devices.states.DoorDeviceState;
import ar.edu.itba.hci.api.models.devices.states.SpeakerDeviceState;
import ar.edu.itba.hci.api.models.devices.states.SpeakerSong;
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

    @GET("devices/{deviceId}/logs/limit/{limit}/offset/{offset}")
    Call<Result<Object>> getLogs(@Path("deviceId") String deviceId, @Path("limit") String limit, @Path("offset") String offset);


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
