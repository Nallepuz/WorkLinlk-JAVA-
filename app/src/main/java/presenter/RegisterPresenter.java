package presenter;

import com.example.worklink.R;

import model.api.WorklinkApi;
import model.api.WorklinkApiInterface;
import model.domain.User;
import model.logic.RegisterValidation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import view.activity.RegisterActivity;

public class RegisterPresenter {
    private RegisterActivity view;
    private WorklinkApiInterface api;
    private RegisterValidation validation;

    public RegisterPresenter(RegisterActivity view) {
        this.view = view;
        this.api = WorklinkApi.buildInstance();
        this.validation = new RegisterValidation();
    }

    public void intentarRegistro(String name, String phone, String email, String password,
                                 String repeatPass, String lonStr, String latStr, boolean isAdmin) {

        RegisterValidation.Result result = validation.checkRegister(name, phone, email, password, repeatPass);

        if (result != RegisterValidation.Result.OK) {
            view.mostrarMensaje(obtenerMensajeError(result));
            return;
        }

        if (lonStr.isEmpty() || latStr.isEmpty()) {
            view.mostrarMensaje(view.getString(R.string.introduce_longitud_y_latitud));
            return;
        }

        try {
            double longitude = Double.parseDouble(lonStr);
            double latitude = Double.parseDouble(latStr);

            User user = new User();
            user.setName(name);
            user.setPhone(phone);
            user.setEmail(email);
            user.setPassword(password);
            user.setActive(isAdmin);
            user.setLongitud(longitude);
            user.setLatitud(latitude);
            user.setRolId(isAdmin ? 1L : 2L);

            api.registerUser(user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        view.onRegisterSuccess();
                    } else if (response.code() == 409) {
                        view.mostrarMensaje(view.getString(R.string.email_o_tel_fono_ya_registrado));
                    } else {
                        view.mostrarMensaje(view.getString(R.string.error_al_registrar) + response.code());
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    view.mostrarMensaje(view.getString(R.string.error_de_red3) + t.getMessage());
                }
            });

        } catch (NumberFormatException e) {
            view.mostrarMensaje(view.getString(R.string.longitud_latitud_no_v_lidas));
        }
    }

    private String obtenerMensajeError(RegisterValidation.Result result) {
        switch (result) {
            case EMPTY_FIELDS: return view.getString(R.string.rellena_todos_los_campos3);
            case PASSWORD_MISMATCH: return view.getString(R.string.las_contrase_as_no_coinciden);
            case INVALID_EMAIL: return view.getString(R.string.email_no_v_lido);
            default: return view.getString(R.string.error_desconocido);
        }
    }
}