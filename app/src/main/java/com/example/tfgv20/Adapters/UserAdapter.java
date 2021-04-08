package com.example.tfgv20.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfgv20.POJO.ObjetoMiniatura;
import com.example.tfgv20.POJO.ObjetoUser;
import com.example.tfgv20.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.CardVH> {

    private Context context;
    private ArrayList<ObjetoUser> lista;
    private int resource;

    public UserAdapter(Context context, ArrayList<ObjetoUser> lista, int resource) {
        this.context = context;
        this.lista = lista;
        this.resource = resource;
    }

    @NonNull
    @Override
    public UserAdapter.CardVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(context).inflate(resource, null);
        UserAdapter.CardVH cardVH = new UserAdapter.CardVH(card, mListener);
        return cardVH;
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.CardVH holder, int position) {
        ObjetoUser objeto = lista.get(position);
        int id = lista.get(position).getId();
        objeto.setId(id);
        Picasso.get().load(objeto.getImagen()).into(holder.imagenCard);
        holder.textCard.setText(objeto.getNombre());
    }

    public interface  OnItemClickListener{
        void onItemClick(int position);
    }

    private UserAdapter.OnItemClickListener mListener;

    public void setOnItemClickListener(UserAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class CardVH extends RecyclerView.ViewHolder{
        private ImageView imagenCard;
        private TextView textCard;

        public CardVH(@NonNull View itemView, UserAdapter.OnItemClickListener listener) {
            super(itemView);
            imagenCard = itemView.findViewById(R.id.imgCardUser);
            textCard = itemView.findViewById(R.id.txtCardUser);

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
