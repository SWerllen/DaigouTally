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

public class StockAdapter extends BaseAdapter {
    private Context context;
    private List<Stock> data;
    StockOperation stockOperation;
    int lastposition=-1;
    int showexit=-1;
    public boolean[] isshowbtn;
    public StockAdapter(Context context, List<Stock> data,StockOperation stockOperation) {
        this.context = context;
        this.data = data;
        this.stockOperation=stockOperation;
        this.isshowbtn=new boolean[data.size()];
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
    public View getView(final int i, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHold holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.stock_item, null);
            holder = new ViewHold();
            holder.pos=(TextView) convertView.findViewById(R.id.tv_stockpos);
            holder.name = (TextView) convertView.findViewById(R.id.stock_name);
            holder.price = (TextView) convertView.findViewById(R.id.stock_price);
            holder.quantity = (TextView) convertView.findViewById(R.id.stock_quantity);
            holder.date=convertView.findViewById(R.id.stock_date);
            holder.btn=convertView.findViewById(R.id.btn_layout);
            convertView.setTag(holder);
            convertView.findViewById(R.id.card_stock_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv=(TextView) v.findViewById(R.id.tv_stockpos);
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
                    stockOperation.delete(lastposition);
                    isshowbtn[lastposition]=false;
                }
            });
            convertView.findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stockOperation.edit(lastposition);
                    isshowbtn[lastposition]=false;
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
        holder.name.setText(String.valueOf(data.get(i).commodity));
        holder.quantity.setText(String.format("%d",data.get(i).quantity));
        String pricecontent=String.format("%s - %.2f = %.2f",data.get(i).showprice.getString(),data.get(i).discount,data.get(i).showprice.nums-data.get(i).discount);
        holder.price.setText(pricecontent);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        holder.date.setText(format1.format(data.get(i).date));
        return convertView;
    }
    static class ViewHold {
        TextView pos;
        TextView name;
        TextView price;
        TextView quantity;
        TextView date;
        ConstraintLayout btn;
    }
    public interface StockOperation{
        void delete(int positon);
        void edit(int positon);
    }
    public void hidetool(){
        if(lastposition!=-1) isshowbtn[lastposition]=false;
    }
}