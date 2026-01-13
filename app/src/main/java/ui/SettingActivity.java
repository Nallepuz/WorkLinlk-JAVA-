package ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.worklink.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savesInstanceState) {
        super.onCreate(savesInstanceState);

        setContentView(R.layout.activity_settings);

        BottomNavigationView bottomNav = findViewById(R.id.botNav);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                Intent intent = new Intent( SettingActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            if (id == R.id.nav_maps) {
                Intent intent = new Intent(SettingActivity.this, MapsActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            if (id == R.id.nav_logout) {
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            if (id == R.id.nav_request) {
                return true;
            }

            return false;
        });
    }
}
