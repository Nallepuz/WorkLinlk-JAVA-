package view.activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.worklink.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

import model.domain.Application;
import presenter.ListDetailsPresenter;


public class ListDetailsActivity extends AppCompatActivity {

    private ListDetailsPresenter presenter;
    private EditText regType, regStatus, regHours;
    private TextView regFrom, regTo, serverName;
    private Switch checkUrgent;
    private long requestId;
    public static final String EXTRA_REQUEST_ID = "REQUEST_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listdetails);

        presenter = new ListDetailsPresenter(this);
        initViews();
        configurarNavegacion();

        requestId = getIntent().getLongExtra(EXTRA_REQUEST_ID, -1L);
        if (requestId == -1L) {
            mostrarMensaje(getString(com.example.worklink.R.string.id_no_v_lido));
            finish();
            return;
        }

        serverName.setText(getString(com.example.worklink.R.string.solicitud_n2) + requestId);
        presenter.cargarSolicitud(requestId);

        findViewById(R.id.btn_reqMod).setOnClickListener(v -> presenter.modificarSolicitud(
                requestId,
                regType.getText().toString().trim(),
                regStatus.getText().toString().trim(),
                regHours.getText().toString().trim(),
                regFrom.getText().toString().trim(),
                regTo.getText().toString().trim(),
                checkUrgent.isChecked()
        ));

        findViewById(R.id.btn_reqDelete).setOnClickListener(v -> presenter.eliminarSolicitud(requestId));

        regFrom.setOnClickListener(v -> showTimePicker(true));
        regTo.setOnClickListener(v -> showTimePicker(false));
    }

    public void mostrarDetalle(Application app) {
        regType.setText(app.getType());
        regStatus.setText(app.getStatus());
        regHours.setText(String.valueOf(app.getHoursRequested()));
        regFrom.setText(app.getFromTime() != null ? app.getFromTime() : "Seleccionar");
        regTo.setText(app.getToTime() != null ? app.getToTime() : "Seleccionar");
        checkUrgent.setChecked(app.getUrgent() != null && app.getUrgent());
    }

    public void onAccionCorrecta(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        finish();
    }

    public void mostrarMensaje(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void initViews() {
        serverName = findViewById(R.id.textServerName);
        regType = findViewById(R.id.reqType);
        regStatus = findViewById(R.id.reqEstado);
        regHours = findViewById(R.id.reqHours);
        regFrom = findViewById(R.id.reqFrom);
        regTo = findViewById(R.id.reqTo);
        checkUrgent = findViewById(R.id.reqUrgent);
    }

    private void showTimePicker(boolean isStart) {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(this, (view, h, m) -> {
            String time = String.format("%02d:%02d:00", h, m);
            if (isStart) regFrom.setText(time);
            else regTo.setText(time);
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
    }

    private void configurarNavegacion() {
        BottomNavigationView bottomNav = findViewById(R.id.botNav);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) startActivity(new Intent(this, HomeActivity.class));
            else if (id == R.id.nav_config) startActivity(new Intent(this, SettingActivity.class));
            else if (id == R.id.nav_request) startActivity(new Intent(this, ReqActivity.class));
            else if (id == R.id.nav_reqList) startActivity(new Intent(this, ListReqActivity.class));
            else if (id == R.id.nav_createNotes) startActivity(new Intent(this, CreateNotesActivity.class));
            else if (id == R.id.nav_notes) startActivity(new Intent(this, ListNotesActivity.class));
            finish();
            return true;
        });
    }
}
