package com.hexamind.coffeemoi.EditOrders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.hexamind.coffeemoi.R;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

public class EditOrdersViewHolder extends RecyclerView.ViewHolder {
    TextView orderID, orderDate,  coffeeType, coffeeSize, creme, milk, sugar, coffeePrice;
    ImageView upDown;
    AppCompatButton updateOrder;
    MaterialCardView showHideView, mainView;

    public EditOrdersViewHolder(@NonNull View itemView) {
        super(itemView);

        orderID = itemView.findViewById(R.id.orderID);
        mainView = itemView.findViewById(R.id.mainView);
        orderDate = itemView.findViewById(R.id.orderDate);
        coffeeType = itemView.findViewById(R.id.coffeeType);
        coffeeSize = itemView.findViewById(R.id.coffeeSize);
        creme = itemView.findViewById(R.id.cremeInCoffee);
        milk = itemView.findViewById(R.id.milkInCoffee);
        sugar = itemView.findViewById(R.id.sugarInCoffee);
        coffeePrice = itemView.findViewById(R.id.coffeePrice);
        upDown = itemView.findViewById(R.id.upDown);
        updateOrder = itemView.findViewById(R.id.updateOrder);
        showHideView = itemView.findViewById(R.id.showHideView);
    }
}
