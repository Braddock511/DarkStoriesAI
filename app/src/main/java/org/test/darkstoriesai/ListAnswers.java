package org.test.darkstoriesai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class ListAnswers extends ArrayAdapter<Answer> {
    private final Context context;
    private final List<Answer> items;

    public ListAnswers(Context context, List<Answer> items) {
        super(context, R.layout.list_answers, items);
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_answers, parent, false);

        TextView headerViewItem = rowView.findViewById(R.id.topicViewItem);
        TextView textViewItem = rowView.findViewById(R.id.textViewItem);
        ProgressBar loadingIndicator = rowView.findViewById(R.id.loadingIndicator);

        Answer listItem = items.get(position);

        // Show or hide loading indicator based on the item state
        if (listItem.isLoading()) {
            loadingIndicator.setVisibility(View.VISIBLE);
            headerViewItem.setVisibility(View.GONE);
            textViewItem.setVisibility(View.GONE);
        } else {
            loadingIndicator.setVisibility(View.GONE);
            headerViewItem.setVisibility(View.VISIBLE);
            textViewItem.setVisibility(View.VISIBLE);

            headerViewItem.setText(listItem.getHeader());
            textViewItem.setText(listItem.getQuestion());
        }

        return rowView;
    }
}


