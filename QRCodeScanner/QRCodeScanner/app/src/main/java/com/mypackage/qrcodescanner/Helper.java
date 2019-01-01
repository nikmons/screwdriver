package com.mypackage.qrcodescanner;

import android.os.AsyncTask;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class Helper {
    public static class Post extends AsyncTask<String, Void, String> {

        private Object obj;
        private String prefix;
        private String accessToken;

        public Post(String accessToken, String prefix, Object obj) {
            this.obj = obj;
            this.prefix = prefix;
            this.accessToken = accessToken;
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {
                Gson gson = new Gson();
                String json = gson.toJson(obj);
                String link = "https://screwdriver-api-heroku.herokuapp.com/todo/api/v1.0/" + prefix;
                HttpClient client = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(link);
                httpPost.addHeader("Authorization", "Bearer " + accessToken);
                StringEntity entity = new StringEntity(json, HTTP.UTF_8);
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
                HttpResponse response = client.execute(httpPost);
                HttpEntity httpEntity = response.getEntity();
                return EntityUtils.toString(httpEntity).replace("\n", "");
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
}
