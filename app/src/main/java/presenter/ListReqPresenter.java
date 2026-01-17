package presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.worklink.R;

import java.util.List;
import model.api.WorklinkApi;
import model.api.WorklinkApiInterface;
import model.domain.Application;
import model.domain.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import view.activity.ListReqActivity;

public class ListReqPresenter {
    private ListReqActivity view;
    private WorklinkApiInterface api;

    public ListReqPresenter(ListReqActivity view) {
        this.view = view;
        this.api = WorklinkApi.buildInstance();
    }

    public void cargarDatos() {
        SharedPreferences prefs = view.getSharedPreferences("worklink_prefs", Context.MODE_PRIVATE);
        String email = prefs.getString("LOGGED_EMAIL", null);

        if (email == null) {
            view.mostrarMensaje(view.getString(R.string.no_hay_usuario_logueado));
            view.cerrarPantalla();
            return;
        }

        // Primero obtenemos el usuario para tener su ID
        api.getUsersByEmail(email).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    long userId = response.body().get(0).getId();
                    obtenerSolicitudes();
                } else {
                    view.mostrarMensaje(view.getString(R.string.usuario_no_encontrado));
                    view.cerrarPantalla();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                view.mostrarMensaje(view.getString(R.string.error_de_red_al_cargar_usuario));
            }
        });
    }

    private void obtenerSolicitudes() {
        api.getApplication().enqueue(new Callback<List<Application>>() {
            @Override
            public void onResponse(Call<List<Application>> call, Response<List<Application>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.mostrarSolicitudes(response.body());
                } else {
                    view.mostrarMensaje(view.getString(R.string.error_al_cargar_solicitudes) + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Application>> call, Throwable t) {
                view.mostrarMensaje(view.getString(R.string.error_de_red_al_cargar_solicitudes));
            }
        });
    }
}