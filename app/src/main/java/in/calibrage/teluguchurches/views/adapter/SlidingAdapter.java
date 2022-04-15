package in.calibrage.teluguchurches.views.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.views.model.GetAllBannersId;

/**
 * this adapter is helpful to show the banners in home screen
 */
public class SlidingAdapter extends PagerAdapter {
    private LayoutInflater inflater;
    private Context context;
    GetAllBannersId images = new GetAllBannersId();

    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *
     * @param context
     * @param images
     */
    public SlidingAdapter(Context context, GetAllBannersId images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    // items count
    @Override
    public int getCount() {
        return images.getListResult().size();

    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = imageLayout.findViewById(R.id.image);

        Glide.with(context).load(images.getListResult().get(position).getBannerImage())
                .fitCenter()
                .into(imageView);
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


}
