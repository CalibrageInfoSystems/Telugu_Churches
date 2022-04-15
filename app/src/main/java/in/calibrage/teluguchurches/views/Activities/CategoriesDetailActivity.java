package in.calibrage.teluguchurches.views.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;

import java.io.IOException;
import java.util.ArrayList;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.services.APIConstants;
import in.calibrage.teluguchurches.services.ChurchService;
import in.calibrage.teluguchurches.services.ServiceFactory;
import in.calibrage.teluguchurches.util.BaseActivity;
import in.calibrage.teluguchurches.util.CommonUtil;
import in.calibrage.teluguchurches.util.Constants;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
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

public class CategoriesDetailActivity extends BaseActivity implements DocumentAdapter.OnCartChangedListener, CommonUtil.OnCartChangedListener, CategoryImageAdapter.OnCartChangedListener {
    TextView noRecords;
    private ImageView headerImg;
    private RecyclerView docRecyclerView, videoRecyclerView, audioRecyclerView, imgRecyclerView;
    private CategoryImageAdapter cAdapter;
    private YoutubePlayerAdapter adapter;
    private AudioYoutubePlayerAdapter adapterAudio;
    private Context mContext;
    private Intent intent;
    public int postId,spValue;
    private Subscription mSubscription;
    GetPostByCategoryIdResponseModel listResponse;
    private LinearLayout docCardView, videoLay, imageCardView, audioCardView;
    private ArrayList<String> documentList = new ArrayList<>();
    private ArrayList<String> videoList = new ArrayList<>();
    private ArrayList<String> audioList = new ArrayList<>();
    private ArrayList<String> imageList = new ArrayList<>();
    private ArrayList<String> audioArrayList = new ArrayList<>();
    private ArrayList<String> linkArray = new ArrayList<>();
    private ArrayList<String> linkAudioArray = new ArrayList<>();
    private String link, url,categoryName, image;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        // added crash report's to mail
        Fabric.with(getApplicationContext(), new Crashlytics());

        //assining layout
        setContentView(R.layout.activity_categories_dtail);

        intent = getIntent();
        if (intent != null) {
            /*
             * to check the intent value
             * @param  categoryName
             *@param  image
             */
            categoryName = intent.getStringExtra("name");
            image = intent.getStringExtra("image");
        }
        spValue = SharedPrefsData.getInt(CategoriesDetailActivity.this, Constants.TOYOUTUBE, Constants.PREF_NAME);

        /* intializing and assigning ID's */
        initViews();
        setViews();
    }

    /* intializing and assigning ID's */
    private void initViews() {

        Toolbar toolbar =findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle(categoryName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spValue == 1) {
                    Intent intent;
                    intent = new Intent(getApplicationContext(), CategoriesActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {

                    finish();
                }

            }
        });

        headerImg = findViewById(R.id.headerImg);
        headerImg.setColorFilter(ContextCompat.getColor(CategoriesDetailActivity.this, R.color.primaryTransColor), android.graphics.PorterDuff.Mode.MULTIPLY);
        Glide.with(getApplicationContext()).load(image)
                .fitCenter()
                .error(R.drawable.church11)
                .into(headerImg);


        docRecyclerView = findViewById(R.id.docRecyclerView);
        videoRecyclerView = findViewById(R.id.videoRecyclerView);
        audioRecyclerView =findViewById(R.id.audioRecyclerView);
        imgRecyclerView = findViewById(R.id.imgRecyclerView);
        docCardView = findViewById(R.id.docCardView);
        videoLay = findViewById(R.id.videoLay);
        audioCardView =findViewById(R.id.audioCardView);
        imageCardView = findViewById(R.id.imageCardView);
        noRecords =findViewById(R.id.no_records);
        noRecords.setVisibility(View.GONE);

        // to start the Progress Dialog
        showProgressDialog();

        // API to get Particular Category posts
        getPostCategoryById();

    }

    private void setViews() {

    }

    // API to get Particular Category posts
    private void getPostCategoryById() {
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        String url = APIConstants.GetPostbyCategoryId + SharedPrefsData.getInt(getApplicationContext(), "" + Constants.CategoryId, Constants.PREF_NAME)
                + "/" + SharedPrefsData.getInt(getApplicationContext(), Constants.ID, Constants.PREF_NAME);

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
                        hideProgressDialog();
                        if (mResponse.getIsSuccess()) {

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
                                audioRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                                adapterAudio = new AudioYoutubePlayerAdapter(getApplicationContext(), mResponse, linkAudioArray);
                                audioRecyclerView.setAdapter(adapterAudio);

                            }
                            if (videoList.size() > 0) {
                                videoRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                                adapter = new YoutubePlayerAdapter(getApplicationContext(), mResponse, linkArray);
                                videoRecyclerView.setAdapter(adapter);
                            }
                            if (imageList.size() > 0) {
                                imgRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                                cAdapter = new CategoryImageAdapter(getApplicationContext(), mResponse, "image");
                                imgRecyclerView.setAdapter(cAdapter);
                            }
                            if (documentList.size() > 0) {
                                docRecyclerView.setHasFixedSize(true);
                                docRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                                DocumentAdapter adapter = new DocumentAdapter(getApplicationContext(), mResponse);
                                docRecyclerView.setAdapter(adapter);
                                adapter.setOnCartChangedListener(CategoriesDetailActivity.this);
                                CommonUtil.setOnCartChangedListener(CategoriesDetailActivity.this);

                            }


                        }else {
                           noRecords.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    // Interface between adapter and class
    /*
     * passing value to PdfReader
     * @param  document
     */

    /*
     * passing value to YoutubePlayer
     * @param  link
     * @param  url
     * @param  postId
     */
    @Override
    public void setCartClickListener(String status, int position) {
        if (status.equalsIgnoreCase("position")) {
//            CommonUtil.showAlertDialog(CategoriesDetailActivity.this, "Download " + listResponse.getListResult().get(position).getTitle(), "Do you want to download the catalog pdf? We recommend downloading on WiFi connection", false, R.drawable.ic_action_tick);
            startActivity(new Intent(getApplicationContext(), PdfReaderActivity.class).putExtra("document", documentList.get(position)));
        } else if (status.equalsIgnoreCase("audio")) {
           /* startActivity(new Intent(getApplicationContext(), YoutubePlayerActivity.class)
                    .putExtra("audio", audioList.get(position))
                    .putExtra("postTypeId", listResponse.getResult().getAudios().get(position).getId()));*/
            Intent intent = new Intent(mContext, YoutubePlayerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         /*   intent.putExtra("audio", BindDataListResults.get(position).getPostImage())
                    .putExtra("postTypeId", BindDataListResults.get(position).getId());*/

            postId = listResponse.getResult().getAudios().get(position).getId();
            link = listResponse.getResult().getAudios().get(position).getEmbedId();
            url = listResponse.getResult().getAudios().get(position).getEmbededUrl();

            SharedPrefsData.putString(mContext, Constants.LinkEnd, link, Constants.PREF_NAME);
            SharedPrefsData.putString(mContext, Constants.URL, url, Constants.PREF_NAME);
            SharedPrefsData.putInt(mContext, Constants.PostId, postId, Constants.PREF_NAME);
            mContext.startActivity(intent);
        }
    }

    @Override
    public void setCartClickListener(String status) {
        if (status.equalsIgnoreCase("ok")) {
//            CommonUtil.download(CategoriesDetailActivity.this, listResponse.getListResult().get(0).getPostImage(), listResponse.getListResult().get(0).getDesc() + ".pdf", false);
        }
    }

    @Override
    public void onBackPressed() {
        if (spValue == 1) {
            Intent intent;
            intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            finish();
        }
    }
}
