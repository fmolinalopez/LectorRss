package org.molina.lectorrss;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://ep00.epimg.net/rss/tags/ultimas_noticias.xml");
    }

    private class DownloadData extends AsyncTask<String, Void, String> {

        private static final String TAG = "DownloadData";

        @Override
        protected String doInBackground(String... strings) {
            String result = downloadXmlFile(strings[0]);

            if (result == null) {
                Log.d(TAG, "Problema descargando el XML");
            }

            return result;
        }

        public String downloadXmlFile(String urlPath){
            // StringBuilder vs StringBuffer
            StringBuilder tempBuffer = new StringBuilder();

            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "Response Code: " + response);

                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int charsRead;
                char[] inputBuffer = new char[500];

                while( true ) {
                    charsRead = isr.read(inputBuffer);

                    if ( charsRead <= 0 ) {
                        break;
                    }

                    tempBuffer.append(String.copyValueOf(inputBuffer, 0, charsRead));
                }

                return tempBuffer.toString();
            }catch (IOException e){
                Log.d(TAG, "Error: No se pudo descargar el RSS - " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Log.d(TAG, "Resultado: " + result);
            System.out.print("Resultado: " + result);
        }
    }
}
