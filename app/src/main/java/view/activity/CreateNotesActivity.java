package view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.worklink.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import presenter.CreateNotePresenter;

public class CreateNotesActivity extends AppCompatActivity {

    private EditText editTitulo, editEstado;
    private Button btnSend;
    private CreateNotePresenter presenter;

    @Override
    protected void onCreate(Bundle savesInstanceState) {
        super.onCreate(savesInstanceState);
        setContentView(R.layout.activity_create_notes);

        editTitulo = findViewById(R.id.editTitulo);
        editEstado = findViewById(R.id.editEstado);
        btnSend = findViewById(R.id.btn_send);
        BottomNavigationView bottomNav = findViewById(R.id.botNav);

        presenter = new CreateNotePresenter(this);

        btnSend.setOnClickListener(v -> guardarNota());

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                Intent intent = new Intent(CreateNotesActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            if (id == R.id.nav_config) {
                Intent intent = new Intent(CreateNotesActivity.this, SettingActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            if (id == R.id.nav_request) {
                Intent intent = new Intent(CreateNotesActivity.this, ReqActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            if (id == R.id.nav_reqList) {
                Intent intent = new Intent(CreateNotesActivity.this, ListReqActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            if (id == R.id.nav_notes) {
                Intent intent = new Intent(CreateNotesActivity.this, ListNotesActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            return false;
        });
    }
    private void guardarNota() {
        String titulo = editTitulo.getText().toString().trim();
        String descripcion = editEstado.getText().toString().trim();

        presenter.guardarNota(titulo, descripcion);
    }
    public void onNotaGuardada() {
        Toast.makeText(this, com.example.worklink.R.string.guardado, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CreateNotesActivity.this, ListNotesActivity.class);
        startActivity(intent);
        finish();
    }
    public void mostrarError(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}

