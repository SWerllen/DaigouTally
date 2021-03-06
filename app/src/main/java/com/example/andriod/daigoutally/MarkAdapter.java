package com.example.andriod.daigoutally;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

import java.util.List;

public class MarkAdapter extends BaseAdapter {
    private Context context;
    private List<Mark> data;
    public int lastposition=-1;
    private  int showexit=-1;
    private boolean[] isshowbtn;
    private boolean[] isALL;
    BitmapDescriptor mapicon = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_gcoding);
    MarkOperation markOperation;
    public MarkAdapter(Context context, List<Mark> data,MarkOperation markOperation) {
        this.context = context;
        this.data = data;
        this.markOperation=markOperation;
        isshowbtn=new boolean[data.size()];
        isALL=new boolean[data.size()];
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
    public View getView(final int i, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHold holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.mark_whole_item, null);
            holder = new ViewHold();
            holder.pos=convertView.findViewById(R.id.tv_markpos);
            holder.markmap = convertView.findViewById(R.id.mark_map);
            holder.decription = (TextView) convertView.findViewById(R.id.mark_description);
            holder.btn=convertView.findViewById(R.id.btn_layout);
            holder.map=convertView.findViewById(R.id.mv_location);
            holder.map.getMap().getUiSettings().setAllGesturesEnabled(false);
            convertView.setTag(holder);
            convertView.findViewById(R.id.card_mark_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv = (TextView) v.findViewById(R.id.tv_markpos);
                    int pos = Integer.valueOf(tv.getText().toString());
                    Log.i("CLICKCARD:", String.format("You click %d item!", pos));
                    if (lastposition > -1) {
                        isshowbtn[lastposition] = false;
                        showexit = lastposition;
                    }
                    if (lastposition == pos) {
                        lastposition = -1;
                        notifyDataSetChanged();
                        return;
                    }
                    lastposition = pos;
                    isshowbtn[lastposition] = true;
                    notifyDataSetChanged();
                }
            });
            convertView.findViewById(R.id.card_mark_item).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    TextView tv = (TextView) v.findViewById(R.id.tv_markpos);
                    int pos = Integer.valueOf(tv.getText().toString());
                    Log.i("LONGCLICKCARD:", String.format("You longclick %d item!", pos));
                    markOperation.seepic(pos);
                    return false;
                }
            });

            convertView.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    markOperation.delete(lastposition);
                }
            });
            convertView.findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    markOperation.edit(lastposition);
                }
            });
            convertView.findViewById(R.id.btn_seeall).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    markOperation.seeall(lastposition);
                    hidetool();
                    notifyDataSetChanged();
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
        if(data.get(i).map!=null||data.get(i).loadbyheight(holder.markmap.getMaxHeight()))
            holder.markmap.setImageBitmap(data.get(i).map);
        String dec=data.get(i).description;

        if(data.get(i).isAll){
            holder.decription.setText(dec);
            holder.map.setVisibility(View.VISIBLE);
            //holder.map.onResume();
            holder.map.getMap().clear();
            LatLng latLng=new LatLng(data.get(i).location.getLatitude(),data.get(i).location.getLongitude());
            MapStatusUpdate status1 = MapStatusUpdateFactory.newLatLng(latLng);
            holder.map.getMap().setMapStatus(status1);
            if(!isALL[i]){
                holder.map.setAnimation(AnimationUtils.loadAnimation(context,R.anim.map_expand));
                isALL[i]=true;
            }

            OverlayOptions option = new MarkerOptions()
                    .position(latLng)
                    .icon(mapicon);
            holder.map.getMap().addOverlay(option);
        }
        else{
            if(isALL[i]){
                isALL[i]=false;
            }
            String add=(dec.length()>200)? "...":"";
            holder.decription.setText(dec.substring(0,Math.min(dec.length(),100))+add);
            holder.map.setVisibility(View.GONE);
        }
        return convertView;
    }
    static class ViewHold {
        TextureMapView map;
        TextView pos;
        ImageView markmap;
        TextView decription;
        ConstraintLayout btn;
    }
    public void hidetool(){
        if(lastposition!=-1) isshowbtn[lastposition]=false;
        lastposition=-1;
    }
    public interface MarkOperation{
        void seeall(int positon);
        void delete(int positon);
        void edit(int positon);
        void seepic(int position);
    }
}