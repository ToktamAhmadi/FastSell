package com.example.toktam.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class AllAdsActivity extends MainActivity {

  TextView not_found;
  int location=0;
  int category=0;
  String search_key="";
  RecyclerView recyclerView;
  DataAdapter dataAdapters;
  public int last_id=0;
  TextView location_filter;
  TextView category_filter;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //setContentView(R.layout.activity_all_ads);
    FrameLayout content_frame = (FrameLayout) findViewById (R.id.content_frame);
    getLayoutInflater().inflate(R.layout.activity_all_ads,content_frame);
    not_found=(TextView)findViewById(R.id.not_found);
//---------------------------------------------
    recyclerView = (RecyclerView) findViewById(R.id.recycler_ads);
    recyclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
    recyclerView.setLayoutManager(layoutManager);

    RecyclerView.ItemAnimator itemAnimator = new SlideInUpAnimator();
    itemAnimator.setAddDuration(1000);
    itemAnimator.setRemoveDuration(1000);
    recyclerView.setItemAnimator(itemAnimator);

    final ArrayList<JSONObject> data_list = new ArrayList<JSONObject>();
    dataAdapters = new DataAdapter(getApplicationContext(), data_list) {

      @Override
      public void load_more() {
        if (last_id != -1) {
          getJson();
        }
      }
    };
    recyclerView.setAdapter(dataAdapters);
  /**********************************************************/
    getJson();
    /************************************************************/
    final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById (R.id.swip);
    assert swipeRefreshLayout != null;
    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        not_found.setVisibility(View.GONE);
        last_id = 0;
        dataAdapters.clear_list();
        getJson();
        swipeRefreshLayout.setRefreshing(false);
      }
    });
//------------------------------------------------------------------------------------
    location_filter = (TextView) findViewById (R.id.location_filter);
    String[] province_list=getResources().getStringArray(R.array.province);
    province_list[0]="همه ایران";
    location_filter.setText(province_list[preferences.getInt("location_filter",0)]);
    location_filter.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final ArrayList<String> list = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.province)));
        list.set(0,"همه ایران");
        AlertDialog.Builder builder = new AlertDialog.Builder(AllAdsActivity.this);
        builder.setAdapter(new ArrayAdapter<String>(AllAdsActivity.this, R.layout.item_list, R.id.txt_list, list),
          new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              if (!location_filter.getText().toString().equals(list.get(which))) {
                location_filter.setText(list.get(which));
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("location_filter", which);
                editor.apply();
                last_id = 0;
                dataAdapters.clear_list();
                not_found.setVisibility(View.GONE);
                getJson();
              }            }
          });
        builder.show();
      }
    });
//--------------------------------------------------------------------------------------------
    category_filter = (TextView) findViewById (R.id.category_filter);
    String[] category_list=getResources().getStringArray(R.array.category);
    category_list[0]="همه گروه ها";
    category_filter.setText(category_list[preferences.getInt("category_filter",0)]);
    category_filter.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        final ArrayList<String> list = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.category)));
        list.set(0," همه گروه ها");

        AlertDialog.Builder builder = new AlertDialog.Builder(AllAdsActivity.this);
        builder.setAdapter(new ArrayAdapter<String>(AllAdsActivity.this, R.layout.item_list, R.id.txt_list, list),
          new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              if (!category_filter.getText().toString().equals(list.get(which))) {
                category_filter.setText(list.get(which));
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("category_filter", which);
                editor.apply();
                last_id = 0;
                dataAdapters.clear_list();
                not_found.setVisibility(View.GONE);
                getJson();
              }
            }
          });
        builder.show();
      }
    });
  }//onCreat
/*****************************************************************************************************/
 @Override
 public void onResume(){
   super.onResume();
   last_id=0;
   dataAdapters.clear_list();
   getJson();
 }
 /*****************************************************/
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    getMenuInflater().inflate(R.menu.search_menu, menu);

    MenuItem searchitem=menu.findItem(R.id.action_search);

    if (searchitem!=null){
      SearchView searchview=(SearchView) MenuItemCompat.getActionView(searchitem);
      TextView searchtext=(TextView)searchview.findViewById(android.support.v7.appcompat.R.id.search_src_text);
      //searchtext.setTypeface(myfont);

      searchview.setQueryHint("جستجو ...");

      searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
          search_key=query;
          last_id =0;
          dataAdapters.clear_list();
         getJson();
          return false;
        }
        @Override
        public boolean onQueryTextChange(String newText) {
          if (newText.equals(""))
          {
            search_key="";
            last_id =0;
            dataAdapters.clear_list();
            getJson();
          }
          return false;
        }
      });
    }
    return true;
  }
  /********************************************************/
  private void getJson() {
   // String MY_URL="http://192.168.1.2/fastsell/test2.php";
    String MY_URL="http://10.0.2.2:80/fastsell/test2.php";
    JSONObject json = new JSONObject();
    try {
      json.put("command", "all_ad");
      json.put("location_filter",preferences.getInt("location_filter", 0));
      json.put("category_filter",preferences.getInt("category_filter", 0));
      json.put("search_key", search_key);
      json.put("last_id", last_id);
      json.put("user_id", preferences.getString("mobile", null));

    } catch (JSONException e) {
      e.printStackTrace();
    }
   MyJsonArrayRequest request = new MyJsonArrayRequest(Request.Method.POST,MY_URL,json,
      new Response.Listener<JSONArray>() {

        @Override
        public void onResponse(JSONArray response) {

          if (response != null) {
           // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

            if (last_id == 0 && response.length() == 0) {
              not_found.setVisibility(View.VISIBLE);

            }
            if (response.length() != 0) {
              not_found.setVisibility(View.GONE);
             // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

              try {
                last_id = response.getJSONObject(response.length() - 1).getInt("id");

              } catch (JSONException e) {
                e.printStackTrace();
              }
              if (response.length() != 10) {
                last_id = -1;
              }

            } else {
              last_id = -1;
            }
            dataAdapters.insert(dataAdapters.getItemCount(), response);
          } else {
            Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_LONG).show();
        }
        }
      }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
      }
    });
    AppController.getInstance().addToRequestQueue(request);
  }
/**************************************************************************************/
    public void floatingNewAd_setOnClickListener(View view) {
        if (preferences.contains("mobile"))

        {
            Intent i = new Intent(getApplicationContext(), NewAdsActivity.class);
            startActivity(i);
        }
        else
        {
          MDToast mdToast = MDToast.makeText(getBaseContext(),"برای درج آگهی باید  ثبت نام کنید.",MDToast.LENGTH_LONG,MDToast.TYPE_WARNING);
          mdToast.setIcon(R.drawable.ic_warning);
          mdToast.show();
          Intent i=new Intent(getApplicationContext(),LoginActivity.class);
          startActivity(i);
        }
    }
    /************************************************************/
}
