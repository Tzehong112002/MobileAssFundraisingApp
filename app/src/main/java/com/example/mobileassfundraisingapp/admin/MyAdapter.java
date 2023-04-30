package com.example.mobileassfundraisingapp.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.mobileassfundraisingapp.EventDetailsUser;
import com.example.mobileassfundraisingapp.R;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<DataClass> dataList;
    public MyAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getDataImage()).into(holder.recImage);
        holder.recTitle.setText(dataList.get(position).getDataTitle());
        holder.recDesc.setText(dataList.get(position).getDataDesc());
        holder.recLang.setText(dataList.get(position).getDataLang());
        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String activityName = view.getContext().getClass().getSimpleName();

                if (activityName.equals("adminActivity")) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("Image", dataList.get(holder.getAdapterPosition()).getDataImage());
                    intent.putExtra("Description", dataList.get(holder.getAdapterPosition()).getDataDesc());
                    intent.putExtra("Title", dataList.get(holder.getAdapterPosition()).getDataTitle());
                    intent.putExtra("Key", dataList.get(holder.getAdapterPosition()).getKey());
                    intent.putExtra("Language", dataList.get(holder.getAdapterPosition()).getDataLang());
                    context.startActivity(intent);

                } else{
                    EventDetailsUser fragment = new EventDetailsUser();
                    Bundle args = new Bundle();
                    args.putString("Image", dataList.get(holder.getAdapterPosition()).getDataImage());
                    args.putString("Description", dataList.get(holder.getAdapterPosition()).getDataDesc());
                    args.putString("Title", dataList.get(holder.getAdapterPosition()).getDataTitle());
                    args.putString("Key", dataList.get(holder.getAdapterPosition()).getKey());
                    args.putString("Language", dataList.get(holder.getAdapterPosition()).getDataLang());
                    fragment.setArguments(args);

                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .commit();

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public void searchDataList(ArrayList<DataClass> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}
class MyViewHolder extends RecyclerView.ViewHolder{
    ImageView recImage;
    TextView recTitle, recDesc, recLang;
    CardView recCard;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        recImage = itemView.findViewById(R.id.recImage);
        recCard = itemView.findViewById(R.id.recCard);
        recDesc = itemView.findViewById(R.id.recDesc);
        recLang = itemView.findViewById(R.id.recPriority);
        recTitle = itemView.findViewById(R.id.recTitle);
    }
}
