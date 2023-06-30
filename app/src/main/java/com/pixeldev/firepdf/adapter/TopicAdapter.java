package com.pixeldev.firepdf.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pixeldev.firepdf.activities.PdfViewActivity;
import com.cloud.firepdf.R;
import com.pixeldev.firepdf.models.TopicModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.MyViewHolder> {
    private List<TopicModel> topicModelList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView book_card;
        public ImageView iv_thumb;
        public TextView tv_desc;
        public TextView tv_name;

        public MyViewHolder(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_desc = (TextView) view.findViewById(R.id.tv_desc);
            iv_thumb = (ImageView) view.findViewById(R.id.iv_thumb);
            book_card = (CardView) view.findViewById(R.id.book_card);
        }
    }

    public TopicAdapter(List<TopicModel> list) {
        topicModelList = list;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_topic, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        final TopicModel bookModel = topicModelList.get(i);
        myViewHolder.tv_name.setText(bookModel.getTitle());
        myViewHolder.tv_desc.setText(bookModel.getContent());
        Glide.with(myViewHolder.itemView.getContext()).load(bookModel.getImage()).into(myViewHolder.iv_thumb);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PdfViewActivity.class);
                intent.putExtra("title", bookModel.getTitle());
                intent.putExtra("file_url", bookModel.getLink());
                view.getContext().startActivity(intent);
            }
        });
    }

    public int getItemCount() {
       return topicModelList.size();
    }
}
