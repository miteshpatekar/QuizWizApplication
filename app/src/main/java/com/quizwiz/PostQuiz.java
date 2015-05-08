package com.quizwiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class PostQuiz extends ActionBarActivity {

    Firebase ref=null;
    TextView header;
    String category;
    EditText postQuestion;
    EditText postOptionA;
    EditText postOptionB;
    EditText postOptionC;
    EditText postOptionD;
    String question="";
    String optionA="";
    String optionB="";
    String optionC="";
    String optionD="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_quiz);
        setActionBar("Post Question");
        header=(TextView)findViewById(R.id.postHeader);
        postQuestion=(EditText)findViewById(R.id.editText6);
        postOptionA=(EditText)findViewById(R.id.editText7);
        postOptionB=(EditText)findViewById(R.id.editText8);
        postOptionC=(EditText)findViewById(R.id.editText9);
        postOptionD=(EditText)findViewById(R.id.editText10);
        Intent i=getIntent();
        category=i.getStringExtra("category");
        header.setText("Posting question in: "+category);

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

    public void onPost(View view){
        Intent i2=new Intent(PostQuiz.this,PreviewQuestion.class);
        question=postQuestion.getText().toString();
        optionA=postOptionA.getText().toString();
        optionB=postOptionB.getText().toString();
        optionC=postOptionC.getText().toString();
        optionD=postOptionD.getText().toString();

        if(optionA.equals("")||optionB.equals("")||optionC.equals("")||optionD.equals("")){
            Toast.makeText(this, "Fill out all fields before previewing", Toast.LENGTH_SHORT).show();
        }
        else {
            i2.putExtra("category", category);
            i2.putExtra("question", question);
            i2.putExtra("optionA", optionA);
            i2.putExtra("optionB", optionB);
            i2.putExtra("optionC", optionC);
            i2.putExtra("optionD", optionD);
            startActivity(i2);
        }
    }
}
