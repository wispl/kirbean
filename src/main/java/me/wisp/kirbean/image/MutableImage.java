package me.wisp.kirbean.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class MutableImage {
    private BufferedImage image;
    private int width;
    private int height;

    public MutableImage(String url) {
        try {
            image = ImageIO.read(new URL(url));
            width = image.getWidth();
            height = image.getHeight();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not convert " + url + " into a proper URL", e);
        } catch (IOException e) {
            throw new RuntimeException("Could not get image from url");
        }
    }

    public MutableImage(BufferedImage image) {
        this.image = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        this.image.setData(image.getRaster());
        this.width = this.image.getWidth();
        this.width = this.image.getWidth();
    }

    public BufferedImage getImage() {
        return image;
    }

    public void destroyImage() {
        image.flush();
    }

    public void drawImage(MutableImage image, Coordinate coordinates) {
        drawImage(image, coordinates.x(), coordinates.y());
    }

    public void drawImage(MutableImage image, int x, int y) {
        Graphics2D graphics = this.image.createGraphics();
        graphics.drawImage(image.getImage(), x, y, null);
        image.destroyImage();
        graphics.dispose();
    }

    public MutableImage setRound() {
        BufferedImage output = new BufferedImage(width, height, image.getType());
        Graphics2D graphics = output.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.fillOval(0, 0, image.getWidth(), image.getHeight());
        graphics.setComposite(AlphaComposite.SrcIn);
        graphics.drawImage(image, 0, 0, null);

        destroyImage();
        graphics.dispose();

        image = output;
        return this;
    }

    public MutableImage setScale(int factor) {
        width *= factor;
        height *= factor;
        BufferedImage output = new BufferedImage(width, height, image.getType());
        Graphics2D graphics = output.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.drawImage(image, 0, 0, width, height, null);

        destroyImage();
        graphics.dispose();

        image = output;
        return this;
    }

    public ByteArrayInputStream asInputStream(String extension) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, extension, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        destroyImage();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    public static BufferedImage getImageFromFile(String path) {
        try {
            return ImageIO.read(Objects.requireNonNull(MutableImage.class.getClassLoader().getResource(path)));
        } catch (IOException e) {
            throw new RuntimeException("Could not load image form path " + path, e);
        }
    }
}
