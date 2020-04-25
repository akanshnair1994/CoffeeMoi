package com.hexamind.coffeemoi.EditOrders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.hexamind.coffeemoi.CreateOrderActivity;
import com.hexamind.coffeemoi.Model.Orders;
import com.hexamind.coffeemoi.R;
import com.hexamind.coffeemoi.Utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class EditOrderAdapter extends RecyclerView.Adapter<EditOrdersViewHolder> {
    private Context context;
    private List<Orders> orderList;
    private boolean orderEditable = false;
    private boolean isToDelete;
    int oldPosition = -1;
    private FirebaseDatabase db;
    private DatabaseReference ref;

    public EditOrderAdapter(Context context, List<Orders> orderList, boolean isToDelete) {
        this.context = context;
        this.orderList = orderList;
        this.isToDelete = isToDelete;
        db = FirebaseDatabase.getInstance();
        ref = db.getReference("orders");
    }

    @NonNull
    @Override
    public EditOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EditOrdersViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_edit_orders, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final EditOrdersViewHolder holder, final int position) {
        final Orders order = orderList.get(position);

        if (oldPosition != -1) {
            if (oldPosition == position)
                holder.mainView.setVisibility(View.VISIBLE);
            else
                holder.mainView.setVisibility(View.GONE);
        }
        holder.orderID.setText(order.getOrderId());
        holder.orderDate.setText(order.getOrderDate());
        holder.coffeeType.setText(order.getCoffeeType());
        holder.coffeeSize.setText(order.getCoffeeSize());
        holder.creme.setText(context.getString(R.string.progress_value, String.valueOf(order.getCremeInCoffee())));
        holder.milk.setText(context.getString(R.string.progress_value, String.valueOf(order.getMilkInCoffee())));
        holder.sugar.setText(context.getString(R.string.progress_value, String.valueOf(order.getSugarInCoffee())));
        holder.coffeePrice.setText(context.getString(R.string.price_value, String.valueOf(order.getPrice())));
        holder.upDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.upDown.getDrawable().getConstantState().equals(context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp, null).getConstantState())) {
                    holder.upDown.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_keyboard_arrow_down_black_24dp));
                    hideView(holder.showHideView);
                } else {
                    holder.upDown.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_keyboard_arrow_up_black_24dp));
                    showView(holder.showHideView);
                }
            }
        });
        Date orderDate = convertStringToDate(order.getOrderDate());
        Date currentDate = convertTimezone(new Date());
        holder.orderDate.setText(getTime(orderDate, currentDate));
        if (!orderEditable) {
            holder.updateOrder.setEnabled(false);
            holder.updateOrder.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.drawable_button_delete_order_disabled));
            holder.updateOrder.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
            holder.updateOrder.setEnabled(true);
            holder.updateOrder.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.drawable_button_delete_order));
            holder.updateOrder.setTextColor(ContextCompat.getColor(context, android.R.color.white));

        }
        if (isToDelete)
            holder.updateOrder.setText(context.getString(R.string.delete_order_string));
        else
            holder.updateOrder.setText(context.getString(R.string.update_order_string));

        holder.updateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isToDelete) {
                    Gson gson = new Gson();
                    String orderInJson = gson.toJson(order);

                    Intent intent = new Intent(context, CreateOrderActivity.class);
                    intent.putExtra(Constants.USERNAME, order.getUsername());
                    intent.putExtra(Constants.TO_EDIT, true);
                    intent.putExtra(Constants.PREVIOUS_ORDER_DETAILS, orderInJson);
                    context.startActivity(intent);

                    ((EditOrdersActivity) context).overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                    ((EditOrdersActivity) context).finish();
                } else {
                    new AlertDialog.Builder(context)
                            .setTitle(context.getString(R.string.delete_order_string))
                            .setMessage(context.getString(R.string.delete_order_dialog_message_string))
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialogInterface, int i) {
                                    ref.child(order.getOrderId()).child(order.getOrderId())
                                            .removeValue()
                                            .addOnCompleteListener(((EditOrdersActivity) context), new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        order.setDeleted(true);
                                                        oldPosition = position;
                                                        notifyDataSetChanged();
                                                        holder.mainView.setVisibility(View.GONE);
                                                    } else {
                                                        Toast.makeText(context, "There was some problem in deleting the order..", Toast.LENGTH_SHORT).show();
                                                        dialogInterface.dismiss();
                                                    }
                                                }
                                            });
                                }
                            }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    private String getTime(Date fromDate, Date toDate) {
        long difference = fromDate.getTime() - toDate.getTime();
        long seconds = 1000;
        long minutes = seconds * 60;
        long hours = minutes * 60;
        long days = hours * 24;
        long requiredDays = difference / days;
        difference %= days;
        long requiredHours = difference  / hours;
        difference %= hours;
        long requiredMinutes = difference / minutes;

        if (requiredDays == 0 && requiredHours == 0 && requiredMinutes > 10)
            orderEditable = false;
        else
            orderEditable = true;

        isOrderEditable(requiredHours);
        if (requiredDays == 0)
            return context.getString(R.string.hours_value, String.valueOf(Math.abs(requiredHours)));
        else if (requiredMinutes == 0)
            return context.getString(R.string.minutes_value, String.valueOf(Math.abs(requiredMinutes)));
        else
            return context.getString(R.string.days_value, String.valueOf(Math.abs(requiredDays)));
    }

    private void isOrderEditable(long hours) {
        if (hours < 10)
            orderEditable = true;
        else
            orderEditable = false;
    }

    private Date convertStringToDate(String date) {
        Date newDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.getDefault());
            newDate = sdf.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return newDate;
    }

    private Date convertTimezone(Date date) {
        Date newDate = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.getDefault());
            String dt = sdf.format(date);
            SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.getDefault());
            newDate =  newDateFormat.parse(dt);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return newDate;
    }

    private void showView(View view) {
        view.setVisibility(View.VISIBLE);
    }

    private void hideView(View view) {
        view.setVisibility(View.GONE);
    }
}
