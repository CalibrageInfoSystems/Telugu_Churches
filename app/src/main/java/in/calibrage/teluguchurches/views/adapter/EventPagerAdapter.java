package in.calibrage.teluguchurches.views.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.util.CommonUtil;
import in.calibrage.teluguchurches.views.model.GetUpComingEvents;

/**
 * This adapter is helpfull to show events in main screen
 */

public class EventPagerAdapter extends PagerAdapter {
    private LayoutInflater inflater;
    private Context context;
    GetUpComingEvents dataList;
    private OnCartChangedListener onCartChangedListener;


    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *
     * @param context
     * @param dataList
     */
    public EventPagerAdapter(Context context, GetUpComingEvents dataList) {
        this.context = context;
        this.dataList = dataList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    // items count
    @Override
    public int getCount() {
        return dataList.getListResult().size();

    }

    /*
     * binding data to viewholder from API here
     * */
    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimagesview_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = imageLayout.findViewById(R.id.image);
        final TextView churchNameTxt = imageLayout.findViewById(R.id.churchNameTxt);
        final TextView mobileNoTxt = imageLayout.findViewById(R.id.mobileNoTxt);
        final TextView titleTxt = imageLayout.findViewById(R.id.titleTxt);
        final TextView openTimeTxt = imageLayout.findViewById(R.id.openTimeTxt);
        final TextView pastorNameTxt = imageLayout.findViewById(R.id.pastorNameTxt);
        final RelativeLayout mainLay = imageLayout.findViewById(R.id.mainLay);
        imageView.setColorFilter(ContextCompat.getColor(context, R.color.primaryTransColor), android.graphics.PorterDuff.Mode.MULTIPLY);

        if (dataList.getListResult().get(position).getChurchName() != null) {
            churchNameTxt.setText(CommonUtil.fromHtml("<b><font color=\"#ffffff\">" + context.getString(R.string.church_names) + "</font><b>" + " " + " : " + " " + dataList.getListResult().get(position).getChurchName()));
        } else {
            churchNameTxt.setVisibility(View.GONE);
        }
        if (dataList.getListResult().get(position).getAuthorName() != null) {
            pastorNameTxt.setText(CommonUtil.fromHtml("<b><font color=\"#ffffff\">" + context.getString(R.string.pator_name) + "</font><b>" + " " + " : " + " " + dataList.getListResult().get(position).getAuthorName()));
        } else {
            pastorNameTxt.setVisibility(View.GONE);
        }
        if (dataList.getListResult().get(position).getTitle() != null) {
            titleTxt.setText(CommonUtil.fromHtml("<b><font color=\"#ffffff\">" + context.getString(R.string.event_names) + "</font><b>" + " " + " : " + " " + dataList.getListResult().get(position).getTitle()));
        } else {
            titleTxt.setText(CommonUtil.fromHtml("<b><font color=\"#ffffff\">" + context.getString(R.string.event_names) + "</font><b>" + " " + " : " + " " + ""));
            //titleTxt.setVisibility(View.GONE);
        }
        if (dataList.getListResult().get(position).getIsChurchEvent() == 1 || dataList.getListResult().get(position).getContactNumber() != null) {
            mobileNoTxt.setText(CommonUtil.fromHtml("<b><font color=\"#ffffff\">"
                    + context.getString(R.string.contact_number) + " " + ":" + " " + "</font><b>" + dataList.getListResult().get(position).getContactNumber()));
        } else {
            if (dataList.getListResult().get(position).getMobileNumber() != null) {
                mobileNoTxt.setText(CommonUtil.fromHtml("<b><font color=\"#ffffff\">" + context.getString(R.string.mobile_number_event)
                        + " " + ":" + " " + "</font><b>" + dataList.getListResult().get(position).getMobileNumber()));
            } else {
                mobileNoTxt.setVisibility(View.GONE);
            }

        }
        String from = "<b><font color='#ffffff'>" + context.getString(R.string.event_from) + " </font>  <b><font color='#ffffff'>" + " " + " : " + " " + CommonUtil.formatDateTimeUi(dataList.getListResult().get(position).getStartDate()) + "</font>";
        String to = "<b><font color='#ffffff'>" + context.getString(R.string.to) + " </font>  <b><font color='#ffffff'>" + " " + " : " + " " + CommonUtil.formatDateTimeUi(dataList.getListResult().get(position).getEndDate()) + "</font>";

        openTimeTxt.setText(Html.fromHtml(from + " " + " " + to));


        Glide.with(context).load(dataList.getListResult().get(position).getEventImage())
                .fitCenter()
                .error(R.drawable.upcomingevents4)
                .into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCartChangedListener.setCartClickListener("event", position);
            }
        });
        view.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    /**
     * Container Activity must implement this interface
     * you can define any parameter as per your requirement
     */
    public interface OnCartChangedListener {
        void setCartClickListener(String status, int position);

    }

    /**
     * @param onCartChangedListener
     */
    public void setOnCartChangedListener(OnCartChangedListener onCartChangedListener) {
        this.onCartChangedListener = onCartChangedListener;
    }

}
