package ahh.com.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
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
import com.nononsenseapps.filepicker.FilePickerActivity;

import java.util.ArrayList;

import ahh.com.app.beans.CoreMember;

import static ahh.com.app.beans.constantVariables.CORETEAM_PROFILEPIC_URL;

public class Activity_AddMember extends AppCompatActivity {

    private static final int FILE_CODE = 10101;
    private Button submit;
    private EditText firstName_et;
    private EditText lastName_et;
    private EditText email_et;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Button tstbtn;
    //    private CoreMember member;
    private CardView formView;
    private CardView imgUploaderView;
    private Button uploadImage;
    private FirebaseStorage storage;
    private ImageView memberImg;
    private String memberKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("core_members");
        storage = FirebaseStorage.getInstance();


        addViews();
        setListeners();
    }

    private void setListeners() {
        submit.setOnClickListener(new SubmitEvent());
        uploadImage.setOnClickListener(new PickFile());
    }

    private void addViews() {
        submit = (Button) findViewById(R.id.submit);
        firstName_et = (EditText) findViewById(R.id.firstName);
        lastName_et = (EditText) findViewById(R.id.lastname);
        email_et = (EditText) findViewById(R.id.emailid);
        formView = (CardView) findViewById(R.id.card_form);
        imgUploaderView = (CardView) findViewById(R.id.imageupload_card);
        uploadImage = (Button) findViewById(R.id.upload);
        memberImg = (ImageView) findViewById(R.id.member_img);


    }


    private class SubmitEvent implements View.OnClickListener {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {

            String firstName = firstName_et.getText().toString();
            String lastName = lastName_et.getText().toString();
            String emailId = email_et.getText().toString();

            addMember(firstName, lastName, emailId);

        }
    }

    /**
     * Call to add new CoreMember data to firebase
     *
     * @param firstName
     * @param lastName
     * @param emailId
     */
    private void addMember(String firstName, String lastName, String emailId) {
        Log.e("AddMember", "Fn " + firstName + ", Ln " + lastName + "eid " + emailId);


//        myRef.setValue("Hello, World!");
        String key = myRef.push().getKey();
        Log.e("AddMember", "key " + key);

        CoreMember member = new CoreMember(firstName, lastName, emailId);
        myRef.child(key).setValue(member, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println("Data could not be saved " + databaseError.getMessage());
                } else {
                    System.out.println("Data saved successfully." + databaseReference.toString());
                    memberKey = databaseReference.getKey();
                    hideInputForm(true);

                }
            }
        });
    }


    private void hideInputForm(Boolean val) {
        if (val == true) {
            formView.setVisibility(View.GONE);
            imgUploaderView.setVisibility(View.VISIBLE);
        } else {
            formView.setVisibility(View.VISIBLE);
            imgUploaderView.setVisibility(View.GONE);
        }

    }


    private void getData(String emailId) {
        myRef.orderByChild("email").startAt(emailId).endAt(emailId).addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            Log.e("onChildAdded", dataSnapshot.toString());
            Log.e("getChildrenCount", dataSnapshot.getChildrenCount() + "");
            Log.e("getKey", dataSnapshot.getKey());

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


    private void uploadImage(Uri file) {

        StorageReference storageRef = storage.getReferenceFromUrl(CORETEAM_PROFILEPIC_URL);

        StorageReference riversRef = storageRef.child("CoreTeamPic/" + memberKey + ".jpg");
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
                StorageReference downloadUrl = taskSnapshot.getStorage();
                downloadImage(downloadUrl);
                Log.e("downloadUrl", " " + downloadUrl);


            }
        });


    }

    private void downloadImage(StorageReference downloadUrl) {


        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(downloadUrl)
                .into(memberImg);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_CODE && resultCode == Activity.RESULT_OK) {
            if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {
                // For JellyBean and above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ClipData clip = data.getClipData();

                    if (clip != null) {
                        for (int i = 0; i < clip.getItemCount(); i++) {
                            Uri uri = clip.getItemAt(i).getUri();
                            // Do something with the URI
                            uploadImage(uri);
                        }
                    }
                    // For Ice Cream Sandwich
                } else {
                    ArrayList<String> paths = data.getStringArrayListExtra
                            (FilePickerActivity.EXTRA_PATHS);

                    if (paths != null) {
                        for (String path : paths) {
                            Uri uri = Uri.parse(path);
                            // Do something with the URI
                            uploadImage(uri);

                        }
                    }
                }

            } else {
                Uri uri = data.getData();
                // Do something with the URI
                uploadImage(uri);

            }
        }
    }

    class PickFile implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // This always works
            Intent i = new Intent(getApplicationContext(), FilePickerActivity.class);
            // This works if you defined the intent filter
            // Intent i = new Intent(Intent.ACTION_GET_CONTENT);

            // Set these depending on your use case. These are the defaults.
            i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
            i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
            i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);

            // Configure initial directory by specifying a String.
            // You could specify a String like "/storage/emulated/0/", but that can
            // dangerous. Always use Android's API calls to get paths to the SD-card or
            // internal memory.
            i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());

            startActivityForResult(i, FILE_CODE);
        }
    }
}
