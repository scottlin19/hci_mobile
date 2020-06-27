package ar.edu.itba.hci.ui.rooms;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.models.Room;
import ar.edu.itba.hci.ui.home.HomeFragment;

public class RoomsFragment extends Fragment {
    RecyclerView rv;
    List<Room> rooms;
    RecyclerViewRoomsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rooms, container, false);
        Log.d("CREATED","CREATED ROOMS FRAGMENT");
        rv = root.findViewById(R.id.rv_rooms);
        RoomsViewModel roomsViewModel = new ViewModelProvider(this).get(RoomsViewModel.class);
        roomsViewModel.getRooms().observe(getViewLifecycleOwner(), rooms -> {
            this.rooms = rooms;
            Log.d("FETCHED","FETCHED ROOMS");
            Log.d("ROOMS",this.rooms.toString());
            adapter = new RecyclerViewRoomsAdapter(getContext(), rooms);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
            rv.setLayoutManager(gridLayoutManager);
            rv.setAdapter(adapter);
        });


        Toolbar toolbar = root.findViewById(R.id.rooms_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> {


            FragmentManager fm = getActivity()
                    .getSupportFragmentManager();
            fm.popBackStack();
//            HomeFragment newFragment = new HomeFragment();
//            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
//
//
//            // Replace whatever is in the fragment_container view with this fragment,
//            // and add the transaction to the back stack so the user can navigate back
//            transaction.replace(R.id.nav_host_fragment, newFragment);
//            transaction.addToBackStack(null);
//
//            // Commit the transaction
//            transaction.commit();
        });
        return root;
    }
}

