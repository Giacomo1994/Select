package com.example.select_db;

import androidx.appcompat.app.AppCompatActivity;

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

public class DeleteActivity extends AppCompatActivity {
    //GITHUB
    EditText input_delete;
    Button btn_delete;
    TextView output_delete;
    String nome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        input_delete=findViewById(R.id.input_delete);
        btn_delete=findViewById(R.id.btn_Delete);
        output_delete=findViewById(R.id.output_delete);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nome=input_delete.getText().toString();
                new MyDeleteTask().execute();
            }
        });
    }

    private class MyDeleteTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... strings) {
            try {
                URL url = new URL("http://10.0.2.2/pmsc/team_delete.php");
                //URL url = new URL("http://pmsc9.altervista.org/team_delete.php");

                //Passo parametri al file php
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setDoOutput(true); //manda dei dati al file php
                urlConnection.setRequestMethod("POST"); //metodo Post
                String parametri = "nome=" + URLEncoder.encode(nome, "UTF-8"); //imposto parametri da passare
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
            output_delete.setText(esito);
        }
    }



}
