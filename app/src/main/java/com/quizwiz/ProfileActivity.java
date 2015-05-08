package com.quizwiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;


public class ProfileActivity extends ActionBarActivity {
    Firebase userMapRef=null;
    Firebase ref=null;
    String username;
    TextView profilePointsText;
    TextView profileUsernameText;
    String totalPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar("Profile");
        setContentView(R.layout.profile_activity);
        profilePointsText=(TextView)findViewById(R.id.profilePoints);
        profileUsernameText=(TextView)findViewById(R.id.profileUsername);
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        username = prefs.getString("uname", "No name defined");//"No name defined" is the default value.
        userMapRef=new Firebase(getString(R.string.FireBaseDBReference)+"/UserMap");
        ref=new Firebase(getString(R.string.FireBaseDBReference)+"/User");


        userMapRef.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Map<String, Object> ureq = (Map<String, Object>) snapshot.getValue();
                int pointsInt = 0;
                // store the values in map structure
                if (snapshot.getValue() == null) {
                    // AlertDialog alert = builder.setMessage("Username "+username+ " doesnt exists !").create();
                    // alert.show();
                } else {
                    Log.d("yesssssss", snapshot.getValue().toString());
                    final String userKey = snapshot.getValue().toString();

                    ref.child(userKey).child(username).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            Map<String, Object> ureq = (Map<String, Object>) snapshot.getValue();
                            int pointsInt = 0;
                            // store the values in map structure
                            if (snapshot.getValue() == null) {
                                // AlertDialog alert = builder.setMessage("Username "+username+ " doesnt exists !").create();
                                // alert.show();
                            } else {

                                if (ureq.get("points") != null) {
                                    Map<String, String> pointsReq = (Map<String, String>) ureq.get("points");
                                    String p = pointsReq.get("Points");
                                    profileUsernameText.setText("Username: "+username);
                                    profilePointsText.setText("Total Points: "+p);
                                }

                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            System.out.println("The read failed: " + firebaseError.getMessage());
                        }
                    });

                    /*int total=prevPoints+pointsInt;
                    uname.put("Points", Integer.toString(total));
                    ref.child(userKey).child(username).child("points").setValue(uname);
                    Log.d("username selected is- ", username);
*/                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
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

    public void onLogout(View view) {
        Intent intent = new Intent(this,InitialLoginActivity.class);
        startActivity(intent);
    }


}

