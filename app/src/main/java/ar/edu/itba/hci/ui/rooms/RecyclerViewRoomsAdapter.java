package ar.edu.itba.hci.ui.rooms;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.models.Room;

public class RecyclerViewRoomsAdapter extends RecyclerView.Adapter<RecyclerViewRoomsAdapter.ViewHolder>{

    private Context context;
    private List<Room> rooms;
    private LayoutInflater inflater;
    private Map<String,Integer> iconsMap = new HashMap<String,Integer>(){{
        put("image",R.drawable.ic_baseline_image_24);
        put("weekend",R.drawable.ic_baseline_weekend_24);
        put("mdi-chef-hat",R.drawable.chef_hat_white);
        put("mdi-car",R.drawable.ic_baseline_directions_car_24);
        put("restaurant",R.drawable.ic_baseline_restaurant_24);
        put("mdi-shower",R.drawable.shower);
        put("mdi-baby-bottle-outline",R.drawable.baby_bottle_outline);
        put("mdi-bed",R.drawable.ic_baseline_hotel_24);
        put("mdi-flower",R.drawable.ic_baseline_local_florist_24);
        }};

    public RecyclerViewRoomsAdapter(Context context, List<Room> rooms){
        this.context = context;
        this.rooms = rooms;
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
        holder.roomName.setText(rooms.get(position).getName());
        holder.roomName.setCompoundDrawablesWithIntrinsicBounds(0,0,0,iconsMap.get(rooms.get(position).getMeta().getIcon()));
        holder.cv.setOnClickListener(view -> {
            Intent intent = new Intent(context, RoomScreen.class);
            intent.putExtra("room",rooms.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView roomName;
        CardView cv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            roomName = itemView.findViewById(R.id.room_name_id);
            cv = itemView.findViewById(R.id.room_card_id);
        }
    }
}
