package view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.worklink.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.gestures.GesturesUtils;
import com.mapbox.maps.plugin.gestures.OnMapClickListener;

import model.domain.User;
import presenter.SettingPresenter;

public class SettingActivity extends AppCompatActivity {

    private SettingPresenter presenter;

    private TextView vistaName, vistaPhone, vistaEmail, vistaPass;
    private EditText editName, editPhone, editEmail, editPassword, editRepeatPassword;
    private Button btnUpdate, btnDelete;

    private MapView mapViewSettings;
    private PointAnnotationManager pointAnnotationManager;
    private PointAnnotation currentAnnotation = null;

    private Double selectedLon = null;
    private Double selectedLat = null;

    private Double currentUserLon = null;
    private Double currentUserLat = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        presenter = new SettingPresenter(this);

        initViews();
        configurarNavegacion();

        ScrollView scroll = findViewById(R.id.scrollViewSettings);
        View overlay = findViewById(R.id.touchOverlaySettings);

        if (overlay != null) {
            overlay.setOnTouchListener((v, event) -> {
                int action = event.getActionMasked();

                if (action == MotionEvent.ACTION_DOWN ||
                        action == MotionEvent.ACTION_MOVE ||
                        action == MotionEvent.ACTION_POINTER_DOWN) {
                    scroll.requestDisallowInterceptTouchEvent(true);
                } else if (action == MotionEvent.ACTION_UP ||
                        action == MotionEvent.ACTION_CANCEL ||
                        action == MotionEvent.ACTION_POINTER_UP) {
                    scroll.requestDisallowInterceptTouchEvent(false);
                }

                return mapViewSettings.onTouchEvent(event);
            });
        }

        mapViewSettings.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS, style -> {
            Drawable drawable = ContextCompat.getDrawable(this, android.R.drawable.ic_menu_mylocation);
            if (drawable != null) {
                int w = drawable.getIntrinsicWidth() > 0 ? drawable.getIntrinsicWidth() : 48;
                int h = drawable.getIntrinsicHeight() > 0 ? drawable.getIntrinsicHeight() : 48;

                Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);

                style.addImage("marker-icon-id", bitmap);
            }
            setupMapSettings();
            renderUserLocationIfReady();
        });

        presenter.cargarDatosUsuario();

        btnUpdate.setOnClickListener(v -> {
            Double lonToSend = (selectedLon != null) ? selectedLon : currentUserLon;
            Double latToSend = (selectedLat != null) ? selectedLat : currentUserLat;

            presenter.actualizarPerfil(
                    editName.getText().toString().trim(),
                    editPhone.getText().toString().trim(),
                    editEmail.getText().toString().trim(),
                    editPassword.getText().toString().trim(),
                    editRepeatPassword.getText().toString().trim(),
                    lonToSend != null ? String.valueOf(lonToSend) : "",
                    latToSend != null ? String.valueOf(latToSend) : ""
            );
        });

        btnDelete.setOnClickListener(v -> confirmDeleteUser());
    }

    private void initViews() {
        vistaName = findViewById(R.id.vistaName);
        vistaPhone = findViewById(R.id.vistaPhone);
        vistaEmail = findViewById(R.id.vistaEmail);
        vistaPass = findViewById(R.id.vistaPass);

        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPass);
        editRepeatPassword = findViewById(R.id.editRepeat);

        mapViewSettings = findViewById(R.id.mapViewSettings);

        btnUpdate = findViewById(R.id.btn_update);
        btnDelete = findViewById(R.id.btn_deleteUser);
    }

    private void setupMapSettings() {
        AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(mapViewSettings);
        pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(
                annotationPlugin,
                new AnnotationConfig()
        );

        var g = GesturesUtils.getGestures(mapViewSettings);
        g.setScrollEnabled(true);
        g.setPinchToZoomEnabled(true);
        g.setRotateEnabled(true);
        g.setPitchEnabled(true);

        g.addOnMapClickListener(new OnMapClickListener() {
            @Override
            public boolean onMapClick(@NonNull Point point) {
                if (currentAnnotation != null) {
                    pointAnnotationManager.delete(currentAnnotation);
                }

                PointAnnotationOptions options = new PointAnnotationOptions()
                        .withPoint(point)
                        .withIconImage("marker-icon-id");

                currentAnnotation = pointAnnotationManager.create(options);

                selectedLat = point.latitude();
                selectedLon = point.longitude();

                Toast.makeText(SettingActivity.this, "Ubicación actualizada", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        renderUserLocationIfReady();
    }

    private void renderUserLocationIfReady() {
        if (pointAnnotationManager == null) return;
        if (currentUserLon == null || currentUserLat == null) return;

        Point p = Point.fromLngLat(currentUserLon, currentUserLat);

        if (currentAnnotation != null) {
            pointAnnotationManager.delete(currentAnnotation);
        }

        PointAnnotationOptions options = new PointAnnotationOptions()
                .withPoint(p)
                .withIconImage("marker-icon-id");

        currentAnnotation = pointAnnotationManager.create(options);

        mapViewSettings.getMapboxMap().setCamera(
                new CameraOptions.Builder()
                        .center(p)
                        .zoom(14.0)
                        .build()
        );
    }

    public void mostrarDatosUsuario(User user) {
        vistaName.setText(user.getName());
        vistaPhone.setText(user.getPhone());
        vistaEmail.setText(user.getEmail());
        vistaPass.setText(user.getPassword());

        editName.setText(user.getName());
        editPhone.setText(user.getPhone());
        editEmail.setText(user.getEmail());
        editPassword.setText("");
        editRepeatPassword.setText("");

        currentUserLon = user.getLongitud();
        currentUserLat = user.getLatitud();

        renderUserLocationIfReady();
    }

    public void onActualizacionCorrecta(User user) {
        mostrarDatosUsuario(user);
        Toast.makeText(this, R.string.datos_actualizados_correctamente, Toast.LENGTH_SHORT).show();
    }

    public void onEliminacionCorrecta() {
        Toast.makeText(this, R.string.cuenta_eliminada_correctamente, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void mostrarError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void onUsuarioNoEncontrado() {
        Toast.makeText(this, R.string.usuario_no_encontrado2, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void confirmDeleteUser() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.eliminar_cuenta)
                .setMessage(R.string.seguro_que_quieres_eliminar_tu_cuenta_esta_acci_n_no_se_puede_deshacer)
                .setPositiveButton(R.string.eliminar4, (dialog, which) -> presenter.eliminarCuenta())
                .setNegativeButton(R.string.cancelar, null)
                .show();
    }

    private void configurarNavegacion() {
        BottomNavigationView bottomNav = findViewById(R.id.botNav);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            }
            if (id == R.id.nav_request) {
                startActivity(new Intent(this, ReqActivity.class));
                finish();
                return true;
            }
            if (id == R.id.nav_reqList) {
                startActivity(new Intent(this, ListReqActivity.class));
                finish();
                return true;
            }
            if (id == R.id.nav_createNotes) {
                startActivity(new Intent(this, CreateNotesActivity.class));
                finish();
                return true;
            }
            if (id == R.id.nav_notes) {
                startActivity(new Intent(this, ListNotesActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mapViewSettings != null) mapViewSettings.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mapViewSettings != null) mapViewSettings.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapViewSettings != null) mapViewSettings.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapViewSettings != null) mapViewSettings.onLowMemory();
    }
}
