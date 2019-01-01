package com.mypackage.qrcodescanner;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import google.zxing.integration.android.IntentIntegrator;
import google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout rl = (RelativeLayout)findViewById(R.id.rl);
        rl.setBackgroundColor(Color.parseColor("#0488CA"));
        Button scanBtn = (Button)findViewById(R.id.scan_button);
        scanBtn.setOnClickListener(this);
    }

    public void onClick(View v){
        if(v.getId()==R.id.scan_button){
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String parts[] = scanContent.split(" ");
            TimeLineEntryModel model = new TimeLineEntryModel();
            model.Act_id = Integer.parseInt(parts[2]);
            if (parts.length == 3)
                model.Ist_Comment ="";
            else
                model.Ist_Comment = parts[3];
            try {
                String res = new Helper.Post(parts[0], "myissues/" + parts[1] + "/timeline", model).execute().get();
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Scanned Successfully!", Toast.LENGTH_SHORT);
                toast.show();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Error with Post!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
