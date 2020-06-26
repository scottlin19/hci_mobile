package ar.edu.itba.hci.database.wrapper;

import java.util.HashMap;
import java.util.Map;

import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.devices.states.AcDeviceState;
import ar.edu.itba.hci.api.models.devices.states.AlarmDeviceState;
import ar.edu.itba.hci.api.models.devices.states.BlindsDeviceState;
import ar.edu.itba.hci.api.models.devices.states.DoorDeviceState;
import ar.edu.itba.hci.api.models.devices.states.FaucetDeviceState;
import ar.edu.itba.hci.api.models.devices.states.FridgeDeviceState;
import ar.edu.itba.hci.api.models.devices.states.LampDeviceState;
import ar.edu.itba.hci.api.models.devices.states.OvenDeviceState;
import ar.edu.itba.hci.api.models.devices.states.SpeakerDeviceState;
import ar.edu.itba.hci.api.models.devices.states.VacuumDeviceState;


public class WrapperAdapter {
    private static Map<String,Class<?>> classMap = new HashMap<String,Class<?>>(){{
        put("speaker",SpeakerDeviceWrapper.class);
        put("faucet",FaucetDeviceWrapper.class);
        put("blinds",BlindsDeviceWrapper.class);
        put("oven",OvenDeviceWrapper.class);
        put("lamp",LampDeviceWrapper.class);
        put("ac",AcDeviceWrapper.class);
        put("door",DoorDeviceWrapper.class);
        put("alarm",AlarmDeviceWrapper.class);
        put("vacuum",VacuumDeviceWrapper.class);
        put("refrigerator",FridgeDeviceWrapper.class);

    }};

   public static Class<?> getWrapperClass(String typeId){
       return classMap.get(typeId);
   }

    public static AcDeviceWrapper fromDeviceToAcWrapper(Device device){
        return new AcDeviceWrapper(device.getId(),device.getName(),device.getType(),device.getMeta(), (AcDeviceState) device.getState(),device.getRoom());
    }
    public static AlarmDeviceWrapper fromDeviceToAlarmWrapper(Device device){
        return new AlarmDeviceWrapper(device.getId(),device.getName(),device.getType(),device.getMeta(), (AlarmDeviceState) device.getState(),device.getRoom());
    }

    public static BlindsDeviceWrapper fromDeviceToBlindsWrapper(Device device){
        return new BlindsDeviceWrapper(device.getId(),device.getName(),device.getType(),device.getMeta(), (BlindsDeviceState) device.getState(),device.getRoom());
    }

    public static DoorDeviceWrapper fromDeviceToDoorWrapper(Device device){
        return new DoorDeviceWrapper(device.getId(),device.getName(),device.getType(),device.getMeta(), (DoorDeviceState) device.getState(),device.getRoom());
    }

    public static FaucetDeviceWrapper fromDeviceToFaucetWrapper(Device device){
        return new FaucetDeviceWrapper(device.getId(),device.getName(),device.getType(),device.getMeta(), (FaucetDeviceState) device.getState(),device.getRoom());
    }

    public static FridgeDeviceWrapper fromDeviceToFridgeWrapper(Device device){
        return new FridgeDeviceWrapper(device.getId(),device.getName(),device.getType(),device.getMeta(), (FridgeDeviceState) device.getState(),device.getRoom());
    }

    public static LampDeviceWrapper fromDeviceToLampWrapper(Device device){
        return new LampDeviceWrapper(device.getId(),device.getName(),device.getType(),device.getMeta(), (LampDeviceState) device.getState(),device.getRoom());
    }

    public static OvenDeviceWrapper fromDeviceToOvenWrapper(Device device){
        return new OvenDeviceWrapper(device.getId(),device.getName(),device.getType(),device.getMeta(), (OvenDeviceState) device.getState(),device.getRoom());
    }

    public static VacuumDeviceWrapper fromDeviceToVacuumWrapper(Device device){
        return new VacuumDeviceWrapper(device.getId(),device.getName(),device.getType(),device.getMeta(), (VacuumDeviceState) device.getState(),device.getRoom());
    }

    public static SpeakerDeviceWrapper fromDeviceToSpeakerWrapper(Device device){
        return new SpeakerDeviceWrapper(device.getId(),device.getName(),device.getType(),device.getMeta(), (SpeakerDeviceState) device.getState(),device.getRoom());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Device fromWrapperToAc(AcDeviceWrapper wrapper){
       return new Device(wrapper.getId(),wrapper.getName(),wrapper.getDeviceType(),wrapper.getState(),wrapper.getRoom(),wrapper.getDeviceMeta());
    }
    public static Device fromWrapperToAlarm(AlarmDeviceWrapper wrapper){
        return new Device(wrapper.getId(),wrapper.getName(),wrapper.getDeviceType(),wrapper.getState(),wrapper.getRoom(),wrapper.getDeviceMeta());
    }
    public static Device fromWrapperToFaucet(FaucetDeviceWrapper wrapper){
        return new Device(wrapper.getId(),wrapper.getName(),wrapper.getDeviceType(),wrapper.getState(),wrapper.getRoom(),wrapper.getDeviceMeta());
    }
    public static Device fromWrapperToFridge(FridgeDeviceWrapper wrapper){
        return new Device(wrapper.getId(),wrapper.getName(),wrapper.getDeviceType(),wrapper.getState(),wrapper.getRoom(),wrapper.getDeviceMeta());
    }
    public static Device fromWrapperToBlinds(BlindsDeviceWrapper wrapper){
        return new Device(wrapper.getId(),wrapper.getName(),wrapper.getDeviceType(),wrapper.getState(),wrapper.getRoom(),wrapper.getDeviceMeta());
    }
    public static Device fromWrapperToOven(OvenDeviceWrapper wrapper){
        return new Device(wrapper.getId(),wrapper.getName(),wrapper.getDeviceType(),wrapper.getState(),wrapper.getRoom(),wrapper.getDeviceMeta());
    }
    public static Device fromWrapperToLamp(LampDeviceWrapper wrapper){
        return new Device(wrapper.getId(),wrapper.getName(),wrapper.getDeviceType(),wrapper.getState(),wrapper.getRoom(),wrapper.getDeviceMeta());
    }
    public static Device fromWrapperToSpeaker(SpeakerDeviceWrapper wrapper){
        return new Device(wrapper.getId(),wrapper.getName(),wrapper.getDeviceType(),wrapper.getState(),wrapper.getRoom(),wrapper.getDeviceMeta());
    }
    public static Device fromWrapperToDoor(DoorDeviceWrapper wrapper){
        return new Device(wrapper.getId(),wrapper.getName(),wrapper.getDeviceType(),wrapper.getState(),wrapper.getRoom(),wrapper.getDeviceMeta());
    }
    public static Device fromWrapperToVacuum(VacuumDeviceWrapper wrapper){
        return new Device(wrapper.getId(),wrapper.getName(),wrapper.getDeviceType(),wrapper.getState(),wrapper.getRoom(),wrapper.getDeviceMeta());
    }


}
