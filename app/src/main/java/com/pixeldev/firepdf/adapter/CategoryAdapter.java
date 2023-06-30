package com.pixeldev.firepdf.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cloud.firepdf.R;
import com.pixeldev.firepdf.activities.TopicActivity;
import com.pixeldev.firepdf.models.CategoryModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private List<CategoryModel> categoryModelList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView name_card;
        public TextView tv_name;

        public MyViewHolder(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            name_card = (CardView) view.findViewById(R.id.card_view);
        }
    }

    public CategoryAdapter(List<CategoryModel> list) {
        categoryModelList = list;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_category, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        final CategoryModel categoryModel = categoryModelList.get(i);
        myViewHolder.tv_name.setText(categoryModel.getTitle());
        Log.e("TAG_APP", "onBindViewHolder: "+categoryModel.topiclist );
        myViewHolder.name_card.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TopicActivity.class);
                intent.putExtra("model", categoryModel);
                view.getContext().startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return categoryModelList.size();
    }
}
