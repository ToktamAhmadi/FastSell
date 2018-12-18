package com.example.toktam.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import com.theartofdev.edmodo.cropper.CropImage;
import com.valdesekamdem.library.mdtoast.MDToast;

public class NewAdsActivity extends MainActivity {

    DataAdapter dataAdapter;
    ArrayList<ImageView> images = new ArrayList<ImageView>();
    int current_image = 0;
    JSONObject jsonObject;
    Uri uri;
    EditText title_text;
    TextInputLayout title_layout;
    EditText description_text;
    TextInputLayout description_layout;
    EditText district_text;
    TextInputLayout district_layout;
    EditText phone_text;
    TextInputLayout phone_layout;
    Spinner spinner_province;
    Spinner spinner_city;
    Spinner spinner_category;
    TextView category_error;
    TextView province_error;
    TextView city_error;
    Spinner spinner_price_type;
    TextInputLayout price_layout;
    EditText price_text;
    Button btn_submit;
    Boolean[] fill_images = new Boolean[5];
    String[] address_images = new String[5];
    String image_base64;
    String oprator;
    JSONObject json;
    /***************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_new_ads);
        FrameLayout content_frame = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_new_ads, content_frame);
        /***********/
        setUpToolbar();
 /**************/
        if(!preferences.contains("mobile"))
        {
            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }
/************************************************************/
Bundle bundle =getIntent().getExtras();
//String obj= bundle.getString("item.bundle");

        if (bundle != null && bundle.containsKey("item.bundle"))
        {
            oprator="Edit";
            try {
                String obj = bundle.getString("item.bundle");
                json=new JSONObject(obj);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            oprator="insert";
        }
/*****************************************************/
        images.add((ImageView) findViewById(R.id.image_view1));
        images.add((ImageView) findViewById(R.id.image_view2));
        images.add((ImageView) findViewById(R.id.image_view3));
        images.add((ImageView) findViewById(R.id.image_view4));
        images.add((ImageView) findViewById(R.id.image_view5));
        title_text = (EditText) findViewById(R.id.title_text);
        title_layout = (TextInputLayout) findViewById(R.id.title_layout);
        description_text = (EditText) findViewById(R.id.discription_text);
        description_layout = (TextInputLayout) findViewById(R.id.dis_layout);
        district_text = (EditText) findViewById(R.id.district_text);
        district_layout = (TextInputLayout) findViewById(R.id.district_layout);
        phone_text = (EditText) findViewById(R.id.phone_text);
        phone_layout = (TextInputLayout) findViewById(R.id.phone_layout);
        spinner_province = (Spinner) findViewById(R.id.spinner_province);
        spinner_city = (Spinner) findViewById(R.id.spinner_city);
        spinner_category = (Spinner) findViewById(R.id.spinner_category);
        category_error = (TextView) findViewById(R.id.category_error);
        province_error = (TextView) findViewById(R.id.province_error);
        city_error = (TextView) findViewById(R.id.city_error);
        price_layout = (TextInputLayout) findViewById(R.id.price_layout);
        price_text = (EditText) findViewById(R.id.price_text);
        spinner_price_type = (Spinner) findViewById(R.id.spinner_price_type);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        final EditText title_text = (EditText) findViewById(R.id.title_text);
        title_text.requestFocus();
 /*********************************************************************/

        if(oprator.equals("Edit"))
            try {
            //imageView1
            if (json.getString("image1").equals("")) {
                current_image = 0;
                fill_images[current_image] = false;
                address_images[current_image] = "";// jsonObject.getString("img");

            } else if (!json.getString("image1").equals("")) {
                current_image = 0;
                fill_images[current_image] = true;
                address_images[current_image] = json.getString("image1");
                Glide.with(getApplicationContext())
                        .load("http://10.0.2.2:80/fastsell/" + json.getString("image1"))
                        .apply(new RequestOptions().override(128, 128))
                        .into(images.get(current_image));

            }
            Toast.makeText(getApplicationContext(), fill_images[current_image].toString(), Toast.LENGTH_LONG).show();

            /***********/
            //imageView2
            if (json.getString("image2").equals("")) {
                current_image = 1;
                fill_images[current_image] = false;
                address_images[current_image] = "";// jsonObject.getString("img");

            } else if (!json.getString("image2").equals("")) {
                current_image = 1;
                fill_images[current_image] = true;
                address_images[current_image] = json.getString("image2");
                Glide.with(getApplicationContext())
                        .load("http://10.0.2.2:80/fastsell/" + json.getString("image2"))
                        .apply(new RequestOptions().override(128, 128))
                        .into(images.get(current_image));
            }
            /*****/
            //imageView1
            if (json.getString("image3").equals("")) {
                current_image = 2;
                fill_images[current_image] = false;
                address_images[current_image] = "";// jsonObject.getString("img");

            } else if (!json.getString("image3").equals("")) {
                current_image = 2;
                fill_images[current_image] = true;
                address_images[current_image] = json.getString("image3");
                Glide.with(getApplicationContext())
                        .load("http://10.0.2.2:80/fastsell/" + json.getString("image3"))
                        .apply(new RequestOptions().override(128, 128))
                        .into(images.get(current_image));

            }
            /*****/
            //imageView1
            if (json.getString("image4").equals("")) {
                current_image = 3;
                fill_images[current_image] = false;
                address_images[current_image] = "";// jsonObject.getString("img");

            } else if (!json.getString("image4").equals("")) {
                current_image = 3;
                fill_images[current_image] = true;
                address_images[current_image] = json.getString("image4");
                Glide.with(getApplicationContext())
                        .load("http://10.0.2.2:80/fastsell/" + json.getString("image4"))
                        .apply(new RequestOptions().override(128, 128))
                        .into(images.get(current_image));

            }
            /*****/
            //imageView1
            if (json.getString("image5").equals("")) {
                current_image = 4;
                fill_images[current_image] = false;
                address_images[current_image] = "";// jsonObject.getString("img");

            } else if (!json.getString("image5").equals("")) {
                current_image = 4;
                fill_images[current_image] = true;
                address_images[current_image] = json.getString("image5");
                Glide.with(getApplicationContext())
                        .load("http://10.0.2.2:80/fastsell/" + json.getString("image5"))
                        .apply(new RequestOptions().override(128, 128))
                        .into(images.get(current_image));

            }
            title_text.setText(json.getString("title"));
            description_text.setText(json.getString("description"));
            spinner_price_type.setSelection(json.getInt("price_type"));
//if(json.getInt("price_type")==3){
            //   price_text.setVisibility(View.VISIBLE);
//    price_text.setText(json.getInt("price"));}
            spinner_province.setSelection(json.getInt("province"));
            spinner_city.setSelection(json.getInt("city"));
            spinner_category.setSelection(json.getInt("category"));
            phone_text.setText(json.getString("phone"));
            district_text.setText(json.getString("adress"));
            //-----------------
        } catch (JSONException e) {
            e.printStackTrace();
        }
            /**********************************/
/********************************/
//imageView1
        images.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_image = 0;
//                Toast.makeText(getApplicationContext(),fill_images[current_image].toString(),Toast.LENGTH_LONG).show();

                if (!fill_images[current_image]) {
                    showDialog();
                } else {
                    show_delete_dialog();
                }


            }
        });
        //-------------------
        //imageView2
        images.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_image = 1;
                if (!fill_images[current_image]) {
                    showDialog();
                } else {
                    show_delete_dialog();
                }
            }
        });
        //-------------------
        //imageView3
        images.get(2).setOnClickListener(new View.OnClickListener() {
            //(String[])
            @Override
            // Toast.makeText(getApplicationContext(),(String(images.get(2))),Toast.LENGTH_LONG).show();
            public void onClick(View v) {
                current_image = 2;
                if (!fill_images[current_image]) {
                    showDialog();
                } else {
                    show_delete_dialog();
                }
            }
        });
        //-------------------
        //imageView4
        images.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                current_image = 3;
                if (!fill_images[current_image]) {
                    showDialog();
                } else {
                    show_delete_dialog();
                }
            }
        });
        //-------------------
        //imageView5
        images.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_image = 4;
                if (!fill_images[current_image]) {
                    showDialog();
                } else {
                    show_delete_dialog();
                }
            }
        });
        //-----------------
        Arrays.fill(fill_images, false);
        Arrays.fill(address_images, "");
/*************************************/
//spinner_city
        ArrayAdapter<CharSequence> myadapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.province, R.layout.item_list);
        spinner_province.setAdapter(myadapter);
        spinner_province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<CharSequence> myadapter = ArrayAdapter.createFromResource(getApplicationContext(), getResources().getIdentifier("array/city" + spinner_province.getSelectedItemPosition(), null, getApplicationContext().getPackageName()), R.layout.item_list);
                spinner_city.setAdapter(myadapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
//-----------------------------------
        //spinner)_category
        myadapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.category, R.layout.item_list);
        spinner_category.setAdapter(myadapter);
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
// ----------------------------
        //spinner_price
        myadapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.price, R.layout.item_list);
        spinner_price_type.setAdapter(myadapter);
        spinner_price_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner_price_type.getSelectedItemPosition() == 3) {
                    price_layout.setVisibility(View.VISIBLE);
                }//if
                else {
                    price_layout.setVisibility(View.GONE);
                }//else
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
               // price_layout.setVisibility(View.GONE);
            }
        });
     //--------------------------
        //btn_submit
 btn_submit.setOnClickListener(new View.OnClickListener()
    {
       @Override
       public void onClick(View v) {
       boolean is_validate = true;
       //--------
       //title
        if (title_text.getText().toString().trim().length() >= 5) {
        title_layout.setErrorEnabled(false);
        }//if_title
        else {
         title_layout.setError(" لطفا برای متن آگهی تیتر را وارد کنید. ");
         title_layout.setErrorEnabled(true);
         is_validate = false;
          }//else_title
         //------------------
          //discription
          if (description_text.getText().toString().trim().length() >= 10) {
             description_layout.setErrorEnabled(false);
          }//if_description
           else {
           description_layout.setError(" لطفا توضیحات آگهی را وارد کنید ");
           description_layout.setErrorEnabled(true);
           is_validate = false;
           }//else_description
           //-------------------
           //district
           if (district_text.getText().toString().trim().length() >= 3) {
                   district_layout.setErrorEnabled(false);
           }//if_district
           else {
           district_layout.setError(" لطفا آدرس را وارد نمایید ");
           district_layout.setErrorEnabled(true);
           is_validate = false;
            }//else_district
            //--------------------
            //phone
            if (phone_text.getText().toString().trim().length() == 11) {
            phone_layout.setErrorEnabled(false);
            }//if_phone
            else {
            phone_layout.setError(" لطفا شماره تلفن را صحیح وارد کنید");
            phone_layout.setErrorEnabled(true);
            is_validate = false;
            }//else_phone
            //--------------------------
            //spinner_category
            if (spinner_category.getSelectedItemPosition() == 0) {
            category_error.setText(" لطفا یکی از گزینه های بالا را انتخاب کنید ");
            is_validate = false;
            }//if_category
            else {
            category_error.setText("");
            }//else_category
           //--------------------
           //spinner_province
           if (spinner_province.getSelectedItemPosition() == 0) {
               province_error.setText(" لطفا یکی از گزینه های بالا را انتخاب کنید ");
               is_validate = false;
           }//if
           else {
               province_error.setText("");
           }//els
            //-----------
            //city
          if (spinner_city.getSelectedItemPosition() == 0) {
          city_error.setText(" لطفا یکی از گزینه های بالا را انتخاب کنید ");
          is_validate = false;
            }//if_city
            else {
          city_error.setText("");
           //is_validate = true;

          }//else_city
           /***************/
          //price
         if (spinner_price_type.getSelectedItemPosition() == 3) {
             if(price_text.getText().toString().trim().length()==0||price_text.getText().toString().trim().length()>=12||Integer.parseInt(price_text.getText().toString())<=0)
             {
                 price_layout.setError(" قیمت را درست وارد نمایید.");
          price_layout.setErrorEnabled(true);
           is_validate = false;
           }//if_price_text
             else  {
                 price_layout.setErrorEnabled(false);
               }//else_price_text

           }//if_spinner_price
              else {
               price_layout.setErrorEnabled(false);

                 }//else_spinner_price

                                              //--------------------------------------------------------------------------------------
                                             if (is_validate) {
                                                  jsonObject = new JSONObject();
                                                  try {
                                                      //-----------
                                                      //if image1
                                                      if (fill_images[0] == true) {
                                                          jsonObject.put("image1", address_images[0]);
                                                      }//if image1
                                                      else {
                                                          jsonObject.put("image1", "");
                                                      }//else image1
                                                      //-------------
                                                      //if image2
                                                      if (fill_images[1] == true) {
                                                          jsonObject.put("image2", address_images[1]);
                                                      } else {
                                                          jsonObject.put("image2", "");
                                                      }
                                                      //-----------------
                                                      //if image3
                                                      if (fill_images[2] == true) {
                                                          jsonObject.put("image3", address_images[2]);
                                                      } else {
                                                          jsonObject.put("image3", "");
                                                      }
                                                      //--------------
                                                      //if image4
                                                      if (fill_images[3] == true) {
                                                          jsonObject.put("image4", address_images[3]);
                                                      } else {
                                                          jsonObject.put("image4", "");
                                                      }
                                                      //------------
                                                      //if image5
                                                      if (fill_images[4] == true) {
                                                          jsonObject.put("image5", address_images[4]);
                                                      } else {
                                                          jsonObject.put("image5", "");
                                                      }
                                                      //-----------
 jsonObject.put("user_id",preferences.getString("mobile",null));
 jsonObject.put("title", title_text.getText().toString().trim());
 jsonObject.put("phone",phone_text.getText().toString().trim());
 jsonObject.put("description", district_text.getText().toString().trim());
 jsonObject.put("category", spinner_category.getSelectedItemPosition());
 jsonObject.put("province", spinner_province.getSelectedItemPosition());
 jsonObject.put("city", spinner_city.getSelectedItemPosition());
 jsonObject.put("adress", district_text.getText().toString().trim());
 jsonObject.put("price_type", spinner_price_type.getSelectedItemPosition());


 //-----------------------------
                                                      //price

                                                      if (spinner_price_type.getSelectedItemPosition() == 3) {
                                                          jsonObject.put("price", Integer.parseInt(price_text.getText().toString().trim()));
                                                      } else {
                                                          jsonObject.put("price", 0);
                                                      }

                                                      //price
                                                      //-------------------------------
                                                      if(oprator.equals("insert")) {
                                                          jsonObject.put("command", "new_ad");
                                                      }
                                                      else
                                                      {
                                                          jsonObject.put("ad_id", json.getInt("id"));

                                                          jsonObject.put("command", "edit_my_ad");
                                                      }
                                                  } catch (JSONException e) {
                                                      e.printStackTrace();
                                                  }
                                                  SendJson();

                                              }
                                              else
                                              {
                                                  MDToast mdToast = MDToast.makeText(getBaseContext(),"خطا در ورود اطلاعات",MDToast.LENGTH_LONG,MDToast.TYPE_ERROR);
                                                  mdToast.setIcon(R.drawable.ic_error);
                                                  mdToast.show();
                                              }

                                          }
                                      });
//-------------------
    }//onCreat
/***************************************/

        public void showDialog() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("انتخاب عکس از گالری");
       // list.add("گرفتن عکس با دوربین");


        AlertDialog.Builder builder = new AlertDialog.Builder(NewAdsActivity.this);

        builder.setAdapter(new ArrayAdapter<String>(NewAdsActivity.this, R.layout.item_list, R.id.txt_list, list)
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {//gallery
                            Intent gallery_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(Intent.createChooser(gallery_intent, "لطفا یک عکس را انتخاب کنید"), 2);
                        } else {//camera
                            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File file = new File(Environment.getExternalStorageDirectory(), "file" + String.valueOf(System.currentTimeMillis() + ".jpg"));
                            uri = Uri.fromFile(file);
                            camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            camera_intent.putExtra("return-data", true);
                            startActivityForResult(camera_intent, 1);
                        }
                    }
                });
        builder.show();
    }

    /*******************************************************/

    public void show_delete_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewAdsActivity.this);
        builder.setMessage("آیا مطمئن به حذف عکس هستید ؟");
        builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                images.get(current_image).setImageResource(R.drawable.ic_add_a_photo);
                fill_images[current_image] = false;
            }
        });
        builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /*******************************************************/

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {//camera
            CropImage.activity(uri).setAspectRatio(1, 1).setRequestedSize(512, 512).start(this);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {//gallery
            uri = data.getData();
            CropImage.activity(uri).setAspectRatio(1, 1).setRequestedSize(512, 512).start(this);
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri resutlUri = result.getUri();
            images.get(current_image).setImageURI(resutlUri);
            fill_images[current_image] = true;
            BitmapDrawable bd = ((BitmapDrawable) images.get(current_image).getDrawable());
            Bitmap bm = bd.getBitmap();
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 90, bao);
            image_base64 = Base64.encodeToString(bao.toByteArray(), Base64.DEFAULT);
            UploadImg();

        }
    }

    /***************************************/
    private void UploadImg() {
        String MY_URL = "http://10.0.2.2:80/fastsell/testimage.php";
        JSONObject json = new JSONObject();
        try {
            json.put("command", "uploadimage");
            json.put("image", image_base64);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            address_images[current_image] = response;
                        } else {
                            Toast.makeText(getApplicationContext(), "مشکلی رخ داده است", Toast.LENGTH_LONG).show();
                            //images.setImageResource(R.drawable.noimage);
                            fill_images[current_image] = false;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            public byte[] getBody() throws AuthFailureError {

                return json.toString().getBytes();
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    /**************************************/
   public void setUpToolbar()
  {
    Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
toolbar.setTitle("ثبت آگهی");
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
  }

    //________________________________________________________________________________________
//help in toolbar
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.help_menu, menu);
        return true;
    }
    */
  /********************************************************/
private void SendJson() {
    String MY_URL="http://10.0.2.2:80/fastsell/test.php";

    StringRequest stringRequest = new StringRequest(Request.Method.POST,MY_URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response!=null) {
                        //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        //NewAdsActivity.this.finish();
                        MDToast mdToast = MDToast.makeText(getBaseContext(),"اطلاعات با موفقیت درج شد",MDToast.LENGTH_LONG,MDToast.TYPE_SUCCESS);
                        mdToast.setIcon(R.drawable.ic_check_ok);
                        mdToast.show();
                        startActivity(new Intent(NewAdsActivity.this,AllAdsActivity.class));

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

            return jsonObject.toString().getBytes();
        }
    };

    AppController.getInstance().addToRequestQueue(stringRequest);
}

}//class





