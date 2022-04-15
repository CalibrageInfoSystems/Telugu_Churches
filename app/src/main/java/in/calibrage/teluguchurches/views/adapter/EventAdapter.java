package in.calibrage.teluguchurches.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.util.CommonUtil;
import in.calibrage.teluguchurches.views.model.GetUpComingEvents;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private Context context;
    ArrayList<GetUpComingEvents.ListResult> dataList = new ArrayList<>();
    private OnCartChangedListener onCartChangedListener;
    String fromStr, toStr;

    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *
     * @param context
     * @param dataList
     */

    public EventAdapter(Context context, ArrayList<GetUpComingEvents.ListResult> dataList) {
        this.dataList = dataList;
        this.context = context;

    }

    /*
     * creating viewholder to recycler view here
     * */
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_event_text, parent, false);
        return new EventAdapter.ViewHolder(view);
    }

    /*
     * binding data to viewholder from API here
     * */
    @Override
    public void onBindViewHolder(EventAdapter.ViewHolder holder, final int position) {

        if (dataList.get(position).getChurchName() != null) {
            holder.churchNameText.setText(CommonUtil.fromHtml("<b><font color=\"#606060\">" + context.getString(R.string.church_name) + " " + ":" + " " + "</font><b>" + dataList.get(position).getChurchName()));
        } else {
            holder.churchNameText.setVisibility(View.GONE);
            //  holder.churchNameText.setText(CommonUtil.fromHtml("<b><font color=\"#606060\">" + context.getString(R.string.church_name)+" "+":"+" " + "</font><b>" + ""));
        }
        if (dataList.get(position).getTitle() != null) {
            holder.eventText.setText(CommonUtil.fromHtml("<b><font color=\"#606060\">" + context.getString(R.string.event_name) + " " + ":" + " " + "</font><b>" + dataList.get(position).getTitle()));
        } else {
            holder.eventText.setText(CommonUtil.fromHtml("<b><font color=\"#606060\">" + context.getString(R.string.event_name) + " " + ":" + " " + "</font><b>" + ""));
        }

        if (dataList.get(position).getAuthorName() != null) {
            holder.authorNameText.setText(CommonUtil.fromHtml("<b><font color=\"#606060\">" + context.getString(R.string.author_name) + " " + ":" + " " + "</font><b>" + dataList.get(position).getAuthorName()));
        } else {
            holder.authorNameText.setVisibility(View.GONE);
            //holder.authorNameText.setText(CommonUtil.fromHtml("<b><font color=\"#606060\">" + context.getString(R.string.author_name) +" "+":"+" " +"</font><b>" +""));
        }

        if (dataList.get(position).getIsChurchEvent() == 1 || dataList.get(position).getContactNumber() != null) {
            holder.numberText.setText(CommonUtil.fromHtml("<b><font color=\"#606060\">"
                    + context.getString(R.string.contact_number) + " " + ":" + " " + "</font><b>" + dataList.get(position).getContactNumber()));
        } else {
            if (dataList.get(position).getMobileNumber() != null) {
                holder.numberText.setText(CommonUtil.fromHtml("<b><font color=\"#606060\">" + context.getString(R.string.mobile_number_event)
                        + " " + ":" + " " + "</font><b>" + dataList.get(position).getMobileNumber()));
            } else {
                holder.numberText.setVisibility(View.GONE);
            }

        }

        fromStr = dataList.get(position).getStartDate();
        toStr = dataList.get(position).getEndDate();
        String from = "<b><font color='#606060'>" + context.getString(R.string.start_date) + " " + ":" + " " + " </font>  <b><font color='#979494'>" + CommonUtil.formatDateTimeUi(fromStr) + "</font>";
        String to = "<b><font color='#606060'>" + context.getString(R.string.end_date) + " " + ":" + " " + " </font>  <b><font color='#979494'>" + CommonUtil.formatDateTimeUi(toStr) + "</font>";

        holder.openTimeText.setText(Html.fromHtml(from + " "));
        holder.closeTimeText.setText(Html.fromHtml(to + " "));

        Glide.with(context).load(dataList.get(position).getEventImage())
                .fitCenter()
                .error(R.drawable.church12)
                .into(holder.userImg);
        holder.mainrlty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCartChangedListener.setCartClickListener("event", position);
            }
        });
    }


    // items count
    @Override
    public int getItemCount() {
        return dataList.size();
    }


    /*
     * initialising viewholder to recycler view
     * */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView churchNameText, eventText, numberText, openTimeText, closeTimeText, authorNameText;
        private ImageView userImg;
        RelativeLayout mainrlty;

        public ViewHolder(View itemView) {
            super(itemView);
            churchNameText = itemView.findViewById(R.id.churchNameText);
            eventText = itemView.findViewById(R.id.eventText);
            authorNameText = itemView.findViewById(R.id.authorNameText);
            numberText = itemView.findViewById(R.id.numberText);
            openTimeText = itemView.findViewById(R.id.openTimeText);
            closeTimeText = itemView.findViewById(R.id.closeTimeText);
            userImg = itemView.findViewById(R.id.userImg);
            mainrlty = itemView.findViewById(R.id.mainrlty);
        }
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
