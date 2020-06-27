package ar.edu.itba.hci.ui.routines;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Routine;
import ar.edu.itba.hci.api.models.RoutineAction;
import ar.edu.itba.hci.api.notifications.SharedPreferencesHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecyclerViewRoutinesAdapter extends RecyclerView.Adapter<RecyclerViewRoutinesAdapter.RoutineVH> {

    List<Routine> routineList;
    private Context context;
    public RecyclerViewRoutinesAdapter(Context context,List<Routine> routineList) {
        this.context = context;
        this.routineList = routineList;
    }

    @NonNull
    @Override
    public RoutineVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.routine_card,parent,false);
        return new RoutineVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineVH holder, int position) {
        Routine routine = routineList.get(position);
        holder.routineName.setText(routine.getName());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.routine_action_list_item, routineActionsAdapter(routine.getActions()));
        holder.routineActionList.setAdapter(arrayAdapter);

        setListViewHeightBasedOnChildren(holder.routineActionList);

        holder.startAction.setOnClickListener( v -> executeRoutine(routine));
        boolean isExpanded = routine.isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return routineList.size();
    }


    class RoutineVH extends RecyclerView.ViewHolder{

        private TextView routineName;
        private ConstraintLayout expandableLayout;
        private ListView routineActionList;
        private Button startAction;


        public RoutineVH(@NonNull View itemView) {
            super(itemView);

            routineName = itemView.findViewById(R.id.routineName);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            routineActionList = itemView.findViewById(R.id.action_list);
            startAction = itemView.findViewById(R.id.start_action_button);

            routineName.setOnClickListener(v -> {
                Routine routine = routineList.get(getAdapterPosition());
                routine.setExpanded(!routine.isExpanded());
                notifyItemChanged(getAdapterPosition());
          });
        }
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter mAdapter = listView.getAdapter();
        int totalHeight = 0;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);
            mView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += mView.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @SuppressWarnings("NullableProblems")
    public void executeRoutine(Routine routine) {
        ApiClient.getInstance().executeRoutine(routine.getId(), new Callback<Result<Object>>() {
            @Override
            public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                Toast.makeText(context, String.format("%s %s", routine.getName(), context.getResources().getString(R.string.exec_msg)), Toast.LENGTH_SHORT).show();
                SharedPreferencesHelper.refreshSavedPreferences(context.getApplicationContext());
                Log.v("Routine execution", String.format("%s %s", routine.getName(), context.getResources().getString(R.string.exec_msg)));
            }
            @Override
            public void onFailure(Call<Result<Object>> call, Throwable t) {
                Toast.makeText(context, String.format("Error %s", t.getLocalizedMessage()), Toast.LENGTH_SHORT).show();
                Log.e("Routine execution", t.getLocalizedMessage());
          }
        });
    }

    public String[] routineActionsAdapter(@NonNull List<RoutineAction> actions) {
        String[] parsedActions = new String[actions.size()];
        StringBuilder actionStr = new StringBuilder();
        int id, idx = 0;
        List<String> params;

        for(RoutineAction routineAction : actions) {
            id = context.getResources().getIdentifier(routineAction.getActionName(), "string", context.getPackageName());
            if(id == 0) actionStr.append("MISSING_ACTION");
            else actionStr.append(context.getResources().getString(id));

            actionStr.append(" ").append(routineAction.getDevice().getName());

            if((params = routineAction.getParams()).size() != 0) {
                actionStr.append(" ").append(context.getResources().getString(R.string.action_msg)).append(" ").append(params.get(0));
            }

            parsedActions[idx++] = actionStr.toString();
            actionStr.setLength(0);
        }

        return parsedActions;
    }
}