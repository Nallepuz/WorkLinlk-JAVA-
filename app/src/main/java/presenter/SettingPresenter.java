package presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.worklink.R;

import java.util.List;
import model.api.WorklinkApi;
import model.api.WorklinkApiInterface;
import model.domain.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import view.activity.SettingActivity;

public class SettingPresenter {
    private SettingActivity view;
    private WorklinkApiInterface api;
    private User currentUser;

    public SettingPresenter(SettingActivity view) {
        this.view = view;
        this.api = WorklinkApi.buildInstance();
    }

    public void cargarDatosUsuario() {
        SharedPreferences prefs = view.getSharedPreferences("worklink_prefs", Context.MODE_PRIVATE);
        String email = prefs.getString("LOGGED_EMAIL", null);

        if (email == null) {
            view.onUsuarioNoEncontrado();
            return;
        }

        api.getUsersByEmail(email).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    currentUser = response.body().get(0);
                    view.mostrarDatosUsuario(currentUser);
                } else {
                    view.onUsuarioNoEncontrado();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                view.mostrarError("Error de red: " + t.getMessage());
            }
        });
    }

    public void actualizarPerfil(String name, String phone, String email, String pass, String repeatPass, String lonStr, String latStr) {
        if (currentUser == null) return;

        // Validaciones básicas
        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            view.mostrarError(view.getString(R.string.nombre_tel_fono_y_email_son_obligatorios));
            return;
        }

        if (!pass.isEmpty() && !pass.equals(repeatPass)) {
            view.mostrarError(view.getString(R.string.las_contrase_as_no_coinciden2));
            return;
        }

        try {
            double lon = lonStr.isEmpty() ? currentUser.getLongitud() : Double.parseDouble(lonStr);
            double lat = latStr.isEmpty() ? currentUser.getLatitud() : Double.parseDouble(latStr);

            User updated = new User();
            updated.setId(currentUser.getId());
            updated.setName(name);
            updated.setPhone(phone);
            updated.setEmail(email);
            updated.setPassword(pass.isEmpty() ? currentUser.getPassword() : pass);
            updated.setActive(currentUser.isActive());
            updated.setLongitud(lon);
            updated.setLatitud(lat);
            updated.setRolId(currentUser.getRolId());

            api.modifyUser(currentUser.getId(), updated).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        currentUser = response.body();
                        // Actualizar email local por si cambió
                        view.getSharedPreferences("worklink_prefs", Context.MODE_PRIVATE)
                                .edit().putString("LOGGED_EMAIL", currentUser.getEmail()).apply();

                        view.onActualizacionCorrecta(currentUser);
                    } else {
                        view.mostrarError(view.getString(R.string.error_al_actualizar) + response.code());
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    view.mostrarError(view.getString(R.string.fallo_de_red3));
                }
            });
        } catch (Exception e) {
            view.mostrarError(view.getString(R.string.coordenadas_no_v_lidas));
        }
    }

    public void eliminarCuenta() {
        if (currentUser == null) return;
        api.deleteUser(currentUser.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    view.getSharedPreferences("worklink_prefs", Context.MODE_PRIVATE).edit().clear().apply();
                    view.onEliminacionCorrecta();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) { view.mostrarError(view.getString(R.string.error_al_eliminar3)); }
        });
    }
}