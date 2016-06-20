package practice.com.restfulservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Flower> flowerList;
    private RecyclerView rview;
    private ProgressDialog pb;

    private TextView output;
    public static final String baseURL = "http://services.hanselandpetal.com/photos/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        rview = (RecyclerView) findViewById(R.id.flower_view);
//        rview.setHasFixedSize(true);
//        RecyclerView.LayoutManager rl = new LinearLayoutManager(this);
//        rview.setLayoutManager(rl);
//
        output = (TextView) findViewById(R.id.output);

//        pb = new ProgressDialog(this);
//        pb.setMessage("Loading...");
//        pb.setIndeterminate(true);
//        pb.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        RelativeLayout rel = (RelativeLayout) findViewById(R.id.relative);
        String tag = (String) rel.getTag();

        if(tag.equals("regular")) {
            System.out.println("Regular");
            Toast.makeText(this, "Regular", Toast.LENGTH_LONG).show();
        } else if(tag.equals("xlarge")) {
            Toast.makeText(this, "Xlarge", Toast.LENGTH_LONG).show();
        } else if(tag.equals("land")) {
            Toast.makeText(this, "Land", Toast.LENGTH_LONG).show();
        }
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
            if(isOnline()) {
                //get json data
//                requestData("http://services.hanselandpetal.com/feeds/flowers.json");
                //send to php
                requestData("http://services.hanselandpetal.com/restfuljson.php");
            } else {
                System.out.println("No network available");
            }


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyTask extends AsyncTask<String, String, List<Flower>> {

        @Override
        protected void onPreExecute() {
            pb.show();
        }

        @Override
        protected List<Flower> doInBackground(String... params) {
            String content = HttpManager.getdata(params[0]);
            flowerList = FlowerJSONParser.parseFeed(content);

            return flowerList;
        }

        @Override
        protected void onPostExecute(List<Flower> result) {
//            updateDisplay();
//            pb.dismiss();
        }
    }

    //Check network connection
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            //Check if wifi
            if(networkInfo.getType() != cm.TYPE_WIFI) {
                Toast.makeText(this, "Doesnt work w/o wifi", Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public void requestData(String url) {
        //Task for retreving json data
//        MyTask task = new MyTask();
//        task.execute(url);
//        //Parallel processing of asynch tasks
////        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);


        //Packaging up parameters to send to web service
        RequestPackage p = new RequestPackage();
//        p.setMethod("GET");
        p.setMethod("POST");
        p.setUri(url);
        p.setParam("param1", "Value1");
        p.setParam("param2", "Value2");
        p.setParam("param3", "Value3");
        p.setParam("param4", "Value4");

        My2Task my2Task = new My2Task();
        my2Task.execute(p);

    }

    protected void updateDisplay(String result) {
//        FlowerAdapter adapter = new FlowerAdapter(this, R.layout.flower_item, flowerList);
//        rview.setAdapter(adapter);
        output.setText(result);

    }

    private class My2Task extends AsyncTask<RequestPackage, Void, String> {
        @Override
        protected String doInBackground(RequestPackage... params) {
            String content = HttpManager2.getdata(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String s) {
            updateDisplay(s);
        }
    }

}