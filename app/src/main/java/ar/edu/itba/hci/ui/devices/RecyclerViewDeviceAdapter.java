package ar.edu.itba.hci.ui.devices;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.IconAdapter;
import ar.edu.itba.hci.ui.rooms.RoomScreen;

public class RecyclerViewDeviceAdapter extends RecyclerView.Adapter<ar.edu.itba.hci.ui.devices.RecyclerViewDeviceAdapter.MyViewHolder> {

    private Context context;
    private List<Device> data;

    public RecyclerViewDeviceAdapter(Context context, List<Device> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerViewDeviceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.device_card_view,parent,false);

        return new RecyclerViewDeviceAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ar.edu.itba.hci.ui.devices.RecyclerViewDeviceAdapter.MyViewHolder holder, final int position) {
        holder.tv_device_name.setText(data.get(position).getName());
       holder.tv_device_name.setCompoundDrawablesWithIntrinsicBounds(0,0,0, IconAdapter.getIntIcon(data.get(position).getMeta().getIcon()));
        holder.cv.setOnClickListener(view -> {
            Intent intent = new Intent(context, DeviceDetailsActivity.class);


            System.out.println("DEVICE BUNDLE: "+data.get(position));
            intent.putExtra("device", data.get(position));
            context.startActivity(intent);
        });

    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_device_name;

        CardView cv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_device_name = itemView.findViewById(R.id.tv_device_name);

            cv = itemView.findViewById(R.id.cardview_device);

        }
    }
}