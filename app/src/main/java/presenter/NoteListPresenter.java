package presenter;

import java.util.List;

import model.db.AppDatabase;
import model.domain.Notes;
import view.activity.ListNotesActivity;

public class NoteListPresenter {
    private ListNotesActivity view;
    private AppDatabase db;

    public NoteListPresenter(ListNotesActivity view) {
        this.view = view;
        this.db = AppDatabase.getInstance(view.getApplicationContext());
    }

    public void cargarNotas() {
        new Thread(() -> {
            try {

                List<Notes> listaNotas = db.notesDao().findAll();

                new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                    if (view != null) {
                        view.mostrarNotas(listaNotas);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
