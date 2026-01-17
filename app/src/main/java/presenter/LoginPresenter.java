package presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.worklink.R;

import java.util.List;
import model.api.WorklinkApi;
import model.api.WorklinkApiInterface;
import model.domain.User;
import model.logic.LoginValidation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import view.activity.LoginActivity;

public class LoginPresenter {
    private LoginActivity view;
    private WorklinkApiInterface api;
    private LoginValidation validation;

    public LoginPresenter(LoginActivity view) {
        this.view = view;
        this.api = WorklinkApi.buildInstance();
        this.validation = new LoginValidation();
    }

    public void intentarLogin(String email, String pass) {
        // 1. Validación
        LoginValidation.Result validationResult = validation.checkLogin(email, pass);
        if (validationResult == LoginValidation.Result.EMPTY_FIELDS) {
            view.mostrarError(view.getString(R.string.rellena_todos_los_campos2));
            return;
        }

        // 2. Llamada a la API
        api.getUsersByEmail(email).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    view.mostrarError(view.getString(R.string.error_al_conectar_con_el_servidor));
                    return;
                }

                List<User> users = response.body();
                if (users.isEmpty()) {
                    view.mostrarError(view.getString(R.string.usuario_o_contrase_a_incorrectos));
                    return;
                }

                User dbUser = users.get(0);
                if (dbUser.getPassword() == null || !pass.equals(dbUser.getPassword())) {
                    view.mostrarError(view.getString(R.string.usuario_o_contrase_a_incorrectos2));
                    return;
                }

                guardarSesion(dbUser.getEmail());
                view.onLoginSuccess(dbUser.getEmail());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                view.mostrarError(view.getString(R.string.error_de_red2) + t.getMessage());
            }
        });
    }

    private void guardarSesion(String email) {
        SharedPreferences prefs = view.getSharedPreferences("worklink_prefs", Context.MODE_PRIVATE);
        prefs.edit().putString("LOGGED_EMAIL", email).apply();
    }
}