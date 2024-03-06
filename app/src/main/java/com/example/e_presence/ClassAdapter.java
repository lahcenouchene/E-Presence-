package com.example.e_presence;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassVieHolder> {
     ArrayList<ClassItems> classItems;
     Context context;
     private OnItemClickListner onItemClickListner;

    public void setOnItemClickListner(OnItemClickListner onItemClickListner) {
        this.onItemClickListner=onItemClickListner;
    }


    public interface OnItemClickListner{
          void onClick(int position);
     }

    public ClassAdapter(Context context, ArrayList<ClassItems> classItems) {
        this.classItems = classItems;
        this.context=context;
    }

    public static class ClassVieHolder extends RecyclerView.ViewHolder{
    TextView className;
    TextView subjectName;
        public ClassVieHolder(@NonNull View itemView,OnItemClickListner onItemClickListner) {

            super(itemView);
            className=itemView.findViewById(R.id.class_id);
            subjectName=itemView.findViewById(R.id.Subject_id);
            itemView.setOnClickListener(v -> onItemClickListner.onClick(getAdapterPosition()));
        }
    }
    @NonNull
    @Override
    public ClassVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item,parent,false);
        return new ClassVieHolder(itemView,onItemClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassAdapter.ClassVieHolder holder, int position) {
        holder.className.setText(classItems.get(position).getClassName());
        holder.subjectName.setText(classItems.get(position).getSubjectName());
    }

    @Override
    public int getItemCount() {

        return classItems.size();
    }
}
