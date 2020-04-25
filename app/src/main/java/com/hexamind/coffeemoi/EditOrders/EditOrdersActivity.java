package com.hexamind.coffeemoi.EditOrders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.hexamind.coffeemoi.HomeActivity;
import com.hexamind.coffeemoi.Model.Orders;
import com.hexamind.coffeemoi.R;
import com.hexamind.coffeemoi.Utils.Constants;
import com.hexamind.coffeemoi.Utils.SharedPreferenceManager;
import com.jgabrielfreitas.core.BlurImageView;

import java.util.ArrayList;
import java.util.List;

public class EditOrdersActivity extends AppCompatActivity {
    private BlurImageView imgBackground;
    private TextView title;
    private ImageView back,  logout;
    private RecyclerView recyclerView;
    private List<Orders> orderList = new ArrayList<>();
    private EditOrderAdapter adapter;
    private Orders order;
    private boolean isToDelete = false;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private FirebaseUser currentUser;
    private FirebaseAuth auth;
    private final String TAG = EditOrdersActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_orders);

        imgBackground = findViewById(R.id.imgBackground);
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
        logout = findViewById(R.id.logout);
        recyclerView = findViewById(R.id.recyclerView);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        ref = db.getReference("orders");
        logout.setVisibility(View.GONE);
        imgBackground.setBlur(2);

        currentUser = auth.getCurrentUser();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ref.child(currentUser.getUid()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Orders orders = ds.getValue(Orders.class);
                            orders.setOrderId(ds.getKey());
                            orderList.add(orders);
                            adapter = new EditOrderAdapter(EditOrdersActivity.this, orderList, isToDelete);
                            recyclerView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled: ", databaseError.toException());
                    }
                }
        );
        if (!isToDelete)
            title.setText(getString(R.string.edit_order_string));
        else
            title.setText(getString(R.string.delete_order_string));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToHomeScreen();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (auth != null) {
            currentUser = auth.getCurrentUser();
            Toast.makeText(this, "current user: " + currentUser.getUid(), Toast.LENGTH_SHORT).show();
        }
    }

    private void backToHomeScreen() {
        Intent intent = new Intent(EditOrdersActivity.this, HomeActivity.class);
        startActivity(intent);

        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        finish();
    }
}
