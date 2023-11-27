package org.test.darkstoriesai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class AdapterListItems extends ArrayAdapter<ListItem> {
    private Context context;
    private List<ListItem> items;

    // Constructor for the custom adapter
    public AdapterListItems(Context context, List<ListItem> items) {
        super(context, R.layout.list_item_layout, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the inflater to inflate the custom list item layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Inflate the custom list item layout
        View rowView = inflater.inflate(R.layout.list_item_layout, parent, false);

        // Set the data for the item
        TextView headerViewItem = rowView.findViewById(R.id.headerViewItem);
        TextView textViewItem = rowView.findViewById(R.id.textViewItem);
        ProgressBar loadingIndicator = rowView.findViewById(R.id.loadingIndicator);

        ListItem listItem = items.get(position);

        // Show or hide loading indicator based on the item state
        if (listItem.isLoading()) {
            loadingIndicator.setVisibility(View.VISIBLE);
            headerViewItem.setVisibility(View.GONE);
            textViewItem.setVisibility(View.GONE);
        } else {
            loadingIndicator.setVisibility(View.GONE);
            headerViewItem.setVisibility(View.VISIBLE);
            textViewItem.setVisibility(View.VISIBLE);

            // Set the question text
            headerViewItem.setText(listItem.getHeader());
            textViewItem.setText(listItem.getQuestion());
        }

        return rowView;
    }
}


