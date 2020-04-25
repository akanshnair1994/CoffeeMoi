package com.hexamind.coffeemoi.CoffeeSize;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hexamind.coffeemoi.Listeners.OnItemClickListener;
import com.hexamind.coffeemoi.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class CoffeeSizeAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<CoffeeSizeViewHolder> {
    ArrayList<String> coffeeSizeList;
    private OnItemClickListener callback;
    Context context;
    int oldPosition = -1;

    public CoffeeSizeAdapter(Context context, ArrayList<String> coffeeSizeList, OnItemClickListener callback) {
        this.context = context;
        this.coffeeSizeList = coffeeSizeList;
        this.callback  = callback;
    }

    @NonNull
    @Override
    public CoffeeSizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CoffeeSizeViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.layout_coffee_size, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CoffeeSizeViewHolder holder, final int position) {
        final String coffeeSizeStr = coffeeSizeList.get(position);

        holder.coffeeSize.setText(coffeeSizeStr);
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
                callback.onClick(coffeeSizeStr);
            }
        });
    }

    @Override
    public int getItemCount() {
        return coffeeSizeList.size();
    }
}
