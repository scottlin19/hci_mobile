package ar.edu.itba.hci.ui.devices.category;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.ui.Utility;
import ar.edu.itba.hci.ui.devices.DeviceListFragment;

public class RecyclerViewCategoryAdapter extends RecyclerView.Adapter<RecyclerViewCategoryAdapter.MyViewHolder> {

    private Context context;
    private List<Category> categoryData;
    private List<Device> deviceList;
    private Fragment currFragment;

    public RecyclerViewCategoryAdapter(Context context, List<Category> categoryData, List<Device> deviceList, Fragment currFragment) {
        this.context = context;
        this.categoryData = categoryData;
        this.deviceList = deviceList;

        this.currFragment = currFragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.category_card_view,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        String categ_name;

        int id = context.getResources().getIdentifier(categoryData.get(position).getName(), "string", context.getPackageName());
        if(id == 0) categ_name = "MISSING NAME";
        else categ_name = context.getResources().getString(id);

        holder.tv_category_name.setText(Utility.capitalizeFirstLetter(categ_name));
        holder.tv_category_name.setCompoundDrawablesWithIntrinsicBounds(0,0,0,categoryData.get(position).getThumbnail());
        holder.cv.setOnClickListener(view -> {
        Bundle bundle = new Bundle();
        this.deviceList = this.deviceList.stream().filter(d ->   d.getType().getName().equals(categoryData.get(position).getName())).collect(Collectors.toList());
           bundle.putParcelableArrayList("devices", (ArrayList<? extends Parcelable>) deviceList);
            System.out.println(deviceList);
           bundle.putString("category",categ_name);
            DeviceListFragment newFragment = new DeviceListFragment();
            newFragment.setArguments(bundle);
           currFragment.getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,newFragment).commit();
        });

    }



    @Override
    public int getItemCount() {
        return categoryData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_category_name;
        CardView cv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_category_name = (TextView) itemView.findViewById(R.id.categ_name);
           cv = (CardView) itemView.findViewById(R.id.cardview_category);

        }
    }
}
