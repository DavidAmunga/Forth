package com.labs.buttercell.forth.driver;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.labs.buttercell.forth.R;
import com.labs.buttercell.forth.RiderMainActivity;
import com.labs.buttercell.forth.model.Driver;
import com.rengwuxian.materialedittext.MaterialEditText;

import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DriverMainActivity extends AppCompatActivity {

    Button btnSignIn, btnRegister;
    RelativeLayout rootLayout;
    private TextView txtRider;

    FirebaseAuth mAuth;
    DatabaseReference users;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Overlock-Regular.ttf")
                .setFontAttrId(R.attr.fontPath).build());
        setContentView(R.layout.activity_driver_main);


//        Init Views
        initViews();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog();
            }
        });
    }

    private void showLoginDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("SIGN IN");
        dialog.setMessage("Please use email to sign in");


        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout = inflater.inflate(R.layout.layout_login, null);

        final MaterialEditText edtEmail = login_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtPassword = login_layout.findViewById(R.id.edtPassword);


        dialog.setView(login_layout);

//        Set button

        dialog.setPositiveButton("SIGN IN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                final String email = edtEmail.getText().toString().trim();
                final String pass = edtPassword.getText().toString().trim();

//                Set disable button Sign in if is processing
                btnSignIn.setEnabled(false);


//                Check validation
                if (TextUtils.isEmpty(email)) {
                    Snackbar.make(rootLayout, "Please enter email address", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (pass.length() < 6) {
                    Snackbar.make(rootLayout, "Password too short", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                final SpotsDialog waitingDialog = new SpotsDialog(DriverMainActivity.this);
                waitingDialog.show();

//                Sign in
                mAuth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        users.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                                {
                                    if(postSnapshot.child("email").getValue().equals(email) && postSnapshot.child("password").getValue().equals(pass))
                                    {
                                        waitingDialog.dismiss();
                                        startActivity(new Intent(DriverMainActivity.this, DriverMap.class));
                                        finish();

                                    }
                                    else
                                    {
                                        waitingDialog.dismiss();
                                        Snackbar.make(rootLayout, "Sorry, driver does not exist", Snackbar.LENGTH_SHORT)
                                                .show();

                                        btnSignIn.setEnabled(true);

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                waitingDialog.dismiss();
                                Snackbar.make(rootLayout, "Failed " + e.getLocalizedMessage(), Snackbar.LENGTH_SHORT)
                                        .show();

//                                Activate button
                                btnSignIn.setEnabled(true);
                            }
                        });
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showRegisterDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("REGISTER");
        dialog.setMessage("Please use email to register");


        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.layout_register, null);

        final MaterialEditText edtEmail = register_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtName = register_layout.findViewById(R.id.edtName);
        final MaterialEditText edtPhone = register_layout.findViewById(R.id.edtPhone);
        final MaterialEditText edtPassword = register_layout.findViewById(R.id.edtPassword);


        dialog.setView(register_layout);

//        Set button

        dialog.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

//                Check validation
                if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                    Snackbar.make(rootLayout, "Please enter email address", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edtPhone.getText().toString())) {
                    Snackbar.make(rootLayout, "Please enter Phone", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edtName.getText().toString())) {
                    Snackbar.make(rootLayout, "Please enter username", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (edtPassword.getText().toString().length() < 6) {
                    Snackbar.make(rootLayout, "Password too short", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                final SpotsDialog waitingDialog = new SpotsDialog(DriverMainActivity.this);
                waitingDialog.show();
//                Register now
                mAuth.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
//                                Save driver to db

                                Driver driver = new Driver();
                                driver.setEmail(edtEmail.getText().toString());
                                driver.setPassword(edtPassword.getText().toString());
                                driver.setPhone(edtPhone.getText().toString());
                                driver.setName(edtName.getText().toString());

//                                  Using Driver ID as key
                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(driver).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        waitingDialog.dismiss();
                                        Snackbar.make(rootLayout, "Registered successfully", Snackbar.LENGTH_SHORT).show();
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                waitingDialog.dismiss();
                                                Snackbar.make(rootLayout, "Failed " + e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                waitingDialog.dismiss();
                                Snackbar.make(rootLayout, "Failed " + e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();

                            }
                        });
            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void initViews() {
        btnRegister = findViewById(R.id.btnRegister);
        btnSignIn = findViewById(R.id.btnSignIn);

        txtRider=findViewById(R.id.txt_rider_app2);

        mAuth = FirebaseAuth.getInstance();
        users = FirebaseDatabase.getInstance().getReference("Drivers");
        rootLayout = findViewById(R.id.rootLayout);



        txtRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DriverMainActivity.this,RiderMainActivity.class));
            }
        });
    }
}
