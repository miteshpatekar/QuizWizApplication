package com.quizwiz;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


public class InitialLoginActivity extends ActionBarActivity {
    private static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initital_login);

       //getSupportActionBar().setTitle("Hello world App");
        setActionBar("Quiz Wiz");
    }

    public void onRegister(View view) {
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    public void onLogin(View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    public void onGuest(View view) {
        Intent intent = new Intent(this,HomePage.class);
        intent.putExtra("Guest","GuestUser");
        startActivity(intent);
    }


    public void setActionBar(String heading) {
        // TODO Auto-generated method stub
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.actionbarcustom, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.header)));
        mTitleTextView.setText(heading);
        actionBar.setCustomView(mCustomView);
        actionBar.setDisplayShowCustomEnabled(true);
    }
}
