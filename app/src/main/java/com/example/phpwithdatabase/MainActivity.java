package com.example.phpwithdatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String url = "http://192.168.56.1:8080/PhpProject1/select.php";
    String Addurl = "http://192.168.56.1:8080/PhpProject1/addstudent.php";
    String deleteurl = "http://192.168.56.1:8080/PhpProject1/delete.php";
    String updateurl = "http://192.168.56.1:8080/PhpProject1/update.php";
    ListView lstStud;
    EditText txtStudentName, txtCity, txtContactNo,txtid;
    Button btnAdd,btnupdate;
    int StudentID;
    int studid=0;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ControlInitialization();
        RequestJSON();
        events();
        registerForContextMenu(lstStud);
    }

    private void events() {
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtStudentName.equals("") || txtCity.equals("") || txtContactNo.equals("")){
                    Toast.makeText(getApplicationContext(), "please fill all the details", Toast.LENGTH_SHORT).show();
                }
                else {
                    UpdateRequest();
                }
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select Option");
        menu.add("EDIT");
        menu.add("DELETE");

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected( MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String data = adapter.getItem(info.position);
        String dataarr[] = data.split(",");
        studid = Integer.parseInt(dataarr[0]);

        if(item.getTitle().equals("EDIT"))
        {
            //Toast.makeText(this, "EDIT", Toast.LENGTH_SHORT).show();
            txtStudentName.setText(dataarr[1]);
            txtCity.setText(dataarr[2]);
            txtContactNo.setText(dataarr[3]);
        }
        if(item.getTitle().equals("DELETE"))
        {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure want to delete this record?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DeleteRequest();
                        }
                    })
                    .setNegativeButton("No",null)
                    .show();
           //Toast.makeText(this, "DELETE "+studid, Toast.LENGTH_SHORT).show();
        }
        return super.onContextItemSelected(item);
    }


    private void ControlInitialization() {
        lstStud = findViewById(R.id.lstStud);
        txtStudentName = findViewById(R.id.txtName);
        txtCity = findViewById(R.id.txtCity);
        txtid=findViewById(R.id.txtid);
        txtContactNo = findViewById(R.id.txtContactNo);
        btnAdd = findViewById(R.id.btnAdd);
        btnupdate=findViewById(R.id.btnupdate);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddRequest();
                RequestJSON();
            }
        });

        lstStud.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapter.getItem(i);
                String[] SelectedColumn = selectedItem.split(",");

                StudentID = Integer.parseInt(SelectedColumn[0]);
                txtid.setText(SelectedColumn[0]);
                txtStudentName.setText(SelectedColumn[1]);
                txtContactNo.setText((SelectedColumn[2]));
                txtCity.setText(SelectedColumn[3]);
                //DeleteRequest(StudentID);

            }
        });
        RequestJSON();

    }



    private void AddRequest() {
        StringRequest addRequest = new StringRequest(Request.Method.POST, Addurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        txtStudentName.setText("");
                        txtCity.setText("");
                        txtContactNo.setText("");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("StudentName", txtStudentName.getText().toString());
                params.put("City", txtCity.getText().toString());
                params.put("ContactNo", txtContactNo.getText().toString());
                return params;
            }
        };

        RequestQueue addqueue = Volley.newRequestQueue(getApplicationContext());
        addqueue.add(addRequest);


    }

    private void RequestJSON() {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Stringobject", response);
                    JSONObject object = new JSONObject(response);
                    JSONArray dataArray = object.getJSONArray("students");
                    ArrayList<String> StudentArrayList = new ArrayList<String>();
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject dataObject = dataArray.getJSONObject(i);
                        StudentArrayList.add(dataObject.getString("id") + "," +
                                dataObject.getString("StudentName") + "," +
                                dataObject.getString("City") + "," +
                                dataObject.getString("ContactNo"));
                    }

                    adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_list_item_1, StudentArrayList);
                    lstStud.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void DeleteRequest() {
        StringRequest deleteRequest = new StringRequest(Request.Method.POST, deleteurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                RequestJSON();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("id", String.valueOf(studid));
                return param;
            }
        };

        RequestQueue deleteQueue = Volley.newRequestQueue(getApplicationContext());
        deleteQueue.add(deleteRequest);
    }

    public void UpdateRequest(){
        String StudentName = txtStudentName.getText().toString();
        String City = txtCity.getText().toString();
        String ContactNo = txtContactNo.getText().toString();

        StringRequest request = new StringRequest(Request.Method.POST, updateurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        RequestJSON();
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        txtStudentName.setText("");
                        txtCity.setText("");
                        txtContactNo.setText("");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<String,String>();
                params.put("id",String.valueOf(studid));
                params.put("StudentName", StudentName);
                params.put("City", City);
                params.put("ContactNo", ContactNo);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}