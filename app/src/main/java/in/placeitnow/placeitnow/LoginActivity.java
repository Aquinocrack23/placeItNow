package in.placeitnow.placeitnow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private TextView info1,how_to_use;
    private TextView info2;
    private EditText email;
    private EditText password;
    private Button login;
    private ProgressDialog progressDialog;
    private String user_email,user_password;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase database;
    private DatabaseReference rootRef;
    FirebaseUser user;
    String TAG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        info1 = (TextView) findViewById(R.id.info1);
        info2 = (TextView) findViewById(R.id.info2);
        email = (EditText) findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        how_to_use = (TextView)findViewById(R.id.how_to_use);
        //email.requestFocus();
        login = (Button) findViewById(R.id.register);
        //get firebase auth instance
        mAuth = FirebaseAuth.getInstance();


        //Firebase Auth Listener acts as a session handler to generate user uid
        //we can save the uid generated by below logic
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                 user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in

                    /** value event listeners give a snapshot of whole the data as a single snapshot and it is accessible by looping
                     * over the snapshot and accessing the children
                     *
                     * */
                    if(user.isEmailVerified()){
                        Intent intent = new Intent(LoginActivity.this, BaseActivityFragment.class);
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this,"Signed In",Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    }
                } else {
                    // User is signed out
                    //Toast.makeText(LoginActivity.this,"You are not logged in, Please LogIn to continue",Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        how_to_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,HowToUse.class));
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    email.setHint("");
                }
                else{
                    email.setHint("Email ID");
                }
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    password.setHint("");
                }
                else {
                    password.setHint("Password");
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide keyboard when button is clicked
                InputMethodManager imm = (InputMethodManager) getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(login.getWindowToken(), 0);
                user_email = email.getText().toString().trim();
                user_password=password.getText().toString().trim();
                if(user_email.contentEquals("")||user_password.contentEquals("")){
                    Toast.makeText(getBaseContext(),"fields are empty",Toast.LENGTH_SHORT).show();
                }
                if(!user_email.contentEquals("")&&!user_password.contentEquals("")) {
                    showProcessDialog();
                   // authUserWithPassword(user_email,user_password);
                    authenticateUser(user_email, user_password);
                }
            }
        });

        info1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    /*private void authUserWithPassword(String email,String password){
        Firebase ref = new Firebase("https://placeitnow-1cd03.firebaseio.com/");
        ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                progressDialog.dismiss();
                System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                progressDialog.dismiss();
                Toast.makeText(getBaseContext(),"Login error!",Toast.LENGTH_SHORT).show();
            }
        });
    }*/
    private void authenticateUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // When login failed
                        if (!task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(getBaseContext(),"Login error!",Toast.LENGTH_SHORT).show();
                        } else {
                            //When login successful, redirect user to main activity
                            progressDialog.dismiss();
                           // Intent intent = new Intent(LoginActivity.this, BaseActivityFragment.class);
                           // startActivity(intent);
                          //  finish();
                        }
                    }
                });
    }

    private void showProcessDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Login");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("logging in...");
        progressDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
//        System.exit(0);
    }
}
