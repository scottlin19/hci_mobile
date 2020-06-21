package ar.edu.itba.hci.ui.routines;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.models.Routine;
import ar.edu.itba.hci.ui.devices.DevicesViewModel;
import ar.edu.itba.hci.ui.devices.category.RecyclerViewCategoryAdapter;

public class RoutinesFragment extends Fragment {

    private RoutinesViewModel routinesViewModel;

    List<Routine> routineList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_routines, container, false);
        RecyclerView rv = (RecyclerView) root.findViewById(R.id.rv_routines);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        rv.setLayoutManager(gridLayoutManager);
        routinesViewModel = new ViewModelProvider(this).get(RoutinesViewModel.class);

        routinesViewModel.getRoutines().observe(getViewLifecycleOwner(), routineList ->{
            this.routineList = routineList;
            final  RecyclerViewRoutinesAdapter adapter = new RecyclerViewRoutinesAdapter(getContext(),routineList);
            rv.setAdapter(adapter);
        });

        return root;
    }
}