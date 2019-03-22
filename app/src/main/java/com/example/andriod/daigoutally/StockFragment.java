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

public class StockFragment extends Fragment {
    private View mRootView;
    private boolean mIsInited;
    private boolean mIsPrepared;
    MyDataOperation  dataope;
    List<Stock> stocks;
    ListView listView;
    StockAdapter arrayAdapter;
    StockAdapter.StockOperation stockOperation;
    private int firstVisiblePosition=0;
    private int firstVisiblePositionTop=0;

    private boolean issearch=false;
    String [] target;
    List<Stock> searchstocks;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_stock, container, false);
        stockOperation=new StockAdapter.StockOperation() {
            @Override
            public void delete(int positon) {
                savePosition();
                showDeleteDialog();
            }

            @Override
            public void edit(int positon) {
                savePosition();
                Intent editstock=new Intent(getContext(),EditStockActivity.class);
                if(issearch) EditStockActivity.mstock=searchstocks.get(positon);
                else EditStockActivity.mstock=stocks.get(positon);
                startActivity(editstock);
            }
        };
        mIsPrepared = true;
        lazyLoad();
        return mRootView;
    }
    public void lazyLoad() {
        if (getUserVisibleHint() && mIsPrepared && !mIsInited) {
            //异步初始化，在初始化后显示正常UI
            loadData();
        }
        if(mIsInited){
            listView = mRootView.findViewById(R.id.stock_listview);
            listView.setAdapter(arrayAdapter);
            mRootView.findViewById(R.id.container_stock_empty).setVisibility(View.INVISIBLE);
            mRootView.findViewById(R.id.container_stock).setVisibility(View.VISIBLE);
        }
    }
    private void loadData() {
        new Thread() {
            public void run() {
                dataope=new MyDataOperation(getContext());
                stocks = dataope.getStocks();

                arrayAdapter= new StockAdapter(getContext(), stocks,stockOperation);
                listView = mRootView.findViewById(R.id.stock_listview);
                listView.setAdapter(arrayAdapter);
                Log.i("ONCREATE","Set Stock ListView Successfully!");

                mRootView.findViewById(R.id.container_stock_empty).setVisibility(View.INVISIBLE);
                mRootView.findViewById(R.id.container_stock).setVisibility(View.VISIBLE);
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
    public static StockFragment newInstance() {
        return new StockFragment();
    }

    public void setDataOperation(MyDataOperation myDataOperation){
        dataope=myDataOperation;
    }

    public void refresh(){
        if(dataope==null) dataope=new MyDataOperation(getContext());
        if(!dataope.mDataBase.isOpen()) dataope.open();
        if(issearch){
            searchstocks = dataope.getStocks(target);
            arrayAdapter= new StockAdapter(getContext(), searchstocks,stockOperation);
            listView = mRootView.findViewById(R.id.stock_listview);
            listView.setAdapter(arrayAdapter);
        }
        else{
            stocks = dataope.getStocks();
            arrayAdapter= new StockAdapter(getContext(), stocks,stockOperation);
            listView = mRootView.findViewById(R.id.stock_listview);
            listView.setAdapter(arrayAdapter);
        }
        recovPosition();
        dataope.close();
    }

    void deleteStock(int position){
        dataope.open();
        savePosition();
        if(issearch){
            if(!dataope.removeStock(searchstocks.get(position))){
                Toast.makeText(getContext(),"Failed! Maybe it's my fault!",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else{
            if(!dataope.removeStock(stocks.get(position))){
                Toast.makeText(getContext(),"Failed! Maybe it's my fault!",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(getContext(),"It has been removed!",Toast.LENGTH_SHORT).show();
        refresh();
    }
    private void showDeleteDialog(){
        DialogInterface.OnClickListener setListener = null;

        AlertDialog.Builder normalMoreButtonDialog = new AlertDialog.Builder(getContext());
//        normalMoreButtonDialog.setTitle(getString(R.string.dialog_make_sure_title));
        normalMoreButtonDialog.setIcon(R.mipmap.ic_launcher_round);
        normalMoreButtonDialog.setMessage(getString(R.string.dialog_make_sure_content));

        setListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        Log.i("Verify:","User try to remove a stock!");
                        deleteStock(arrayAdapter.lastposition);
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
            if(listView!=null)listView.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.list_enter));
            savePosition();
        }
        else{
            recovPosition();
            if(arrayAdapter!=null) arrayAdapter.hidetool();
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
        firstVisiblePosition=firstVisiblePositionTop=0;
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
