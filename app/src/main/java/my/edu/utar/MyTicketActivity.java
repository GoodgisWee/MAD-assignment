package my.edu.utar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import my.edu.utar.profile.userProfilePage;

public class MyTicketActivity extends AppCompatActivity {

    private SQLiteAdapter mySQLiteAdapter;
    private RecyclerView recyclerView;
    private static TicketAdapter ticketAdapter;
    private ArrayList<String[]> ticketList;
    private ImageButton homeBtn, bookingBtn, profileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ticket);

        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");

        //initialize database connection
        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToRead();
        ticketList = mySQLiteAdapter.readBookingByCondition("userID", uid);

        //initialize recyle view
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize and set up the RecyclerView adapter
        ticketAdapter = new TicketAdapter(ticketList);
        recyclerView.setAdapter(ticketAdapter);

        // Inside MyTicketActivity onCreate method
        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabs);

        //Bottom Bar Navigation
        homeBtn = findViewById(R.id.homeBtn);
        bookingBtn = findViewById(R.id.bookingBtn);
        profileBtn = findViewById(R.id.profileBtn);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyTicketActivity.this, BookingPage.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyTicketActivity.this, my.edu.utar.profile.userProfilePage.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
            }
        });

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    CurrentTicketFragment currentFragment = new CurrentTicketFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("allTickets", ticketList);
                    currentFragment.setArguments(bundle);
                    return currentFragment;
                } else {
                    PastTicketFragment pastFragment = new PastTicketFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("allTickets", ticketList);
                    pastFragment.setArguments(bundle);
                    return pastFragment;
                }
            }

            @Override
            public int getCount() {
                return 2; // Number of tabs
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0) {
                    return "Current";
                } else {
                    return "Past";
                }
            }
        };

        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        TextView noTicketsTextView = findViewById(R.id.noTicketsTextView);
    }

    private void updateFragmentsWithFilteredTickets(ArrayList<String[]> filteredTickets) {
        CurrentTicketFragment currentFragment = new CurrentTicketFragment();
        Bundle currentBundle = new Bundle();
        currentBundle.putSerializable("allTickets", filteredTickets);
        currentFragment.setArguments(currentBundle);

        PastTicketFragment pastFragment = new PastTicketFragment();
        Bundle pastBundle = new Bundle();
        pastBundle.putSerializable("allTickets", filteredTickets);
        pastFragment.setArguments(pastBundle);
    }


    private ArrayList<String[]> filterTickets(ArrayList<String[]> tickets, String keyword) {
        ArrayList<String[]> filteredTickets = new ArrayList<>();
        for (String[] ticket : tickets) {
            boolean containsKeyword = false;
            for (String field : ticket) {
                if (field.toLowerCase().contains(keyword.toLowerCase())) {
                    containsKeyword = true;
                    break;
                }
            }
            if (containsKeyword) {
                filteredTickets.add(ticket);
            }
        }
        return filteredTickets;
    }

    public static class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {

        private ArrayList<String[]> tickets;

        public TicketAdapter(ArrayList<String[]> tickets) {
            this.tickets = tickets;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_card_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String[] ticket = tickets.get(position);

            // Populate the card view with ticket details
            holder.ticketDateTextView.setText(ticket[1]); // Replace with appropriate index
            holder.pickupTextView.setText(ticket[2]); // Replace with appropriate index
            holder.dropoffTextView.setText(ticket[3]); // Replace with appropriate index
            holder.statusTextView.setText(ticket[4]);
        }

        @Override
        public int getItemCount() {
            return tickets.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView ticketDateTextView, pickupTextView, dropoffTextView, statusTextView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                ticketDateTextView = itemView.findViewById(R.id.ticketDateTextView);
                pickupTextView = itemView.findViewById(R.id.pickupTextView);
                dropoffTextView = itemView.findViewById(R.id.dropoffTextView);
                statusTextView = itemView.findViewById(R.id.statusTextView);

            }
        }
    }

    public static class CurrentTicketFragment extends Fragment {

        private ArrayList<String[]> currentTickets;
        private RecyclerView recyclerView;
        private TicketAdapter ticketAdapter;

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_current_ticket, container, false);

            // Retrieve the list of all tickets from the arguments bundle
            ArrayList<String[]> allTickets = (ArrayList<String[]>) getArguments().getSerializable("allTickets");

            // Initialize RecyclerView
            recyclerView = rootView.findViewById(R.id.currentTicketRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            // Filter and display current tickets
            currentTickets = filterTicketsByStatus(allTickets, "current");

            // Initialize and set up the RecyclerView adapter
            ticketAdapter = new TicketAdapter(currentTickets);
            recyclerView.setAdapter(ticketAdapter);

            return rootView;
        }

        private ArrayList<String[]> filterTicketsByStatus(ArrayList<String[]> tickets, String status) {
            ArrayList<String[]> filteredTickets = new ArrayList<>();
            for (String[] ticket : tickets) {
                if ("current".equalsIgnoreCase(ticket[4])) {
                    filteredTickets.add(ticket);
                }
            }
            return filteredTickets;
        }
    }

    public static class PastTicketFragment extends Fragment {

        private ArrayList<String[]> pastTickets;
        private RecyclerView recyclerView;
        private TicketAdapter ticketAdapter;

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_past_ticket, container, false);

            // Retrieve the list of all tickets from the arguments bundle
            ArrayList<String[]> allTickets = (ArrayList<String[]>) getArguments().getSerializable("allTickets");

            // Initialize RecyclerView
            recyclerView = rootView.findViewById(R.id.pastTicketRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            // Filter and display past tickets
            pastTickets = filterTicketsByStatus(allTickets, "past");

            // Initialize and set up the RecyclerView adapter
            ticketAdapter = new TicketAdapter(pastTickets);
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
    }
}