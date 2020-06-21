package ar.edu.itba.hci.ui.routines;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.models.Routine;

public class RoutinesFragment extends Fragment {

    private RoutinesViewModel routinesViewModel;

    List<Routine> routineList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_routines, container, false);
        RecyclerView rv = root.findViewById(R.id.rv_routines);
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