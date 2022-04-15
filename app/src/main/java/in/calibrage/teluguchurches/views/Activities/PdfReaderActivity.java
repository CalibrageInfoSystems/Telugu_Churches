package in.calibrage.teluguchurches.views.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class PdfReaderActivity extends AppCompatActivity {
    private Intent intent;
    private String document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // added crash report's to mail
        Fabric.with(PdfReaderActivity.this, new Crashlytics());
//        setContentView(R.layout.activity_pdf_reader);


        intent = getIntent();
        if (intent != null) {
            /*
             * to check the intent value
             * @param  document
           */
            document = intent.getStringExtra("document");
        }

        // Open pdf flie link
        openLinkInBrowser(PdfReaderActivity.this, document);



    }

    // Open pdf flie link
    public void openLinkInBrowser(Context mContext, String link) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setType("vnd.android-dir/mms-sms");
            i.setData(Uri.parse(link));
            mContext.startActivity(i);
            finish();

        } catch (Exception e) {
            Toast.makeText(mContext, "issue with URL", Toast.LENGTH_SHORT).show();
        }

    }


}
