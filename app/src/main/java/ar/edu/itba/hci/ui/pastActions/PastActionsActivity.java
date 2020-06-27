package ar.edu.itba.hci.ui.pastActions;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ar.edu.itba.hci.R;

public class PastActionsActivity extends AppCompatActivity {

    private PastActionsViewModel pastActionsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.past_actions);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        RecyclerView rv = findViewById(R.id.past_actions_rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        rv.setLayoutManager(gridLayoutManager);


        pastActionsViewModel = new ViewModelProvider(this).get(PastActionsViewModel.class);
        pastActionsViewModel.getActions().observe(this, actionList -> pastActionsViewModel.getDevices().observe(this, devices -> {
            final RecyclerViewPastActionsAdapter adapter = new RecyclerViewPastActionsAdapter(this, actionList, devices);
            rv.setAdapter(adapter);
        }));
    }
}