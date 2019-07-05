package com.emsi.myevents.Model;

import com.google.firebase.firestore.Exclude;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


public class Evenement {
    public String id;
    public String name;
    public String start;
    public String end;
    public String description;
    public String location;
    public String type;
    public String uid;
    public float price;
    //public int bail;
    public int seat_number;
    public int seat_free;
    public String organisateur;
    public int logo;

    public static DateFormat dateFormat =
            DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
    public static DateFormat dateFormatter = new SimpleDateFormat("kk:mm dd/MM/yyyy");

    public Evenement(){
    }

    public Evenement(String uid,String name, String start, String end, String type, String description, String location,
                 float price,  int seat_number, String organisateur,int logo){
        this.uid=uid;
        this.name=name;
        this.start=start;
        this.end=end;
        this.type=type;
        this.description=description;
        this.location=location;
        this.price=price;
       // this.bail=bail;
        this.seat_number=seat_number;
        this.seat_free=seat_number;
        this.logo=logo;
        this.organisateur=organisateur;
    }

    public Evenement(String uid, String name, String start, String end, String type,String organisateur,
                 String location, float price, int seat_number, String description){
        this.uid=uid;
        this.name=name;
        this.start=start;
        this.end=end;
        this.type=type;
        this.organisateur=organisateur;
        this.location=location;
        this.price=price;
        this.seat_free=seat_number;
        this.seat_number=seat_number;
        this.description=description;
    }

    /**
     * Two methods to control the number of seats available
     */
    public void reserveSeat(){
        seat_free -= seat_free;
    }

    public void freeSeat(){
        seat_free += seat_free;
    }

    /**
     * Define how data will be stored in database
     */
    @Exclude
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("description", description);
        result.put("type", type);
        result.put("organisateur", organisateur);
        result.put("location", location);
        result.put("price", price);
        result.put("seats number", seat_number);
        result.put("seats free", seat_free);
        result.put("start", start);
        result.put("end", end);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name= ").append(name).append("\n");
        sb.append("organisateur= ").append(organisateur).append("\n");
        sb.append("start= ").append(start).append("\n");
        sb.append("end= ").append(end).append("\n");
        sb.append("seatNumber= ").append(seat_number).append("\n");
        sb.append("seatFree= ").append(seat_free).append("\n");
        return sb.toString();
    }
}