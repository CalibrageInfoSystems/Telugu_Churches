package in.calibrage.teluguchurches.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.views.Activities.Option;

/**
 * This adapter is helpfull to choose the different file for file chosser
 */

public class FileArrayAdapter extends ArrayAdapter<Option> {

    private Context c;
    private int id;
    private List<Option> items;

    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *
     * @param context
     * @param textViewResourceId
     * @param objects
     */
    public FileArrayAdapter(Context context, int textViewResourceId,
                            List<Option> objects) {
        super(context, textViewResourceId, objects);
        c = context;
        id = textViewResourceId;
        items = objects;
    }

    public Option getItem(int i) {
        return items.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) c
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(id, null);
        }
        final Option o = items.get(position);
        if (o != null) {
            ImageView im = v.findViewById(R.id.img1);
            TextView t1 = v.findViewById(R.id.TextView01);
            TextView t2 = v.findViewById(R.id.TextView02);

            if (o.getData().equalsIgnoreCase("folder")) {
                im.setImageResource(R.drawable.ic_folder_image);
            } else if (o.getData().equalsIgnoreCase("parent directory")) {
                im.setImageResource(R.drawable.ic_back_button);
            } else {
                /*
                 * assigning different images for various types of files
                 * */
                String name = o.getName().toLowerCase();
                if (name.endsWith(".docx"))
                    im.setImageResource(R.drawable.docx_sbi);
                else if (name.endsWith(".doc"))
                    im.setImageResource(R.drawable.doc_sbi);
                else if (name.endsWith(".pdf"))
                    im.setImageResource(R.drawable.pdf_sbi);
                else if (name.endsWith(".txt"))
                    im.setImageResource(R.drawable.txt_sbi);
            }

            if (t1 != null)
                t1.setText(o.getName());
            if (t2 != null)
                t2.setText(o.getData());

        }
        return v;
    }
}
