package com.mypackage.project;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewCourierAdapter extends RecyclerView.Adapter<RecyclerViewCourierAdapter.MyViewHolder> {

    private List<DevicesToRepairModel> devicesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView trackingNumber, name_surname, address;

        public MyViewHolder(View view) {
            super(view);
            trackingNumber = (TextView) view.findViewById(R.id.txt1);
            address = (TextView) view.findViewById(R.id.txt2);
            name_surname = (TextView) view.findViewById(R.id.txt3);
        }
    }


    public RecyclerViewCourierAdapter(List<DevicesToRepairModel> devicesList) {
        this.devicesList = devicesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.devices_to_repair_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DevicesToRepairModel devicesToRepairModel = devicesList.get(position);
        holder.trackingNumber.setText(devicesToRepairModel.trackingNumber);
        holder.name_surname.setText(devicesToRepairModel.name_surname);
        holder.address.setText(devicesToRepairModel.address);
    }

    @Override
    public int getItemCount() {
        return devicesList.size();
    }
}
