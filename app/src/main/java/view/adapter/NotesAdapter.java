package view.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.worklink.R;
import java.util.List;

import view.activity.EditNoteActivity;
import model.domain.Notes;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<Notes> notesList;

    public NotesAdapter(List<Notes> notesList) {
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Notes note = notesList.get(position);
        holder.tvTitle.setText(note.getTitle());
        holder.tvDesc.setText(note.getDescription());

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(v.getContext(), EditNoteActivity.class);


            intent.putExtra("id", note.getId());
            intent.putExtra("titulo", note.getTitle());
            intent.putExtra("desc", note.getDescription());


            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvNoteTitle);
            tvDesc = itemView.findViewById(R.id.tvNoteDesc);
        }
    }
}