package com.hydromark.hydromark;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    public static String TAG = "MainActivity";

    //
//    static {
//        if (!OpenCVLoader.initDebug()) {
//            Log.d(TAG, "OpenCV not loaded");
//        } else {
//            Log.d(TAG, "OpenCV loaded");
//        }
//    }
    Button bSubmit;
    EditText etText;
    String appFileDirectory;
    String executableFilePath;
    TextView tvResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResults = (TextView) findViewById(R.id.tvResults);
        appFileDirectory = getApplicationContext().getApplicationInfo().dataDir;

        String fileString = "";
        for (int i = 1; i <= 6; i++) {
            fileString += Environment.getExternalStorageDirectory() + "/Dataset/boat" + i + ".jpg ";
        }

        executableFilePath = appFileDirectory + "/VoDCA_x86 " + fileString +
                " --output " + Environment.getExternalStorageDirectory() + "/Dataset/boat_stitch.jpg";

        bSubmit = (Button) findViewById(R.id.bSubmit);
        etText = (EditText) findViewById(R.id.etText);
        etText.setText(executableFilePath);
        //copyAssets("VoDCA", appFileDirectory);
        copyAssets("VoDCA_x86", appFileDirectory);
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "Performing Image Stitching Please wait", Toast.LENGTH_LONG).show();
                executableFilePath = etText.getText().toString();
                new ImageStitchingTask(executableFilePath).execute();

            }
        });

//        try {
//            final String libPath = Environment.getExternalStorageDirectory() + "/VoDCA/PackOpenCV_fat.jar";
//            final File tmpDir = getDir("dex", 0);
//
//            final DexClassLoader classloader = new DexClassLoader(libPath, tmpDir.getAbsolutePath(), getApplicationContext().getApplicationInfo().nativeLibraryDir, this.getClass().getClassLoader());
//            final Class<Object> classToLoad = (Class<Object>) classloader.loadClass("Main");
//
//            final Object myInstance = classToLoad.newInstance();
//            final Method doSomething = classToLoad.getMethod("doSomething");
//
//            doSomething.invoke(myInstance);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }

    private void copyAssets(String filename, String appFileDirectory) {

        AssetManager assetManager = getAssets();

        InputStream in = null;
        OutputStream out = null;
        Log.d(TAG, "Attempting to copy this file: " + filename); // + " to: " +       assetCopyDestination);

        try {
            in = assetManager.open(filename);
            Log.d(TAG, "outDir: " + appFileDirectory);
            File outFile = new File(appFileDirectory, filename);
            out = new FileOutputStream(outFile);
            IOUtils.copy(in, out);
            in.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            Log.e(TAG, "Failed to copy asset file: " + filename, e);
        }

        Log.d(TAG, "Copy success: " + filename);
    }

    private class ImageStitchingTask extends AsyncTask<String, String, String> {

        String command;
        long totalTime;

        public ImageStitchingTask(String command) {
            this.command = command;
        }

        @Override
        protected String doInBackground(String... strings) {
            long startTime = System.nanoTime();
            File execFile = new File(command);
            execFile.setExecutable(true);
            try {
                //Runtime.getRuntime().exec("/system/bin/chmod 744 " + appFileDirectory + "/VoDCA");
                Runtime.getRuntime().exec("/system/bin/chmod 744 " + appFileDirectory + "/VoDCA_x86");

                Process process = Runtime.getRuntime().exec(command);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));
                int read;
                char[] buffer = new char[4096];
                StringBuffer output = new StringBuffer();
                while ((read = reader.read(buffer)) > 0) {
                    output.append(buffer, 0, read);
                }
                reader.close();
                process.waitFor();
                Log.d(TAG, "output: " + output.toString());
                //Toast.makeText(MainActivity.this, "Output : " + output.toString(), Toast.LENGTH_SHORT).show();
                long endTime = System.nanoTime();
                totalTime = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tvResults.setText("Results : " + totalTime + "ms");
            Toast.makeText(MainActivity.this, "Results : " + totalTime, Toast.LENGTH_SHORT).show();

        }
    }


}
