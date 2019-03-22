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

public class MarkFragment extends Fragment {
    private View mRootView;
    private boolean mIsInited;
    private boolean mIsPrepared;
    MyDataOperation  dataope;
    List<Mark> marks;
    ListView listView;
    MarkAdapter arrayAdapter;
    long readytooperate=-1;
    MarkAdapter.MarkOperation markOperation;
    private int firstVisiblePosition=0;
    private int firstVisiblePositionTop=0;

    private boolean issearch=false;
    private String[] target;
    List<Mark> searchmarks;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_mark, container, false);
        mIsPrepared = true;
        markOperation=new MarkAdapter.MarkOperation() {
            @Override
            public void seeall(int positon) {
                savePosition();
                if(positon!=-1){
                    if(issearch) searchmarks.get(positon).isAll=!searchmarks.get(positon).isAll;
                    else marks.get(positon).isAll=!marks.get(positon).isAll;
                }
            }

            @Override
            public void delete(int positon) {
                savePosition();showDeleteDialog();
            }

            @Override
            public void edit(int positon) {
                savePosition();
                if(!issearch) EditMarkActivity.mmark=marks.get(positon);
                else EditMarkActivity.mmark=searchmarks.get(positon);
                Intent intent=new Intent(getContext(),EditMarkActivity.class);
                startActivity(intent);
            }
        };
        lazyLoad();
        return mRootView;
    }
    public void lazyLoad() {
        if (getUserVisibleHint() && mIsPrepared && !mIsInited) {
            //异步初始化，在初始化后显示正常UI
            loadData();
        }
        if(mIsInited){
            listView = mRootView.findViewById(R.id.mark_listview);
            listView.setAdapter(arrayAdapter);
            mRootView.findViewById(R.id.container_mark_empty).setVisibility(View.INVISIBLE);
            mRootView.findViewById(R.id.container_mark).setVisibility(View.VISIBLE);
        }
    }
    private void loadData() {
        new Thread() {
            public void run() {
                dataope=new MyDataOperation(getContext());
                dataope.open();
                marks = dataope.getmarks();

                arrayAdapter= new MarkAdapter(getContext(), marks,markOperation);
                listView = mRootView.findViewById(R.id.mark_listview);
                listView.setAdapter(arrayAdapter);
                Log.i("ONCREATE","Set mark ListView Successfully!");

                mRootView.findViewById(R.id.container_mark_empty).setVisibility(View.INVISIBLE);
                mRootView.findViewById(R.id.container_mark).setVisibility(View.VISIBLE);
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
    public static MarkFragment newInstance() {
        return new MarkFragment();
    }

    public void refresh(){
        savePosition();
        if(!mIsInited) return;
        if(dataope==null) dataope=new MyDataOperation(getContext());
        if(!dataope.mDataBase.isOpen()) dataope.open();

        if(issearch){
            searchmarks=dataope.getmarks(target);
            for(Mark c:searchmarks){
                int i=0;
                for(;i<marks.size();i++){
                    if(c.id==marks.get(i).id){
                        c.map=marks.get(i).map;
                        break;
                    }
                }
            }
            arrayAdapter= new MarkAdapter(getContext(), searchmarks,markOperation);
            listView = mRootView.findViewById(R.id.mark_listview);
            listView.setAdapter(arrayAdapter);
        }
        else{
            List<Mark> newmarks = dataope.getmarks();
            if(newmarks.size()>marks.size()){
                marks.add(0,newmarks.get(0));
            }
            arrayAdapter= new MarkAdapter(getContext(), marks,markOperation);
            listView = mRootView.findViewById(R.id.mark_listview);
            listView.setAdapter(arrayAdapter);
        }
        recovPosition();
        dataope.close();
    }
    public void onHiddenChanged(boolean hidd) {
        if(!hidd){
            savePosition();
            if(listView!=null)listView.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.list_enter));
        }
        else{
            if(arrayAdapter!=null)arrayAdapter.hidetool();
            recovPosition();
        }
        super.onResume();
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
                        deleteMark(arrayAdapter.lastposition);
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
    }

    private void deleteMark(int pos){
        dataope.open();
        boolean res=false;
        if(issearch) {
            res=dataope.removeMark(searchmarks.get(pos));
            Mark temp=searchmarks.get(pos);
            for(int i=0;i<marks.size();++i){
                if(temp.id==marks.get(i).id){
                    marks.remove(i);
                    break;
                }
            }
            searchmarks.remove(pos);
        }
        else{
            res=dataope.removeMark(marks.get(pos));
            marks.remove(pos);
        }
        if(!res){
            Toast.makeText(getContext(),"Failed! Maybe it's my fault!",Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getContext(),"It has been removed!",Toast.LENGTH_SHORT).show();
        dataope.close();
        refresh();
    }

    private void savePosition() {
        if(listView==null) return;
        firstVisiblePosition = listView.getFirstVisiblePosition();
        View item = listView.getChildAt(0);
        firstVisiblePositionTop = (item == null) ? 0 : item.getTop();
    }

    private void recovPosition(){
        if(listView!=null) listView.setSelectionFromTop(firstVisiblePosition,firstVisiblePositionTop);
        firstVisiblePosition=0;
        firstVisiblePositionTop=0;
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
