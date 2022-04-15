package in.calibrage.teluguchurches.views.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.util.BaseActivity;
import in.calibrage.teluguchurches.views.adapter.BibleChapterAdapter;
import in.calibrage.teluguchurches.views.model.ChapterReadModel;
import io.fabric.sdk.android.Fabric;



public class BibleVerseActivity extends BaseActivity {
    private TextView selectedChapterTitle;
    private ImageButton prevBtn, nextBtn;
    private ImageView toolbar_image;
    private LinearLayout btnLay;
    RecyclerView recyclerViewBibleStudy;
    private BibleChapterAdapter bibleChapterAdapter;
    private Context mContext;
    private Toolbar toolbar;
    private int chapterCount, totalChapterCount,eventNum = 0,maxEvents, position, chapterPossition;
    private ChapterReadModel.Chapter chapter;
    private ChapterReadModel ChapterList;
    private ChapterReadModel.Chapter data;
    ArrayList<String> bigData = new ArrayList<>();
    private String verList, title, language;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getApplicationContext();

        // added crash report's to mail
        Fabric.with(getApplicationContext(), new Crashlytics());

        //assining layout
        setContentView(R.layout.bible_study);

        toolbar = findViewById(R.id.toolbar);
        toolbar_image = findViewById(R.id.toolbar_image);
        toolbar_image.setVisibility(View.GONE);
        if (getIntent() != null) {

            /*
             * to check the intent value
             * @param  chapterPossition
             *@param  verList
             *@param  language
             *@param  title
             *@param  chapterCount
             *@param  totalChapterCount
            */
            chapterPossition = getIntent().getIntExtra("position", 0);
            verList = getIntent().getStringExtra("versesList");
            language = getIntent().getStringExtra("language");
            title = getIntent().getStringExtra("title");
            chapterCount = getIntent().getIntExtra("chapterCount", 0);
            totalChapterCount = getIntent().getIntExtra("totalChapterCount", -1);
            getIntent().getSerializableExtra("versesList");
        }
        eventNum = chapterPossition;
        toolbar.setTitle("Bible");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnLay = findViewById(R.id.btnLay);
        selectedChapterTitle = findViewById(R.id.selectedChapterTitle);
        btnLay.setVisibility(View.VISIBLE);
        prevBtn = findViewById(R.id.prevBtn);
        nextBtn = findViewById(R.id.nextBtn);
        recyclerViewBibleStudy = findViewById(R.id.recyclerViewBibleStudy);
        ChapterList = (ChapterReadModel) getIntent().getSerializableExtra("versesList");
        chapter = ChapterList.getChapter().get(chapterPossition);
        selectedChapterTitle.setText(title + " " + chapterCount);

        position = chapter.getVerse().size();
        for (int i = 0; i < chapter.getVerse().size(); i++) {
            bigData.add((i + 1) + ". " + chapter.getVerse().get(i).getVerse());
            bibleChapterAdapter = new BibleChapterAdapter(BibleVerseActivity.this, null, "verse", bigData);
            recyclerViewBibleStudy.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            recyclerViewBibleStudy.setAdapter(bibleChapterAdapter);
        }

        maxEvents = totalChapterCount;

        /**
         * @param OnClickListner
         */
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventNum >= 1) {
                    eventNum = eventNum - 1;
                    selectedChapterTitle.setText(title + " " + eventNum);
                    bigData.clear();
//                    if (bigData.size() < 2) {
                    ChapterReadModel.Chapter data = ChapterList.getChapter().get(eventNum);
                    for (int i = 0; i < data.getVerse().size(); i++) {
                        bigData.add((i + 1) + ". " + data.getVerse().get(i).getVerse());
                    }
//                    }
                    bibleChapterAdapter = new BibleChapterAdapter(BibleVerseActivity.this, null, "verse", bigData);
                    recyclerViewBibleStudy.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    recyclerViewBibleStudy.setAdapter(bibleChapterAdapter);
                    bibleChapterAdapter.notifyDataSetChanged();
                }


            }
        });

        /**
         * @param OnClickListner
         */
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventNum < maxEvents - 1) {
                    eventNum = eventNum + 1;
                    bigData.clear();
//                    if (bigData.size() < 2) {
                    data = ChapterList.getChapter().get(eventNum);
                    selectedChapterTitle.setText(title + " " + eventNum);
                    for (int i = 0; i < data.getVerse().size(); i++) {
                        bigData.add((i + 1) + ". " + data.getVerse().get(i).getVerse());
                    }
//                    }
                    bibleChapterAdapter = new BibleChapterAdapter(BibleVerseActivity.this, null, "verse", bigData);
                    recyclerViewBibleStudy.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    recyclerViewBibleStudy.setAdapter(bibleChapterAdapter);
                    bibleChapterAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(BibleVerseActivity.this, "No Records found", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // call ActionBarDrawerToggle.onOptionsItemSelected(), if it returns true
        switch (item.getItemId()) {
            // Respond to the action bar's action bibleHome button
            case R.id.action_bibleHome:
                if (language.equalsIgnoreCase("Telugu")) {
                    Intent i = new Intent(BibleVerseActivity.this, BibleStudyActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                } else {
                    Intent i = new Intent(BibleVerseActivity.this, BibleStudyEnglish.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }

                break;

            default:
                break;
        }

        return true;
    }


}
