package com.quizwiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import java.util.Map;

public class TwoPlayerResult extends ActionBarActivity {
    String gameId;
    String player1Str;
    String player2Str;
    String playerType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_player_result);
        setActionBar("Results");
        Intent intent = getIntent();
        //get game id
        if (intent.getStringExtra("GameId") != null) {
            gameId = intent.getStringExtra("GameId");
        }
        if (intent.getStringExtra("player1") != null) {
            player1Str = intent.getStringExtra("player1");
        }
        if (intent.getStringExtra("player2") != null) {
            player2Str = intent.getStringExtra("player2");
        }
        if (intent.getStringExtra("playerType") != null) {
            playerType = intent.getStringExtra("playerType");
        }

        TextView player1 = (TextView) findViewById(R.id.player1ResultName);
        TextView player2 = (TextView) findViewById(R.id.player2ResultName);
        final TextView player1Points = (TextView) findViewById(R.id.player1ResultPoints);
        final TextView player2Points = (TextView) findViewById(R.id.player2ResultPoints);
        final TextView Summary = (TextView) findViewById(R.id.Summary);

        player1.setText(player1Str);
        player2.setText(player2Str);

        // to update the points of other player
        final Firebase gameRef1 = new Firebase(getString(R.string.FireBaseDBReference)+"/Game/"+gameId+"/game");
        gameRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if(snapshot.getValue()!=null) {
                    // store the values in map structure
                    Map<String, Object> gameReq = (Map<String, Object>) snapshot.getValue();
                    int p1Points=0,p2Points=0;
                    //if(playerType.equals("player1")
                    if(gameReq.get("player2Points")!=null) {
                        String p2pointsStr=gameReq.get("player2Points").toString();
                        p2Points=Integer.parseInt(p2pointsStr);
                        Log.d("Result Player2 points",p2pointsStr);
                        player2Points.setText("Points:" + p2pointsStr);
                    }

                    if(gameReq.get("player1Points")!=null) {
                        String p1pointsStr = gameReq.get("player1Points").toString();
                        p1Points=Integer.parseInt(p1pointsStr);;
                        Log.d("Result Player1 points",p1pointsStr);
                        player1Points.setText("Points:"+p1pointsStr);
                    }
                    if(p1Points==p2Points)
                    {
                        Summary.setText("Its a TIE !!");
                    }
                    else if(p1Points>p2Points)
                    {
                        if(playerType.equals("player1"))
                        {
                            Summary.setText("Yaay.!!.You Won :D ");
                        }
                        if(playerType.equals("player2"))
                        {
                            Summary.setText("OOPs.!!.You Lost :D ");
                        }
                    }

                }
                else
                {
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

    public void HomeButton(View v){
        Intent in =new Intent(TwoPlayerResult.this,HomePage.class);
        String username,ukey;

        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        username = prefs.getString("uname", "No name defined");//"No name defined" is the default value.

        //SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        ukey = prefs.getString("ukey", "No name defined");//"No name defined" is the default value.

        in.putExtra("username", username);
        in.putExtra("userKey", ukey);
        startActivity(new Intent(TwoPlayerResult.this,HomePage.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_two_player_result, menu);
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
}
