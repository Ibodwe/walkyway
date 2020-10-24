package com.example.walkway.upload;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.example.walkway.R;
import com.example.walkway.profile.ProfileActivity;
import com.example.walkway.walkingwalkway.StartWalkwayActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


// 이미지 업로드 액티비티
public class ImgUploadActivity  extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "ImgUploadActivity";
    public static final int sub = 1001;

    AmazonS3 s3;
    TransferUtility transferUtility;

    Button uploadBtn, selectBtn;
    ImageView imageview;
    File f;

    private String userChoosenTask;
    Uri imageUri;
    String imagePath;
    private Uri mImageUri;
    private int PICTURE_CHOICE = 1;
    private int REQUEST_CAMERA = 2;
    private int SELECT_FILE = 3;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
// 스토리지 퍼미션 확인(안되는 듯)
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    // 이미지 업로드 시작화면 출력
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_upload);

        uploadBtn = (Button) findViewById(R.id.uploadBtn);
        selectBtn = (Button) findViewById(R.id.selectBtn);
        imageview = (ImageView) findViewById(R.id.imageview1);

        uploadBtn.setOnClickListener(this);
        selectBtn.setOnClickListener(this);



// Amazon Cognito 인증 공급자를 초기화합니다
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-northeast-2:8c082547-7189-4058-9fc6-3bd3d7a2464a", // 자격 증명 풀 ID
                Regions.AP_NORTHEAST_2 // 리전
        );

        s3 = new AmazonS3Client(credentialsProvider, Region.getRegion(Regions.AP_NORTHEAST_2));

        transferUtility = new TransferUtility(s3, getApplicationContext());
        s3.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
        s3.setEndpoint("s3.ap-northeast-2.amazonaws.com");
        TransferNetworkLossHandler.getInstance(getApplicationContext());

        // 아래 코드 입력 시, '사진 촬영' 선택해도 꺼지지 않음 (StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());) 에러 해결
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    // 버튼 선택 시, 해당 기능 실행
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.uploadBtn:
                TransferObserver observer = transferUtility.upload(
                        "serverless-img-bucket/account_profile",
                        f.getName(),
                        f
                );
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivityForResult(intent,sub);
                break;
            case R.id.selectBtn:
                selectImage();
                break;
        }
    }

    // 사진 선택 버튼 클릭 시 액션 선택하기
    private void selectImage() {

        Log.d(TAG, "select Image");
        final CharSequence[] items = {"촬영하기", "사진 가져오기",
                "취소"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("사진가져오기");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getApplicationContext());

                if (items[item].equals("촬영하기")) {
                    userChoosenTask = "촬영하기";
                    if (result)
                        cameraIntent();


                } else if (items[item].equals("사진 가져오기")) {
                    userChoosenTask = "사진 가져오기";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("취소")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    // 앨범에서 선택하기
    private void galleryIntent() {
        Log.d(TAG, "Gallery Intent");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    // 사진 찍기 선택
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File photo = null;
        try {
            // place where to store camera taken picture
            photo = this.createTemporaryFile("picture", ".jpg");
            photo.delete();
        } catch (Exception e) {
            Log.v(TAG, "Can't create file to take picture!");
            Toast.makeText(this, "sd카드를 확인해주세요", Toast.LENGTH_SHORT);
        }
        mImageUri = PhotoProvider.getPhotoUri(photo);
        Log.d(TAG, mImageUri.toString());
        if (Build.VERSION.SDK_INT <= 19) {

        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        }
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    // 사진 찍기로 생성된 사진을 저장하는 임시 파일 생성
    private File createTemporaryFile(String part, String ext) throws Exception {
        File tempDir = Environment.getExternalStorageDirectory();
        tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        return File.createTempFile(part, ext, tempDir);
    }

    // 사진 찍기로 생성된 사진을 선택
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                Log.d(TAG, "onActivityResult, SELECT_FILE");
                onSelectFromGalleryResult(data, SELECT_FILE);
            } else if (requestCode == REQUEST_CAMERA) {
                try {
                    onCaptureImageResult(data, REQUEST_CAMERA);
                } catch (Exception e) {
                    this.grabImage(imageview, REQUEST_CAMERA);
                }
            }

        }
    }

    // 사진 포맷과 규격에 맞춰 변경
    private void onCaptureImageResult(Intent data, int imagetype) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        Bitmap bm = null;
        bm = null;
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        Bitmap correctBmp = null;
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();

            File f = new File(RealPathUtil.getRealPathFromURI_API19(this, data.getData()));
            ExifInterface exif = new ExifInterface(f.getPath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            int angle = 0;

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                angle = 90;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                angle = 180;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                angle = 270;
            }

            Matrix mat = new Matrix();
            mat.postRotate(angle);

            Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f), null, null);
            correctBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bm = getResizedBitmap(correctBmp, getResources().getDimensionPixelSize(R.dimen.idcard_pic_height), getResources().getDimensionPixelSize(R.dimen.idcard_pic_width));
        if (imagetype == REQUEST_CAMERA) {
            f = SaveImage(bm);
            imageview.setImageBitmap(bm);
        }
    }

    // 앨범에서 생성된 이미지 규격에 맞춰 변경
    private void onSelectFromGalleryResult(Intent data, int imagetype) {

        Log.d(TAG, "onSelectFromGalleryResult");
        Bitmap bm = null;
        imageUri = data.getData();
        if (Build.VERSION.SDK_INT < 11) {
            imagePath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, imageUri);
            Log.d(TAG, Build.VERSION.SDK_INT + "");
        } else if (Build.VERSION.SDK_INT < 19) {
            Log.d(TAG, Build.VERSION.SDK_INT + "");
            imagePath = RealPathUtil.getRealPathFromURI_API11to18(this, imageUri);
        } else {
            Log.d(TAG, Build.VERSION.SDK_INT + "");
            imagePath = RealPathUtil.getRealPathFromURI_API19(this, imageUri);
        }
        Log.d(TAG, imagePath);


        try {
            bm = getResizedBitmap(decodeUri(data.getData()), getResources().getDimensionPixelSize(R.dimen.idcard_pic_height), getResources().getDimensionPixelSize(R.dimen.idcard_pic_width));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (imagetype == SELECT_FILE) {
            f = new File(imagePath);
            imageview.setImageBitmap(bm);
        }
    }

    // 사진 규격
    public static Bitmap getResizedBitmap(Bitmap image, int newHeight, int newWidth) {
        int width = image.getWidth();
        int height = image.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        if (Build.VERSION.SDK_INT <= 19) {
            //matrix.postRotate(90);
        }
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(image, 0, 0, width, height,
                matrix, false);
        return resizedBitmap;
    }


    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                this.getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 100;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(
                this.getContentResolver().openInputStream(selectedImage), null, o2);
    }

    // 이미지 파일 bitmap 형식으로 저장
    public File SaveImage(Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/MobileCard");

        if (!myDir.exists()) {
            Log.d("SaveImage", "non exists : " + myDir);
            myDir.mkdirs();
        }

        long now = System.currentTimeMillis();
        String fname = now + ".jpg";
        File file = new File(myDir, fname);

        if (file.exists()) {
            Log.d("SaveImage", "file exists");
            file.delete();
        } else {
            Log.d("SaveImage", "file non exists");
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            Log.d("SaveImage", "file save");
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }
    public void grabImage(ImageView imgView, int imagetype) {

        this.getContentResolver().notifyChange(mImageUri, null);
        ContentResolver cr = this.getContentResolver();
        Bitmap bitmap = null;
        Bitmap bm = null;
        Bitmap correctBmp = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(cr, mImageUri);

            File f = new File(RealPathUtil.getRealPathFromURI_API19(this, mImageUri));
            ExifInterface exif = new ExifInterface(f.getPath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            int angle = 0;

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                angle = 90;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                angle = 180;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                angle = 270;
            }

            Matrix mat = new Matrix();
            mat.postRotate(angle);

            Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f), null, null);
            correctBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);
            Toast.makeText(this, "불러오기에 성공했습니다", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "불러오기에 실패했습니다", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Failed to load", e);
        }
        bm = getResizedBitmap(correctBmp, getResources().getDimensionPixelSize(R.dimen.idcard_pic_height), getResources().getDimensionPixelSize(R.dimen.idcard_pic_width));

        if (imagetype == REQUEST_CAMERA) {
            f = SaveImage(bm);
            imageview.setImageBitmap(bm);
        }
    }

}