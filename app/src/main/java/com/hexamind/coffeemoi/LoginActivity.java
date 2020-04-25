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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hexamind.coffeemoi.Utils.Constants;
import com.hexamind.coffeemoi.Utils.SharedPreferenceManager;
import com.jgabrielfreitas.core.BlurImageView;

public class LoginActivity extends AppCompatActivity {
    private ImageView back;
    private AppCompatEditText username, password;
    private TextView forgotPassword;
    private AppCompatButton login;
    private BlurImageView imgBackground;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        back = findViewById(R.id.back);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        forgotPassword = findViewById(R.id.forgotPassword);
        login = findViewById(R.id.login);
        imgBackground = findViewById(R.id.imgBackground);
        imgBackground.setBlur(2);
        layout = findViewById(R.id.layout);
        auth = FirebaseAuth.getInstance();

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Patterns.EMAIL_ADDRESS.matcher(username.getText().toString()).matches()
                    && password.getText().toString().length() >= 6) {
                    login.setEnabled(true);
                    login.setBackgroundDrawable(ContextCompat.getDrawable(LoginActivity.this, R.drawable.drawable_button));
                } else {
                    login.setEnabled(false);
                    login.setBackgroundDrawable(ContextCompat.getDrawable(LoginActivity.this, R.drawable.drawable_button_disabled));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra(Constants.USERNAME, username.getText().toString());
                SharedPreferenceManager.saveStringToSharedPreference(LoginActivity.this, username.getText().toString());
                startActivity(intent);

                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(username.getText().toString(), password.getText().toString());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (auth != null) {
            currentUser = auth.getCurrentUser();
        }
    }

    private void login(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);

                            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                            finish();
                        } else {
                            Snackbar.make(layout, getString(R.string.login_error_message_string), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {

    }
}
