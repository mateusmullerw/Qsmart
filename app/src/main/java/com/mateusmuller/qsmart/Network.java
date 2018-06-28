package com.mateusmuller.qsmart;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Network {

    static String resposta = null;

    public static String sendRecieve(String url){
        new getStringTask().execute(url);
        return Network.resposta;
    }

    static private class getStringTask extends AsyncTask<String, Void, String>{
    @Override
    protected  String doInBackground(String...url){
        return getString(url[0]);
    }
    protected void onPostExecute(String string){
        resposta = string;
    }
    }

    static public String getString(String path){
        String string = null;
        //InputStream inStream;
        final String TAG = "Download task";
        try{

            URL url = new URL(path);
            HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();

            urlConn.setConnectTimeout(5000);
            urlConn.setReadTimeout(2500);
            urlConn.setFixedLengthStreamingMode(0);
            urlConn.setRequestMethod("GET");
            urlConn.setDoInput(true);

            urlConn.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            StringBuffer buffer = new StringBuffer(50);
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                buffer.append(inputLine);
            in.close();
            string = buffer.toString();
            /*
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            string = result.toString("UTF-8");*/

        }catch (MalformedURLException e){
            Log.e(TAG, "url error :" + e.getMessage());
        }catch (IOException e){
            Log.e(TAG, "Download Failed :" + e.getMessage());
        }
    return string;
    }
}