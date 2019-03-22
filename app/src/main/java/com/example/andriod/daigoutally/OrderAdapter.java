package com.example.andriod.daigoutally;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

public class OrderAdapter extends BaseAdapter {
    private Context context;
    private List<Order> data;
    public boolean[] isshowbtn;
    int lastposition=-1;
    int showexit=-1;
    OrderOperation orderOperation;
    public OrderAdapter(Context context, List<Order> data,OrderOperation orderOperation) {
        this.context = context;
        this.data = data;
        this.isshowbtn=new boolean[data.size()];
        this.orderOperation=orderOperation;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if(i>10){
            Log.i("ss","chuxianle!");
        }
        ViewHold holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.order_item, null);
            holder = new ViewHold();
            holder.pos=convertView.findViewById(R.id.tv_orderpos);
            holder.commodity = (TextView) convertView.findViewById(R.id.order_name);
            holder.price = (TextView) convertView.findViewById(R.id.order_price);
            holder.buyer = (TextView) convertView.findViewById(R.id.order_buyer);
            holder.price = convertView.findViewById(R.id.order_price);
            holder.quantity = convertView.findViewById(R.id.order_quantity);
            holder.date=convertView.findViewById(R.id.order_date);
            holder.btn=convertView.findViewById(R.id.btn_layout);
            holder.strok=convertView.findViewById(R.id.Layout_text);
            convertView.setTag(holder);
            convertView.findViewById(R.id.card_order_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv=(TextView) v.findViewById(R.id.tv_orderpos);
                    int pos=Integer.valueOf(tv.getText().toString());
                    Log.i("CLICKCARD:",String.format("You click %d item!",pos));
                    if(lastposition>-1){
                        isshowbtn[lastposition]=false;
                        showexit=lastposition;
                    }
                    if(lastposition==pos){
                        lastposition=-1;
                        notifyDataSetChanged();
                        return;
                    }
                    lastposition=pos;
                    isshowbtn[lastposition]=true;
                    notifyDataSetChanged();
                }
            });
            convertView.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderOperation.delete(lastposition);
                }
            });
            convertView.findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderOperation.edit(lastposition);
                }
            });
            convertView.findViewById(R.id.btn_change_state).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderOperation.changestate(lastposition);
                }
            });
        } else {
            holder = (ViewHold) convertView.getTag();
        }
        if(isshowbtn[i]){
            holder.btn.setVisibility(View.VISIBLE);
            holder.btn.startAnimation(AnimationUtils.loadAnimation(context,R.anim.btn_enter));
        }
        else if(showexit==i){
            holder.btn.startAnimation(AnimationUtils.loadAnimation(context,R.anim.btn_exit));
            holder.btn.setVisibility(View.INVISIBLE);
            showexit=-1;
        }
        else{
            holder.btn.setVisibility(View.INVISIBLE);
        }

        holder.pos.setText(String.valueOf(i));
        holder.commodity.setText(String.valueOf(data.get(i).commodity));
        holder.price.setText(data.get(i).sellprice.getString());
        holder.buyer.setText(data.get(i).buyer);
        holder.quantity.setText(String.valueOf(data.get(i).quantity));
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        holder.date.setText(format1.format(data.get(i).selltime));
        switch(data.get(i).state){
            case NOSHIP:
                holder.strok.setBackgroundResource(R.drawable.shape_order_item_noshiped);
                break;
            case WAITRECIEVE:
                holder.strok.setBackgroundResource(R.drawable.shape_order_item_notrecieve);
                break;
            case RECIEVED:
                holder.strok.setBackgroundResource(R.drawable.shape_order_item_hasrecieve);
                break;
        }
        return convertView;
    }
    static class ViewHold {
        TextView pos;
        TextView commodity;
        TextView price;
        TextView buyer;
        TextView date;
        TextView quantity;
        ConstraintLayout btn;
        ConstraintLayout strok;
    }

    public interface OrderOperation{
        void delete(int positon);
        void changestate(int positon);
        void edit(int positon);
    }

    public void hidetool(){
        if(lastposition!=-1) isshowbtn[lastposition]=false;
    }
}