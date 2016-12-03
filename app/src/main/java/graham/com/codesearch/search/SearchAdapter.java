package graham.com.codesearch.search;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

import graham.com.codesearch.R;
import graham.com.codesearch.search.model.Repo;

/**
 * Created by Graham Murray on 16/11/16.
 */

public class SearchAdapter extends ArrayAdapter<Repo> implements ListAdapter {

    private List<Repo> resultList;
    private Context context;
    private TextView nameTextView;
    private ImageView imageView;

    public SearchAdapter(Context context, int resId, List<Repo> resultList) {
        super(context, resId, resultList);

        this.resultList = resultList;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view != null) {
            initializeWidgets(view);
        } else {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_search_item, null);
            initializeWidgets(view);
        }

        configureTextView(position);
        configureImageView(position);

        return view;
    }

    private void configureImageView(int position) {
        byte[] imageArray = resultList.get(position).getOwner().getImage();

        if (imageArray != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
            imageView.setImageBitmap(bmp);
            nameTextView.invalidate();
        }

    }

    private void configureTextView(int position) {
        String name = resultList.get(position).getName();

        if (!name.isEmpty()) {
            if (name.length() > 20) {
                name = name.substring(0, Math.min(name.length(), 20)) + "...";
            }

            nameTextView.setText(name);
        }
    }

    private void initializeWidgets(View view) {
        nameTextView = (TextView) view.findViewById(R.id.name);
        imageView = (ImageView) view.findViewById(R.id.repoImageView);
    }

    @Override
    public int getCount() {
        return resultList.size();
    }
}
