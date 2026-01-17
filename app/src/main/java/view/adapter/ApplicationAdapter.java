package view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.worklink.R;

import java.util.List;

import model.domain.Application;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ApplicationViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Application application);
    }
    private List<Application> applicationList;
    private OnItemClickListener listener;
    public ApplicationAdapter(List<Application> applicationList, OnItemClickListener listener) {
        this.applicationList = applicationList;
        this.listener = listener;
    }
    public void setData(List<Application> newList) {
        this.applicationList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_aplication, parent, false);
        return new ApplicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationViewHolder holder, int position) {
        Application app = applicationList.get(position);
        holder.bind(app, listener);
    }

    @Override
    public int getItemCount() {
        return applicationList != null ? applicationList.size() : 0;
    }
    static class ApplicationViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate;
        TextView txtTypeStatus;
        TextView txtHours;
        TextView txtUrgent;

        public ApplicationViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.textDate);
            txtTypeStatus = itemView.findViewById(R.id.textTypeStatus);
            txtHours = itemView.findViewById(R.id.textHours);
            txtUrgent = itemView.findViewById(R.id.textUrgent);
        }

        public void bind(final Application app, final OnItemClickListener listener) {


            String created = app.getCreated();
            String from = app.getFromTime();
            String to = app.getToTime();

            txtDate.setText(created != null ? created : "");

            String typeStatus = app.getType() + " - " + app.getStatus();
            txtTypeStatus.setText(typeStatus);

            String hoursText = "Horas: " + app.getHoursRequested() +
                    " (" + (from != null ? from : "--:--") +
                    " - " + (to != null ? to : "--:--") + ")";
            txtHours.setText(hoursText);

            txtUrgent.setText(app.getUrgent() ? "Urgente" : "Normal");

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(app);
                }
            });
        }
    }
}
