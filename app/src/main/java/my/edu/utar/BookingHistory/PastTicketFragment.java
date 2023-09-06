package my.edu.utar.BookingHistory;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import my.edu.utar.R;
import my.edu.utar.Database.SQLiteAdapter;

public class PastTicketFragment extends Fragment {

    private ArrayList<String[]> pastTickets;
    private RecyclerView recyclerView;
    private TicketAdapter ticketAdapter;

    private SQLiteAdapter mySQLiteAdapter;
    private ArrayList<String[]> username;
    private ArrayList<String[]> schedule;
    private ArrayList<String[]> busplate;

    public PastTicketFragment() {
        // Default constructor with no arguments.
    }


    @SuppressLint("ValidFragment")
    public PastTicketFragment(ArrayList<String[]> username, ArrayList<String[]> schedule, ArrayList<String[]> busplate) {
        this.username = username;
        this.schedule = schedule;
        this.busplate = busplate;
    }

    public void setSQLiteAdapter(SQLiteAdapter mySQLiteAdapter) {
        this.mySQLiteAdapter = mySQLiteAdapter;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_past_ticket, container, false);

        // Retrieve the list of all tickets from the arguments bundle
        /*ArrayList<String[]> allTickets = (ArrayList<String[]>) getArguments().getSerializable("allTickets");*/
        AllTicketsWrapper allTicketsWrapper = (AllTicketsWrapper) getArguments().getSerializable("allTickets");
        ArrayList<String[]> allTickets = allTicketsWrapper.ticketList;
        ArrayList<String[]> username = allTicketsWrapper.username;
        ArrayList<String[]> schedule = allTicketsWrapper.schedule;
        ArrayList<String[]> busplate = allTicketsWrapper.busplate;

        // Initialize RecyclerView
        recyclerView = rootView.findViewById(R.id.pastTicketRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Filter and display past tickets
        pastTickets = filterTicketsByStatus(allTickets, "past");

        // Initialize and set up the RecyclerView adapter
        ticketAdapter = new TicketAdapter(mySQLiteAdapter, pastTickets, username, schedule, busplate);
        recyclerView.setAdapter(ticketAdapter);

        return rootView;
    }

    private ArrayList<String[]> filterTicketsByStatus(ArrayList<String[]> tickets, String status) {
        ArrayList<String[]> filteredTickets = new ArrayList<>();
        for (String[] ticket : tickets) {
            if ("past".equalsIgnoreCase(ticket[4])) {
                filteredTickets.add(ticket);
            }
        }
        return filteredTickets;
    }

    // Inside PastTicketFragment
    public void updateTickets(ArrayList<String[]> filteredTickets) {
        pastTickets.clear();
        pastTickets.addAll(filteredTickets);
        ticketAdapter.notifyDataSetChanged();
    }
}
