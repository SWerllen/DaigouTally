package com.example.andriod.daigoutally;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import static android.content.Context.MODE_PRIVATE;

public class SetFragment extends Fragment {
    private View mRootView;
    private boolean mIsInited;
    private boolean mIsPrepared;
    Spinner sp_priceunit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_set, container, false);
        mIsPrepared = true;
        sp_priceunit=mRootView.findViewById(R.id.pane_default_unit_spinner);
        SharedPreferences preferences=getActivity().getSharedPreferences("user_set",MODE_PRIVATE);
        String unit=preferences.getString("unit","CNY");
        setSpinnerByValue(sp_priceunit,unit);
        sp_priceunit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences preferences=getActivity().getSharedPreferences("user_set",MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                String unit=sp_priceunit.getSelectedItem().toString();
                editor.putString("unit",unit);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return mRootView;
    }

    public static SetFragment newInstance() {
        return new SetFragment();
    }

    private static void setSpinnerByValue(Spinner spinner,String value){
        SpinnerAdapter apsAdapter= spinner.getAdapter();
        int k= apsAdapter.getCount();
        for(int i=0;i<k;i++){
            if(value.equals(apsAdapter.getItem(i).toString())){
                spinner.setSelection(i,true);
                break;
            }
        }
    }
}
