package com.quizwiz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends ActionBarActivity {

    Firebase reference = null;
    Firebase userMapRef = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        setActionBar("Register");
        Firebase.setAndroidContext(this);
        reference =new Firebase(getString(R.string.FireBaseDBReference)+"/User");
        userMapRef = new Firebase(getString(R.string.FireBaseDBReference)+"/UserMap");

//
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

    public void onRegister(View view) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });


        /*EditText regEdittext=(EditText)findViewById(R.id.editText);
        String regEmailAddress=regEdittext.getText().toString();
       */
        Context context = view.getContext();
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected == true) {

            EditText usernameText = (EditText) findViewById(R.id.usernameReg);
            final String usrname = usernameText.getText().toString();

            EditText passwordText = (EditText) findViewById(R.id.passwordReg);
            String password = passwordText.getText().toString();

            EditText passwordConfirmText = (EditText) findViewById(R.id.passwordRegConfirm);
            String passwordConfirm = passwordConfirmText.getText().toString();

            //  EditText regEdittex4 = (EditText)findViewById(R.id.editText11);
            //  String name = regEdittex4.getText().toString();

            if (password.equals(passwordConfirm)) {
                User usr = new User(usrname, null, password);

                Map<String, User> users = new HashMap<String, User>();
                users.put(usrname, usr);

                Firebase newPostRef = reference.push();
                newPostRef.setValue(users);

                final String postId = newPostRef.getKey();
                Log.d(" Entered key is :", postId);

                final Intent intent = new Intent(this, HomePage.class);

                // to make usermap table
                userMapRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // store the values in map structure
                        Map<String, String> usermaps = (Map<String, String>) snapshot.getValue();
                        if (usermaps == null) {
                            Map<String, String> userMapName = new HashMap<String, String>();
                            userMapName.put(usrname, postId);
                            userMapRef.setValue(userMapName);
                        } else {
                            usermaps.put(usrname, postId);
                            userMapRef.setValue(usermaps);
                        }

                        intent.putExtra("username", usrname);
                        intent.putExtra("userKey", postId);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        System.out.println("The read failed: " + firebaseError.getMessage());
                    }
                });

            } else {

                AlertDialog alert = builder.setMessage("Password and Confirm Password do not match..!").create();
                alert.show();
            }
        }else
        {
            AlertDialog alert = builder.setMessage("No Network Connectivity. Please Check your Network Settings").create();
            alert.show();
        }
    }

}
