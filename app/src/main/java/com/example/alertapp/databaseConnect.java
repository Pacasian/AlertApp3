package com.example.alertapp;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class databaseConnect extends Activity {


    //ESTABLISHES CONNECTION WITH MS SQL DATABASE
    //can be called from any class

    /**
     * AJAY CHANGE FROM HERE TO,
     */


    private static String ipFromJson="json_not_received";
    @SuppressLint("NewApi")
    public Connection ConnectDB()
    {
        String ip= "192.168.4";
        //sharedPref =sharedPreference.getInstance(this);
        //ip = sharedPref.get("ip");
        /** try {
         sharedPref = sharedPreference.getInstance(this);
         ip = sharedPref.get("ip");
         }catch (NullPointerException e){
         System.out.println("_________________Error in connecting the server IP____________");
         }
         System.out.println(ip);
         String port = "1600";
         String Classes = "net.sourceforge.jtds.jdbc.Driver";
         String database = "dosthDatabase";
         String username = "dosth_db_salem";
         String password = "dosth_salem_db";
         //String username = "test";
         //String password = "test";
         //String database = "testDatabase";
         **/
        new JsonTask().execute("https://hits-rail.herokuapp.com/static/data.json");
        try {

           // sharedPref = sharedPreference.getInstance(this);
            ip=ipFromJson;

            if (ip.equals("json_not_received")){
                /**
                 * if the json IP is not updated in the connection class, then
                 * use the json IP from the SplashScreen Activity
                 */
                //ip = sharedPref.get("ip");
            }else{
                /**
                 * if the json IP is updated in the connection class, then
                 * update this IP into the sharedPreference.
                 * This is to adapt the application to handle the IP changes, while working in the app
                 */
                //ip=ipFromJson;
               // sharedPref.put("ip",ipFromJson);
            }


        }catch (NullPointerException e){
            System.out.println("_________________Error in connecting the server IP____________");
        }
        //ip="210.18.146.56";
        System.out.println(ip);
        String port = "1600";
        String Classes = "net.sourceforge.jtds.jdbc.Driver";
        String database = "dosthDatabase";
        String username = "dosth_db_salem";
        String password = "dosth_salem_db";
        String url = "jdbc:jtds:sqlserver://"+ip+":"+port+"/"+database;
        System.out.println(url);
        /**
         * AJAY, CHANGES END HERE.
         */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        try
        {
            Class.forName(Classes);
            connection = DriverManager.getConnection(url, username,password);
            //textView.setText("DATABASE CONNECTED");
        }
        catch (SQLException se)
        {
            Log.e("error here 1 : ", Objects.requireNonNull(se.getMessage()));
        }
        catch (ClassNotFoundException e)
        {
            Log.e("error here 2 : ", Objects.requireNonNull(e.getMessage()));
        }
        catch (Exception e)
        {
            Log.e("error here 3 : ", Objects.requireNonNull(e.getMessage()));
        }
        return connection;
    }
    private static class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }
                JSONObject json = null;
                try {
                    json = new JSONObject(buffer.toString());
                    String aJsonString = json.getString("ip");
                    System.out.println("-----------------------");
                    System.out.println(aJsonString);
                    ipFromJson=aJsonString;
                    //sharedInfo.put("ip",aJsonString);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return buffer.toString();


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            //txtJson.setText(result);


        }
    }
}