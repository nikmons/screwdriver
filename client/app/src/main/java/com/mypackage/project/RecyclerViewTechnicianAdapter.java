package com.mypackage.project;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewTechnicianAdapter extends RecyclerView.Adapter<RecyclerViewTechnicianAdapter.MyViewHolder> {

    private List<DeviceModel> devicesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView device, endDate, problem;

        public MyViewHolder(View view) {
            super(view);
            device = (TextView) view.findViewById(R.id.txt1);
            problem = (TextView) view.findViewById(R.id.txt2);
            endDate = (TextView) view.findViewById(R.id.txt3);
        }
    }


    public RecyclerViewTechnicianAdapter(List<DeviceModel> devicesList) {
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
        DeviceModel deviceModel = devicesList.get(position);
        /*holder.device.setText(deviceModel.device);
        holder.problem.setText(deviceModel.problem);
        holder.endDate.setText(deviceModel.endDate);*/
    }

    @Override
    public int getItemCount() {
        return devicesList.size();
    }
}
