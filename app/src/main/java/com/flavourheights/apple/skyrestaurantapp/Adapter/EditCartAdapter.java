package com.flavourheights.apple.skyrestaurantapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flavourheights.apple.skyrestaurantapp.Model.CartListPlanet;
import com.flavourheights.apple.skyrestaurantapp.R;

import java.util.ArrayList;

public class EditCartAdapter extends RecyclerView.Adapter<EditCartAdapter.ListHolder> {

    public static ArrayList<CartListPlanet> mPlanetList;
    private OnItemClickListner mlistner;
    private ClickOnItemListener clickItemListerner;
    int total = 0;
   // private LayoutInflater inflater;

    int totalamount;

    public interface OnItemClickListner
    {
        void onItemClick(int position);

        void plusOnClick(View v, int position);

        void minusOnClick(View v, int position);
    }

    public interface ClickOnItemListener {
        void onOrderItemClick(int position);


    }

    public void setOnItemClick(ClickOnItemListener listner){
        this.clickItemListerner = listner;

//        this.fragment = itemAllFragment;
    }

    public void setOnItemClickListner(OnItemClickListner listner){
        this.mlistner = listner;

//        this.fragment = itemAllFragment;
    }

    public EditCartAdapter(ArrayList<CartListPlanet> mPlanetList1) {
        //inflater = LayoutInflater.from(ctx);
        this.mPlanetList = mPlanetList1;

    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.edit_cart_item,parent,false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListHolder holder, final int position) {


        holder.textViewsubitemname.setText(mPlanetList.get(position).getItemName());
        holder.textViewrate.setText(mPlanetList.get(position).getCost());
        holder.textViewtot.setText(mPlanetList.get(position).getTotalCost());
        holder.textViewtotcount.setText(mPlanetList.get(position).getTotalCount());


        holder.textViewplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count= Integer.parseInt(String.valueOf(holder.textViewtotcount.getText()));
                count++;

                holder.textViewtotcount.setText(String.valueOf(count));
                int totrate = Integer.parseInt(String.valueOf(holder.textViewrate.getText())) * Integer.parseInt(String.valueOf(holder.textViewtotcount.getText()));

                holder.textViewtot.setText(String.valueOf(totrate));

                //totalamount = Integer.parseInt(holder.textViewtot.getText().toString());

//                totalamount = 0;
//                for (int i = 0; i < mPlanetList.size(); i++ )
//                {
//
//                    holder.textViewtot.setText(mPlanetList.get(i).getTotalCount());
//
////                    int ammount = Integer.parseInt(String.valueOf(mPlanetList.get(i).getTotalCost()));
////                    totalamount = totalamount + ammount;
//
//                    //clickItemListerner.onOrderItemClick(totalamount);
//                }

               // totalamount = totalamount + Integer.parseInt(String.valueOf(holder.textViewrate.getText()));

                mPlanetList.get(holder.getAdapterPosition()).setTotalCost(holder.textViewtot.getText().toString());
            }
        });


        holder.textViewminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count= Integer.parseInt(String.valueOf(holder.textViewtotcount.getText()));
                count--;

                holder.textViewtotcount.setText(String.valueOf(count));
                int totrate = Integer.parseInt(String.valueOf(holder.textViewrate.getText())) * Integer.parseInt(String.valueOf(holder.textViewtotcount.getText()));

                holder.textViewtot.setText(String.valueOf(totrate));


            }
        });





    }

    @Override
    public int getItemCount() {
        return mPlanetList.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder {
        TextView textViewsubitemname,textViewrate,textViewtot,textViewplus,textViewminus,textViewtotcount;

        public ListHolder(View itemView) {
            super(itemView);
            // parentlayout = itemView.findViewById(R.id.list);
            textViewsubitemname = (TextView) itemView.findViewById(R.id.tveditcartitemname);
            textViewrate = (TextView) itemView.findViewById(R.id.tveditcartcost);
            textViewtot = (TextView) itemView.findViewById(R.id.tvedittotalcost);
            textViewplus = (TextView) itemView.findViewById(R.id.tvplus);
            textViewminus = (TextView) itemView.findViewById(R.id.tvminus);
            textViewtotcount = (TextView) itemView.findViewById(R.id.tveditnoofitem);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mlistner != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mlistner.onItemClick(position);
                        }
                    }
                }
            });

            textViewtot.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                     mPlanetList.get(getAdapterPosition()).setTotalCost(textViewtot.getText().toString());
                    total = 0;
                    for (int i=0; i<mPlanetList.size(); i++)
                    {
                        total = total + Integer.parseInt(mPlanetList.get(i).getTotalCost());


                    }

                clickItemListerner.onOrderItemClick(total);


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

//
//            textViewplus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mlistner != null){
//                        int position = getAdapterPosition();
//                        if(position != RecyclerView.NO_POSITION){
//                            mlistner.plusOnClick(v, getAdapterPosition());
//                        }
//                    }
//                }
//            });
//
//            textViewminus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mlistner != null){
//                        int position = getAdapterPosition();
//                        if(position != RecyclerView.NO_POSITION){
//                            mlistner.minusOnClick(v, getAdapterPosition());
//                        }
//                    }
//                }
//            });
        }
    }


}