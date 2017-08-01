package ch.blutch.battlephoto.controller.utils;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

public class ImageUtil
{
  public static final int IMAGE_UNKNOWN = -1;
  public static final int IMAGE_JPEG = 0;
  public static final int IMAGE_PNG = 1;
  public static final int IMAGE_GIF = 2;

  public static BufferedImage resizeImage(String imgName, int type, int maxWidth, int maxHeight)
  {
	BufferedImage toReturn = null;
    try {
    	toReturn = resizeImage(ImageIO.read(new File(imgName)), type, maxWidth, maxHeight);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    
    return toReturn;
  }

  public static BufferedImage resizeImage(BufferedImage image, int type, int maxWidth, int maxHeight)
  {
    Dimension largestDimension = new Dimension(maxWidth, maxHeight);

    // Original size
    int imageWidth = image.getWidth(null);
    int imageHeight = image.getHeight(null);

    float aspectRatio = (float) imageWidth / imageHeight;

    if (imageWidth > maxWidth || imageHeight > maxHeight) {
      if ((float) largestDimension.width / largestDimension.height > aspectRatio) {
        largestDimension.width = (int) Math.ceil(largestDimension.height * aspectRatio);
      }
      else {
        largestDimension.height = (int) Math.ceil(largestDimension.width / aspectRatio);
      }

      imageWidth = largestDimension.width;
      imageHeight = largestDimension.height;
    }

    return createHeadlessSmoothBufferedImage(image, type, imageWidth, imageHeight);
  }

  public static boolean saveImage(BufferedImage image, String toFileName, int type)
  {
	  boolean toReturn = false;
    try {
      toReturn = ImageIO.write(image, type == IMAGE_JPEG ? "jpg" : "png", new File(toFileName));
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    
    return toReturn;
  }

  public static void saveCompressedImage(BufferedImage image, String toFileName, int type)
  {
    try {
      if (type == IMAGE_PNG) {
        throw new UnsupportedOperationException("PNG compression not implemented");
      }

      Iterator iter = ImageIO.getImageWritersByFormatName("jpg");
      ImageWriter writer;
      writer = (ImageWriter) iter.next();

      ImageOutputStream ios = ImageIO.createImageOutputStream(new File(toFileName));
      writer.setOutput(ios);

      ImageWriteParam iwparam = new JPEGImageWriteParam(Locale.getDefault());

      iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
      iwparam.setCompressionQuality(0.6F);

      writer.write(null, new IIOImage(image, null, null), iwparam);

      ios.flush();
      writer.dispose();
      ios.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static BufferedImage createHeadlessBufferedImage(BufferedImage image, int type, int width, int height)
  {
    if (type == ImageUtil.IMAGE_PNG && hasAlpha(image)) {
      type = BufferedImage.TYPE_INT_ARGB;
    }
    else {
      type = BufferedImage.TYPE_INT_RGB;
    }

    BufferedImage bi = new BufferedImage(width, height, type);

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        bi.setRGB(x, y, image.getRGB(x * image.getWidth() / width, y * image.getHeight() / height));
      }
    }

    return bi;
  }

  public static BufferedImage createHeadlessSmoothBufferedImage(BufferedImage source, int type, int width, int height)
  {
    if (type == ImageUtil.IMAGE_PNG && hasAlpha(source)) {
      type = BufferedImage.TYPE_INT_ARGB;
    }
    else {
      type = BufferedImage.TYPE_INT_RGB;
    }

    BufferedImage dest = new BufferedImage(width, height, type);

    int sourcex;
    int sourcey;

    double scalex = (double) width / source.getWidth();
    double scaley = (double) height / source.getHeight();

    int x1;
    int y1;

    double xdiff;
    double ydiff;

    int rgb;
    int rgb1;
    int rgb2;

    for (int y = 0; y < height; y++) {
      sourcey = y * source.getHeight() / dest.getHeight();
      ydiff = scale(y, scaley) - sourcey;

      for (int x = 0; x < width; x++) {
        sourcex = x * source.getWidth() / dest.getWidth();
        xdiff = scale(x, scalex) - sourcex;

        x1 = Math.min(source.getWidth() - 1, sourcex + 1);
        y1 = Math.min(source.getHeight() - 1, sourcey + 1);

        rgb1 = getRGBInterpolation(source.getRGB(sourcex, sourcey), source.getRGB(x1, sourcey), xdiff);
        rgb2 = getRGBInterpolation(source.getRGB(sourcex, y1), source.getRGB(x1, y1), xdiff);

        rgb = getRGBInterpolation(rgb1, rgb2, ydiff);

        dest.setRGB(x, y, rgb);
      }
    }

    return dest;
  }

  private static double scale(int point, double scale)
  {
    return point / scale;
  }

  private static int getRGBInterpolation(int value1, int value2, double distance)
  {
    int alpha1 = (value1 & 0xFF000000) >>> 24;
    int red1 = (value1 & 0x00FF0000) >> 16;
    int green1 = (value1 & 0x0000FF00) >> 8;
    int blue1 = (value1 & 0x000000FF);

    int alpha2 = (value2 & 0xFF000000) >>> 24;
    int red2 = (value2 & 0x00FF0000) >> 16;
    int green2 = (value2 & 0x0000FF00) >> 8;
    int blue2 = (value2 & 0x000000FF);

    int rgb = ((int) (alpha1 * (1.0 - distance) + alpha2 * distance) << 24)
      | ((int) (red1 * (1.0 - distance) + red2 * distance) << 16)
      | ((int) (green1 * (1.0 - distance) + green2 * distance) << 8)
      | (int) (blue1 * (1.0 - distance) + blue2 * distance);

    return rgb;
  }

  public static boolean hasAlpha(Image image)
  {
    try {
      PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
      pg.grabPixels();

      return pg.getColorModel().hasAlpha();
    }
    catch (InterruptedException e) {
      return false;
    }
  }
}