package com.quizwiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.quizwiz.R;

import java.util.HashMap;
import java.util.Map;

public class Results extends ActionBarActivity {

    TextView pointsText;
    String points="";
    Firebase userMapRef=null;
    Firebase ref=null;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        setActionBar("Result");
        userMapRef=new Firebase(getString(R.string.FireBaseDBReference)+"/UserMap");
        ref=new Firebase(getString(R.string.FireBaseDBReference)+"/User");

        pointsText=(TextView)findViewById(R.id.textView16);
        Intent i=getIntent();

        if(i.getStringExtra("username")!=null) {
            username = i.getStringExtra("username");
        }

        if(i.getStringExtra("points")!=null) {
            points = i.getStringExtra("points");
        }

        Log.d("Pointsssss ",points);
        pointsText.setText(points);

        final int prevPoints=Integer.parseInt(points);

        final Map<String, String> uname = new HashMap<String, String>();
        uname.put("Points",points);

        userMapRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
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

                    ref.child(userKey).child(username).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                    pointsInt = Integer.parseInt(p);
                                    Log.d("PrevPoints ", Integer.toString(pointsInt));
                                }

                                int total = prevPoints + pointsInt;
                                uname.put("Points", Integer.toString(total));
                                ref.child(userKey).child(username).child("points").setValue(uname);
                                Log.d("username selected is- ", username);
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
*/
                }
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

    public void onFinish(View v)
    {
        Intent intent = new Intent(this,HomePage.class);
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);

        //SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        String ukey = prefs.getString("ukey", "No name defined");//"No name defined" is the default value.


        intent.putExtra("userKey", ukey);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
