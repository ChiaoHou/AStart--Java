package me.chiaohou.astar;


import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            // 实例化 窗口
            JFrame frame = new BmpViewerFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // 禁止改变窗口大小
            frame.setVisible(true);
        });


    }
}
