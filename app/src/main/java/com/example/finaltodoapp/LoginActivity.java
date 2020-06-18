package com.example.finaltodoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthCredential;

public class LoginActivity extends AppCompatActivity {
    EditText txtUsername,txtPassword;
    Button btnLogin,btnCancel,btnGoogleSignIn;
    AlertDialog.Builder mAlertDialogue;
    private GoogleSignInClient mGoogleSignInClient;
    public final static int RC_SIGN_IN=07;
    private FirebaseAuth mAuth;
    private SignInButton signInButton;


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user =mAuth.getCurrentUser();
        if (user!=null)
        {
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtUsername=findViewById(R.id.splash_txt_username);
        txtPassword=findViewById(R.id.splash_txt_pssword);
        btnLogin=findViewById(R.id.splash_btn_login);
        btnCancel=findViewById(R.id.splash_btn_cancel);

        signInButton=findViewById(R.id.google_signIn_btn);

        //Authentication
        mAuth=FirebaseAuth.getInstance();
       signInButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

                   switch(v.getId()) {
                       case R.id.google_signIn_btn:
                           signIn();
                           break;

                   }
           }
       });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username =txtUsername.getText().toString();
                String Password =txtPassword.getText().toString();

                if (Username.equals("") && Password.equals(""))
                {
                    txtUsername.setError(getString(R.string.login_username_required));
                    txtUsername.requestFocus();


                }
                else if (Password.equals(""))
                {
                    txtPassword.setError(getString(R.string.login_password_required));
                    txtPassword.requestFocus();
                }
                else
                {
                    if(Username.equals("admin") && Password.equals("admin"))
                    {
                        SharedPreferences preferences =getApplicationContext().getSharedPreferences("todo_pref",0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("authentication",true);
                        editor.apply();
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        txtUsername.setError(getString(R.string.login_invalid_login));
                    }
                }



            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialogue = new AlertDialog.Builder(LoginActivity.this);

                mAlertDialogue.setMessage(getString(R.string.Quit_Application))
                        .setCancelable(false)
                        .setTitle(getString(R.string.app_name))
                        .setIcon(R.mipmap.ic_launcher);

                mAlertDialogue.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });
                mAlertDialogue.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                mAlertDialogue.show();



            }
        });
       // mAlertDialogue.show();
        createRequest();
    }
    private void createRequest()
    {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }
    private void signIn() {
       Intent signInIntent = mGoogleSignInClient.getSignInIntent();
       startActivityForResult(signInIntent,RC_SIGN_IN);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
               
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
               
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.

                        }

                        // ...
                    }

                    private void updateUI(FirebaseUser user) {
                        GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                        if (account!=null)
                        {
                            String personName=account.getDisplayName();
                            String personGivenName=account.getGivenName();
                            String personFamilyName=account.getFamilyName();
                            String personEmail=account.getEmail();
                            String personId=account.getId();
                            Uri personPhoto=account.getPhotoUrl();
                            Toast.makeText(LoginActivity.this, personName+personEmail, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}