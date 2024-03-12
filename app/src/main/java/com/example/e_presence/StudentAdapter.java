package com.example.e_presence;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentVieHolder> {
    ArrayList<StudentItem> studentItems;
    Context context;
    private OnItemClickListner onItemClickListner;

    public void setOnItemClickListner(OnItemClickListner onItemClickListner) {
        this.onItemClickListner=onItemClickListner;
    }


    public interface OnItemClickListner{
        void onClick(int position);
    }

    public StudentAdapter(Context context, ArrayList<StudentItem> studentItems) {
        this.studentItems = studentItems;
        this.context=context;
    }
    public static class StudentVieHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        TextView roll;
        TextView name;
        TextView status;
        CardView cardView;
        public StudentVieHolder(@NonNull View itemView, OnItemClickListner onItemClickListner) {

            super(itemView);
            roll=itemView.findViewById(R.id.roll);
            name=itemView.findViewById(R.id.name);
            status=itemView.findViewById(R.id.status);
            cardView=itemView.findViewById(R.id.cardView);
            itemView.setOnClickListener(v -> onItemClickListner.onClick(getAdapterPosition()));
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(),0,0,"EDIT");
            menu.add(getAdapterPosition(),1,0,"DELETE");
        }

    }
    @NonNull
    @Override
    public StudentVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item,parent,false);
        return new StudentVieHolder(itemView,onItemClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentVieHolder holder, int position) {
        holder.roll.setText(studentItems.get(position).getRoll()+"");
        holder.name.setText(studentItems.get(position).getName());
        holder.status.setText(studentItems.get(position).getStatus());
        holder.cardView.setCardBackgroundColor(getColor(position));
    }
    private int getColor(int position){
        String status=studentItems.get(position).getStatus();
        if (status.equals("P")){
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.prsent)));
        }
        else if (status.equals("A")){
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.absent)));

        }
        else {
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.normal)));
        }
    }


    @Override
    public int getItemCount() {

        return studentItems.size();
    }
}