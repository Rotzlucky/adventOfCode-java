package aoc2018;

import aoc.Day;
import aoc2018.helper.Node;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day8 extends Day {
    
    public Day8(String year, String day) {
        super(year, day);
    }

    @Override
    protected void part1(List<String> inputs) {
        String input = inputs.get(0);

        List<Integer> bytes = Arrays.asList(input.split(" "))
                .stream()
                .map(s -> Integer.parseInt(s))
                .collect(Collectors.toList());

        Node node = parseBytes(bytes);

        int count = countMetaData(node);

        printSolution(1, count);
    }

    @Override
    protected void part2(List<String> inputs) {
        String input = inputs.get(0);

        List<Integer> bytes = Arrays.asList(input.split(" "))
                .stream()
                .map(s -> Integer.parseInt(s))
                .collect(Collectors.toList());

        Node node = parseBytes(bytes);

        int nodeValue = calculateNodeValue(node);

        printSolution(2, nodeValue);
    }

    private int calculateNodeValue(Node node) {
        int value;

        if (node.getChildren().isEmpty()) {
            value = node.getMetaData()
                    .stream()
                    .mapToInt(i -> i.intValue()).sum();
        } else {
            value = node.getMetaData()
                    .stream()
                    .filter(metaDatum -> metaDatum <= node.getChildren().size())
                    .mapToInt(metaDatum -> calculateNodeValue(node.getChildren().get(metaDatum - 1)))
                    .sum();
        }

        return value;
    }

    private int countMetaData(Node node) {
        int sum = node.getMetaData().stream().mapToInt(i -> i.intValue()).sum();

        for (Node child : node.getChildren()) {
            sum += countMetaData(child);
        }

        return sum;
    }

    private Node parseBytes(List<Integer> bytes) {
        Node node = new Node();

        node.setNumChildren(bytes.get(0));
        node.setNumMetaData(bytes.get(1));

        int parsedBytes = 2;
        List<Integer> contentBytes = bytes.subList(parsedBytes, bytes.size());

        for (int i = 0; i < node.getNumChildren(); i++) {
            Node childNode = parseBytes(contentBytes);

            int size = childNode.getSize();
            parsedBytes += size;

            contentBytes = contentBytes.subList(size, contentBytes.size());
            node.addChild(childNode);
        }

        node.setMetaData(bytes.subList(parsedBytes, parsedBytes + node.getNumMetaData()));
        parsedBytes += node.getNumMetaData();
        node.setSize(parsedBytes);

        return node;
    }
}
