package view.activity;

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

            if (id == R.id.nav_config) {
                Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            if (id == R.id.nav_request) {
                Intent intent = new Intent(HomeActivity.this, ReqActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            if (id == R.id.nav_reqList) {
                Intent intent = new Intent(HomeActivity.this, ListReqActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            if (id == R.id.nav_createNotes) {
                Intent intent = new Intent(HomeActivity.this, CreateNotesActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            if (id == R.id.nav_notes) {
                Intent intent = new Intent(HomeActivity.this, ListNotesActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            return false;
        });

        TextView textBienvenido = findViewById(R.id.textServerName);
        String user = getIntent().getStringExtra("email");
        if (user != null) {
            textBienvenido.setText(getString(com.example.worklink.R.string.bienvenido) + user + "!");
        }


        CalendarView calendarView = findViewById(R.id.calendarView);
        TextView textTurno = findViewById(R.id.textTurno);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                String turno;
                if (dayOfMonth % 3 == 0) {
                    turno = getString(com.example.worklink.R.string.turno_de_ma_ana);
                } else if (dayOfMonth % 3 == 1) {
                    turno = getString(com.example.worklink.R.string.turno_de_tarde);
                } else {
                    turno = getString(com.example.worklink.R.string.turno_de_noche);
                }

                textTurno.setText("El " + dayOfMonth + "/" + (month + 1) + "/" + year + ": " + turno);
            }
        });

    }
}
