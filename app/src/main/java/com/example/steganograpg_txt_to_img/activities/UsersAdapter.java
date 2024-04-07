package com.example.steganograpg_txt_to_img.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.steganograpg_txt_to_img.Models.User;
import com.example.steganograpg_txt_to_img.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.GroupViewHolder> {

    private Context context;
    private ArrayList<User> filteredUsers;
    private ArrayList<User> allUsersList;


    private UserCallback userCallback;


    public UsersAdapter(Context context) {
        this.filteredUsers = new ArrayList<>();
        this.allUsersList = new ArrayList<>();
        this.context = context;
    }

    public UsersAdapter setUserCallback(UserCallback userCallback) {
        this.userCallback = userCallback;
        return this;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        GroupViewHolder groupViewHolder = new GroupViewHolder(view);
        return groupViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        User user = getItem(position);
        holder.user_TV_userName.setText(user.getName());
    }

    private User getItem(int position) {
        return filteredUsers.get(position);
    }

    @Override
    public int getItemCount() {
        return filteredUsers == null ? 0 : filteredUsers.size();
    }

    public void updateGroups(ArrayList<User> users) {
        this.filteredUsers = users;
        this.allUsersList = users;
        notifyDataSetChanged();
    }


    public ArrayList<User> getAllUsersList() {
        return allUsersList;
    }

    private void filterList(ArrayList<User> filterList) {
        this.filteredUsers = filterList;
        notifyDataSetChanged();
    }


    public void filter(String text) {
        ArrayList<User> filteredList = new ArrayList<>();
        for (User user : allUsersList) {
            if (user.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(user);
            }
        }
        filterList(filteredList);
    }


    public class GroupViewHolder extends RecyclerView.ViewHolder {
        private MaterialTextView user_TV_userName;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews();
            itemView.setOnClickListener(view -> userCallback.itemClicked(getItem(getAdapterPosition()), getAdapterPosition()));
        }

        private void initViews() {
            user_TV_userName = itemView.findViewById(R.id.user_TV_userName);
        }
    }
}
