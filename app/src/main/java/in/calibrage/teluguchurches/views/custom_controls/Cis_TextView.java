package in.calibrage.teluguchurches.views.custom_controls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;


/*
 * this class is created to maintain the custom and commmom textview
 * this will extends the textview
 * we can use this button anywhere in project
 * we need to import this class
 * */
@SuppressLint("AppCompatCustomView")
public class Cis_TextView extends TextView {
    private CharSequence mText;
    private int mIndex;
    private long mDelay = 500; //Default 500ms delay


    public Cis_TextView(Context context) {
        super(context);
    }

    public Cis_TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //Runnable handlers to handle the views
    private Handler mHandler = new Handler();
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            setText(mText.subSequence(0, mIndex++));
            if (mIndex <= mText.length()) {
                mHandler.postDelayed(characterAdder, mDelay);
            }
        }
    };

    public void animateText(CharSequence text) {
        // for text animation
        mText = text;
        mIndex = 0;

        setText("");
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }

    public void setCharacterDelay(long millis) {
        mDelay = millis;
    }
}
