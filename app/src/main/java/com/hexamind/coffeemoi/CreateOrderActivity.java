package com.hexamind.coffeemoi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.hexamind.coffeemoi.CoffeeContents.CoffeeContentsFragment;
import com.hexamind.coffeemoi.CoffeeSize.CoffeeSizeFragment;
import com.hexamind.coffeemoi.CoffeeType.CoffeeTypeFragment;
import com.hexamind.coffeemoi.Listeners.OnContentsConfirmedListener;
import com.hexamind.coffeemoi.Listeners.OnContentsItemClickListener;
import com.hexamind.coffeemoi.Listeners.OnPaymentMethodClickListener;
import com.hexamind.coffeemoi.Model.Orders;
import com.hexamind.coffeemoi.Payments.PaymentsFragment;
import com.hexamind.coffeemoi.Utils.Constants;
import com.hexamind.coffeemoi.Utils.SharedPreferenceManager;
import com.jgabrielfreitas.core.BlurImageView;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateOrderActivity extends AppCompatActivity implements OnContentsItemClickListener, OnPaymentMethodClickListener, OnContentsConfirmedListener {
    private ImageView back;
    private StateProgressBar stateProgressBar;
    private String[] descData;
    private ImageView logout;
    private TextView title, price, priceTitle;
    private AppCompatButton next;
    private BlurImageView imgBackground;
    private Double cost, sizeCost, contentsCost;
    private Orders order;
    private Orders previousOrder;
    private String username;
    private RelativeLayout layout;
    private DecimalFormat decimalFormat = new DecimalFormat("#.#");
    private boolean contentsConfirmed = false;
    private boolean coffeeTypeSelected = false;
    private boolean coffeeSizeSelected = false;
    private boolean paymentMethodChosen = false;
    private boolean toEdit = false;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private FirebaseUser currentUser;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        stateProgressBar = findViewById(R.id.stateProgressBar);
        logout = findViewById(R.id.logout);
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
        next = findViewById(R.id.next);
        imgBackground = findViewById(R.id.imgBackground);
        price = findViewById(R.id.price);
        layout = findViewById(R.id.layout);
        priceTitle = findViewById(R.id.priceTitle);
        imgBackground.setBlur(2);
        username = getIntent().getStringExtra(Constants.USERNAME);
        order = new Orders();
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        ref = db.getReference("orders");
        toEdit = getIntent().getBooleanExtra(Constants.TO_EDIT, false);
        currentUser = auth.getCurrentUser();

        order.setUsername(username);
        logout.setVisibility(View.GONE);
        title.setText(getString(R.string.create_order_string));
        descData = getResources().getStringArray(R.array.descData);
        stateProgressBar.setStateDescriptionData(descData);
        loadFragment(new CoffeeTypeFragment());
        if (toEdit) {
            priceTitle.setText(getString(R.string.new_total_price_string));
            Gson gson = new Gson();
            previousOrder = gson.fromJson(Constants.PREVIOUS_ORDER_DETAILS, Orders.class);
        }
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stateProgressBar.getCurrentStateNumber() == 1) {
                    if (coffeeTypeSelected) {
                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                        stateProgressBar.enableAnimationToCurrentState(true);

                        loadFragment(new CoffeeSizeFragment());
                    } else {
                        Snackbar.make(layout, getString(R.string.coffee_type_selection_error), Snackbar.LENGTH_SHORT).show();
                    }
                } else if (stateProgressBar.getCurrentStateNumber() == 2) {
                    if (coffeeSizeSelected) {
                        cost += sizeCost;
                        price.setText(getString(R.string.price_value, String.valueOf(decimalFormat.format(cost))));
                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                        stateProgressBar.enableAnimationToCurrentState(true);
                        stateProgressBar.enableAnimationToCurrentState(true);

                        loadFragment(new CoffeeContentsFragment(CreateOrderActivity.this, CreateOrderActivity.this));
                    } else {
                        Snackbar.make(layout, getString(R.string.coffee_size_selection_error), Snackbar.LENGTH_SHORT).show();
                    }
                } else if (stateProgressBar.getCurrentStateNumber() == 3) {
                    if (contentsConfirmed) {
                        next.setText(getString(R.string.done_string));
                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
                        stateProgressBar.enableAnimationToCurrentState(true);

                        order.setPrice(Double.parseDouble(decimalFormat.format(cost)));
                        System.out.println(order.toString());
                        SharedPreferenceManager.saveObjectToSharedPreference(CreateOrderActivity.this, order);
                        loadFragment(new PaymentsFragment(CreateOrderActivity.this));
                    } else
                        Snackbar.make(layout, getString(R.string.confirm_contents_error_string), Snackbar.LENGTH_SHORT).show();
                } else {
                    if (paymentMethodChosen) {
                        order.setOrderDate(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.getDefault()).format(new Date()));
                        order.setDeleted(false);
                        if (toEdit) {
                            order.setOrderId(previousOrder.getOrderId());
                            ref.child(order.getOrderId())
                                    .child(currentUser.getUid())
                                    .child(order.getOrderId())
                                    .setValue(order)
                                    .addOnCompleteListener(CreateOrderActivity.this, new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Snackbar.make(layout, getString(R.string.changes_made_string), Snackbar.LENGTH_SHORT).show();
                                            } else {
                                                Snackbar.make(layout, getString(R.string.changes_made_error_string), Snackbar.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }

                        stateProgressBar.enableAnimationToCurrentState(true);
                        ref.child(currentUser.getUid())
                                .push()
                                .setValue(order)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Snackbar.make(layout, getString(R.string.order_created_success_string), Snackbar.LENGTH_SHORT).show();
                                            backToHomeScreen();
                                        } else {
                                            Snackbar.make(layout, getString(R.string.order_created_fail_string), Snackbar.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        Snackbar.make(layout, getString(R.string.payment_method_error_string), Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToHomeScreen();
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }

    public void getValues(String value, String data, Boolean dataSelected) {
        if (data.equals(getResources().getStringArray(R.array.descData)[0])) {
            if (dataSelected)
                coffeeTypeSelected = true;
            else
                coffeeTypeSelected = false;
            if (value.equals(getResources().getStringArray(R.array.coffeeTypes)[0])) {
                cost = Double.parseDouble(getResources().getStringArray(R.array.coffee_type_prices)[0]);
            } else if (value.equals(getResources().getStringArray(R.array.coffeeTypes)[1])) {
                cost = Double.parseDouble(getResources().getStringArray(R.array.coffee_type_prices)[1]);
            } else if (value.equals(getResources().getStringArray(R.array.coffeeTypes)[2])) {
                cost = Double.parseDouble(getResources().getStringArray(R.array.coffee_type_prices)[2]);
            } else if (value.equals(getResources().getStringArray(R.array.coffeeTypes)[3])) {
                cost = Double.parseDouble(getResources().getStringArray(R.array.coffee_type_prices)[3]);
            } else if (value.equals(getResources().getStringArray(R.array.coffeeTypes)[4])) {
                cost = Double.parseDouble(getResources().getStringArray(R.array.coffee_type_prices)[4]);
            }

            price.setText(getString(R.string.price_value, String.valueOf(decimalFormat.format(cost))));
            order.setCoffeeType(value);
        } else if (data.equals(getResources().getStringArray(R.array.descData)[1])) {
            if (dataSelected)
                coffeeSizeSelected = true;
            else
                coffeeSizeSelected = false;
            if (value.equals(getResources().getStringArray(R.array.coffeeSizes)[0])) {
                sizeCost = Double.parseDouble(getResources().getStringArray(R.array.coffee_size_prices)[0]);
            } else if (value.equals(getResources().getStringArray(R.array.coffeeSizes)[1])) {
                sizeCost = Double.parseDouble(getResources().getStringArray(R.array.coffee_size_prices)[1]);
            } else if (value.equals(getResources().getStringArray(R.array.coffeeSizes)[2])) {
                sizeCost = Double.parseDouble(getResources().getStringArray(R.array.coffee_size_prices)[2]);
            } else if (value.equals(getResources().getStringArray(R.array.coffeeSizes)[3])) {
                sizeCost = Double.parseDouble(getResources().getStringArray(R.array.coffee_size_prices)[3]);
            }

            price.setText(getString(R.string.price_value, String.valueOf(decimalFormat.format(cost))));
            order.setCoffeeSize(value);
        } else if (data.equals(getResources().getStringArray(R.array.descData)[2])) {
            if (value.equals(getResources().getStringArray(R.array.coffeeContents)[0])) {
                contentsCost = Double.parseDouble(getResources().getStringArray(R.array.coffee_contents_prices)[0]);
            } else if (value.equals(getResources().getStringArray(R.array.coffeeContents)[1])) {
                contentsCost = Double.parseDouble(getResources().getStringArray(R.array.coffee_contents_prices)[1]);
            } else if (value.equals(getResources().getStringArray(R.array.coffeeContents)[2])) {
                contentsCost = Double.parseDouble(getResources().getStringArray(R.array.coffee_contents_prices)[2]);
            }

            price.setText(getString(R.string.price_value, String.valueOf(decimalFormat.format(cost))));
        }
    }

    @Override
    public void onClick(String contentName, int contentPercent) {
        Double contentCost = 0.0;

        if (contentName.equals(getResources().getStringArray(R.array.coffeeContents)[0]) && contentPercent > 0)
            contentCost = Double.parseDouble(getResources().getStringArray(R.array.coffee_contents_prices)[0]);
        if (contentName.equals(getResources().getStringArray(R.array.coffeeContents)[1]) && contentPercent > 0)
            contentCost = Double.parseDouble(getResources().getStringArray(R.array.coffee_contents_prices)[1]);
        if (contentName.equals(getResources().getStringArray(R.array.coffeeContents)[2]) && contentPercent > 0)
            contentCost = Double.parseDouble(getResources().getStringArray(R.array.coffee_contents_prices)[2]);

        cost += contentCost;
        price.setText(getString(R.string.price_value, String.valueOf(decimalFormat.format(cost))));
        if (contentName.equals(getResources().getStringArray(R.array.coffeeContents)[0]))
            order.setCremeInCoffee(contentPercent);
        if (contentName.equals(getResources().getStringArray(R.array.coffeeContents)[1]))
            order.setMilkInCoffee(contentPercent);
        if (contentName.equals(getResources().getStringArray(R.array.coffeeContents)[2]))
            order.setSugarInCoffee(contentPercent);
    }

    @Override
    public void onPaymentMethodSelected(String paymentMethod) {
        if (paymentMethod == null)
            paymentMethodChosen = false;
        else
            paymentMethodChosen = true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (auth != null) {
            currentUser = auth.getCurrentUser();
        }
    }

    private void backToHomeScreen() {
        Intent intent = new Intent(CreateOrderActivity.this, HomeActivity.class);
        startActivity(intent);

        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        finish();
    }

    @Override
    public void isContentsConfirmed(boolean confirmed) {
        contentsConfirmed = true;
    }
}