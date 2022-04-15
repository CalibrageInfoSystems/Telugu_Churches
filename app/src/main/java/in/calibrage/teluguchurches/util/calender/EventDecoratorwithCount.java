package in.calibrage.teluguchurches.util.calender;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import in.calibrage.teluguchurches.R;


/*
 * To show the events in calendar screen as decorated
 * if event will be there for the date,that particular date will be shown as highlighted
 * */
public class EventDecoratorwithCount implements DayViewDecorator {
    private Drawable drawable;
    private int color;
    private CalendarDay dates;
    int count;

    public EventDecoratorwithCount(int color, CalendarDay dates, Activity context, int Count) {
        this.color = color;
        this.dates = dates;
        this.count = Count;
        drawable = writeOnDrawable(R.drawable.square_copy, count, context);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates != null && day.equals(dates);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);

    }

    public BitmapDrawable writeOnDrawable(int drawableId, int text, Activity context) {

        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), drawableId).copy(Bitmap.Config.ARGB_8888, true);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(75);
        Canvas canvas = new Canvas(bm);
        canvas.drawText("" + text, (float) (bm.getWidth() / 2.5), bm.getHeight() / 3, paint);
        return new BitmapDrawable(bm);
    }

}