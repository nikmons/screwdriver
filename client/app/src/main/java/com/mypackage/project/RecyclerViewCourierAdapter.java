package com.mypackage.project;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewCourierAdapter extends RecyclerView.Adapter<RecyclerViewCourierAdapter.MyViewHolder> {

    private List<CourierModel> modelList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView trackingNumber, name_surname, address;

        public MyViewHolder(View view) {
            super(view);
            trackingNumber = (TextView) view.findViewById(R.id.txt1);
            address = (TextView) view.findViewById(R.id.txt2);
            name_surname = (TextView) view.findViewById(R.id.txt3);
        }
    }


    public RecyclerViewCourierAdapter(List<CourierModel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerviewstyle, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CourierModel model = modelList.get(position);
        holder.trackingNumber.setText(model.trackingNumber);
        holder.name_surname.setText(model.nameSurname);
        holder.address.setText(model.address);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
