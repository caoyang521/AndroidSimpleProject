package com.three.SecurityCode;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;


public class SecurityCodeHelper {

    /**
     * 这是一个自动识别验证码的程序。要求是简单的验证码，固定大小，固定位置，固定字体；字体纯色最好，如不是需要修改代码。
     *
     * @author acer
     *
     */

    // 存放所有下载验证码的目录
    private static final String DOWNLOAD_DIR = "Work\\test";

    // 存放已经拆分开的单个数字图片的目录，供比对用
    private static final String TRAIN_DIR = "Work\\train";

    // 存放比对结果的目录（重新以验证码所含数字命名文件，非常直观）
    private static final String RESULT_DIR = "Work\\result";

    // 存放比对图片与代表数字的Map
    private static Map<BufferedImage, String> trainMap = new HashMap<BufferedImage, String>();

    // 图片过滤器，想要什么样的图片，传进名称即可。如：png/gif/.png
    static class ImageFileFilter implements FileFilter {
        private String postfix = ".png";

        public ImageFileFilter(String postfix) {
            if(!postfix.startsWith("."))
                postfix = "." + postfix;

            this.postfix = postfix;
        }

        @Override
        public boolean accept(File pathname) {
            return pathname.getName().toLowerCase().endsWith(postfix);
        }
    }

    static {
        try {
            // 将TRAIN_DIR目录的供比对的图片装载进来
            File dir = new File(TRAIN_DIR);
            File[] files = dir.listFiles(new ImageFileFilter("png"));
            for (File file : files) {
                trainMap.put(ImageIO.read(file), file.getName().charAt(0) + "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 1.下载验证码：将多个验证码图片下载到指定目录，要求各种可能的验证码（单个数字）都应该有，比如：0-9。
    private void downloadImage() throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        String url = "http://10.22.151.40/scripts/login.exe/stamp?id="+ URLEncoder.encode("{6D997658-9655-476D-B070-B2217DD95108}", "UTF-8");

        HttpGet getMethod = new HttpGet(url);
        getMethod.setHeader("Cookie","UserName=144173551; LOGINID=%7B3CB28E85-DD2F-4D18-9EE4-D0D9794E998D%7D");
        getMethod.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
        getMethod.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");

        try {

            HttpResponse response = httpClient.execute(getMethod, new BasicHttpContext());
            HttpEntity entity = response.getEntity();
            InputStream instream = entity.getContent();
            OutputStream outstream = new FileOutputStream(new File(DOWNLOAD_DIR, "0" + ".png"));
            int l = -1;
            byte[] tmp = new byte[2048];
            while ((l = instream.read(tmp)) != -1) {
                outstream.write(tmp);
            }
            outstream.close();

        } finally {
            getMethod.releaseConnection();
        }

        System.out.println("下载验证码完毕！");
    }

    //2.去除图像干扰像素（非必须操作，只是可以提高精度而已）。
    public static BufferedImage removeInterference(BufferedImage image)
            throws Exception {
        int width = image.getWidth();
        int roundBlackCount = 0;
        for (int x = 0; x < width; ++x) {
            if (isFontColor(image.getRGB(x, 12))) {
                roundBlackCount++;
                if(roundBlackCount >= 30) {
                    for (int y = 0; y < width; ++y)
                        image.setRGB(y, 12, Color.WHITE.getRGB());
                }
            }

        }
        return image;
     }


    // 3.判断拆分验证码的标准：就是定义验证码中包含的各数字的x、y坐标值，及它们的宽度（width）、高度（height）。
    private static List<BufferedImage> splitImage(BufferedImage image) throws Exception {
        final int DIGIT_WIDTH = 10;
        final int DIGIT_HEIGHT = 16;

        List<BufferedImage> digitImageList = new ArrayList<BufferedImage>();
        digitImageList.add(image.getSubimage(3, 2, DIGIT_WIDTH, DIGIT_HEIGHT));
        digitImageList.add(image.getSubimage(13, 2, 8, 18));
        digitImageList.add(image.getSubimage(22, 2, 10, 18));
        digitImageList.add(image.getSubimage(32, 2, 11, DIGIT_HEIGHT));

        return digitImageList;
    }

    // 4.判断字体的颜色含义：正常可以用rgb三种颜色加起来表示，字与非字应该有显示的区别，找出来。
    private static boolean isFontColor(int colorInt) {
        Color color = new Color(colorInt);

        return color.getRed() + color.getGreen() + color.getBlue() < 100;
    }


    // 5.将下载的验证码图片全部拆分到另一个目录。
    public void generateStdDigitImgage() throws Exception {
        File dir = new File(DOWNLOAD_DIR);
        File[] files = dir.listFiles(new ImageFileFilter("png"));

        int counter = 0;
        for (File file : files) {
            BufferedImage image = ImageIO.read(file);
            //removeInterference(image);
            List<BufferedImage> digitImageList = splitImage(image);
            for (int i = 0; i < digitImageList.size(); i++) {
                BufferedImage bi = digitImageList.get(i);
                ImageIO.write(bi, "PNG", new File(TRAIN_DIR, "temp_" + counter++ + ".png"));
            }
        }
        System.out.println("生成供比对的图片完毕，请到目录中手工识别并重命名图片，并删除其它无关图片！");
    }


    // 7.测试判断效果：运行方法，可以调整rgb三值，以达到高的分辨率。
    // 目前此方法提供在输出判断结果的同时，在目标目录生成以判断结果命名的新验证码图片，以批量检查效果。
    public void testDownloadImage() throws Exception {
        File dir = new File(DOWNLOAD_DIR);
        File[] files = dir.listFiles(new ImageFileFilter("png"));

        for (File file : files) {
            String validateCode = getValidateCode(file);
            System.out.println(file.getName() + "=" + validateCode);
        }

        System.out.println("判断完毕，请到相关目录检查效果！");
    }

    /**
     * 8.提供给外界接口调用。
     * @param file
     * @return
     * @throws Exception
     */
    public static String getValidateCode(File file) throws Exception {

        // 装载图片
        BufferedImage image = ImageIO.read(file);
        //removeInterference(image);
        // 拆分图片
        List<BufferedImage> digitImageList = splitImage(image);

        // 循环每一位数字图进行比对
        StringBuilder sb = new StringBuilder();
        for (BufferedImage digitImage : digitImageList) {
            String result = "";
            int width = digitImage.getWidth();
            int height = digitImage.getHeight();
            // 最小的不同次数（初始值为总像素），值越小就越像。
            int minDiffCount = width * height;
            for (BufferedImage bi : trainMap.keySet()) {
                int w=bi.getWidth();
                int h=bi.getHeight();
                if(width>w)
                    width=w;
                if(height>h)
                    height=h;
                // 对每一位数字图与字典中的进行按像素比较
                int currDiffCount = 0; // 按像素比较不同的次数
                outer : for (int x = 0; x < width; ++x) {
                    for (int y = 0; y < height; ++y) {
                        if (isFontColor(digitImage.getRGB(x, y)) != isFontColor(bi.getRGB(x, y))) {
                            // 按像素比较如果不同，则加1；
                            currDiffCount++;
                            // 如果值大于minDiffCount，则不用再比较了，因为我们要找最小的minDiffCount。
                            if (currDiffCount >= minDiffCount)
                                break outer;
                        }
                    }
                }
                if (currDiffCount < minDiffCount) {
                    // 现在谁差别最小，就先暂时把值赋予给它
                    minDiffCount = currDiffCount;
                    result = trainMap.get(bi);
                }
            }
            sb.append(result);
        }
        ImageIO.write(image, "PNG", new File(RESULT_DIR, sb.toString() + ".png"));

        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        SecurityCodeHelper ins = new SecurityCodeHelper();

        // 第1步，下载验证码到DOWNLOAD_DIR
        //ins.downloadImage();

        // 第2步，去除干扰的像素
        File dir = new File(DOWNLOAD_DIR);
        File[] files = dir.listFiles(new ImageFileFilter("png"));
        for (File file : files) {
            BufferedImage image = ImageIO.read(file);
            removeInterference(image);
            ImageIO.write(image, "PNG", file);
            System.out.println("成功处理：" + file.getName());
        }

        // 第3步，判断拆分验证码的标准
        // 通过PhotoShop打开验证码并放大观察，我这儿的结果参考splitImage()方法中的变量

        // 第4步，判断字体的颜色含义
        // 通过PhotoShop打开验证码并放大观察，我这儿字体颜色的rgb总值加起来在340。因为是纯色。

        // 第5步，将下载的验证码图片全部拆分到TRAIN_DIR目录。
        //  ins.generateStdDigitImgage();

        // 第6步，手工命名文件
        // 打开资源管理器，选择TRAIN_DIR，分别找出显示0-9数字的文件，以它的名字重新命名，删除其它所有的。

        // 第7步，测试判断效果，运行后打开RESULT_DIR，检查文件名是否与验证码内容一致。
        ins.testDownloadImage();

        // 第8步，提供给外界接口调用。
        String validateCode = SecurityCodeHelper.getValidateCode(new File(DOWNLOAD_DIR, "0.png"));
        System.out.println("验证码为：" + validateCode);
    }

}
