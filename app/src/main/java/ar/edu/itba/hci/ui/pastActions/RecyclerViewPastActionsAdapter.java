package ar.edu.itba.hci.ui.pastActions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.IconAdapter;
import ar.edu.itba.hci.api.models.PastAction;
import okhttp3.internal.Util;

public class RecyclerViewPastActionsAdapter extends RecyclerView.Adapter<RecyclerViewPastActionsAdapter.PastActionVH> {

    List<PastAction> actionList;
    List<Device> deviceList;
    private Context context;
    private DateFormat dateFormat;

    public RecyclerViewPastActionsAdapter(Context context, List<PastAction> actionList, List<Device> deviceList) {
        this.context = context;
        this.actionList = actionList;
        this.deviceList = deviceList;
        this.dateFormat = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.DEFAULT,context.getResources().getConfiguration().getLocales().get(0));
    }

    @NonNull
    @Override
    public PastActionVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.past_action_list_item, parent, false);
        return new PastActionVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PastActionVH holder, int position) {
        PastAction pastAction = actionList.get(position);
        int actId;
        String devName, actionName;
        String devIcon = null;

        Optional<Device> dev = deviceList.stream().filter(d -> d.getId().equals(pastAction.getDeviceId())).findFirst();
        if(dev.isPresent()){
            devName = dev.get().getName();
            devIcon = dev.get().getMeta().getIcon();
        }
        else devName = "NOT_FOUND";

        actId = context.getResources().getIdentifier(pastAction.getAction(), "string", context.getPackageName());
        if(actId == 0) actionName = pastAction.getAction();
        else actionName = context.getResources().getString(actId);

        holder.device.setText(devName);
        holder.device.setCompoundDrawablesWithIntrinsicBounds(IconAdapter.getIntSmallIcon(devIcon),0,0,0);

        holder.date.setText(String.format("%s: %s", context.getResources().getString(R.string.date), dateFormat.format(pastAction.getTimestamp())));
        holder.action.setText(String.format("%s: %s", context.getResources().getString(R.string.action), actionName));

        if(pastAction.getParams() == null || pastAction.getParams().size() == 0)
            holder.params.setVisibility(View.GONE);
        else {
            holder.params.setVisibility(View.VISIBLE);
            holder.params.setText(String.format("%s: %s", context.getResources().getString(R.string.parameter), pastAction.getParams().get(0)));
        }
    }

    @Override
    public int getItemCount() {
        return actionList.size();
    }

    class PastActionVH extends RecyclerView.ViewHolder{

        private TextView device,date,action,params;


        public PastActionVH(@NonNull View itemView) {
            super(itemView);

            device = itemView.findViewById(R.id.past_action_device);
            date = itemView.findViewById(R.id.past_action_date);
            action = itemView.findViewById(R.id.past_action_action);
            params = itemView.findViewById(R.id.past_action_params);


        }
    }


}
