package com.example.toktam.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends MainActivity {

  EditText mobile_text;
  TextView mobile_error;
  EditText email_text;
  Button submit_btn;
  EditText activation_code;
  Button btn_logint;
  LinearLayout linear_main;
  LinearLayout activation_layout;
  LinearLayout linear_login;
  TextInputLayout email_layout;
  Boolean is_valid=true;
  String mobile;
  String email;
  String code;
  TextView activation_error;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //setContentView(R.layout.activity_login);
    FrameLayout content_frame = (FrameLayout) findViewById (R.id.content_frame);
    getLayoutInflater().inflate(R.layout.activity_login,content_frame);
    Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
    toolbar.setTitle("ثبت نام");
    setUpToolbar();
//------------------------------------------------------
    mobile_text =(EditText)findViewById (R.id.mobile_text);
    mobile_error =(TextView) findViewById (R.id.mobile_error);
    email_text =(EditText)findViewById (R.id.email_text);
    activation_code =(EditText)findViewById (R.id.activation_code);
     email_layout = (TextInputLayout)findViewById(R.id.email_layout);
     submit_btn=(Button)findViewById(R.id.submit_btn);
    btn_logint=(Button)findViewById(R.id.btn_logint);
     linear_main=(LinearLayout)findViewById(R.id.linear_main);
     activation_layout=(LinearLayout)findViewById(R.id.activation_layout);
    linear_login=(LinearLayout)findViewById(R.id.linear_login);
    activation_error =(TextView) findViewById (R.id.activation_error);
    mobile_text.requestFocus();

//----------------------------------------------------------
    submit_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        if(!mobile_text.getText().toString().startsWith("09") || mobile_text.getText().toString().length()!=11)
        {
          is_valid=false;
          mobile_error.setText("شماره موبایل معتبر نیست");
        }else
        {mobile_error.setText("");}
        if(!email_text.getText().toString().trim().equals("") )
        {}
        else
        {email_text.setText("");}
        /****************************************************/
        if(is_valid)
        {
          mobile=mobile_text.getText().toString();
          if(!email_text.getText().toString().equals("")) {
            email = email_text.getText().toString();
          }
          else
          {
            email="";
          }
          CheckUser();
        }
      }
    });
    /*************************************************/

    btn_logint.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(activation_code.getText().toString().length()==4)
        {
          activation_error.setText("");
          code=activation_code.getText().toString();
          CheckCode();
        }
        else
        {
          activation_error.setText("کدفعالسازی صحیح نمی باشد");
        }
      }
    });
    /*************************************************/
  }//onCreat
  /*********************************************************************/

  public void setUpToolbar()

  {
    Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setTitle("ایجاد حساب کاربری");
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
  }

  //--------------------------------------------------------------------
//exit in toolbar
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    getMenuInflater().inflate(R.menu.exit_menu, menu);

    return true;

  }
//------------------------------------------------------------



/*************************************************/
public void CheckUser () {
  String MY_URL = "http://10.0.2.2:80/fastsell/login.php";
  final JSONObject json = new JSONObject();
  try {
  json.put("command", "send_activation_key");
  json.put("mobile", mobile);
  } catch (JSONException e) {
  e.printStackTrace();
  }
  StringRequest stringRequest = new StringRequest(Request.Method.POST,MY_URL,
  new Response.Listener<String>() {
@Override
public void onResponse(String response) {
  if(response!=null) {
    linear_login.setVisibility(View.GONE);
    activation_layout.setVisibility(View.VISIBLE);
    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
    activation_code.requestFocus();
  }
  else
  {
  Toast.makeText(getApplicationContext(), "مشکلی رخ داده است", Toast.LENGTH_LONG).show();
  }

  }

  }, new Response.ErrorListener() {
@Override
public void onErrorResponse(VolleyError error) {
  Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
  }
  }){

@Override
public byte[] getBody() throws AuthFailureError {

  return json.toString().getBytes();
  }
  };

  AppController.getInstance().addToRequestQueue(stringRequest);
  }
/***************************************/
public void CheckCode() {
  String MY_URL = "http://10.0.2.2:80/fastsell/apply.php";
  final JSONObject json = new JSONObject();
  try {
  json.put("command","apply_activation_key");
  json.put("activation_key", Integer.parseInt(code));
  json.put("mobile",mobile);
  json.put("email",email);
  } catch (JSONException e) {
  e.printStackTrace();
  }

  StringRequest stringRequest = new StringRequest(Request.Method.POST,MY_URL,
  new Response.Listener<String>() {
@Override
public void onResponse(String response) {
  if(response!=null) {
    /********************************/
  if(response.equals("ok")) {
    /*****/
    SharedPreferences.Editor editor = preferences.edit();
    editor.putString("mobile", mobile);
    editor.apply();
    /*****/
    Toast.makeText(getApplicationContext(), "کاربر عزیز خوش آمدید", Toast.LENGTH_LONG).show();
    Intent i = new Intent(LoginActivity.this, AllAdsActivity.class);
    startActivity(i);
    LoginActivity.this.finish();
  }
    else {
    Toast.makeText(getApplicationContext(), "اشتباه می باشد", Toast.LENGTH_LONG).show();
  }

  }

    /********************************/
  }

  }, new Response.ErrorListener() {
@Override
public void onErrorResponse(VolleyError error) {
  Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
  }
  }){

@Override
public byte[] getBody() throws AuthFailureError {

  return json.toString().getBytes();
  }
  };

  AppController.getInstance().addToRequestQueue(stringRequest);
  }


/***************************************/
  }
