import java.util.Arrays;
import java.io.*;
/*
  Hidden Markov Model class
  Implements the Viterbi Decoding algorithm
*/
public class HMM{

  private String[] symbols;
  private double[] initial;
  private double[][] transitions;
  private double[][] outputDistribution;

  public HMM(String[] symbols, double[] initial, double[][] transitions, double[][] outputDistribution){
    this.symbols = Arrays.copyOf(symbols, symbols.length);
    this.initial = Arrays.copyOf(initial, initial.length);
    this.transitions = new double[transitions.length][];
    for(int i = 0; i < transitions.length; i++){
      this.transitions[i] = Arrays.copyOf(transitions[i], transitions[i].length);
    }
    this.outputDistribution = new double[outputDistribution.length][];
    for(int i = 0; i < transitions.length; i++){
      this.outputDistribution[i] = Arrays.copyOf(outputDistribution[i], outputDistribution[i].length);
    }
  }

  // get the index of the given observation
  private int getSymbolIndex(String observation){
    for(int i = 0; i < symbols.length; i++){
      if(symbols[i].equals(observation))
        return i;
    }
    return -1;
  }

  // get the probabilities and parents of the states
  private void buildViterbiProbabilities(double[][] probs, int[][] parents, String[] observation){
    // build first column of probabilities
    int index = getSymbolIndex(observation[0]);
    for(int i = 0; i < initial.length; i++){
      probs[i][0] = initial[i]*outputDistribution[i][index];
    }

    // build the rest of the probabilities
    for(int k = 1; k < observation.length; k++){
      // current time stamp is k
      index = getSymbolIndex(observation[k]);
      for(int i = 0; i < initial.length; i++){
        // current state is i
        double currentMax = -1;
        int parent = -1;
        for(int j = 0; j < initial.length; j++){
          // checking state j as a potential parent
          double tempProb = probs[j][k-1]*transitions[j][i]*outputDistribution[i][index];
          if(tempProb > currentMax){
            currentMax = tempProb;
            parent = j;
          }
        }
        probs[i][k] = currentMax;
        parents[i][k-1] = parent;
      }
    }
  }

  // get the viterbi state sequence given the probabilities and parents
  private int[] getViterbiStateSequence(double[][] probs, int[][] parents){
    int[] states = new int[probs[0].length];
    int curState = 0;
    int index = probs[0].length-1;
    // get the last state (state with highest probability at last time stamp)
    for(int k = 1; k < initial.length; k++){
      if(probs[k][index] > probs[curState][index]){
        curState = k;
      }
    }
    states[index] = curState;
    // get the rest of the states
    for(int k = states.length-2; k >= 0; k--){
      states[k] = parents[states[k+1]][k];
    }
    return states;
  }

  // get the most probable state sequence given an observation
  public int[] getViterbiDecoding(String[] observation){
    double[][] probs = new double[initial.length][observation.length];
    int[][] parents = new int[initial.length][observation.length-1];

    buildViterbiProbabilities(probs, parents, observation);
    return getViterbiStateSequence(probs, parents);
  }
}
