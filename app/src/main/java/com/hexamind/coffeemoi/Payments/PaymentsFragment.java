package com.hexamind.coffeemoi.Payments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hexamind.coffeemoi.Listeners.OnPaymentMethodClickListener;
import com.hexamind.coffeemoi.Model.Orders;
import com.hexamind.coffeemoi.R;
import com.hexamind.coffeemoi.Utils.Constants;
import com.hexamind.coffeemoi.Utils.SharedPreferenceManager;

import java.text.DecimalFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentsFragment extends Fragment {
    private View root;
    private Orders order;
    private TextView cost;
    private RadioGroup payment;
    private RadioButton cardPay, codPay;
    private OnPaymentMethodClickListener callback;
    private DecimalFormat decimalFormat = new DecimalFormat("#.#");

    public PaymentsFragment(OnPaymentMethodClickListener callback) {
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_payments, container, false);

        order = SharedPreferenceManager.getObjectFromSharedPreference(Constants.STRING_DATA, Orders.class, root.getContext());
        cost = root.findViewById(R.id.cost);
        payment = root.findViewById(R.id.payment);
        cardPay = root.findViewById(R.id.cardPay);
        codPay = root.findViewById(R.id.creditPay);

        cost.setText(getString(R.string.price_value, String.valueOf(order.getPrice())));
        payment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if (id == R.id.cardPay)
                    callback.onPaymentMethodSelected(cardPay.getText().toString());
                else if (id == R.id.cardPay)
                    callback.onPaymentMethodSelected(codPay.getText().toString());
                else
                    callback.onPaymentMethodSelected(null);
            }
        });

        return root;
    }

}
