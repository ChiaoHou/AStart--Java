package me.chiaohou.astar;

import me.chiaohou.astar.bmpUtil.BMPReader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BmpViewerFrame extends JFrame {
    // 展示图片
    private BmpPanel bmpViewerPanel = null;

    // 文件选择器
    private JFileChooser fileChooser = null;

    // 窗口的宽高度，从 BMP 读取数据后
    // 默认为 20
    private int frameWidth = 60;
    private int frameHeight = 60;

    private BMPReader bmpReader = null;

    // 选取的 BMP 路径
    private String fileName = null;

    public BmpViewerFrame() {
        super();
        // 设置标题
        this.setTitle("最优路径：A* 算法");

        // 设置窗口大小
        this.setSize(this.frameWidth, this.frameHeight);

        // Java 提供的文件选择框
        this.fileChooser = new JFileChooser();

        // 设置选择框的默认路径为当前路径
        fileChooser.setCurrentDirectory(new File("."));

        // 添加菜单
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // 菜单名称
        JMenu menu = new JMenu("操作");
        menuBar.add(menu);

        // 「选择图片」选项
        JMenuItem selectBmpItem = new JMenuItem("选择图片");
        menu.add(selectBmpItem);
        selectBmpItem.addActionListener(e -> {
            // 文件过滤，只能选择 BMP 文件
            FileNameExtensionFilter filter = new FileNameExtensionFilter("*.bmp", "bmp");
            fileChooser.setFileFilter(filter);
            fileChooser.setMultiSelectionEnabled(false);

            // 弹出文件选择对话框
            int result = fileChooser.showOpenDialog(null);
            // 判断是否选择了文件
            if (result == JFileChooser.APPROVE_OPTION) {
                fileName = fileChooser.getSelectedFile().getPath();
                // 判断是否选择了 *.BMP 文件
                if (!fileName.endsWith(".bmp")) {
                    JOptionPane.showMessageDialog(null, "图片类型错误！");
                } else {
                    try {
                        bmpReader = new BMPReader();
                        int[][] bmpDate = bmpReader.readBMP(fileName);
                        // 更新窗口大小，加上偏差
                        this.frameWidth = bmpReader.getBmpWidth() + 20;
                        this.frameHeight = bmpReader.getBmpHeight() + 65;
                        // 刷新 Panel
                        freshBmpPanel(bmpDate);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "未选择图片！");
            }

        });

        // 「开始寻路」选项
        JMenuItem startItem = new JMenuItem("开始寻路");
        menu.add(startItem);
        startItem.addActionListener(e -> {
            if (fileName == null || bmpReader == null) {
                JOptionPane.showMessageDialog(null, "请选择图片！！");
            } else {

                int[][] mazResult = null;
                try {
                    mazResult = new AStarCoreAlgorithm(bmpReader.getBmpDate()).start();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                freshBmpPanel(mazResult);


            }

        });

        // 「退出」选项
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

    public void freshBmpPanel(int[][] date) {
        if (this.bmpViewerPanel != null) {
            this.remove(this.bmpViewerPanel);
        }
        this.bmpViewerPanel = new BmpPanel(date);
        add(this.bmpViewerPanel);
        setSize(this.frameWidth, this.frameHeight);
        this.invalidate();
        this.validate();
    }
}
