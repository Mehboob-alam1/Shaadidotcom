package com.mehboob.myshadi.views.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityDashBoardBinding;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.utils.Utils;
import com.mehboob.myshadi.viewmodel.FUPViewModel;
import com.mehboob.myshadi.views.dashboard.frags.ChatFragment;
import com.mehboob.myshadi.views.dashboard.frags.HomeFragment;
import com.mehboob.myshadi.views.dashboard.frags.InboxFragment;
import com.mehboob.myshadi.views.dashboard.frags.MatchesFragment;
import com.mehboob.myshadi.views.dashboard.frags.PremiumFragment;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class DashBoardActivity extends AppCompatActivity {
private ActivityDashBoardBinding binding;

private SessionManager sessionManager;
private FUPViewModel fupViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDashBoardBinding.inflate(getLayoutInflater());
        fupViewModel = new ViewModelProvider(this).get(FUPViewModel.class);
        setContentView(binding.getRoot());
        sessionManager= new SessionManager(this);

setBottomBar();


        getProfileUpdates();


        fupViewModel.getUserProfileLiveData().observe(this, userProfile -> Utils.showSnackBar(DashBoardActivity.this,userProfile.toString()));



    }

    @SuppressLint("NonConstantResourceId")
    private void setBottomBar() {


BottomNavigationView bottomNavigation= findViewById(R.id.bottom_nav);

        NavController   navController= Navigation.findNavController(this,R.id.nav_host_fragment);

        NavigationUI.setupWithNavController(bottomNavigation,navController);






    }

    private void getProfileUpdates() {


        UserProfile profile= new UserProfile(sessionManager.fetchGender(),
                sessionManager.fetchLivingIn(),
                sessionManager.fetchReligion(),
                sessionManager.fetchCommunity(),
                sessionManager.fetchSubCommunity(),
                sessionManager.fetchMaritalStatus(),
                FirebaseAuth.getInstance().getCurrentUser().getUid());
        fupViewModel.getProfile(profile);
    }



}