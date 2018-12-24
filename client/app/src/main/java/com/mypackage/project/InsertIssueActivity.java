package com.mypackage.project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class InsertIssueActivity extends AppCompatActivity {
    private TextView toastMessage;
    private Toast toast;
    private ProgressBar progressBar;
    private int h, w;
    private Helper helper;
    private String[] parts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertissue);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#0193D7"));
        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Helper.hideSoftKeyboard(InsertIssueActivity.this);
                return false;
            }
        });
        helper = new Helper();
        parts = helper.getPrefs(this);
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        Display display = getWindowManager().getDefaultDisplay();
        w = display.getWidth();
        h = display.getHeight();
        Intent myIntent = getIntent();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getLayoutParams().height = h * 95 / 100;
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.colorPrimaryDark),
                PorterDuff.Mode.SRC_IN);
        Gson gson = new Gson();
        String json = null;
        CustomerModel[] customersModel = {};
        DeviceModel[] devModel = {};
        ProblemModel[] problemsModel = {};
        try {
            json = new Helper.Get(rl, parts, "customers").execute().get();
            customersModel = gson.fromJson(json, CustomerModel[].class);
            json = new Helper.Get(rl, parts, "devices").execute().get();
            devModel = gson.fromJson(json, DeviceModel[].class);
            json = new Helper.Get(rl, parts, "problems").execute().get();
            problemsModel = gson.fromJson(json, ProblemModel[].class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        List<String> customers = new ArrayList<String>();
        final List<Integer> customersIds = new ArrayList<Integer>();
        List<String> devices = new ArrayList<String>();
        final List<Integer> devicesIds = new ArrayList<Integer>();
        List<String> problems = new ArrayList<String>();
        final List<Integer> problemsIds = new ArrayList<Integer>();
        for (CustomerModel item : customersModel) {
            customers.add(item.Cust_First_Name + " " + item.Cust_Last_Name);
            customersIds.add(item.Cust_id);
        }
        for (DeviceModel item : devModel) {
            devices.add(item.Dev_Manufacturer + " " + item.Dev_Model);
            devicesIds.add(item.Dev_id);
        }
        for (ProblemModel item : problemsModel) {
            problems.add(item.Prob_Name);
            problemsIds.add(item.Prob_id);
        }
        TextView customerLabel = new TextView(this);
        final Spinner customer = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        customers);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        customer.setAdapter(spinnerArrayAdapter);
        TextView deviceLabel = new TextView(this);
        final Spinner device = new Spinner(this);
        spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        devices);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        device.setAdapter(spinnerArrayAdapter);
        TextView problemLabel = new TextView(this);
        final Spinner problem = new Spinner(this);
        spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        problems);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        problem.setAdapter(spinnerArrayAdapter);
        Button submit = new Button(this);
        rl.addView(customerLabel);
        rl.addView(customer);
        rl.addView(device);
        rl.addView(deviceLabel);
        rl.addView(problem);
        rl.addView(problemLabel);
        rl.addView(submit);
        RelativeLayout.LayoutParams relativeParams;
        toastMessage = new TextView(this);
        toastMessage.setTextColor(Color.WHITE);
        toastMessage.setTypeface(null, Typeface.BOLD);
        toastMessage.setPadding(w * 5 / 100, h * 2 / 100, w * 5 / 100, h * 2 / 100);
        toastMessage.setTextSize((float) (w * 2 / 100));
        toast = Toast.makeText(getApplicationContext(), null,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, h * 3 / 100);
        relativeParams = (RelativeLayout.LayoutParams) customerLabel.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h *  18/ 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        relativeParams.height = h * 10 / 100;
        customerLabel.setLayoutParams(relativeParams);
        customerLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
        customerLabel.setText("Customer:");

        relativeParams = (RelativeLayout.LayoutParams) customer.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 23 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        customer.setLayoutParams(relativeParams);
        customer.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

        relativeParams = (RelativeLayout.LayoutParams) deviceLabel.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 33 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        relativeParams.height = h * 10 / 100;
        deviceLabel.setLayoutParams(relativeParams);
        deviceLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
        deviceLabel.setText("Device:");

        relativeParams = (RelativeLayout.LayoutParams) device.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 38 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        device.setLayoutParams(relativeParams);
        device.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);


        relativeParams = (RelativeLayout.LayoutParams) problemLabel.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 48 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        relativeParams.height = h * 10 / 100;
        problemLabel.setLayoutParams(relativeParams);
        problemLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
        problemLabel.setText("Problem:");

        relativeParams = (RelativeLayout.LayoutParams) problem.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 53 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        problem.setLayoutParams(relativeParams);
        problem.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

        relativeParams = (RelativeLayout.LayoutParams) submit.getLayoutParams();
        relativeParams.setMargins(w * 37 / 100, h * 65 / 100, 0, 0);
        relativeParams.width = w * 28 / 100;
        relativeParams.height = h * 7 / 100;
        submit.setLayoutParams(relativeParams);
        submit.setText("submit");
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (getCurrentFocus() != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                if (!Helper.isOnline(getApplicationContext())) {
                    toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                    toastMessage.setText("No internet connection!");
                    toast.setView(toastMessage);
                    toast.show();
                } else {
                    IssueModel model = new IssueModel();
                    model.Cust_id = customersIds.get(customer.getSelectedItemPosition());
                    model.Dev_id = devicesIds.get(device.getSelectedItemPosition());
                    model.Prob_id = problemsIds.get(problem.getSelectedItemPosition());
                    try {
                        String res = new Helper.Post(rl, parts, "issues", model).execute().get();
                        if (res.contains("null"))
                        {
                            toastMessage.setBackgroundColor(Color.parseColor("#038E18"));
                            toastMessage.setText("Inserted Successfully!");
                            toast.setView(toastMessage);
                            toast.show();

                        }
                        else
                        {
                            toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                            toastMessage.setText(res);
                            toast.setView(toastMessage);
                            toast.show();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}

