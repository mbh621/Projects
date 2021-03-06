import java.util.*;
import java.io.*;
/*
  Main method for a Hidden Markov Model implementation program.
  The program takes two files as command line arguments:
    1: A file containing model data
    2: A file containing observation sequence data
  and runs the Viterbi Decoding algorithm against the given observations and data.

  The output of the program is line delimited outputs of the Viterbi Decoding algorithm.
  Each Viterbi Decoding output is a space delimited sequence of 1-based states.
*/
public class Solution{

  public static void main(String[] args) throws Exception{
    try{
      if(args.length != 2)
        throw new Exception();
      Scanner data = new Scanner(new FileInputStream(args[0]));
      Scanner test = new Scanner(new FileInputStream(args[1]));

      int numStates = data.nextInt();
      data.nextLine();
      // process initial state data
      double[] initial = new double[numStates];
      for(int k = 0; k < numStates; k++){
        initial[k] = data.nextDouble();
      }
      data.nextLine();

      // process state transition data
      double[][] transitions = new double[numStates][numStates];
      for(int row = 0; row < numStates; row++){
        for(int col = 0; col < numStates; col++){
          transitions[row][col] = data.nextDouble();
        }
      }
      data.nextLine();

      // process symbol data
      int numSymbols = data.nextInt();
      data.nextLine();
      String[] symbols = new String[numSymbols];
      for(int k = 0; k < numSymbols; k++){
        symbols[k] = data.next();
      }
      data.nextLine();

      // process state output probability data
      double[][] outputDistribution = new double[numStates][numSymbols];
      for(int row = 0; row < numStates; row++){
        for(int col = 0; col < numSymbols; col++){
          outputDistribution[row][col] = data.nextDouble();
        }
      }
      data.close();

      // create the model
      HMM model = new HMM(symbols, initial, transitions, outputDistribution);

      // test the model
      while(test.hasNextLine()){
        String[] observation = test.nextLine().split(" ");
        if(observation.length == 0)
          break;
        for(int s : model.getViterbiDecoding(observation))
          System.out.format("%d ", s+1);
        System.out.println();
      }
    }
    catch(Exception e){
      System.out.println("Usage: java Solution <model file> <observations file>");
      System.exit(-1);
    }
  }
}
