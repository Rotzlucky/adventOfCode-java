package aoc;

import java.lang.reflect.InvocationTargetException;

public class DayFactory {
    public static Day getDay(String year, String day)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        String className = "aoc" + year + ".Day" + day;

        return (Day) Class.forName(className).getConstructor(String.class, String.class).newInstance(year, day);
    }
}
