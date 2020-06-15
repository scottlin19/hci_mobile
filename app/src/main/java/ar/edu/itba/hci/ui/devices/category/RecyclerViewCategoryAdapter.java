package ar.edu.itba.hci.ui.devices.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ar.edu.itba.hci.R;

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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_category_name.setText(data.get(position).getName());
        holder.tv_category_name.setCompoundDrawablesWithIntrinsicBounds(0,data.get(position).getThumbnail(),0,0);


    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_category_name;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_category_name = (TextView) itemView.findViewById(R.id.categ_name);

        }
    }
}
