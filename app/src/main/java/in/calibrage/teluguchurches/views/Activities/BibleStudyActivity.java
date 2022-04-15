package in.calibrage.teluguchurches.views.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;

import java.io.IOException;
import java.util.ArrayList;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.services.APIConstants;
import in.calibrage.teluguchurches.services.ChurchService;
import in.calibrage.teluguchurches.services.ServiceFactory;
import in.calibrage.teluguchurches.util.BaseActivity;
import in.calibrage.teluguchurches.views.adapter.ChapterListAdapter;
import in.calibrage.teluguchurches.views.model.BibleReadModel;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BibleStudyActivity extends BaseActivity implements ChapterListAdapter.OnCartChangedListener {
    private ImageView toolbar_image;
    RecyclerView recyclerViewBibleStudy;
    ChapterListAdapter chapterListAdapter;
    private Context mContext;
    private Toolbar toolbar;
    private Integer listPosition = 0;
    private Subscription mSubscription;
    BibleReadModel mModel = new BibleReadModel();
    private String TAG = BibleStudyActivity.class.getSimpleName();
    ArrayList<String> bibleBookList = new ArrayList<>();
    ArrayList<String> bibleChapterList = new ArrayList<>();
    ArrayList<String> bibleChapterCount = new ArrayList<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getApplicationContext();
        // added crash report's to mail
        Fabric.with(getApplicationContext(), new Crashlytics());
        //assining layout
        setContentView(R.layout.bible_study);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Bible");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar_image = findViewById(R.id.toolbar_image);
        /**
         * @param OnClickListner
         */
        toolbar_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BibleStudyActivity.this, HomeActivity.class));
            }
        });
        recyclerViewBibleStudy = findViewById(R.id.recyclerViewBibleStudy);

        // to check the phone is connected to Internet or Not
        if (isOnline()) {

            // API to Bible Chapter list
            getBibleChapter();

            // to start the Progress Dialog
            showProgressDialog();
        } else {
            // when no Internet connection
            showToastLong(getString(R.string.no_internet));
        }

    }
        // API to Bible Chapter telugu
    private void getBibleChapter() {
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        String url = APIConstants.GetBibleChapter;
        mSubscription = service.GetBibleChapter(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BibleReadModel>() {
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
                    public void onNext(BibleReadModel mResponse) {

                        // to hide the Progress Dialog
                        hideProgressDialog();
                        mModel = mResponse;
                        for (int i = 0; i < mResponse.getListResult().size(); i++) {
                            bibleBookList.add(mResponse.getListResult().get(i).getName());
                            bibleChapterList.add(mResponse.getListResult().get(i).getUrl());
                            bibleChapterCount.add(mResponse.getListResult().get(i).getChapterCount());
                        }
                        chapterListAdapter = new ChapterListAdapter(BibleStudyActivity.this, bibleBookList, bibleChapterCount);
                        chapterListAdapter.setOnCartChangedListener(BibleStudyActivity.this);
                        recyclerViewBibleStudy.setAdapter(chapterListAdapter);
                        recyclerViewBibleStudy.setLayoutManager(new LinearLayoutManager(BibleStudyActivity.this, LinearLayoutManager.VERTICAL, false));
                        chapterListAdapter.setOnCartChangedListener(BibleStudyActivity.this);

                    }
                });
    }

    // Interface between adapter and class
    /*
     * intent value passing to BibleChapterActivity
     * @param  url
     *@param  language
     *@param  title
    */

    @Override
    public void setCartClickListener(int position, String status) {
        listPosition = position;
        if (status.equalsIgnoreCase("click")) {
            startActivity(new Intent(getApplicationContext(), BibleChapterActivity.class)
                    .putExtra("url", bibleChapterList.get(position))
                    .putExtra("language", "Telugu")
                    .putExtra("title", mModel.getListResult().get(position).getName()));
        }


    }


}
