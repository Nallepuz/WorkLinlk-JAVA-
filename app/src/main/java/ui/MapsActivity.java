package ui;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.worklink.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MapsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savesInstanceState) {
        super.onCreate(savesInstanceState);

        setContentView(R.layout.activity_maps);

        BottomNavigationView bottomNav = findViewById(R.id.botNav);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                Intent intent = new Intent( MapsActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            if (id == R.id.nav_config) {
                Intent intent = new Intent(MapsActivity.this, SettingActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            if (id == R.id.nav_logout) {
                Intent intent = new Intent(MapsActivity.this, LoginActivity.class);
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

