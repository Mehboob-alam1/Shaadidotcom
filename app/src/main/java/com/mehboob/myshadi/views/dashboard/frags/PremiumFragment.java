package com.mehboob.myshadi.views.dashboard.frags;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.FragmentPremiumBinding;


public class PremiumFragment extends Fragment {

private FragmentPremiumBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding=FragmentPremiumBinding.inflate(inflater,container,false);




        return binding.getRoot();
    }
}