package ahh.com.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    CarouselView carouselView;
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


    String summary = "<html><p>\n" +
            "We're excited to announce the full speaker list and agenda for the first ever\n" +
            "Firebase Dev Summit!\n" +
            "</p>" +
            "<p>\n" +
            "Find the latest schedule on our newly launched site <a href=\"https://events.withgoogle.com/firebase-dev-summit/agenda/\">here</a>\n" +
            "featuring sessions such as <em>How to Develop Rock Solid Apps with\n" +
            "Firebase</em>, <em>a Firebase Analytics deep dive</em>, <em>Develop Mobile Apps\n" +
            "without Infrastructure </em>, and more!\n" +
            "</p>" +
            "<p>We built this program with expert analysts and leading technology companies like Facebook and MongoDB to ensure you master the skills needed to meet the requirements of industry.</p>" +
            "<h5>\n" +
            "            <span>\n" +
            "              <img alt=\"Career guidance\" src=\"https://d125fmws0bore1.cloudfront.net/assets/pages/ndop_a/icon_careerguidance-2dbaeadd021e7c4b8b2af1b2347346b83644fcb9eb7c21e1e49f12ebb75d0020.svg\">\n" +
            "            </span>\n" +
            "            Personalized Career Guidance\n" +
            "          </h5>" +
            "<p>Take advantage of free resume and online profile reviews. You’ll also receive access to our network of hiring partners, and prepare for hiring questions with mock interviews.</p>" +
            "<ul \n" +
            "                    <li>Use NumPy arrays, pandas series, and vectorized operations to ease the data analysis process</li>\n" +
            "                    <li>Use two-dimensional NumPy arrays and pandas DataFrames</li>\n" +
            "                    <li>Understand how to group data and to combine data from multiple files</li>\n" +
            "                  </ul></html>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView desctiption = (TextView) findViewById(R.id.description);
        desctiption.setText(Html.fromHtml(summary));

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(imageUrl.length);


        carouselView.setImageListener(imageListener);
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Picasso.with(getApplicationContext()).load(imageUrl[position]).into(imageView);


        }
    };


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.sp_member_data), getApplicationContext().MODE_PRIVATE);

        String photoUrl = sharedPref.getString(getString(R.string.sp_member_imgurl), null);
        String displayName = sharedPref.getString(getString(R.string.sp_member_name), null);
        String emailId = sharedPref.getString(getString(R.string.sp_member_email), null);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);

        Log.d("onResume ", sharedPref.toString() + " , \n" + photoUrl + " , \n" + displayName + " , \n" + emailId);
        TextView nav_user = (TextView) hView.findViewById(R.id.memberName);
        nav_user.setText(displayName);

        TextView nav_eid = (TextView) hView.findViewById(R.id.memberEid);
        nav_eid.setText(emailId);
        ImageView nav_IV = (ImageView) hView.findViewById(R.id.memberImage);
        Glide.with(this)
                .load(photoUrl)
                .into(nav_IV);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_core) {
            // Handle the camera action
            Intent coreTeamIntent = new Intent(getApplicationContext(), Activity_CoreTeam.class);
            startActivity(coreTeamIntent);
        } else if (id == R.id.nav_gallery) {

            startActivity(new Intent(getApplicationContext(), Activity_SubGallery.class));
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.login) {
            startActivity(new Intent(getApplicationContext(), UserAuth.class));

        } else if (id == R.id.admin) {
            startActivity(new Intent(getApplicationContext(), Admin.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
