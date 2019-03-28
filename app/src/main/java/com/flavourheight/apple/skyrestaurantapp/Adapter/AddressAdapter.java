package com.flavourheight.apple.skyrestaurantapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flavourheight.apple.skyrestaurantapp.Model.AddressPlanet;
import com.flavourheight.apple.skyrestaurantapp.R;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ListHolder> {

    private List<AddressPlanet> mPlanetList;
    private OnItemClickListner mlistner;
    private OnAddressImageViewClickListner onAddressImageViewClickListner;
    int selectedPosition = -1;


    public interface OnItemClickListner
    {
        void onItemClick(int position);

    }
    public interface OnAddressImageViewClickListner
    {
        void onAddressItemClick(String position);

    }

    public void setOnItemClickListner(OnItemClickListner listner){
        this.mlistner = listner;
//        this.fragment = itemAllFragment;
    }

    public void setOnAddressImageViewClickListner(OnAddressImageViewClickListner listner){
        this.onAddressImageViewClickListner = listner;
//        this.fragment = itemAllFragment;
    }

    public AddressAdapter(List<AddressPlanet> mPlanetList1) {
        this.mPlanetList = mPlanetList1;
    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.addresslist_item,parent,false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListHolder holder, final int position) {

        String name = mPlanetList.get(position).getCustName();
        String addresstype = mPlanetList.get(position).getAddressType();
        String landmark = mPlanetList.get(position).getLandmark();
        String locality = mPlanetList.get(position).getLocality();
        String city = mPlanetList.get(position).getCity();
        String pincode = mPlanetList.get(position).getPincode();
        String state = mPlanetList.get(position).getState();

        holder.textViewcustname.setText(name+ " " +addresstype);
//        holder.textViewaddresstype.setText(mPlanetList.get(position).getAddressType());
        holder.textViewhousenm.setText(mPlanetList.get(position).getHousename());
        holder.textViewlandmark.setText(landmark+" "+locality);
        holder.textViewstate.setText(state);
//        holder.textViewlocality.setText(mPlanetList.get(position).getLocality());
        holder.textViewcity.setText(city+" "+pincode);
//        holder.textViewpincode.setText(mPlanetList.get(position).getPincode());

        if (selectedPosition == position)
        {
            holder.imageViewcheck.setVisibility(View.VISIBLE);
//            int address = 1;
           String address = holder.textViewhousenm.getText()+" "+ holder.textViewlandmark.getText()+" "+holder.textViewstate.getText()+" "+holder.textViewcity.getText();
            onAddressImageViewClickListner.onAddressItemClick(address);
        }
        else
        {
            holder.imageViewcheck.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            selectedPosition = position;
            notifyDataSetChanged();
//                if (holder.imageViewcheck.isEnabled())
//                {
//                    holder.imageViewcheck.setVisibility(View.GONE);
//                }
//                else
//                {
//                    holder.imageViewcheck.setVisibility(View.VISIBLE);
//                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mPlanetList.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder {
        TextView textViewhousenm,textViewlandmark,textViewlocality,textViewcity,textViewstate, textViewaddresstype, textViewcustname;
        ImageView imageViewcheck;
        //LinearLayout parentlayout;
        public ListHolder(View itemView) {
            super(itemView);
            // parentlayout = itemView.findViewById(R.id.list);

            textViewcustname = (TextView) itemView.findViewById(R.id.tvcustname);
            textViewhousenm = (TextView) itemView.findViewById(R.id.tvaddhouse);
            textViewlandmark = (TextView) itemView.findViewById(R.id.tvaddlandmark);
            textViewcity = (TextView) itemView.findViewById(R.id.tvaddcity);
            textViewstate = (TextView) itemView.findViewById(R.id.tvaddressstate);
            imageViewcheck=(ImageView) itemView.findViewById(R.id.imgaddcheck);

            imageViewcheck.setVisibility(View.GONE);

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




        }
    }


}
