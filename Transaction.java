import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.Console;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;
import java.text.ParseException;

import java.util.Calendar;

public class Transaction {
    private String id;
    private String date;
    private double price;
    private List<TicketInfo> tickets;

    public Transaction(String id, String date, double price, List<TicketInfo> tickets) {
        this.id = id;
        this.date = date;
        this.price = price;
        this.tickets = tickets;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<TicketInfo> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketInfo> tickets) {
        this.tickets = tickets;
    }
    
}
