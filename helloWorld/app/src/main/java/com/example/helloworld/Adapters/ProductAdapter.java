package com.example.helloworld.Adapters;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.helloworld.Interfaces.ItemClickListener;
import com.example.helloworld.Model.Product;
import com.example.helloworld.ProductDetailsActivity;
import com.example.helloworld.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ProductAdapter extends FirebaseRecyclerAdapter<Product, ProductAdapter.ProductViewHolder> {

    Context context;
    private ItemClickListener listener;
    Product product;
    public ProductAdapter(@NonNull FirebaseRecyclerOptions<Product> options,Context context,Product product, ItemClickListener listener) {
        super(options);
        this.context = context;
        this.listener = listener;
        this.product = product;

    }

    @Override
    protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Product product) {
        productViewHolder.tv_name.setText(product.getProductName());
        productViewHolder.tv_price.setText("NIS"+product.getPrice());
        productViewHolder.tv_desc.setText(product.getDescription());
        Glide.with(productViewHolder.iv_productImage.getContext()).load(product.getImage()).into(productViewHolder.iv_productImage);

        productViewHolder.iv_productImage.setTag(product.getImage());
//        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, ProductDetailsActivity.class);
//                intent.putExtra("pid",product.getPid());
//            }
//        });
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_product, parent, false);
        return new ProductViewHolder(view);    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name, tv_price, tv_desc;
        public ImageView iv_productImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_productImage = itemView.findViewById(R.id.cutom_item_iv_productImage);
            tv_name = itemView.findViewById(R.id.custom_item_pName);
            tv_price = itemView.findViewById(R.id.custom_item_pPrice);
            tv_desc = itemView.findViewById(R.id.custom_item_pDesc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = (String) iv_productImage.getTag();
//                    listener.onItemClick(id);
                }
            });
        }
    }
}
