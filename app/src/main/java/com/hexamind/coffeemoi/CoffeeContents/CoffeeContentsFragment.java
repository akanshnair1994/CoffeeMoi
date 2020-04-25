package com.hexamind.coffeemoi.CoffeeContents;


import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hexamind.coffeemoi.Listeners.OnContentsConfirmedListener;
import com.hexamind.coffeemoi.Listeners.OnContentsItemClickListener;
import com.hexamind.coffeemoi.R;

import java.util.HashMap;
import java.util.Map;

public class CoffeeContentsFragment extends Fragment {
    private View root;
    private TextView cremeProgress, milkProgress, sugarProgress;
    private SeekBar cremeSeekbar, milkSeekbar, sugarSeekbar;
    private AppCompatButton confirmContents;
    private Map<String, Integer> contentsMap = new HashMap<>();
    private OnContentsItemClickListener callback;
    private OnContentsConfirmedListener contentsConfirmedCallback;

    public CoffeeContentsFragment(OnContentsItemClickListener callback, OnContentsConfirmedListener contentsConfirmedCallback) {
        this.callback = callback;
        this.contentsConfirmedCallback = contentsConfirmedCallback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_coffee_contents, container, false);

        cremeProgress = root.findViewById(R.id.seekbarProgressCreme);
        milkProgress = root.findViewById(R.id.seekbarProgressMilk);
        sugarProgress = root.findViewById(R.id.seekbarProgressSugar);
        cremeSeekbar = root.findViewById(R.id.seekbarCreme);
        milkSeekbar = root.findViewById(R.id.seekbarMilk);
        sugarSeekbar = root.findViewById(R.id.seekbarSugar);
        confirmContents = root.findViewById(R.id.confirm);

        cremeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                cremeProgress.setText(getString(R.string.progress_value, String.valueOf(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        milkSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                milkProgress.setText(getString(R.string.progress_value, String.valueOf(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sugarSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                sugarProgress.setText(getString(R.string.progress_value, String.valueOf(progress)));
                contentsMap.put(getResources().getStringArray(R.array.coffeeContents)[2], progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        confirmContents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contentsConfirmedCallback.isContentsConfirmed(true);
                callback.onClick(getResources().getStringArray(R.array.coffeeContents)[0], cremeSeekbar.getProgress());
                callback.onClick(getResources().getStringArray(R.array.coffeeContents)[1], milkSeekbar.getProgress());
                callback.onClick(getResources().getStringArray(R.array.coffeeContents)[2], sugarSeekbar.getProgress());
                confirmContents.setEnabled(false);
                confirmContents.setBackgroundDrawable(ContextCompat.getDrawable(root.getContext(), R.drawable.drawable_button_confirm_contents_disabled));
            }
        });

        return root;
    }
}
