package ahh.com.ahh;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;


public class Activity_CoreTeam extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "Activity_CoreTeam";
    String[] imageUrl = {
            "http://greenleafaquariums.com/content/images/buonpane1.jpg",
            "http://greenleafaquariums.com/content/images/buonpane2.jpg",
            "http://greenleafaquariums.com/content/images/wong1.jpg",
            "http://greenleafaquariums.com/content/images/wong2.jpg",
            "http://greenleafaquariums.com/content/images/pulkki1.jpg",
            "http://greenleafaquariums.com/content/images/pulkki2.jpg",
            "http://greenleafaquariums.com/content/images/pulkki3.jpg",
            "http://greenleafaquariums.com/content/images/rountree2.jpg",
            "http://greenleafaquariums.com/content/images/tamara1.jpg",
            "http://greenleafaquariums.com/content/images/tamara2.jpg",
            "http://greenleafaquariums.com/content/images/helgeson1.jpg",
            "http://greenleafaquariums.com/content/images/devin1.jpg",
            "http://greenleafaquariums.com/content/images/devin2.jpg",
            "http://greenleafaquariums.com/content/images/deki1.jpg",
            "http://greenleafaquariums.com/content/images/deki2.jpg",
            "http://greenleafaquariums.com/content/images/johnson1.jpg",
            "http://greenleafaquariums.com/content/images/briegel1.jpg",
            "http://greenleafaquariums.com/content/images/rastovac1.jpg",
    };

//    int[] sampleImages = {R.drawable.image_1, R.drawable.image_2, R.drawable.image_3, R.drawable.image_4, R.drawable.image_5, R.drawable.image_6, R.drawable.image_7,
//            R.drawable.image_8, R.drawable.image_9, R.drawable.image_10, R.drawable.image_11};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__core_team);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(getDataSet(), getApplicationContext());

        mRecyclerView.setAdapter(mAdapter);
    }

    private ArrayList<DataObject> getDataSet() {
        ArrayList results = new ArrayList<DataObject>();
        for (int index = 0; index < imageUrl.length; index++) {
            DataObject obj = new DataObject("Some Primary Text " + index,
                    "Secondary " + index, imageUrl[index]);
            results.add(index, obj);
        }
        return results;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
            }
        });
    }
}
