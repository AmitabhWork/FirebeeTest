package ahh.com.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nononsenseapps.filepicker.FilePickerActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import ahh.com.app.beans.BannerDtl;
import ahh.com.app.util.RealPathUtil;

import static ahh.com.app.beans.constantVariables.BANNER_IMAGE_DIR_URL;
import static android.provider.MediaStore.*;
import static android.provider.MediaStore.Images.*;

public class Activity_AddBanner extends AppCompatActivity {

    private static final int PICK_PHOTO_FOR_BANNER = 1121;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseStorage storage;
    private String fileName;
    private Button uploadImage;
    private ImageView imageView;
    private String realPath;
    private Uri uriFromPath;
    private Button selectImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__add_banner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("front_banner");
        storage = FirebaseStorage.getInstance();
        uploadImage = (Button) findViewById(R.id.updoad_image);
        imageView = (ImageView) findViewById(R.id.imageView2);
        selectImage = (Button) findViewById(R.id.selectImage);
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uriFromPath != null) {
                    addBannerData();
                } else {
                    Toast.makeText(getApplicationContext(), "Image path not found", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void hideInputForm(Boolean val) {
        if (val == true) {
            selectImage.setVisibility(View.GONE);
            uploadImage.setVisibility(View.VISIBLE);
        } else {
            selectImage.setVisibility(View.VISIBLE);
            uploadImage.setVisibility(View.GONE);
        }

    }

    private void addBannerData() {
        String key = myRef.push().getKey();
        Log.e("addBannerData", "key " + key);

        BannerDtl bdt = new BannerDtl();
        bdt.setPriority(0);
        bdt.setName(key);

        myRef.child(key).setValue(bdt, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println("Data could not be saved " + databaseError.getMessage());
                } else {
                    System.out.println("Data saved successfully." + databaseReference.toString());
                    fileName = databaseReference.getKey();

                    if (uriFromPath != null) {
                        uploadImage(uriFromPath);
                    } else {
                        Toast.makeText(getApplicationContext(), "Image path not found", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


    private void uploadImage(Uri file) {

        StorageReference storageRef = storage.getReferenceFromUrl(BANNER_IMAGE_DIR_URL);

        StorageReference riversRef = storageRef.child("front_banner/" + fileName + ".jpg");
        UploadTask uploadTask = riversRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                hideInputForm(false);
                Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_LONG).show();
//                StorageReference downloadUrl = taskSnapshot.getStorage();
////                downloadImage(downloadUrl);
//                Log.e("downloadUrl", " " + downloadUrl);


            }
        });


    }
//    private void downloadImage(StorageReference downloadUrl) {
//
//
//        Glide.with(this)
//                .using(new FirebaseImageLoader())
//                .load(downloadUrl)
//                .into(memberImg);
//    }

//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == FILE_CODE && resultCode == Activity.RESULT_OK) {
//            if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {
//                // For JellyBean and above
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    ClipData clip = data.getClipData();
//
//                    if (clip != null) {
//                        for (int i = 0; i < clip.getItemCount(); i++) {
//                            Uri uri = clip.getItemAt(i).getUri();
//                            // Do something with the URI
//                            uploadImage(uri);
//                        }
//                    }
//                    // For Ice Cream Sandwich
//                } else {
//                    ArrayList<String> paths = data.getStringArrayListExtra
//                            (FilePickerActivity.EXTRA_PATHS);
//
//                    if (paths != null) {
//                        for (String path : paths) {
//                            Uri uri = Uri.parse(path);
//                            // Do something with the URI
//                            uploadImage(uri);
//
//                        }
//                    }
//                }
//
//            } else {
//                Uri uri = data.getData();
//                // Do something with the URI
//                uploadImage(uri);
//
//            }
//        }
//    }

//    private class PickFile implements View.OnClickListener {
//
//        @Override
//        public void onClick(View v) {
//            // This always works
//            Intent i = new Intent(getApplicationContext(), FilePickerActivity.class);
//            // This works if you defined the intent filter
//            // Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//
//            // Set these depending on your use case. These are the defaults.
//            i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
//            i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
//            i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
//
//            // Configure initial directory by specifying a String.
//            // You could specify a String like "/storage/emulated/0/", but that can
//            // dangerous. Always use Android's API calls to get paths to the SD-card or
//            // internal memory.
//            i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
//
//            startActivityForResult(i, FILE_CODE);
//        }
//    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_BANNER);
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        if (resCode == Activity.RESULT_OK && data != null) {

            // SDK < API11
            if (Build.VERSION.SDK_INT < 11)
                realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());

                // SDK >= 11 && SDK < 19
            else if (Build.VERSION.SDK_INT < 19)
                realPath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());

                // SDK > 19 (Android 4.4)
            else
                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());


            setTextViews(Build.VERSION.SDK_INT, data.getData().getPath(), realPath);
        }
    }

    private void setTextViews(int sdk, String uriPath, String realPath) {

//        this.txtSDK.setText("Build.VERSION.SDK_INT: " + sdk);
//        this.txtUriPath.setText("URI Path: " + uriPath);
//        this.txtRealPath.setText("Real Path: " + realPath);

        uriFromPath = Uri.fromFile(new File(realPath));

        // you have two ways to display selected image

        // ( 1 ) imageView.setImageURI(uriFromPath);

        // ( 2 ) imageView.setImageBitmap(bitmap);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uriFromPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(bitmap);
        hideInputForm(true);
        Log.d("HMKCODE", "Build.VERSION.SDK_INT:" + sdk);
        Log.d("HMKCODE", "URI Path:" + uriPath);
        Log.d("HMKCODE", "Real Path: " + realPath);
    }

}


//    public String getRealPathFromURI(Uri uri) {
//        String[] projection = new String[]{Media.DATA};
//        @SuppressWarnings("deprecation")
//        Cursor cursor = managedQuery(uri, projection, null, null, null);
//        int column_index = cursor
//                .getColumnIndexOrThrow(Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//    }
//    public String getPath(Uri uri) {
//        String result = null;
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
//        if (cursor.moveToFirst()) {
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            result = cursor.getString(column_index);
//        }
//        cursor.close();
//        return result;
//    }
//}
