package com.hexamind.coffeemoi.CoffeeSize;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hexamind.coffeemoi.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CoffeeSizeViewHolder extends RecyclerView.ViewHolder {
    TextView coffeeSize;
    ImageView checked;
    RelativeLayout layout;

    public CoffeeSizeViewHolder(@NonNull View itemView) {
        super(itemView);

        coffeeSize = itemView.findViewById(R.id.coffeeSize);
        checked  = itemView.findViewById(R.id.checked);
        layout = itemView.findViewById(R.id.layout);
    }
}
