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
import in.calibrage.teluguchurches.views.fragments.AuthorsEventsFragment;
import in.calibrage.teluguchurches.views.fragments.DetailsFragment;
import in.calibrage.teluguchurches.views.fragments.PostsFragment;
import io.fabric.sdk.android.Fabric;


public class AdminDetailsActivity extends BaseActivity {
    ImageView imageView;
    private ViewPager mViewPager;
    private ViewPagerAdapter adapter;
    Intent intent;
    private int adminId, notiAuthorEventId, notiAuthorPostId,value;
    private String name, mobileNo, email, churchName, churchImage, userImage,authorName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // added crash report's to mail
        Fabric.with(AdminDetailsActivity.this, new Crashlytics());

        //assining layout
        setContentView(R.layout.activity_admindetails);


        intent = getIntent();
        if (intent != null) {

            /*
             * to check the intent value
             * @param  notiAuthorEventId
             *@param  notiAuthorPostId
             *@param  adminId
             *@param  name
             *@param  mobileNo
             *@param  email
             *@param  churchName
             *@param  churchImage
             *@param  userImage
             *@param  authorName
            */

            notiAuthorEventId = getIntent().getIntExtra("notiAuthorEventId", 0);
            notiAuthorPostId = getIntent().getIntExtra("notiAuthorPostId", 0);
            adminId = intent.getIntExtra("id", 0);
            name = intent.getStringExtra("name");
            mobileNo = intent.getStringExtra("mobile");
            email = intent.getStringExtra("email");
            churchName = intent.getStringExtra("church name");
            churchImage = intent.getStringExtra("church image");
            userImage = intent.getStringExtra("image");
            authorName=intent.getStringExtra("author name");


        }
        value = SharedPrefsData.getInt(getApplicationContext(), Constants.ISCHURCH, Constants.PREF_NAME);
        Toolbar toolbar =findViewById(R.id.toolbar);
        imageView =findViewById(R.id.headerImg);
        imageView.setColorFilter(ContextCompat.getColor(AdminDetailsActivity.this, R.color.primaryTransColor), android.graphics.PorterDuff.Mode.MULTIPLY);

        Glide.with(this).load(userImage).fitCenter().error(R.drawable.authordetails).into(imageView);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(authorName);
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
       * @param adminId
       * @param notiAuthorPostId
     */
    private void setupViewPager(ViewPager viewPager) {
        Bundle bundle = new Bundle();
        bundle.putString("id", ""+adminId);
        bundle.putString("notiAuthorPostId", "" + notiAuthorPostId);
        PostsFragment postsFragment = new PostsFragment();
        postsFragment.setArguments(bundle);

        bundle.putString("notiAuthorEventId", "" + notiAuthorEventId);
        AuthorsEventsFragment authorsEventsFragment = new AuthorsEventsFragment();
        authorsEventsFragment.setArguments(bundle);


        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DetailsFragment(), getString(R.string.information));
        adapter.addFragment(authorsEventsFragment, getString(R.string.event_title));
        adapter.addFragment(postsFragment, getString(R.string.posts));
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
