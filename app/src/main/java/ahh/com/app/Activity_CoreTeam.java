package ahh.com.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;
import java.util.List;

import ahh.com.app.beans.CoreMember;

import static ahh.com.app.beans.constantVariables.CORETEAM_PROFILEPIC_URL;


public class Activity_CoreTeam extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "Activity_CoreTeam";

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private StorageReference storageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__core_team);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("core_members");
        storageRef = storage.getReferenceFromUrl(CORETEAM_PROFILEPIC_URL);

        StorageReference riversRef = storageRef.child("CoreTeamPic/" + "-K_EbhsiV0CfJ2tJuVhp" + ".jpg");

//        StorageReference fileRef = storageRef.child("amitabh.jpg");
        String imgPath = riversRef.getPath();
//        ImageView testimg = (ImageView) findViewById(R.id.testimg);
//
//        Glide.with(this)
//                .using(new FirebaseImageLoader())
//                .load(riversRef)
//                .into(testimg);

//        testimg.setImageURI(Uri.parse(imgPath));
        Log.d("log", imgPath);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        getCoreMemberList();
    }

    private ArrayList<DataObject> getDataSet(List<CoreMember> membersData) {
        ArrayList results = new ArrayList<DataObject>();
        for (int index = 0; index < membersData.size(); index++) {
            CoreMember md = membersData.get(index);
            StorageReference riversRef = storageRef.child("CoreTeamPic/" + md.getKey() + ".jpg");
            DataObject obj = new DataObject(md.getFirstname() + " " + md.getLastname(), riversRef);

            results.add(index, obj);
        }
        return results;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (mAdapter != null) {
//            ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
//                    .MyClickListener() {
//                @Override
//                public void onItemClick(int position, View v) {
//                    Log.i(LOG_TAG, " Clicked on Item " + position);
//                }
//            });
//        }
    }

    private void getCoreMemberList() {

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("Count ", "" + dataSnapshot.getChildrenCount());

                ArrayList<CoreMember> membersKeyList = new ArrayList<CoreMember>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    CoreMember post = postSnapshot.getValue(CoreMember.class);

                    Log.e("Get Data", postSnapshot.getKey() + " , " + post.getEmail() + " , " + post.getFirstname() + " , " + post.getLastname());

                    post.setKey(postSnapshot.getKey());
                    membersKeyList.add(post);
                }


                mAdapter = new MyRecyclerViewAdapter(getDataSet(membersKeyList), getApplicationContext());
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
    }


}
