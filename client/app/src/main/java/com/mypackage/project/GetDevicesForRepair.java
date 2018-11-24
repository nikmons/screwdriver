package com.mypackage.project;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Arrays;

public class GetDevicesForRepair extends AsyncTask<String, Void, String> {

    private MainActivity mainActivity;
    public GetDevicesForRepair(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
        protected void onPreExecute () {

        }

        @Override
        protected String doInBackground (String...arg0){
            try {
                String link = "https://screwdriver-api-heroku.herokuapp.com/todo/api/v1.0/tasks";
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.addHeader(BasicScheme.authenticate( new UsernamePasswordCredentials("admin", "admin"), "UTF-8", false));
                request.setURI(new URI(link));
                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));

                String json = "",line;
                while ((line = in.readLine()) != null) {
                    json = line;
                    int index = json.indexOf("[");
                    if(index >= 0) {
                        json = json.substring(index);
                        json = json.substring(0, json.length() - 1);
                    }
                    break;
                }
                in.close();
                Gson gson = new Gson();
                DevicesToRepairModel[] model = gson.fromJson(json, DevicesToRepairModel[].class);
                mainActivity.devicesList = Arrays.asList(model);
                return "Done";
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute (String result){
        }
    }
