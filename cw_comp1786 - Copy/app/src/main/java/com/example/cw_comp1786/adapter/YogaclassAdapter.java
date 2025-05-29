package com.example.cw_comp1786.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cw_comp1786.R;
import com.example.cw_comp1786.db.entity.Yogaclass;

import java.util.List;

public class YogaclassAdapter extends RecyclerView.Adapter<YogaclassAdapter.YogaClassViewHolder> {

    private List<Yogaclass> yogaClassList;
    private OnItemClickListener listener;

    public YogaclassAdapter(List<Yogaclass> yogaClassList) {
        this.yogaClassList = yogaClassList;
    }

    @NonNull
    @Override
    public YogaClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_yoga_class, parent, false);
        return new YogaClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YogaClassViewHolder holder, int position) {

        Yogaclass yogaclass = yogaClassList.get(position);

        holder.classTitle.setText(yogaclass.getClasstitle());
        holder.classTime.setText(yogaclass.getTime());
        holder.classPrice.setText("Price: $" + yogaclass.getPrice());
        holder.classCapacity.setText("Capacity: " + yogaclass.getCapacity());
        holder.classDay.setText(yogaclass.getDay());
        holder.classType.setText(yogaclass.getClasstype());
        holder.classDuration.setText(yogaclass.getDuration() + " mins");

        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(yogaclass, position);
            }
        });
        holder.updateButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUpdateClick(yogaclass, position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return yogaClassList.size();
    }

    public void updateYogaClassList(List<Yogaclass> newYogaClassList) {
        this.yogaClassList = newYogaClassList;
        notifyDataSetChanged();
    }


    public static class YogaClassViewHolder extends RecyclerView.ViewHolder {

        TextView classTitle, classTime, classPrice, classCapacity, classDay, classType, classDuration;
        TextView updateButton, deleteButton;

        public YogaClassViewHolder(@NonNull View itemView) {
            super(itemView);
            classTitle = itemView.findViewById(R.id.tv_class_session_title);
            classTime = itemView.findViewById(R.id.tv_class_time);
            classPrice = itemView.findViewById(R.id.tv_class_price);
            classCapacity = itemView.findViewById(R.id.tv_class_capacity);
            classDay = itemView.findViewById(R.id.tv_class_dayofweek);
            classType = itemView.findViewById(R.id.tv_class_type);
            classDuration = itemView.findViewById(R.id.tv_class_duration);
            updateButton = itemView.findViewById(R.id.Update);
            deleteButton = itemView.findViewById(R.id.Delete);
        }
    }


    // Interface to handle update and delete button clicks
    public interface OnItemClickListener {
        void onUpdateClick(Yogaclass yogaClass, int position);
        void onDeleteClick(Yogaclass yogaClass, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
