import aoc.DayFactory;

import java.lang.reflect.InvocationTargetException;

public class Main {

    public static final String[] YEARS = new String[]{"2017", "2018"};
    public static final int DAYS_AVAILABLE = 25;

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Running all puzzles");

            for (String year : YEARS) {
                runAllPuzzlesForYear(year);
            }

        } else if (args.length == 1) {

            String year = args[0];

            if (year.matches("\\d{4}")) {
                runAllPuzzlesForYear(year);
            }
        } else if (args.length == 2) {
            String year = args[0];
            String day = args[1];

            if (year.matches("\\d{4}") && day.matches("\\d{1,2}")) {
                runPuzzle(year, day);
            }
        } else if (args.length == 3) {
            String year = args[0];
            String day = args[1];
            String variant = args[2];

            if (year.matches("\\d{4}") && day.matches("\\d{1,2}")) {
                runPuzzle(year, day+variant);
            }
        }
    }

    private static void runAllPuzzlesForYear(String year) {
        System.out.println("*********************************************");
        for (int i = 1; i <= DAYS_AVAILABLE; i++) {
            runPuzzle(year, String.valueOf(i));
        }
    }

    private static void runPuzzle(String year, String day) {
        try {
            DayFactory.getDay(year, day).solve();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            // System.out.println("Could not create class " + day + " for year: " + year);
        } catch (Exception e) {
            System.out.println("Could not solve puzzle " + day + " for year: " + year);
        }
    }
}
