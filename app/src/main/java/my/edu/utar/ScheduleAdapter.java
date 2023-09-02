package my.edu.utar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import my.edu.utar.R;
import my.edu.utar.ScheduleItem;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private List<ScheduleItem> scheduleItems;
    private OnBookClickListener bookClickListener;

    public ScheduleAdapter(List<ScheduleItem> scheduleItems, OnBookClickListener listener) {
        this.scheduleItems = scheduleItems;
        this.bookClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScheduleItem scheduleItem = scheduleItems.get(position);

        // Bind data to UI elements in the ViewHolder
        holder.textViewScheduleId.setText("Schedule ID: " + scheduleItem.getScheduleId());
        holder.textViewDepartureTime.setText(scheduleItem.getDepartureTime());
        holder.textViewArrivalTime.setText(scheduleItem.getArrivalTime());
        holder.textViewSeatAvailable.setText(scheduleItem.getSeatAvailable());
        holder.textViewBusPlate.setText("Bus Plate: " + scheduleItem.getBusPlate());

        holder.buttonBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bookClickListener != null) {
                    bookClickListener.onBookClick(scheduleItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return scheduleItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewScheduleId;
        TextView textViewDepartureTime;
        TextView textViewArrivalTime;
        TextView textViewSeatAvailable;
        TextView textViewBusPlate;
        Button buttonBook;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewScheduleId = itemView.findViewById(R.id.textViewScheduleId);
            textViewDepartureTime = itemView.findViewById(R.id.textViewDepartureTime);
            textViewArrivalTime = itemView.findViewById(R.id.textViewArrivalTime);
            textViewSeatAvailable = itemView.findViewById(R.id.textViewSeatAvailable);
            textViewBusPlate = itemView.findViewById(R.id.textViewBusPlate);
            buttonBook = itemView.findViewById(R.id.buttonBook);
        }
    }

    public interface OnBookClickListener {
        void onBookClick(ScheduleItem scheduleItem);
    }
}
