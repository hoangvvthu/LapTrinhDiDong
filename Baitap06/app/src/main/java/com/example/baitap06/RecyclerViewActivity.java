package com.example.baitap06;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitap06.databinding.ActivityRecyclerviewBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecyclerViewActivity extends AppCompatActivity {

    private ActivityRecyclerviewBinding binding;
    private ArrayList<IconModel> arrayList;
    private IconAdapter iconAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecyclerviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up the Toolbar
        setSupportActionBar(binding.recyclerToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("RecyclerView Example");
        }

        // 1. Initialize data
        initData();

        // 2. Set up RecyclerView
        iconAdapter = new IconAdapter(this, arrayList);
        binding.rcIcon.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        binding.rcIcon.setAdapter(iconAdapter);

        // 3. Set up search listener
        setupSearchListener();
    }

    private void initData() {
        arrayList = new ArrayList<>();
        arrayList.add(new IconModel(R.drawable.ic_baseline_person_24, "Account"));
        arrayList.add(new IconModel(R.drawable.ic_baseline_person_24, "Gifts"));
        arrayList.add(new IconModel(R.drawable.ic_baseline_person_24, "Wallet"));
        arrayList.add(new IconModel(R.drawable.ic_baseline_person_24, "Offers"));
        arrayList.add(new IconModel(R.drawable.ic_baseline_person_24, "Orders"));
        arrayList.add(new IconModel(R.drawable.ic_baseline_person_24, "Rewards"));
        arrayList.add(new IconModel(R.drawable.ic_baseline_person_24, "Settings"));
        arrayList.add(new IconModel(R.drawable.ic_baseline_person_24, "Logout"));
        arrayList.add(new IconModel(R.drawable.ic_baseline_person_24, "Help"));
    }

    private void setupSearchListener() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String text) {
        List<IconModel> filteredList = new ArrayList<>();
        if (text.isEmpty()) {
            filteredList.addAll(arrayList);
        } else {
            for (IconModel item : arrayList) {
                if (item.getDesc().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) {
                    filteredList.add(item);
                }
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show();
        }
        
        iconAdapter.filterList(filteredList);
    }
}
