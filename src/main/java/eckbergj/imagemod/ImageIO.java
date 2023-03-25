/*
 * CS1021 - 011
 * Winter 2021-2022
 * Lab 8: Image Manipulation
 * John Eckberg
 * 5/17/2022
 */
package eckbergj.imagemod;

import edu.msoe.cs1021.ImageUtil;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.Files.newBufferedWriter;
import static java.nio.file.Files.newOutputStream;
import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * The ImageIO class is the model for the Image Gui
 * Facilitates reading, editing, and writing files
 */
public class ImageIO {

    static final int b = 66;
    static final int m = 77;
    static final int s = 83;
    static final int o = 79;
    static final int e = 69;

    /**
     * Reads an image from a specified file path
     * @param path Image file path
     * @return Image, or null if no valid file is found and an exception is not thrown
     */
    public static Image read(Path path) throws IllegalArgumentException, IOException {

        Image image;

        String pathString = path.toString();
        String typeString = pathString.split("\\.")[1];
        System.out.println(typeString);

        if(typeString.matches("msoe")){
            image = readMSOE(path); //throws IO
        } else if(typeString.matches("jpeg")| typeString.matches("png")){
            image = ImageUtil.readImage(path);
        } else if(typeString.matches("bmsoe")){
            image = readBMSOE(path);
        } else {
            System.out.println(pathString);
            throw new IllegalArgumentException("File extension invalid");
        }
        return image;
    }



    /**
     * Writes a given image to a given path
     * @param image Image to be written
     * @param path Path to write to
     */
    public static void write(Image image, Path path) throws IllegalArgumentException, IOException {
        /*
        uses file extension to determine if
        Image util class should be called
        or if writemsoe

        if not in specified in paths, create file format getter
        that throws IllegalArgumentException
        */


        if(image != null){

            String pathString = path.toString();
            String typeString = pathString.split("\\.")[1];
            if(typeString.matches("msoe")){
                writeMSOE(image, path); //Throws IO

            } else if(typeString.matches("jpeg")| typeString.matches("png")){
                ImageUtil.writeImage(path, image); //Throws IO

            } else if(typeString.matches("bmsoe")){
                writeBMSOE(image, path);
            } else {
                throw new IllegalArgumentException("File format invalid");
            }
        } else {
            System.out.println("wtf this was working like 3 hours ago");
            throw new NoSuchFileException("Image not present");
        }
    }



    private static Image readMSOE(Path path) throws IOException{

        //create lists to process
        //NOTE: THIS WOULD BE HELLA FASTER if .lines and stream filter was used for input checking
        List<String> lines = Files.readAllLines(path);
        List<String> imageLines = lines.subList(2, lines.size());



        //break this down into two different strings using string split
        String[] dimensions = lines.get(1).split("\s+");
        System.out.println(lines.get(1));

        //checking for correct formatting
        if(!lines.get(0).matches("MSOE")){
            throw new IOException("MSoe bad");

        }
        if(!(dimensions[0].matches("[0-9]+") && dimensions [1].matches("[0-9]+")) ){
            System.out.println(dimensions[0]);
            System.out.println(dimensions[1]);
            System.out.println("Dimension failure");
            throw new IOException("Image dimensions incorrectly formatted");
        }
        //pulls out dimensions
        int width = Integer.parseInt(dimensions[0]);
        int height = Integer.parseInt(dimensions[1]);



        WritableImage image = new WritableImage(width, height);
        PixelWriter writer = image.getPixelWriter();


        for(int y = 0; y < height-1; y++){

            String[] line = imageLines.get(y).split("\s+");

            for(int x = width-2; x >= 0; --x){
                //"^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$"
                if(line[x].matches("^#[0-9a-fA-F]{8}$|#[0-9a-fA-F]{6}$|#[0-9a-fA-F]{4}$|#[0-9a-fA-F]{3}$")){
                    writer.setColor(x, y, Color.web(line[x]));
                } else {
                    System.out.println("color fail");
                    throw new IOException("Color data incorrectly formatted");
                }
                // then go through array of strings and check each one against hex regex
                // if true, set color
            }
        }

        return image;
    }

    private static void writeMSOE(Image image, Path path) throws IOException {

        OpenOption[] options = new OpenOption[] { WRITE, CREATE_NEW };
        PixelReader reader = image.getPixelReader();
        try (BufferedWriter writer = newBufferedWriter(path, options)){

            int width = (int) image.getWidth();
            int height = (int) image.getHeight();


            writer.write("MSOE");
            writer.newLine();
            writer.write(width + " " + height);

            for (int y = height-1; y > 0; --y) {
                //bumps line down
                writer.newLine();
                for (int x = width-1; x > 0; --x) {

                    //gets color (stupid casting)
                    Color color = reader.getColor(x, y);
                    int r = (int) color.getRed();
                    int g = (int) color.getGreen();
                    int b = (int) color.getBlue();

                    // uses given pixel color to create a hex string and save it in file
                    String hex = String.format("#%02x%02x%02x  ", r, g, b);
                    writer.write(hex);
                }
            }
        }
    }

    /**
     * reads from the BMSOE format
     * @param path
     * @return Image
     * @throws IOException
     */
    private static Image readBMSOE(Path path) throws IOException {

        WritableImage writableImage;
        //create streams
        InputStream bStream = new FileInputStream(path.toFile()); //Throws IO
        InputStream b1Stream = new FileInputStream(path.toFile());

        /*
        NOTE: I'm confused on why i had to make a second input stream & then skip instead of
        picking up where I left off in the first stream
         */

        InputStreamReader charReader = new InputStreamReader(bStream, StandardCharsets.US_ASCII);
        BufferedInputStream buffStream = new BufferedInputStream(b1Stream);
        try (DataInputStream dStream = new DataInputStream(buffStream)) {


            //Use input stream reader to read first 5
            char[] bMSOEChar = new char[5];
            StringBuilder bMSOEStringBuilder = new StringBuilder();
            charReader.read(bMSOEChar, 0, 5);

            for (char c: bMSOEChar){
                bMSOEStringBuilder.append(c);
            }
            String bmsoeString = bMSOEStringBuilder.toString();
            System.out.println(bmsoeString);

            if (!bmsoeString.contains("BMSOE")) {
                System.out.println("regex failure");
                throw new IOException();
            }

            //then grab height & width
            dStream.skip(5);
            int width = dStream.readInt();
            int height = dStream.readInt();
            System.out.println(width);
            System.out.println(height);

            // then use data input stream to process color data
            writableImage = new WritableImage(width, height);
            PixelWriter writer = writableImage.getPixelWriter();

            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    writer.setArgb(x, y, dStream.readInt());
                }
            }
        }
        return writableImage;
    }

    /**
     * Writes a BMSOE formated file
     * @param image
     * @param path
     * @throws IOException
     */
    private static void writeBMSOE(Image image, Path path) throws IOException {

        //write bmsoe char sequence
        OutputStream fileOutputStream = newOutputStream(path); //throws IO
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        try(DataOutputStream outputStream = new DataOutputStream(bufferedOutputStream)) {

            outputStream.writeByte(b);
            outputStream.writeByte(m);
            outputStream.writeByte(s);
            outputStream.writeByte(o);
            outputStream.writeByte(e);

            //Write double space then add height space width
            int height = (int) image.getHeight();
            outputStream.writeInt(height);

            int width = (int) image.getWidth();
            outputStream.writeInt(width);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    //This is the reverse order of the read function, why does it work differently?
                    outputStream.writeInt(image.getPixelReader().getArgb(x, y));
                }
            }
        }
    }

    /**
     * lambda greyscale. transforms pixel to a greyscale pixel
     */
    public static Transformable greyScale = (y, color) -> color.grayscale();

    /**
     * lambda greyscale. transforms pixel to an inverted pixel
     */
    public static Transformable invert = (y, color) -> color.invert();

    /**
     * lambda greyscale. transforms pixel to red only pixel
     */
    public static Transformable red = (y, color) -> Color.color(color.getRed(),0,0);

    /**
     * lambda greyscale. transforms pixel to a red/grey pixel
     */
    public static Transformable redGray = (y, color) -> {
        if(y%2 == 0){
            return Color.color(color.getRed(),0,0);
        } else {
            return color.grayscale();
        }
    };



}
