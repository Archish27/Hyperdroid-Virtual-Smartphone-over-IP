package com.hyperdoid.hyperdroidbenchmark;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private TextView avltb;
    private Button avlbtn;

    private TextView srttb;
    private Button srtbtn;

    private TextView exptb;
    private Button expbtn;


    private TextView facttb;
    private Button factbtn;


    private TextView nqtb;
    private Button nqbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        avltb = (TextView) findViewById(R.id.avltb);
        avlbtn = (Button) findViewById(R.id.avlbtn);
        avlbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"AVL Test Started",Toast.LENGTH_SHORT).show();
                new AVLTask().execute();
            }
        });


        srttb = (TextView) findViewById(R.id.srttb);
        srtbtn = (Button) findViewById(R.id.srtbtn);
        srtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Sorting Test Started",Toast.LENGTH_SHORT).show();
                new SortingTask().execute();
            }
        });


        exptb = (TextView) findViewById(R.id.exptb);
        expbtn = (Button) findViewById(R.id.expbtn);
        expbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"HighExponential Test Started",Toast.LENGTH_SHORT).show();
                new ExpTask().execute();


            }
        });


        facttb = (TextView) findViewById(R.id.facttb);
        factbtn = (Button) findViewById(R.id.factbtn);
        factbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Factorial Test Started",Toast.LENGTH_SHORT).show();
                new FactorialTask().execute();
            }
        });


        nqtb = (TextView) findViewById(R.id.nqtb);
        nqbtn = (Button) findViewById(R.id.nqbtn);
        nqbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"NQueens Test Started",Toast.LENGTH_SHORT).show();
              new NQueensTask().execute();
            }
        });
    }
    private class NQueensTask extends AsyncTask<String, String, String> {
        long sum[] = new long[10];

        @Override
        protected String doInBackground(String... strings) {
            for (int i = 0; i < 10; i++)
                sum[i] = BigFact.TestExpo(30000);
            Arrays.sort(sum);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            nqtb.setText(Long.toString(sum[9]));
            Toast.makeText(getApplicationContext(),"NQueens Test Completed ",Toast.LENGTH_SHORT).show();
        }
    }

    private class AVLTask extends AsyncTask<String, String, String> {
        long sum[] = new long[10];

        @Override
        protected String doInBackground(String... strings) {

            for (int i = 0; i < 10; i++)
                sum[i] = AVLTree.TestingAVL(30000);
            Arrays.sort(sum);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            avltb.setText(Long.toString(sum[9]));
            Toast.makeText(getApplicationContext(),"AVL Test Completed ",Toast.LENGTH_SHORT).show();
        }
    }

    private class FactorialTask extends AsyncTask<String, String, String> {
        long sum[] = new long[10];

        @Override
        protected String doInBackground(String... strings) {

            for (int i = 0; i < 10; i++)
                sum[i] = BigFact.TestExpo(30000);
            Arrays.sort(sum);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            facttb.setText(Long.toString(sum[9]));
            Toast.makeText(getApplicationContext(),"Factorial Test Completed ",Toast.LENGTH_SHORT).show();
        }
    }

    private class SortingTask extends AsyncTask<String, String, String> {
        long sum[] = new long[10];

        @Override
        protected String doInBackground(String... strings) {
            for (int i = 0; i < 10; i++)
                sum[i] = Sorting.TestingSorting(30000);
            Arrays.sort(sum);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            srttb.setText(Long.toString(sum[9]));
            Toast.makeText(getApplicationContext(),"Sorting Test Completed ",Toast.LENGTH_SHORT).show();
        }
    }

    private class ExpTask extends AsyncTask<String, String, String> {
        long sum[] = new long[10];

        @Override
        protected String doInBackground(String... strings) {

            for (int i = 0; i < 10; i++)
                sum[i] = HighExpo.TestExpo(30000, 30000);
            Arrays.sort(sum);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            exptb.setText(Long.toString(sum[9]));
            Toast.makeText(getApplicationContext(),"HighExponential Test Completed ",Toast.LENGTH_SHORT).show();
        }
    }
}