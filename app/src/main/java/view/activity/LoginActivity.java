package view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.worklink.R;

import presenter.LoginPresenter;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private TextView tvError;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_user);

        presenter = new LoginPresenter(this);

        etEmail = findViewById(R.id.userEmail);
        etPassword = findViewById(R.id.password);
        tvError = findViewById(R.id.tvError);
        Button btnLogin = findViewById(R.id.button_login);
        TextView textRegister = findViewById(R.id.textRegister);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();
            presenter.intentarLogin(email, pass);
        });

        textRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    public void onLoginSuccess(String email) {
        tvError.setVisibility(View.GONE);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
    }

    public void mostrarError(String mensaje) {
        tvError.setText(mensaje);
        tvError.setVisibility(View.VISIBLE);
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}
