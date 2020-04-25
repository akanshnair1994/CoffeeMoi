package com.hexamind.coffeemoi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hexamind.coffeemoi.Model.Users;
import com.hexamind.coffeemoi.Utils.Constants;
import com.hexamind.coffeemoi.Utils.SharedPreferenceManager;
import com.jgabrielfreitas.core.BlurImageView;

public class RegisterActivity extends AppCompatActivity {
    private AppCompatEditText fullName, emailAddress, contactNo, password, confirmPassword;
    private AppCompatButton register;
    private ImageView back;
    private BlurImageView imgBackground;
    private RelativeLayout layout;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private RelativeLayout progress;
    private FirebaseDatabase db;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        back =  findViewById(R.id.back);
        fullName = findViewById(R.id.fullName);
        emailAddress = findViewById(R.id.emailAddress);
        contactNo = findViewById(R.id.contactNo);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        register = findViewById(R.id.register);
        layout = findViewById(R.id.layout);
        progress = findViewById(R.id.progress);
        imgBackground = findViewById(R.id.imgBackground);
        imgBackground.setBlur(2);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        ref = db.getReference(Constants.USER_REF_STRING);

        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (fullName.getText().toString().length() > 0
                        && Patterns.EMAIL_ADDRESS.matcher(emailAddress.getText().toString()).matches()
                        && contactNo.getText().toString().length() == 10
                        && password.getText().toString().length() >= 6
                        && confirmPassword.getText().toString().length() >= 6
                        && password.getText().toString().equals(confirmPassword.getText().toString())) {
                    register.setEnabled(true);
                    register.setBackgroundDrawable(ContextCompat.getDrawable(RegisterActivity.this, R.drawable.drawable_button));
                } else {
                    register.setEnabled(false);
                    register.setBackgroundDrawable(ContextCompat.getDrawable(RegisterActivity.this, R.drawable.drawable_button_disabled));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);

                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Users user = new Users(fullName.getText().toString(), emailAddress.getText().toString(), Long.parseLong(contactNo.getText().toString()), password.getText().toString());
                registerUser(emailAddress.getText().toString(), password.getText().toString(), user);
            }
        });
    }

    private void registerUser(String email, String password, final Users user) {
        progress.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            currentUser = auth.getCurrentUser();
                            SharedPreferenceManager.saveCurrentUser(RegisterActivity.this, currentUser);
                            Snackbar.make(layout, getString(R.string.register_success_message), Snackbar.LENGTH_SHORT).show();

                            saveUserDetails(user, currentUser);
                        } else {
                            progress.setVisibility(View.GONE);
                            Snackbar.make(layout, getString(R.string.register_failure_message), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserDetails(Users user, FirebaseUser currentUser) {
        ref.child(currentUser.getUid())
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progress.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);

                            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                            finish();
                        } else {
                            Snackbar.make(layout, getString(R.string.user_registered_success_message_string), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress.setVisibility(View.GONE);
                Snackbar.make(layout, getString(R.string.user_registered_success_message_string), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth != null)
            currentUser = auth.getCurrentUser();
    }
}
