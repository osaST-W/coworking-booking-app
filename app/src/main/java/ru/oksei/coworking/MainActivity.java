package ru.oksei.coworking;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

import ru.oksei.coworking.adapters.WorkspaceAdapter;
import ru.oksei.coworking.database.DatabaseHelper;
import ru.oksei.coworking.models.Workspace;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private WorkspaceAdapter adapter;
    private ChipGroup chipGroupCategories;
    private String currentCategory = "Все";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.rvWorkspaces);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        chipGroupCategories = findViewById(R.id.chipGroupCategories);
        setupFilterChips();

        Button btnMyBookings = findViewById(R.id.btnOpenMyBookings);
        btnMyBookings.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, MyBookingsActivity.class));
        });

        loadWorkspaces();
    }

    private void setupFilterChips() {
        chipGroupCategories.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                currentCategory = "Все";
            } else {
                Chip chip = findViewById(checkedIds.get(0));
                if (chip != null) {
                    currentCategory = chip.getText().toString();
                }
            }
            loadWorkspaces();
        });
    }

    private void loadWorkspaces() {
        List<Workspace> list = dbHelper.getAllWorkspaces(currentCategory);
        if (adapter == null) {
            adapter = new WorkspaceAdapter(this, list);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateList(list);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWorkspaces();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_my_bookings) {
            startActivity(new Intent(this, MyBookingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
