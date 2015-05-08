package com.quizwiz;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


public class Categories extends ActionBarActivity {

    String category="";
    String activity="quiz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        setActionBar("Categories");
        Intent intent=getIntent();
        if(intent.getStringExtra("activity")!=null) {
            activity = intent.getStringExtra("activity");
        }
        Log.d("Categories",activity);
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

    public void onCS(View v)
    {
        Intent i;
        category="CS";
        if (activity.equals("quiz")) {
            i = new Intent(Categories.this, QuizQuestions.class);
        }
        else if (activity.equals("challenge")){
            i = new Intent(Categories.this, SelectChallenger.class);
        }
        else{
            i = new Intent(Categories.this, PostQuiz.class);
        }
        i.putExtra("category",category);
        startActivity(i);
    }
    public void onGK(View v)
    {
        Intent i;
        category="General Knowledge";
        if (activity.equals("quiz")) {
            i=new Intent(Categories.this, QuizQuestions.class);
        }
        else if (activity.equals("challenge")){
            i = new Intent(Categories.this, SelectChallenger.class);
        }
        else{
            i = new Intent(Categories.this, PostQuiz.class);
        }
        i.putExtra("category",category);
        startActivity(i);
    }
    public void onMovies(View v)
    {
        Intent i;
        category="Movies";
        if (activity.equals("quiz")) {
            i=new Intent(Categories.this, QuizQuestions.class);
        }
        else if (activity.equals("challenge")){
            i = new Intent(Categories.this, SelectChallenger.class);
        }
        else{
            i = new Intent(Categories.this, PostQuiz.class);
        }
        i.putExtra("category",category);
        startActivity(i);
    }
}