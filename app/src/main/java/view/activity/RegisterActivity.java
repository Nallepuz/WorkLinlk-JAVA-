package view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.worklink.R;
import com.mapbox.geojson.Point;
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

import presenter.RegisterPresenter;

public class RegisterActivity extends AppCompatActivity {

    private RegisterPresenter presenter;

    private EditText regName, regPhone, regEmail, regPass, regRepeatPass;
    private CheckBox checkAdmin;

    private MapView mapView;
    private PointAnnotationManager pointAnnotationManager;
    private PointAnnotation currentAnnotation = null;

    private Double selectedLon = null;
    private Double selectedLat = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        presenter = new RegisterPresenter(this);

        regName = findViewById(R.id.reg_name);
        regPhone = findViewById(R.id.reg_phone);
        regEmail = findViewById(R.id.reg_email);
        regPass = findViewById(R.id.reg_pass);
        regRepeatPass = findViewById(R.id.reg_repeatPass);
        checkAdmin = findViewById(R.id.checkAdmin);

        Button btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(v -> {

            if (selectedLat == null || selectedLon == null) {
                mostrarMensaje(getString(R.string.introduce_longitud_y_latitud));
                return;
            }

            presenter.intentarRegistro(
                    regName.getText().toString().trim(),
                    regPhone.getText().toString().trim(),
                    regEmail.getText().toString().trim(),
                    regPass.getText().toString().trim(),
                    regRepeatPass.getText().toString().trim(),
                    String.valueOf(selectedLon),
                    String.valueOf(selectedLat),
                    checkAdmin.isChecked()
            );
        });

        findViewById(R.id.textLogin).setOnClickListener(v -> finish());

        mapView = findViewById(R.id.mapView);



        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS, style -> {

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

            setupMap();
        });

    }

    public void onRegisterSuccess() {
        Toast.makeText(this, R.string.usuario_registrado_correctamente, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    private void setupMap() {

        var g = GesturesUtils.getGestures(mapView);
        g.setScrollEnabled(true);
        g.setPinchToZoomEnabled(true);
        g.setRotateEnabled(true);
        g.setPitchEnabled(true);

        AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(mapView);
        pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(
                annotationPlugin,
                new AnnotationConfig()
        );

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

                Toast.makeText(RegisterActivity.this,
                        "GUARDADO: " + selectedLat + ", " + selectedLon,
                        Toast.LENGTH_SHORT).show();

                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mapView != null) mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mapView != null) mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null) mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) mapView.onLowMemory();
    }
}
