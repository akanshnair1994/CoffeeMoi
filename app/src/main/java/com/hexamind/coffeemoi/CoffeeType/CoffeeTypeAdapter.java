package com.hexamind.coffeemoi.CoffeeType;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hexamind.coffeemoi.Listeners.OnItemClickListener;
import com.hexamind.coffeemoi.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class CoffeeTypeAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<CoffeeTypeViewHolder> {
    ArrayList<String> coffeeTypeList;
    private OnItemClickListener callback;
    Context context;
    int oldPosition = -1;

    public CoffeeTypeAdapter(Context context, ArrayList<String> coffeeTypeList, OnItemClickListener callback) {
        this.context = context;
        this.coffeeTypeList = coffeeTypeList;
        this.callback  = callback;
    }

    @NonNull
    @Override
    public CoffeeTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CoffeeTypeViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.layout_coffee_type, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CoffeeTypeViewHolder holder, final int position) {
        final String coffeeTypeStr = coffeeTypeList.get(position);

        holder.coffeeType.setText(coffeeTypeStr);
        if (oldPosition != -1) {
            if (oldPosition == position)
                holder.checked.setVisibility(View.VISIBLE);
            else
                holder.checked.setVisibility(View.GONE);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldPosition = position;
                notifyDataSetChanged();
                callback.onClick(coffeeTypeStr);
            }
        });
    }

    @Override
    public int getItemCount() {
        return coffeeTypeList.size();
    }
}
