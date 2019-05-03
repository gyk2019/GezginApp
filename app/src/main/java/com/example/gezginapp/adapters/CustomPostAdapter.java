package com.example.gezginapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gezginapp.R;
import com.example.gezginapp.models.PostModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomPostAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    List<PostModel> postModelList;

    public CustomPostAdapter(LayoutInflater layoutInflater, List<PostModel> postModelList) {
        this.layoutInflater = layoutInflater;
        this.postModelList = postModelList;
    }

    @Override
    public int getCount() {
        return postModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return postModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context=parent.getContext();

        PostModel postModel = postModelList.get(position);

        View postView = layoutInflater.inflate(R.layout.post_list, null);
        ImageView ivPostPicture = (ImageView) postView.findViewById(R.id.iv_post_picture);
        TextView tvPostTitle = (TextView) postView.findViewById(R.id.tv_post_title);
        TextView tvPostDescription = (TextView) postView.findViewById(R.id.tv_post_description);
        Picasso.with(context)
                .load(postModel.getPostUrl())
                .into(ivPostPicture);

        tvPostTitle.setText(postModel.getPostName());
        tvPostDescription.setText(postModel.getPostDescription());

        return postView;
    }
}