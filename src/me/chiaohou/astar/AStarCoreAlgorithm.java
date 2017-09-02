package me.chiaohou.astar;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class AStarCoreAlgorithm {
    private int[][] date = null;
    private String[][] maz = null;
    private int[][] mazResult = null;
    private static final int MOVED_COLOR = 41704;
    private static final int ROUTE_COLOR = 16777215;
    private static final int OBSTACLE_COLOR = 15539236;

    private static final String MOVED_SYMBOL = "@";
    private static final String ROUTE_SYMBOL = "*";
    private static final String OBSTACLE_SYMBOL = "|";

    public static final int STEP = 30;

    private ArrayList<Node> openList = new ArrayList<Node>();
    private ArrayList<Node> closeList = new ArrayList<Node>();


    public AStarCoreAlgorithm(int[][] date) {
        this.date = flipUpAndDown(date);
    }

    public int[][] start() throws InterruptedException {
        narrowBmpDate();

        // 打印原始迷宫
        printMaz(this.maz);

        // 开始解密
        Node startNode = new Node(0, 0);
        Node endNode = new Node(9, 9);
        Node parent = this.findPath(startNode, endNode);

        ArrayList<Node> arrayList = new ArrayList<Node>();

        while (parent != null) {
            arrayList.add(new Node(parent.x, parent.y));
            parent = parent.parent;
        }
        for (int i = 0; i < this.maz.length; i++) {
            for (int j = 0; j < this.maz[i].length; j++) {
                if (exists(arrayList, i, j)) {
                    this.maz[i][j] = MOVED_SYMBOL;
                } else {
                    continue;
                }

            }
        }

        // 打印解答好的迷宫
        printMaz(this.maz);


        antiNarrowBmpDate();

        return flipUpAndDown(this.mazResult);
    }

    private void antiNarrowBmpDate() {
        /***
         * 将迷宫转换为 BMP 数组
         */
        // 初始化
        this.mazResult = new int[this.date.length][this.date[0].length];
        for (int i = 0; i < this.mazResult.length; i++) {
            for (int j = 0; j < this.mazResult[i].length; j++) {
                this.mazResult[i][j] = 0;
            }
        }

        for (int i = 0; i < this.maz.length; i++) {
            for (int j = 0; j < this.maz[i].length; j++) {
                int color = 0;
                if (this.maz[i][j] == MOVED_SYMBOL) {
                    color = MOVED_COLOR;
                } else if (this.maz[i][j] == ROUTE_SYMBOL) {
                    color = ROUTE_COLOR;
                } else if (this.maz[i][j] == OBSTACLE_SYMBOL) {
                    color = OBSTACLE_COLOR;
                }
                for (int k = i * 50 + 3; k < i * 50 + 3 + 43; k++) {
                    for (int l = j * 50 + 3; l < j * 50 + 3 + 43; l++) {
                        this.mazResult[k][l] = color;
                    }
                }
            }

        }
    }

    private void narrowBmpDate() {
        /***
         * 将 BMP 数组 转换为二维数组
         */
        this.maz = new String[10][10];
        int row = 0;
        int column = 0;
        for (int i = 3; i < this.date.length - 1; i += 50) {
            for (int j = 3; j < this.date[i].length; j += 50) {
                if (date[i][j] == MOVED_COLOR) {
                    maz[row][column] = ROUTE_SYMBOL;
                } else if (this.date[i][j] == ROUTE_COLOR) {
                    maz[row][column] = ROUTE_SYMBOL;
                } else if (this.date[i][j] == OBSTACLE_COLOR) {
                    maz[row][column] = OBSTACLE_SYMBOL;
                }
                column += 1;
            }
            row += 1;
            column = 0;
        }
    }

    private void printMaz(String[][] mazArray) {
        /***
         * 打印数组
         */
        for (int i = 0; i < mazArray.length; i++) {
            for (int j = 0; j < mazArray[i].length; j++) {
                System.out.print(mazArray[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println();
    }

    private int[][] flipUpAndDown(int[][] array) {
        int[][] temp = new int[array.length][array[0].length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                temp[i][j] = array[array.length - 1 - i][j];
            }
        }

        return temp;
    }

    public Node findMinFNodeInOpenList() {
        Node tempNode = openList.get(0);
        for (Node node : openList) {
            if (node.F < tempNode.F) {
                tempNode = node;
            }
        }
        return tempNode;
    }

    public ArrayList<Node> findNeighborNodes(Node currentNode) {
        ArrayList<Node> arrayList = new ArrayList<Node>();
        // 只考虑上下左右，不考虑斜对角
        int topX = currentNode.x;
        int topY = currentNode.y - 1;
        if (canReach(topX, topY) && !exists(closeList, topX, topY)) {
            arrayList.add(new Node(topX, topY));
        }
        int bottomX = currentNode.x;
        int bottomY = currentNode.y + 1;
        if (canReach(bottomX, bottomY) && !exists(closeList, bottomX, bottomY)) {
            arrayList.add(new Node(bottomX, bottomY));
        }
        int leftX = currentNode.x - 1;
        int leftY = currentNode.y;
        if (canReach(leftX, leftY) && !exists(closeList, leftX, leftY)) {
            arrayList.add(new Node(leftX, leftY));
        }
        int rightX = currentNode.x + 1;
        int rightY = currentNode.y;
        if (canReach(rightX, rightY) && !exists(closeList, rightX, rightY)) {
            arrayList.add(new Node(rightX, rightY));
        }
        return arrayList;
    }

    public boolean canReach(int x, int y) {
        if (x >= 0 && x < maz.length && y >= 0 && y < maz[0].length) {
            return maz[x][y] == ROUTE_SYMBOL;
        }
        return false;
    }

    public Node findPath(Node startNode, Node endNode) {

        // 把起点加入 open list
        openList.add(startNode);

        while (openList.size() > 0) {
            // 遍历 open list ，查找 F 值最小的节点，把它作为当前要处理的节点
            Node currentNode = findMinFNodeInOpenList();
            // 从 open list 中移除
            openList.remove(currentNode);
            // 把这个节点移到 close list
            closeList.add(currentNode);

            ArrayList<Node> neighborNodes = findNeighborNodes(currentNode);
            for (Node node : neighborNodes) {
                if (exists(openList, node)) {
                    foundPoint(currentNode, node);
                } else {
                    notFoundPoint(currentNode, endNode, node);
                }
            }
            if (find(openList, endNode) != null) {
                return find(openList, endNode);
            }
        }

        return find(openList, endNode);
    }

    private void foundPoint(Node tempStart, Node node) {
        int G = calcG(tempStart, node);
        if (G < node.G) {
            node.parent = tempStart;
            node.G = G;
            node.calcF();
        }
    }

    private void notFoundPoint(Node tempStart, Node end, Node node) {
        node.parent = tempStart;
        node.G = calcG(tempStart, node);
        node.H = calcH(end, node);
        node.calcF();
        openList.add(node);
    }

    private int calcG(Node start, Node node) {
        int G = STEP;
        int parentG = node.parent != null ? node.parent.G : 0;
        return G + parentG;
    }

    private int calcH(Node end, Node node) {
        int step = Math.abs(node.x - end.x) + Math.abs(node.y - end.y);
        return step * STEP;
    }

    public static Node find(List<Node> nodes, Node point) {
        for (Node n : nodes)
            if ((n.x == point.x) && (n.y == point.y)) {
                return n;
            }
        return null;
    }

    public static boolean exists(List<Node> nodes, Node node) {
        for (Node n : nodes) {
            if ((n.x == node.x) && (n.y == node.y)) {
                return true;
            }
        }
        return false;
    }

    public static boolean exists(List<Node> nodes, int x, int y) {
        for (Node n : nodes) {
            if ((n.x == x) && (n.y == y)) {
                return true;
            }
        }
        return false;
    }

    public static class Node {
        public int x;
        public int y;

        public int F;
        public int G;
        public int H;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void calcF() {
            this.F = this.G + this.H;
        }

        public Node parent;
    }
}
