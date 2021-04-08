package com.example.tfgv20.Fragments.Especifico;

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

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.graphql.StaffQuery;
import com.example.tfgv20.Adapters.RelationAdapter;
import com.example.tfgv20.POJO.ObjetoRelation;
import com.example.tfgv20.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

public class StaffFragment extends Fragment {

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
        vista = inflater.inflate(R.layout.fragment_staff, container, false);

        Bundle bundle = getArguments();

        if (bundle != null){
            id = bundle.getInt("id");
        }

        list = new ArrayList<>();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        apolloClient = ApolloClient.builder().serverUrl(BASE_URL).okHttpClient(okHttpClient).build();
        apolloClient.query(StaffQuery.builder().id(id).build()).enqueue(new ApolloCall.Callback<StaffQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<StaffQuery.Data> response) {

                for (int i = 0; i < response.getData().Page().media().get(0).staff().edges().size(); i++){
                    String imagen = response.getData().Page().media().get(0).staff().edges().get(i).node().image().large();
                    String name = response.getData().Page().media().get(0).staff().edges().get(i).node().name().full();
                    String role = response.getData().Page().media().get(0).staff().edges().get(i).role();
                    ObjetoRelation objeto = new ObjetoRelation(imagen, name, role);
                    list.add(objeto);
                }

                StaffFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView = vista.findViewById(R.id.recyclerStaffFragment);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                        adapter = new RelationAdapter(StaffFragment.this.getContext(), list, resource);
                        layoutManager = new GridLayoutManager(StaffFragment.this.getContext(), 2);

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
