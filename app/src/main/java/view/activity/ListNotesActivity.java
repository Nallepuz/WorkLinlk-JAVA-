package view.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.worklink.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import presenter.NoteListPresenter;
import view.adapter.NotesAdapter;
import model.domain.Notes;

public class ListNotesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private NoteListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        recyclerView = findViewById(R.id.rvNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        presenter = new NoteListPresenter(this);

        BottomNavigationView bottomNav = findViewById(R.id.botNav);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            }
            if (id == R.id.nav_config) {
                startActivity(new Intent(this, SettingActivity.class));
                finish();
                return true;
            }
            if (id == R.id.nav_request) {
                startActivity(new Intent(this, ReqActivity.class));
                finish();
                return true;
            }
            if (id == R.id.nav_reqList) {
                startActivity(new Intent(this, ListReqActivity.class));
                finish();
                return true;
            }
            if (id == R.id.nav_createNotes) {
                startActivity(new Intent(this, CreateNotesActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    public void mostrarNotas(List<Notes> listaNotas) {
        if (listaNotas != null) {
            adapter = new NotesAdapter(listaNotas);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.cargarNotas();
    }
}
