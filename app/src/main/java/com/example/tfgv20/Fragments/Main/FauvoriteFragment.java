package com.example.tfgv20.Fragments.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfgv20.Adapters.MiniaturaAdapter;
import com.example.tfgv20.POJO.ObjetoMiniatura;
import com.example.tfgv20.R;
import com.example.tfgv20.VerEspecificoActicity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class FauvoriteFragment extends Fragment {

    View vista;
    //Fila
    private int resource = R.layout.card_miniatura;
    //conjunto de datos
    private ArrayList<ObjetoMiniatura> lista;
    private ArrayList<String> indices;
    //Adapter
    private MiniaturaAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;


    private FirebaseDatabase database;
    private DatabaseReference FavFref;
    //TablaHas
    private Map<String, ObjetoMiniatura> mapFirebase;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_fauvorite, container, false);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        String userID = mAuth.getCurrentUser().getUid();

        FavFref = mDatabase.child("users").child(userID).child("favoritos");



        lista = new ArrayList<>();
        recyclerView = vista.findViewById(R.id.RecyclerViewFaouvorite1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MiniaturaAdapter(FauvoriteFragment.this.getContext(), lista, resource);
        layoutManager = new GridLayoutManager(FauvoriteFragment.this.getContext(), 3);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MiniaturaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(FauvoriteFragment.this.getContext(), VerEspecificoActicity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });



        FavFref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                GenericTypeIndicator<Map<String, ObjetoMiniatura>> genericTypeIndicator =
                        new GenericTypeIndicator<Map<String, ObjetoMiniatura>>() {};
                mapFirebase = dataSnapshot.getValue(genericTypeIndicator);

                lista.clear();
                //indices.clear();
                if (mapFirebase != null){
                    lista.addAll(mapFirebase.values());
                    //indices.addAll(mapFirebase.keySet());
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return vista;
    }
}
