package com.example.helloworld.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.helloworld.Interfaces.ItemClickListener;
import com.example.helloworld.R;

public class PViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView tv_name, tv_price, tv_desc;
    public ImageView iv_productImage;

    public ItemClickListener listner;
    public PViewHolder(@NonNull View itemView) {
        super(itemView);
        iv_productImage = itemView.findViewById(R.id.cutom_item_iv_productImage);
        tv_name = itemView.findViewById(R.id.custom_item_pName);
        tv_price = itemView.findViewById(R.id.custom_item_pPrice);
        tv_desc = itemView.findViewById(R.id.custom_item_pDesc);
    }

    public void setItemClickListner(ItemClickListener listner){
        this.listner = listner;
    }
    @Override
    public void onClick(View v) {
    listner.onClick(v,getAdapterPosition(),false);
    }
}
