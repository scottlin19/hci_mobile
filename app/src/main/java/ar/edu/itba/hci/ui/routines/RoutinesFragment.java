package ar.edu.itba.hci.ui.routines;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.models.Routine;
import ar.edu.itba.hci.ui.devices.category.RecyclerViewCategoryAdapter;

public class RoutinesFragment extends Fragment {

    private RoutinesViewModel routinesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        routinesViewModel =
                ViewModelProviders.of(this).get(RoutinesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_routines, container, false);

        /*final TextView textView = root.findViewById(R.id.text_routine);
        routinesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        /*RecyclerView rv = (RecyclerView) root.findViewById(R.id.rv_routines);

        RecyclerViewCategoryAdapter adapter = new RecyclerViewCategoryAdapter(getContext(),);
        rv.setLayoutManager(new GridLayoutManager(getContext(),3));
        rv.setAdapter(adapter);*/

        return root;
    }
}