package org.test.darkstoriesai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

        ListItem listItem = items.get(position);
        headerViewItem.setText(listItem.getHeader());
        textViewItem.setText(listItem.getQuestion());

        return rowView;
    }
}


