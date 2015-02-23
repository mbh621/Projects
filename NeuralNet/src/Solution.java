import java.io.FileInputStream;
import java.util.*;
/**
 * Created by maxwell on 2/21/15.
 */
/*
    INPUTS:
    (1) a training file
    (2) a test file
    (3) a learning rate
    (4) the number of iterations to run the algorithm
 */
public class Solution {

    public static void main(String[] args) throws Exception{
        if(args.length != 4){
            System.out.println("Usage: java Solution <training file> <test file> <learning rate> <training iterations>");
            System.exit(-1);
        }
        // initialize variables
        Scanner train, test;
        train = test = null;
        try{
            train = new Scanner(new FileInputStream(args[0]));
            test = new Scanner(new FileInputStream(args[1]));
        }
        catch(Exception e){
            System.out.format("Unable to open file %s\n", e.getMessage());
            System.out.println("Usage: java Solution <training file> <test file> <learning rate> <training iterations>");
            System.exit(-1);
        }
        double rate = Double.parseDouble(args[2]);
        int iterations = Integer.parseInt(args[3]);
        ArrayList<Byte[]> inputAttributes = new ArrayList<Byte[]>();
        ArrayList<Byte> inputClasses = new ArrayList<Byte>();

        // process training file contents
        Scanner line = new Scanner(train.nextLine());
        int count = 0;
        while(line.hasNext()){
            line.next();
            count++;
        }
        line.close();
        while(train.hasNext()){
            line = new Scanner(train.nextLine());
            // want to stop if a line is blank
            if(!line.hasNextByte()){
                line.close();
                break;
            }
            Byte[] attributes = new Byte[count];
            for(int x = 0; x < count; x++){
                attributes[x] = line.nextByte();
            }
            inputClasses.add(line.nextByte());
            inputAttributes.add(attributes);
            line.close();
        }
        train.close();

        // train the neural net
        Perceptron net = new Perceptron(count, rate);
        int size = inputAttributes.size();
        for(int x = 0; x < iterations; x++){
            net.train(inputAttributes.get(x % size), inputClasses.get(x % size));
        }

        // test neural net on training data
        int correct = 0;
        for(int x = 0; x < size ; x++){
            if(net.classify(inputAttributes.get(x)) == inputClasses.get(x))
                correct++;
        }
        System.out.format("Accuracy on training set (%d instances): %.2f%%\n", size, correct/((float) size)*100);

        correct = 0;
        size = 0;
        // test neural net on test data
        test.nextLine(); // skip the top line
        while(test.hasNextLine()){
            line = new Scanner(test.nextLine());
            // stop if a line is blank
            if(!line.hasNextByte()) {
                line.close();
                break;
            }
            size++;
            Byte[] attributes = new Byte[count];
            for(int x = 0; x < count; x++){
                attributes[x] = line.nextByte();
            }
            Byte cls = line.nextByte();
            line.close();
            if(net.classify(attributes) == cls)
                correct++;
        }
        System.out.format("Accuracy on test set (%d instances): %.2f%%\n", size, correct/((float) size)*100);



    }
}
