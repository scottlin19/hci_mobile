package ar.edu.itba.hci.api;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.DeviceType;
import ar.edu.itba.hci.api.models.devices.AcDevice;
import ar.edu.itba.hci.api.models.devices.AlarmDevice;
import ar.edu.itba.hci.api.models.devices.BlindsDevice;
import ar.edu.itba.hci.api.models.devices.DoorDevice;
import ar.edu.itba.hci.api.models.devices.FaucetDevice;
import ar.edu.itba.hci.api.models.devices.FridgeDevice;
import ar.edu.itba.hci.api.models.devices.LampDevice;
import ar.edu.itba.hci.api.models.devices.OvenDevice;
import ar.edu.itba.hci.api.models.devices.SpeakerDevice;
import ar.edu.itba.hci.api.models.devices.VacuumDevice;

public class DeviceDeserializer implements JsonDeserializer<Device>{
    @Override
    public Device deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new Gson();
        final JsonObject jsonObject = json.getAsJsonObject();

        final JsonElement jsonElementType = jsonObject.get("type");

        DeviceType deviceType = gson.fromJson(jsonElementType,DeviceType.class);

        if(deviceType.getId().equals("c89b94e8581855bc")){
            return gson.fromJson(jsonObject,new TypeToken<SpeakerDevice>(){}.getType());
        }else if(deviceType.getId().equals("dbrlsh7o5sn8ur4i")){
            return gson.fromJson(jsonObject,new TypeToken<FaucetDevice>(){}.getType());

        }else if(deviceType.getId().equals("eu0v2xgprrhhg41g")){
            return gson.fromJson(jsonObject,new TypeToken<BlindsDevice>(){}.getType());
        }else if(deviceType.getId().equals("go46xmbqeomjrsjr")){
            return gson.fromJson(jsonObject,new TypeToken<LampDevice>(){}.getType());
        }else if(deviceType.getId().equals("im77xxyulpegfmv8")){
            return gson.fromJson(jsonObject,new TypeToken<OvenDevice>(){}.getType());
        }else if(deviceType.getId().equals("li6cbv5sdlatti0j")){
            return gson.fromJson(jsonObject,new TypeToken<AcDevice>(){}.getType());
        }else if(deviceType.getId().equals("lsf78ly0eqrjbz91")){
            return gson.fromJson(jsonObject,new TypeToken<DoorDevice>(){}.getType());
        }else if(deviceType.getId().equals("mxztsyjzsrq7iaqc")){
            return gson.fromJson(jsonObject,new TypeToken<AlarmDevice>(){}.getType());
        }else if(deviceType.getId().equals("ofglvd9gqx8yfl3l")){
            return gson.fromJson(jsonObject,new TypeToken<VacuumDevice>(){}.getType());
        }else if(deviceType.getId().equals("rnizejqr2di0okho")){
            return gson.fromJson(jsonObject,new TypeToken<FridgeDevice>(){}.getType());
        }
        return null;
    }
}
