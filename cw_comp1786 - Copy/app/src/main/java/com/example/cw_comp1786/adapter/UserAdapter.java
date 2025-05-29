package com.example.cw_comp1786.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cw_comp1786.R;
import com.example.cw_comp1786.db.entity.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private OnItemClickListener listener;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }


    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
            User user = userList.get(position);

            holder.username.setText(user.getName());
            holder.role.setText("Role: " + user.getRole());
            holder.email.setText("Email: " +user.getEmail());

            holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(user, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView username, role, email;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tv_class_session_title);
            role = itemView.findViewById(R.id.tv_user_role);
            email = itemView.findViewById(R.id.tv_class_time);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(User user, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
