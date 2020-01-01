package com.istiak.onlinephonebook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    //Declaring Java EditText objects
    EditText etxtName,etxtCell,etxtPassword;
    //Declaring Java Button objects
    Button btnSignUp;
    ProgressDialog loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        //Assigning java object to xml edit text fields
        etxtName=findViewById(R.id.etxt_name);
        etxtCell=findViewById(R.id.etxt_cell);
        etxtPassword=findViewById(R.id.etxt_password);

        //Assigning java object to xml edit text fields
        btnSignUp=findViewById(R.id.btn_signup);

        //set click listener in Sign up button
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sign_up();

            }
        });
    }




    private void sign_up() {


        //Getting values from edit texts
        final String name = etxtName.getText().toString().trim();
        final String cell = etxtCell.getText().toString().trim();
        final String password = etxtPassword.getText().toString().trim();


        //Checking  field/validation
        if (name.isEmpty()) {
            etxtName.setError("Please enter name !");
            etxtName.requestFocus();
        }
        else if (cell.isEmpty()) {

            etxtCell.setError("Please enter cell !");
            etxtCell.requestFocus();

        }

        else if (password.isEmpty()) {

            etxtPassword.setError("Please enter password !");
            etxtPassword.requestFocus();

        }

        else
        {
            loading = new ProgressDialog(this);
            loading.setIcon(R.drawable.wait_icon);
            loading.setTitle("Sign up");
            loading.setMessage("Please wait....");
            loading.show();


            StringRequest stringRequest=new StringRequest(Request.Method.POST, Constant.SIGNUP_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    //for track response in logcat
                    Log.d("RESPONSE", response);

                    if (response.equals("success")) {
                        loading.dismiss();
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        Toast.makeText(SignUpActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    } else if (response.equals("exists")) {

                        Toast.makeText(SignUpActivity.this, "User already exists!", Toast.LENGTH_SHORT).show();
                        loading.dismiss();

                    }

                    else if (response.equals("failure")) {

                        Toast.makeText(SignUpActivity.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                        loading.dismiss();

                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(SignUpActivity.this, "No Internet Connection or \nThere is an error !!!", Toast.LENGTH_LONG).show();
                            loading.dismiss();
                        }
                    }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request

                    params.put(Constant.KEY_NAME, name);
                    params.put(Constant.KEY_CELL, cell);
                    params.put(Constant.KEY_PASSWORD, password);


                    Log.d("info",""+name+" "+cell);

                    //returning parameter
                    return params;
                }
            };


            //Adding the string request to the queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }





}
