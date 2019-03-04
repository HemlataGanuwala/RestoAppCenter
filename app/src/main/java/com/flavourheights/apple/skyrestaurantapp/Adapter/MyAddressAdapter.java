package com.flavourheights.apple.skyrestaurantapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flavourheights.apple.skyrestaurantapp.Model.AddressPlanet;
import com.flavourheights.apple.skyrestaurantapp.R;

import java.util.ArrayList;
import java.util.List;

public class MyAddressAdapter extends RecyclerView.Adapter<MyAddressAdapter.ListHolder> {

    public static List<AddressPlanet> mPlanetList;
    private OnItemClickListner mlistner;

    public interface OnItemClickListner
    {
        void onItemClick(int position);
        void icondeleteImageViewOnClick(View v, int position);
    }


    public void setOnItemClickListner(OnItemClickListner listner){
        this.mlistner = listner;
//        this.fragment = itemAllFragment;
    }

    public MyAddressAdapter(List<AddressPlanet> mPlanetList1) {
        this.mPlanetList = mPlanetList1;
    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.myaddress_list,parent,false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, int position) {

        String name = mPlanetList.get(position).getCustName();
        String addresstype = mPlanetList.get(position).getAddressType();
        String landmark = mPlanetList.get(position).getLandmark();
        String locality = mPlanetList.get(position).getLocality();
        String city = mPlanetList.get(position).getCity();
        String pincode = mPlanetList.get(position).getPincode();

        holder.textViewname.setText(name + " "+ addresstype);
        holder.textViewaddress1.setText(mPlanetList.get(position).getHousename());
        holder.textViewaddress2.setText(landmark+","+locality);
        holder.textViewaddress3.setText(city+","+pincode);

    }


    @Override
    public int getItemCount() {
        return mPlanetList.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder{

        TextView textViewname, textViewaddress1, textViewaddress2, textViewaddress3;
        ImageView imageViewdelete;
        public ListHolder(View itemView) {
            super(itemView);

            textViewname = (TextView) itemView.findViewById(R.id.tvaddressname);
            textViewaddress1 = (TextView) itemView.findViewById(R.id.tvaddress1);
            textViewaddress2 = (TextView) itemView.findViewById(R.id.tvaddress2);
            textViewaddress3 = (TextView) itemView.findViewById(R.id.tvaddress3);
            imageViewdelete = (ImageView) itemView.findViewById(R.id.imgvaddressdelete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mlistner != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mlistner.onItemClick(position);
                        }
                    }
                }
            });

            imageViewdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mlistner != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mlistner.icondeleteImageViewOnClick(v, getAdapterPosition());
                        }
                    }
                }
            });
        }
    }
}
