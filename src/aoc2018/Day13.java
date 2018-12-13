package aoc2018;

import aoc.Day;
import aoc2018.helper.Car;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day13 extends Day {

    private char[][] tracks;
    List<Car> cars;
    private int carIndex = 0;

    public Day13(String year, String day) {
        super(year, day);
    }

    @Override
    protected void part1(List<String> inputs) {
        buildTrack(inputs);

        printTrack();

        Point crashSite;

        trackOnline:
        while (true) {
            Collections.sort(cars, Car.getPositionComparator());

            for (Car car : cars) {
                Point position = car.move();
                if (checkCrash(car)) {
                    crashSite = position;
                    break trackOnline;
                }
                car.checkTurn(tracks[position.x][position.y]);
            }
        }

        printSolution(1, crashSite.x + "," + crashSite.y);
    }

    @Override
    protected void part2(List<String> inputs) {
        buildTrack(inputs);

        while (true) {
            cars = cars.stream().filter(c -> !c.isBroken()).collect(Collectors.toList());
            Collections.sort(cars, Car.getPositionComparator());

            if (cars.size() == 1) {
                break;
            }

            for (Car car : cars) {
                if (car.isBroken()) {
                    continue;
                }

                Point position = car.move();
                if (checkCrash(car)) {
                    continue;
                }
                car.checkTurn(tracks[position.x][position.y]);
            }
        }

        Car remainingCar = cars.get(0);
        printSolution(2, remainingCar.getX() + "," + remainingCar.getY());
    }

    private void printTrack() {
        for (int y = 0; y < tracks[0].length; y++) {
            String row = "";
            for (int x = 0; x < tracks.length; x++) {
                row += tracks[x][y];
            }
            System.out.println(row);
        }
    }

    private boolean checkCrash(Car car) {
        List<Car> otherCars = cars
                .stream()
                .filter(c -> c.getIndex() != car.getIndex())
                .collect(Collectors.toList());

        for (Car otherCar : otherCars) {
            if (otherCar.getX() == car.getX() && otherCar.getY() == car.getY()) {
                car.setBroken(true);
                otherCar.setBroken(true);
                return true;
            }
        }

        return false;
    }


    private void buildTrack(List<String> inputs) {
        cars = new ArrayList<>();
        int maxWidth = inputs.stream().mapToInt(s -> s.length()).max().orElse(0);
        tracks = new char[maxWidth][inputs.size()];

        int y = 0;
        for (String input : inputs) {
            for (int x = 0; x < maxWidth; x++) {
                char trackTile;
                try {
                    trackTile = input.charAt(x);
                } catch (StringIndexOutOfBoundsException e) {
                    trackTile = ' ';
                }
                if (Car.isCar(trackTile)) {
                    Car car = new Car(carIndex++, x, y, trackTile);
                    tracks[x][y] = car.getInitialTrackTile();
                    cars.add(car);
                    continue;
                }

                tracks[x][y] = trackTile;
            }

            y++;
        }
    }
}
