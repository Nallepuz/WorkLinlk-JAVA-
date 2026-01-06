package ui;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.worklink.R;
import logic.LoginValidation;


public class LoginActivity extends AppCompatActivity {

    private LoginValidation loginValidation;

    @Override
    protected void onCreate(Bundle savesInstanceState) {
        super.onCreate(savesInstanceState);

        setContentView(R.layout.login_user);

        loginValidation = new LoginValidation();

        EditText etUsername = findViewById(R.id.username);
        EditText etPassword = findViewById(R.id.password);
        Button btnLogin = findViewById(R.id.button_login);
        TextView tvError = findViewById(R.id.tvError);


        btnLogin.setOnClickListener(v -> {
            String user = etUsername.getText().toString();
            String pass = etPassword.getText().toString();

            LoginValidation.Result result = loginValidation.checkLogin(user, pass);

            switch (result) {

                case OK:
                    tvError.setVisibility(View.GONE);
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    break;

                case EMPTY_FIELDS:

                    Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                    tvError.setText("Rellena todos los campos");
                    tvError.setVisibility(View.VISIBLE);
                    break;

                case INVALID_CREDENTIALS:
                    Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    tvError.setText("Usuario o contraseña incorrectos");
                    tvError.setVisibility(View.VISIBLE);
            }
        });
    }
}
