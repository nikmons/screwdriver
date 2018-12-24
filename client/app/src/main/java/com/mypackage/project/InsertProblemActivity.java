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

public class InsertProblemActivity extends AppCompatActivity {
    private TextView toastMessage;
    private Toast toast;
    private ProgressBar progressBar;
    private int h, w;
    private Helper helper;
    private String[] parts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertproblem);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#0193D7"));
        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Helper.hideSoftKeyboard(InsertProblemActivity.this);
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
        TextView nameLabel = new TextView(this);
        final EditText name = new EditText(this);
        TextView descrLabel = new TextView(this);
        final EditText descr = new EditText(this);
        Button submit = new Button(this);
        rl.addView(nameLabel);
        rl.addView(name);
        rl.addView(descr);
        rl.addView(descrLabel);
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
        relativeParams = (RelativeLayout.LayoutParams) nameLabel.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 25 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        relativeParams.height = h * 10 / 100;
        nameLabel.setLayoutParams(relativeParams);
        nameLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
        nameLabel.setText("Name:");

        relativeParams = (RelativeLayout.LayoutParams) name.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 28 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        name.setLayoutParams(relativeParams);
        name.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

        relativeParams = (RelativeLayout.LayoutParams) descrLabel.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 40 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        relativeParams.height = h * 10 / 100;
        descrLabel.setLayoutParams(relativeParams);
        descrLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
        descrLabel.setText("Description:");

        relativeParams = (RelativeLayout.LayoutParams) descr.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 43 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        descr.setLayoutParams(relativeParams);
        descr.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);



        relativeParams = (RelativeLayout.LayoutParams) submit.getLayoutParams();
        relativeParams.setMargins(w * 37 / 100, h * 55 / 100, 0, 0);
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
                    ProblemModel model = new ProblemModel();
                    model.Prob_Name = name.getText().toString();
                    model.Prob_Desc = descr.getText().toString();
                    try {
                        String res = new Helper.Post(rl, parts, "problems", model).execute().get();
                        if (res.contains("null"))
                        {
                            toastMessage.setBackgroundColor(Color.parseColor("#038E18"));
                            toastMessage.setText("Inserted Successfully!");
                            toast.setView(toastMessage);
                            toast.show();
                            name.setText("");
                            descr.setText("");
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

