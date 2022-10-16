package com.chessboard.tc_chessboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class User_Adapter extends RecyclerView.Adapter<User_Adapter.ViewHolder>{

    private List<User_Model> userList;

    public User_Adapter(List<User_Model> userList){
        this.userList = userList;
    }

    @NonNull
    @Override
    public User_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.registered_users,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull User_Adapter.ViewHolder holder, int position) {
        String userName = userList.get(position).getUserName();

        holder.setData(userName);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView username;
        private ImageButton delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.ru_user1);
            delete   = itemView.findViewById(R.id.ru_delete1);

        }

        public void setData(String userName) {
            delete.setImageResource(R.mipmap.delete);
            username.setText(userName);
        }
    }
}
