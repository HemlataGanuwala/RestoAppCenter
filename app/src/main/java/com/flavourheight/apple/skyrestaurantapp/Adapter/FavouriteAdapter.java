package com.flavourheight.apple.skyrestaurantapp.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flavourheight.apple.skyrestaurantapp.Model.FavouritePlanet;
import com.flavourheight.apple.skyrestaurantapp.R;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ListHolder>{

    private List<FavouritePlanet> mPlanetList;
    private OnItemClickListener mListener;


    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }

    public void setOnItemClickListner(OnItemClickListener listner){
        this.mListener = listner;
//        this.fragment = itemAllFragment;
    }

    public FavouriteAdapter(List<FavouritePlanet> mPlanetList1) {
        this.mPlanetList = mPlanetList1;

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
        byte[] decodeString = Base64.decode(mPlanetList.get(position).getFImage(), Base64.DEFAULT);
        Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        holder.imageViewitem.setImageBitmap(decodebitmap);
    }

    @Override
    public int getItemCount() {
        return mPlanetList.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder {

        TextView textViewitemname,textViewsubitemname,textViewrate;
        ImageView imageViewitem;

        public ListHolder(View itemView) {
            super(itemView);

            textViewitemname = (TextView) itemView.findViewById(R.id.tvfavouriteitemname);
            textViewsubitemname = (TextView) itemView.findViewById(R.id.tvfavouritename);
            textViewrate = (TextView) itemView.findViewById(R.id.tvfavouriterate);
            imageViewitem = (ImageView) itemView.findViewById(R.id.imgviewfavouriteitem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}
