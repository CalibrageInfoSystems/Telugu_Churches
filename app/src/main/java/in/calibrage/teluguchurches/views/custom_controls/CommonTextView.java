package in.calibrage.teluguchurches.views.custom_controls;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import in.calibrage.teluguchurches.util.Fontcache;


/*
 * this class is created to maintain the custom and commmom textview
 * this will extends the textview
 * we can use this button anywhere in project
 * we need to import this class
 * */

public class CommonTextView extends android.support.v7.widget.AppCompatTextView {

    public CommonTextView(Context context) {
        super(context);
        ApplyCustomFont(context);
    }

    public CommonTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ApplyCustomFont(context);
    }

    public CommonTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ApplyCustomFont(context);
    }


    //appliying custom font
    private void ApplyCustomFont(Context context) {
        Typeface customFont = Fontcache.gettypeFace(context, "Font-Regular.ttf");
        setTypeface(customFont);
    }


}
