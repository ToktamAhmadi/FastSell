package com.example.toktam.myapplication;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.valdesekamdem.library.mdtoast.MDToast;


public class MainActivity extends AppCompatActivity{

  SharedPreferences preferences;
  DrawerLayout drawerLayout;
  String settings="";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
/********/
    preferences= getSharedPreferences(settings,MODE_PRIVATE);
/**********/
    setUpToolbar();
    setUpNavigationView();
/**********/

}//onCreate
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

  public void setUpToolbar()
  {
    Toolbar toolbar = (Toolbar) findViewById (R.id.toolbar);
    drawerLayout = (DrawerLayout) findViewById (R.id.drawerLayout);
    drawerLayout.closeDrawers();
    toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
    setSupportActionBar(toolbar);
    android.support.v7.app.ActionBar actionBar = getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled (true);
    actionBar.setHomeButtonEnabled (true);
     ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle
      (this,drawerLayout,toolbar,0,0);
    drawerLayout.addDrawerListener(drawerToggle);
    drawerToggle.syncState();
  }
//----------------------------------------------------------------------------------------------
private void setUpNavigationView()
{
  NavigationView navigationView = (NavigationView) findViewById (R.id.navigationView);
  assert navigationView != null;
  navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

      switch (item.getItemId()) {
        case R.id.nav_all_advertisment_home:
          item.setChecked(true);
          startActivity(new Intent(MainActivity.this, AllAdsActivity.class));
          drawerLayout.closeDrawer(Gravity.RIGHT);


          break;
        /******/
        case R.id.nav_regist_advertisement:
          if (preferences.contains("mobile")) {
            startActivity(new Intent(getApplicationContext(), NewAdsActivity.class));
            drawerLayout.closeDrawer(Gravity.RIGHT);

          } else
          {
            MDToast mdToast = MDToast.makeText(getBaseContext(),"برای درج آگهی باید  ثبت نام کنید.",MDToast.LENGTH_LONG,MDToast.TYPE_WARNING);
            mdToast.setIcon(R.drawable.ic_warning);
            mdToast.show();
            Intent i=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
            drawerLayout.closeDrawer(Gravity.RIGHT);
            drawerLayout.clearFocus();
          }

          break;
          /******/
        case R.id.nav_my_advertisement:
          if (preferences.contains("mobile")) {
            startActivity(new Intent(getApplicationContext(), MyADsActivity.class));
            drawerLayout.closeDrawer(Gravity.RIGHT);
          } else
          {
            MDToast mdToast = MDToast.makeText(getBaseContext(),"برای درج آگهی باید  ثبت نام کنید.",MDToast.LENGTH_SHORT,MDToast.TYPE_INFO);
            mdToast.setIcon(R.drawable.ic_error);
            mdToast.show();
            Intent i=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
            drawerLayout.closeDrawer(Gravity.RIGHT);
          }
          break;
          /************/
        case R.id.nav_favorite:
          if (preferences.contains("mobile")) {
            startActivity(new Intent(getApplicationContext(), BookmarkAdsListActivity
                    .class));
            drawerLayout.closeDrawer(Gravity.RIGHT);
          } else
          {
            MDToast mdToast = MDToast.makeText(getBaseContext(),"برای درج آگهی باید  ثبت نام کنید.",MDToast.LENGTH_SHORT,MDToast.TYPE_ERROR);
            mdToast.setIcon(R.drawable.ic_error);
            mdToast.show();
            Intent i=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
            drawerLayout.closeDrawer(Gravity.RIGHT);
          }
          break;
          /************************/
          /*
        case R.id.nav_account:
          if (preferences.contains("mobile")) {
            startActivity(new Intent(getApplicationContext(), AccountActivity.class));
            drawerLayout.closeDrawer(Gravity.RIGHT);

          } else
          {
            Toast.makeText(getBaseContext(),"برای درج آگهی باید یک حساب ماری ایجاد کنید",Toast.LENGTH_SHORT).show();
            Intent i=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
            drawerLayout.closeDrawer(Gravity.RIGHT);
            drawerLayout.clearFocus();
          }

          break;
          */
        /**************************/
        case R.id.nav_share :
          Intent intent = new Intent(Intent.ACTION_SEND);
          intent.setType("text/plain");
          intent.putExtra(Intent.EXTRA_SUBJECT,"fastsell");
          intent.putExtra(Intent.EXTRA_TEXT,"برنامه را نصب کن و از امکاناتش لذت ببر");
          startActivity(Intent.createChooser(intent,"برنامه مورد نظر را انتخاب کن"));
          drawerLayout.closeDrawer(Gravity.RIGHT);
          break;

      }

      return true;
    }
  });

}
//-------------------------------------------------------------------------
 
}//class MainActivity
