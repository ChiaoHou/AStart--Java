package me.chiaohou.astar;

import me.chiaohou.astar.bmpUtil.BMPReader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class BmpViewerFrame extends JFrame {
    // 展示图片
    private BmpPanel bmpViewerPanel = null;
    // 文件选择器
    private JFileChooser fileChooser = null;
    // 默认宽度
    private static final int DEFAULT_WIDTH = 600;
    // 默认长度
    private static final int DEFAULT_HEIGHT = 600;

    public BmpViewerFrame() {
        super();
        setTitle("最优路径：A* 算法");
        setSize(this.DEFAULT_WIDTH, this.DEFAULT_HEIGHT);


        // Java 提供的文件选择框
        this.fileChooser = new JFileChooser();
        // 设置当前浏览路径
        fileChooser.setCurrentDirectory(new File("."));

        // 设置菜单
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // 菜单名称
        JMenu menu = new JMenu("操作");
        menuBar.add(menu);

        // 选择图片
        JMenuItem selectBmpItem = new JMenuItem("选择图片");
        menu.add(selectBmpItem);
        selectBmpItem.addActionListener(e -> {
            // 后缀过滤，只能选择 BMP 文件
            FileNameExtensionFilter filter = new FileNameExtensionFilter("*.bmp", "bmp");
            fileChooser.setFileFilter(filter);
            fileChooser.setMultiSelectionEnabled(false);

            // 弹出文件选择对话框
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                String fileName = fileChooser.getSelectedFile().getPath();
                if (!fileName.endsWith(".bmp")) {
                    JOptionPane.showMessageDialog(null, "图片类型错误！");
                } else {
                    try {
                        // 显示图片
                        //bmpViewerLabel.setIcon(new ImageIcon(ImageIO.read(new File(fileName))));
                        BMPReader reader = new BMPReader();
                        bmpViewerPanel = new BmpPanel(reader.readBMP(fileName));
                        add(bmpViewerPanel);
                        this.invalidate();
                        this.validate();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "未选择图片！");
            }

        });

        // 开始寻路算法
        JMenuItem startItem = new JMenuItem("开始寻路");
        menu.add(startItem);
        startItem.addActionListener(e -> {

        });

        // 保存图片
        JMenuItem saveBmpItem = new JMenuItem("保存图片");
        menu.add(saveBmpItem);
        saveBmpItem.addActionListener(e -> {

        });

        // 退出
        JMenuItem exitItem = new JMenuItem("退出");
        menu.add(exitItem);
        exitItem.addActionListener(e -> System.exit(0));


    }
    class BmpPanel extends JPanel {
        private int[][] date = null;

        public BmpPanel(int[][] date) {
            this.date = date;
        }

        public void paint(Graphics g) {
            super.paint(g);
            // 读取数据
            // 判断是否存在
            if (date != null) {
                // this.setPreferredSize(new
                // Dimension(date[0].length,date.length));
                this.setPreferredSize(new Dimension(date[0].length, date.length));
                // 遍历
                for (int i = 0; i < date.length; i++) {
                    for (int j = 0; j < date[i].length; j++) {
                        Color c = new Color(date[i][j]);
                        g.setColor(c);
                        g.drawLine(j, date.length - i, j, date.length - i);
                    }
                }
            }
        }
    }
}
