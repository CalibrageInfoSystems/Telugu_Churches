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
import in.calibrage.teluguchurches.services.ChurchService;
import in.calibrage.teluguchurches.services.ServiceFactory;
import in.calibrage.teluguchurches.util.BaseActivity;
import in.calibrage.teluguchurches.views.adapter.BibleChapterAdapter;
import in.calibrage.teluguchurches.views.model.ChapterReadModel;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;



public class BibleChapterActivity extends BaseActivity implements BibleChapterAdapter.OnCartChangedListener {

    private ImageView toolbar_image;
    RecyclerView recyclerViewBibleStudy;
    private Intent intent;
    private Context mContext;
    private Toolbar toolbar;
    public static ArrayList<String> myList = new ArrayList<>();
    ArrayList<Integer> chapterCount = new ArrayList<>();
    private Subscription mSubscription;
    public static ChapterReadModel mResponseModel = new ChapterReadModel();
    private String url, title, language;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getApplicationContext();

        // added crash report's to mail
        Fabric.with(getApplicationContext(), new Crashlytics());

        //assining layout
        setContentView(R.layout.bible_study);

        toolbar = findViewById(R.id.toolbar);
        toolbar_image=findViewById(R.id.toolbar_image);
        toolbar_image.setVisibility(View.GONE);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        intent = getIntent();
        if (intent != null) {
            /*
             * to check the intent value
             * @param  url
             *@param  title
             *@param  language
             */
            url = getIntent().getStringExtra("url");
            title = getIntent().getStringExtra("title");
            language = getIntent().getStringExtra("language");
        }

        // to start the Progress Dialog
        showProgressDialog();

        // API to Chapter list
        getBibleChapterTelugu();

        recyclerViewBibleStudy = findViewById(R.id.recyclerViewBibleStudy);
    }

    // Interface between adapter and class

    /*
     * intent value passing to BibleVerse
     * @param  versesList
     *@param  chapterCount
     *@param  position
     *@param  language
     *@param  address2
     *@param  totalChapterCount
    */
    @Override
    public void setCartClickListener(int position, String status) {
        if (status.equalsIgnoreCase("chapter")) {
            ChapterReadModel BaseListBible = mResponseModel;
            startActivity(new Intent(getApplicationContext(), BibleVerseActivity.class)
                    .putExtra("versesList", BaseListBible).putExtra("title", title)
                    .putExtra("chapterCount", chapterCount.get(position))
                    .putExtra("position", position)
                    .putExtra("language", language)
                    .putExtra("totalChapterCount", chapterCount.size()));

        }
    }
    // API to Chapter list
    private void getBibleChapterTelugu() {
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        mSubscription = service.GetChapter(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ChapterReadModel>() {
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
                    public void onNext(ChapterReadModel mResponse) {

                        // to hide the Progress Dialog
                        hideProgressDialog();
                        mResponseModel = mResponse;
                        myList.clear();
                        for (int i = 0; i < mResponse.getChapter().size(); i++) {
                            if (language.equalsIgnoreCase("Telugu")) {
                                myList.add("అధ్యాయము " + (i + 0));
                            } else {
                                myList.add("Chapter " + (i + 0));
                            }
                            chapterCount.add(i + 0);
                        }
                        BibleChapterAdapter bibleChapterAdapter = new BibleChapterAdapter(getApplicationContext(), myList, "chapter", null);
                        recyclerViewBibleStudy.setAdapter(bibleChapterAdapter);
                        recyclerViewBibleStudy.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        bibleChapterAdapter.setOnCartChangedListener(BibleChapterActivity.this);

                    }
                });
    }
}
