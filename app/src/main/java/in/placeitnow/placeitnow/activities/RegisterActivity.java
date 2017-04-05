package in.placeitnow.placeitnow.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.placeitnow.placeitnow.R;

public class RegisterActivity extends AppCompatActivity implements View.OnFocusChangeListener {

    TextView info1;
    private EditText name;
    private EditText password,contact,address,email;
    Button register;
    ProgressDialog progressDialog;
    private String name_reg,pass_reg,email_reg,contact_reg,address_reg;
    private FirebaseAuth auth;                         //FirebaseAuthentication
    private FirebaseAuth.AuthStateListener mAuthListener;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        contact = (EditText) findViewById(R.id.contact);
        address = (EditText) findViewById(R.id.address);
        email = (EditText) findViewById(R.id.email);
        //name.requestFocus();
        register = (Button) findViewById(R.id.register);
        info1 = (TextView) findViewById(R.id.info1);
        register.setEnabled(true);
        progressDialog = new ProgressDialog(RegisterActivity.this);
        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference rootRef =database.getReference("users");
        name.setOnFocusChangeListener(this);
        password.setOnFocusChangeListener(this);
        contact.setOnFocusChangeListener(this);
        email.setOnFocusChangeListener(this);
        address.setOnFocusChangeListener(this);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                     //use for email verification
                        user.sendEmailVerification();

                    // User is signed in
                        final DatabaseReference name, contact, address, password, email;
                        uid = user.getUid();
                        DatabaseReference user_ref = rootRef.child(uid);
                        name = user_ref.child("Name");
                        contact = user_ref.child("Contact");
                        address = user_ref.child("Address");
                        name.setValue(name_reg, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            }
                        });
                        contact.setValue(contact_reg, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            }
                        });
                        address.setValue(address_reg, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            }
                        });
                        //Toast.makeText(RegisterActivity.this,"You are already Registered !",Toast.LENGTH_SHORT).show();
                        // Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    //Toast.makeText(RegisterActivity.this,"Signed Out",Toast.LENGTH_SHORT).show();
                   // Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide keyboard when button is clicked
                InputMethodManager imm = (InputMethodManager) getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(register.getWindowToken(), 0);
                getStrings();
                if(name_reg.contentEquals("")||pass_reg.contentEquals("")||email_reg.contentEquals("")||contact_reg.contentEquals("")||address_reg.contentEquals("")){
                    Toast.makeText(getBaseContext(),"Fields are empty !",Toast.LENGTH_SHORT).show();
                }

                //requesting Firebase server
                if (!name_reg.contentEquals("")&&!pass_reg.contentEquals("")&&!email_reg.contentEquals("")&&!contact_reg.contentEquals("")&&!address_reg.contentEquals("")) {
                    showProcessDialog();
                    String txt=email_reg;

                    String re1="((?:[a-z][a-z0-9_]*))";	// Variable Name 1
                    String re2="(@)";	// Any Single Character 1
                    String re3="(students\\.iitmandi\\.ac\\.in)";	// Fully Qualified Domain Name 1
                    String re4="(iitmandi\\.ac\\.in)";	// Fully Qualified Domain Name 2

                    Pattern p1 = Pattern.compile(re1+re2+re3,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                    Pattern p2 = Pattern.compile(re1+re2+re4,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                    Matcher m1 = p1.matcher(txt);
                    Matcher m2 = p2.matcher(txt);
                    if (m1.find() || m2.find()) {
                        auth.createUserWithEmailAndPassword(email_reg, pass_reg)
                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getBaseContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getBaseContext(), "Please verify the email sent to your email id to login", Toast.LENGTH_SHORT).show();
                                            //startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                            progressDialog.dismiss();
                                            finish();
                                        }
                                    }
                                });
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Please enter your college email ID.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        info1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                RegisterActivity.this.finish();
            }
        });
    }
    private void showProcessDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("User Registration");
        progressDialog.setMessage("Registering...");
        progressDialog.show();
    }
    public void getStrings(){
        name_reg = name.getText().toString();
        email_reg=email.getText().toString();
        pass_reg=password.getText().toString();
        contact_reg=contact.getText().toString();
        address_reg=address.getText().toString();
    }
    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            auth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        RegisterActivity.this.finish();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.name:
            if(hasFocus){
                name.setHint("");
            }
            else{
                name.setHint("Full Name");
            }
                break;
            case  R.id.password:
                if(hasFocus){
                    password.setHint("");
                }
                else{
                    password.setHint("Password");
                }
                break;
            case R.id.contact:
                if(hasFocus){
                    contact.setHint("");
                }
                else{
                    contact.setHint("Contact Number");
                }
                break;
            case  R.id.address:
                if(hasFocus){
                    address.setHint("");
                }
                else{
                    address.setHint("Address");
                }
                break;
            case R.id.email:
                if(hasFocus){
                    email.setHint("");
                }
                else{
                    email.setHint("Email ID");
                }
                break;

        }
    }
}