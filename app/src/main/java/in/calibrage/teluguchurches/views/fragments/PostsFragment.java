package in.calibrage.teluguchurches.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.util.BaseFragment;
import io.fabric.sdk.android.Fabric;



public class PostsFragment extends BaseFragment {
    private Context mContext;
    private View rootView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Adapter adapter;
    private int adminId;
    private TabLayout tabhost;
    private String position;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();

        // added crash report's to mail
        Fabric.with(getActivity(), new Crashlytics());

        //assining layout
        rootView = inflater.inflate(R.layout.admin_post, null, false);

        position = getArguments().getString("notiAuthorPostId");
        if (position != null && !position.equalsIgnoreCase("0")) {
            tabhost = (TabLayout) getActivity().findViewById(R.id.tabs);
            tabhost.getTabAt(2).select();
        }

        /* intializing and assigning ID's */
        initViews();


        return rootView;
    }
    /* intializing and assigning ID's */
    private void initViews() {

        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        adminId = bundle.getInt("id");
        /*
         * getting data using bundle
         * */
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);


    }


    // Adding view pager to show different views in one screen
    private void setupViewPager(ViewPager viewPager) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", adminId);
        AuthorPostAudioFragment audioFragment = new AuthorPostAudioFragment();
        audioFragment.setArguments(bundle);

        AuthorPostImageFragment imageFragment = new AuthorPostImageFragment();
        imageFragment.setArguments(bundle);

        AuthorPostVideoFragment videoFragment = new AuthorPostVideoFragment();
        videoFragment.setArguments(bundle);

        AuthorPostDocumentsFragment documentsFragment = new AuthorPostDocumentsFragment();
        documentsFragment.setArguments(bundle);

        adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(audioFragment, getString(R.string.audio));
        adapter.addFragment(videoFragment, getString(R.string.video));
        adapter.addFragment(imageFragment, getString(R.string.images));
        adapter.addFragment(documentsFragment, getString(R.string.document));
        viewPager.setAdapter(adapter);
    }

    //adapter for different pages
    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }


}
