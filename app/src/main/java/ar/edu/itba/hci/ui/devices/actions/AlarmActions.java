package ar.edu.itba.hci.ui.devices.actions;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.devices.states.AlarmDeviceState;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlarmActions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlarmActions extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "device";

    // TODO: Rename and change types of parameters
    private Device<AlarmDeviceState> device;
    private TextInputEditText securityCodeInput,newSecurityCodeInput;
    private ImageButton unlockBtn,showChangeBtn;
    private Button changeBtn;
    private List<String> statusList;
    private LinearLayout new_code_layout,status_layout;
    private boolean lock;



    public AlarmActions() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param device Parameter 1.
     * @return A new instance of fragment VacuumDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlarmActions newInstance(Device<AlarmDeviceState> device) {
        AlarmActions fragment = new AlarmActions();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, device);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            device = (Device) getArguments().getParcelable(ARG_PARAM);
            statusList = new ArrayList<>();
            lock = true;
            System.out.println("DEVICE STATE CLASS"+ this.device.getState().getClass().toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =inflater.inflate(R.layout.fragment_alarm_actions, container, false);

        securityCodeInput = root.findViewById(R.id.security_code_input);
        securityCodeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() != 4){
                    disableImageButton(unlockBtn);


                }else{
                    enableImageButton(unlockBtn);

                }
                System.out.println(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        newSecurityCodeInput = root.findViewById(R.id.new_security_code_input);

        unlockBtn = root.findViewById(R.id.unlock_btn);
        unlockBtn.setOnClickListener(view -> {
            String code = securityCodeInput.getText().toString();
            Object[] params = {code,code};

            ApiClient.getInstance().executeAction(device.getId(), "changeSecurityCode", params, new Callback<Result<Object>>() {
                @Override
                public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                    if(response.isSuccessful()){
                        Boolean res = (Boolean) response.body().getResult();
                        if(res == true){
                            System.out.println("Esta bien");
                            lock = false;
                            showUnlockedLayouts();

                        }else{
                            System.out.println("Esta mal");
                        }
                    }
                }

                @Override
                public void onFailure(Call<Result<Object>> call, Throwable t) {
                    Log.e("SECURITY CODE INPUT","ERROR: CALL WAS A FAILURE");
                }
            });
        });

        showChangeBtn = root.findViewById(R.id.show_code_btn);
        showChangeBtn.setOnClickListener(view -> {

            if(this.new_code_layout.getVisibility() == View.VISIBLE){
                this.new_code_layout.setVisibility(View.GONE);
            }else{
                this.new_code_layout.setVisibility(View.VISIBLE);
            }
        });
        changeBtn = root.findViewById(R.id.change_btn);

        new_code_layout = root.findViewById(R.id.new_code_layout);

        status_layout = root.findViewById(R.id.alarm_status_layout
        );


        Spinner spinner = (Spinner) root.findViewById(R.id.alarm_state_spinner);
        final String[] statusArray = {"armStay",
                "armAway",
                "disarm",

        };
        Context context  =getContext();
        for(int i = 0; i < statusArray.length;i++){

            String status;
            int id = context.getResources().getIdentifier(statusArray[i], "string", context.getPackageName());
            if(id == 0) status = "MISSING_NAME";
            else status = context.getResources().getString(id);
            statusList.add(status);

        }
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,statusList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(statusList.indexOf( context.getResources().getString(context.getResources().getIdentifier(this.device.getState().getStatus(), "string", context.getPackageName()))));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object[] params = {};

                ApiClient.getInstance().executeAction(device.getId(), statusArray[(i)], params, new Callback<Result<Object>>() {
                    @Override
                    public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                        if(response.isSuccessful()){

                            System.out.println("STATUS CHANGED: "+response.body().getResult());

                        }
                    }

                    @Override
                    public void onFailure(Call<Result<Object>> call, Throwable t) {
                        Log.e("GENRE CHANGE","ERROR: CALL WAS A FAILURE");
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return root;

    }

    private void enableImageButton(ImageButton btn){
        btn.setEnabled(true);
        btn.setClickable(true);
        btn.setFocusable(true);
        btn.setImageResource(R.drawable.ic_lock_enabled);
        btn.setBackgroundResource(R.drawable.circular_btn_enabled_bg);


    }

    private void disableImageButton(ImageButton btn){

        btn.setEnabled(false);
        btn.setClickable(false);
        btn.setFocusable(false);
        btn.setBackgroundResource(R.drawable.circular_btn_disabled_bg);

        btn.setImageResource(lock ? R.drawable.ic_lock_disabled : R.drawable.ic_unlocked);
    }

    private void showUnlockedLayouts(){



        this.status_layout.setVisibility(View.VISIBLE);
        this.showChangeBtn.setVisibility(View.VISIBLE);

        this.securityCodeInput.setFocusable(false);

        disableImageButton(this.unlockBtn);
    }
}