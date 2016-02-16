package com.example.kayle.multithreading;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public ArrayAdapter<String> stringAdpt;
    private ProgressBar bar;
    public Context contextC;
    public Context contextL;
    List<String> list = new ArrayList<>();

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bar = (ProgressBar) findViewById(R.id.progressBar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createFile(View v) throws InterruptedException {
        contextC = this;
        Creater task = new Creater();
        task.execute();

        //Thread creating= new Thread(new Creater());
        //creating.start();
        //creating.join();

    }

    class Creater extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute(){
            bar.setProgress(0);
        }

        @Override
        protected Void doInBackground(Void... params){
            String filename = "numbers.txt";
            File file = new File(contextC.getFilesDir(), filename);

            try {
                FileWriter fileWriter = new FileWriter(file);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write("1");
                String num;
                for(int i= 2; i<=10; i++) {
                    bufferedWriter.newLine();
                    num = String.valueOf(i);
                    bufferedWriter.write(num);
                    Thread.sleep(250);
                    publishProgress(i);
                }
                bufferedWriter.close();

            }
            catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values){
            bar.setProgress(values[0]);
        }

        @Override
        protected  void onPostExecute(Void v){
        }


    }

    public void load(View v) throws InterruptedException {
        contextL = this;
        Loader task = new Loader();
        task.execute();


        }

    private class Loader extends AsyncTask<Void, Integer, Void>{
        @Override
        protected void onPreExecute(){
            bar.setProgress(0);
        }

        @Override
        protected Void doInBackground(Void... params){
            String filename = "numbers.txt";
            String content = "";
            try {
                FileInputStream fis = openFileInput(filename);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                String line;
                int i = 1;
                while ((line = br.readLine()) != null) {
                    list.add(line);
                    Thread.sleep(250);
                    publishProgress(i);
                    i++;
                }
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values){
            bar.setProgress(values[0]);
        }

        @Override
        protected  void onPostExecute(Void v){

            stringAdpt = new ArrayAdapter<>(contextL, android.R.layout.simple_list_item_1,list);
            ListView listView = (ListView)findViewById(R.id.listView);
            listView.setAdapter(stringAdpt);
        }
    }

        public void clear(View v){
        stringAdpt.clear();
        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(stringAdpt);
    }
}
