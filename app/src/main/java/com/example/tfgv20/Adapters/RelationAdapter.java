package com.example.tfgv20.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfgv20.POJO.ObjetoRelation;
import com.example.tfgv20.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RelationAdapter  extends RecyclerView.Adapter<RelationAdapter.CardVH> {

    private Context context;
    private ArrayList<ObjetoRelation> list;
    private int resource;


    public RelationAdapter(Context context, ArrayList<ObjetoRelation> list, int resource) {
        this.context = context;
        this.list = list;
        this.resource = resource;
    }

    @NonNull
    @Override
    public CardVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(context).inflate(resource, null);
        CardVH cardVH = new CardVH(card);
        return cardVH;
    }

    @Override
    public void onBindViewHolder(@NonNull CardVH holder, int position) {
        ObjetoRelation objeto = list.get(position);
        holder.txt1Card.setText(objeto.getTexto1());
        holder.txt2Card.setText(objeto.getTesto2());
        Picasso.get().load(objeto.getImagen()).into(holder.imagenCard );

    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class  CardVH extends RecyclerView.ViewHolder {
        private ImageView imagenCard;
        private TextView txt1Card, txt2Card;

        public CardVH(@NonNull View itemView) {
            super(itemView);
            imagenCard = itemView.findViewById(R.id.imgCardVerEspecifico);
            txt1Card = itemView.findViewById(R.id.txt1CardVerEspecifico);
            txt2Card = itemView.findViewById(R.id.txt2CardVerEspecifico);


        }
    }
}
