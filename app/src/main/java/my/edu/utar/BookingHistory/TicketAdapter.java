package my.edu.utar.BookingHistory;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import my.edu.utar.R;
import my.edu.utar.SQLiteAdapter;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {

    private SQLiteAdapter mySQLiteAdapter;
    private ArrayList<String[]> tickets;
    private ArrayList<String[]> usernames;
    private ArrayList<String[]> schedules;
    private ArrayList<String[]> buses;
    private int positions;

    public TicketAdapter(SQLiteAdapter mySQLiteAdapter, ArrayList<String[]> tickets, ArrayList<String[]> usernames, ArrayList<String[]> schedules, ArrayList<String[]> bus) {
    this.mySQLiteAdapter = mySQLiteAdapter;
    this.tickets = tickets;
    this.usernames = usernames;
    this.schedules = schedules;
    this.buses = bus;
}

    @NonNull
    @Override
    public TicketAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_card_layout, parent, false);
    return new TicketAdapter.ViewHolder(view);
}

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    String[] ticket = tickets.get(position);
    positions = position;

    // Check if position is within bounds for usernames, schedules, and bus
    if (position < usernames.size() && position < schedules.size() && position < buses.size()) {
        String[] username = usernames.get(position);
        String[] schedule = schedules.get(position);
        String[] bus = buses.get(position);

        // Populate the card view with ticket details
        holder.ticketDateTextView.setText("Date: " + ticket[1]); // Replace with appropriate index
        holder.pickupTextView.setText("Depart Location: " + ticket[2]); // Replace with appropriate index
        holder.dropoffTextView.setText("Destination Location: " + ticket[3]); // Replace with appropriate index
        holder.statusTextView.setText("Status: " + ticket[4]);

        // Fetch additional details from SQLite database
        holder.bookingIDText.setText("Booking ID: " + ticket[0]);
        holder.busPlateText.setText("Bus Plate: " + bus[1]);
        holder.userNameText.setText("Name: " + ticket[7]);
        holder.scheduleIDText.setText("Schedule ID: " + schedule[0]);
        holder.scheduleTimeStartText.setText("Depart Time: " + schedule[1]);
        holder.scheduleTimeEndText.setText("Estimated Time Arrival: " + schedule[2]);
    } else {
        // Handle the case where the position is out of bounds
        // Can set default values or show an error message as needed
    }
}

    @Override
    public int getItemCount() {
    return tickets.size();
}

    public class ViewHolder extends RecyclerView.ViewHolder {

        // New TextViews
        public TextView busPlateText;
        public TextView scheduleIDText, scheduleTimeStartText, scheduleTimeEndText;
        public TextView userNameText;
        public TextView bookingIDText;

        public Button editButton, saveUsernameButton, cancelUsernameButton, deleteButton, refundButton, trackButton;
        public EditText edit_username_text;
        public Group additionalInfoLayout;

        TextView ticketDateTextView, pickupTextView, dropoffTextView, statusTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ticketDateTextView = itemView.findViewById(R.id.ticketDateTextView);
            pickupTextView = itemView.findViewById(R.id.pickupTextView);
            dropoffTextView = itemView.findViewById(R.id.dropoffTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);

            // Initialize new TextViews
            bookingIDText = itemView.findViewById(R.id.booking_id_text);
            busPlateText = itemView.findViewById(R.id.bus_plate_text);
            scheduleIDText = itemView.findViewById(R.id.schedule_id_text);
            scheduleTimeStartText = itemView.findViewById(R.id.schedule_time_start_text);
            scheduleTimeEndText = itemView.findViewById(R.id.schedule_time_end_text);
            userNameText = itemView.findViewById(R.id.user_name_text);

            additionalInfoLayout = itemView.findViewById(R.id.additional_info_group);
            additionalInfoLayout.setVisibility(View.GONE);

            //track
            trackButton = itemView.findViewById(R.id.trackBusButton);
            trackButton.setVisibility(View.GONE);

            trackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = itemView.getContext();
                    Intent intent = new Intent(context, my.edu.utar.API.GoogleMaps.class);
                    intent.putExtra("uid", usernames.get(positions)[0]);
                    intent.putExtra("busID", buses.get(positions)[0]);
                    context.startActivity(intent);
                }
            });

            //editing
            editButton = itemView.findViewById(R.id.edit_button);
            editButton.setVisibility(View.GONE);
            edit_username_text = itemView.findViewById(R.id.edit_username_text);
            saveUsernameButton = itemView.findViewById(R.id.save_username_button);
            cancelUsernameButton = itemView.findViewById(R.id.cancel_username_button);

            //delete
            deleteButton = itemView.findViewById(R.id.delete_button);
            deleteButton.setVisibility(View.GONE);
            refundButton = itemView.findViewById(R.id.refund_button);


            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    toggleAdditionalInfo();
                    if ("current".equalsIgnoreCase(tickets.get(getAdapterPosition())[4])) {
                        if (additionalInfoLayout.getVisibility() == View.GONE) {
                            editButton.setVisibility(View.GONE);
                            refundButton.setVisibility(View.GONE);
                            trackButton.setVisibility(View.VISIBLE);
                        } else {
                            editButton.setVisibility(View.VISIBLE);
                            refundButton.setVisibility(View.VISIBLE);
                            trackButton.setVisibility(View.VISIBLE);
                        }
                    }


                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            editButton.setVisibility(editButton.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                            refundButton.setVisibility(refundButton.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                            edit_username_text.setVisibility(edit_username_text.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                            saveUsernameButton.setVisibility(saveUsernameButton.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                            cancelUsernameButton.setVisibility(cancelUsernameButton.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                        }
                    });

                    cancelUsernameButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            editButton.setVisibility(editButton.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                            refundButton.setVisibility(refundButton.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                            edit_username_text.setVisibility(edit_username_text.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                            saveUsernameButton.setVisibility(saveUsernameButton.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                            cancelUsernameButton.setVisibility(cancelUsernameButton.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                        }
                    });

                    // Inside ViewHolder constructor
                    refundButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                // Get the ticket ID (assuming it's at index 0 in the ticket data)
                                final String ticketId = tickets.get(position)[0];

                                // Show an AlertDialog for confirmation
                                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                                builder.setTitle("Delete Ticket");
                                builder.setMessage("Are you sure you want to refund this ticket?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // User clicked "Yes," so delete the ticket
                                        mySQLiteAdapter.deleteBooking(ticketId);

                                        // Show a toast message indicating that the ticket has been deleted
                                        Toast.makeText(itemView.getContext(), "Ticket refunded. Your credit will be refunded in 3-5 working days", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(itemView.getContext(), "Please reenter this page to view the changes.", Toast.LENGTH_LONG).show();
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // User clicked "No," do nothing
                                    }
                                });
                                builder.show();
                            }
                        }
                    });


                    saveUsernameButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Get the edited username from the EditText
                            String editedUsername = edit_username_text.getText().toString().trim(); // Trim to remove leading/trailing spaces

                            // Check if the edited username contains only alphabetic characters
                            if (isAlphabetic(editedUsername)) {
                                // Get the ticket ID (assuming it's at index 0 in the ticket data)
                                String bookingName = tickets.get(getAdapterPosition())[7];

                                // Update the username in the database using your SQLiteAdapter
                                boolean isUpdated = mySQLiteAdapter.updateUsername(bookingName, editedUsername);

                                if (isUpdated) {
                                    // Update the displayed username in the TextView
                                    userNameText.setText("Name: " + editedUsername);

                                    // Notify the adapter that the data has changed
                                    notifyDataSetChanged();

                                    // After updating, hide the EditText and Save Button
                                    editButton.setVisibility(View.VISIBLE);
                                    refundButton.setVisibility(View.VISIBLE);
                                    edit_username_text.setVisibility(View.GONE);
                                    saveUsernameButton.setVisibility(View.GONE);
                                    cancelUsernameButton.setVisibility(View.GONE);

                                    // Show a toast message indicating that the username has been updated
                                    Toast.makeText(itemView.getContext(), "Username updated", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(itemView.getContext(), "Please reenter this page to view the changes.", Toast.LENGTH_LONG).show();
                                } else {
                                    // Handle the case where the update failed (e.g., show an error message)
                                    Toast.makeText(itemView.getContext(), "Update Error", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Username contains non-alphabetic characters, show an error toast
                                Toast.makeText(itemView.getContext(), "Enter Full Name", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }

                // Helper function to check if a string contains only alphabetic characters
                private boolean isAlphabetic(String str) {
                    return str.matches("[a-zA-Z]+$");
                }


                private void toggleAdditionalInfo() {
                    TransitionManager.beginDelayedTransition((ViewGroup) itemView, new ChangeBounds());
                    additionalInfoLayout.setVisibility(
                            additionalInfoLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE
                    );

                    if ("Past".equalsIgnoreCase(tickets.get(getAdapterPosition())[4])) {
                        // Show the delete button only when the ticket status is "past"
                        deleteButton.setVisibility(additionalInfoLayout.getVisibility() == View.VISIBLE ? View.VISIBLE : View.GONE);

                        // Inside ViewHolder constructor
                        deleteButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int position = getAdapterPosition();
                                if (position != RecyclerView.NO_POSITION) {
                                    // Get the ticket ID (assuming it's at index 0 in the ticket data)
                                    final String ticketId = tickets.get(position)[0];

                                    // Show an AlertDialog for confirmation
                                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                                    builder.setTitle("Delete Ticket");
                                    builder.setMessage("Are you sure you want to delete this ticket?");
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            // User clicked "Yes," so delete the ticket
                                            mySQLiteAdapter.deleteBooking(ticketId);

                                            // Show a toast message indicating that the ticket has been deleted
                                            Toast.makeText(itemView.getContext(), "PAST Ticket deleted", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(itemView.getContext(), "Please reenter this page to view the changes.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            // User clicked "No," do nothing
                                        }
                                    });
                                    builder.show();
                                }
                            }
                        });
                    }

                    if (additionalInfoLayout.getVisibility() == View.GONE) {
                        edit_username_text.setVisibility(View.GONE);
                        saveUsernameButton.setVisibility(View.GONE);
                        cancelUsernameButton.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
}
