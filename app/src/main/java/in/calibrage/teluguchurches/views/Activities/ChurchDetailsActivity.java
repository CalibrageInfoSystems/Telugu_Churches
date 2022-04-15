package in.calibrage.teluguchurches.views.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.util.BaseActivity;
import in.calibrage.teluguchurches.util.Constants;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
import in.calibrage.teluguchurches.views.fragments.ChurchEventsFragment;
import in.calibrage.teluguchurches.views.fragments.ChurchPostsFragment;
import in.calibrage.teluguchurches.views.fragments.InfoFragment;
import io.fabric.sdk.android.Fabric;

public class ChurchDetailsActivity extends BaseActivity {
    private ImageView headerImg;
    private ViewPager mViewPager;
    private ViewPagerAdapter adapter;
    private Intent intent;
    private int intValue,value, fragmentId, notiChurchPostId;
    private String name, regNo, address1, address2, landmark, mobileNo, email, pastor, image, notiChurchName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // added crash report's to mail
        Fabric.with(ChurchDetailsActivity.this, new Crashlytics());

        //assining layout
        setContentView(R.layout.activity_churchu_details);
        value = SharedPrefsData.getInt(getApplicationContext(), Constants.ISCHURCH, Constants.PREF_NAME);
        intent = getIntent();
        if (intent != null) {

            /*
             * to check the intent value
             * @param  fragmentId
             *@param  notiChurchPostId
             *@param  intValue
             *@param  regNo
             *@param  name
             *@param  address1
             *@param  address2
             *@param  landmark
             *@param  email
             *@param  image
          */
            fragmentId = getIntent().getIntExtra("notiChurchEventId", 0);
            notiChurchPostId = getIntent().getIntExtra("notiChurchPostId", 0);
            intValue = intent.getIntExtra("id", 0);
            regNo = intent.getStringExtra("regNo");
            name = intent.getStringExtra("name");
            address1 = intent.getStringExtra("address1");
            address2 = intent.getStringExtra("address2");
            landmark = intent.getStringExtra("landmark");
            mobileNo = intent.getStringExtra("mobileNo");
            email = intent.getStringExtra("email");
            pastor = intent.getStringExtra("pastor");
            image = intent.getStringExtra("image");
            //   Constants.Churchid = intValue;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        headerImg = findViewById(R.id.headerImg);
        headerImg.setColorFilter(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.primaryTransColor), android.graphics.PorterDuff.Mode.MULTIPLY);


        Glide.with(getApplicationContext()).load(image)
                .fitCenter()
                .error(R.drawable.church16)
                .into(headerImg);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mViewPager = findViewById(R.id.viewpager);

        // load the fragments when the tab is selected or page is swiped
        setupViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(3);


        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * * Layout manager that allows the user to flip left and right
     * through pages of data.  You supply an implementation of a
     * {PagerAdapter} to generate the pages that the view shows.
     *
     * @param viewPager
     */

    /*
     * to check the bundle value
     * @param id
     * @param notiChurchEventId
     * @param notiChurchPostId
     */
    private void setupViewPager(ViewPager viewPager) {
        Bundle bundle = new Bundle();
        bundle.putString("id", "" + intValue);
        bundle.putString("notiChurchEventId", "" + fragmentId);
        bundle.putString("notiChurchPostId", "" + notiChurchPostId);
        ChurchEventsFragment tabFragment = new ChurchEventsFragment();
        tabFragment.setArguments(bundle);
        ChurchPostsFragment churchPostsFragment = new ChurchPostsFragment();
        churchPostsFragment.setArguments(bundle);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new InfoFragment(), getString(R.string.information));
        adapter.addFragment(tabFragment, getString(R.string.event_title));
        adapter.addFragment(churchPostsFragment, getString(R.string.posts));
        viewPager.setAdapter(adapter);

    }

    /**
     * * Implementation of {PagerAdapter} that
     * represents each page as a {@link Fragment} that is persistently
     * kept in the fragment manager as long as the user can return to the page.
     */

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return mFragmentTitleList.get(position);
        }


    }

}