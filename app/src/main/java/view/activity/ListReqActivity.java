package view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.worklink.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import presenter.ListReqPresenter;
import view.adapter.ApplicationAdapter;
import model.domain.Application;

public class ListReqActivity extends AppCompatActivity implements ApplicationAdapter.OnItemClickListener {

    private ListReqPresenter presenter;
    private RecyclerView recyclerView;
    private ApplicationAdapter adapter;
    private List<Application> applications = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listreq);

        presenter = new ListReqPresenter(this);

        recyclerView = findViewById(R.id.rvApplications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ApplicationAdapter(applications, this);
        recyclerView.setAdapter(adapter);

        configurarNavegacion();
    }

    public void mostrarSolicitudes(List<Application> lista) {
        applications.clear();
        applications.addAll(lista);
        adapter.notifyDataSetChanged();

        if (applications.isEmpty()) {
            mostrarMensaje(getString(com.example.worklink.R.string.no_tienes_solicitudes));
        }
    }

    @Override
    public void onItemClick(Application app) {
        Intent intent = new Intent(this, ListDetailsActivity.class);
        intent.putExtra("REQUEST_ID", app.getId());
        startActivity(intent);
    }

    public void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    public void cerrarPantalla() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.cargarDatos();
    }

    private void configurarNavegacion() {
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
            if (id == R.id.nav_createNotes) {
                startActivity(new Intent(this, CreateNotesActivity.class));
                finish();
                return true;
            }
            if (id == R.id.nav_notes) {
                startActivity(new Intent(this, ListNotesActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }
}