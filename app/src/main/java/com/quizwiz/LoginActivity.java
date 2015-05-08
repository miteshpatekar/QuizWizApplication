package com.quizwiz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;


public class LoginActivity extends ActionBarActivity {

    Firebase reference = null;
    Firebase userMapRef = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        setActionBar("Login");
        Firebase.setAndroidContext(this);
        reference =new Firebase(getString(R.string.FireBaseDBReference)+"/User");
        userMapRef = new Firebase(getString(R.string.FireBaseDBReference)+"/UserMap");
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
        mTitleTextView.setText("Quiz Wiz");
        actionBar.setCustomView(mCustomView);
        actionBar.setDisplayShowCustomEnabled(true);
    }

    public void onLogin(View view) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });


        Context context = view.getContext();
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected == true) {

            final Intent intent = new Intent(this, HomePage.class);

            EditText loginUname = (EditText) findViewById(R.id.username);
            final String username = loginUname.getText().toString();

            EditText loginPwd = (EditText) findViewById(R.id.password);
            final String pwd = loginPwd.getText().toString();

            userMapRef.child(username).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    // store the values in map structure
                    if (snapshot.getValue() == null) {
                        AlertDialog alert = builder.setMessage("Username " + username + " doesnt exists !").create();
                        alert.show();
                    } else {
                        Log.d("yesssssss", snapshot.getValue().toString());
                        final String userKey = snapshot.getValue().toString();

                        reference.child(snapshot.getValue().toString()).child(username).child("password").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {

                                // store the values in map structure
                                if (snapshot.getValue() == null) {

                                } else {
                                    if (snapshot.getValue().toString().equals(pwd)) {
                                        intent.putExtra("username", username);
                                        intent.putExtra("userKey", userKey);
                                        startActivity(intent);
                                    } else {
                                        AlertDialog alert = builder.setMessage("Incorrect Password..!").create();
                                        alert.show();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                System.out.println("The read failed: " + firebaseError.getMessage());
                            }
                        });

                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("The read failed: " + firebaseError.getMessage());
                }
            });
        }else
        {
            AlertDialog alert = builder.setMessage("No Network Connectivity. Please Check your Network Settings").create();
            alert.show();
        }

    }

}
