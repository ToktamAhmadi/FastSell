package com.example.toktam.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class BookmarkAdsListActivity extends MainActivity {
    /******************/
    DataAdapter dataAdapters;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    String search_key="";
    TextView not_found;

    /*********************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_my_ads);
        FrameLayout content_frame = (FrameLayout) findViewById (R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_bookmark_ads_list,content_frame);
        /**********/
        not_found=(TextView)findViewById(R.id.not_found_bookmark);
/**************/
        if(!preferences.contains("mobile"))
        {
            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }
        /***********************/
        getJson();
        setUpToolbar();
        //-----------------------------
        recyclerView = (RecyclerView) findViewById(R.id.recycler_bookmark);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator itemAnimator =new SlideInRightAnimator();
        itemAnimator.setAddDuration(2000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        ArrayList<JSONObject> data_list=new ArrayList<JSONObject>();
        dataAdapters = new DataAdapter(getApplicationContext(), data_list) {
            @Override
            public void load_more() {
            }
        };
        /*****/

        recyclerView.setAdapter(dataAdapters);
        /************************************************/
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swip_bookmark);
        assert swipeRefreshLayout != null;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                not_found.setVisibility(View.GONE);
                dataAdapters.clear_list();
                getJson();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }//oncreat
    /*******************************/
    public void setUpToolbar()
    {
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("آگاهی های مورد علاقه من");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    /******************************************/
//search
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
                    dataAdapters.clear_list();
                    getJson();
                    return false;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    if (newText.equals(""))
                    {
                        search_key="";
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

        //String MY_URL = "http://10.0.2.2:80/fastsell/getbookmark_without_search.php";
        String MY_URL = "http://10.0.2.2:80/fastsell/getbookmark.php";
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("command", "get_bookmark_ad_list");
            jsonParams.put("user_id",preferences.getString("mobile",null));
            jsonParams.put("search_key", search_key);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyJsonArrayRequest request = new MyJsonArrayRequest(Request.Method.POST, MY_URL, jsonParams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response!=null) {
                              if (response.length() == 0) {

                               not_found.setVisibility(View.VISIBLE);
                            }

                            else if (response.length() != 0) {

                                not_found.setVisibility(View.GONE);
                                dataAdapters.insert(dataAdapters.getItemCount(), response);
                            }


                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "مشکلی رخ داده است", Toast.LENGTH_LONG).show();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }
}
