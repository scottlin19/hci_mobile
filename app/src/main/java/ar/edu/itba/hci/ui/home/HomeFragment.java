package ar.edu.itba.hci.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.stream.Collectors;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.ui.devices.DevicesFragment;
import ar.edu.itba.hci.ui.devices.RecyclerViewDeviceAdapter;
import ar.edu.itba.hci.ui.rooms.RoomsFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private List<Device> fav_devices;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.button_rooms);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        Button device_button = root.findViewById(R.id.button_devices);
        device_button.setOnClickListener(view -> {

            DevicesFragment newFragment = new DevicesFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();


            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.nav_host_fragment, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        });
        Button rooms_button = root.findViewById(R.id.button_rooms);
        rooms_button.setOnClickListener(view -> {
            RoomsFragment newFragment = new RoomsFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.nav_host_fragment, newFragment);
            transaction.addToBackStack(null);
            // Commit the transaction
            transaction.commit();
//               ((MainActivity) getActivity()).setCurrentFragment(new RoomsFragment());
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        RecyclerView rv = getActivity().findViewById(R.id.rv_fav);
        fetchFavorites(rv);
    }

    private void fetchFavorites(RecyclerView rv) {
        ApiClient.getInstance().getDevices(new Callback<Result<List<Device>>>() {
            @Override
            public void onResponse(Call<Result<List<Device>>> call, Response<Result<List<Device>>> response) {
                if(response.isSuccessful()){
                    fav_devices = response.body().getResult().stream().filter(device -> device.getMeta().getFavorite()).collect(Collectors.toList());
                    rv.setLayoutManager(new GridLayoutManager(getContext(),2, GridLayoutManager.VERTICAL, false));
                    rv.setAdapter(new RecyclerViewDeviceAdapter(getContext(),fav_devices));
                }
            }

            @Override
            public void onFailure(Call<Result<List<Device>>> call, Throwable t) {

            }
        });
    }
}