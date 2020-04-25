package com.hexamind.coffeemoi.CoffeeType;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hexamind.coffeemoi.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CoffeeTypeViewHolder extends RecyclerView.ViewHolder {
    TextView coffeeType;
    ImageView checked;
    RelativeLayout layout;

    public CoffeeTypeViewHolder(@NonNull View itemView) {
        super(itemView);

        coffeeType = itemView.findViewById(R.id.coffeeType);
        checked  = itemView.findViewById(R.id.checked);
        layout = itemView.findViewById(R.id.layout);
    }
}
