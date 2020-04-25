package com.hexamind.coffeemoi.CoffeeSize;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hexamind.coffeemoi.CreateOrderActivity;
import com.hexamind.coffeemoi.Listeners.OnItemClickListener;
import com.hexamind.coffeemoi.R;

import java.util.ArrayList;
import java.util.Arrays;

public class CoffeeSizeFragment extends Fragment implements OnItemClickListener {
    private View root;
    private RecyclerView recyclerView;
    private ArrayList<String> coffeeSizes = new ArrayList<>();
    private CoffeeSizeAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_coffee_size, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        coffeeSizes.addAll(Arrays.asList(getResources().getStringArray(R.array.coffeeSizes)));
        adapter = new CoffeeSizeAdapter(root.getContext(), coffeeSizes, this);
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onClick(String value) {
        Boolean selected = false;
        if (value.equals(""))
            selected = false;
        else
            selected = true;


        ((CreateOrderActivity) getActivity()).getValues(value, getResources().getStringArray(R.array.descData)[1], selected);
    }
}
