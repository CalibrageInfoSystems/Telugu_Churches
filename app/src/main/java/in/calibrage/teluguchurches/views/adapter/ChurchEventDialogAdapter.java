package in.calibrage.teluguchurches.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.util.CommonUtil;
import in.calibrage.teluguchurches.views.model.GetEventByDateAndUserIdResponseModel;


/**
 * This adapter is helpfull to show the particular church evnets information in dialog box
 */
public class ChurchEventDialogAdapter extends RecyclerView.Adapter<ChurchEventDialogAdapter.MyHolder> {
    private Context mContext;
    GetEventByDateAndUserIdResponseModel itemList = new GetEventByDateAndUserIdResponseModel();


    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *
     * @param context
     * @param itemList
     */
    public ChurchEventDialogAdapter(Context context, GetEventByDateAndUserIdResponseModel itemList) {
        this.mContext = context;
        this.itemList = itemList;

    }

    /*
     * creating viewholder to recycler view here
     * */
    @Override
    public ChurchEventDialogAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.adapter_dialog, null);
        MyHolder mh = new MyHolder(v);
        return mh;
    }

    /*
     * binding data to viewholder from API here
     * */
    @Override
    public void onBindViewHolder(ChurchEventDialogAdapter.MyHolder holder, int position) {
        holder.eventText.setText(itemList.getListResult().get(position).getEventTitle());
        holder.startDateText.setText(CommonUtil.dateFormat(itemList.getListResult().get(position).getStartDate()));
        holder.endDateText.setText(CommonUtil.dateFormat(itemList.getListResult().get(position).getEndDate()));
    }

    // items count
    @Override
    public int getItemCount() {
        return (null != itemList ? itemList.getListResult().size() : 0);
    }

    /*
     * initialising viewholder to recycler view
     * */
    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView eventText, startDateText, endDateText;
        private RelativeLayout titleLay;

        public MyHolder(View itemView) {
            super(itemView);
            eventText =  itemView.findViewById(R.id.eventText);
            startDateText = itemView.findViewById(R.id.startDateText);
            endDateText = itemView.findViewById(R.id.endDateText);
        }
    }
}
