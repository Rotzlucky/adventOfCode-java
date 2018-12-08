package aoc2018.helper;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private int numChildren;
    private int numMetaData;
    private int size;

    private List<Node> children = new ArrayList<>();
    private List<Integer> metaData = new ArrayList<>();

    public int getNumChildren() {
        return numChildren;
    }

    public void setNumChildren(int numChildren) {
        this.numChildren = numChildren;
    }

    public int getNumMetaData() {
        return numMetaData;
    }

    public void setNumMetaData(int numMetaData) {
        this.numMetaData = numMetaData;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public List<Integer> getMetaData() {
        return metaData;
    }

    public void setMetaData(List<Integer> metaData) {
        this.metaData = metaData;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void addChild(Node node) {
        children.add(node);
    }
}
