package com.example.cw_comp1786.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cw_comp1786.R;
//import com.example.cw_comp1786.db.entity.ClassSession;
import com.example.cw_comp1786.db.entity.ClassInstanceDisplay;
import com.example.cw_comp1786.db.entity.User;

import java.util.List;

public class ClassinstanceAdapter extends RecyclerView.Adapter<ClassinstanceAdapter.SessionViewHolder> {
    private List<ClassInstanceDisplay> sessionList;
    private OnItemClickListener listener;

    public ClassinstanceAdapter(List<ClassInstanceDisplay> sessionList) {
        this.sessionList = sessionList;
    }

    @NonNull
    @Override
    public SessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_session, parent, false);
        return new SessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassinstanceAdapter.SessionViewHolder holder, int position) {
        ClassInstanceDisplay session = sessionList.get(position);
        holder.title.setText(session.getClassTitle());
        holder.day.setText("Day: " + session.getSessionDate());
        holder.time.setText(session.getTime());
        holder.teacher.setText("Teacher: " + session.getUserName());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(session.getInstanceId(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }

    public static class SessionViewHolder extends RecyclerView.ViewHolder {
        TextView title, day, time, teacher;

        public SessionViewHolder(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.tv_class_session_title);
            day = view.findViewById(R.id.tv_class_session_day);
            time = view.findViewById(R.id.tv_class_session_time);
            teacher = view.findViewById(R.id.tv_class_session_teacher);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(int sessionId, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void updateData(List<ClassInstanceDisplay> newSessionList) {
        this.sessionList.clear();
        if (newSessionList != null) {
            this.sessionList.addAll(newSessionList);
        }
        notifyDataSetChanged();
    }
}
