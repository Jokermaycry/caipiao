package com.ailide.apartmentsabc.utils;

public class GrayScaleUtil {

    interface GrayScaleCompute {
        int grayScale(int r, int g, int b);
    }

    public enum GrayScale {

        Lightness(new GrayScaleCompute() {
            @Override
            public int grayScale(int r, int g, int b) {
                return GrayScaleUtil.lightness(r, g, b);
            }
        }),
        Average(new GrayScaleCompute() {
            @Override
            public int grayScale(int r, int g, int b) {
                return GrayScaleUtil.average(r, g, b);
            }
        }),
        Luminosity(new GrayScaleCompute() {
            @Override
            public int grayScale(int r, int g, int b) {
                return GrayScaleUtil.luminosity(r, g, b);
            }
        }),

        BT709(new GrayScaleCompute() {

            @Override
            public int grayScale(int r, int g, int b) {
                return GrayScaleUtil.BT709(r, g, b);
            }
        }),

        RMY(new GrayScaleCompute() {
            @Override
            public int grayScale(int r, int g, int b) {
                return GrayScaleUtil.RMY(r, g, b);
            }
        }),
        Y(new GrayScaleCompute() {
            @Override
            public int grayScale(int r, int g, int b) {
                return GrayScaleUtil.Y(r, g, b);
            }
        });

        private GrayScaleCompute gc;

        GrayScale(GrayScaleCompute gc) {
            this.gc = gc;
        }

        public int grayScale(int r, int g, int b) {
            return this.gc.grayScale(r, g, b);
        }
    }

    //Lightness = (max(r,g,b)+min(r,g,b))/2
    public static int lightness(int r, int g, int b) {
        return (Math.max(Math.max(r, g), b) + Math.min(Math.min(r, g), b)) / 2;
    }

    // Average Brightness = (r+g+b)/3
    public static int average(int r, int g, int b) {
        return (r + g + b) / 3;
    }

    //Luminosity =(0.21*r+0.72*g+0.07*b)
    public static int luminosity(int r, int g, int b) {
        return (int) (0.21 * r + 0.72 * g + 0.07 * b);
    }

    /**
     * Magic number about grayscale from http://jscience.org/experimental/javadoc/org/jscience/computing/ai/vision/GreyscaleFilter.html
     */
    //BT709 Greyscale: Red: 0.2125 Green: 0.7154 Blue: 0.0721
    public static int BT709(int r, int g, int b) {
        return (int) (0.2125 * r + 0.7154 * g + 0.0721 * b);
    }

    //RMY Greyscale: Red: 0.5 Green: 0.419 Blue: 0.081
    public static int RMY(int r, int g, int b) {
        return (int) (0.5 * r + 0.419 * g + 0.081 * b);
    }

    //Y-Greyscale (YIQ/NTSC): Red: 0.299 Green: 0.587 Blue: 0.114
    public static int Y(int r, int g, int b) {
        return (int) (0.299 * r + 0.587 * g + 0.114 * b);
    }
}
