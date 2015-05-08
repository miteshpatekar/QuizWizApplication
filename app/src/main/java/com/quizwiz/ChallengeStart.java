package com.quizwiz;

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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.quizwiz.R;

import java.util.HashMap;
import java.util.Map;

public class ChallengeStart extends ActionBarActivity {

    String gameId;
    String player1Str,player2Str;
    String category=null;
    String playerType=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_start);
        setActionBar("Challenge");
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);

        Intent intent=getIntent();
        //get game id
        if(intent.getStringExtra("GameId")!=null) {
            gameId = intent.getStringExtra("GameId");
        }

        if(intent.getStringExtra("category")!=null) {
            category = intent.getStringExtra("category");
        }
        Log.d("Categoruuuuu",category);

        // get playertype
        if(intent.getStringExtra("player")!=null)
        {
            playerType=intent.getStringExtra("player");
        }

        final Button startBtn=(Button)findViewById(R.id.startBtn);
        startBtn.setEnabled(false);
        startBtn.setClickable(false);
        // hide button if its player2


        if(playerType.equals("player2"))
            startBtn.setVisibility(View.INVISIBLE);


       // final String uname = prefs.getString("uname", "No name defined");//"No name defined" is the default value.
        final TextView player1= (TextView)findViewById(R.id.player1);
        final TextView player2= (TextView)findViewById(R.id.player2);
        final TextView player1Host= (TextView)findViewById(R.id.player1Host);
        final TextView player2Joined= (TextView)findViewById(R.id.player2Joined);

        // get the value of the friend challenged
        SharedPreferences prefs1 = getSharedPreferences("challengeFriend", MODE_PRIVATE);
        final String unameChallenger = prefs.getString("challengeFriend", "No name defined");//"No name defined" is the default value.


        final Firebase gameRef = new Firebase(getString(R.string.FireBaseDBReference)+"/Game/"+gameId+"/game");

        gameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.getValue()!=null) {
                    // store the values in map structure
                    Map<String, Object> gameReq = (Map<String, Object>) snapshot.getValue();
                    //Game g=ga;
                   // Log.d("values is Player1 ",gameReq.get("player1").toString());
                    if(gameReq.get("player1")!=null) {
                        player1Str=gameReq.get("player1").toString();
                        Log.d("player111",player1Str);
                        player1.setText(gameReq.get("player1").toString());
                        player1Host.setVisibility(View.VISIBLE);
                    }

                    if(gameReq.get("player2")!=null) {
                        player2Str=gameReq.get("player2").toString();
                        player2.setText(gameReq.get("player2").toString());
                        Log.d("player222",player2Str);
                        startBtn.setEnabled(true);
                        startBtn.setClickable(true);
                        player2Joined.setVisibility(View.VISIBLE);
                    }

                    if(gameReq.get("gameStatus")!=null) {
                       // Log.d("afterrrrNewwww value",gameReq.toString());
                        if (gameReq.get("gameStatus").equals("Started") ) {
                            Log.d("afterrrrNewww valuessss",gameReq.get("gameStatus").toString());
                            Intent in = new Intent(ChallengeStart.this, TwoPlayerQuiz.class);
                            in.putExtra("GameId",gameId);
                            in.putExtra("category",category);
                            in.putExtra("player",playerType);
                            gameRef.removeEventListener(this);
                            startActivity(in);
                        }
                    }
                   // Log.d("Gamesss are",gameReq.toString());

                   /* for (Map.Entry<String, Game> entry : gameReq.entrySet()) {
                      //  String key = entry.getKey(); // gets the key of object
                        Log.d("Gamesss obj are",entry.getValue().toString());
                        Game g=(Game)entry.getValue();
                        Log.d("Valuesss are is", entry.getValue().toString());
                        Log.d("values is Player1 ",g.getPlayer1());
                        Log.d("values is Player2",g.getPlayer1());
                    }*/
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

public void startGameButton(View v){
  //  Game gm1=new Game()
    Game gm=new Game(player1Str,player2Str,"Started",category);
    Log.d("GameIDiiiii44444",gameId);
    Log.d("GameIDiiiiiOBj",gm.getCategory()+" "+gm.getGameStatus());
    Firebase gameRef = new Firebase(getString(R.string.FireBaseDBReference)+"/Game/"+gameId);

    Map<String, Game> gamesup = new HashMap<String, Game>();
    gamesup.put("game",gm);
    gameRef.setValue(gamesup);
    Intent in=new Intent(ChallengeStart.this, TwoPlayerQuiz.class);
    in.putExtra("GameId",gameId);
    in.putExtra("category",category);
    in.putExtra("player",playerType);
    startActivity(in);
}

}
