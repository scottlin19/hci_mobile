package ar.edu.itba.hci.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.ui.devices.DevicesFragment;
import ar.edu.itba.hci.ui.rooms.RoomsFragment;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

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


}