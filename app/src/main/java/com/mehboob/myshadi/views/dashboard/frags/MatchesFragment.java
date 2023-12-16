package com.mehboob.myshadi.views.dashboard.frags;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.adapters.homeAdapters.MatchesTabAdapter;
import com.mehboob.myshadi.databinding.FragmentMatchesBinding;


public class MatchesFragment extends Fragment {


   private FragmentMatchesBinding binding;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding=FragmentMatchesBinding.inflate(inflater,container,false);


        settingTabLayout();



     return    binding.getRoot();
    }

    private void settingTabLayout() {


//        binding.tabLayout.addTab(new TabLayout.Tab().setText("My Matches"));
//        binding.tabLayout.addTab(new TabLayout.Tab().setText("Near me") .setIcon(R.drawable.baseline_location_on_24));
//        binding.tabLayout.addTab(new TabLayout.Tab().setText("Shortlisted"));
//        binding.tabLayout.addTab(new TabLayout.Tab().setText("Search").setIcon(R.drawable.baseline_search_24));
//        binding.tabLayout.addTab(new TabLayout.Tab().setText("New"));
//        binding.tabLayout.addTab(new TabLayout.Tab().setText("Daily"));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final MatchesTabAdapter adapter = new MatchesTabAdapter(requireActivity(),requireActivity().getSupportFragmentManager(), binding.tabLayout.getTabCount());
        binding.viewPager.setAdapter(adapter);

        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}