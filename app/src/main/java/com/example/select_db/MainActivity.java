package com.example.select_db;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity { //SELECT
    ArrayAdapter array;
    ListView lista;

    Button to_insert;
    Button to_delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lista= (ListView) findViewById(R.id.lista);
        to_insert=findViewById(R.id.to_insert);
        to_delete=findViewById(R.id.to_delete);


        to_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SecondActivity.class);
                startActivityForResult(intent, 100);
            }
        });

        to_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,DeleteActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        new MyDownloadTask().execute();




    }

    protected void onStart() {
        super.onStart();
        new MyDownloadTask().execute();
    }

    private class MyDownloadTask extends AsyncTask<Void, Void, Squadra[]> {
        @Override
        protected Squadra[] doInBackground(Void... strings) {
            try {
                URL url = new URL("http://10.0.2.2/pmsc/team.php");
                //URL url = new URL("http://pmsc9.altervista.org/team.php");

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(1000);
                urlConnection.setConnectTimeout(1500);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                /*urlConnection.setRequestMethod("POST"); //metodo Post
                String parametri = "nome=" + URLEncoder.encode("inter", "UTF-8");
                DataOutputStream dos = new DataOutputStream(urlConnection.getOutputStream());
                dos.writeBytes(parametri);
                dos.flush();
                dos.close();*/

                urlConnection.connect();
                InputStream is = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();

                String result = sb.toString();

                JSONArray jArray = new JSONArray(result);

                Squadra[] output = new Squadra[jArray.length()];
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    output[i]=new Squadra(json_data.getInt("id"),json_data.getString("nome"),json_data.getString("citta"),json_data.getString("paese"));
                    Log.i("log_tag", "_id: " + json_data.getInt("id") +
                            ", name: " + json_data.getString("nome") +
                            ", city: " + json_data.getString("citta") +
                            ", country: " + json_data.getString("paese"));
                }
                return output;
            } catch (Exception e) {
                Log.e("log_tag", "Error " + e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Squadra[] squadre) {
            array = new ArrayAdapter<Squadra>(MainActivity.this, R.layout.row_layout, 100, squadre) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent){

                    Squadra item = getItem(position);
                    LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View rowView = inflater.inflate(R.layout.row_layout, null);
                    TextView _id = (TextView)rowView.findViewById(R.id._id);
                    TextView _nome = (TextView)rowView.findViewById(R.id._nome);
                    TextView _citta = (TextView)rowView.findViewById(R.id._citta);
                    TextView _paese = (TextView)rowView.findViewById(R.id._paese);
                    _id.setText(""+item.id);
                    _nome.setText(item.nome);
                    _citta.setText(item.citta);
                    _paese.setText(item.paese);

                    return rowView;
                }
            };

            lista.setAdapter(array);

            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Squadra squadra = (Squadra) parent.getItemAtPosition(position);
                    Intent intent=new Intent(MainActivity.this, SecondActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putParcelable("squadra",squadra);
                    intent.putExtra("bundle", bundle);
                    startActivityForResult(intent, 100);
                }
            });
        }
    }




}
