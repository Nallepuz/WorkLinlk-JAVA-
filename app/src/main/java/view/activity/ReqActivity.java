package view.activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.worklink.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import presenter.ReqPresenter;

public class ReqActivity extends AppCompatActivity {

    private ReqPresenter presenter;
    private EditText regType, regStatus, regHours;
    private TextView regFrom, regTo;
    private Switch checkUrgent;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        presenter = new ReqPresenter(this);

        initViews();
        configurarNavegacion();

        presenter.cargarUsuario();

        regFrom.setOnClickListener(v -> showTimePicker(true));
        regTo.setOnClickListener(v -> showTimePicker(false));

        btnSend.setOnClickListener(v -> presenter.enviarSolicitud(
                regType.getText().toString().trim(),
                regStatus.getText().toString().trim(),
                regHours.getText().toString().trim(),
                regFrom.getText().toString().trim(),
                regTo.getText().toString().trim(),
                checkUrgent.isChecked()
        ));
    }

    private void initViews() {
        regType = findViewById(R.id.editTitulo);
        regStatus = findViewById(R.id.editEstado);
        regHours = findViewById(R.id.editHours);
        regFrom = findViewById(R.id.editFrom);
        regTo = findViewById(R.id.editTo);
        checkUrgent = findViewById(R.id.editUrgent);
        btnSend = findViewById(R.id.btn_send);
    }

    public void onSolicitudEnviada() {
        Toast.makeText(this, com.example.worklink.R.string.solicitud_enviada_correctamente, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    public void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    public void cerrarPantalla() {
        finish();
    }

    private void showTimePicker(boolean isStart) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            String time = String.format("%02d:%02d:00", hourOfDay, minute);
            if (isStart) regFrom.setText(time);
            else regTo.setText(time);
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        dialog.show();
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
            if (id == R.id.nav_notes) {
                startActivity(new Intent(this, ListNotesActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }
}

