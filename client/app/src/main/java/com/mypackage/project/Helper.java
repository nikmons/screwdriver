package com.mypackage.project;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

public class Helper {

    public static class Get extends AsyncTask<String, Void, String> {

        private ProgressBar progressBar;
        private String prefix;

        public Get(ProgressBar progressBar, String prefix) {
            this.progressBar = progressBar;
            this.prefix = prefix;
        }

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {
                String link = "https://screwdriver-api-heroku.herokuapp.com/todo/api/v1.0/" + prefix;
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials("admin", "admin"), "UTF-8", false));
                request.setURI(new URI(link));
                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));

                String json = "", line;
                while ((line = in.readLine()) != null) {
                    json = line;
                    int index = json.indexOf("[");
                    if (index >= 0) {
                        json = json.substring(index);
                        json = json.substring(0, json.length() - 1);
                    }
                    break;
                }
                in.close();
                return json;
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public static class Post extends AsyncTask<String, Void, String> {

        private ProgressBar progressBar;
        private Object obj;
        private String prefix;

        public Post(ProgressBar progressBar, String prefix, Object obj) {
            this.progressBar = progressBar;
            this.obj = obj;
            this.prefix = prefix;
        }

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {
                Gson gson = new Gson();
                String json = gson.toJson(obj);
                String link = "https://screwdriver-api-heroku.herokuapp.com/todo/api/v1.0/" + prefix;
                HttpClient client = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(link);
                String authorizationString = "Basic " + Base64.encodeToString("admin:admin".getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
                httpPost.setHeader("Authorization", authorizationString);
                StringEntity entity = new StringEntity(json, HTTP.UTF_8);
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
                HttpResponse response = client.execute(httpPost);
                HttpEntity httpEntity = response.getEntity();
                return EntityUtils.toString(httpEntity).replace("\n","");
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.INVISIBLE);

        }
    }

    public static class Delete extends AsyncTask<String, Void, String> {

        private ProgressBar progressBar;
        private int id;
        private String prefix;

        public Delete(ProgressBar progressBar, String prefix, int id) {
            this.progressBar = progressBar;
            this.id = id;
            this.prefix = prefix;
        }

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {
                String link = "https://screwdriver-api-heroku.herokuapp.com/todo/api/v1.0/" + prefix + "/" + id;
                HttpClient client = new DefaultHttpClient();
                HttpDelete httpDelete = new HttpDelete(link);
                String authorizationString = "Basic " + Base64.encodeToString("admin:admin".getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
                httpDelete.setHeader("Authorization", authorizationString);
                HttpResponse response = client.execute(httpDelete);
                HttpEntity httpEntity = response.getEntity();
                return EntityUtils.toString(httpEntity);
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    static public boolean isOnline(Context context) {
        boolean connected = false;
        try {
            ConnectivityManager connectivityManager;
            NetworkInfo wifiInfo, mobileInfo;
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;

        } catch (Exception e) {

        }
        return connected;
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public String[] getPrefs(Context context)
    {
        String[] parts = new String[2];
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        parts[0] = pref.getString("access_token", null);
        parts[1] = pref.getString("refresh_token", null);
        return parts;
    }
}
