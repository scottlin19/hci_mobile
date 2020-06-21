package ar.edu.itba.hci.ui.rooms;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.DeviceType;
import ar.edu.itba.hci.api.models.IconAdapter;
import ar.edu.itba.hci.api.models.Room;
import ar.edu.itba.hci.ui.devices.category.Category;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewRoomsAdapter extends RecyclerView.Adapter<RecyclerViewRoomsAdapter.ViewHolder>{

    private Context context;
    private List<Room> rooms;
    private LayoutInflater inflater;
    private List<Device> deviceList;

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
        holder.roomName.setCompoundDrawablesWithIntrinsicBounds(0,0,0,IconAdapter.getIntIcon(rooms.get(position).getMeta().getIcon()));
        holder.cv.setOnClickListener(view -> {

            ApiClient.getInstance().getRoomDevices(rooms.get(position).getId(),new Callback<Result<List<Device>>>() {
                @Override
                public void onResponse(Call<Result<List<Device>>> call, Response<Result<List<Device>>> response) {
                    if (response.isSuccessful()) {
                         deviceList = response.body().getResult();
                        Intent intent = new Intent(context, RoomScreen.class);
                        intent.putExtra("room", rooms.get(position));
                        intent.putParcelableArrayListExtra("devices", (ArrayList<? extends Parcelable>) deviceList);
                        context.startActivity(intent);
                    } else {
                        Log.d("ERROR","error");

                    }
                }

                @Override
                public void onFailure(Call<Result<List<Device>>> call, Throwable t) {

                }
            });

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
