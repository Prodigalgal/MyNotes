package BlueCup;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class S2021No1F {
    public static void main(String[] args) {
        LocalTime lt = LocalTime.MIDNIGHT.plusSeconds((Long.parseLong("46800999") / 1000));
        System.out.println(lt.format(DateTimeFormatter.ISO_LOCAL_TIME));
    }
}
