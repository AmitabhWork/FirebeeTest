package ahh.com.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import ahh.com.app.beans.BannerDtl;
import ahh.com.app.beans.DataBinder;
import ahh.com.app.util.RealPathUtil;

import static ahh.com.app.beans.constantVariables.BANNER_IMAGE_DIR_URL;

public class Activity_AddGallary extends AppCompatActivity {

    private Spinner selectedGalleryTitleSpinner;
    private Button uploadImage;
    private Button pickImage;
    private ImageView selectedImage;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseStorage storage;
    private ArrayList<DataBinder> galleryTitleArray;
    private String realPath;
    private Uri uriFromPath;
    private String fileName;
    private Button addTitle;
    private ArrayAdapter<DataBinder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__add_gallary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("gallery_title");
        storage = FirebaseStorage.getInstance();
        galleryTitleArray = new ArrayList<DataBinder>();

        selectedGalleryTitleSpinner = (Spinner) findViewById(R.id.galleryTitleSpin);
        selectedImage = (ImageView) findViewById(R.id.selectedImage);
        pickImage = (Button) findViewById(R.id.selectImage);
        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        uploadImage = (Button) findViewById(R.id.uploadImage);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uriFromPath != null) {
//                    addBannerData();
                } else {
                    Toast.makeText(getApplicationContext(), "Image path not found", Toast.LENGTH_LONG).show();
                }

            }
        });
        addTitle = (Button) findViewById(R.id.addTitle);
        addTitle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Create custom dialog object
                final Dialog dialog = new Dialog(Activity_AddGallary.this);
                // Include dialog.xml file
                dialog.setContentView(R.layout.dialog_gallerytitle);
                // Set dialog title
                dialog.setTitle("Custom Dialog");

                // set values for custom dialog components - text, image and button
                final EditText text = (EditText) dialog.findViewById(R.id.g_title);


                dialog.show();

                Button declineButton = (Button) dialog.findViewById(R.id.btn_ok);
                // if decline button is clicked, close the custom dialog
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Close dialog

                        String title = text.getText().toString();
                        if (title != "" && title != null && !title.isEmpty()) {
                            addGallaryTitleinDb(title);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "Title Cannot be Empty", Toast.LENGTH_LONG).show();

                        }


                    }
                });

            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = new ArrayAdapter<DataBinder>(this
                , android.R.layout.simple_spinner_item, galleryTitleArray);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        selectedGalleryTitleSpinner.setAdapter(adapter);
        getTitlesFromFB();
        selectedGalleryTitleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void hideInputForm(Boolean val) {
        if (val == true) {
            pickImage.setVisibility(View.GONE);
            uploadImage.setVisibility(View.VISIBLE);
        } else {
            pickImage.setVisibility(View.VISIBLE);
            uploadImage.setVisibility(View.GONE);
        }

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
        uriFromPath = Uri.fromFile(new File(realPath));
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uriFromPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        selectedImage.setImageBitmap(bitmap);
        hideInputForm(true);
        Log.d("HMKCODE", "Build.VERSION.SDK_INT:" + sdk);
        Log.d("HMKCODE", "URI Path:" + uriPath);
        Log.d("HMKCODE", "Real Path: " + realPath);
    }

    private void addGallaryTitleinDb(String title) {
        String key = myRef.push().getKey();
        Log.e("addGallaryTitleinDb ", "key " + key);

        BannerDtl bdt = new BannerDtl();
        bdt.setPriority(0);
        bdt.setName(title);

        myRef.child(key).setValue(bdt, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Toast.makeText(getApplicationContext(), "Title NOT saved", Toast.LENGTH_LONG).show();
                } else {
                    fileName = databaseReference.getKey();
                    Toast.makeText(getApplicationContext(), "Title saved successfully", Toast.LENGTH_LONG).show();
                    getTitlesFromFB();
                }
            }
        });
    }

    private void getTitlesFromFB() {
        galleryTitleArray.clear();
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DataBinder db = new DataBinder();
                db.setKey(dataSnapshot.getKey());
                db.setValue(dataSnapshot.child("name").getValue().toString());
                galleryTitleArray.add(db);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Log.e("onChildAdded", dataSnapshot.toString());
                Log.e("getChildrenCount", dataSnapshot.getChildrenCount() + "");
                Log.e("getKey", dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                Log.e("onChildAdded", dataSnapshot.toString());
                Log.e("getChildrenCount", dataSnapshot.getChildrenCount() + "");
                Log.e("getKey", dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                Log.e("onChildAdded", dataSnapshot.toString());
                Log.e("getChildrenCount", dataSnapshot.getChildrenCount() + "");
                Log.e("getKey", dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.e("onCancelled", databaseError.toString());

            }

        });


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
                hideInputForm(false);
                Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_LONG).show();


            }
        });


    }

}
