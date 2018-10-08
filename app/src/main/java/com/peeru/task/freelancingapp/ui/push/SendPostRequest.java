package com.peeru.task.freelancingapp.ui.push;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.peeru.task.freelancingapp.util.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class SendPostRequest extends AsyncTask<String, Void, String> {
    final static private String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    private final Context context;
    private String msg = null;
    public SendPostRequest(Context c) {
        this.context = c;
    }

    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... strings) {
        List<String> sendMsgList = Arrays.asList(strings);
        Log.d("TAG","Peeru Send List"+sendMsgList.toString());
        try {

            URL url = new URL(FCM_URL);

            // create connection.
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();


            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            //set method as POST or GET
            conn.setRequestMethod("POST");

            //pass FCM server key
            conn.setRequestProperty("Authorization", "key=AIzaSyBqmoQYfJgb_pMsimECiyKsS5Zyh8gByak");

            //Specify Notifications Format
            conn.setRequestProperty("Content-Type", "application/json");

            //Create JSON Object & pass value
            JSONObject notificationJson = new JSONObject();
            notificationJson.put("check_action", Constant.PUSH_ACTION);
            JSONObject infoJson = new JSONObject();
            infoJson.put("title", "Message");
            infoJson.put("message", sendMsgList.get(0));
            infoJson.put("action", Constant.PUSH_ACTION);
            infoJson.put("msg_status",sendMsgList.get(1));
            JSONObject json = new JSONObject();
            json.put("registration_ids", new JSONArray(sendMsgList.subList(2, sendMsgList.size())));
            json.put("notification", notificationJson);
            json.put("data", infoJson);

            String urlParameters = json.toString();
            DataOutputStream dStream = new DataOutputStream(conn.getOutputStream());
            dStream.writeBytes(urlParameters);
            dStream.flush();
            dStream.close();
            int status = 0;
            status = conn.getResponseCode();
            if (status == 200) {
                msg = "Notifications Sent Successfully!!!";
               // Toast.makeText(EnrichlabApplication.getAppContext(), "Notifications Sent !! ", Toast.LENGTH_LONG).show();
            } else {
                msg = "Some issue in message sent!!!";
            }
            final StringBuilder output = new StringBuilder("Request URL " + url);
            output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
            output.append(System.getProperty("line.separator") + "Response Code " + status);
            output.append(System.getProperty("line.separator") + "Type " + "POST");
            output.append(System.getProperty("line.separator") + "Api Key " + strings[1]);
            output.append(System.getProperty("line.separator") + "Android Id " + strings[0]);
            System.out.print("Request Output =========" + output.toString());
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            StringBuilder responseOutput = new StringBuilder();
            System.out.println("output===============" + br);
            while ((line = br.readLine()) != null) {
                System.out.println(" Line =======" + line);
            }
            responseOutput.append(line);
            br.close();
            output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + "Response Output" + responseOutput.toString());
            System.out.print("Response Output =========" + output);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Notifications", "Peeru Notifications : " + msg);
        return msg;
    }

    protected void onPostExecute() {

    }

}