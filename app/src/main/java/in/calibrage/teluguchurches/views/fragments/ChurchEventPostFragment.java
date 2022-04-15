package in.calibrage.teluguchurches.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;

import java.io.IOException;
import java.util.ArrayList;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.services.APIConstants;
import in.calibrage.teluguchurches.services.ChurchService;
import in.calibrage.teluguchurches.services.ServiceFactory;
import in.calibrage.teluguchurches.util.BaseFragment;
import in.calibrage.teluguchurches.util.Constants;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
import in.calibrage.teluguchurches.views.Activities.AudioActivity;
import in.calibrage.teluguchurches.views.Activities.PdfReaderActivity;
import in.calibrage.teluguchurches.views.Activities.YoutubePlayerActivity;
import in.calibrage.teluguchurches.views.adapter.AudioYoutubePlayerAdapter;
import in.calibrage.teluguchurches.views.adapter.CategoryImageAdapter;
import in.calibrage.teluguchurches.views.adapter.DocumentAdapter;
import in.calibrage.teluguchurches.views.adapter.YoutubePlayerAdapter;
import in.calibrage.teluguchurches.views.model.GetPostByCategoryIdResponseModel;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;



public class ChurchEventPostFragment extends BaseFragment implements DocumentAdapter.OnCartChangedListener, CategoryImageAdapter.OnCartChangedListener {
    private Context mContext;
    private View rootView;
    private int eventId,postId,spValue;
    private RecyclerView docRecyclerView, videoRecyclerView, audioRecyclerView, imgRecyclerView;
    private Subscription mSubscription;
    private LinearLayout docCardView, videoLay, imageCardView, audioCardView;
    private ArrayList<String> documentList = new ArrayList<>();
    private ArrayList<String> videoList = new ArrayList<>();
    private ArrayList<String> audioList = new ArrayList<>();
    private ArrayList<String> imageList = new ArrayList<>();
    private ArrayList<String> audioArrayList = new ArrayList<>();
    private AudioYoutubePlayerAdapter adapterAudio;
    private ArrayList<String> linkAudioArray = new ArrayList<>();
    private CategoryImageAdapter cAdapter;
    private YoutubePlayerAdapter adapter;
    private ArrayList<String> linkArray = new ArrayList<>();
    GetPostByCategoryIdResponseModel listResponse;
    private ImageView headerImg;
    private Intent intent;
    private String categoryName, image;
    public String link, url;
    TextView noRecords;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();

        // added crash report's to mail
        Fabric.with(getActivity(), new Crashlytics());

        //assining layout
        rootView = inflater.inflate(R.layout.activity_categories_dtail, null, false);

        intent = getActivity().getIntent();
        if (intent != null) {
            categoryName = intent.getStringExtra("name");
            image = intent.getStringExtra("image");
        }

        //storing value's from shared preference
        spValue = SharedPrefsData.getInt(mContext, Constants.TOYOUTUBE, Constants.PREF_NAME);

        /* intializing and assigning ID's */
        initViews();

        return rootView;
    }

    /* intializing and assigning ID's */
    private void initViews() {
        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        /*
         * getting data using bundle
         * */
        eventId = bundle.getInt("eventId");
        headerImg = (ImageView) rootView.findViewById(R.id.headerImg);

        Glide.with(mContext).load(image)
                .fitCenter()
                .error(R.drawable.eventsdetails)
                .into(headerImg);


        docRecyclerView = (RecyclerView) rootView.findViewById(R.id.docRecyclerView);
        videoRecyclerView = (RecyclerView) rootView.findViewById(R.id.videoRecyclerView);
        audioRecyclerView = (RecyclerView) rootView.findViewById(R.id.audioRecyclerView);
        imgRecyclerView = (RecyclerView) rootView.findViewById(R.id.imgRecyclerView);
        docCardView = (LinearLayout) rootView.findViewById(R.id.docCardView);
        videoLay = (LinearLayout) rootView.findViewById(R.id.videoLay);
        audioCardView = (LinearLayout) rootView.findViewById(R.id.audioCardView);
        imageCardView = (LinearLayout) rootView.findViewById(R.id.imageCardView);
        noRecords = (TextView) rootView.findViewById(R.id.no_records);
        noRecords.setVisibility(View.GONE);

        // to start the Progress Dialog
        activity.showProgressDialog();

        /*
         * API to get post by churchID
         * */
        getPostCategoryById();

    }

    /*
     * API to get post by churchID
     * */
    private void getPostCategoryById() {
        /*
         * API to get post by churchID
         * */
        ChurchService service = ServiceFactory.createRetrofitService(mContext, ChurchService.class);
        String url = APIConstants.GetPostbyEventId + eventId;
        mSubscription = service.GetPostbyCategoryId(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetPostByCategoryIdResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(GetPostByCategoryIdResponseModel mResponse) {

                        // to hide the Progress Dialog
                        activity.hideProgressDialog();

                        if (mResponse.getIsSuccess()) {
                            if (mResponse.getResult()==null){
                                noRecords.setVisibility(View.VISIBLE);
                            }
                            listResponse = mResponse;
                            for (int i = 0; i < mResponse.getResult().getAudios().size(); i++) {
                                if (mResponse.getResult().getAudios().get(i).getMediaTypeId().equals(3)) {
                                    audioCardView.setVisibility(View.VISIBLE);
                                    audioList.add(mResponse.getResult().getAudios().get(i).getPostImage());
                                    audioArrayList.add(mResponse.getResult().getAudios().get(i).getEmbededUrl());
                                }
                            }
                            for (int i = 0; i < mResponse.getResult().getVideos().size(); i++) {
                                if (mResponse.getResult().getVideos().get(i).getMediaTypeId().equals(4) && mResponse.getResult().getVideos().get(i).getEmbededUrl() != null && !mResponse.getResult().getVideos().get(i).getEmbededUrl().equalsIgnoreCase("")) {
                                    videoLay.setVisibility(View.VISIBLE);
                                    videoList.add(mResponse.getResult().getVideos().get(i).getEmbededUrl());
                                }
                            }

                            for (int i = 0; i < mResponse.getResult().getImages().size(); i++) {
                                if (mResponse.getResult().getImages().get(i).getMediaTypeId().equals(5)) {
                                    imageCardView.setVisibility(View.VISIBLE);
                                    imageList.add(mResponse.getResult().getImages().get(i).getPostImage());
                                }
                            }
                            for (int i = 0; i < mResponse.getResult().getDocuments().size(); i++) {
                                if (mResponse.getResult().getDocuments().get(i).getMediaTypeId().equals(6)) {
                                    docCardView.setVisibility(View.VISIBLE);
                                    documentList.add(mResponse.getResult().getDocuments().get(i).getPostImage());
                                }
                            }

                            /**
                             * Split String
                             */
                            for (int i = 0; i < videoList.size(); i++) {
                                String input = videoList.get(i);
                                int index = input.lastIndexOf("/");
                                if (index != -1) {
                                    String videoCode = videoList.get(i).substring(index).replace("/", "");
                                    linkArray.add(videoCode);
                                }
                            }

                            for (int i = 0; i < audioArrayList.size(); i++) {
                                String input = audioArrayList.get(i);
                                int index = input.lastIndexOf("/");
                                if (index != -1) {
                                    String videoCode = audioArrayList.get(i).substring(index).replace("/", "");
                                    linkAudioArray.add(videoCode);
                                }
                            }

                            if (audioList.size() > 0) {
                                audioRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                                adapterAudio = new AudioYoutubePlayerAdapter(mContext, mResponse, linkAudioArray);
                                audioRecyclerView.setAdapter(adapterAudio);
                            }
                            if (videoList.size() > 0) {
                                videoRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                                adapter = new YoutubePlayerAdapter(mContext, mResponse, linkArray);
                                videoRecyclerView.setAdapter(adapter);
                            }
                            if (imageList.size() > 0) {
                                imgRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                                cAdapter = new CategoryImageAdapter(mContext, mResponse, "image");
                                imgRecyclerView.setAdapter(cAdapter);
                            }
                            if (documentList.size() > 0) {
                                docRecyclerView.setHasFixedSize(true);
                                docRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                                DocumentAdapter adapter = new DocumentAdapter(mContext, mResponse);
                                docRecyclerView.setAdapter(adapter);
                                adapter.setOnCartChangedListener(ChurchEventPostFragment.this);

                            }


                        }
                    }
                });
    }


    @Override
    public void setCartClickListener(String status, int position) {
        if (status.equalsIgnoreCase("position")) {
            startActivity(new Intent(mContext, PdfReaderActivity.class).putExtra("document", documentList.get(position)));
        } else if (status.equalsIgnoreCase("audio")) {
            /*
             * sending data using intent
             * */

            /*
             * intent value passing to YoutubePlayerActivity
             * @param  postId
             * @param  link
             * @param  url
             */             Intent intent = new Intent(mContext, YoutubePlayerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            postId = listResponse.getResult().getAudios().get(position).getId();
            link = listResponse.getResult().getAudios().get(position).getEmbedId();
            url = listResponse.getResult().getAudios().get(position).getEmbededUrl();

            /*
             * storing values in shared preferences
             * */
            /*
             * passing values in shared preferences to YoutubePlayerActivity
             * @param  link
             * @param  url
             * @param  postId
            */
            SharedPrefsData.putString(mContext, Constants.LinkEnd, link, Constants.PREF_NAME);
            SharedPrefsData.putString(mContext, Constants.URL, url, Constants.PREF_NAME);
            SharedPrefsData.putInt(mContext, Constants.PostId, postId, Constants.PREF_NAME);
            mContext.startActivity(intent);
        }
    }


}
