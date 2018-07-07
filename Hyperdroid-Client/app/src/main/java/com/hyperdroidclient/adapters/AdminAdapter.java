package com.hyperdroidclient.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hyperdroidclient.R;
import com.hyperdroidclient.data.local.remote.User;
import com.hyperdroidclient.widgets.BaseTextView;

import java.util.ArrayList;

/**
 * Created by archish on 8/3/18.
 */

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.AdminHolder> {

    ArrayList<User> data;
    OnHolderClickListener commander;

    public AdminAdapter(ArrayList<User> data, OnHolderClickListener commander) {
        this.data = data;
        this.commander = commander;
    }

    @Override
    public AdminHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_admin, parent, false);
        return new AdminHolder(v);
    }

    @Override
    public void onBindViewHolder(AdminHolder holder, int position) {
        holder.tvName.setText(data.get(position).getName());
        holder.tvMachine.setText(data.get(position).getMachine());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnHolderClickListener {
        void onHolderClicked(User data);
    }

    class AdminHolder extends RecyclerView.ViewHolder {
        LinearLayout llItem;
        BaseTextView tvName, tvMachine;

        public AdminHolder(View itemView) {
            super(itemView);
            tvName = (BaseTextView) itemView.findViewById(R.id.tvName);
            tvMachine = (BaseTextView) itemView.findViewById(R.id.tvMachine);
            llItem = (LinearLayout) itemView.findViewById(R.id.llItem);
            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (commander != null)
                        commander.onHolderClicked(data.get(getAdapterPosition()));
                }
            });

        }
    }
}
