package com.example.helloworld.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.helloworld.Adapters.ProductAdapter;
import com.example.helloworld.Interfaces.ItemClickListener;
import com.example.helloworld.Model.Product;
import com.example.helloworld.ProductDetailsActivity;
import com.example.helloworld.R;
import com.example.helloworld.ViewHolder.PViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment {

    private DatabaseReference productRef;
    private RecyclerView rv;
    private RecyclerView.LayoutManager layoutManager;
    private ProductAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_product, container, false);
        rv = v.findViewById(R.id.recycler_menu);

        productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        return v;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv.setHasFixedSize(true);
//        rv.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Product> options
                = new FirebaseRecyclerOptions.Builder<Product>().setQuery(productRef, Product.class).build();

        FirebaseRecyclerAdapter<Product, PViewHolder> adapter = new FirebaseRecyclerAdapter<Product, PViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PViewHolder pViewHolder, int i, @NonNull Product product) {
                pViewHolder.tv_name.setText(product.getProductName());
                pViewHolder.tv_price.setText("NIS " + product.getPrice());
                pViewHolder.tv_desc.setText(product.getDescription());


                Picasso.get().load(product.getImage()).into(pViewHolder.iv_productImage);

                pViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), ProductDetailsActivity.class);
                        intent.putExtra("pid", product.getPid());
                        startActivity(intent);

                    }
                });
//                Glide.with(pViewHolder.iv_productImage.getContext()).load(product.getImage()).into(pViewHolder.iv_productImage);

//                pViewHolder.iv_productImage.setTag(product.getImage());
            }

            @NonNull
            @Override
            public PViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_product, parent, false);
                PViewHolder holder = new PViewHolder(view);
                return holder;
            }
        };

        rv.setAdapter(adapter);

        adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
//        adapter.stopListening();
    }
}