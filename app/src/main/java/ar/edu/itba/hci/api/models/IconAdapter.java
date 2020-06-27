package ar.edu.itba.hci.api.models;

import java.util.HashMap;
import java.util.Map;

import ar.edu.itba.hci.R;

public class IconAdapter {
    private static Map<String,Integer> iconsMap = new HashMap<String,Integer>(){{
        put("image", R.drawable.ic_baseline_image_24);
        put("weekend",R.drawable.ic_baseline_weekend_24);
        put("mdi-chef-hat",R.drawable.chef_hat_white);
        put("mdi-car",R.drawable.ic_baseline_directions_car_24);
        put("restaurant",R.drawable.ic_baseline_restaurant_24);
        put("mdi-shower",R.drawable.shower);
        put("mdi-baby-bottle-outline",R.drawable.baby_bottle_outline);
        put("mdi-bed",R.drawable.ic_baseline_hotel_24);
        put("mdi-flower",R.drawable.ic_baseline_local_florist_24);
        put("mdi-speaker",R.drawable.ic_speaker);
       put("mdi-blinds",R.drawable.ic_blinds);
        put("mdi-lightbulb-on-outline",R.drawable.ic_lightbulb_on);
        put("mdi-stove",R.drawable.ic_stove);
        put("mdi-air-conditioner",R.drawable.ic_air_conditioner);
        put("mdi-door",R.drawable.ic_door);
        put("mdi-alarm-light-outline",R.drawable.ic_alarm_light_outline);
        put("mdi-robot-vacuum",R.drawable.ic_robot_vacuum);
        put("mdi-fridge-outline",R.drawable.ic_fridge);
        put("mdi-water-pump",R.drawable.ic_water_pump);
    }};

    private static Map<String,Integer> smallIconsMap = new HashMap<String,Integer>(){{
        put("image", R.drawable.ic_baseline_image_24_small);
        put("weekend",R.drawable.ic_baseline_weekend_24_small);
        put("mdi-speaker",R.drawable.ic_speaker_small);
        put("mdi-blinds",R.drawable.ic_blinds_small);
        put("mdi-lightbulb-on-outline",R.drawable.ic_lightbulb_on_small);
        put("mdi-stove",R.drawable.ic_stove_small);
        put("mdi-air-conditioner",R.drawable.ic_air_conditioner_small);
        put("mdi-door",R.drawable.ic_door_small);
        put("mdi-alarm-light-outline",R.drawable.ic_alarm_light_outline_small);
        put("mdi-robot-vacuum",R.drawable.ic_robot_vacuum_small);
        put("mdi-fridge-outline",R.drawable.ic_fridge_small);
        put("mdi-water-pump",R.drawable.ic_water_pump_small);
    }};

    public static Integer getIntIcon(String icon){
      return iconsMap.get(icon);
    }

    public static Integer getIntSmallIcon(String icon){
        return smallIconsMap.get(icon);
    }
}
