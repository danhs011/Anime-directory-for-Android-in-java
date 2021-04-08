package com.example.tfgv20.Fragments.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.graphql.GetSeasonAnimeQuery;
import com.example.graphql.PorgeneroQuery;
import com.example.graphql.SearchQuery;
import com.example.graphql.type.MediaSeason;
import com.example.graphql.type.MediaType;
import com.example.tfgv20.Adapters.MiniaturaAdapter;
import com.example.tfgv20.POJO.ObjetoMiniatura;
import com.example.tfgv20.R;
import com.example.tfgv20.SearchActivity;
import com.example.tfgv20.VerEspecificoActicity;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

public class HomeFragment extends Fragment {

    View vista;

    private ImageView imagenAl1, imagenAl2, imgSearch;
    private Button btnAnime, btnManga;

    private ApolloClient apolloClient;
    private static  final String BASE_URL = "https://graphql.anilist.co";
    private static MediaType TypeMedia = MediaType.ANIME;

    private int resoure = R.layout.card_miniatura;

    private MiniaturaAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;

    private ArrayList<ObjetoMiniatura> listaAccion;
    private ArrayList<ObjetoMiniatura> listaAventura;
    private ArrayList<ObjetoMiniatura> listaComedia;
    private ArrayList<ObjetoMiniatura> listaDrama;
    private ArrayList<ObjetoMiniatura> listaMisterio;
    private ArrayList<ObjetoMiniatura> listaMechas;
    private ArrayList<ObjetoMiniatura> listaDeportes;
    private ArrayList<ObjetoMiniatura> listaRomance;
    private ArrayList<ObjetoMiniatura> listaSyfy;

    private TextView txtAccion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_home, container, false);

        imagenAl1 = vista.findViewById(R.id.aleatorio1ImgHome);
        imagenAl2 = vista.findViewById(R.id.aleatorio2ImgHome);
        btnAnime = vista.findViewById(R.id.btnAnimeHome);
        btnManga = vista.findViewById(R.id.btnMangaHome);
        imgSearch = vista.findViewById(R.id.imgSearchHome);

        txtAccion = vista.findViewById(R.id.txtAnimAccionHome);

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeFragment.this.getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        //OkHttp
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        //Apollo
        apolloClient = ApolloClient.builder().serverUrl(BASE_URL).okHttpClient(okHttpClient).build();


        llenarImagen1();
        llenarImagen2();

        llenarRecyclers();

        btnAnime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TypeMedia = MediaType.ANIME;
                llenarRecyclers();
                llenarImagen1();
                llenarImagen2();
            }
        });

        btnManga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TypeMedia = MediaType.MANGA;
                llenarRecyclers();
            }
        });

        return vista;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_buscar, menu);
    }


    public void llenarRecyclers(){

        //Animes de Accion
        listaAccion = new ArrayList<>();
        apolloClient.query(PorgeneroQuery.builder().page(1).perPage(30).genre("Action").type(TypeMedia).date(20000101).isAdult(false).build()).enqueue(new ApolloCall.Callback<PorgeneroQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<PorgeneroQuery.Data> response) {

                int numPaginas = response.getData().Page().pageInfo().lastPage();
                int PagAl = (int)(Math.random() * numPaginas) + 1;

                apolloClient.query(PorgeneroQuery.builder().page(PagAl).perPage(30).genre("Action").type(TypeMedia).date(20000101).isAdult(false).build()).enqueue(new ApolloCall.Callback<PorgeneroQuery.Data>() {
                    @Override

                    public void onResponse(@NotNull Response<PorgeneroQuery.Data> response) {

                        int x = response.getData().Page().media().size();

                        for (int i = 0; i < x; i++){
                            String imagen = response.getData().Page().media().get(i).coverImage().extraLarge();
                            int id = response.getData().Page().media().get(i).id();
                            ObjetoMiniatura objetoMiniatura = new ObjetoMiniatura(imagen, id);
                            listaAccion.add(objetoMiniatura);
                        }


                        HomeFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView = vista.findViewById(R.id.accionRecyclerHome);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                                adapter = new MiniaturaAdapter(HomeFragment.this.getContext(), listaAccion, resoure);
                                layoutManager = new LinearLayoutManager(HomeFragment.this.getContext(), RecyclerView.HORIZONTAL, false);

                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(adapter);

                                adapter.setOnItemClickListener(new MiniaturaAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        Intent intent = new Intent(HomeFragment.this.getContext(), VerEspecificoActicity.class);
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

            @Override
            public void onFailure(@NotNull ApolloException e) {

            }
        });


        //Animes de Aventura
        listaAventura = new ArrayList<>();
        apolloClient.query(PorgeneroQuery.builder().page(1).perPage(30).genre("Adventure").type(TypeMedia).date(20000101).isAdult(false).build()).enqueue(new ApolloCall.Callback<PorgeneroQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<PorgeneroQuery.Data> response) {

                int numPaginas = response.getData().Page().pageInfo().lastPage();
                int PagAl = (int)(Math.random() * numPaginas) + 1;

                apolloClient.query(PorgeneroQuery.builder().page(PagAl).perPage(30).genre("Adventure").type(TypeMedia).date(20000101).isAdult(false).build()).enqueue(new ApolloCall.Callback<PorgeneroQuery.Data>() {
                    @Override

                    public void onResponse(@NotNull Response<PorgeneroQuery.Data> response) {

                        int x = response.getData().Page().media().size();

                        for (int i = 0; i < x; i++){
                            String imagen = response.getData().Page().media().get(i).coverImage().extraLarge();
                            int id = response.getData().Page().media().get(i).id();
                            ObjetoMiniatura objetoMiniatura = new ObjetoMiniatura(imagen, id);
                            listaAventura.add(objetoMiniatura);
                        }


                        HomeFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView = vista.findViewById(R.id.aventuraRecyclerHome);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                                adapter = new MiniaturaAdapter(HomeFragment.this.getContext(), listaAventura, resoure);
                                layoutManager = new LinearLayoutManager(HomeFragment.this.getContext(), RecyclerView.HORIZONTAL, false);

                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(adapter);

                                adapter.setOnItemClickListener(new MiniaturaAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        Intent intent = new Intent(HomeFragment.this.getContext(), VerEspecificoActicity.class);
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

            @Override
            public void onFailure(@NotNull ApolloException e) {

            }
        });


        //Animes de Comedia
        listaComedia = new ArrayList<>();
        apolloClient.query(PorgeneroQuery.builder().page(1).perPage(30).genre("Comedy").type(TypeMedia).date(20000101).isAdult(false).build()).enqueue(new ApolloCall.Callback<PorgeneroQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<PorgeneroQuery.Data> response) {

                int numPaginas = response.getData().Page().pageInfo().lastPage();
                int PagAl = (int)(Math.random() * numPaginas) + 1;

                apolloClient.query(PorgeneroQuery.builder().page(PagAl).perPage(30).genre("Comedy").type(TypeMedia).date(20000101).isAdult(false).build()).enqueue(new ApolloCall.Callback<PorgeneroQuery.Data>() {
                    @Override

                    public void onResponse(@NotNull Response<PorgeneroQuery.Data> response) {

                        int x = response.getData().Page().media().size();

                        for (int i = 0; i < x; i++){
                            String imagen = response.getData().Page().media().get(i).coverImage().extraLarge();
                            int id = response.getData().Page().media().get(i).id();
                            ObjetoMiniatura objetoMiniatura = new ObjetoMiniatura(imagen, id);
                            listaComedia.add(objetoMiniatura);
                        }


                        HomeFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView = vista.findViewById(R.id.comediaRecyclerHome);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                                adapter = new MiniaturaAdapter(HomeFragment.this.getContext(), listaComedia, resoure);
                                layoutManager = new LinearLayoutManager(HomeFragment.this.getContext(), RecyclerView.HORIZONTAL, false);

                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(adapter);

                                adapter.setOnItemClickListener(new MiniaturaAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        Intent intent = new Intent(HomeFragment.this.getContext(), VerEspecificoActicity.class);
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

            @Override
            public void onFailure(@NotNull ApolloException e) {

            }
        });


        //Animes de Drama
        listaDrama = new ArrayList<>();
        apolloClient.query(PorgeneroQuery.builder().page(1).perPage(30).genre("Drama").type(TypeMedia).date(20000101).isAdult(false).build()).enqueue(new ApolloCall.Callback<PorgeneroQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<PorgeneroQuery.Data> response) {

                int numPaginas = response.getData().Page().pageInfo().lastPage();
                int PagAl = (int)(Math.random() * numPaginas) + 1;

                apolloClient.query(PorgeneroQuery.builder().page(PagAl).perPage(30).genre("Drama").type(TypeMedia).date(20000101).isAdult(false).build()).enqueue(new ApolloCall.Callback<PorgeneroQuery.Data>() {
                    @Override

                    public void onResponse(@NotNull Response<PorgeneroQuery.Data> response) {

                        int x = response.getData().Page().media().size();

                        for (int i = 0; i < x; i++){
                            String imagen = response.getData().Page().media().get(i).coverImage().extraLarge();
                            int id = response.getData().Page().media().get(i).id();
                            ObjetoMiniatura objetoMiniatura = new ObjetoMiniatura(imagen, id);
                            listaDrama.add(objetoMiniatura);
                        }


                        HomeFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView = vista.findViewById(R.id.dramaRecyclerHome);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                                adapter = new MiniaturaAdapter(HomeFragment.this.getContext(), listaDrama, resoure);
                                layoutManager = new LinearLayoutManager(HomeFragment.this.getContext(), RecyclerView.HORIZONTAL, false);

                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(adapter);

                                adapter.setOnItemClickListener(new MiniaturaAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        Intent intent = new Intent(HomeFragment.this.getContext(), VerEspecificoActicity.class);
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

            @Override
            public void onFailure(@NotNull ApolloException e) {

            }
        });


        //Animes de Misterio
        listaMisterio = new ArrayList<>();
        apolloClient.query(PorgeneroQuery.builder().page(1).perPage(30).genre("Mystery").type(TypeMedia).date(20000101).isAdult(false).build()).enqueue(new ApolloCall.Callback<PorgeneroQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<PorgeneroQuery.Data> response) {

                int numPaginas = response.getData().Page().pageInfo().lastPage();
                int PagAl = (int)(Math.random() * numPaginas) + 1;

                apolloClient.query(PorgeneroQuery.builder().page(PagAl).perPage(30).genre("Mystery").type(TypeMedia).date(20000101).isAdult(false).build()).enqueue(new ApolloCall.Callback<PorgeneroQuery.Data>() {
                    @Override

                    public void onResponse(@NotNull Response<PorgeneroQuery.Data> response) {

                        int x = response.getData().Page().media().size();

                        for (int i = 0; i < x; i++){
                            String imagen = response.getData().Page().media().get(i).coverImage().extraLarge();
                            int id = response.getData().Page().media().get(i).id();
                            ObjetoMiniatura objetoMiniatura = new ObjetoMiniatura(imagen, id);
                            listaMisterio.add(objetoMiniatura);
                        }


                        HomeFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView = vista.findViewById(R.id.misterioRecyclerHome);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                                adapter = new MiniaturaAdapter(HomeFragment.this.getContext(), listaMisterio, resoure);
                                layoutManager = new LinearLayoutManager(HomeFragment.this.getContext(), RecyclerView.HORIZONTAL, false);

                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(adapter);

                                adapter.setOnItemClickListener(new MiniaturaAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        Intent intent = new Intent(HomeFragment.this.getContext(), VerEspecificoActicity.class);
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

            @Override
            public void onFailure(@NotNull ApolloException e) {

            }
        });


        //Animes de Mechas
        listaMechas = new ArrayList<>();
        apolloClient.query(PorgeneroQuery.builder().page(1).perPage(30).genre("Mecha").type(TypeMedia).date(20000101).isAdult(false).build()).enqueue(new ApolloCall.Callback<PorgeneroQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<PorgeneroQuery.Data> response) {

                int numPaginas = response.getData().Page().pageInfo().lastPage();
                int PagAl = (int)(Math.random() * numPaginas) + 1;

                apolloClient.query(PorgeneroQuery.builder().page(PagAl).perPage(30).genre("Mecha").type(TypeMedia).date(20000101).isAdult(false).build()).enqueue(new ApolloCall.Callback<PorgeneroQuery.Data>() {
                    @Override

                    public void onResponse(@NotNull Response<PorgeneroQuery.Data> response) {

                        int x = response.getData().Page().media().size();

                        for (int i = 0; i < x; i++){
                            String imagen = response.getData().Page().media().get(i).coverImage().extraLarge();
                            int id = response.getData().Page().media().get(i).id();
                            ObjetoMiniatura objetoMiniatura = new ObjetoMiniatura(imagen, id);
                            listaMechas.add(objetoMiniatura);
                        }


                        HomeFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView = vista.findViewById(R.id.mechasRecyclerHome);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                                adapter = new MiniaturaAdapter(HomeFragment.this.getContext(), listaMechas, resoure);
                                layoutManager = new LinearLayoutManager(HomeFragment.this.getContext(), RecyclerView.HORIZONTAL, false);

                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(adapter);

                                adapter.setOnItemClickListener(new MiniaturaAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        Intent intent = new Intent(HomeFragment.this.getContext(), VerEspecificoActicity.class);
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

            @Override
            public void onFailure(@NotNull ApolloException e) {

            }
        });


        //Animes de Deportes
        listaDeportes = new ArrayList<>();
        apolloClient.query(PorgeneroQuery.builder().page(1).perPage(30).genre("Sports").type(TypeMedia).date(20000101).isAdult(false).build()).enqueue(new ApolloCall.Callback<PorgeneroQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<PorgeneroQuery.Data> response) {

                int numPaginas = response.getData().Page().pageInfo().lastPage();
                int PagAl = (int)(Math.random() * numPaginas) + 1;

                apolloClient.query(PorgeneroQuery.builder().page(PagAl).perPage(30).genre("Sports").type(TypeMedia).date(20000101).isAdult(false).build()).enqueue(new ApolloCall.Callback<PorgeneroQuery.Data>() {
                    @Override

                    public void onResponse(@NotNull Response<PorgeneroQuery.Data> response) {

                        int x = response.getData().Page().media().size();

                        for (int i = 0; i < x; i++){
                            String imagen = response.getData().Page().media().get(i).coverImage().extraLarge();
                            int id = response.getData().Page().media().get(i).id();
                            ObjetoMiniatura objetoMiniatura = new ObjetoMiniatura(imagen, id);
                            listaDeportes.add(objetoMiniatura);
                        }


                        HomeFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView = vista.findViewById(R.id.deportesRecyclerHome);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                                adapter = new MiniaturaAdapter(HomeFragment.this.getContext(), listaDeportes, resoure);
                                layoutManager = new LinearLayoutManager(HomeFragment.this.getContext(), RecyclerView.HORIZONTAL, false);

                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(adapter);

                                adapter.setOnItemClickListener(new MiniaturaAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        Intent intent = new Intent(HomeFragment.this.getContext(), VerEspecificoActicity.class);
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

            @Override
            public void onFailure(@NotNull ApolloException e) {

            }
        });


        //Animes de Romance
        listaRomance = new ArrayList<>();
        apolloClient.query(PorgeneroQuery.builder().page(1).perPage(30).genre("Romance").type(TypeMedia).date(20000101).isAdult(false).build()).enqueue(new ApolloCall.Callback<PorgeneroQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<PorgeneroQuery.Data> response) {

                int numPaginas = response.getData().Page().pageInfo().lastPage();
                int PagAl = (int)(Math.random() * numPaginas) + 1;

                apolloClient.query(PorgeneroQuery.builder().page(PagAl).perPage(30).genre("Romance").type(TypeMedia).date(20000101).isAdult(false).build()).enqueue(new ApolloCall.Callback<PorgeneroQuery.Data>() {
                    @Override

                    public void onResponse(@NotNull Response<PorgeneroQuery.Data> response) {

                        int x = response.getData().Page().media().size();

                        for (int i = 0; i < x; i++){
                            String imagen = response.getData().Page().media().get(i).coverImage().extraLarge();
                            int id = response.getData().Page().media().get(i).id();
                            ObjetoMiniatura objetoMiniatura = new ObjetoMiniatura(imagen, id);
                            listaRomance.add(objetoMiniatura);
                        }


                        HomeFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView = vista.findViewById(R.id.romanceRecyclerHome);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                                adapter = new MiniaturaAdapter(HomeFragment.this.getContext(), listaRomance, resoure);
                                layoutManager = new LinearLayoutManager(HomeFragment.this.getContext(), RecyclerView.HORIZONTAL, false);

                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(adapter);

                                adapter.setOnItemClickListener(new MiniaturaAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        Intent intent = new Intent(HomeFragment.this.getContext(), VerEspecificoActicity.class);
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

            @Override
            public void onFailure(@NotNull ApolloException e) {

            }
        });


        //Animes de Ciencia Ficcion
        listaSyfy = new ArrayList<>();
        apolloClient.query(PorgeneroQuery.builder().page(1).perPage(30).genre("Sci-Fi").type(TypeMedia).date(20000101).isAdult(false).build()).enqueue(new ApolloCall.Callback<PorgeneroQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<PorgeneroQuery.Data> response) {

                int numPaginas = response.getData().Page().pageInfo().lastPage();
                int PagAl = (int)(Math.random() * numPaginas) + 1;

                apolloClient.query(PorgeneroQuery.builder().page(PagAl).perPage(30).genre("Sci-Fi").type(TypeMedia).date(20000101).isAdult(false).build()).enqueue(new ApolloCall.Callback<PorgeneroQuery.Data>() {
                    @Override

                    public void onResponse(@NotNull Response<PorgeneroQuery.Data> response) {

                        int x = response.getData().Page().media().size();

                        for (int i = 0; i < x; i++){
                            String imagen = response.getData().Page().media().get(i).coverImage().extraLarge();
                            int id = response.getData().Page().media().get(i).id();
                            ObjetoMiniatura objetoMiniatura = new ObjetoMiniatura(imagen, id);
                            listaSyfy.add(objetoMiniatura);
                        }


                        HomeFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView = vista.findViewById(R.id.cienciaFiccionRecyclerHome);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                                adapter = new MiniaturaAdapter(HomeFragment.this.getContext(), listaSyfy, resoure);
                                layoutManager = new LinearLayoutManager(HomeFragment.this.getContext(), RecyclerView.HORIZONTAL, false);

                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(adapter);

                                adapter.setOnItemClickListener(new MiniaturaAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        Intent intent = new Intent(HomeFragment.this.getContext(), VerEspecificoActicity.class);
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

            @Override
            public void onFailure(@NotNull ApolloException e) {

            }
        });



    }


    public void llenarImagen1(){
        apolloClient.query(GetSeasonAnimeQuery.builder().page(1).perPage(50).type(TypeMedia).seasonYear(2020).isAdult(false).season(MediaSeason.SPRING).build()).enqueue(new ApolloCall.Callback<GetSeasonAnimeQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetSeasonAnimeQuery.Data> response) {
                int numPaginas = response.getData().Page().pageInfo().lastPage();
                int PagAl = (int)(Math.random() * numPaginas) + 1;

                apolloClient.query(GetSeasonAnimeQuery.builder().page(PagAl).perPage(50).type(TypeMedia).seasonYear(2020).isAdult(false).season(MediaSeason.SPRING).build()).enqueue(new ApolloCall.Callback<GetSeasonAnimeQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<GetSeasonAnimeQuery.Data> response) {

                        int numeroItems = response.getData().Page().media().size();
                        int itemAl = (int)(Math.random()* numeroItems-1) + 1;

                        HomeFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (response.getData().Page().media().get(itemAl).bannerImage() == null){

                                    String nombre = "No Guns Life 2";

                                    apolloClient.query(SearchQuery.builder().search(nombre).build()).enqueue(new ApolloCall.Callback<SearchQuery.Data>() {
                                        @Override
                                        public void onResponse(@NotNull Response<SearchQuery.Data> response) {
                                            String imagen = response.getData().Page().media().get(0).bannerImage();
                                            int id = response.getData().Page().media().get(0).id();
                                            ObjetoMiniatura objeto = new ObjetoMiniatura(imagen, id);
                                            HomeFragment.this.getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Picasso.get().load(objeto.getImagen()).into(imagenAl1);
                                                    imagenAl1.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent intent = new Intent(HomeFragment.this.getContext(), VerEspecificoActicity.class);
                                                            Bundle bundle = new Bundle();
                                                            bundle.putInt("id", id);
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

                                }else {
                                    String imagen = response.getData().Page().media().get(itemAl).bannerImage();
                                    int id = response.getData().Page().media().get(itemAl).id();
                                    ObjetoMiniatura objeto = new ObjetoMiniatura(imagen, id);
                                    HomeFragment.this.getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Picasso.get().load(objeto.getImagen()).into(imagenAl1);
                                            imagenAl1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(HomeFragment.this.getContext(), VerEspecificoActicity.class);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt("id", id);
                                                    intent.putExtras(bundle);
                                                    startActivity(intent);
                                                }
                                            });
                                        }
                                    });


                                }
                            }
                        });

                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {

                    }
                });
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {

            }
        });

    }


    public void llenarImagen2(){
        apolloClient.query(GetSeasonAnimeQuery.builder().page(1).perPage(50).type(TypeMedia).seasonYear(2020).isAdult(false).season(MediaSeason.SPRING).build()).enqueue(new ApolloCall.Callback<GetSeasonAnimeQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetSeasonAnimeQuery.Data> response) {
                int numPaginas = response.getData().Page().pageInfo().lastPage();
                int PagAl = (int)(Math.random() * numPaginas) + 1;

                apolloClient.query(GetSeasonAnimeQuery.builder().page(PagAl).perPage(50).type(TypeMedia).seasonYear(2020).isAdult(false).season(MediaSeason.SPRING).build()).enqueue(new ApolloCall.Callback<GetSeasonAnimeQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<GetSeasonAnimeQuery.Data> response) {

                        int numeroItems = response.getData().Page().media().size();
                        int itemAl = (int)(Math.random()* numeroItems);

                        HomeFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (response.getData().Page().media().get(itemAl).bannerImage() == null){

                                    String nombre = "Gibiate";

                                    apolloClient.query(SearchQuery.builder().search(nombre).build()).enqueue(new ApolloCall.Callback<SearchQuery.Data>() {
                                        @Override
                                        public void onResponse(@NotNull Response<SearchQuery.Data> response) {
                                            String imagen = response.getData().Page().media().get(0).bannerImage();
                                            int id = response.getData().Page().media().get(0).id();
                                            ObjetoMiniatura objeto = new ObjetoMiniatura(imagen, id);
                                            HomeFragment.this.getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Picasso.get().load(objeto.getImagen()).into(imagenAl2);
                                                    imagenAl2.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent intent = new Intent(HomeFragment.this.getContext(), VerEspecificoActicity.class);
                                                            Bundle bundle = new Bundle();
                                                            bundle.putInt("id", id);
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

                                }else {
                                    String imagen = response.getData().Page().media().get(itemAl).bannerImage();
                                    int id = response.getData().Page().media().get(itemAl).id();
                                    ObjetoMiniatura objeto = new ObjetoMiniatura(imagen, id);
                                    HomeFragment.this.getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Picasso.get().load(objeto.getImagen()).into(imagenAl2);
                                            imagenAl2.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(HomeFragment.this.getContext(), VerEspecificoActicity.class);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt("id", id);
                                                    intent.putExtras(bundle);
                                                    startActivity(intent);
                                                }
                                            });
                                        }
                                    });


                                }
                            }
                        });

                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {

                    }
                });
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {

            }
        });
    }
}
