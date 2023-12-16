package com.mehboob.myshadi.views.dashboard.frags.matches;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityDashBoardBinding;
import com.mehboob.myshadi.databinding.FragmentMyMatchesBinding;


public class MyMatchesFragment extends Fragment {


   private FragmentMyMatchesBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding= FragmentMyMatchesBinding.inflate(inflater,container,false);


        return binding.getRoot();
    }
}