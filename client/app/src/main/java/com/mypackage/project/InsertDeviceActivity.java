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


import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class InsertDeviceActivity extends AppCompatActivity {
    private TextView toastMessage;
    private Toast toast;
    private ProgressBar progressBar;
    private int h, w, min;
    private Helper helper;
    private String[] parts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertdevice);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#0193D7"));
        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Helper.hideSoftKeyboard(InsertDeviceActivity.this);
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
        min = myIntent.getIntExtra("min", -1);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getLayoutParams().height = h * 95 / 100;
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.colorPrimaryDark),
                android.graphics.PorterDuff.Mode.SRC_IN);
        TextView ident_codeLabel = new TextView(this);
        final EditText ident_code = new EditText(this);
        TextView manufLabel = new TextView(this);
        final EditText manuf = new EditText(this);
        TextView modelLabel = new TextView(this);
        final EditText model = new EditText(this);
        TextView modelYearLabel = new TextView(this);
        final EditText modelyear = new EditText(this);
        Button submit = new Button(this);
        rl.addView(ident_codeLabel);
        rl.addView(ident_code);
        rl.addView(manufLabel);
        rl.addView(manuf);
        rl.addView(modelLabel);
        rl.addView(model);
        rl.addView(modelYearLabel);
        rl.addView(modelyear);
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
        relativeParams = (RelativeLayout.LayoutParams) ident_codeLabel.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 5 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        relativeParams.height = h * 10 / 100;
        ident_codeLabel.setLayoutParams(relativeParams);
        ident_codeLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
        ident_codeLabel.setText("Identifier Code:");

        relativeParams = (RelativeLayout.LayoutParams) ident_code.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 8 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        ident_code.setLayoutParams(relativeParams);
        ident_code.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

        relativeParams = (RelativeLayout.LayoutParams) manufLabel.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 20 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        relativeParams.height = h * 10 / 100;
        manufLabel.setLayoutParams(relativeParams);
        manufLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
        manufLabel.setText("Manufacturer:");

        relativeParams = (RelativeLayout.LayoutParams) manuf.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 23 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        manuf.setLayoutParams(relativeParams);
        manuf.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

        relativeParams = (RelativeLayout.LayoutParams) modelLabel.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 35 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        relativeParams.height = h * 10 / 100;
        modelLabel.setLayoutParams(relativeParams);
        modelLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
        modelLabel.setText("Model:");

        relativeParams = (RelativeLayout.LayoutParams) model.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 38 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        model.setLayoutParams(relativeParams);
        model.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

        relativeParams = (RelativeLayout.LayoutParams) modelYearLabel.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 50 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        relativeParams.height = h * 10 / 100;
        modelYearLabel.setLayoutParams(relativeParams);
        modelYearLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
        modelYearLabel.setText("Model Year:");

        relativeParams = (RelativeLayout.LayoutParams) modelyear.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 53 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        modelyear.setLayoutParams(relativeParams);
        modelyear.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

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
                    DeviceModel devModel = new DeviceModel();
                    devModel.Dev_Identifier_Code = ident_code.getText().toString();
                    devModel.Dev_Manufacturer = manuf.getText().toString();
                    devModel.Dev_Model = model.getText().toString();
                    devModel.Dev_Model_Year = modelyear.getText().toString();
                    try {
                        String res = new Helper.Post(rl, parts, "devices", devModel).execute().get();
                        if (res.contains("null"))
                        {
                            toastMessage.setBackgroundColor(Color.parseColor("#038E18"));
                            toastMessage.setText("Inserted Successfully!");
                            toast.setView(toastMessage);
                            toast.show();
                            ident_code.setText("");
                            manuf.setText("");
                            model.setText("");
                            modelyear.setText("");
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
        intent2.putExtra("min", min);
        setResult(2, intent2);
        finish();
    }
}

