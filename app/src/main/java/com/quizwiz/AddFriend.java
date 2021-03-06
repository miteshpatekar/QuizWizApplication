package com.quizwiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.quizwiz.R;

import java.util.HashMap;
import java.util.Map;

public class AddFriend extends ActionBarActivity {

    Firebase ref=null;
    Firebase userMapRef=null;
    Map<String, Boolean> reqNameList = null;

    public void setMap(Map<String, Boolean> req){
        this.reqNameList=req;

    }

    public Map<String, Boolean> getMap(){
        return reqNameList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        setActionBar("Add Friend");

        ref=new Firebase(getString(R.string.FireBaseDBReference)+"/User");
        userMapRef=new Firebase(getString(R.string.FireBaseDBReference)+"/UserMap");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_friend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    public void AddFriendBtn(View v)
    {
        // get session state username
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        final String uname = prefs.getString("uname", "No name defined");//"No name defined" is the default value.

        // get session state useruserKey
        final String ukey = prefs.getString("ukey", "No name defined");//"No name defined" is the default value.


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });

        final EditText edtext=(EditText)findViewById(R.id.AddFriendText);
        final String unameFriend=edtext.getText().toString();
        Log.d("username ", uname + " " + unameFriend);

        userMapRef.child(unameFriend).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                // store the values in map structure
                if(snapshot.getValue()==null)
                {
                    AlertDialog alert = builder.setMessage("Username "+unameFriend+ " doesnt exists !").create();
                    alert.show();
                }
                else {
                    // get the key of friend user
                    Log.d("yesssssss", snapshot.getValue().toString());
                    final String userFriendKey=snapshot.getValue().toString();


                    ref.child(userFriendKey).child(unameFriend).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() == null) {
                                //AlertDialog alert = builder.setMessage(unameFriend+" doesnt exist!").create();
                                //alert.show();
                            } else {
                                ref.child(userFriendKey).child(unameFriend).child("requestList").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        // store the values in map structure
                                        Map<String, Boolean> requests = (Map<String, Boolean>) snapshot.getValue();
                                        if (requests == null) {
                                            Map<String, Boolean> requestName = new HashMap<String,Boolean>();
                                            requestName.put(uname, false);
                                            ref.child(userFriendKey).child(unameFriend).child("requestList").setValue(requestName);
                                        }
                                        else
                                        {
                                            requests.put(uname, false);
                                            ref.child(userFriendKey).child(unameFriend).child("requestList").setValue(requests);
                                        }

                                        // show alert

                                        AlertDialog alert = builder.setMessage("Friend Request Sent to "+unameFriend).create();
                                        alert.show();
                                        edtext.setText("");

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

                        }
                    });




                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });












        Log.d("string afterrr","kkkkk");
        Map<String, Boolean> re=getMap();


        //reqName[0].put(uname, false);
        //ref.child(unameFriend).child("requestList").setValue(reqName[0]);
        //Toast.makeText(this,"Friend Request sent to "+ unameFriend,Toast.LENGTH_SHORT).show();
    }

}
