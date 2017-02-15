package com.zenglb.framework.activity.setHeadImg;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.zenglb.commonlib.base.BaseActivity;
import com.zenglb.commonlib.utils.FileCachePathConfig;
import com.zenglb.commonlib.utils.FileStorage;
import com.zenglb.framework.R;

import java.io.File;

/**
 * 拍照或者从相册中选取照片，然后裁剪好；并显示裁剪好的照片
 * <p>
 * -- Warming --
 * 2.请注意Android 6.0 后面的权限检查,请先去检查权限
 * 3.测试要注意小于4.4   等于4.4  大于 4.4 5 6 7 来分别的测试
 */
public class SetHeadImgActivity extends BaseActivity implements View.OnClickListener {
    private static final int REQUEST_PICK_IMAGE = 1;   //相册选取
    private static final int REQUEST_CAPTURE = 2;      //拍照
    private static final int REQUEST_PICTURE_CUT = 3;  //剪裁图片
    private ImageView iv;
    private Button take_btn, album_btn;

    private Uri imageUri;        //拍照的原图保存地址
    private String imagePath;    //相册选择的原图地址
    private Uri cropImgUri;      //裁剪过的图片地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected int setLayoutId() {
        return R.layout.activity_set_head_img;
    }

    protected void initViews() {
        take_btn = (Button) findViewById(R.id.take_btn);
        album_btn = (Button) findViewById(R.id.album_btn);
        iv = (ImageView) findViewById(R.id.iv);
        take_btn.setOnClickListener(this);
        album_btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.take_btn:
                openCamera();
                break;
            case R.id.album_btn:
                selectFromAlbum();
                break;
        }
    }

    /**
     * 打开系统相机
     */
    private void openCamera() {
        File file = new FileStorage(FileCachePathConfig.CACHE_IMAGE_CHILD).createTempFile("temp.jpg");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(SetHeadImgActivity.this, "com.zenglb.framework.fileprovider", file);//通过FileProvider创建一个content类型的Uri
        } else {
            imageUri = Uri.fromFile(file);
        }
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);          //设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);         //将拍取的照片保存到指定URI
        startActivityForResult(intent, REQUEST_CAPTURE);
    }

    /**
     * 从相册选择
     */
    private void selectFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }


    /**
     * 裁剪相片
     */
    private void cropPhoto() {
        File file = new FileStorage(FileCachePathConfig.CACHE_IMAGE_CHILD).createTempFile("temp.jpg");
        cropImgUri = Uri.fromFile(file);//缩略图保存地址
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 400);

        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImgUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_PICTURE_CUT);
    }


    private void handleImageBeforeKitKat(Intent intent) {
        imageUri = intent.getData();
        imagePath = getImagePath(imageUri, null);
        cropPhoto();
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        if (data == null || data.getData() == null) {
            Toast.makeText(this, "你放弃了操作", Toast.LENGTH_SHORT).show();
            return;
        }
        imagePath = null;
        imageUri = data.getData();
        if (DocumentsContract.isDocumentUri(this, imageUri)) {
            //如果是document类型的uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(imageUri);
            if ("com.android.providers.media.documents".equals(imageUri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.downloads.documents".equals(imageUri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(imageUri, null);
        } else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            //如果是file类型的Uri,直接获取图片路径即可
            imagePath = imageUri.getPath();
        }
        cropPhoto();
    }

    /**
     * @param uri
     * @param selection
     * @return
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection老获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PICK_IMAGE://从相册选择
                if (Build.VERSION.SDK_INT >= 19) {
                    handleImageOnKitKat(data);
                } else {
                    handleImageBeforeKitKat(data);
                }
                break;
            case REQUEST_CAPTURE://拍照
                if (resultCode == RESULT_OK) {
                    cropPhoto();
                }
                break;
            case REQUEST_PICTURE_CUT://裁剪完成
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(cropImgUri));
                    iv.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
