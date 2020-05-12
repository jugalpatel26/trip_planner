package com.example.jugal.hw08;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignIn extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;
    EditText repassword;
    Button signIn;
    Button cancel;
    String name;
    String passw;
    String repassw;
    String eml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setTitle("Sign In");

        mAuth = FirebaseAuth.getInstance();

        findAllViews();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getting user inputs
                name = firstName.getText().toString() + " " + lastName.getText().toString();
                eml = email.getText().toString();
                passw = password.getText().toString();
                repassw = repassword.getText().toString();

                //checking user inputs and storing in firebase
                if (name.length() > 0 && eml.length() > 0 && passw.length() > 0 && repassw.length() > 0) {
                    if (passw.equals(repassw)) {
                        mAuth.createUserWithEmailAndPassword(eml, passw)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                                        user.updateProfile(profileUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Intent i = new Intent(SignIn.this, Trips.class);
                                                startActivity(i);
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {


                                Toast.makeText(SignIn.this, e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(SignIn.this, "Password Not Match",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignIn.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //method to find all views
    void findAllViews() {
        firstName = (EditText) findViewById(R.id.editText_firstname);
        lastName = (EditText) findViewById(R.id.editText_lastname);
        email = (EditText) findViewById(R.id.editText_email);
        password = (EditText) findViewById(R.id.editText_password);
        repassword = (EditText) findViewById(R.id.editText_repassword);
        signIn = (Button) findViewById(R.id.button_signin);
        cancel = (Button) findViewById(R.id.button_cancel);
    }
}