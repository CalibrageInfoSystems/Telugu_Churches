package in.calibrage.teluguchurches.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.util.CommonUtil;
import in.calibrage.teluguchurches.views.model.GetEventInFoByUserIdMonthYear;

/**
 * This adapter is helpfull to show the particular pastor evnets information
 */

public class AuthorEventInfoByUseridAdapterMonth   extends RecyclerView.Adapter<AuthorEventInfoByUseridAdapterMonth.ViewHolder> {

    Context mContext;
    GetEventInFoByUserIdMonthYear mResponse;

    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *  @param mContext
     * @param mResponse
     */
    public AuthorEventInfoByUseridAdapterMonth(Context mContext, GetEventInFoByUserIdMonthYear mResponse) {

        this.mContext=mContext;
        this.mResponse=mResponse;
    }


    @Override
    public AuthorEventInfoByUseridAdapterMonth.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_adapter, parent, false);
        return new ViewHolder(view);
    }

    /*
     * binding data to viewholder from API here
     * */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.authorText.setText((CommonUtil.fromHtml("<b><font color=\"#ffffff\">"  + mContext.getString(R.string.author_name) + "</font><b>" +" "+" : "+" "+ mResponse.getListResult().get(position).getAuthorName())));
        holder.eventText.setText(CommonUtil.fromHtml("<b><font color=\"#ffffff\">" +mContext.getString(R.string.event_name)+ "</font><b>" +" "+" : "+" "+ mResponse.getListResult().get(position).getTitle()));
        holder.numberText.setText((CommonUtil.fromHtml("<b><font color=\"#ffffff\">" + mContext.getString(R.string.mobile_number_event) + "</font><b>" +" "+" : "+" "+mResponse.getListResult().get(position).getMobileNumber())));
        String from = "<b><font color='#ffffff'>" + mContext.getString(R.string.event_from) +" "+" : "+" "+ " </font>  <b><font color='#ffffff'>"
                + CommonUtil.formatDateTimeUi(mResponse.getListResult().get(position).getStartDate()) + "</font>";
        String to = "<b><font color='#ffffff'>" +  mContext.getString(R.string.event_to) +" "+" : "+" "+ " </font>  <b><font color='#ffffff'>"
                + CommonUtil.formatDateTimeUi(mResponse.getListResult().get(position).getEndDate()) + "</font>";

         holder.openTimeText.setText(Html.fromHtml(from ));
         holder.closeTimeText.setText(Html.fromHtml( to));
    }


    // items count
    @Override
    public int getItemCount() {
        return mResponse.getListResult().size();
    }

    /*
     * initialising viewholder to recycler view
     * */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView authorText, eventText, numberText,
                openTimeText,closeTimeText;
        public ViewHolder(View itemView) {
            super(itemView);
            authorText = itemView.findViewById(R.id.authorText);
            eventText =  itemView.findViewById(R.id.eventText);
            numberText =  itemView.findViewById(R.id.numberText);
            openTimeText =  itemView.findViewById(R.id.openTimeText);
            closeTimeText =  itemView.findViewById(R.id.closeTimeText);
        }
    }
}
