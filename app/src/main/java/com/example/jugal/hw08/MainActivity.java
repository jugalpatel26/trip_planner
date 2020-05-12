package com.example.jugal.hw08;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String email=null;
    private String password=null;
    EditText editTextEmail;
    EditText editTextPassword;
    Button logIn;
    Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//Login Activity
        setTitle("Login");

        findAllViews();

        mAuth = FirebaseAuth.getInstance();

        //Onclicklistener for login button
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                if(isConnected()){
                    if(email.length()>0&&password.length()>0) {
                        Log.d("a", "inif" + password + email);
                        auth(email, password);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "Please Connect to Network", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Onclicklistener for singup button
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,SignIn.class);
                startActivity(i);

            }
        });

    }

    //method to authenticate login credentials
    public void auth(String email,String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //using explicit intend to start trips activity.
                            Intent i = new Intent(MainActivity.this,Trips.class);
                            startActivity(i);

                        } else {

                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();

        //getting current user and starting
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            Intent i = new Intent(MainActivity.this,Trips.class);
            startActivity(i);
        }

    }

    //method to find all views
    void findAllViews(){
        editTextEmail= (EditText) findViewById(R.id.email);
        editTextPassword =(EditText) findViewById(R.id.password);
        logIn = (Button) findViewById(R.id.button_login);
        signUp=(Button) findViewById(R.id.button_signup);
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;

    }

}
