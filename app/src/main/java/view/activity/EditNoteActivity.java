package view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.worklink.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import model.db.AppDatabase;
import model.domain.Notes;

public class EditNoteActivity extends AppCompatActivity {

    private EditText editTitulo, editEstado;
    private int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_notes);

        editTitulo = findViewById(R.id.editTitulo);
        editEstado = findViewById(R.id.editEstado);
        Button btnUpdate = findViewById(R.id.btn_send);
        Button btnDelete = findViewById(R.id.btnDel);

        noteId = getIntent().getIntExtra("id", -1);
        editTitulo.setText(getIntent().getStringExtra("titulo"));
        editEstado.setText(getIntent().getStringExtra("desc"));

        btnUpdate.setOnClickListener(v -> actualizarNota());
        btnDelete.setOnClickListener(v -> eliminarNota());

        BottomNavigationView bottomNav = findViewById(R.id.botNav);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                Intent intent = new Intent(EditNoteActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            if (id == R.id.nav_config) {
                Intent intent = new Intent(EditNoteActivity.this, SettingActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            if (id == R.id.nav_request) {
                Intent intent = new Intent(EditNoteActivity.this, ReqActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            if (id == R.id.nav_reqList) {
                Intent intent = new Intent(EditNoteActivity.this, ListReqActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            if (id == R.id.nav_createNotes) {
                Intent intent = new Intent(EditNoteActivity.this, CreateNotesActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            if (id == R.id.nav_notes) {
                Intent intent = new Intent(EditNoteActivity.this, ListNotesActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            return false;
        });
    }

    private void actualizarNota() {
        String nuevoTitulo = editTitulo.getText().toString().trim();
        String nuevaDesc = editEstado.getText().toString().trim();

        if (nuevoTitulo.isEmpty()) return;

        Notes notaActualizada = new Notes();
        notaActualizada.setId(noteId);
        notaActualizada.setTitle(nuevoTitulo);
        notaActualizada.setDescription(nuevaDesc);

        new Thread(() -> {
            AppDatabase.getInstance(getApplicationContext()).notesDao().update(notaActualizada);
            runOnUiThread(() -> {
                Toast.makeText(this, com.example.worklink.R.string.nota_actualizada, Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }

    private void eliminarNota() {
        Notes notaABorrar = new Notes();
        notaABorrar.setId(noteId);

        new Thread(() -> {
            AppDatabase.getInstance(getApplicationContext()).notesDao().delete(notaABorrar);
            runOnUiThread(() -> {
                Toast.makeText(this, com.example.worklink.R.string.nota_eliminada, Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
}
