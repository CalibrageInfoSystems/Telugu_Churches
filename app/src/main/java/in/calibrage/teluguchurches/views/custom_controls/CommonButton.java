package in.calibrage.teluguchurches.views.custom_controls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import in.calibrage.teluguchurches.util.Fontcache;

/*
 * this class is created to maintain the custom and commmom button
 * this will extends the Button
 * we can use this Button anywhere in project
 * we need to import this class
 * */

@SuppressLint("AppCompatCustomView")
public class CommonButton extends Button {
    public CommonButton(Context context) {
        super(context);
        ApplyCustomFont(context);
    }

    public CommonButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        ApplyCustomFont(context);

    }

    public CommonButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ApplyCustomFont(context);
    }

    // appliying custom font
    private void ApplyCustomFont(Context context) {
        Typeface customFont = Fontcache.gettypeFace(context, "Font-Regular.ttf");
        setTypeface(customFont);
    }
}
