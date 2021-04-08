package com.example.tfgv20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.tfgv20.Fragments.Main.FauvoriteFragment;
import com.example.tfgv20.Fragments.Main.HomeFragment;
import com.example.tfgv20.Fragments.Main.ListFragment;
import com.example.tfgv20.Fragments.Main.RankingFragment;
import com.example.tfgv20.Fragments.Main.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView botomNav = findViewById(R.id.bottomNavigationMain);
        botomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()){
                case R.id.homeMenuMain:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.favoriteMenuMain:
                    selectedFragment = new FauvoriteFragment();
                    break;
                case R.id.listMenuMain:
                    selectedFragment = new ListFragment();
                    break;
                case R.id.rankingMenuMain:
                    selectedFragment = new RankingFragment();
                    break;
                case R.id.userMenuMain:
                    selectedFragment = new UserFragment();
                    break;
                default:
                    selectedFragment = new HomeFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };


}
