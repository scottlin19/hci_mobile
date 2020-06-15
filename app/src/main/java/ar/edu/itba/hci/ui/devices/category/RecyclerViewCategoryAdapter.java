package ar.edu.itba.hci.ui.devices.category;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.ui.devices.DeviceListActivity;

public class RecyclerViewCategoryAdapter extends RecyclerView.Adapter<RecyclerViewCategoryAdapter.MyViewHolder> {

    private Context context;
    private List<Category> data;

    public RecyclerViewCategoryAdapter(Context context, List<Category> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.cardview_category,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.tv_category_name.setText(data.get(position).getName());
        holder.tv_category_name.setCompoundDrawablesWithIntrinsicBounds(0,data.get(position).getThumbnail(),0,0);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DeviceListActivity.class);
                intent.putExtra("category",data.get(position).getName());
                context.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_category_name;
        CardView cv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_category_name = (TextView) itemView.findViewById(R.id.categ_name);
            cv = (CardView) itemView.findViewById(R.id.cardview_category_id);

        }
    }
}
