package com.example.fabianlopezverdugo.mapit;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class PreviewActivity extends AppCompatActivity {

    private ImageView mImage;
    private Bitmap bitmap;
    private VisitorDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        mImage = (ImageView) findViewById(R.id.imagePrew);
        dataSource = new VisitorDataSource(this);
        dataSource.open();
    }

    public void takePicture(View v){
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK && data != null){
            Bundle extract = data.getExtras();
            bitmap = (Bitmap)extract.get("data");
            mImage.setImageBitmap(bitmap);
            EditText mDescriptionText = (EditText) findViewById(R.id.editTextDesc);
            EditText mName = (EditText) findViewById(R.id.editTextName);
            String name = mName.getText().toString();
            String desc = mDescriptionText.getText().toString();
            saveImageToExternalStorage(bitmap,name,desc);
            Log.v("CAMERA:","Photo changed correctly");
        }
        Log.v("CAMERA:","Photo not changed correctly");
    }

    private void saveImageToExternalStorage(Bitmap finalBitmap,String name, String desc) {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";

        System.out.println("Name"+name+"|Desc"+desc);
        dataSource.createUser(name,desc,fname);
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(this, new String[] { file.toString() }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.v("WRITE", "Photo Scanned");
                    }
                });

    }

    public void onClickPublishButton(View view){
        SimpleNotification(1,"MapIt","Your photo has been published!");
        Intent intent = new Intent(this, ViewAllActivity.class);
        startActivity(intent);
    }

    private void SimpleNotification(int id, String title, String content) {

        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setColor(getResources().getColor(R.color.colorPrimary))
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(id, builder.build());

    }
}
