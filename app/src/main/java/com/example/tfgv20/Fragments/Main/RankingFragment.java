package com.example.tfgv20.Fragments.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;

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
import com.example.graphql.ScoreListQuery;
import com.example.tfgv20.Adapters.MiniaturaAdapter;
import com.example.tfgv20.POJO.ObjetoMiniatura;
import com.example.tfgv20.R;
import com.example.tfgv20.VerEspecificoActicity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

public class RankingFragment extends Fragment {

    View vista;
    ApolloClient apolloClient;
    private static  final String BASE_URL = "https://graphql.anilist.co";

    private ArrayList<ObjetoMiniatura> lista;

    private int resource = R.layout.card_miniatura;
    private MiniaturaAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private int lastPage;


    private boolean isScrolling;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_ranking, container, false);


        lista = new ArrayList<>();
        recyclerView = vista.findViewById(R.id.recyclerRanking);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        apolloClient = ApolloClient.builder().serverUrl(BASE_URL).okHttpClient(okHttpClient).build();
        apolloClient.query(ScoreListQuery.builder().page(1).perPage(50).averageg(1).build()).enqueue(new ApolloCall.Callback<ScoreListQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<ScoreListQuery.Data> response) {
                lastPage = response.getData().Page().pageInfo().lastPage();

                for (int i = 0; i < 3; i++){
                    apolloClient.query(ScoreListQuery.builder().page(lastPage).perPage(50).averageg(1).build()).enqueue(new ApolloCall.Callback<ScoreListQuery.Data>() {
                        @Override
                        public void onResponse(@NotNull Response<ScoreListQuery.Data> response) {


                            int x = (response.getData().Page().media().size());
                            for (int i = (x - 1); i > 0; i--){
                                String imagen = response.getData().Page().media().get(i).coverImage().extraLarge();
                                int id = response.getData().Page().media().get(i).id();
                                ObjetoMiniatura objetoMiniatura = new ObjetoMiniatura(imagen, id);
                                lista.add(objetoMiniatura);
                            }

                            RankingFragment.this.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    llenarRecycler();

                                }
                            });
                        }

                        @Override
                        public void onFailure(@NotNull ApolloException e) {

                        }
                    });

                    lastPage -=1;
                }


                }




            @Override
            public void onFailure(@NotNull ApolloException e) {

            }
        });

        return vista;
    }

    private void pagination(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isScrolling){
                    isScrolling = false;
                    lastPage--;
                }
            }
        });
    }

    private void llenarRecycler(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MiniaturaAdapter(RankingFragment.this.getContext(), lista, resource);
        layoutManager = new GridLayoutManager(RankingFragment.this.getContext(), 3);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MiniaturaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(RankingFragment.this.getContext(), VerEspecificoActicity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
