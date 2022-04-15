package in.calibrage.teluguchurches.views.adapter;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.views.model.GetAllChurchesResponseModel;
import rx.Subscription;

/**
 * This adapter is helpfull to show churches list in home screen
 */

public class churchHomepagerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private List<T> myDataSet;
    // The minimum amount of items to have below your current scroll position before loading more.
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount, subscibeval, churchId, authorId;
    private boolean loading;
    private churchHomepagerAdapter.OnLoadMoreListener onLoadMoreListener;
    private Subscription mSubscription;
    Context context;
    ArrayList<GetAllChurchesResponseModel.ListResult> churchesResponseModel;
    private churchHomepagerAdapter.OnCartChangedListener onCartChangedListener;
    private String openTime, closeTime;


    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *  @param context
     * @param myDataSet
     */
    public churchHomepagerAdapter(Context context, List<T> myDataSet, RecyclerView recyclerView) {
        this.myDataSet = myDataSet;
        this.context = context;
        churchesResponseModel = (ArrayList<GetAllChurchesResponseModel.ListResult>) myDataSet;

    }

    @Override
    public int getItemViewType(int position) {
        return myDataSet.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    /*
     * creating viewholder to recycler view here
     * */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_churches_home, parent, false);

            vh = new churchHomepagerAdapter.TextViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_item, parent, false);

            vh = new churchHomepagerAdapter.ProgressViewHolder(v);
        }
        return vh;
    }

    /*
     * binding data to viewholder from API here
     * */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof churchHomepagerAdapter.TextViewHolder) {
            if (churchesResponseModel.get(position).getOpeningTime() != null
                    && churchesResponseModel.get(position).getClosingTime() != null &&
                    !churchesResponseModel.get(position).getOpeningTime().equals("") &&
                    !churchesResponseModel.get(position).getClosingTime().equals("")) {
                String open = churchesResponseModel.get(position).getOpeningTime();
                String close = churchesResponseModel.get(position).getClosingTime();

                String filename = open.split("\\.")[0];
                String filenameClose = close.split("\\.")[0];

                DateFormat df = new SimpleDateFormat("HH:mm:ss");
                //Date/time pattern of desired output date
                DateFormat outputformat = new SimpleDateFormat("hh:mm aa");
                Date date = null;
                Date date1 = null;
                closeTime = null;
                openTime = null;
                try {
                    //Conversion of input String to date
                    date = df.parse(filenameClose);
                    date1 = df.parse(filename);
                    //old date format to new date format
                    closeTime = outputformat.format(date);
                    openTime = outputformat.format(date1);
                    System.out.println(closeTime);
                    System.out.println(openTime);
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
            }
            ((churchHomepagerAdapter.TextViewHolder) holder).churchNameText.setText(churchesResponseModel.get(position).getName());
            ((churchHomepagerAdapter.TextViewHolder) holder).emailIdText.setText(churchesResponseModel.get(position).getEmail());
            ((churchHomepagerAdapter.TextViewHolder) holder).mobileNumberText.setText(churchesResponseModel.get(position).getContactNumber());
            ((churchHomepagerAdapter.TextViewHolder) holder).stateText.setText(churchesResponseModel.get(position).getStateName());
            ((churchHomepagerAdapter.TextViewHolder) holder).districtNameText.setText(churchesResponseModel.get(position).getVillageName() + " " + "," + " "
                    + churchesResponseModel.get(position).getDistrictName());
            ((churchHomepagerAdapter.TextViewHolder) holder).villageNameText.setText(churchesResponseModel.get(position).getVillageName());
            ((churchHomepagerAdapter.TextViewHolder) holder).timingText.setText(openTime + "-" + closeTime);

            ((churchHomepagerAdapter.TextViewHolder) holder).image.setColorFilter(ContextCompat.getColor(context, R.color.primaryTransColor), android.graphics.PorterDuff.Mode.MULTIPLY);

            ((churchHomepagerAdapter.TextViewHolder) holder).mainLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCartChangedListener.setCartClickListenerchurh("MainClick", position);
                }
            });


            Glide.with(context).load(churchesResponseModel.get(position).getChurchImage())
                    .fitCenter()
                    .error(R.drawable.church13)
                    .into(((churchHomepagerAdapter.TextViewHolder) holder).image);

        }

    }


    public void setLoaded() {
        loading = false;
    }

    // items count
    @Override
    public int getItemCount() {
        return this.myDataSet.size();
    }

    //interface for view more functionality
    public void setOnLoadMoreListener(churchHomepagerAdapter.OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    //interface for view more functionality
    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    /*
     * initialising viewholder to recycler view
     * */
    public static class TextViewHolder extends RecyclerView.ViewHolder {
        public TextView churchNameText, emailIdText, mobileNumberText, stateText, districtNameText, villageNameText, timingText;
        private RelativeLayout mainLay;
        private ImageView image;


        public TextViewHolder(View v) {
            super(v);
            churchNameText = (TextView) v.findViewById(R.id.churchNameText);
            emailIdText = (TextView) v.findViewById(R.id.emailIdText);
            mobileNumberText = (TextView) v.findViewById(R.id.mobileNumberText);
            stateText = (TextView) v.findViewById(R.id.stateText);
            districtNameText = (TextView) v.findViewById(R.id.districtNameText);
            villageNameText = (TextView) v.findViewById(R.id.villageNameText);
            timingText = (TextView) v.findViewById(R.id.timingText);
            mainLay = (RelativeLayout) v.findViewById(R.id.mainLay);
            image = (ImageView) v.findViewById(R.id.image);

        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
        }
    }

    /**
     * @param onCartChangedListener
     */
    public void setCartClickListenerchurh(OnCartChangedListener onCartChangedListener) {
        this.onCartChangedListener = onCartChangedListener;
    }

    /**
     * Container Activity must implement this interface
     * you can define any parameter as per your requirement
     */
    public interface OnCartChangedListener {
        void setCartClickListenerchurh(String status, int position);
    }
}
