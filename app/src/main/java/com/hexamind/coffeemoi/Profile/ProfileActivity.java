package com.hexamind.coffeemoi.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hexamind.coffeemoi.Model.Users;
import com.hexamind.coffeemoi.R;
import com.hexamind.coffeemoi.Utils.Constants;
import com.jgabrielfreitas.core.BlurImageView;

public class ProfileActivity extends AppCompatActivity {
    private BlurImageView imgBackground;
    private TextView title, fullName, email, phoneNumber;
    private ImageView back, edit;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imgBackground = findViewById(R.id.imgBackground);
        title = findViewById(R.id.title);
        fullName = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);
        back = findViewById(R.id.back);
        edit = findViewById(R.id.logout);
        imgBackground = findViewById(R.id.imgBackground);

        imgBackground.setBlur(2);
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        ref = db.getReference(Constants.USER_REF_STRING);
        edit.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_edit_black_24dp));
        title.setText(getString(R.string.profile_screen_title_string));

        ref.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                fullName.setText(user.getFullName());
                email.setText(user.getEmailAddress());
                phoneNumber.setText(String.valueOf(user.getPhoneNumber()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Error" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, UpdateProfileActivity.class));
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
