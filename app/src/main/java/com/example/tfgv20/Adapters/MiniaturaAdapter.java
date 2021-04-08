package com.example.tfgv20.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfgv20.POJO.ObjetoMiniatura;
import com.example.tfgv20.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MiniaturaAdapter extends RecyclerView.Adapter<MiniaturaAdapter.CardVH> {

    private Context context;
    private ArrayList<ObjetoMiniatura> lista;
    private int resource;

    public MiniaturaAdapter(Context context, ArrayList<ObjetoMiniatura> lista, int resource) {
        this.context = context;
        this.lista = lista;
        this.resource = resource;
    }

    @NonNull
    @Override
    public CardVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(context).inflate(resource, null);
        CardVH cardVH = new CardVH(card, mListener);
        return cardVH;
    }

    @Override
    public void onBindViewHolder(@NonNull CardVH holder, int position) {
        ObjetoMiniatura objeto = lista.get(position);
        int id = lista.get(position).getId();
        objeto.setId(id);
        Picasso.get().load(objeto.getImagen()).into(holder.imagenCard);
    }

    public interface  OnItemClickListener{
        void onItemClick(int position);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class CardVH extends RecyclerView.ViewHolder{
        private ImageView imagenCard;

        public CardVH(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            imagenCard = itemView.findViewById(R.id.imagenCardMiniatura);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        int id = lista.get(position).getId();
                        listener.onItemClick(id);
                    }
                }
            });
        }
    }
}
