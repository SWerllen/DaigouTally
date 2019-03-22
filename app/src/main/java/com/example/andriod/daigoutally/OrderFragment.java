package com.example.andriod.daigoutally;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class OrderFragment extends Fragment {
    private View mRootView;
    private boolean mIsInited;
    private boolean mIsPrepared;
    MyDataOperation  dataope;
    List<Order> orders;
    List<Order> searchOrders;
    private boolean issearch=false;
    ListView listView;
    OrderAdapter arrayAdapter;
    OrderAdapter.OrderOperation orderOperation;
    private int firstVisiblePosition=0;
    private int firstVisiblePositionTop=0;
    private String[] target;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_order, container, false);
        mIsPrepared=true;
        lazyLoad();
        orderOperation=new OrderAdapter.OrderOperation(){

            @Override
            public void delete(int positon) {
                savePosition();
                showDeleteDialog();
            }

            @Override
            public void changestate(int positon) {
                savePosition();
                showChangeStateDialog();
            }

            @Override
            public void edit(int positon) {
                savePosition();
                if(issearch)
                    EditOrderActivity.morder=searchOrders.get(positon);
                else
                    EditOrderActivity.morder=orders.get(positon);
                Intent editorder=new Intent(getContext(),EditOrderActivity.class);
                startActivity(editorder);
            }
        };
        return mRootView;
    }
    public void lazyLoad() {
        if (getUserVisibleHint()  && mIsPrepared && !mIsInited) {
            //异步初始化，在初始化后显示正常UI
            loadData();
        }
    }
    private void loadData() {
        new Thread() {
            public void run() {
                dataope=new MyDataOperation(getContext());
                orders = dataope.getOrders();

                if(arrayAdapter==null)
                    arrayAdapter = new OrderAdapter(getContext(), orders, orderOperation);
                listView = mRootView.findViewById(R.id.order_listview);
                listView.setAdapter(arrayAdapter);
                Log.i("ONCREATE","Set order ListView Successfully!");

                mRootView.findViewById(R.id.container_order_empty).setVisibility(View.INVISIBLE);
                mRootView.findViewById(R.id.container_order).setVisibility(View.VISIBLE);
                mIsInited=true;
                dataope.close();
                //1. 加载数据
                //2. 更新UI
                //3. mIsInited = true
            }
        }.start();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            lazyLoad();
        }
    }
    public static OrderFragment newInstance() {
        return new OrderFragment();
    }

    public void setDataOperation(MyDataOperation myDataOperation){
        dataope=myDataOperation;
    }

    public void refresh(){
        dataope.open();
        mRootView.findViewById(R.id.container_order_empty).setVisibility(View.VISIBLE);
        mRootView.findViewById(R.id.container_order).setVisibility(View.INVISIBLE);
        if(!issearch){
            if(listView==null) return;
            if(dataope==null) dataope=new MyDataOperation(getContext());
            if(!dataope.mDataBase.isOpen()) dataope.open();
            orders = dataope.getOrders();
            arrayAdapter= new OrderAdapter(getContext(), orders,orderOperation);
            listView.setAdapter(arrayAdapter);
            recovPosition();
        }
        else{
            searchOrders=dataope.getOrders(target);
            arrayAdapter= new OrderAdapter(getContext(), searchOrders,orderOperation);
            listView.setAdapter(arrayAdapter);
        }
        dataope.close();
        mRootView.findViewById(R.id.container_order_empty).setVisibility(View.INVISIBLE);
        mRootView.findViewById(R.id.container_order).setVisibility(View.VISIBLE);
    }

    public void deleteOrder(int position){
        if(!dataope.mDataBase.isOpen()) dataope.open();
        if(issearch){
            if(!dataope.removeOrder(searchOrders.get(position))){
                Toast.makeText(getContext(),"Failed! Maybe it's my fault!",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else{
            if(!dataope.removeOrder(orders.get(position))){
                Toast.makeText(getContext(),"Failed! Maybe it's my fault!",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(getContext(),"It has been removed!",Toast.LENGTH_SHORT).show();
        refresh();
    }

    public void changeState(int which){
        List<Order> orderList;
        if(issearch) orderList=searchOrders;
        else orderList=orders;
        dataope.open();
        Order temp=orderList.get(arrayAdapter.lastposition);
        temp.state= Order.ORDERSTATE.values()[which];
        if(dataope.updateOrder(temp)){
            Toast.makeText(getContext(),"Update successfully!",Toast.LENGTH_SHORT).show();
            refresh();
        }
        else{
            Toast.makeText(getContext(),"Update Failed!",Toast.LENGTH_SHORT).show();
        }
        dataope.close();
    }

    private void showChangeStateDialog(){
        final String[] items = { "No Shiped!","Wait for Recieving","Has Recieved" };
        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(getContext());
        singleChoiceDialog.setTitle(R.string.dialog_choose_state);
        List<Order> orderList;
        if(issearch) orderList=searchOrders;
        else orderList=orders;
        // 第二个参数是默认选项，此处设置为0
        int checkedpos=orderList.get(arrayAdapter.lastposition).state.ordinal();
        singleChoiceDialog.setSingleChoiceItems(items, Math.min(2,checkedpos),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeState(which);
                        dialog.dismiss();
                    }
                });
        singleChoiceDialog.show();
    }

    private void showDeleteDialog(){
        DialogInterface.OnClickListener setListener = null;

        AlertDialog.Builder normalMoreButtonDialog = new AlertDialog.Builder(getContext());
        normalMoreButtonDialog.setIcon(R.mipmap.ic_launcher_round);
        normalMoreButtonDialog.setMessage(getString(R.string.dialog_make_sure_content));

        setListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        Log.i("Verify:","User try to remove a stock!");
                        deleteOrder(arrayAdapter.lastposition);
                        dialog.dismiss();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        Log.i("Verify:","User don't remove it.");
                        dialog.dismiss();
                        break;
                }
            }
        };
        normalMoreButtonDialog.setPositiveButton(getString(R.string.dialog_btn_confirm_text),setListener);
        normalMoreButtonDialog.setNegativeButton(getString(R.string.dialog_btn_cancel_text),setListener);

        normalMoreButtonDialog.create().show();
        savePosition();
    }

    public void onHiddenChanged(boolean hidd) {
        if(!hidd){
            savePosition();
            listView.setSelectionFromTop(firstVisiblePosition,firstVisiblePositionTop);
            if(listView!=null)listView.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.list_enter));
        }
        else{
            if(arrayAdapter!=null) arrayAdapter.hidetool();
            recovPosition();
        }
        super.onResume();
    }

    private void savePosition() {
        firstVisiblePosition = listView.getFirstVisiblePosition();
        View item = listView.getChildAt(0);
        firstVisiblePositionTop = (item == null) ? 0 : item.getTop();
    }

    private void recovPosition(){
        if(listView!=null) listView.setSelectionFromTop(firstVisiblePosition,firstVisiblePositionTop);
    }

    public void closesearch(){
        issearch=false;
        refresh();
    }

    public void search(String input){
        if(input=="") return;
        issearch=true;
        target=input.split(" ");
        refresh();
    }
}