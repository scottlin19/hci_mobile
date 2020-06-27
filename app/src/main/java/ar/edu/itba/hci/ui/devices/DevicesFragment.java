package ar.edu.itba.hci.ui.devices;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


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

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import static androidx.core.content.FileProvider.getUriForFile;

public class DevicesFragment extends Fragment {



   private List<Category> categoryList;
   private   List<Device> deviceList;
    private Map<String,Boolean> categoryMap;
    private Fragment instance;

    private final static int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAKE_PHOTO_TAG = "Take Photo";
    private BarcodeDetector detector;
    private Uri photoUri;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        detector =
                new BarcodeDetector.Builder(getActivity().getApplicationContext())
                        .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                        .build();

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
                  deviceList = new ArrayList<>();

                   // System.out.println("DEVICES "+response.body().getResult());
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

        Toolbar toolbar = root.findViewById(R.id.device_details_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.inflateMenu(R.menu.devices_menu);
        toolbar.setOnMenuItemClickListener((Toolbar.OnMenuItemClickListener) item -> {
                    switch (item.getItemId()) {

                        case R.id.qr_scan:
                            checkQr();
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



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        System.out.println("on activity result fragment");
        System.out.println("requestoode = "+requestCode + " resultcode = "+resultCode+ "activityresultok= "+Activity.RESULT_OK);
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            System.out.println("on activity result fragment ok ");

            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
                scanQr(bitmap);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        /*
                Bitmap imageBitmap = (Bitmap)  extras.get(MediaStore.EXTRA_OUTPUT);

                scanQr(imageBitmap);*/
                System.out.println("termino de scannear");
               /* Intent intent = new Intent(getContext(), DeviceDetailsActivity.class);
                intent.putExtra("image",imageBitmap);
                getContext().startActivity(intent);*/

                //getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,newFragment).addToBackStack(null).commit();

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void checkQr(){

        waitUntilBarcodeDetectorIsOperational(detector, 10);
    }


    private void waitUntilBarcodeDetectorIsOperational(BarcodeDetector detector, int retries) {
        final Handler handler = new Handler();
        Toast.makeText(getContext(), R.string.qr_wait, Toast.LENGTH_LONG).show();
        handler.postDelayed(() -> {


            if (retries > 0) {
                if(detector.isOperational()) {
                    takePhoto();
                } else {
                    waitUntilBarcodeDetectorIsOperational(detector, retries - 1);
                }
            } else {

                Toast.makeText(getContext(), R.string.error_message, Toast.LENGTH_LONG).show();
            }
        }, 10000);
    }


    private void takePhoto() {
        System.out.println("take photo");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(TAKE_PHOTO_TAG, ex.toString());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoUri = getUriForFile(getContext(),
                        getActivity().getApplicationContext().getPackageName() + ".fileprovider",
                        photoFile);
                Log.d(TAKE_PHOTO_TAG, photoUri.toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "Photo_" + UUID.randomUUID();
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );
    }

    private boolean scanQr(Bitmap bitmap){
        // Detect the barcode
        System.out.println("scanning");
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Barcode> barcodeArray = detector.detect(frame);

        if(barcodeArray != null && barcodeArray.size() > 0){

            Barcode barcode = barcodeArray.valueAt(0);
            System.out.println("qr scan: " + barcode.rawValue);

            if(deviceList != null){
                System.out.println("deviceListqr"+ deviceList);
               ApiClient.getInstance().getDevice(barcode.rawValue, new Callback<Result<Device>>() {
                   @Override
                   public void onResponse(Call<Result<Device>> call, Response<Result<Device>> response) {
                        if(response.isSuccessful()){
                            Device device = response.body().getResult();
                            System.out.println("device found: "+device);
                            if(device != null){

                                    System.out.println(device);
                                    Intent intent = new Intent(getContext(), DeviceDetailsActivity.class);
                                    intent.putExtra("device", device.getId());
                                    startActivity(intent);
                                }else{
                                System.out.println("device not found");
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(getResources().getString(R.string.qr_device_not_found_1));
                                    sb.append(" "+barcode.rawValue +" ");
                                    sb.append(getResources().getString(R.string.qr_device_not_found_2));
                                    Toast.makeText(getContext(), sb.toString(), Toast.LENGTH_LONG).show();
                                }

                        }else{
                            System.out.println("device not found");
                            StringBuilder sb = new StringBuilder();
                            sb.append(getResources().getString(R.string.qr_device_not_found_1));
                            sb.append(" "+barcode.rawValue +" ");
                            sb.append(getResources().getString(R.string.qr_device_not_found_2));
                            Toast.makeText(getContext(), sb.toString(), Toast.LENGTH_LONG).show();
                        }
                   }

                   @Override
                   public void onFailure(Call<Result<Device>> call, Throwable t) {
                        Log.e("QR SCAN","Device call failure");
                   }
               });

            }else{

                System.out.println("device list empty");

                StringBuilder sb = new StringBuilder();
                sb.append(getResources().getString(R.string.qr_device_not_found_1));
                sb.append(" "+barcode.rawValue +" ");
                sb.append(getResources().getString(R.string.qr_device_not_found_2));
                Toast.makeText(getContext(), sb.toString(), Toast.LENGTH_LONG).show();
            }


        }else{
            System.out.println("qr fail");
            Toast.makeText(getContext(), R.string.qr_scan_failed, Toast.LENGTH_LONG).show();
        }
        // Decode the barcode


        return false;
    }
}