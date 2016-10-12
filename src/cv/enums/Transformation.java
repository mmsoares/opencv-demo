package cv.enums;

import cv.main.Sample;
import org.opencv.core.Mat;
import org.opencv.core.Size;

import static org.opencv.core.Core.BORDER_DEFAULT;
import static org.opencv.core.Core.addWeighted;
import static org.opencv.core.Core.convertScaleAbs;
import static org.opencv.core.CvType.CV_16S;
import static org.opencv.imgproc.Imgproc.*;

public enum Transformation {
    BLUR() {
        @Override
        public Mat doAction(Mat original) {
            Mat helperMat = new Mat();
            int size = Sample.getKernelSize();
            GaussianBlur(original, helperMat, new Size(size, size), 0, 0, BORDER_DEFAULT);
            return helperMat;
        }
    },
    EDGE_DETECTION() {
        @Override
        public Mat doAction(Mat original) {
            //Pre-processing: use gaussian blur to reduce noise
            final int size = 15;
            Mat helperMat = new Mat();
            GaussianBlur(original, helperMat, new Size(size, size), 0, 0, BORDER_DEFAULT);

            //Actual transformation
            Mat transformedMat = new Mat();
            Canny(helperMat, transformedMat, 30, 60);
            return transformedMat;
        }
    },
    GRADIENT() {
        @Override
        public Mat doAction(Mat original) {
            //Pre-processing: use gaussian blur to reduce noise
            final int size = 15;
            Mat helperMat = new Mat();
            GaussianBlur(original, helperMat, new Size(size, size), 0, 0, BORDER_DEFAULT);

            //Actual transformation
            Mat inputGray = new Mat();
            cvtColor(helperMat, inputGray, COLOR_RGB2GRAY);

            Mat gradX = new Mat();
            Mat gradY = new Mat();
            Mat absGradX = new Mat();
            Mat absGradY = new Mat();

            int dDepth = CV_16S;

            Sobel(inputGray, gradX, dDepth, 1, 0);
            convertScaleAbs(gradX, absGradX);

            Sobel(inputGray, gradY, dDepth, 0, 1);
            convertScaleAbs(gradY, absGradY);

            Mat output = new Mat();
            addWeighted(absGradX, 0.5, absGradY, 0.5, 0, output);

            return output;
        }
    },
    BRIGHT() {
        @Override
        public Mat doAction(Mat original) {
            Mat modified = new Mat();
            original.convertTo(modified, original.type(), 1, 80);

            return modified;
        }
    },
    CONTRAST() {
        @Override
        public Mat doAction(Mat original) {
            Mat modified = new Mat();
            original.convertTo(modified, original.type(), 1.7, 0);

            return modified;
        }
    },
    NEGATIVE() {
        @Override
        public Mat doAction(Mat original) {
            Mat modified = new Mat();
            original.convertTo(modified, original.type(), -1, 255);
            return modified;
        }
    },
    GRAYSCALE() {
        @Override
        public Mat doAction(Mat original) {
            Mat modified = new Mat();
            cvtColor(original, modified, COLOR_RGB2GRAY);
            return modified;
        }
    },
    NONE {
        @Override
        public Mat doAction(Mat original) {
            return original;
        }
    };

    public abstract Mat doAction(Mat original);
}
