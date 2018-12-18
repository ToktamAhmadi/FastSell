package com.example.toktam.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;


import ss.com.bannerslider.Slider;
import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.event.OnSlideClickListener;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class ShowDetailsAdActivity extends MainActivity {
    JSONObject item;
    TextView title;
    TextView price;
    TextView location;
    TextView category;
    TextView description;
    TextView address;
    ArrayList<JSONObject> data_list;
    Menu menu;
    Button btn_delete_ad;
    Button btn_edit_ad;
    LinearLayout linear_delete_edit;
    /*************************************/
    private static int permission_request_code = 111;
/***************************/
TextView txt_bookmark;
TextView txt_visibility;
TextView txt_time;
/*************************/
private static final String BOOKMARKS_PREFERENCES = "bookmarks_preferences";
    boolean isBookmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_show_details_ad);
        FrameLayout content_frame = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_show_details_ad, content_frame);
        /***********************/
        try {
            item = new JSONObject(getIntent().getStringExtra("ad"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /************************/
        title = (TextView) findViewById(R.id.ad_title);
        description = (TextView) findViewById(R.id.ad_description);
        price = (TextView) findViewById(R.id.ad_price);
        location = (TextView) findViewById(R.id.ad_location);
        category = (TextView) findViewById(R.id.ad_category);
        address = (TextView) findViewById(R.id.ad_address);
        btn_delete_ad = (Button) findViewById(R.id.btn_delete_ad);
        btn_edit_ad = (Button) findViewById(R.id.btn_edit_ad);
        linear_delete_edit = (LinearLayout) findViewById(R.id.linear_delete_edit);
        txt_bookmark=(TextView) findViewById(R.id.txt_bookmark);
        txt_visibility=(TextView) findViewById(R.id.txt_visibility);
        txt_time=(TextView) findViewById(R.id.txt_time);
        /***************************/
        setUpToolbar();
        visibility();
        countbookmark();
        setUpSliderImage();
        /***********************/
        if (preferences.contains("mobile")) {
            try {
                if (item.getString("user_id").equals(preferences.getString("mobile", null))) {
                    linear_delete_edit.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        /************************/
        try {
            title.setText(item.getString("title"));
            description.setText(item.getString("description"));
            address.setText(item.getString("adress"));

            if (item.getInt("price_type") == 0) {
                price.setText("");
            }

            if (item.getInt("price_type") == 1) {
                price.setText("قیمت توافقی");
            }


            if (item.getInt("price_type") == 2) {
                price.setText("جهت معاوضه");
            }

            if (item.getInt("price_type") == 3) {

                NumberFormat numberformat = NumberFormat.getNumberInstance(Locale.US);

                price.setText(numberformat.format(item.getInt("price")) + " تومان ");
            }
            String[] province_list = getResources().getStringArray(R.array.province);
            location.setText("استان " + province_list[item.getInt("province")]);
            ArrayAdapter<CharSequence> myadapter = ArrayAdapter.createFromResource(getApplicationContext(), getApplicationContext().getResources().getIdentifier("array/city" + item.getInt("province"), null, getApplicationContext().getPackageName()), R.layout.item_list);
            location.setText(location.getText() + " , " + myadapter.getItem(item.getInt("city")));
            String[] category_list = getResources().getStringArray(R.array.category);
            category.setText(category_list[item.getInt("category")]);
            //-----------------
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*************************************/
        btn_delete_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                show_delete_ads();
            }
        });
        /*****************************/
        btn_edit_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent i = new Intent(ShowDetailsAdActivity.this, NewAdsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                String obj=item.toString();
                i.putExtra("item",obj);
                startActivity(i);
                */
                Intent i = new Intent(ShowDetailsAdActivity.this, NewAdsActivity.class);
                Bundle bundle = new Bundle();
                String obj=item.toString();
                bundle.putString("item.bundle",obj);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        /*****************************************/
            try {
            int myTimestamp = (int) (item.getInt("date"));
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND,
                tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date currenTimeZone=new java.util.Date((long)myTimestamp*1000);

        String[] parts = sdf.format(currenTimeZone).split("\\-"); // String array, each element is text between dots

        int year=Integer.parseInt(parts[0]);
        int month=Integer.parseInt(parts[1]);
        int day=Integer.parseInt(parts[2]);
        JalaliCalendar.gDate jalalidate=new JalaliCalendar.gDate(year,month,day);
        JalaliCalendar.gDate  jalali=JalaliCalendar.MiladiToJalali(jalalidate);
        txt_time.setText("" + jalali);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*********************************************/
    }//onCreat

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_bookmark_menu, menu);
        this.menu = menu;

           // SharedPreferences preferences = getSharedPreferences(BOOKMARKS_PREFERENCES, MODE_PRIVATE);
          //  isBookmark = preferences.getBoolean("itemId",false);
            //Toast.makeText(getApplicationContext(),item.getBoolean("bookmark")+"   "+isBookmark,Toast.LENGTH_LONG).show();

            //if ( isBookmark ) {
                try {

                    if (item.getBoolean("bookmark")) {
                    menu.findItem(R.id.bookmark).setIcon(R.drawable.ic_favorite_bookmark);
                } else {
                    menu.findItem(R.id.bookmark).setIcon(R.drawable.ic_favorite_border);

                }
                    return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
           // }
          /*  else{
                menu.findItem(R.id.bookmark).setIcon(R.drawable.ic_favorite_border);
            }
*/
        return true;
    }
    //-------------------------------------------------------------------------------------
    //select share and bookmark icon  in toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.share) {    //select share icon
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            try {
                intent.putExtra(Intent.EXTRA_SUBJECT, item.getString("title"));
                intent.putExtra(Intent.EXTRA_TEXT, "این آگهی رو توی زود بخر و بفروش ببین : " + item.getString("title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            startActivity(Intent.createChooser(intent, "یک برنامه رو به دلخواه خودت انتخاب کن"));
        }//if

        if (menuItem.getItemId() == R.id.bookmark) { //selecte bookmark icon
            if (!preferences.contains("mobile")) {
                Toast.makeText(getBaseContext(), "شما  حساب کاربری ایجاد نکرده اید", Toast.LENGTH_SHORT).show();
            } else {
                CheckBookmark();
            }
        }//if

        countbookmark();
        return super.onOptionsItemSelected(menuItem);
    }

    /**************************/
    //initialize every view
    public void setUpSliderImage() {
        SliderLayout slider = (SliderLayout) findViewById(R.id.slider_image_ad);
        try {
            if (item.getString("image1").trim().equals("") && item.getString("image2").trim().equals("") && item.getString("image3").trim().equals("") && item.getString("image4").trim().equals("") && item.getString("image5").trim().equals("")) {

                DefaultSliderView sliderview = new DefaultSliderView(this);

                sliderview.image(R.drawable.icon);
                slider.addSlider(sliderview);
            } else {
                if (!item.getString("image1").trim().equals("")) {
                    DefaultSliderView sliderview = new DefaultSliderView(this);

                    sliderview.image("http://10.0.2.2:80/fastsell/" + item.getString("image1").trim());
                    slider.addSlider(sliderview);
                }
                if (!item.getString("image2").trim().equals("")) {
                    DefaultSliderView sliderview = new DefaultSliderView(this);

                    sliderview.image("http://10.0.2.2:80/fastsell/" + item.getString("image2").trim());
                    slider.addSlider(sliderview);
                }
                if (!item.getString("image3").trim().equals("")) {
                    DefaultSliderView sliderview = new DefaultSliderView(this);

                    sliderview.image("http://10.0.2.2:80/fastsell/" + item.getString("image3").trim());
                    slider.addSlider(sliderview);
                }
                if (!item.getString("image4").trim().equals("")) {
                    DefaultSliderView sliderview = new DefaultSliderView(this);
                    sliderview.image("http://10.0.2.2:80/fastsell/" + item.getString("image4").trim());
                    slider.addSlider(sliderview);
                }
                if (!item.getString("image5").trim().equals("")) {
                    DefaultSliderView sliderview = new DefaultSliderView(this);

                    sliderview.image("http://10.0.2.2:80/fastsell/" + item.getString("image5").trim());
                    slider.addSlider(sliderview);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Top);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(500);
       // slider.addOnPageChangeListener(ShowDet.this);
        slider.stopAutoCycle();
    }

    //-------------------------------------------------------------------------------------
    public void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("جزییات آگهی");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable arrow = getResources().getDrawable(R.drawable.ic_arrow_back);
        getSupportActionBar().setHomeAsUpIndicator(arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDetailsAdActivity.this.finish();
            }
        });
    }
    /*********************************/
    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == permission_request_code) {

            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getApplicationContext(), "permission denied. closing application", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    /****************************/
    public void floatingCall_setOnClickListener(View view) {

        String mobile = "";

        try {
            mobile = item.getString("phone");


            if (mobile != "") {
                mobile = " ( " + item.getString("phone") + " ) ";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final ArrayList<String> list = new ArrayList<String>();
        //list.add("تماس تلفنی" + mobile);
        list.add("ارسال پیامک" + mobile);


        AlertDialog.Builder builder = new AlertDialog.Builder(ShowDetailsAdActivity.this);

        builder.setAdapter(new ArrayAdapter<String>(ShowDetailsAdActivity.this, R.layout.item_list, R.id.txt_list, list)
                , new DialogInterface.OnClickListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {//call
                            try {
                                Intent callintent = new Intent(Intent.ACTION_CALL);
                                callintent.setData(Uri.parse("tel:" + item.getString("phone")));
                                startActivity(callintent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (i == 1) {//sms


                            try {
                                Intent smsintent = new Intent(Intent.ACTION_SENDTO);
                                smsintent.setData(Uri.parse("sms:" + item.getString("phone")));
                                startActivity(smsintent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        builder.show();
    }

    /******************************/
    public void show_delete_ads() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShowDetailsAdActivity.this);
        builder.setMessage("آیا می خواهید آگهی را پاک کنید؟");

        builder.setPositiveButton("بله، حذف می کنم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Delete_ads_Json();
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

    //-------------------------------------------------------------------------------------
    public void CheckBookmark() {
        String MY_URL = "http://10.0.2.2:80/fastsell/bookmark.php";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("command", "bookmark_ads");
            jsonObject.put("user_id", preferences.getString("mobile", null));
            jsonObject.put("ad_id", item.getInt("id"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        /*******************************************/
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            //Toast.makeText(getBaseContext(),response, Toast.LENGTH_SHORT).show();
                          //  SharedPreferences.Editor editor = getSharedPreferences(BOOKMARKS_PREFERENCES, MODE_PRIVATE).edit();
                            try {
                                if (item.getBoolean("bookmark")) {

                                    item.put("bookmark", false);
                                    MDToast mdToast = MDToast.makeText(getBaseContext(),"آگهی با موفقیت از لیست نشان گذاری حذف شد",MDToast.LENGTH_LONG,MDToast.TYPE_SUCCESS);
                                    mdToast.setIcon(R.drawable.ic_check_ok);
                                    mdToast.show();
                                    menu.findItem(R.id.bookmark).setIcon(R.drawable.ic_favorite_border);
                                   // editor.putBoolean("itemId", false);
                                } else {
                                    item.put("bookmark", true);
                                    MDToast mdToast = MDToast.makeText(getBaseContext(),"آگهی با موفقیت به لیست نشان گذاری اضافه شد",MDToast.LENGTH_LONG,MDToast.TYPE_SUCCESS);
                                    mdToast.setIcon(R.drawable.ic_check_ok);
                                    mdToast.show();
                                    menu.findItem(R.id.bookmark).setIcon(R.drawable.ic_favorite_bookmark);
                                  //  editor.putBoolean("itemId", true);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//editor.apply();
                        } else {

                            Toast.makeText(getApplicationContext(), "مشکلی رخ داده است", Toast.LENGTH_LONG).show();


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

                return jsonObject.toString().getBytes();
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    /****************************************************************************/
    private void Delete_ads_Json() {
//
        String MY_URL = "http://10.0.2.2:80/fastsell/deletAds.php";
        JSONObject json_delete = new JSONObject();
        try {
            json_delete.put("command", "delete_ad");
            json_delete.put("user_id", preferences.getString("mobile", null));
            json_delete.put("ad_id", item.getInt("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, MY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            MDToast mdToast = MDToast.makeText(getBaseContext(),"حدف با موفقیت انجام شد",MDToast.LENGTH_LONG,MDToast.TYPE_SUCCESS);
                            mdToast.setIcon(R.drawable.ic_check_ok);
                            mdToast.show();
                            startActivity(new Intent(ShowDetailsAdActivity.this, AllAdsActivity.class));
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

                return json_delete.toString().getBytes();
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
/***************************************/
public  void visibility()
{

    String MY_URL = "http://10.0.2.2:80/fastsell/visibility.php";

    JSONObject json_visibility = new JSONObject();
    try {
        json_visibility.put("command", "change_visibility");
        json_visibility.put("ad_id", item.getInt("id"));
        json_visibility.put("visibility",item.getInt("visibility")+1);

    } catch (JSONException e) {
        e.printStackTrace();
    }
/*******************************************/
    StringRequest stringRequest = new StringRequest(Request.Method.POST, MY_URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response != null) {
                         txt_visibility.setText(response);
                    } else {
                        Toast.makeText(getApplicationContext(), "مشکلی رخ داده است", Toast.LENGTH_LONG).show();
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

            return json_visibility.toString().getBytes();
        }
    };

    AppController.getInstance().addToRequestQueue(stringRequest);

}
    /***************************************/
    public  void countbookmark()
    {

        String MY_URL = "http://10.0.2.2:80/fastsell/countbookmark.php";
        /******************************************/
        JSONObject json_countbookmark = new JSONObject();
        try {
            json_countbookmark.put("command", "count_selected_bookmark");
            json_countbookmark.put("ad_id", item.getInt("id"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
/*******************************************/
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            txt_bookmark.setText(response);
                        } else {
                            Toast.makeText(getApplicationContext(), "مشکلی رخ داده است", Toast.LENGTH_LONG).show();
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

                return json_countbookmark.toString().getBytes();
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);

    }
}//class
/******************************************************************************************************/
