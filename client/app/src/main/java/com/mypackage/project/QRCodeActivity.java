package com.mypackage.project;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class QRCodeActivity extends AppCompatActivity {
    private int width, height;
    private ImageView imageView;
    private String qrCodeStr;
    private int issueId, availActionId;
    private List<Integer> availActionIds = new ArrayList<Integer>();
    private List<String> availActionNames = new ArrayList<String>();
    private String[] parts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode_activity);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#BDB9B9"));
        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Helper.hideSoftKeyboard(QRCodeActivity.this);
                return false;
            }
        });
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        final QRCodeActivity qrCodeActivity = this;
        Intent myIntent = getIntent();
        issueId = myIntent.getIntExtra("issueId", -1);
        final EditText editText = (EditText) findViewById(R.id.editText);
        imageView = (ImageView) findViewById(R.id.qrCode);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        RelativeLayout.LayoutParams relativeParams;
        Helper helper = new Helper();
        parts = helper.getPrefs(this);
        String res = null;
        try {
            res = new Helper.Get(rl, parts, "issues/" + issueId).execute().get();
            JSONObject objRes = new JSONObject(res);
            String availableActions = objRes.getString("Available_actions");
            JSONArray jsonArray = new JSONArray(availableActions);
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject obj = new JSONObject(jsonArray.get(i).toString());
                availActionIds.add(obj.getInt("Act_id"));
                availActionNames.add(obj.getString("Act_Name"));

            }
            TextView nameLabel = new TextView(this);
            final Spinner name = new Spinner(this);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                    (this, android.R.layout.simple_spinner_item,
                            availActionNames);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                    .simple_spinner_dropdown_item);
            name.setAdapter(spinnerArrayAdapter);
            name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    availActionId = availActionIds.get(position);
                    qrCodeStr = parts[0] + " " +  issueId + " " + availActionIds.get(position) + " " + editText.getText();
                    generateQRCode(qrCodeStr);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
            rl.addView(name);
            rl.addView(nameLabel);

            relativeParams = (RelativeLayout.LayoutParams) nameLabel.getLayoutParams();
            relativeParams.setMargins(width * 34 / 100, height *  6/ 100, 0, 0);
            relativeParams.width = width * 50 / 100;
            relativeParams.height = height * 10 / 100;
            nameLabel.setLayoutParams(relativeParams);
            nameLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, width * 4 / 100);
            nameLabel.setText("Action:");

            relativeParams = (RelativeLayout.LayoutParams) name.getLayoutParams();
            relativeParams.setMargins(width * 33 / 100, height * 11 / 100, 0, 0);
            relativeParams.width = width * 35 / 100;
            name.setLayoutParams(relativeParams);
            name.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

            relativeParams = (RelativeLayout.LayoutParams) editText.getLayoutParams();
            relativeParams.setMargins(width * 25 / 100, height * 58 / 100, 0, 0);
            relativeParams.width = width * 50 / 100;
            editText.setLayoutParams(relativeParams);
            Button btn = (Button) findViewById(R.id.button);
            relativeParams = (RelativeLayout.LayoutParams) btn.getLayoutParams();
            relativeParams.setMargins(width * 30 / 100, height * 16 / 100, 0, 0);
            relativeParams.height = height * 12 / 100;
            relativeParams.width = width * 40 / 100;
            btn.setLayoutParams(relativeParams);
            btn.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    qrCodeStr = parts[0] + " " +  issueId + " " + availActionId + " " + editText.getText();
                    generateQRCode(qrCodeStr);
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    String msg = "Are you sure?";
                    AlertDialog.Builder builder = new AlertDialog.Builder(qrCodeActivity);

                    builder.setTitle("Confirm");
                    builder.setMessage(msg);
                    builder.setCancelable(false);
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent2 = new Intent();
                            setResult(1, intent2);
                            finish();
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
            qrCodeStr = parts[0] + " " + issueId + " " + availActionIds.get(0);
            relativeParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
            relativeParams.setMargins(width * 25 / 100, height * 10 / 100, 0, 0);
            relativeParams.height = height * 50 / 100;
            relativeParams.width = width * 50 / 100;
            imageView.setLayoutParams(relativeParams);
            generateQRCode(qrCodeStr);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        //this is only needed if you have specific things
        //that you want to do when the user presses the back button.
        /* your specific things...*/
        super.onBackPressed();
    }

    private void generateQRCode(String str) {
        try {
            Bitmap bitmap = encodeAsBitmap(str.trim());
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, 300, 400, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? Color.BLACK : Color.parseColor("#BDB9B9");
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 300, 0, 0, w, h);
        return bitmap;
    }
}
