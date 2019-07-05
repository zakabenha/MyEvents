package com.emsi.myevents.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.emsi.myevents.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.emsi.myevents.Model.Organisateur;
import com.emsi.myevents.Model.Evenement;

public class HomeActivity extends AppCompatActivity {
    private ListView listEvents;
    private ArrayList<HashMap<String, Object>> listValuesEvents = new ArrayList<>();
    private SimpleAdapter adapter;
    private List<Evenement> listeEvenements = new ArrayList<>();
    private List<Evenement> listEventSort = new ArrayList<>();
    private HashMap<String, String> listOrganisateur= new HashMap<>();
    private final SimpleDateFormat inputDate  = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.FRANCE);
    private final SimpleDateFormat simpleDate  = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
    private final SimpleDateFormat outputDate = new SimpleDateFormat("EEEE d MMM à HH:mm", Locale.FRANCE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        loadEventInBackground();
    }

    public void launchEventPage() {
        listEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), EventInfosActivity.class);
                Evenement event = listEventSort.get(position);

                intent.putExtra("eventUID", event.uid);
                intent.putExtra("eventName", event.name);
                intent.putExtra("eventEndDate", event.end);
                startActivity(intent);
            }
        });
    }

    public Boolean convertToDate(String eventDate) {
        DateFormat dateFormatter = new SimpleDateFormat("kk:mm dd/MM/yyyy");
        Date start;
        Date today;
        Calendar c = Calendar.getInstance();
        try {
            start = dateFormatter.parse(eventDate);
            today = dateFormatter.parse(dateFormatter.format(c.getTime()));
            if (start.after(today)) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public Boolean compareDate(String event1, String event2) {
        DateFormat dateFormatter = new SimpleDateFormat("kk:mm dd/MM/yyyy");
        Date myEvent;
        Date eventNext;
        try {
            myEvent = dateFormatter.parse(event1);
            eventNext = dateFormatter.parse(event2);
            if (myEvent.before(eventNext)) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public void loadEventInBackground() {
        DatabaseReference references = FirebaseDatabase.getInstance().getReference("organisateur");
        references.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataS) {
                if (dataS.exists()) {
                    for (DataSnapshot e : dataS.getChildren()) {
                        Organisateur a = e.getValue(Organisateur.class);
                        a.id = e.getKey();
                        if (listOrganisateur.get(a.id) == null)
                            listOrganisateur.put(a.id, a.name);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("events");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int tailleList;
                int nbEvent;
                int eventRestant;

                if (dataSnapshot.exists()) {
                    for (DataSnapshot e : dataSnapshot.getChildren()) {
                        Evenement event = e.getValue(Evenement.class);
                        event.uid = e.getKey();
                        if (convertToDate(event.start) || convertToDate(event.end)) {
                            listeEvenements.add(event);
                        }
                    }
                    eventRestant = listeEvenements.size();
                    while (eventRestant > 0) {
                        tailleList = listeEvenements.size();
                        for (int i = 0; i < tailleList; i++) {
                            nbEvent = 1;
                            for (int j = 0; j < tailleList; j++) {
                                if (i != j && compareDate(listeEvenements.get(i).start, listeEvenements.get(j).start)) {
                                    nbEvent++;
                                }
                            }
                            if (nbEvent == eventRestant) {
                                listEventSort.add(listeEvenements.get(i));
                                eventRestant--;
                            }
                        }
                    }



                    for (Evenement event : listEventSort) {
                        HashMap<String, Object> hashMapValuesEvent = new HashMap<>();
                        hashMapValuesEvent.put("nameEvent", event.name);
                        hashMapValuesEvent.put("association", listOrganisateur.get(event.organisateur));
                        try {
                            hashMapValuesEvent.put("dateEventBegin",
                                    outputDate.format(inputDate.parse(event.start)));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        hashMapValuesEvent.put("locationEvent", event.location);
                        listValuesEvents.add(hashMapValuesEvent);
                    }
                    String[] from = new String[]{"nameEvent",
                            "organisateur",
                            "dateEventBegin",
                            "locationEvent"};
                    int[] to = new int[]{R.id.content_list_events_name_event,
                            R.id.content_list_events_name_association,
                            R.id.content_list_events_date_event_begin,
                            R.id.content_list_events_location_event};

                    listEvents = (ListView) findViewById(R.id.activity_list_events_list);
                    adapter = new SimpleAdapter(HomeActivity.this, listValuesEvents, R.layout.content_list_events, from, to);
                    listEvents.setAdapter(adapter);
                    launchEventPage();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error : ", "onCancelled", databaseError.toException());
            }
        });
    }
}
