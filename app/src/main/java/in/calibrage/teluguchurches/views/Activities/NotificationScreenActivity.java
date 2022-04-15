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
import in.calibrage.teluguchurches.views.fragments.ReadNotificationFragment;
import in.calibrage.teluguchurches.views.fragments.ViewNotificationFragment;
import io.fabric.sdk.android.Fabric;


public class NotificationScreenActivity extends BaseActivity {
    ImageView toolbar_image;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private Toolbar toolbar;
    private Intent intent;
    private int eventId;
    private String eventName, categoryName, image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // added crash report's to mail
        Fabric.with(NotificationScreenActivity.this, new Crashlytics());

        //assining layout
        setContentView(R.layout.activity_notification_tab);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setTitle("Notification");
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
                startActivity(new Intent(NotificationScreenActivity.this, HomeActivity.class));
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

    /*
     * to check the bundle value
     * @param eventId
     */
    private void setupViewPager(ViewPager viewPager) {
        Bundle bundle = new Bundle();
        bundle.putInt("eventId", eventId);
        ReadNotificationFragment readNotificationFragment = new ReadNotificationFragment();
        readNotificationFragment.setArguments(bundle);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ViewNotificationFragment(), getString(R.string.unread_notification));
        adapter.addFragment(new ReadNotificationFragment(), getString(R.string.read_notification));
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
