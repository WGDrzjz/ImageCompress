package com.wgd.myimagelibrary;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wgd.gdcp.gdcplibrary.GDCompress;
import com.wgd.gdcp.gdcplibrary.GDCompressA;
import com.wgd.gdcp.gdcplibrary.GDCompressC;
import com.wgd.gdcp.gdcplibrary.GDCompressImageListener;
import com.wgd.gdcp.gdcplibrary.GDCompressImageListenerA;
import com.wgd.gdcp.gdcplibrary.GDCompressImageS;
import com.wgd.gdcp.gdcplibrary.GDCompressImageSListener;
import com.wgd.gdcp.gdcplibrary.GDConfig;
import com.wgd.gdcp.gdcplibrary.GDImageBean;
import com.wgd.gdcp.gdcplibrary.GDTools;
import com.wgd.gdcp.gdcplibrary.ImageUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "GDCimage";
    @BindView(R.id.btn_select_img)
    Button selectImg ;
    @BindView(R.id.image_view)
    ImageView image_view ;
    @BindView(R.id.btn_img_crop)
    Button btn_img_crop ;
    @BindView(R.id.image_view_c2)
    ImageView image_view_c2 ;
    @BindView(R.id.image_view_c3)
    ImageView image_view_c3 ;
    @BindView(R.id.image_view_crop)
    ImageView image_view_crop ;
    @BindView(R.id.txt_yuantu)
    TextView txt_yuantu ;
    @BindView(R.id.txt_crop)
    TextView txt_crop ;
    @BindView(R.id.txt_c2)
    TextView txt_c2 ;
    @BindView(R.id.txt_c3)
    TextView txt_c3 ;

    private final int REQUEST_CODE_CHOOSE= 1001 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.btn_select_img, R.id.btn_img_crop, R.id.image_view, R.id.image_view_crop, R.id.btn_img_more
    ,R.id.btn_img_c2,R.id.btn_img_c3
    })
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_select_img :
                getRxPermissions();
                break;
            case R.id.btn_img_crop :
                if (null==mSelected || mSelected.size()<=0){
                    Toast.makeText(MainActivity.this, "请先选择图片！", Toast.LENGTH_LONG).show();
                }else {
                    String tempCompressImgPath = mSelectedPath.get(0);//事先准备好的sd卡目录下的图片
//                  单个图片压缩，多线程模式
                  new GDCompressC(MainActivity.this,
                          new GDConfig().setmPath(tempCompressImgPath), new GDCompressImageListener() {
//                          new GDConfig().setmPath(tempCompressImgPath).setChangeWH(true), new GDCompressImageListener() {
                        @Override
                        public void OnSuccess(String path) {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    image_view_crop.setImageBitmap(BitmapFactory
                                            .decodeFile(mSelectedPath.get(0)));
                                    ImageInfoBean imageInfoBean = new ImageGetInfoUtil().getImageInfo(mSelectedPath.get(0));

                                    txt_crop.setText("imageWidth=" + imageInfoBean.getWightHeightSize()[0] + "\n"
                                            + "imageHeight=" + imageInfoBean.getWightHeightSize()[1] + "\n"
                                            + "imageSize=" + imageInfoBean.getFileSize() + "\n"
                                            + "" );
                                }
                            });
                        }

                        @Override
                        public void OnError(int code, String errorMsg) {

                        }
                    });

                }
                break;
            case R.id.btn_img_c2 :
                if (null==mSelected || mSelected.size()<=0){
                    Toast.makeText(MainActivity.this, "请先选择图片！", Toast.LENGTH_LONG).show();
                }else if (1 == mSelected.size()){
//                  单个图片压缩，多线程模式
                    String tempCompressImgPath = mSelectedPath.get(0);//事先准备好的sd卡目录下的图片

                    new GDCompressC(MainActivity.this,
                            new GDConfig().setmPath(tempCompressImgPath), new GDCompressImageListener() {
                        //                          new GDConfig().setmPath(tempCompressImgPath).setChangeWH(true), new GDCompressImageListener() {
                        @Override
                        public void OnSuccess(String path) {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    image_view_c2.setImageBitmap(BitmapFactory
                                            .decodeFile(mSelectedPath.get(0)));
                                    ImageInfoBean imageInfoBean = new ImageGetInfoUtil().getImageInfo(mSelectedPath.get(0));

                                    txt_c2.setText("imageWidth=" + imageInfoBean.getWightHeightSize()[0] + "\n"
                                            + "imageHeight=" + imageInfoBean.getWightHeightSize()[1] + "\n"
                                            + "imageSize=" + imageInfoBean.getFileSize() + "\n"
                                            + "" );
                                }
                            });
                        }

                        @Override
                        public void OnError(int code, String errorMsg) {

                        }
                    });

                }else {
                    // 批量压缩，多线程模式，速度快，但OOM的几率大
                    GDCompressC gdCompressC = new GDCompressC(MainActivity.this, new GDCompressImageListener() {
                        @Override
                        public void OnSuccess(String path) {
                            Log.i(TAG, "OnSuccess: ===批量=====图片压缩=====btn_img_c2========path==" + path);
                        }

                        @Override
                        public void OnError(int code, String errorMsg) {
                            Log.i(TAG, "OnError: ===批量=====图片压缩=====btn_img_c2==========");
                        }
                    });
                    for (int i = 0; i < mSelected.size(); i++) {
                        gdCompressC.start(new GDConfig().setmPath(mSelectedPath.get(i)));
                    }
                }
                break;
            case R.id.btn_img_c3 :
                if (null==mSelected || mSelected.size()<=0){
                    Toast.makeText(MainActivity.this, "请先选择图片！", Toast.LENGTH_LONG).show();
                }else if (1 == mSelected.size()){
//                  单个图片压缩，单线程模式
                    String tempCompressImgPath = mSelectedPath.get(0);//事先准备好的sd卡目录下的图片
                    GDImageBean imageBeana = new GDImageBean();
                    imageBeana.setmGDConfig(new GDConfig().setmPath(tempCompressImgPath));
                    new GDCompressA(MainActivity.this, imageBeana
                            , new GDCompressImageListenerA() {
                        //                          new GDConfig().setmPath(tempCompressImgPath).setChangeWH(true), new GDCompressImageListener() {
                        @Override
                        public void OnSuccess(GDImageBean gdImageBean) {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    image_view_c3.setImageBitmap(BitmapFactory
                                            .decodeFile(mSelectedPath.get(0)));
                                    ImageInfoBean imageInfoBean = new ImageGetInfoUtil().getImageInfo(mSelectedPath.get(0));

                                    txt_c3.setText("imageWidth=" + imageInfoBean.getWightHeightSize()[0] + "\n"
                                            + "imageHeight=" + imageInfoBean.getWightHeightSize()[1] + "\n"
                                            + "imageSize=" + imageInfoBean.getFileSize() + "\n"
                                            + "" );
                                }
                            });
                        }

                        @Override
                        public void OnError(GDImageBean gdImageBean) {

                        }
                    });

                }else {
                    // 批量压缩，单线程模式，速度慢，但OOM几率小很多
                    GDCompressA gdCompressA = new GDCompressA(MainActivity.this, new GDCompressImageListenerA() {
                        @Override
                        public void OnSuccess(GDImageBean gdImageBean) {
                            Log.i(TAG, "OnSuccess: ===批量=====图片压缩=====btn_img_c3========path==" + gdImageBean.getmGDConfig().getmPath());
                        }

                        @Override
                        public void OnError(GDImageBean gdImageBean) {
                            Log.i(TAG, "OnError: ===批量=====图片压缩=====btn_img_c3==========");
                        }
                    });
                    for (int i = 0; i < mSelected.size(); i++) {
                        GDImageBean imageBeana = new GDImageBean();
                        imageBeana.setmGDConfig(new GDConfig().setmPath(mSelectedPath.get(i)));
                        gdCompressA.start(imageBeana);
                    }
                }
                break;
            case R.id.image_view:
            case R.id.image_view_crop:
                BagImageActivity.start(MainActivity.this, mSelectedPath.get(0));
                break;
            case R.id.btn_img_more:
                if (null==mSelected || mSelected.size()<=0){
                    Toast.makeText(MainActivity.this, "请先选择图片！", Toast.LENGTH_LONG).show();
                }else {
//                    批量压缩，单线程模式，速度慢，但OOM几率小很多，内部通过GDCompressA实现
                    showProgress("", false);
                    List<GDImageBean> data = new ArrayList<>();
                    for (int i = 0; i < mSelectedPath.size(); i++) {
                        GDImageBean gdImageBean = new GDImageBean();
                        GDConfig gdConfig = new GDConfig();
                        gdConfig.setmPath(mSelectedPath.get(i));
                        Log.i("GDCimage", "onClick: ===============mSelectedPath.get(i)============" + mSelectedPath.get(i));
                        gdImageBean.setmGDConfig(gdConfig);
                        data.add(gdImageBean);
                    }
                    new GDCompressImageS(MainActivity.this, data, new GDCompressImageSListener() {
                        @Override
                        public void OnSuccess(List<GDImageBean> imageBeanList) {
                            Toast.makeText(MainActivity.this, "图片批量压缩成功！", Toast.LENGTH_LONG).show();
                            hideProgress();
                        }

                        @Override
                        public void OnError(List<GDImageBean> imageBeanList) {
//                           这里的code为0是成功；为1是失败
                            Toast.makeText(MainActivity.this, "图片批量压缩失败！" , Toast.LENGTH_LONG).show();
                            hideProgress();
                        }
                    });
                }
                break;
        }
    }


    private void getRxPermissions(){

        RxPermissions rxPermission = new RxPermissions(MainActivity.this);
        //每条权限单独判断是否同意
//一次直接返回是否全部同意
        rxPermission.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
//                        Toast.makeText(MainActivity.this, "onSubscribe", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Boolean value) {
                        if(value){
//                            Toast.makeText(MainActivity.this, "同意权限", Toast.LENGTH_SHORT).show();
                            getPic();
                        }else {
                            Toast.makeText(MainActivity.this, "拒绝权限", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
//                        Toast.makeText(MainActivity.this, "onError", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
//                        Toast.makeText(MainActivity.this, "onComplete", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void getPic(){
        Matisse.from(MainActivity.this)
                .choose(MimeType.ofAll())//图片类型
                .countable(true)//true:选中后显示数字;false:选中后显示对号
                .maxSelectable(9)//可选的最大数
                .capture(true)//选择照片时，是否显示拍照
                .captureStrategy(new CaptureStrategy(true, "com.wgd.myimagelibrary.fileprovider"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                .imageEngine(new GlideEngine())//图片加载引擎
                .forResult(REQUEST_CODE_CHOOSE);//
    }

    List<Uri> mSelected;
    List<String> mSelectedPath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            try {
                mSelected = Matisse.obtainResult(data);
                mSelectedPath= Matisse.obtainPathResult(data);
                Glide.with(MainActivity.this)
                        .load(mSelected.get(0))
                        .thumbnail(0.1f)
                        .into(image_view);
//                ImageInfoBean imageInfoBean = new ImageGetInfoUtil().getImageInfo(mSelectedPath.get(0));
//                txt_yuantu.setText("imageWidth=" + imageInfoBean.getWightHeightSize()[0] + "\n"
//                                + "imageHeight=" + imageInfoBean.getWightHeightSize()[1] + "\n"
//                                + "imageSize=" + imageInfoBean.getFileSize() + "\n"
//                        + "" );
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    ProgressDialog progressDialog;
    public void showProgress(String message,boolean iscancel) {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                return;
            }
            progressDialog.setMessage(message != null ? message
                    : "loading...");
            progressDialog.show();
        } else {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(iscancel);
            progressDialog.setMessage(message != null ? message
                    : "loading...");
            progressDialog.show();
        }
    }
    public void hideProgress() {
        if (progressDialog != null&&progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
