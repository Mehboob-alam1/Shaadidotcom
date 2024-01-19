package com.mehboob.myshadi.views.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityDashBoardBinding;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.viewmodel.FUPViewModel;
import com.mehboob.myshadi.viewmodel.MatchMakingViewModel;
import com.mehboob.myshadi.viewmodel.UserViewModel;

public class DashBoardActivity extends AppCompatActivity {
    private ActivityDashBoardBinding binding;

    private SessionManager sessionManager;
    private FUPViewModel fupViewModel;

    private UserViewModel userViewModel;

    private MatchMakingViewModel matchMakingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashBoardBinding.inflate(getLayoutInflater());
        fupViewModel = new ViewModelProvider(this).get(FUPViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        matchMakingViewModel = new ViewModelProvider(this).get(MatchMakingViewModel.class);
        setContentView(binding.getRoot());
        sessionManager = new SessionManager(this);

        setBottomBar();


        fupViewModel.getProfile(sessionManager.fetchUserId());

//        checkNotifData();

        userViewModel.getLiveData().observe(this, user -> {

            sessionManager.saveUserID(user.getUserId());

            fupViewModel.updateMatchesPreferences(user.getUserId());
            fupViewModel.updateSharedPreferences(user.getUserId());


            if (!sessionManager.fetchGender().equals("null")) {
                matchMakingViewModel.getUserProfiles();
            }
        });




    }



    @SuppressLint("NonConstantResourceId")
    private void setBottomBar() {


        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_nav);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupWithNavController(bottomNavigation, navController);


    }


    @Override
    protected void onResume() {
        super.onResume();


        matchMakingViewModel.getBestRecentMatchedProfiles().observe(this, userMatches -> {

            Log.d("Profiles", userMatches.toString());
        });


    }
}