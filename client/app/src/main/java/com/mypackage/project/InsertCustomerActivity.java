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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class InsertCustomerActivity extends AppCompatActivity {
    private TextView toastMessage;
    private Toast toast;
    private ProgressBar progressBar;
    private int h, w;
    private Helper helper;
    private String[] parts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertcustomer);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#0193D7"));
        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Helper.hideSoftKeyboard(InsertCustomerActivity.this);
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
        TextView firstLabel = new TextView(this);
        final EditText first = new EditText(this);
        TextView lastLabel = new TextView(this);
        final EditText last = new EditText(this);
        TextView emailLabel = new TextView(this);
        final EditText email = new EditText(this);
        TextView numLabel = new TextView(this);
        final EditText num = new EditText(this);
        TextView num2Label = new TextView(this);
        final EditText num2 = new EditText(this);
        TextView addressLabel = new TextView(this);
        final EditText address = new EditText(this);
        TextView birthdateLabel = new TextView(this);
        final EditText birthdate = new EditText(this);
        Button submit = new Button(this);
        rl.addView(firstLabel);
        rl.addView(first);
        rl.addView(lastLabel);
        rl.addView(last);
        rl.addView(emailLabel);
        rl.addView(email);
        rl.addView(numLabel);
        rl.addView(num);
        rl.addView(num2Label);
        rl.addView(num2);
        rl.addView(addressLabel);
        rl.addView(address);
        rl.addView(birthdateLabel);
        rl.addView(birthdate);
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
        relativeParams = (RelativeLayout.LayoutParams) firstLabel.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 2 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        relativeParams.height = h * 10 / 100;
        firstLabel.setLayoutParams(relativeParams);
        firstLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
        firstLabel.setText("First Name:");

        relativeParams = (RelativeLayout.LayoutParams) first.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 5 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        first.setLayoutParams(relativeParams);
        first.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

        relativeParams = (RelativeLayout.LayoutParams) lastLabel.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 15 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        relativeParams.height = h * 10 / 100;
        lastLabel.setLayoutParams(relativeParams);
        lastLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
        lastLabel.setText("Last Name:");

        relativeParams = (RelativeLayout.LayoutParams) last.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 18 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        last.setLayoutParams(relativeParams);
        last.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

        relativeParams = (RelativeLayout.LayoutParams) emailLabel.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 28 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        relativeParams.height = h * 10 / 100;
        emailLabel.setLayoutParams(relativeParams);
        emailLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
        emailLabel.setText("Email:");

        relativeParams = (RelativeLayout.LayoutParams) email.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 31 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        email.setLayoutParams(relativeParams);
        email.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

        relativeParams = (RelativeLayout.LayoutParams) numLabel.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 41 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        relativeParams.height = h * 10 / 100;
        numLabel.setLayoutParams(relativeParams);
        numLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
        numLabel.setText("Contact Number:");

        relativeParams = (RelativeLayout.LayoutParams) num.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 44 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        num.setLayoutParams(relativeParams);
        num.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

        relativeParams = (RelativeLayout.LayoutParams) num2Label.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 54 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        relativeParams.height = h * 10 / 100;
        num2Label.setLayoutParams(relativeParams);
        num2Label.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
        num2Label.setText("Contact Number 2:");

        relativeParams = (RelativeLayout.LayoutParams) num2.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 57 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        num2.setLayoutParams(relativeParams);
        num2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

        relativeParams = (RelativeLayout.LayoutParams) addressLabel.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 67 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        relativeParams.height = h * 10 / 100;
        addressLabel.setLayoutParams(relativeParams);
        addressLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
        addressLabel.setText("Address Name:");

        relativeParams = (RelativeLayout.LayoutParams) address.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 70 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        address.setLayoutParams(relativeParams);
        address.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

        relativeParams = (RelativeLayout.LayoutParams) birthdateLabel.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 80 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        relativeParams.height = h * 10 / 100;
        birthdateLabel.setLayoutParams(relativeParams);
        birthdateLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
        birthdateLabel.setText("Birth Date:");

        relativeParams = (RelativeLayout.LayoutParams) birthdate.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 83 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        birthdate.setLayoutParams(relativeParams);
        birthdate.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

        relativeParams = (RelativeLayout.LayoutParams) submit.getLayoutParams();
        relativeParams.setMargins(w * 37 / 100, h * 89 / 100, 0, 0);
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
                    CustomerModel model = new CustomerModel();
                    model.Cust_First_Name = first.getText().toString();
                    model.Cust_Last_Name = last.getText().toString();
                    model.Cust_Birth_Date = birthdate.getText().toString();
                    model.Cust_Email = email.getText().toString();
                    model.Cust_Contact_Num_2 = num2.getText().toString();
                    model.Cust_Contact_Num = num.getText().toString();
                    model.Cust_Address_Name = address.getText().toString();
                    try {
                        String res = new Helper.Post(rl, parts, "customers", model).execute().get();
                        if (res.contains("null"))
                        {
                            toastMessage.setBackgroundColor(Color.parseColor("#038E18"));
                            toastMessage.setText("Inserted Successfully!");
                            toast.setView(toastMessage);
                            toast.show();
                            first.setText("");
                            last.setText("");
                            birthdate.setText("");
                            email.setText("");
                            num.setText("");
                            num2.setText("");
                            address.setText("");
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

    @Override
    public void onBackPressed() {
        Intent intent2 = new Intent();
        setResult(3, intent2);
        finish();
    }
}

