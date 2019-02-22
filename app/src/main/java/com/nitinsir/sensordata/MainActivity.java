package com.nitinsir.sensordata;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    //the URL having the json data
    SQLiteDatabase mDatabase;
    public Context context;
    private static final String JSON_URL = "https://api.thingspeak.com/channels/542426/feeds.json?results=2";
    public static List<DataStorage> mylist = new ArrayList<DataStorage>();
    @BindView(R.id.temp)
    TextView temp;
    @BindView(R.id.humid)
    TextView humid;
    @BindView(R.id.freq)
    TextView freq;
    @BindView(R.id.ac)
    TextView ac;
    @BindView(R.id.r)
    TextView r;
    @BindView(R.id.g)
    TextView g;
    @BindView(R.id.b)
    TextView b;
    @BindView(R.id.dist)
    TextView dist;
    @BindView(R.id.addOn)
    ImageView addOn;
    @BindView(R.id.btnload)
    ImageView btnload;
    @BindView(R.id.drawer_layout)
    LinearLayout drawerLayout;
    String imagepath, statement;
    @BindView(R.id.getStatement)
    TextView getStatement;
    @BindView(R.id.imgshow)
    ImageView imgshow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context=this;
        checkPermission();
        mDatabase = openOrCreateDatabase("tasksDb.db", MODE_PRIVATE, null);
        loadDatafromURL();
        getStatement.setText(" \uD83D\uDC49 No statement ");
    }
    private void showEmployeesFromDatabase() {
        //we used rawQuery(sql, selectionargs) for fetching all the employees
        Cursor cursorEmployees = mDatabase.query(TaskContract.TaskEntry.TABLE_NAME ,null,null,null,null,null,null,null);

        //if the cursor has some data
        if (cursorEmployees.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                mylist.add(new DataStorage(
                        cursorEmployees.getString(1),
                        Integer.parseInt(cursorEmployees.getString(3)),
                        Integer.parseInt(cursorEmployees.getString(4)),
                        Integer.parseInt(cursorEmployees.getString(5)),
                        cursorEmployees.getString(2)
                ));
            } while (cursorEmployees.moveToNext());
        }
        //closing the cursor
        cursorEmployees.close();
        //creating the adapter object

        //adding the adapter to listview
     }

    private void loadDatafromURL() {

        //making the progressbar visible

        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray heroArray = obj.getJSONArray("feeds");

                            String a[] = null;
                            //getting the json object of the particular index inside the array
                            JSONObject heroObject = heroArray.getJSONObject(1);
//                            Toast.makeText(MainActivity.this, "" + heroObject, Toast.LENGTH_SHORT).show();
                            //creating a hero object and giving them the values from json object
                            temp.setText(heroObject.getString("field1").toString());
                            humid.setText(heroObject.getString("field2").toString());
                            freq.setText(heroObject.getString("field3").toString());
                            ac.setText(heroObject.getString("field4").toString());
                            r.setText(heroObject.getString("field5").toString());
                            g.setText(heroObject.getString("field6").toString());
                            b.setText(heroObject.getString("field7").toString());
                            dist.setText(heroObject.getString("field8").toString());
                            //adding the adapter to listview

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }

    public void Reloadthedata(View view) {
        loadDatafromURL();
        showEmployeesFromDatabase();


    }

    @OnClick(R.id.addOn)
    public void onViewClicked() {
        startActivity(new Intent(this, Main2Activity.class));
    }

    public void getDetailsImage(View view) {
        showEmployeesFromDatabase();

        for (int i = 0;i<mylist.size();i++)
        {
            if(Integer.parseInt(r.getText().toString()) == mylist.get(i).r_color && Integer.parseInt(g.getText().toString()) == mylist.get(i).g_color && Integer.parseInt(b.getText().toString()) == mylist.get(i).b_color )
            {
                imgshow.setImageURI(Uri.parse(mylist.get(i).image));
                getStatement.setText(" \uD83D\uDC49  "+mylist.get(i).statement);

            }

        }
    }

       @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkPermission()
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Write calendar permission is necessary to write event!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "PERMISSION GRANTED!!!", Toast.LENGTH_SHORT).show();
                } else {
                    //code for deny
                }
                break;
        }
    }
}
