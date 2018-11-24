package com.mypackage.project;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class QRCodeActivity extends AppCompatActivity {
    private int width, height, nextToAction;
    private ImageView imageView;
    private String qrCodeStr;
    private long serviceId;
    private boolean isCourier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode_activity);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#BDB9B9"));
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        final QRCodeActivity qrCodeActivity = this;
        Intent myIntent = getIntent();
        serviceId = myIntent.getLongExtra("serviceId", -1);
        nextToAction = myIntent.getIntExtra("nextToAction", -1);
        isCourier = myIntent.getBooleanExtra("isCourier", false);
        final EditText editText = (EditText) findViewById(R.id.editText);
        imageView = (ImageView) findViewById(R.id.qrCode);
        RelativeLayout.LayoutParams relativeParams;
        relativeParams = (RelativeLayout.LayoutParams) editText.getLayoutParams();
        relativeParams.setMargins(width * 25 / 100, height * 43 / 100, 0, 0);
        relativeParams.width = width * 50 / 100;
        editText.setLayoutParams(relativeParams);
        Button btn = (Button) findViewById(R.id.button);
        relativeParams = (RelativeLayout.LayoutParams) btn.getLayoutParams();
        relativeParams.setMargins(width * 30 / 100, height * 6 / 100, 0, 0);
        relativeParams.height = height * 12 / 100;
        relativeParams.width = width * 40 / 100;
        btn.setLayoutParams(relativeParams);
        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                generateQRCode(qrCodeStr + " " + editText.getText());
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String msg;
                if (!isCourier) {
                    msg = "Are you sure you fix the device and scan the code?";
                }
                else{
                    msg = "Are you sure you scan the code and deliver the device to the right place?";
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(qrCodeActivity);

                builder.setTitle("Confirm");
                builder.setMessage(msg);
                builder.setCancelable(false);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent2 = new Intent();
                        intent2.putExtra("serviceId", serviceId);
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
        qrCodeStr = serviceId + " " + nextToAction + " " + isCourier;
        if (!isCourier) {
            relativeParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
            relativeParams.setMargins(width * 25 / 100, height * -7 / 100, 0, 0);
            relativeParams.height = height * 50 / 100;
            relativeParams.width = width * 50 / 100;
            imageView.setLayoutParams(relativeParams);
        }
        else {
            btn.setVisibility(View.INVISIBLE);
            editText.setVisibility(View.INVISIBLE);
            relativeParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
            relativeParams.setMargins(width * 25 / 100, height * 23 / 100, 0, 0);
            relativeParams.height = height * 50 / 100;
            relativeParams.width = width * 50 / 100;
            imageView.setLayoutParams(relativeParams);
        }
        generateQRCode(qrCodeStr);
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
