package ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.worklink.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import android.widget.CalendarView;
import android.widget.TextView;
import java.util.Calendar;


public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navBar;
    private Toolbar appbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNav = findViewById(R.id.botNav);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_maps) {
                Intent intent = new Intent(HomeActivity.this, MapsActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            if (id == R.id.nav_config) {
                Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            if (id == R.id.nav_logout) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            if (id == R.id.nav_request) {
                return true;
            }

            return false;
        });

        TextView textBienvenido = findViewById(R.id.texto);
        String user = getIntent().getStringExtra("username");
        if (user != null) {
            textBienvenido.setText("¡Bienvenido " + user + "!");
        }


        CalendarView calendarView = findViewById(R.id.calendarView);
        TextView textTurno = findViewById(R.id.textTurno);

// EJEMPLO sencillo de "turnos" inventados
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                // Aquí decidirías el turno según tus datos.
                // De momento, hago un ejemplo muy tonto:
                String turno;
                if (dayOfMonth % 3 == 0) {
                    turno = "Turno de mañana";
                } else if (dayOfMonth % 3 == 1) {
                    turno = "Turno de tarde";
                } else {
                    turno = "Turno de noche";
                }

                textTurno.setText("El " + dayOfMonth + "/" + (month + 1) + "/" + year + ": " + turno);
            }
        });

    }
}
