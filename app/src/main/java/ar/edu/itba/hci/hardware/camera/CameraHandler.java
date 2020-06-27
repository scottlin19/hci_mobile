package ar.edu.itba.hci.hardware.camera;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;

public class CameraHandler {

    private CameraHandler instance;
    private Context context;

    public CameraHandler(Context context) {
        this.context = context;
    }

    public void setContext(Context context){
        this.context = context;
    }

    public CameraHandler getInstace(Context context) {
        if (instance == null) {
            return new CameraHandler(context);
        }
        setContext(context);
        return instance;
    }


}
