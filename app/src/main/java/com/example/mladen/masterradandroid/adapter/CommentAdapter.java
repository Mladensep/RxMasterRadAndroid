package com.example.mladen.masterradandroid.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mladen.masterradandroid.R;
import com.example.mladen.masterradandroid.model.CommentModel;

import java.util.List;


public class CommentAdapter extends BaseAdapter {
    private Context context;
    private List<CommentModel> commentModels;

    public CommentAdapter(Context context, List<CommentModel> commentModels) {
        this.context = context;
        this.commentModels = commentModels;
    }

    @Override
    public int getCount() {
        return commentModels.size();
    }

    @Override
    public Object getItem(int position) {
        return commentModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater li = LayoutInflater.from(context);
            convertView = li.inflate(R.layout.item_comment, null, false);
        }

        CommentModel model = commentModels.get(position);

        TextView textView = (TextView) convertView.findViewById(R.id.comment_id);
        textView.setText(model.getCom() + "\n");

        return convertView;
    }
}
