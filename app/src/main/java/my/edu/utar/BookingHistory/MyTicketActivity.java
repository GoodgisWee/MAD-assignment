package my.edu.utar.BookingHistory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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

import my.edu.utar.BookingPage.BookingPage;
import my.edu.utar.R;
import my.edu.utar.Database.SQLiteAdapter;

public class MyTicketActivity extends AppCompatActivity {

    private SQLiteAdapter mySQLiteAdapter;
    private RecyclerView recyclerView;
    private static TicketAdapter ticketAdapter;
    private ArrayList<String[]> ticketList;
    private ImageButton homeBtn, bookingBtn, profileBtn;
    private ArrayList<String[]> username;
    private ArrayList<String[]> schedule;
    private ArrayList<String[]> bus;
    private String uid, busID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ticket);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        busID = intent.getStringExtra("busID");

        //initialize database connection
        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToRead();
        ticketList = mySQLiteAdapter.readBookingByCondition("userID", uid);
        username = mySQLiteAdapter.readUser();
        schedule = mySQLiteAdapter.readScheduleDetails();
        bus = mySQLiteAdapter.readBus();

        //initialize recyle view
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize and set up the RecyclerView adapter
        ticketAdapter = new TicketAdapter(mySQLiteAdapter,ticketList, username, schedule, bus);
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
                Intent intent = new Intent(MyTicketActivity.this, my.edu.utar.BookingPage.BookingPage.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
                finish();
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyTicketActivity.this, my.edu.utar.profile.userProfilePage.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
                finish();
            }
        });

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    CurrentTicketFragment currentFragment = new CurrentTicketFragment();
                    currentFragment.setSQLiteAdapter(mySQLiteAdapter);
                    Bundle bundle = new Bundle();
                    AllTicketsWrapper allTicketsWrapper = new AllTicketsWrapper(ticketList, username, schedule, bus);
                    bundle.putSerializable("allTickets", allTicketsWrapper);
                    currentFragment.setArguments(bundle);
                    return currentFragment;
                } else {
                    PastTicketFragment pastFragment = new PastTicketFragment();
                    pastFragment.setSQLiteAdapter(mySQLiteAdapter);
                    Bundle bundle = new Bundle();
                    AllTicketsWrapper allTicketsWrapper = new AllTicketsWrapper(ticketList, username, schedule, bus);
                    bundle.putSerializable("allTickets", allTicketsWrapper);
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
    }

    private void updateFragmentsWithFilteredTickets(ArrayList<String[]> filteredTickets) {
        CurrentTicketFragment currentFragment = new CurrentTicketFragment();
        currentFragment.setSQLiteAdapter(mySQLiteAdapter);
        Bundle currentBundle = new Bundle();
        currentBundle.putSerializable("allTickets", filteredTickets);
        currentFragment.setArguments(currentBundle);

        PastTicketFragment pastFragment = new PastTicketFragment();
        pastFragment.setSQLiteAdapter(mySQLiteAdapter);
        Bundle pastBundle = new Bundle();
        pastBundle.putSerializable("allTickets", filteredTickets);
        pastFragment.setArguments(pastBundle);
    }


}