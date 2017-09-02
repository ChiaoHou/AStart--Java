package me.chiaohou.astar.bmpUtil;

import java.io.FileInputStream;
import java.io.IOException;

public class BMPReader {

    private int bmpWidth = 0;
    private int bmpHeight = 0;

    private int[][] bmpDate = null;

    public int getBmpWidth() {
        return bmpWidth;
    }

    public int getBmpHeight() {
        return bmpHeight;
    }


    public int[][] getBmpDate() {
        return bmpDate;
    }

    public int[][] readBMP(String path) throws IOException {
        /**
         * 读取 BMP 文件的方法
         *
         * @param path 文件路径
         * @throws IOException 流异常
         */
        // 读取 BMP 数据
        FileInputStream fis = new FileInputStream(path);
        // 跳过不需要读取的数据
        fis.skip(18);

        // 读取 BMP 的宽高
        this.bmpWidth = changeInt(fis);
        this.bmpHeight = changeInt(fis);

        // 跳过不需要读取的数据
        fis.skip(28);

        // 记录图片数据到二维数组
        int[][] date = new int[this.bmpHeight][this.bmpWidth];
        int t = 0;
        if (this.bmpWidth * 3 % 4 > 0) {
            t = 4 - this.bmpWidth * 3 % 4;
        }
        for (int i = 0; i < this.bmpWidth; i++) {
            for (int j = 0; j < this.bmpHeight; j++) {
                // 调用自定义方法，得到图片的像素点并保存到 int 数组中
                int c = readColor(fis);
                date[i][j] = c;
            }
            fis.skip(t);
        }
        fis.close();

        this.bmpDate = date;
        return date;
    }

    private int changeInt(FileInputStream ips) throws IOException {
        /**
         * 由于读取的是字节，把读取到的 4 个 byte 转化成 1 个 int
         *
         * @param ips 文件输入流对象
         * @return 转换后的 int 值
         * @throws IOException 流异常
         */
        int t1 = ips.read() & 0xff;
        int t2 = ips.read() & 0xff;
        int t3 = ips.read() & 0xff;
        int t4 = ips.read() & 0xff;
        int num = (t4 << 24) + (t3 << 16) + (t2 << 8) + t1;
        return num;
    }


    private int readColor(FileInputStream ips) throws IOException {
        /**
         * 24 位的图片是 1 个像素 3 个字节，所以要将其颜色值合成一个具体的颜色值
         *
         * @param ips 文件输入流对象
         * @return 颜色值的合成值
         * @throws IOException 流异常
         */
        int b = ips.read() & 0xff;
        int g = ips.read() & 0xff;
        int r = ips.read() & 0xff;
        int c = (r << 16) + (g << 8) + b;
        return c;
    }

}