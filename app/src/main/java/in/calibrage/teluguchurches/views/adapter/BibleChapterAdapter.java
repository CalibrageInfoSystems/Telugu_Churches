package in.calibrage.teluguchurches.views.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import in.calibrage.teluguchurches.R;

/**
 * This adapter is helpfull to show bible chapters name
 */

public class BibleChapterAdapter extends RecyclerView.Adapter<BibleChapterAdapter.ViewHolder> {
    Context context;
    private ArrayList<String> verseList = new ArrayList<>();
    private ArrayList<String> dataList = new ArrayList<>();
    private ArrayList<String> verseCount = new ArrayList<>();
    private OnCartChangedListener onCartChangedListener;
    private String comingFrom;
    private int selectedPosition = -1;


    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *  @param context
     * @param verseList
     * @param comingFrom
     * @param dataList
     * */
    public BibleChapterAdapter(Context context, ArrayList<String> verseList, String comingFrom, ArrayList<String> dataList) {
        this.context = context;
        this.verseList = verseList;
        this.comingFrom = comingFrom;
        this.dataList = dataList;
    }

    /*
     * creating viewholder to recycler view here
     * */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bible_chapter_adapter, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    /*
     * binding data to viewholder from API here
     * */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (comingFrom.equalsIgnoreCase("chapter")) {
            holder.chapterText.setText(verseList.get(position).toString());
            holder.chapterLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCartChangedListener.setCartClickListener(position, "chapter");
                }
            });
        } else {
            if (position == selectedPosition) {
                holder.itemView.setSelected(true);
            } else {
                holder.itemView.setSelected(false);
                holder.chapterLay.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.selector_row));
            }

            holder.chapterText.setText(dataList.get(position));
            holder.chapterLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentPosition = holder.getLayoutPosition();
                    if (selectedPosition != currentPosition) {
                        // Temporarily save the last selected position
                        int lastSelectedPosition = selectedPosition;
                        // Save the new selected position
                        selectedPosition = currentPosition;
                        // update the previous selected row
                        notifyItemChanged(lastSelectedPosition);
                        // select the clicked row
                        holder.itemView.setSelected(true);

                    }

                }
            });
        }


    }

    // items count
    @Override
    public int getItemCount() {
        if (comingFrom.equalsIgnoreCase("chapter")) {
            return verseList.size();
        } else {
            return dataList.size();
        }
    }

    /*
     * initialising viewholder to recycler view
     * */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView chapterText;
        RelativeLayout chapterLay;

        public ViewHolder(View itemView) {
            super(itemView);
            chapterText = itemView.findViewById(R.id.chapterText);
            chapterLay = itemView.findViewById(R.id.chapterLay);

        }
    }

    /**
     * @param onCartChangedListener
     */
    public void setOnCartChangedListener(OnCartChangedListener onCartChangedListener) {
        this.onCartChangedListener = onCartChangedListener;
    }


    /**
     * Container Activity must implement this interface
     * you can define any parameter as per your requirement
     */
    public interface OnCartChangedListener {
        void setCartClickListener(int position, String status);
    }
}
