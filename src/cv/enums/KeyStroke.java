package cv.enums;

import cv.main.Sample;

import static cv.main.Sample.setTransformation;
import static java.lang.System.exit;

public enum KeyStroke {
    QUIT('q') {
        @Override
        public void doAction() {
            Sample.releaseResources();
            exit(1);
        }
    },
    BLUR('b') {
        @Override
        public void doAction() {
            setTransformation(Transformation.BLUR);
        }
    },
    EDGE_DETECTION('e') {
        @Override
        public void doAction() {
            setTransformation(Transformation.EDGE_DETECTION);
        }
    },
    GRADIENT('g') {
        @Override
        public void doAction() {
            setTransformation(Transformation.GRADIENT);
        }
    },
    BRIGHT('B') {
        @Override
        public void doAction() {
            setTransformation(Transformation.BRIGHT);
        }
    },
    CONTRAST('c') {
        @Override
        public void doAction() {
            setTransformation(Transformation.CONTRAST);
        }
    },
    NEGATIVE('n') {
        @Override
        public void doAction() {
            setTransformation(Transformation.NEGATIVE);
        }
    },
    GRAYSCALE('G') {
        @Override
        public void doAction() {
            setTransformation(Transformation.GRAYSCALE);
        }
    },
    REDIMENSION('r') {
        @Override
        public void doAction() {
            if (!isRecording) {
                Sample.setRedimension(!Sample.isRedimension());
            }
        }
    },
    ROTATION('R') {
        @Override
        public void doAction() {
            if (!isRecording) {
                Sample.setRotation(!Sample.isRotation());
            }
        }
    },
    VERTICAL_MIRRORING('v') {
        @Override
        public void doAction() {
            Sample.setVertical(!Sample.isVertical());
        }
    },
    HORIZONTAL_MIRRORING('h') {
        @Override
        public void doAction() {
            Sample.setHorizontal(!Sample.isHorizontal());
        }
    },
    START_RECORDING('s') {
        @Override
        public void doAction() {
            boolean blocksRecording = Sample.isRedimension() || Sample.isRotation();

            if (!blocksRecording) {
                System.out.println("Start recording");
                setIsRecordingTo(true);
            } else {
                System.out.println("Won't start recording due to blocking transformation");
            }
        }
    },
    STOP_RECORDING('S') {
        @Override
        public void doAction() {
            System.out.println("Stop recording");
            setIsRecordingTo(false);
        }
    },
    RESET('.') {
        @Override
        public void doAction() {
            setTransformation(Transformation.NONE);
            Sample.disableRotationTransformations();
        }
    },
    ANY('_');

    private final char key;

    private static boolean isRecording;

    KeyStroke(char key) {
        this.key = key;
    }

    private static void setIsRecordingTo(boolean isRecording) {
        KeyStroke.isRecording = isRecording;
    }

    public static boolean isRecording() {
        return isRecording;
    }

    private char getKey() {
        return key;
    }

    public static KeyStroke get(char key) {
        for (KeyStroke keyStroke : KeyStroke.values()) {
            if (keyStroke.getKey() == key) {
                return keyStroke;
            }
        }

        return ANY;
    }

    public void doAction() {
        //do nothing
    }
}
