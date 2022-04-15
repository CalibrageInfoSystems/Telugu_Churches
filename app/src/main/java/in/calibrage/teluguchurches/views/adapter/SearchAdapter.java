package in.calibrage.teluguchurches.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.views.model.GetAllChurchesResponseModel;

/**
 * This adapter is helpfull for searching in careers view
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private Context context;
    private String openTime, closeTime;
    GetAllChurchesResponseModel dataList = new GetAllChurchesResponseModel();

    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *
     * @param context
     * @param dataList
     */
    public SearchAdapter(Context context, GetAllChurchesResponseModel dataList) {
        this.dataList = dataList;
        this.context = context;


    }

    /*
     * creating viewholder to recycler view here
     * */
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_allchurches, parent, false);
        return new SearchAdapter.ViewHolder(view);

    }

    /*
     * binding data to viewholder from API here
     * */
    @Override
    public void onBindViewHolder(SearchAdapter.ViewHolder holder, int position) {
        if (holder instanceof SearchAdapter.ViewHolder) {
            if (dataList.getListResult().get(position).getOpeningTime() != null && dataList.getListResult().get(position).getClosingTime() != null && !dataList.getListResult().get(position).getOpeningTime().equals("") && !dataList.getListResult().get(position).getClosingTime().equals("")) {
                String open = dataList.getListResult().get(position).getOpeningTime();
                String close = dataList.getListResult().get(position).getClosingTime();
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

            ((SearchAdapter.ViewHolder) holder).churchNameText.setText(dataList.getListResult().get(position).getName());
            ((SearchAdapter.ViewHolder) holder).emailIdText.setText(dataList.getListResult().get(position).getEmail());
            ((SearchAdapter.ViewHolder) holder).mobileNumberText.setText(dataList.getListResult().get(position).getContactNumber());
            ((SearchAdapter.ViewHolder) holder).stateText.setText(dataList.getListResult().get(position).getStateName());
            ((SearchAdapter.ViewHolder) holder).districtNameText.setText(dataList.getListResult().get(position).getDistrictName());
            ((SearchAdapter.ViewHolder) holder).villageNameText.setText(dataList.getListResult().get(position).getVillageName());
            ((SearchAdapter.ViewHolder) holder).timingText.setText(openTime + "-" + closeTime);


            Glide.with(context).load(dataList.getListResult().get(position).getChurchImage())
                    .fitCenter()
                    .error(R.drawable.churchimg_bg)
                    .into(((SearchAdapter.ViewHolder) holder).image);

        }
    }

    // items count
    @Override
    public int getItemCount() {
        return dataList.getListResult().size();
    }

    /*
     * initialising viewholder to recycler view
     * */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView churchNameText, emailIdText, mobileNumberText, stateText, districtNameText, villageNameText, timingText;
        private RelativeLayout mainLay;
        private ImageView image;

        public ViewHolder(View v) {
            super(v);
            churchNameText = v.findViewById(R.id.churchNameText);
            emailIdText = v.findViewById(R.id.emailIdText);
            mobileNumberText = v.findViewById(R.id.mobileNumberText);
            stateText = v.findViewById(R.id.stateText);
            districtNameText = v.findViewById(R.id.districtNameText);
            villageNameText = v.findViewById(R.id.villageNameText);
            timingText = v.findViewById(R.id.timingText);
            mainLay = v.findViewById(R.id.mainLay);
            image = v.findViewById(R.id.image);
        }
    }
}
