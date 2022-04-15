package in.calibrage.teluguchurches.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.util.BaseFragment;



public class ChurchPostsFragment extends BaseFragment {

    private View rootView;
    private Context mContext;
    private int churchid;
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private ChurchPostsFragment.ViewPagerAdapter adapter;
    private TabLayout tabhost;
    private String position;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();

        //assining layout
        rootView = inflater.inflate(R.layout.admin_post, null, false);

        position = getArguments().getString("notiChurchPostId");


        if (position != null && !position.equalsIgnoreCase("0")) {
            tabhost = (TabLayout) getActivity().findViewById(R.id.tabs);
            tabhost.getTabAt(2).select();
        }

        /* intializing and assigning ID's */
        initView();


        return rootView;


    }

    private void initView() {
        /* intializing and assigning ID's */
        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        /*
         * getting data using bundle
         * */
        churchid = bundle.getInt("id");
        mViewPager = rootView.findViewById(R.id.viewpager);
        setupViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(4);
        tabLayout = rootView.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(mViewPager);


    }

    // Adding view pager to show different views in one screen

    private void setupViewPager(ViewPager viewPager) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", churchid);
        ChurchAudioFragment churchAudioFragment = new ChurchAudioFragment();
        churchAudioFragment.setArguments(bundle);

        ChurchVideoFragment churchVideoFragment = new ChurchVideoFragment();
        churchVideoFragment.setArguments(bundle);

        ChurchImageFragment churchImageFragment = new ChurchImageFragment();
        churchImageFragment.setArguments(bundle);

        ChurchDocumentFragment churchDocumentFragment = new ChurchDocumentFragment();
        churchDocumentFragment.setArguments(bundle);

        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(churchAudioFragment, getString(R.string.audio));
        adapter.addFragment(churchVideoFragment, getString(R.string.video));
        adapter.addFragment(churchImageFragment, getString(R.string.images));
        adapter.addFragment(churchDocumentFragment, getString(R.string.document));
        viewPager.setAdapter(adapter);
    }


    //adapter for different pages
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

