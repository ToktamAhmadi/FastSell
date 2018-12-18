package com.example.toktam.myapplication;

import android.graphics.drawable.Drawable;

/**
 * Created by Apayron on 7/14/2018.
 */
public class DataADs {

  private int id;
  private Drawable image;
  private String title;
  private String price;
  private String location;
  private String date;

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  public int getId() {
    return id;
  }
//----------------------------------------------

  public void setId(int id) {
    this.id = id;
  }

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

  public String getTitle() {
    return title;
  }
//-----------------------------------------------------

  public void setTitle(String title) {
    this.title = title;
  }
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

  public String getPrice() {
    return price;
  }
//-----------------------------------------------------

  public void setPrice(String price) {
    this.price = price;
  }
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

  public String getLocation() {
    return location;
  }
//------------------------------------------------------

  public void setLocation(String location) {
    this.location = location;
  }

  //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  public String getDate() {
    return date;
  }
//---------------------------------------------------

  public void setDate(String date) {
    this.date = date;
  }

  //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  public Drawable getImage() {
    return image;
  }

  //----------------------------------------------------
  public void setImage(Drawable image) {
    this.image = image;
  }


//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
}
