package pl.edu.mimuw.students.ts335793;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

public class RunPCY {

    private static String fileURI;
    private static int bufferSize;
    private static int supportThreshold;
    private static int maxId;
    private static int maxBucket;

    private static void parseParameters(String[] args) {
        try {
            fileURI = args[0];
            bufferSize = Integer.parseInt(args[1]);
            supportThreshold = Integer.parseInt(args[2]);
            maxId = Integer.parseInt(args[3]);
            maxBucket = Integer.parseInt(args[4]);
        } catch (Exception exception) {
            System.out.println("Usage: pcy <file path> <buffer size> <support threshold> <max id> <max bucket>");
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        parseParameters(args);

        Configuration configuration = new Configuration();

        FileSystem fileSystem;
        try {
            fileSystem = FileSystem.get(configuration);
        } catch (IOException exception) {
            System.out.println("Failed to get file system.");
            System.out.println(exception.getLocalizedMessage());
            System.exit(1);
            return;
        }

        Path filePath = new Path(fileURI);

        FSDataInputStream inputStream;
        try {
            inputStream = fileSystem.open(filePath, bufferSize);
        } catch (IOException exception) {
            System.out.println("Failed to create input stream.");
            System.out.println(exception.getLocalizedMessage());
            System.exit(1);
            return;
        }

        BasketReader basketReader = new BasketReader(inputStream);

        PCY pcy = new PCY(supportThreshold, maxId, maxBucket);
        System.out.println(pcy.getFrequentPairs(basketReader));
    }

    /*public static void main(String[] args) {
        parseParameters(args);

        Configuration configuration = new Configuration();

        FileSystem fileSystem = null;
        try {
            fileSystem = FileSystem.get(configuration);
        } catch (IOException exception) {
            System.out.println("Failed to get file system.");
            System.out.println(exception.getLocalizedMessage());
            return;
        }

        Path filePath = new Path(fileURI);

        System.out.println("Creating file.");
        FSDataOutputStream outputStream = null;
        try {
            outputStream = fileSystem.create(filePath);
        } catch (IOException exception) {
            System.out.println("Failed to create input stream.");
            System.out.println(exception.getLocalizedMessage());
            return;
        }

        Integer[] ids = { 1, 2, 3, -1, 1, 2, -1 };

        for (Integer id : ids) {
            try {
                outputStream.writeInt(id);
            } catch (IOException exception) {
                System.out.println("Failed to write to output stream.");
                System.out.println(exception.getLocalizedMessage());
                return;
            }
        }

        System.out.println("Created file.");
    }*/
}