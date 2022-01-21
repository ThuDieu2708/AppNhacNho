package com.example.ungdungnhacnho.DataBase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ungdungnhacnho.Helper.MyHelper;
import com.example.ungdungnhacnho.Model.ModelNhacNho;
import com.example.ungdungnhacnho.R;
import com.example.ungdungnhacnho.View.UpdateActivity;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> implements Filterable {

    Context context;
    Activity activity;
    List<ModelNhacNho> nhacNhoList;
    List<ModelNhacNho> newList;
    public Adapter(Context context, Activity activity, List<ModelNhacNho> nhacNhoList) {
        this.context = context;
        this.activity = activity;
        this.nhacNhoList = nhacNhoList;
        this.newList = new ArrayList<>(nhacNhoList);
    }
    @Override
    public Filter getFilter() {
        return filter;
    }
    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<ModelNhacNho> filterList = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filterList.addAll(newList);
            }else {
                String sFilter = charSequence.toString().toLowerCase().trim();
                for (ModelNhacNho item: newList) {
                    if(item.getTilte().toLowerCase().contains(sFilter)){
                        filterList.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            nhacNhoList.clear();
            nhacNhoList.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.datetime, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(nhacNhoList.get(position).getTilte());
        holder.content.setText(nhacNhoList.get(position).getContent());
        holder.date.setText(nhacNhoList.get(position).getDate());
        holder.time.setText(nhacNhoList.get(position).getTime());
        holder.date_future.setText(nhacNhoList.get(position).getDate_future());
        holder.time_future.setText(nhacNhoList.get(position).getTim_future());
        //holder.cardView.setBackgroundColor(Color.parseColor("#A9D9D4"));
        holder.cardView.setBackgroundResource(R.drawable.btn_background);
        if(!holder.date_future.getText().toString().trim().equals("empty")){
            //holder.cardView.setBackgroundColor(Color.parseColor("#207335"));
            holder.cardView.setBackgroundResource(R.drawable.btn2_background);
        }

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update....
                Intent intent = new Intent(context, UpdateActivity.class);

                intent.putExtra("id", nhacNhoList.get(position).getId());
                intent.putExtra("title", nhacNhoList.get(position).getTilte());
                intent.putExtra("content", nhacNhoList.get(position).getContent());
                intent.putExtra("date", nhacNhoList.get(position).getDate());
                intent.putExtra("time", nhacNhoList.get(position).getTime());
                intent.putExtra("status", nhacNhoList.get(position).getStatus());
                intent.putExtra("date_future", nhacNhoList.get(position).getDate_future());
                intent.putExtra("time_future", nhacNhoList.get(position).getTim_future());

                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nhacNhoList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title, content, date, time, date_future, time_future;
        RelativeLayout relativeLayout;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.textTitle);
            content = (TextView) itemView.findViewById(R.id.txtContent);
            date = (TextView) itemView.findViewById(R.id.txtDate);
            time = (TextView) itemView.findViewById(R.id.txtTime);
            date_future = (TextView) itemView.findViewById(R.id.txtDateFuture);
            time_future = (TextView) itemView.findViewById(R.id.txtTimeFuture);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
            cardView = (CardView) itemView.findViewById(R.id.cardView);


        }
    }
    public List<ModelNhacNho> getList(){
        return nhacNhoList;
    }

    // Xóa item
    public void removeItem(int index){
        nhacNhoList.remove(index);
        notifyItemRemoved(index);
    }
    // Undo item
    public void undoItem(ModelNhacNho mdNhacnho, int index){
        nhacNhoList.add(index, mdNhacnho);
        notifyItemInserted(index);
    }
}
