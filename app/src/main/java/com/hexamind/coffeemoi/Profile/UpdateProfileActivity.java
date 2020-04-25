package com.hexamind.coffeemoi.Profile;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.hexamind.coffeemoi.HomeActivity;
import com.hexamind.coffeemoi.Model.Users;
import com.hexamind.coffeemoi.R;
import com.hexamind.coffeemoi.RegisterActivity;
import com.hexamind.coffeemoi.Utils.Constants;
import com.jgabrielfreitas.core.BlurImageView;

public class UpdateProfileActivity extends AppCompatActivity {
    private BlurImageView imgBackground;
    private ImageView logout, back;
    private AppCompatEditText fullName, emailAddress, contactNo, password, confirmPassword;
    private AppCompatButton updateProfile;
    private TextView title;
    private Users userDetails;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        imgBackground = findViewById(R.id.imgBackground);
        fullName = findViewById(R.id.fullName);
        emailAddress = findViewById(R.id.emailAddress);
        contactNo = findViewById(R.id.contactNo);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        updateProfile = findViewById(R.id.updateUserDetails);
        logout = findViewById(R.id.logout);
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);

        title.setText(getString(R.string.profile_screen_title_string));
        imgBackground.setBlur(2);
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        ref = db.getReference(Constants.USER_REF_STRING);
        logout.setVisibility(View.GONE);
        Gson gson = new Gson();
        userDetails = gson.fromJson(getIntent().getStringExtra(Constants.USER_DETAILS), Users.class);

        ref.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);

                fullName.setText(user.getFullName());
                emailAddress.setText(user.getEmailAddress());
                contactNo.setText(String.valueOf(user.getPhoneNumber()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateProfileActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Users user = new Users(fullName.getText().toString(), emailAddress.getText().toString(), Long.parseLong(contactNo.getText().toString()), password.getText().toString());
                ref.child(currentUser.getUid())
                        .setValue(user)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(UpdateProfileActivity.this, "User details were updated successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(UpdateProfileActivity.this, "User details were not updated. Please try again later...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

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
                    updateProfile.setEnabled(true);
                    updateProfile.setBackgroundDrawable(ContextCompat.getDrawable(UpdateProfileActivity.this, R.drawable.drawable_button));
                } else {
                    updateProfile.setEnabled(false);
                    updateProfile.setBackgroundDrawable(ContextCompat.getDrawable(UpdateProfileActivity.this, R.drawable.drawable_button_disabled));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToHomeScreen();
            }
        });
    }

    private void backToHomeScreen() {
        Intent intent = new Intent(UpdateProfileActivity.this, HomeActivity.class);
        startActivity(intent);

        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (auth != null)
            currentUser = auth.getCurrentUser();
    }
}
