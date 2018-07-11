package com.wgd.myimagelibrary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BagImageActivity extends AppCompatActivity {

    @BindView(R.id.image)
    ImageView image ;
    @BindView(R.id.txt_image_info)
    TextView txt_image_info ;

    public static void start(Activity activity, String path){
        Intent intent = new Intent(activity, BagImageActivity.class);
        intent.putExtra("path", path);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_bag);
        ButterKnife.bind(this);
        String path = getIntent().getStringExtra("path");
        image.setImageBitmap(BitmapFactory
                .decodeFile(path));
//        Bitmap bitmap = BitmapFactory.decodeFile(path);

//        ImageInfoBean imageInfoBean = new ImageGetInfoUtil().getImageInfo(path);
//
//        txt_image_info.setText("imageWidth=" + imageInfoBean.getWightHeightSize()[0] + "\n"
//                + "imageHeight=" + imageInfoBean.getWightHeightSize()[1] + "\n"
//                + "imageSize=" + imageInfoBean.getFileSize() + "\n"
////                                            + "imageWidthExif=" + imageInfo.getmExifInterface().getAttribute(ExifInterface.TAG_IMAGE_LENGTH ) + "\n"
////                                            + "imageHeightExif=" + imageInfo.getmExifInterface().getAttribute(ExifInterface.TAG_IMAGE_LENGTH ) + "\n"
//                + "" );

//        ImageInfo imageInfo = ImageInfoUtils.getImageInfo(BagImageActivity.this, bitmap);
//        txt_image_info.setText("imageWidth=" + imageInfo.getImageWidth() + "\n"
//                + "imageHeight=" + imageInfo.getImageHeight() + "\n"
//                + "imageSize=" + imageInfo.getImageSize() + "\n"
//                + "imageWidthExif=" + imageInfo.getmExifInterface().getAttribute(ExifInterface.TAG_IMAGE_WIDTH ) + "\n"
//                + "imageHeightExif=" + imageInfo.getmExifInterface().getAttribute(ExifInterface.TAG_IMAGE_LENGTH ) + "\n"
//                + "" );

    }
}
