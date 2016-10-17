package zhaos.pong.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import zhaos.pong.MainView;
import zhaos.pong.R;

/**
 * Created by kodomazer on 10/16/2016.
 */

public class EntryActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Set content view
        //Defaults to "login page"
        setContentView(R.layout.activity_main);

        //link buttons
        Button button;

        //Login Button
        button = (Button) findViewById(R.id.login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call method to handle login information
                login();
            }
        });

        //Account Creation
        //TODO: add mechanism for creating an account.  Will swap out UI

        //Guest play
        //Jumps into game
        button = (Button) findViewById(R.id.guestPlay);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void login(){
        //Strings to use in the call
        String username;
        String password;

        //Getting strings
        EditText info;
        //assumes the current layout is the login layout
        info = (EditText)findViewById(R.id.usernameInput);
        username = info.getText().toString();

        info = (EditText) findViewById(R.id.passwordInput);
        password = info.getText().toString();

        //Put final login call here
        //TODO: handle actual login here.
        //Probably pass it to another Thread to handle, io is thread locking
        //Possibly pass something for it to start the game on login success
        //Otherwise pass a callback to update UI
        //methodCall(String,String); //signature
        //methodCall(username,password); //actual call

    }


    private void startGame(){
        Intent pongGame = new Intent(this, MainView.class);
        //TODO: Find out what <Intents> need to be passed into the Activity

        startActivity(pongGame);

    }
}
