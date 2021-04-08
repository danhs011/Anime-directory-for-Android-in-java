package com.example.tfgv20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.graphql.GetEspecificoQuery;
import com.example.tfgv20.Fragments.Especifico.CharactersFragment;
import com.example.tfgv20.Fragments.Especifico.RelationsFragment;
import com.example.tfgv20.Fragments.Especifico.StaffFragment;
import com.example.tfgv20.POJO.ObjetoMiniatura;
import com.example.tfgv20.POJO.ObjetoUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.OkHttpClient;

public class VerEspecificoActicity extends AppCompatActivity {


    private TextView txtNombre, txtDescripcion;
    ImageView imgBanner, imgCover;
    FloatingActionButton btnAdd, btnLike;

    private ApolloClient apolloClient;
    private static  final String BASE_URL = "https://graphql.anilist.co";
    int id;
    private String imagen;
    private String nombre;
    private boolean estaFav;
    private boolean estaList;

    private ArrayList<ObjetoUser> list;
    private ArrayList<String> indices;
    private ArrayList<ObjetoUser> listAdd;
    private ArrayList<String> indicesAdd;


    private FirebaseDatabase database;
    private DatabaseReference favRef;
    private  DatabaseReference listRef;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private Map<String, ObjetoUser> mapFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_especifico_acticity);

        txtNombre = findViewById(R.id.nombreTxtVerEspecifico);
        txtDescripcion = findViewById(R.id.descripcionTxtVerEspecifico);
        imgCover = findViewById(R.id.coverImgVerEspecifico);
        imgBanner = findViewById(R.id.bannerImgVerEspecifico);
        btnAdd = findViewById(R.id.btnAddVerEspecifico);
        btnLike = findViewById(R.id.btnLikeVerEspecifico);

        list = new ArrayList<>();
        indices = new ArrayList<>();
        listAdd = new ArrayList<>();
        indicesAdd = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();


        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            id = bundle.getInt("id");
        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        //Apollo
        apolloClient = ApolloClient.builder().serverUrl(BASE_URL).okHttpClient(okHttpClient).build();

        apolloClient.query(GetEspecificoQuery.builder().id(id).build()).enqueue(new ApolloCall.Callback<GetEspecificoQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetEspecificoQuery.Data> response) {

                VerEspecificoActicity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtNombre.setText(response.getData().Page().media().get(0).title().romaji());
                        txtDescripcion.setText(response.getData().Page().media().get(0).description());

                        Picasso.get().load(response.getData().Page().media().get(0).coverImage().extraLarge()).into(imgCover);

                        if (response.getData().Page().media().get(0).bannerImage() == null){
                            imgBanner.setImageResource(R.drawable.logo_anilist);
                        }else {
                            Picasso.get().load(response.getData().Page().media().get(0).bannerImage()).into(imgBanner);
                        }


                        imagen = response.getData().Page().media().get(0).coverImage().extraLarge();
                        nombre = response.getData().Page().media().get(0).title().romaji();
                    }
                });
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {

            }
        });


        //Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String userID = mAuth.getCurrentUser().getUid();

        favRef = mDatabase.child("users").child(userID).child("favoritos");
        listRef = mDatabase.child("users").child(userID).child("lista");

        favRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String, ObjetoUser>> genericTypeIndicator =
                        new GenericTypeIndicator<Map<String, ObjetoUser>>() {};
                mapFirebase = dataSnapshot.getValue(genericTypeIndicator);

                list.clear();
                indices.clear();
                if (mapFirebase != null){
                    list.addAll(mapFirebase.values());
                    indices.addAll(mapFirebase.keySet());
                }


                Log.d("Lista", list.toString());
                Log.d("list id", ""+id);

                estaFav = false;

                for (int i = 0; i < list.size(); i++){
                    if (list.get(i).getId() == id){
                        estaFav = true;
                    }
                }

                if (estaFav){
                    btnLike.setImageResource(R.drawable.ic_favorite_black_24dp);
                }else {
                    btnLike.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                }

                btnLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Like();
                    }
                });

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
                mapFirebase = dataSnapshot.getValue(genericTypeIndicator);

                listAdd.clear();
                indicesAdd.clear();
                if (mapFirebase != null){
                    listAdd.addAll(mapFirebase.values());
                    indicesAdd.addAll(mapFirebase.keySet());
                }


                Log.d("Lista", listAdd.toString());
                Log.d("list id", ""+id);

                estaList = false;

                for (int i = 0; i < listAdd.size(); i++){
                    if (listAdd.get(i).getId() == id){
                        estaList = true;
                    }
                }

                if (estaList){
                    btnAdd.setImageResource(R.drawable.ic_check);
                }else {
                    btnAdd.setImageResource(R.drawable.ic_add);
                }

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        BottomNavigationView botomNav = findViewById(R.id.menuVerEspecifico);
        botomNav.setOnNavigationItemSelectedListener(navListener);

        //getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutVer, new RelationsFragment()).commit();



    }

    public void Like(){
        if (estaFav){
            for (int i = 0; i < list.size(); i++){
                if (list.get(i).getId() == id){
                    favRef.child(indices.get(i)).removeValue();

                }
            }
        }

        if (!estaFav){
            ObjetoUser objetoUser = new ObjetoUser(imagen, id, nombre);
            favRef.push().setValue(objetoUser);

        }

    }

    public void list(){
        if (estaList){
            for (int i = 0; i < listAdd.size(); i++){
                if (listAdd.get(i).getId() == id){
                    listRef.child(indicesAdd.get(i)).removeValue();

                }
            }
        }

        if (!estaList){
            ObjetoUser objetoUser = new ObjetoUser(imagen, id, nombre);
            listRef.push().setValue(objetoUser);

        }
    }

    BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.relationMenuVer:
                    RelationsFragment relationsFragment = new RelationsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", id);
                    relationsFragment.setArguments(bundle);
                    selectedFragment = relationsFragment;
                    break;
                case R.id.characterMenuVer:
                    CharactersFragment charactersFragment = new CharactersFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt("id", id);
                    charactersFragment.setArguments(bundle1);
                    selectedFragment = charactersFragment;
                    break;
                case R.id.staffMenuVer:
                    StaffFragment staffFragment = new StaffFragment();
                    Bundle bundle2 = new Bundle();
                    bundle2.putInt("id", id);
                    staffFragment.setArguments(bundle2);
                    selectedFragment = staffFragment;
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutVer, selectedFragment).commit();
            return false;
        }
    };
}
