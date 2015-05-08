package com.quizwiz;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.quizwiz.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TwoPlayerQuiz extends ActionBarActivity {

    String playerType=null;

    String question="Question";
    String optionA="A";
    String optionB="B";
    String optionC="C";
    String optionD="D";
    String correct="Answer";
    Button op1Button;
    Button op2Button;
    Button op3Button;
    Button op4Button;
    Button correctButton;

    TextView questionText;
    TextView categoryText;
    TextView pointsText;
    TextView qLeftText;
    TextView timerText;
    TextView player1;
    TextView player2;
    TextView player1PointsText;
    TextView player2PointsText;

    Drawable buttonColor;
    boolean aState=false;
    boolean bState=false;
    boolean cState=false;
    boolean dState=false;
    boolean answered=false;

    int points=0, player1Points=0,player2Points=0;
    String total="";
    private Timer timer = new Timer();
    int qTime=11;
    int timerVal=qTime;
    final Handler myHandler = new Handler();

    Question q1;
    Question q2;
    Question q3;
    ArrayList<Question> questionList;
    int qCount=-1;
    int questionCount=1;
    String category="";
    int maxQuestions=5;
    String colorVal="#FFFFFF";

    String gameId=null;
    String player1Str=null;
    String player2Str=null;
    Firebase gameRef;
    int execnt=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

       Log.d("uparka countexec: ",Integer.toString(execnt));
        if(execnt==0) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_two_player_quiz);
            setActionBar("Two Player quiz");
            // get the game id
            Intent intent = getIntent();
            //get game id
            if (intent.getStringExtra("GameId") != null) {
                gameId = intent.getStringExtra("GameId");
            }

            if (intent.getStringExtra("player") != null) {
                playerType = intent.getStringExtra("player");
            }


            if (intent.getStringExtra("category") != null) {
                category = intent.getStringExtra("category");
            }

           //Log.d("Categoryyyyy", category);

            qCount = -1;
            questionText = (TextView) findViewById(R.id.questionText);
            player1 = (TextView) findViewById(R.id.player1Text);
            player2 = (TextView) findViewById(R.id.player2Text);
            player1PointsText = (TextView) findViewById(R.id.player1Points);
            player2PointsText = (TextView) findViewById(R.id.player2Points);

            // categoryText=(TextView)findViewById(R.id.textView13);
            // pointsText=(TextView)findViewById(R.id.textView17);
            //  qLeftText=(TextView)findViewById(R.id.textView18);
            timerText = (TextView) findViewById(R.id.timerText);
            op1Button = (Button) findViewById(R.id.buttonA);
            op2Button = (Button) findViewById(R.id.buttonB);
            op3Button = (Button) findViewById(R.id.buttonC);
            op4Button = (Button) findViewById(R.id.buttonD);
            buttonColor = op1Button.getBackground();
            questionList = new ArrayList<Question>();
            getGame();
            retrieveData();
        }


        // to check each points change of player1
        final Firebase gameRef1 = new Firebase(getString(R.string.FireBaseDBReference)+"/Game/"+gameId+"/game/player1Points");
        gameRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if(snapshot.getValue()!=null) {
                    // store the values in map structure
                    if(playerType.equals("player2")) {
                        String points1 = snapshot.getValue().toString();
                        Log.d("player1Points", points1);
                        int p1points = Integer.parseInt(points1);
                        player1PointsText.setText("Points:" + player1Points);
                      //  if (p1points > player1Points) {
                            player1PointsText.setTextColor(Color.GREEN);
                            player1Points = p1points;
                            // player2PointsText.setText("Points:" + player2Points);
                       // } else {
                            player1PointsText.setTextColor(Color.RED);
                        //}
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });



        // to check each points change of player2
        final Firebase gameRef2 = new Firebase(getString(R.string.FireBaseDBReference)+"/Game/"+gameId+"/game/player2Points");
        gameRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if(snapshot.getValue()!=null) {
                    // store the values in map structure
                    if(playerType.equals("player1")) {
                        String points2 = snapshot.getValue().toString();
                        Log.d("player2Points", points2);
                        int p2points = Integer.parseInt(points2);
                        player2PointsText.setText("Points:" + player2Points);
                        //if (p2points > player2Points) {
                            player2PointsText.setTextColor(Color.GREEN);
                            player2Points = p2points;
                            // player2PointsText.setText("Points:" + player2Points);
                        //} else {
                            player2PointsText.setTextColor(Color.RED);
                        //}
                    }
            }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });




        // to update the points of other player
       /* final Firebase gameRef1 = new Firebase(getString(R.string.FireBaseDBReference)+"/Game/"+gameId+"/game");
        gameRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("nicheka countexec: ",Integer.toString(execnt));
                if(snapshot.getValue()!=null) {
                    // store the values in map structure
                    Map<String, Object> gameReq = (Map<String, Object>) snapshot.getValue();


                    //if(playerType.equals("player1")
                    if(gameReq.get("player2Points")!=null) {
                        String p2pointsStr=gameReq.get("player2Points").toString();
                        int p2points=Integer.parseInt(p2pointsStr);
                        player2PointsText.setText("Points:" + player2Points);
                        if(p2points>player2Points) {
                            player2PointsText.setTextColor(Color.GREEN);
                            player2Points=p2points;
                           // player2PointsText.setText("Points:" + player2Points);
                        }
                        else {
                            player2PointsText.setTextColor(Color.RED);
                        }
                        //Log.d("afterrrr value",gameReq.get("gameStatus").toString());
                    }

                    if(gameReq.get("player1Points")!=null) {
                        String p1pointsStr = gameReq.get("player1Points").toString();
                        int p1points = Integer.parseInt(p1pointsStr);
                        player1PointsText.setText("Points:"+player1Points);
                        if (p1points > player1Points) {
                            player1PointsText.setTextColor(Color.GREEN);
                            player1Points=p1points;

                        } else {
                            player1PointsText.setTextColor(Color.RED);

                        }
                        Log.d("afterrrr value",gameReq.get("gameStatus").toString());
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
*/

    }


    public void getGame(){
        Log.d("getGameCalled","called");
        final Firebase gameRef = new Firebase(getString(R.string.FireBaseDBReference)+"/Game/"+gameId+"/game");

        gameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    // store the values in map structure
                    Map<String, Object> gameReq = (Map<String, Object>) snapshot.getValue();

                    if (gameReq.get("player1") != null) {
                        player1Str = gameReq.get("player1").toString();
                        //Log.d("player111",player1Str);
                        player1.setText(gameReq.get("player1").toString());
                    }

                    if (gameReq.get("player2") != null) {
                        player2Str = gameReq.get("player2").toString();
                        player2.setText(gameReq.get("player2").toString());
                        //   Log.d("player222",player2Str);

                    }

                    if (gameReq.get("gameStatus") != null) {
                        //Log.d("afterrrr value",gameReq.get("gameStatus").toString());
                    }

                } else {

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

    }

    public void retrieveData()
    {
        Log.d("RetrieveDataCalled", "Gathering Values");

       // Firebase.setAndroidContext(this);
        //Intent i=getIntent();
        //category = i.getStringExtra("category");
        //categoryText.setText("Category - "+category);
       // pointsText.setText("Points: " +points);

        Firebase myFirebaseRef = new Firebase("https://popping-heat-8474.firebaseio.com/Questions/"  +category);
        // Firebase rootRef = new Firebase("https://docs-examples.firebaseio.com/web/data");

        //myFirebaseRef.orderByKey();
        myFirebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // store the values in map structure
                Map<String, Object> newPost = (Map<String, Object>) snapshot.getValue();
                //iterate through the list
                for (Map.Entry<String, Object> entry : newPost.entrySet()) {

                    String question = snapshot.child(entry.getKey().toString() + "/QText").getValue().toString();
                    //Log.d("Question", question);
                    String optionA = snapshot.child(entry.getKey().toString() + "/Op1").getValue().toString();
                   // Log.d("OptionA", optionA);
                    String optionB = snapshot.child(entry.getKey().toString() + "/Op2").getValue().toString();
                    //Log.d("OptionB", optionB);
                    String optionC = snapshot.child(entry.getKey().toString() + "/Op3").getValue().toString();
                    //Log.d("OptionC", optionC);
                    String optionD = snapshot.child(entry.getKey().toString() + "/Op4").getValue().toString();
                    //Log.d("OptionD", optionD);
                    String correct = snapshot.child(entry.getKey().toString() + "/Answer").getValue().toString();
                    //Log.d("Answer", correct);

                    Question q = new Question(question, optionA, optionB, optionC, optionD, correct);
                    //Log.d("Question", q.toString());
                    questionList.add(q);
                    questionCount++;
                    //Log.d("QuestionCount", questionCount + "");
                }
              //  Collections.shuffle(questionList);
                if (questionList.size()>maxQuestions) {
                    questionList.subList(maxQuestions, questionList.size()).clear();
                }
                nextQuestion();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    public void nextQuestion()
    {
        qCount++;
        player1PointsText.setTextColor(Color.WHITE);
        player2PointsText.setTextColor(Color.WHITE);

        //pointsText.setText("Points: " +points);
        if ((questionList.size()-qCount)<=0){
        //    qLeftText.setText("Questions Left: 0");
        }
        else {
          //  qLeftText.setText("Questions Left: " + (questionList.size() - qCount - 1));
        }


        if(qCount<questionList.size()) {
            question = questionList.get(qCount).getQuestion().toString();
            correct = questionList.get(qCount).getAnswer().toString();
            optionA = questionList.get(qCount).getOption1().toString();
            optionB = questionList.get(qCount).getOption2().toString();
            optionC = questionList.get(qCount).getOption3().toString();
            optionD = questionList.get(qCount).getOption4().toString();

            questionText.setText(question);
            op1Button.setText(optionA);
            op2Button.setText(optionB);
            op3Button.setText(optionC);
            op4Button.setText(optionD);
            if (op1Button.getText().equals(correct)) {
                correctButton = op1Button;
            } else if (op2Button.getText().equals(correct)) {
                correctButton = op2Button;
            } else if (op3Button.getText().equals(correct)) {
                correctButton = op3Button;
            } else if (op4Button.getText().equals(correct)) {
                correctButton = op4Button;
            }
            op1Button.setBackgroundColor(Color.parseColor(colorVal));
            op2Button.setBackgroundColor(Color.parseColor(colorVal));
            op3Button.setBackgroundColor(Color.parseColor(colorVal));
            op4Button.setBackgroundColor(Color.parseColor(colorVal));
            time();
        }
        else
        {
            Toast.makeText(this, "No More Questions", Toast.LENGTH_SHORT).show();
            total=String.valueOf(points)+"/"+String.valueOf(questionList.size());
            Intent i=new Intent(TwoPlayerQuiz.this,TwoPlayerResult.class);
            i.putExtra("GameId",gameId);
            i.putExtra("player1",player1Str);
            i.putExtra("player2",player2Str);
            i.putExtra("playerType",playerType);
            startActivity(i);
        }

        aState=false;
        bState=false;
        cState=false;
        dState=false;
        answered=false;
        timerVal=qTime;
    }


    public void time() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                UpdateGUI();
            }
        }, 0, 1000);
    }

    private void UpdateGUI() {
        timerVal--;
        myHandler.post(myRunnable);
    }

    final Runnable myRunnable = new Runnable() {
        public void run() {
            if (timerVal==0)
            {
                /*if (correctButton == op1Button) {
                    op1Button.setBackgroundColor(Color.GREEN);
                } else {
                    op1Button.setBackgroundColor(Color.RED);
                }
                if (correctButton == op2Button) {
                    op2Button.setBackgroundColor(Color.GREEN);
                } else {
                    op2Button.setBackgroundColor(Color.RED);
                }
                if (correctButton == op3Button) {
                    op3Button.setBackgroundColor(Color.GREEN);
                } else {
                    op3Button.setBackgroundColor(Color.RED);
                }
                if (correctButton == op4Button) {
                    op4Button.setBackgroundColor(Color.GREEN);
                } else {
                    op4Button.setBackgroundColor(Color.RED);
                }*/
                answered=true;
                timer.cancel();
                nextQuestion();
                //Toast.makeText(TwoPlayerQuiz.this, "Time Up - Incorrect", Toast.LENGTH_SHORT).show();
            }
            if(timerVal<10) {
                String t="0"+String.valueOf(timerVal);
                timerText.setText(String.valueOf(t));
            }
            else {
                timerText.setText(String.valueOf(timerVal));
            }

        }
    };

public void setGame(String player1Str,String player2Str,String status,
                    String category,int player1Points,int player2Points){

    Game gm=new Game(player1Str,player2Str,status,category,player1Points,player2Points);
    Log.d("SetGame Called","called "+gameId+" "+gm.getPlayer1Points() +""+gm.getPlayer2Points());
   // Log.d("GameIDiiiii44444",gameId);
   // Log.d("GameIDiiiiiOBj",gm.getCategory()+" "+gm.getGameStatus());
    Firebase gameRef = new Firebase(getString(R.string.FireBaseDBReference)+"/Game/"+gameId);

    Map<String, Game> gamesup = new HashMap<String, Game>();
    gamesup.put("game",gm);
    gameRef.setValue(gamesup);

}
    public void onClickA(View v)
    {
            execnt++;
        Log.d("clicked countexec: ",Integer.toString(execnt));
        if(aState==false&&answered==false) {
            if (correctButton == op1Button) {
     //           op1Button.setBackgroundColor(Color.GREEN);
       //         op2Button.setBackgroundColor(Color.parseColor(colorVal));
         //       op3Button.setBackgroundColor(Color.parseColor(colorVal));
           //     op4Button.setBackgroundColor(Color.parseColor(colorVal));

                Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
                if(playerType.equals("player1")) {
                    player1PointsText.setTextColor(Color.GREEN);
                    player1Points++;
                    player1PointsText.setText("Points:"+player1Points);
                    setGame(player1Str,player2Str,"Started",category,player1Points,player2Points);
                }
                else
                {
                    player2PointsText.setTextColor(Color.GREEN);
                    player2Points++;
                    player2PointsText.setText("Points:"+player2Points);
                    setGame(player1Str,player2Str,"Started",category,player1Points,player2Points);
                }
               // points++;
            } else {
                //op1Button.setBackgroundColor(Color.RED);
               // correctButton.setBackgroundColor(Color.GREEN);
                if(playerType.equals("player1"))
                    player1PointsText.setTextColor(Color.RED);
                else
                    player2PointsText.setTextColor(Color.RED);
                Toast.makeText(this, "Incorrect", Toast.LENGTH_SHORT).show();
            }
           // pointsText.setText("Points: " +points);
            answered=true;
           // timer.cancel();
        }
        else {
            //Toast.makeText(this, "Question answered. Go to next question", Toast.LENGTH_SHORT).show();
        }
    }
    public void onClickB(View v)
    {
        execnt++;
        Log.d("clicked countexec: ",Integer.toString(execnt));
        if(bState==false&&answered==false) {
            if (correctButton == op2Button) {
              /*  op2Button.setBackgroundColor(Color.GREEN);

                op1Button.setBackgroundColor(Color.parseColor(colorVal));
                op3Button.setBackgroundColor(Color.parseColor(colorVal));
                op4Button.setBackgroundColor(Color.parseColor(colorVal));
*/
                Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
                if(playerType.equals("player1")) {
                    player1PointsText.setTextColor(Color.GREEN);
                    player1Points++;
                    player1PointsText.setText("Points:"+player1Points);
                    setGame(player1Str,player2Str,"Started",category,player1Points,player2Points);
                }
                else
                {
                    player2PointsText.setTextColor(Color.GREEN);
                    player2Points++;
                    player2PointsText.setText("Points:"+player2Points);
                    setGame(player1Str,player2Str,"Started",category,player1Points,player2Points);
                }
            //    points++;
            } else {
              /*  op2Button.setBackgroundColor(Color.RED);
                correctButton.setBackgroundColor(Color.GREEN);*/
                Toast.makeText(this, "Incorrect", Toast.LENGTH_SHORT).show();
                if(playerType.equals("player1"))
                    player1PointsText.setTextColor(Color.RED);
                else
                    player2PointsText.setTextColor(Color.RED);
            }
           // pointsText.setText("Points: " +points);
            answered=true;
           // timer.cancel();
        }
        else {
          //  Toast.makeText(this, "Question answered. Go to next question", Toast.LENGTH_SHORT).show();
        }
    }
    public void onClickC(View v)
    {
        execnt++;
        Log.d("clicked countexec: ",Integer.toString(execnt));
        if(cState==false&&answered==false) {
            if (correctButton == op3Button) {
               /* op3Button.setBackgroundColor(Color.GREEN);
                op1Button.setBackgroundColor(Color.parseColor(colorVal));
                op2Button.setBackgroundColor(Color.parseColor(colorVal));
                op4Button.setBackgroundColor(Color.parseColor(colorVal));*/

                Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
                if(playerType.equals("player1")) {
                    player1PointsText.setTextColor(Color.GREEN);
                    player1Points++;
                    player1PointsText.setText("Points:"+player1Points);
                    setGame(player1Str,player2Str,"Started",category,player1Points,player2Points);
                }
                else
                {
                    player2PointsText.setTextColor(Color.GREEN);
                    player2Points++;
                    player2PointsText.setText("Points:"+player2Points);
                    setGame(player1Str,player2Str,"Started",category,player1Points,player2Points);
                }
                //points++;
               // pointsText.setText("Points: " +points);
            } else {
               // op3Button.setBackgroundColor(Color.RED);
               // correctButton.setBackgroundColor(Color.GREEN);
                Toast.makeText(this, "Incorrect", Toast.LENGTH_SHORT).show();
                if(playerType.equals("player1"))
                    player1PointsText.setTextColor(Color.RED);
                else
                    player2PointsText.setTextColor(Color.RED);
            }
            //pointsText.setText("Points: " +points);
            answered=true;
        //    timer.cancel();
        }
        else {
        //    Toast.makeText(this, "Question answered. Go to next question", Toast.LENGTH_SHORT).show();
        }
    }
    public void onClickD(View v)
    {
        execnt++;
        Log.d("clicked countexec: ",Integer.toString(execnt));
        if(dState==false&&answered==false) {
            if (correctButton == op4Button) {
                /*op4Button.setBackgroundColor(Color.GREEN);
                op1Button.setBackgroundColor(Color.parseColor(colorVal));
                op2Button.setBackgroundColor(Color.parseColor(colorVal));
                op3Button.setBackgroundColor(Color.parseColor(colorVal));
*/
                Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
                if(playerType.equals("player1")) {
                    player1PointsText.setTextColor(Color.GREEN);
                    player1Points++;
                    player1PointsText.setText("Points:"+player1Points);
                    setGame(player1Str,player2Str,"Started",category,player1Points,player2Points);
                }
                else
                {
                    player2PointsText.setTextColor(Color.GREEN);
                    player2Points++;
                    player2PointsText.setText("Points:"+player2Points);
                    setGame(player1Str,player2Str,"Started",category,player1Points,player2Points);
                }
                //points++;
            } else {
               // op4Button.setBackgroundColor(Color.RED);
                //correctButton.setBackgroundColor(Color.GREEN);
                Toast.makeText(this, "Incorrect", Toast.LENGTH_SHORT).show();
                if(playerType.equals("player1"))
                    player1PointsText.setTextColor(Color.RED);
                else
                    player2PointsText.setTextColor(Color.RED);
            }
           // pointsText.setText("Points: " +points);
            answered=true;
            //timer.cancel();
        }
        else{
          //  Toast.makeText(this, "Question answered. Go to next question", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_two_player_quiz, menu);
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
}
