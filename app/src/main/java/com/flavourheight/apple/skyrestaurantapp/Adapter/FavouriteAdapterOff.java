package com.flavourheight.apple.skyrestaurantapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flavourheight.apple.skyrestaurantapp.Model.CartListPlanet;
import com.flavourheight.apple.skyrestaurantapp.Model.FavouritePlanet;
import com.flavourheight.apple.skyrestaurantapp.R;

import java.util.List;

public class FavouriteAdapterOff extends RecyclerView.Adapter<FavouriteAdapterOff.ListHolder> {
    private List<FavouritePlanet> mPlanetList;

    public FavouriteAdapterOff(List<FavouritePlanet> mPlanetList) {
        this.mPlanetList = mPlanetList;

    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.favourite_item,parent,false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, int position) {


        holder.textViewitemname.setText(mPlanetList.get(position).getItemName());
        holder.textViewsubitemname.setText(mPlanetList.get(position).getSubItemName());
        holder.textViewrate.setText(mPlanetList.get(position).getRate());

    }

    @Override
    public int getItemCount() {
        return mPlanetList.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder {
        TextView textViewitemname,textViewsubitemname,textViewrate;

        public ListHolder(View itemView) {
            super(itemView);
            // parentlayout = itemView.findViewById(R.id.list);
            textViewitemname = (TextView) itemView.findViewById(R.id.tvfavouriteitemname);
            textViewsubitemname = (TextView) itemView.findViewById(R.id.tvfavouritename);
            textViewrate = (TextView) itemView.findViewById(R.id.tvfavouriterate);


//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mlistner != null) {
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            mlistner.onItemClick(position);
//                        }
//                    }
//                }
//            });

        }
    }
   }
