package com.example.tfgv20.Fragments.Especifico;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.graphql.RelationsQuery;
import com.example.tfgv20.Adapters.RelationAdapter;
import com.example.tfgv20.POJO.ObjetoRelation;
import com.example.tfgv20.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

public class RelationsFragment extends Fragment{

    View vista;
    int id;
    private ApolloClient apolloClient;
    private static  final String BASE_URL = "https://graphql.anilist.co";

    private ArrayList<ObjetoRelation> list;

    //Fila
    private int resource = R.layout.card_relation;
    //Adapter
    private RelationAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_relations, container, false);

        Bundle bundle = getArguments();

        if (bundle != null){
            id = bundle.getInt("id");
        }

        list = new ArrayList<>();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        apolloClient = ApolloClient.builder().serverUrl(BASE_URL).okHttpClient(okHttpClient).build();
        apolloClient.query(RelationsQuery.builder().id(id).build()).enqueue(new ApolloCall.Callback<RelationsQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<RelationsQuery.Data> response) {

                for (int i = 0; i < response.getData().Page().media().get(0).relations().nodes().size(); i++){
                    String imagen = response.getData().Page().media().get(0).relations().nodes().get(i).coverImage().extraLarge();
                    String title = response.getData().Page().media().get(0).relations().nodes().get(i).title().romaji();
                    String relation = response.getData().Page().media().get(0).relations().edges().get(i).relationType().toString();
                    ObjetoRelation objeto = new ObjetoRelation(imagen, title, relation);
                    list.add(objeto);
                }

                RelationsFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView = vista.findViewById(R.id.recyclerRelationsFragment);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                        adapter = new RelationAdapter(RelationsFragment.this.getContext(), list, resource);
                        layoutManager = new GridLayoutManager(RelationsFragment.this.getContext(), 2);

                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);
                    }
                });

            }

            @Override
            public void onFailure(@NotNull ApolloException e) {

            }
        });


        return vista;
    }
}
