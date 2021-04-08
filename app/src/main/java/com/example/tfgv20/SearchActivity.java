package com.example.tfgv20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.graphql.SearchQuery;
import com.example.tfgv20.Adapters.MiniaturaAdapter;
import com.example.tfgv20.Adapters.UserAdapter;
import com.example.tfgv20.Fragments.Main.FauvoriteFragment;
import com.example.tfgv20.Fragments.Main.HomeFragment;
import com.example.tfgv20.POJO.ObjetoMiniatura;
import com.example.tfgv20.POJO.ObjetoUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

public class SearchActivity extends AppCompatActivity {

    private EditText edtSearch;

    private ApolloClient apolloClient;
    private static  final String BASE_URL = "https://graphql.anilist.co";

    private int resource = R.layout.card_user;

    private UserAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;

    private ArrayList<ObjetoUser> list;

    String busqueda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        edtSearch = findViewById(R.id.edtSearch);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                llenar();
                busqueda = edtSearch.getText().toString();
                Log.d("texto", busqueda);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                llenar();
                busqueda = edtSearch.getText().toString();
                Log.d("texto", busqueda );
            }

            @Override
            public void afterTextChanged(Editable s) {
                llenar();
                busqueda = edtSearch.getText().toString();
                Log.d("texto", busqueda );
            }
        });





        //OkHttp
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        //Apollo
        apolloClient = ApolloClient.builder().serverUrl(BASE_URL).okHttpClient(okHttpClient).build();



    }

    public void llenar(){
        apolloClient.query(SearchQuery.builder().search(busqueda).build()).enqueue(new ApolloCall.Callback<SearchQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<SearchQuery.Data> response) {

                list = new ArrayList<>();
                int num = response.getData().Page().media().size();

                for (int i = 0; i < num; i++){
                    String imagen = response.getData().Page().media().get(i).coverImage().extraLarge();
                    int id = response.getData().Page().media().get(i).id();
                    String name = response.getData().Page().media().get(i).title().romaji();
                    ObjetoUser objeto = new ObjetoUser(imagen, id, name);
                    list.add(objeto);
                }

                SearchActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView = findViewById(R.id.recyclerSearch);
                        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                        adapter = new UserAdapter(SearchActivity.this , list, resource);
                        layoutManager = new GridLayoutManager(SearchActivity.this, 1);

                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);

                        adapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                Intent intent = new Intent(SearchActivity.this, VerEspecificoActicity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("id", position);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    }
                });





            }

            @Override
            public void onFailure(@NotNull ApolloException e) {

            }
        });
    }
}