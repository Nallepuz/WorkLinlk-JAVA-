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
import view.activity.ReqActivity;

public class ReqPresenter {
    private ReqActivity view;
    private WorklinkApiInterface api;
    private User currentUser;

    public ReqPresenter(ReqActivity view) {
        this.view = view;
        this.api = WorklinkApi.buildInstance();
    }

    public void cargarUsuario() {
        SharedPreferences prefs = view.getSharedPreferences("worklink_prefs", Context.MODE_PRIVATE);
        String email = prefs.getString("LOGGED_EMAIL", null);

        if (email == null) {
            view.mostrarMensaje(view.getString(com.example.worklink.R.string.no_hay_usuario_logueado2));
            view.cerrarPantalla();
            return;
        }

        api.getUsersByEmail(email).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    currentUser = response.body().get(0);
                } else {
                    view.mostrarMensaje(view.getString(com.example.worklink.R.string.no_se_pudo_obtener_el_usuario));
                    view.cerrarPantalla();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                view.mostrarMensaje(view.getString(com.example.worklink.R.string.error_de_red_al_cargar_usuario2));
                view.cerrarPantalla();
            }
        });
    }

    public void enviarSolicitud(String type, String status, String hoursStr, String from, String to, boolean urgent) {
        if (currentUser == null) {
            view.mostrarMensaje(view.getString(com.example.worklink.R.string.cargando_usuario_espera_un_momento));
            return;
        }

        if (type.isEmpty() || status.isEmpty() || hoursStr.isEmpty()) {
            view.mostrarMensaje(view.getString(com.example.worklink.R.string.tipo_estado_y_horas_son_obligatorios));
            return;
        }

        try {
            float hours = Float.parseFloat(hoursStr);

            Application application = new Application();
            application.setType(type);
            application.setStatus(status);
            application.setHoursRequested(hours);
            application.setFromTime(from);
            application.setToTime(to);
            application.setUrgent(urgent);
            application.setUserId(currentUser.getId());
            application.setResolverId(currentUser.getId());

            api.registerApplication(application).enqueue(new Callback<Application>() {
                @Override
                public void onResponse(Call<Application> call, Response<Application> response) {
                    if (response.isSuccessful()) {
                        view.onSolicitudEnviada();
                    } else {
                        view.mostrarMensaje(view.getString(R.string.error_al_enviar) + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Application> call, Throwable t) {
                    view.mostrarMensaje(view.getString(R.string.error_de_red_al_enviar_solicitud));
                }
            });

        } catch (NumberFormatException e) {
            view.mostrarMensaje(view.getString(R.string.horas_debe_ser_un_n_mero));
        }
    }
}