package com.example.tfgv20.Fragments.Main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfgv20.Adapters.MiniaturaAdapter;
import com.example.tfgv20.Adapters.UserAdapter;
import com.example.tfgv20.POJO.ObjetoMiniatura;
import com.example.tfgv20.POJO.ObjetoUser;
import com.example.tfgv20.R;
import com.example.tfgv20.VerEspecificoActicity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class UserFragment extends Fragment {

    View vista;
    ImageView logOut;
    private FirebaseDatabase database;
    private DatabaseReference listRef;
    private DatabaseReference nameRef;

    private Map<String, ObjetoUser> mapFireBase;

    private ArrayList<ObjetoUser> list;
    private ArrayList<ObjetoUser> list2;

    private int resource = R.layout.card_user;
    private UserAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private TextView txtUsername;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_user, container, false);

        txtUsername = vista.findViewById(R.id.txtUsernameUser);

        mAuth = FirebaseAuth.getInstance();

        logOut = vista.findViewById(R.id.imgLogOutUser);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        String userID = mAuth.getCurrentUser().getUid();

        listRef = mDatabase.child("users").child(userID).child("favoritos");
        nameRef = mDatabase.child("users").child(userID).child("firstName");



        list = new ArrayList<>();
        list2 = new ArrayList<>();
        recyclerView = vista.findViewById(R.id.recyclerUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new UserAdapter(UserFragment.this.getContext(), list2, resource);
        layoutManager = new GridLayoutManager(UserFragment.this.getContext(), 1);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(UserFragment.this.getContext(), VerEspecificoActicity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        nameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                txtUsername.setText(value);
                Log.d("nombre", "Value is: " + value);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        listRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                GenericTypeIndicator<Map<String, ObjetoUser>> genericTypeIndicator =
                        new GenericTypeIndicator<Map<String, ObjetoUser>>() {};
                mapFireBase = dataSnapshot.getValue(genericTypeIndicator);

                list.clear();
                //indices.clear();
                if (mapFireBase != null){
                    list.addAll(mapFireBase.values());
                    //indices.addAll(mapFirebase.keySet());
                }

                int size = list.size();

                if (size > 4){
                    for (int i = 0; i < 4; i++){
                        list2.add(list.get(i));
                    }
                }else {
                    for (int i = 0; i < size; i++){
                        list2.add(list.get(i));
                    }
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
