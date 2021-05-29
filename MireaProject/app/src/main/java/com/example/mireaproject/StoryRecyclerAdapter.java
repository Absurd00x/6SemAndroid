package com.example.mireaproject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

public class StoryRecyclerAdapter extends RecyclerView.Adapter<StoryRecyclerAdapter.ViewHolder> {

    interface ActionCallback {
        void onLongClickListener(Story story);
    }

    private Context context;
    private List<Story> storyList;
    private ActionCallback callback;

    StoryRecyclerAdapter(Context context, List<Story> storyList) {
        this.context = context;
        this.storyList = storyList;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_story, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }

    void updateData(List<Story> stories) {
        this.storyList = stories;
        notifyDataSetChanged();
    }

    //View Holder
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        private TextView dateTextView;
        private TextView contentTextView;

        ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this::onLongClick);

            dateTextView = itemView.findViewById(R.id.dateTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
        }

        void bindData(int position) {
            Story story = storyList.get(position);
            dateTextView.setText(android.text.format.DateFormat.format(
                    "yyyy-MM-dd", story.date));
            contentTextView.setText(story.content);
        }

        @Override
        public boolean onLongClick(View v) {
            if (callback != null)
                callback.onLongClickListener(storyList.get(getAdapterPosition()));
            return true;
        }
    }

    void addActionCallback(ActionCallback actionCallbacks) {
        callback = actionCallbacks;
    }
}