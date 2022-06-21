package me.wisp.kirbean.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Image {

    // wrap using ByteArrays so we do not have to write to file
    public static ByteArrayInputStream asByteArrayStream(BufferedImage image, String extension) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, extension, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    public static BufferedImage clone(BufferedImage source) {
        BufferedImage clone = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        clone.setData(source.getRaster());
        return clone;
    }

    public static BufferedImage fromFile(File file) {
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage fromURL(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage round(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = output.createGraphics();

        graphics.setClip(new Ellipse2D.Float(0, 0, width, height));
        graphics.drawImage(image, 0, 0, null);

        graphics.dispose();
        return output;
    }

    public static BufferedImage scale(BufferedImage image, int factor) {
        BufferedImage output = new BufferedImage(image.getWidth()*factor, image.getHeight()*factor, BufferedImage.TYPE_INT_ARGB);
        AffineTransform scale = AffineTransform.getScaleInstance(factor, factor);
        AffineTransformOp op = new AffineTransformOp(scale, AffineTransformOp.TYPE_BILINEAR);

        op.filter(image, output);
        return output;
    }
}
