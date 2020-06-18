package ar.edu.itba.hci.ui.rooms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ar.edu.itba.hci.R;

public class RecyclerViewRoomsAdapter extends RecyclerView.Adapter<RecyclerViewRoomsAdapter.ViewHolder>{

    private List<String> roomNames;
    private List<Integer> roomIcons;
    private LayoutInflater inflater;

    public RecyclerViewRoomsAdapter(Context context, List<String> roomNames, List<Integer> roomIcons){
        this.roomNames = roomNames;
        this.roomIcons = roomIcons;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerViewRoomsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.room_card_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewRoomsAdapter.ViewHolder holder, int position) {
        holder.roomName.setText(roomNames.get(position));
        holder.roomIcon.setImageResource(roomIcons.get(position));
    }

    @Override
    public int getItemCount() {
        return roomNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView roomName;
        ImageView roomIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            roomName = itemView.findViewById(R.id.room_name_id);
            roomIcon = itemView.findViewById(R.id.room_icon_id);
        }
    }
}
