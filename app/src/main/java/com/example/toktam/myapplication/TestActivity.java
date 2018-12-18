package com.example.toktam.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

public class TestActivity extends MainActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FrameLayout content_frame = (FrameLayout) findViewById(R.id.content_frame);
    getLayoutInflater().inflate(R.layout.activity_test, content_frame);

  }
}
