package in.calibrage.teluguchurches.views.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.util.BaseActivity;
import in.calibrage.teluguchurches.views.fragments.ChurchEventPostFragment;
import in.calibrage.teluguchurches.views.fragments.EventDetailsFragment;
import io.fabric.sdk.android.Fabric;

public class EventDetailTabActivity extends BaseActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private Toolbar toolbar;
    ImageView toolbar_image;
    private Intent intent;
    private int eventId;
    private String eventName, categoryName, image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // added crash report's to mail
        Fabric.with(this, new Crashlytics());

        //assining layout
        setContentView(R.layout.activity_events_tab);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent = getIntent();
        if (intent != null) {
            /*
             * to check the intent value
             * @param  eventId
             *@param  eventName
             *@param  name
             *@param  image
          */
            eventId = intent.getIntExtra("eventId", 0);
            eventName = intent.getStringExtra("eventName");
            categoryName = intent.getStringExtra("name");
            image = intent.getStringExtra("image");
        }
        toolbar.setTitle(eventName);
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        toolbar_image = findViewById(R.id.toolbar_image);
        /**
         * @param OnClickListner
         */
        toolbar_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EventDetailTabActivity.this, HomeActivity.class));
            }
        });

    }

    /**
     * * Layout manager that allows the user to flip left and right
     * through pages of data.  You supply an implementation of a
     * {PagerAdapter} to generate the pages that the view shows.
     *
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        Bundle bundle = new Bundle();
        bundle.putInt("eventId", eventId);
        ChurchEventPostFragment postsFragment = new ChurchEventPostFragment();
        postsFragment.setArguments(bundle);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new EventDetailsFragment(), getString(R.string.events_details));
        adapter.addFragment(postsFragment, getString(R.string.posts));
        viewPager.setAdapter(adapter);
    }

    /**
     * * Implementation of {PagerAdapter} that
     * represents each page as a {@link Fragment} that is persistently
     * kept in the fragment manager as long as the user can return to the page.
     */
    static class ViewPagerAdapter extends FragmentPagerAdapter {
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
