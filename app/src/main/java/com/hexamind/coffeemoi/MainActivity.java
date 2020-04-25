package com.hexamind.coffeemoi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jgabrielfreitas.core.BlurImageView;

public class MainActivity extends AppCompatActivity {
    BlurImageView back;
    AppCompatButton login, signUp;
    RelativeLayout layout;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        back = findViewById(R.id.imgBackground);
        back.setBlur(2);

        login = findViewById(R.id.login);
        signUp = findViewById(R.id.signUp);
        layout = findViewById(R.id.layout);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);

                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth != null) {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            if (currentUser != null) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);

                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                finish();
            }
        }
    }
}
