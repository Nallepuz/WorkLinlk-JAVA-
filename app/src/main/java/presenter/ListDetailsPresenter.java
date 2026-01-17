package presenter;

import com.example.worklink.R;

import model.api.WorklinkApi;
import model.api.WorklinkApiInterface;
import model.domain.Application;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import view.activity.ListDetailsActivity;

public class ListDetailsPresenter {
    private ListDetailsActivity view;
    private WorklinkApiInterface api;
    private Application loadedApplication;

    public ListDetailsPresenter(ListDetailsActivity view) {
        this.view = view;
        this.api = WorklinkApi.buildInstance();
    }

    public void cargarSolicitud(long requestId) {
        api.getApplication(requestId).enqueue(new Callback<Application>() {
            @Override
            public void onResponse(Call<Application> call, Response<Application> response) {
                if (response.isSuccessful() && response.body() != null) {
                    loadedApplication = response.body();
                    view.mostrarDetalle(loadedApplication);
                } else {
                    view.mostrarMensaje(view.getString(R.string.error_al_cargar) + response.code());
                }
            }

            @Override
            public void onFailure(Call<Application> call, Throwable t) {
                view.mostrarMensaje(view.getString(R.string.error_de_red));
            }
        });
    }

    public void modificarSolicitud(long requestId, String type, String status, String hoursStr,
                                   String from, String to, boolean urgent) {
        if (loadedApplication == null) return;

        if (type.isEmpty() || status.isEmpty() || hoursStr.isEmpty()) {
            view.mostrarMensaje(view.getString(R.string.rellena_los_campos_obligatorios));
            return;
        }

        try {
            float hours = Float.parseFloat(hoursStr);

            Application updated = new Application();
            updated.setType(type);
            updated.setStatus(status);
            updated.setHoursRequested(hours);
            updated.setUrgent(urgent);

            // Lógica de tiempos fallback
            updated.setFromTime(from.equalsIgnoreCase("Seleccionar") ? loadedApplication.getFromTime() : from);
            updated.setToTime(to.equalsIgnoreCase("Seleccionar") ? loadedApplication.getToTime() : to);

            updated.setUserId(loadedApplication.getUserId() != null ? loadedApplication.getUserId() : 1L);
            updated.setResolverId(loadedApplication.getResolverId() != null ? loadedApplication.getResolverId() : 1L);

            api.modifyApplication(requestId, updated).enqueue(new Callback<Application>() {
                @Override
                public void onResponse(Call<Application> call, Response<Application> response) {
                    if (response.isSuccessful()) {
                        view.onAccionCorrecta(view.getString(R.string.solicitud_modificada));
                    } else {
                        view.mostrarMensaje(view.getString(R.string.error_al_modificar));
                    }
                }

                @Override
                public void onFailure(Call<Application> call, Throwable t) {
                    view.mostrarMensaje(view.getString(R.string.fallo_de_red));
                }
            });
        } catch (NumberFormatException e) {
            view.mostrarMensaje(view.getString(R.string.horas_no_v_lidas));
        }
    }

    public void eliminarSolicitud(long requestId) {
        api.deleteApplication(requestId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    view.onAccionCorrecta(view.getString(R.string.solicitud_eliminada));
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                view.mostrarMensaje(view.getString(R.string.error_al_eliminar));
            }
        });
    }
}