package com.example.myapplication.myapplication23;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText etMotTransforme = null;
    EditText etMotATransformer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etMotATransformer = findViewById(R.id.et_mot_a_transformer);
        etMotTransforme = findViewById(R.id.et_mot_transforme);

    }

    public void onClickTransformer(View view) {
        Log.i("ACOS", "Entree dans onClickTransformer");
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Log.i("ACOS", "Creation du ConnectivityManager cm=" + cm.toString());
        NetworkInfo info = cm.getActiveNetworkInfo();
        Log.i("ACOS", "Recuperation d'un objet NetworkInfo info" + info);
        if(info != null && info.isConnected()){
            Log.i("ACOS", "Appel de la tache asynchrone va se connecter au web service ok");
            AccessResourceTask task = new AccessResourceTask();
            task.execute(etMotATransformer.getText().toString());
        }else{
            Toast.makeText(MainActivity.this, "Pas d'access à Internet", Toast.LENGTH_SHORT).show();
        }

    }

    private class AccessResourceTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            Log.i("ACOS", "Entree dans le doInBackground de la tache asynchrone");
            HttpURLConnection httpURLConnection = null;
            StringBuffer stringBuffer = new StringBuffer();

            try{
                Log.i("ACOS", "Creation de l'objet URL");
                URL url = new URL("http://192.168.179.1:8080/TrucFournisseur/ress?p="+strings[0]);

                Log.i("ACOS","Création de l'objet HttpURLConnection et envoi de la requête");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                Log.i("ACOS", "Recuperation de la reponse");
                InputStream in = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(in);

                int unCharacter;
                while((unCharacter = isr.read()) != -1){
                    stringBuffer.append((char)unCharacter);
                }

                connection.disconnect();

            } catch (Exception e) {
                Log.e("ACOS","ERREUR : " + e.getMessage());
            }
            Log.i("ACOS", "RESULTAT:" + stringBuffer.toString());
            return stringBuffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            etMotTransforme.setText(s);
        }
    }
}