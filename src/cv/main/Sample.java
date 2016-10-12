package cv.main;

import cv.enums.Transformation;
import cv.util.CVKeyListener;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.opencv.core.Core.flip;
import static org.opencv.imgproc.Imgproc.resize;
import static org.opencv.videoio.Videoio.CV_CAP_PROP_FRAME_HEIGHT;
import static org.opencv.videoio.Videoio.CV_CAP_PROP_FRAME_WIDTH;

public class Sample {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static final String EXTENSION = ".avi";

    private static Transformation transformation;
    private static boolean rotation = false;
    private static boolean redimension = false;
    private static boolean vertical = false;
    private static boolean horizontal = false;

    private static VideoWriter writer = null;
    private static VideoCapture capture = null;
    private static JSlider slider = null;

    private Sample() {
        //Utility classes should not be instantiated
    }

    public static int getKernelSize() {
        return (2 * slider.getValue()) + 1;
    }

    public static void setTransformation(Transformation transformation) {
        Sample.transformation = transformation;
    }

    public static boolean isRedimension() {
        return redimension;
    }

    public static void setRedimension(boolean redimension) {
        Sample.redimension = redimension;
    }

    public static boolean isRotation() {
        return rotation;
    }

    public static void setRotation(boolean rotation) {
        Sample.rotation = rotation;
    }

    public static boolean isHorizontal() {
        return horizontal;
    }

    public static void setHorizontal(boolean horizontal) {
        Sample.horizontal = horizontal;
    }

    public static boolean isVertical() {
        return vertical;
    }

    public static void setVertical(boolean vertical) {
        Sample.vertical = vertical;
    }

    public static void disableRotationTransformations() {
        Sample.setRotation(false);
        Sample.setRedimension(false);
        Sample.setHorizontal(false);
        Sample.setVertical(false);
    }

    public static void main(String[] args) {
        final int camera = 0;
        capture = new VideoCapture(camera);

        if (!capture.isOpened()) {
            return;
        }

        JFrame originalJFrame = new JFrame();
        originalJFrame.setLayout(new FlowLayout());
        originalJFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        originalJFrame.setTitle("Trabalho 3 - Matheus Maciel Soares");
        originalJFrame.addKeyListener(CVKeyListener.get());

        JLabel originalImageJLabel = null;
        JLabel resultImageJLabel = null;

        slider = new JSlider(JSlider.VERTICAL, 0, 70, 0);
        originalJFrame.getContentPane().add(slider);

        setTransformation(Transformation.NONE);

        boolean videoInitialized = false;

        while (true) {
            originalJFrame.requestFocus();

            Mat originalMat = new Mat();
            capture.read(originalMat);
            if (originalMat.empty()) {
                break;
            }

            Mat resultMat = transformation.doAction(originalMat);

            resultMat = handleRedimension(resultMat);
            resultMat = handleRotation(resultMat);
            handleHorizontalFlip(resultMat);
            handleVerticalFlip(resultMat);

            BufferedImage originalImage = matToBufferedImage(originalMat);
            BufferedImage resultImage = matToBufferedImage(resultMat);

            if (cv.enums.KeyStroke.isRecording()) {
                if (!videoInitialized) {
                    initWriter();
                    videoInitialized = true;
                }
                writer.write(resultMat);
            }

            if (originalImageJLabel != null) {
                originalJFrame.getContentPane().remove(originalImageJLabel);
                originalJFrame.getContentPane().remove(resultImageJLabel);
            }

            originalImageJLabel = new JLabel(new ImageIcon(originalImage));
            originalJFrame.getContentPane().add(originalImageJLabel);

            resultImageJLabel = new JLabel(new ImageIcon(resultImage));
            originalJFrame.getContentPane().add(resultImageJLabel);

            originalJFrame.setSize((originalImage.getWidth() + resultImage.getWidth()) + 100, Math.max(originalImage.getHeight(), resultImage.getHeight()) + 30);
            originalJFrame.setVisible(true);
        }

        releaseResources();
    }

    private static void initWriter() {
        String timestamp = new SimpleDateFormat("dd 'de' MMMM 'de' YYYY HH'h'mm'min'ss's'").format(Calendar.getInstance().getTime());
        Size frameSize = new Size(capture.get(CV_CAP_PROP_FRAME_WIDTH), capture.get(CV_CAP_PROP_FRAME_HEIGHT));
        int codecId = 0;
        writer = new VideoWriter(timestamp + EXTENSION, codecId, 20, frameSize, true);
    }

    private static void handleVerticalFlip(Mat resultMat) {
        if (isVertical()) {
            flip(resultMat, resultMat, 0);
        }
    }

    private static void handleHorizontalFlip(Mat resultMat) {
        if (isHorizontal()) {
            flip(resultMat, resultMat, 1);
        }
    }

    private static Mat handleRotation(Mat resultMat) {
        if (isRotation()) {
            Mat helper1 = new Mat();
            Mat helper2 = new Mat();
            Core.transpose(resultMat, helper1);
            Core.flip(helper1, helper2, 1);
            return helper2.clone();
        } else {
            return resultMat;
        }
    }

    private static Mat handleRedimension(Mat resultMat) {
        if (isRedimension()) {
            Mat helper = new Mat();
            resize(resultMat, helper, new Size(resultMat.width() / 2, resultMat.height() / 2));
            return helper.clone();
        } else {
            return resultMat;
        }
    }

    public static void releaseResources() {
        if (writer != null)
            writer.release();

        capture.release();
    }

    private static BufferedImage matToBufferedImage(Mat frame) {
        int type = 0;
        if (frame.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (frame.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        frame.get(0, 0, data);

        return image;
    }
}