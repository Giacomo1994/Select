package com.example.select_db;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SecondActivity extends AppCompatActivity { //INSERT INTO TABLE
    EditText input_nome;
    EditText input_citta;
    EditText input_paese;
    EditText input_id;
    TextView output_insert;
    Button inserisci;
    String id;
    String nome;
    String citta;
    String paese;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        input_id=findViewById(R.id.input_id);
        input_nome=findViewById(R.id.input_nome);
        input_citta=findViewById(R.id.input_citta);
        input_paese=findViewById(R.id.input_paese);
        inserisci=findViewById(R.id.inserisci);
        output_insert=findViewById(R.id.output_insert);

        inserisci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id= input_id.getText().toString();
                citta= input_citta.getText().toString();
                nome= input_nome.getText().toString();
                paese= input_paese.getText().toString();

                new MyInsertTask().execute();
            }
        });
    }
    private class MyInsertTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... strings) {
            try {
                URL url = new URL("http://10.0.2.2/pmsc/team_insert.php");
                //URL url = new URL("http://pmsc9.altervista.org/team_insert.php");

                //Passo parametri al file php
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setDoOutput(true); //manda dei dati al file php
                urlConnection.setRequestMethod("POST"); //metodo Post
                String parametri = "id=" + URLEncoder.encode(id, "UTF-8") + "&nome=" + URLEncoder.encode(nome, "UTF-8") + "&citta=" + URLEncoder.encode(citta, "UTF-8") + "&paese=" + URLEncoder.encode(paese, "UTF-8"); //imposto parametri da passare
                DataOutputStream dos = new DataOutputStream(urlConnection.getOutputStream());
                dos.writeBytes(parametri); //passo parametri
                dos.flush();
                dos.close();

                //leggo stringa di ritorno da file php
                urlConnection.connect();
                InputStream input = urlConnection.getInputStream();  //leggo input mandato dal file php in formato InputStream
                byte[] buffer = new byte[1024]; //parsifico InputStram
                int numRead = 0;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while((numRead=input.read(buffer))!=-1){
                    baos.write(buffer, 0, numRead);
                }
                input.close();
                String stringaRicevuta = new String(baos.toByteArray()); //converto InputStram in stringa
                return stringaRicevuta; //ritorno stringa
            } catch (Exception e) {
                Log.e("SimpleHttpURLConnection", e.getMessage());
                return ""+e.getMessage();
            } finally {
            }
        }

        @Override
        protected void onPostExecute(String esito) {
            output_insert.setText(esito);
        }
    }
}
