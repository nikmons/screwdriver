package com.mypackage.project;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewHelpDeskAdapter extends RecyclerView.Adapter<RecyclerViewHelpDeskAdapter.MyViewHolder> {

    private List<CustomerModel> modelList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView firstLastName, email;

        public MyViewHolder(View view) {
            super(view);
            firstLastName = (TextView) view.findViewById(R.id.txt1);
            email = (TextView) view.findViewById(R.id.txt2);
        }
    }


    public RecyclerViewHelpDeskAdapter(List<CustomerModel> modelList) {
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
        CustomerModel model = modelList.get(position);
        holder.firstLastName.setText(model.Cust_First_Name + " " + model.Cust_Last_Name);
        holder.email.setText(model.Cust_Email);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
