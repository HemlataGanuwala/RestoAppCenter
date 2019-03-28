package com.flavourheight.apple.skyrestaurantapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flavourheight.apple.skyrestaurantapp.Model.OrderDetailsPlanet;
import com.flavourheight.apple.skyrestaurantapp.R;

import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ListHolder>{


    private List<OrderDetailsPlanet> mPlanetList;


    public OrderDetailsAdapter(List<OrderDetailsPlanet> mPlanetList1) {
        this.mPlanetList = mPlanetList1;
    }


    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.order_details,parent,false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, int position) {
        holder.textViewsubitemName.setText(mPlanetList.get(position).getSubitemNmae());
        holder.textViewitemrate.setText(mPlanetList.get(position).getItemeRate());
        holder.textViewtotalcount.setText(mPlanetList.get(position).getTotalCount());
        holder.textViewtotalamt.setText(mPlanetList.get(position).getTotalAmount());
    }

    @Override
    public int getItemCount() {
        return mPlanetList.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder{

        TextView textViewsubitemName, textViewitemrate, textViewtotalcount, textViewtotalamt;
        public ListHolder(View itemView) {
            super(itemView);

            textViewsubitemName = (TextView) itemView.findViewById(R.id.tvorderdetailssubitemname);
            textViewitemrate = (TextView) itemView.findViewById(R.id.tvitemrate);
            textViewtotalcount = (TextView) itemView.findViewById(R.id.tvtotalamount);
            textViewtotalamt = (TextView) itemView.findViewById(R.id.tvquantity);

        }
    }
}
