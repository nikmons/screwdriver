package com.mypackage.project;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

public class Helper {

    public static class Post extends AsyncTask<String, Void, String> {

        private ProgressBar progressBar;
        private int statusCode;
        private Object obj;
        private Toast toast;
        private TextView toastMessage;
        private String prefix;

        public Post(ProgressBar progressBar, TextView toastMessage, Toast toast, String prefix, Object obj) {
            this.progressBar = progressBar;
            this.toastMessage = toastMessage;
            this.toast = toast;
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
                statusCode = response.getStatusLine().getStatusCode();
                return response.getStatusLine().getReasonPhrase();
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.INVISIBLE);
            if (statusCode == 200) {

            } else {
                toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                toastMessage.setText(result);
                toast.setView(toastMessage);
                toast.show();
            }
        }
    }

    public static class Delete extends AsyncTask<String, Void, String> {

        private ProgressBar progressBar;
        private int statusCode;
        private int id;
        private Toast toast;
        private TextView toastMessage;
        private String prefix;

        public Delete(ProgressBar progressBar, TextView toastMessage, Toast toast, String prefix, int id) {
            this.progressBar = progressBar;
            this.toastMessage = toastMessage;
            this.toast = toast;
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
                statusCode = response.getStatusLine().getStatusCode();
                return response.getStatusLine().getReasonPhrase();
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.INVISIBLE);
            if (statusCode == 200) {

            } else {
                toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                toastMessage.setText(result);
                toast.setView(toastMessage);
                toast.show();
            }
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
}
