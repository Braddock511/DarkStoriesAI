package org.test.darkstoriesai;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListStories extends ArrayAdapter<Map<String, Object>> {
    private final Context context;
    private final Activity activity;

    public ListStories(Context context, Activity activity, ArrayList<Map<String, Object>> items) {
        super(context, R.layout.list_stories, items);
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_stories, parent, false);

        TextView headerViewItem = rowView.findViewById(R.id.topicViewItem);
        LinearLayout saveLayout = rowView.findViewById(R.id.save);

        Map<String, Object> story = getItem(position);

        headerViewItem.setText(story.get("topic").toString());

        Button loadButton = rowView.findViewById(R.id.loadButton);

        if (loadButton.getParent() != null) {
            ((ViewGroup) loadButton.getParent()).removeView(loadButton); // Remove it from the parent
            loadButton.setVisibility(View.VISIBLE);

            loadButton.setOnClickListener(view ->{
                activity.finish();

                Intent mainActivity = new Intent(activity, MainActivity.class);
                List<Map<String, Object>> storyWrapper = new ArrayList<>();
                
                storyWrapper.add(story);
                mainActivity.putExtra("story", (Serializable) storyWrapper);
                activity.startActivity(mainActivity);
            });
        }

        saveLayout.addView(loadButton);

        return rowView;
    }
}
