package ar.edu.itba.hci.api;
import java.util.List;

import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.Room;
import ar.edu.itba.hci.api.models.Routine;
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

    //DEVICES
    @PUT("devices/{deviceId}")
    @Headers("Content-Type: application/json")
    Call<Result<Boolean>> modifyDevice(@Path("deviceId") String deviceId, @Body Device device);

    @GET("devices/{deviceId}")
    Call<Result<Device>> getDevice(@Path("deviceId") String deviceId);

    @GET("devices")
    Call<Result<List<Device>>> getDevices();

    //ROUTINES
    @GET("routines")
    Call<Result<List<Routine>>> getRoutines();
}
