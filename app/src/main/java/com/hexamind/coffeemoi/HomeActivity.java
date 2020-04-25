package com.hexamind.coffeemoi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hexamind.coffeemoi.EditOrders.EditOrdersActivity;
import com.hexamind.coffeemoi.Profile.ProfileActivity;
import com.hexamind.coffeemoi.Utils.Constants;
import com.jgabrielfreitas.core.BlurImageView;

public class HomeActivity extends AppCompatActivity {
    private BlurImageView imgBackground;
    private MaterialCardView viewProfile, createOrder, editOrder, deleteOrder, giveRating, aboutUs;
    private ImageView logout;
    private String username;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        imgBackground = findViewById(R.id.imgBackground);
        viewProfile = findViewById(R.id.viewProfile);
        createOrder = findViewById(R.id.createOrder);
        editOrder = findViewById(R.id.changeOrder);
        deleteOrder = findViewById(R.id.removeOrder);
        giveRating = findViewById(R.id.giveRating);
        aboutUs = findViewById(R.id.aboutUs);
        logout = findViewById(R.id.logout);
        username = getIntent().getStringExtra(Constants.USERNAME);
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        imgBackground.setBlur(2);

        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });

        createOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CreateOrderActivity.class);
                intent.putExtra(Constants.USERNAME, username);
                intent.putExtra(Constants.TO_EDIT, false);
                startActivity(intent);

                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                finish();
            }
        });

        editOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, EditOrdersActivity.class);
                intent.putExtra(Constants.USERNAME, username);
                intent.putExtra(Constants.TO_EDIT, true);
                intent.putExtra(Constants.TO_DELETE, false);
                startActivity(intent);

                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                finish();
            }
        });

        deleteOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, EditOrdersActivity.class);
                intent.putExtra(Constants.USERNAME, username);
                intent.putExtra(Constants.TO_EDIT, false);
                intent.putExtra(Constants.TO_DELETE, true);
                startActivity(intent);

                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                finish();
            }
        });

        giveRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "In future consideration", Toast.LENGTH_SHORT).show();
            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "In future consideration", Toast.LENGTH_SHORT).show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
