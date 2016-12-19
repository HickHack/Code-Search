package graham.com.codesearch.search;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Map;

import graham.com.codesearch.R;

/**
 * Created by graham on 03/12/16.
 * Has map adapter for the key value pairs
 * in the result list view
 */

public class RepoMapAdapter extends BaseAdapter {

    private Map<String, String> data;
    private String[] keys;
    private Context context;
    private TextView keyTextView;
    private TextView valueTextView;

    public RepoMapAdapter(Context context, Map<String, String> data) {
        this.context = context;
        this.data = data;

        //Store the keys to be used to set the key text view values
        keys = data.keySet().toArray(new String[data.size()]);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(keys[position]);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view != null) {
            initializeWidgets(view);
        } else {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_repo_item, null);
            initializeWidgets(view);
        }

        //Set widget text for key, value
        keyTextView.setText(keys[pos]);

        if (getItem(pos) != null) {
            valueTextView.setText(getItem(pos).toString());
        }

        return view;
    }

    private void initializeWidgets(View view) {
        //Initialise the key value widgets
        keyTextView = (TextView) view.findViewById(R.id.repoKey);
        valueTextView = (TextView) view.findViewById(R.id.repoValue);
    }
}
