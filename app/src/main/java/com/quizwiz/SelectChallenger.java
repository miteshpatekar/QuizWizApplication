package com.quizwiz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectChallenger extends ActionBarActivity {
    String uname=null;
    String ukey=null;
    Firebase ref=null;
    Firebase userMapRef=null;
    String category=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_challenger);
        Firebase.setAndroidContext(this);
        setActionBar("Select Challenger");
        Intent intent=getIntent();
        //get game id
        if(intent.getStringExtra("category")!=null) {
            category = intent.getStringExtra("category");
        }

        ref=new Firebase(getString(R.string.FireBaseDBReference)+"/User");
        userMapRef=new Firebase(getString(R.string.FireBaseDBReference)+"/UserMap");

        // get the current Username
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        uname = prefs.getString("uname", "No name defined");//"No name defined" is the default value.
        // get the current user key
        ukey=prefs.getString("ukey", "No name defined");
        Log.d("userrnnnnn",uname);

        // to have a list of challengers
        final ListView listview = (ListView) findViewById(R.id.listView);
        final ArrayList<String> list = new ArrayList<String>();

        // to bind the list of challengers to UI
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);

        // Get a reference to our friends List
        final Firebase ref = new Firebase(getString(R.string.FireBaseDBReference)+"/User/");


        ref.child(ukey).child(uname).child("friendsList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                // store the values in map structure
                Map<String, Boolean> friendsList = (Map<String, Boolean>) snapshot.getValue();
                //iterate through the list
                if (friendsList != null) {
                    for (Map.Entry<String, Boolean> entry : friendsList.entrySet()) {
                        String key = entry.getKey(); // gets the key of object
                        Log.d("sssssffffffff", key);
                        list.add(key);
                        listview.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String unameChallenger = (String) parent.getItemAtPosition(position);
                SharedPreferences.Editor editor = getSharedPreferences("challengeFriend", MODE_PRIVATE).edit();
                editor.putString("challengeFriend", unameChallenger); //set username in shared session variable
                editor.commit();

                final Intent i=new Intent(SelectChallenger.this,ChallengeStart.class);

                // Toast.makeText(this,"name is "+item,Toast.LENGTH_SHORT).show();

                // create game
                final Firebase refGame = new Firebase(getString(R.string.FireBaseDBReference)+"/Game/");
                Game quizgame= new Game(uname,"Created",category);
                Map<String, Game> games = new HashMap<String, Game>();
                games.put("game",quizgame);
                Firebase newPostRef = refGame.push();
                newPostRef.setValue(games);
                final String gameId = newPostRef.getKey();
                Log.d(" Entered key is :" , gameId);

                // pass gameid in intent
                i.putExtra("GameId",gameId);
                i.putExtra("player","player1");
                i.putExtra("category",category);

               // getQuestionsForGame(gameId,category);

                // Save value in Join Friend
                // set in his own list
                //Map<String, String> reqFriend = new HashMap<String, String>();
                //reqFriend.put(unameChallenger,gameId);
                // add in joinRequest List
                //ref.child(ukey).child(uname).child("joinRequest").setValue(reqFriend);


                // set in challengers list
                final Map<String, String> challengeFriend = new HashMap<String, String>();
                challengeFriend.put(uname,gameId);

                userMapRef.child(unameChallenger).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        // store the values in map structure
                        if(snapshot.getValue()==null)
                        {
                           // AlertDialog alert = builder.setMessage("Username "+username+ " doesnt exists !").create();
                           // alert.show();
                        }
                        else {
                            Log.d("yesssssss", snapshot.getValue().toString());
                            final String userKey=snapshot.getValue().toString();

                            ref.child(userKey).child(unameChallenger).child("joinRequest").setValue(challengeFriend);
                            Log.d("username selected is- ",unameChallenger);
                            startActivity(i);

                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        System.out.println("The read failed: " + firebaseError.getMessage());
                    }
                });

            }
        });
    }



    public void getQuestionsForGame(int gameId,String category)
    {

        Firebase myFirebaseRef = new Firebase("https://popping-heat-8474.firebaseio.com" + "Questions/"+  category);
        final Firebase questGameRef = new Firebase("https://popping-heat-8474.firebaseio.com" + "GameQuestions/" );

        // Firebase rootRef = new Firebase("https://docs-examples.firebaseio.com/web/data");

        final Query queryRef = myFirebaseRef.orderByKey();
        //myFirebaseRef.orderByKey();
        myFirebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // store the values in map structure
                ArrayList<Question> questionList =new ArrayList<Question>();;
                int questionCount=0;
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
                    Map<String, Question> quest = new HashMap<String, Question>();
                    quest.put("Q1", q);
                    questGameRef.setValue(quest);
                    questionList.add(q);
                    questionCount++;
                    //Log.d("QuestionCount", questionCount + "");
                }
                Collections.shuffle(questionList);
                if (questionList.size()>5) {
                    questionList.subList(5, questionList.size()).clear();
                }
               // nextQuestion();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }
    // Class to bind the List of items
    private class StableArrayAdapter extends ArrayAdapter<String> {

        private ArrayList<String> list = new ArrayList<String>();
        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return 0;
            //return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
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
