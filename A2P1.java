import java.util.*;
import java.io.*;

public class A2P1 {
  /**
  This function looks for the element in question (x) by comparing it to the rightmost element
  in each row. Once we hit an element whose value is greater than x, we search in a zig-zag fashion until
  we hit the element we are looking for or we run out of bounds. Therefore from a theoretical standpoint, my function has a
  worst case runtime of O(2n) which is linear with respect to the size of the matrix as that would entail a linear traversal
  down the rightmost column and another linear traversal up the last row.
  **/
  public static boolean inMatrix(int[][] matrix, int x){
    int col = matrix.length - 1;
    for (int row = 0; row < col + 1; row++) {
      if (matrix[row][col] == x) {
        return true;
      }
      if (matrix[row][col] > x) {
        while (row < matrix.length && col >= 0) {
          if (matrix[row][col] == x) {
            return true;
          } else if (matrix[row][col] < x) {
            row++;
          } else {
            col--;
          }
        }
        return false;
      }
    }
    return false;
  }

  /**
  This is a simple O(n^2) function to print a 2-D array
  **/
  public static void printMatrix(int[][] matrix) {
    for (int row = 0; row < matrix.length; row++) {
      for (int col = 0; col < matrix.length; col++) {
        System.out.print(matrix[row][col] + " ");
      }
      System.out.println("");
    }
  }

  /**
  This is a slow O(n^2) function that I am only running a couple times to make some small and large matrices. This function
  makes sure that the matrix follows the specified rules for this project.
  **/
  public static void makeMatrix(int dimension) {
    File delete = new File("matrix.txt");
    delete.delete();
    String matrix = "";
    int[][] matrixSaver = new int[dimension][dimension];
    Random random = new Random();
    try {
      File matrixFile = new File("matrix.txt");
      FileWriter writer = new FileWriter(matrixFile);
      matrixFile.createNewFile();
      for (int row = 0; row < dimension; row++) {
        for (int col = 0; col < dimension; col++) {
          if (row == 0 && col == 0) {
            matrixSaver[row][col] = random.nextInt(48) + 2;
            matrix += matrixSaver[row][col] + " ";
          } else if (row == 0 && col != 0) {
            matrixSaver[row][col] = (matrixSaver[row][col - 1] + matrixSaver[row][col - 1] + random.nextInt(48) + 2) / 2;
            matrix += matrixSaver[row][col] + " ";
          } else if (row != 0 && col == 0) {
            matrixSaver[row][col] = (matrixSaver[row - 1][col] + matrixSaver[row - 1][col] + random.nextInt(48) + 2) / 2;
            matrix += matrixSaver[row][col] + " ";
          } else {
            int difference = Math.abs(matrixSaver[row - 1][col] - matrixSaver[row][col - 1]) + 2;
            matrixSaver[row][col] = (matrixSaver[row - 1][col] + matrixSaver[row][col - 1] + random.nextInt(50 - difference) + difference) / 2;
            matrix += matrixSaver[row][col] + " ";
          }
        }
        matrix += "\n";
      }
      writer.write(matrix);
      writer.close();
    } catch (IOException e) {
      System.err.println(e);
    }
  }

  /**
  I wrote this function to test the time complexity of inMatrix by writing the dimensions and runtime to a txt file that will be pasted to excel.
  The idea was that I will create a graph that will hopefully show linear growth. However, excel only allows for a max of 255 elements instead of the 1000 I wanted.
  **/
  public static void runtest(int times, int dimensions, int[][] matrix) {
    File delete = new File("results.txt");
    delete.delete();
    String results = "";
    Random random = new Random();
    try {
      File resultsFile = new File("results.txt");
      FileWriter writer = new FileWriter(resultsFile);
      resultsFile.createNewFile();
      while (times > 0) {
        int target = matrix[random.nextInt(dimensions)][random.nextInt(dimensions)] + random.nextInt(1);
        long prevTime = System.nanoTime();
        inMatrix(matrix, target);
        long postTime = System.nanoTime();
        results += dimensions + " " + (postTime - prevTime) + "\n";
        times--;
      }
      writer.write(results);
      writer.close();
    } catch (IOException e) {
      System.err.println(e);
    }
  }

  /**
  This function will attempt to perform a large amount to tests on inMatrix and average the runtime of all of those calls.
  This way, I can have a large sample size without the restrictions from excel.
  **/
  public static void averageRuntimeTest(int times, int dimensions, int[][] matrix) {
    long sumOfRuntime = 0;
    Random random = new Random();
    int initialTimes = times;
    while (times > 0) {
      int target = matrix[random.nextInt(dimensions)][random.nextInt(dimensions)] + random.nextInt(1);
      long prevTime = System.nanoTime();
      inMatrix(matrix, target);
      long postTime = System.nanoTime();
      sumOfRuntime += (postTime - prevTime);
      times--;
    }
    System.out.println("Dimensions of Matrix: " + dimensions + "\n" + "Average runtime of " + initialTimes + " tests: " + (sumOfRuntime / initialTimes) + " nanoseconds.");
  }

  /**
  This function will test whether or not inMatrix works on large matrices.
  **/
  public static void functionalityTest(int times, int dimensions, int[][] matrix) {
    Random random = new Random();
    while (times > 0) {
      int target = matrix[random.nextInt(dimensions)][random.nextInt(dimensions)];
      if (!inMatrix(matrix, target)) {
        System.out.println("test failed!");
      }
      times--;
    }
  }

  public static void main(String[] args) {
    try {
      int matrixDimensions = Integer.parseInt(args[0]);
      int[][] matrix = new int[matrixDimensions][matrixDimensions];
      String matrixFileName = args[1];
      File matrixFile = new File(matrixFileName);
      Scanner reader = new Scanner(matrixFile);
      int rowIndex = 0;
      while (reader.hasNextLine()) {
        String[] rowElements = reader.nextLine().split(" ");
        for (int colIndex = 0; colIndex < matrixDimensions; colIndex++) {
          matrix[rowIndex][colIndex] = Integer.parseInt(rowElements[colIndex]);
        }
        rowIndex++;
      }
      reader.close();
      // printMatrix(matrix);
      Random random = new Random();
      System.out.println();
      System.out.println("-----------------------------------");
      System.out.println("RESULTS: ");
      System.out.println("Matrix dimensions: " + matrixDimensions);
      int target = matrix[random.nextInt(matrixDimensions)][random.nextInt(matrixDimensions)] + random.nextInt(1);
      System.out.println("Value we are looking for: " + target);
      long prevTime = System.nanoTime();
      boolean results = inMatrix(matrix, target);
      long postTime = System.nanoTime();
      // note that this printed runtime is actually longer than the runtimes I found in my testing functions.
      // I do not know the reason for this but it could be main function specific.
      System.out.println("Result of inMatrix: " + results);
      System.out.println("Running time in nanoseconds: " + (postTime - prevTime));
      System.out.println("-----------------------------------");
      //runtest(100, matrixDimensions, matrix);
      //averageRuntimeTest(2000, matrixDimensions, matrix);
      //functionalityTest(2000, matrixDimensions, matrix); // at the moment this test returns true all the time
    } catch (NumberFormatException e) {
      System.out.println("Enter matrix dimensions followed by name of file containing matrix.");
    } catch (FileNotFoundException e) {
      System.out.println("Enter a valid file name.");
    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.println("Enter correct inputs into main function: matrix dimensions followed by name of file containing matrix.");
    }
  }
}
