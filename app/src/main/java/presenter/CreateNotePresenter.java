package presenter;

import com.example.worklink.R;

import model.db.AppDatabase;
import model.domain.Notes;
import view.activity.CreateNotesActivity;

public class CreateNotePresenter {
    private CreateNotesActivity view;
    private AppDatabase db;

    public CreateNotePresenter(CreateNotesActivity view) {
        this.view = view;

        this.db = AppDatabase.getInstance(view.getApplicationContext());
    }

    public void guardarNota(String titulo, String descripcion) {
        if (titulo.isEmpty() || descripcion.isEmpty()) {
            if (view != null) view.mostrarError(view.getString(R.string.rellena_todos_los_campos));
            return;
        }

        new Thread(() -> {
            try {
                Notes nuevaNota = new Notes();
                nuevaNota.setTitle(titulo);
                nuevaNota.setDescription(descripcion);

                db.notesDao().insert(nuevaNota);

                new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                    if (view != null && !view.isFinishing()) {
                        view.onNotaGuardada();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}