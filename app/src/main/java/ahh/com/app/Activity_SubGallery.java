package ahh.com.app;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;


import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import ahh.com.app.adapter.GalleryAdapter;
import ahh.com.app.model.Image;

import static ahh.com.app.beans.constantVariables.CORETEAM_PROFILEPIC_URL;

public class Activity_SubGallery extends AppCompatActivity {

    private String TAG = Activity_SubGallery.class.getSimpleName();
    private static final String endpoint = "http://api.androidhive.info/json/glide.json";
    private ArrayList<Image> images;
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private StorageReference storageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__sub_gallery);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FirebaseStorage storage = FirebaseStorage.getInstance();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("core_members");
        storageRef = storage.getReferenceFromUrl(CORETEAM_PROFILEPIC_URL);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        pDialog = new ProgressDialog(this);
        images = new ArrayList<>();
        mAdapter = new GalleryAdapter(getApplicationContext(), images);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

//        fetchImages();
        getCoreMemberList();
    }

//    private void fetchImages() {
//
//        Image image = new Image();
//        image.setName("Amitabh");
//
//
//        image.setSmall("http://api.androidhive.info/images/glide/small/deadpool.jpg");
//        image.setMedium("http://api.androidhive.info/images/glide/medium/deadpool.jpg");
//        image.setLarge("http://api.androidhive.info/images/glide/large/deadpool.jpg");
//        image.setTimestamp("February 12, 2016");
//
//        images.add(image);
//        Image image1 = new Image();
//        image.setName("Amitabh");
//
//
//        image1.setSmall("http://api.androidhive.info/images/glide/small/deadpool.jpg");
//        image1.setMedium("http://api.androidhive.info/images/glide/medium/deadpool.jpg");
//        image1.setLarge("http://api.androidhive.info/images/glide/large/deadpool.jpg");
//        image1.setTimestamp("February 12, 2016");
//
//        images.add(image1);
//        mAdapter.notifyDataSetChanged();
//
//    }

    private void getCoreMemberList() {

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("Count ", "" + dataSnapshot.getChildrenCount());

//                ArrayList<CoreMember> membersKeyList = new ArrayList<CoreMember>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    CoreMember post = postSnapshot.getValue(CoreMember.class);
                    String key = postSnapshot.getKey();
                    Log.e("Get Data", key + " , " + postSnapshot.toString());

//                    post.setKey(postSnapshot.getKey());
//                    membersKeyList.add(post);
                    StorageReference riversRef = storageRef.child("CoreTeamPic/" + key + ".jpg");
//                    Task<Uri> url = riversRef.getDownloadUrl();
//
//                    Log.e("Get Data", "URI" + " , " + url.toString());

                    Image image = new Image();
                    image.setName("Amitabh");
                    image.setSmall(riversRef);
                    image.setMedium(riversRef);
                    image.setLarge(riversRef);
                    image.setTimestamp("February 12, 2016");
                    images.add(image);
                    mAdapter.notifyDataSetChanged();
                }


//                mAdapter = new MyRecyclerViewAdapter(getDataSet(membersKeyList), getApplicationContext());
//                mRecyclerView.setAdapter(mAdapter);
//                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
    }


//    private void fetchImages() {
//
//        pDialog.setMessage("Downloading json...");
//        pDialog.show();
//
//        JsonArrayRequest req = new JsonArrayRequest(endpoint,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.d(TAG, response.toString());
//                        pDialog.hide();
//
//                        images.clear();
//                        for (int i = 0; i < response.length(); i++) {
//                            try {
//                                JSONObject object = response.getJSONObject(i);
//                                Image image = new Image();
//                                image.setName(object.getString("name"));
//
//                                JSONObject url = object.getJSONObject("url");
//                                image.setSmall(url.getString("small"));
//                                image.setMedium(url.getString("medium"));
//                                image.setLarge(url.getString("large"));
//                                image.setTimestamp(object.getString("timestamp"));
//
//                                images.add(image);
//
//                            } catch (JSONException e) {
//                                Log.e(TAG, "Json parsing error: " + e.getMessage());
//                            }
//                        }
//
//                        mAdapter.notifyDataSetChanged();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Error: " + error.getMessage());
//                pDialog.hide();
//            }
//        });
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(req);
//    }
}