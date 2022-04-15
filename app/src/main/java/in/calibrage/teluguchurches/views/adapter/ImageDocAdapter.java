package in.calibrage.teluguchurches.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.interfaces.DeleteImageListiner;

/*
 * this adater is helpful to show selected doument in image for careers apply screen
 * */
public class ImageDocAdapter extends RecyclerView.Adapter<ImageDocAdapter.MyHolder> {
    private Context context;
    private ArrayList<String> bitmapArrayList;
    private DeleteImageListiner deleteImageListiner;

    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *
     * @param context
     * @param bitmaipArrayList
     */
    public ImageDocAdapter(Context context, ArrayList<String> bitmaipArrayList) {
        this.context = context;
        this.bitmapArrayList = bitmaipArrayList;
    }

    /*
     * creating viewholder to recycler view here
     * */
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_images, null);
        ImageDocAdapter.MyHolder mh = new ImageDocAdapter.MyHolder(v);
        return mh;
    }

    /*
     * binding data to viewholder from here
     * */
    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        if (bitmapArrayList.get(holder.getAdapterPosition()) != null) {
            Picasso.with(context).load(new File(bitmapArrayList.get(holder.getAdapterPosition()))).into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.doc_sbi);
        }

        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteImageListiner.onAdapterClickListiner(holder.getAdapterPosition(), false);
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (bitmapArrayList.get(holder.getAdapterPosition()) != null) {
                    deleteImageListiner.onAdapterClickListiner(holder.getAdapterPosition(), true);
                }
            }
        });
    }

    // items count
    @Override
    public int getItemCount() {
        return bitmapArrayList.size();
    }


    /*
     * initialising viewholder to recycler view
     * */
    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageView imageView, deleteIcon;

        public MyHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            deleteIcon = itemView.findViewById(R.id.deleteIcon);
        }
    }

    /**
     * @param onAdapterListener
     */
    public void setOnAdapterListener(DeleteImageListiner onAdapterListener) {
        this.deleteImageListiner = onAdapterListener;
    }
}
