package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityStateCityBinding;
import com.mehboob.myshadi.databinding.BottomSheetStateBinding;

public class StateCityActivity extends AppCompatActivity {
private ActivityStateCityBinding binding;

private String[] states={"Gilgit-Baltistan","Punjab","Sindh","Khyber Pakhtunkhwa","Balochistan","Azad Kashmir"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityStateCityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

binding.txtState.setOnTouchListener(new View.OnTouchListener() {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        showBottomDialog(states,binding.txtState);
        return false;
    }
});

    }


    private void showBottomDialog(String[] array, TextView textView){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_state);

        ImageView imgCancel= bottomSheetDialog.findViewById(R.id.imgCancel);
        ListView listView=bottomSheetDialog.findViewById(R.id.listView);
        imgCancel.setOnClickListener(view -> bottomSheetDialog.dismiss());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String itemSelected = array[i];
                textView.setText(itemSelected);
                bottomSheetDialog.dismiss();
            }
        });



        bottomSheetDialog.show();
    }
}