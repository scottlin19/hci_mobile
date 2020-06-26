package ar.edu.itba.hci.ui.pastActions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Optional;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.PastAction;

public class RecyclerViewPastActionsAdapter extends RecyclerView.Adapter<RecyclerViewPastActionsAdapter.PastActionVH> {

    List<PastAction> actionList;
    List<Device> deviceList;
    private Context context;

    public RecyclerViewPastActionsAdapter(Context context,List<PastAction> actionList, List<Device> deviceList) {
        this.context = context;
        this.actionList = actionList;
        this.deviceList = deviceList;
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
        holder.actionItem.setText(actionAdapter(pastAction));
    }

    @Override
    public int getItemCount() {
        return actionList.size();
    }

    class PastActionVH extends RecyclerView.ViewHolder{

        private TextView actionItem;

        public PastActionVH(@NonNull View itemView) {
            super(itemView);

            actionItem = itemView.findViewById(R.id.past_action_item);
        }
    }

    private String actionAdapter(PastAction pastAction) {
        StringBuilder str = new StringBuilder();
        int actId;
        String devName, actionName, param;

        Optional<Device> dev = deviceList.stream().filter(d -> d.getId().equals(pastAction.getDeviceId())).findFirst();
        if(dev.isPresent()) devName = dev.get().getName();
        else devName = "NOT_FOUND";

        actId = context.getResources().getIdentifier(pastAction.getAction(), "string", context.getPackageName());
        if(actId == 0) actionName = pastAction.getAction();
        else actionName = context.getResources().getString(actId);

        if(pastAction.getParams() == null || pastAction.getParams().length == 0) param = "--";
        else param = pastAction.getParams()[0];

        str.append(context.getResources().getString(R.string.device))
                .append(": ")
                .append(devName)
                .append(" - ")
                .append(context.getResources().getString(R.string.action))
                .append(": ")
                .append(actionName)
                .append(" - ")
                .append("Params: ")
                .append(param)
                .append(" - ")
                .append(context.getResources().getString(R.string.date))
                .append(": ")
                .append(pastAction.getTimestamp());
        return str.toString();
    }
}
