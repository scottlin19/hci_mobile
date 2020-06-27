package ar.edu.itba.hci.ui.devices;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import ar.edu.itba.hci.QrScan;
import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Device;

import ar.edu.itba.hci.api.models.IconAdapter;

import ar.edu.itba.hci.ui.devices.category.Category;
import ar.edu.itba.hci.ui.devices.category.RecyclerViewCategoryAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DevicesFragment extends Fragment {

   List<Category> categoryList;
    Map<String,Boolean> categoryMap;
    Fragment instance;
    private final static int REQUEST_IMAGE_CAPTURE = 1;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_devices, container, false);

        categoryList = new ArrayList<>();

        categoryMap = new HashMap<String,Boolean>(){{
            put("ac",false);
            put("alarm",false);
            put("blinds",false);
            put("vacuum",false);
            put("oven",false);
            put("refrigerator",false);
            put("door",false);
            put("lamp",false);
            put("speaker",false);
            put("faucet",false);
        }};
        instance = this;

        RecyclerView rv = root.findViewById(R.id.rv_category);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        rv.setLayoutManager(gridLayoutManager);

        ApiClient.getInstance().getDevices(new Callback<Result<List<Device>>>() {
            @Override
            public void onResponse(Call<Result<List<Device>>> call, Response<Result<List<Device>>> response) {
                if(response.isSuccessful()){
                    List<Device> deviceList = new ArrayList<>();

                    System.out.println("DEVICES "+response.body().getResult());
                    response.body().getResult().forEach(device->{

                        if((device.getMeta().getNotif_status()) == null){
                            device.getMeta().setNotif_status(true);
                            ApiClient.getInstance().modifyDevice(device, new Callback<Result<Boolean>>() {
                                @Override
                                public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                                    if(response.isSuccessful()){
                                        deviceList.add(device);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Result<Boolean>> call, Throwable t) {

                                }
                            });
                        }else{
                            deviceList.add(device);
                        }


                    });
                    deviceList.forEach(device ->{
                        if(categoryMap.get(device.getType().getName()) == false){
                            String categName = device.getType().getName();
                            categoryMap.put(categName,true);
                            Integer icon_id = IconAdapter.getIntIcon(device.getMeta().getIcon());
                            if(icon_id == null){
                                icon_id = R.drawable.ic_image;
                            }
                            categoryList.add(new Category(categName,icon_id));

                        }

                    });
                    final RecyclerViewCategoryAdapter adapter = new RecyclerViewCategoryAdapter(getContext(), categoryList,instance);
                    rv.setAdapter(adapter);

                }

            }

            @Override
            public void onFailure(Call<Result<List<Device>>> call, Throwable t) {

            }
        });

        Toolbar toolbar = root.findViewById(R.id.device_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.inflateMenu(R.menu.devices_menu);
        toolbar.setOnMenuItemClickListener((Toolbar.OnMenuItemClickListener) item -> {
                    switch (item.getItemId()) {

                        case R.id.qr_scan:
                            scanQr();
                            //qr scanning
                            break;

                        default:
                            return true;
                    }
                    return false;
                }

        );

        toolbar.setNavigationOnClickListener(v -> {

            FragmentManager fm = getActivity()
                    .getSupportFragmentManager();
            fm.popBackStack();
//
        });





        return root;
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void scanQr(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getActivity().getPackageManager()) != null){
            getActivity().startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);

           Toast toast =  Toast.makeText(getContext(), getResources().getString(R.string.qr_toast), Toast.LENGTH_LONG);
           toast.setGravity(Gravity.TOP,0,0);//Corr
            toast.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            Bundle extras = data.getExtras();
            if(extras != null){
                Bitmap imageBitmap = (Bitmap)  extras.get("data");
                Intent intent = new Intent(getContext(), DeviceDetailsActivity.class);
                intent.putExtra("image",imageBitmap);
                getContext().startActivity(intent);

                //getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,newFragment).addToBackStack(null).commit();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void checkQr(){

    }
}