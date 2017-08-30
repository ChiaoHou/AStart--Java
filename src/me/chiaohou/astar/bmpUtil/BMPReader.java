package me.chiaohou.astar.bmpUtil;

import java.io.FileInputStream;
import java.io.IOException;

public class BMPReader {

    /**
     * 读取 BMP 文件的方法
     *
     * @param path 文件路径
     * @throws IOException 流异常
     */
    public int[][] readBMP(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        fis.skip(18);
        // 跳过不需要的，读取宽度和高度
        int width = changeInt(fis);
        int height = changeInt(fis);
        fis.skip(28);
        // 跳过，直接读取位图数据。
        int[][] date = new int[height][width];
        int t = 0;
        if (width * 3 % 4 > 0) {
            t = 4 - width * 3 % 4;
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // 调用自定义方法，得到图片的像素点并保存到 int 数组中
                int c = readColor(fis);
                date[i][j] = c;

            }
            fis.skip(t);
        }
        fis.close();
        // 刷新，重绘面板，打开系统保存的图片。
        return date;
    }

    /**
     * 由于读取的是字节，把读取到的 4 个 byte 转化成 1 个 int
     *
     * @param ips 文件输入流对象
     * @return 转换后的 int 值
     * @throws IOException 流异常
     */
    private int changeInt(FileInputStream ips) throws IOException {
        int t1 = ips.read() & 0xff;
        int t2 = ips.read() & 0xff;
        int t3 = ips.read() & 0xff;
        int t4 = ips.read() & 0xff;
        int num = (t4 << 24) + (t3 << 16) + (t2 << 8) + t1;
        return num;
    }

    /**
     * 24 位的图片是 1 个像素 3 个字节，所以要将其颜色值合成一个具体的颜色值
     *
     * @param ips 文件输入流对象
     * @return 颜色值的合成值
     * @throws IOException 流异常
     */
    private int readColor(FileInputStream ips) throws IOException {
        int b = ips.read() & 0xff;
        int g = ips.read() & 0xff;
        int r = ips.read() & 0xff;
        int c = (r << 16) + (g << 8) + b;
        return c;
    }

}