package com.example.andriod.daigoutally;

        import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.githang.statusbar.StatusBarCompat;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class AddMarkActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE=1;
    private static final int RESULT_CAMERA_IMAGE=2;
    private static final int RESULT_ZOOM=3;
    LocationHelper locationHelper;
    private static final String AUTHORITY=BuildConfig.APPLICATION_ID+".provider";

    TextView tv_description;
    ImageView picture;
    File image;
    Location mLocation=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mark);
        picture=findViewById(R.id.picture);
        tv_description=findViewById(R.id.tv_description);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorBackground),true);
        locationHelper=new LocationHelper(this, new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if(bdLocation==null){
                    return;
                }
                int code=bdLocation.getLocType();
                switch (code){
                    case BDLocation.TypeGpsLocation:
                        Log.i("LKing","GPS Location--------------");break;
                    case BDLocation.TypeNetWorkLocation:
                        Log.i("LKing","Network Location--------------");break;
                    case BDLocation.TypeOffLineLocation:
                        Log.i("LKing","Offline Location--------------");break;
                    case BDLocation.TypeServerError:
                        Log.i("LKing","Location Failed--------------");return;
                    case BDLocation.TypeNetWorkException:
                        Log.i("LKing","The network is not so good!--------------");return;
                    case BDLocation.TypeCriteriaException:
                        Log.i("LKing","The network has stoped working!--------------");return;
                        default:
                            Log.i("LKing","Nothing get!");return;
                }
                if(mLocation==null) mLocation=new Location("dummyprovider");
                mLocation.setLatitude(bdLocation.getLatitude());
                mLocation.setLongitude(bdLocation.getLongitude());
                locationHelper.Stop();
            }
        });
    }
    public void onClickBack(View v){
        finish();
    }

    public void onClickAdd(View v){
        Mark mark=new Mark();
        mark.description=tv_description.getText().toString();
        if(mark.description.equals("")){
            ToastShow("You should write something!");
            return ;
        }
        else if(mark.description.length()>1000){
            ToastShow("The length of charactors should be less than 1000!");
            return ;
        }
        if(!image.exists()){
            ToastShow("Cannot find the image!");
            return ;
        }

        Location temp=locationHelper.getLocationFromFile(image.getPath());
        if(temp!=null) mLocation=temp;
        else Log.i("LoadLocationfromFile:","Can't get the location from file.");

        if(mark.location==null){
            if(mLocation==null){
                ToastShow("Cannot get the location, please open the GPS!");
                return;
            }
            else{
                mark.location=mLocation;
            }
        }

        mark.filepath=image.getPath();
        MyDataOperation operation=new MyDataOperation(getBaseContext());
        if(operation.addMark(mark)){
            ToastShow("Add successfully!");
            locationHelper.Stop();
            finish();
        }
        else{
            ToastShow("Add failed!");
        }
    }

    public void onClickPhoto(View v){
        showPopueWindow();
    }

    private void ToastShow(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

    private void showPopueWindow(){
        View popView = View.inflate(this,R.layout.popupwindow_camera_need,null);
        Button bt_album =  popView.findViewById(R.id.btn_pop_album);
        Button bt_camera = popView.findViewById(R.id.btn_pop_camera);
        Button bt_cancle = popView.findViewById(R.id.btn_pop_cancel);

        int weight = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels*1/3;

        final PopupWindow popupWindow = new PopupWindow(popView,weight,height);
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

        bt_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photos();
                popupWindow.dismiss();

            }
        });
        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera();
                popupWindow.dismiss();

            }
        });
        bt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM,0,50);

    }

    private void camera() {
        try {
            image = new File(this.getExternalCacheDir(), "DaigouTally_"+System.currentTimeMillis() + ".jpg");
            if(image.exists()){
                image.delete();
            }
            else{
                boolean b = image.createNewFile();
                if (b) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri uri=geturi(image);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    }
                    else if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN) {
                        ClipData clip=
                                ClipData.newUri(getContentResolver(), "A photo", uri);

                        intent.setClipData(clip);
                        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    }
                    else {
                        List<ResolveInfo> resInfoList=
                                getPackageManager()
                                        .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

                        for (ResolveInfo resolveInfo : resInfoList) {
                            String packageName = resolveInfo.activityInfo.packageName;
                            grantUriPermission(packageName, uri,
                                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        }
                    }

                    startActivityForResult(intent, RESULT_CAMERA_IMAGE);
                }
            }
        } catch (IOException e) {
            ToastShow("Camera cannot work!");
            Log.i("OpenCamera:","wrong!");
        }
    }

    private void photos() {
        Intent getImage = new Intent(Intent.ACTION_PICK, null);
        getImage.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(getImage, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case RESULT_CAMERA_IMAGE:
                    if (image.exists()) {
                        Bitmap smallBitmap=BitmapThumb.decodeSampledBitmapFromFd(image.getAbsolutePath(),picture.getWidth());
                        picture.setImageBitmap(smallBitmap);
                    }
                    break;
                case RESULT_LOAD_IMAGE:
                    if (data != null) {
                        Uri uri = data.getData();
                        image=new File(RealPathFromUriUtils.getRealPathFromUri(this,uri));
                        if(!image.exists())
                            ToastShow("Cannot get the image file!");
                        Bitmap smallBitmap=BitmapThumb.decodeSampledBitmapFromFd(image.getAbsolutePath(),picture.getWidth());
                        picture.setImageBitmap(smallBitmap);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private Uri geturi(File f){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(this, AUTHORITY, f);
        } else {
            return Uri.fromFile(f);
        }
    }

    @Override
    protected void onDestroy() {
        locationHelper.Stop();
        super.onDestroy();
    }
}
