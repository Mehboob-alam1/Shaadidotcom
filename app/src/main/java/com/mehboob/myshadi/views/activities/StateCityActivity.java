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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class StateCityActivity extends AppCompatActivity  {
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

    private List<String> loadCountries() {
        List<String> countryList = new ArrayList<>();

        try {
            // Load JSON file from assets
            InputStream is = getAssets().open("countries.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            // Parse JSON array
            JSONObject jsonObject = new JSONObject(json);
            JSONArray dataArray = jsonObject.getJSONArray("Data");

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject country = dataArray.getJSONObject(i);
                String countryName = country.getString("name");
                countryList.add(countryName);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return countryList;
    }
}