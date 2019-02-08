import java.util.Arrays;
import java.util.stream.*;
import java.util.Collections;
import java.util.Scanner;
import java.text.DecimalFormat;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

class InsufficientElementsException extends Exception
{
    public InsufficientElementsException()
    {
        super("Insufficient number of elements");
    }
}

class InvalidRowNumberException extends Exception
{
    public InvalidRowNumberException()
    {
        super("Invalid row number");
    }
}

class InvalidColumnNumberException extends Exception
{
    public InvalidColumnNumberException()
    {
        super("Invalid column number");
    }
}


final class DoubleMatrix 

{
   private double[][] matrix;
   private Integer m, n;
   
   public DoubleMatrix(double a[], int m, int n)
       throws InsufficientElementsException
   {
       if(a.length<m*n)
       {
           throw new InsufficientElementsException();
       }
       
       double[][] temp=new double[m][n];
       
       int offset=0;
       
       if(a.length>m*n)
       {
          offset=a.length-m*n;
       }
       
       for(int i=0; i<m; i++)
       {
           for(int j=0; j<n; j++)
           {
               temp[i][j]=a[i*n+j+offset];
               /*System.out.print(temp[i][j]);System.out.print("/");System.out.print(a[i*m+j]);
               System.out.println("|");*/
           }
          //System.out.println();
       }
	   
       this.matrix=temp;
       this.m=m;
       this.n=n;
   }
    
   public String getDimensions()
   {
       StringBuilder s=new StringBuilder();
       s.append("[");
       s.append(this.m.toString());
       s.append(" x ");
       s.append(this.n.toString());
       s.append("]");
       
       return s.toString();
   }
    
    public int rows()
    {
        return this.m;
    }
    
    public int columns()
    {
        return this.n;
    }
    
    public double maxElementAtRow(int row)
        throws InvalidRowNumberException
    {
        if(row>this.m || row<=0)
        {
            throw new InvalidRowNumberException();
        }
        
        double max=this.matrix[row-1][0];
        
        for(int i=0; i<this.n; i++)
        {
            if(this.matrix[row-1][i]>max)
            {
                max=this.matrix[row-1][i];
            }
        }
        return max;
        
    }
    
    
    public double maxElementAtColumn(int column)
        throws InvalidColumnNumberException
    {
        if(column>this.n || column<=0)
        {
            throw new InvalidColumnNumberException();
        }
        
        double max=this.matrix[0][column-1];
        
        
        for(int i=1; i<this.m; i++)
        {
            if(this.matrix[i][column-1]>max)
            {
                max=this.matrix[i][column-1];
            }
        }
        
        return max;
    
    }
    
    public double sum()
    {
       return Arrays.stream(this.matrix).mapToDouble(row->Arrays.stream(row).sum()).sum();
       /*double sum=0.0;
       for(int i=0; i<this.m; i++)
       {
           for(int j=0; j<this.n; j++)
           {
               sum+=this.matrix[i][j];
           }
       }
       
       return sum;*/
    }
    
    public double[] toSortedArray()
    {
        double[]result=new double[this.m*this.n];
        double[][]temp=Arrays.copyOf(this.matrix, this.m*this.n);
        
        for(int i=0; i<this.m; i++)
        {
            for(int j=0; j<this.n; j++)
            {
                result[i*n+j]=temp[i][j];
                //System.out.print(result[i*m+j]);System.out.print("/");System.out.print(temp[i][j]);System.out.print("|");
            }
            //System.out.println();
        }
        
        Arrays.sort(result);
        
        for(int i=0; i<this.m*this.n; i++)
        {
            /*
            System.out.print(result[i]);System.out.print("|");
            if(i%n==n-1)
                System.out.println();*/
        }
        
        for(int i=0; i<n*m/2; i++)
        {
            double swap;
            swap=result[i];
            result[i]=result[n*m-i-1];
            result[n*m-i-1]=swap;
        }
        
        
        return result;
    }
    
    public String toString()
    {
        StringBuilder s=new StringBuilder();
        DecimalFormat formatter=new DecimalFormat("#0.00");
        
        for(int i=0; i<this.m; i++)
        {
            for(int j=0; j<this.n; j++)
            {
                s.append(formatter.format(this.matrix[i][j]));
                
                if(j!=n-1)
                	s.append("\t");
            }
            
            if(i!=m-1)
            	s.append("\n");
        }
        
        
        return s.toString();
    }
    
    public boolean equals(Object o)
    {
        if(o instanceof DoubleMatrix)
        {
            DoubleMatrix rhs=(DoubleMatrix)o;
            if(this.m!=rhs.m || this.n!=rhs.n)
            {
                return false;
            }
            
            for(int i=0; i<this.m; i++)
            {
                for(int j=0; j<this.n; j++)
                {
                    if(this.matrix[i][j]!=rhs.matrix[i][j])
                        return false;
                }
            }
            
            return true;
        }
        
        else
        {
            return false;
        }
    }
    
    public int hashCode()
    {
        int hash=m.hashCode()*n.hashCode();
        for(int i=0; i<this.m; i++)
        {
            for(int j=0; j<this.n; j++)
            {
                hash*=((Double)this.matrix[i][j]).hashCode();
            }
        }
        
        return hash;
    }
}

class MatrixReader
{
    public static DoubleMatrix read(InputStream input)
        throws InsufficientElementsException
    {
        Scanner scanner=new Scanner(input);
        int m, n;
        m=scanner.nextInt();
        n=scanner.nextInt();
        
        double[]temp=new double[m*n];
        for(int i=0; i<m; i++)
        {
            for(int j=0; j<n; j++)
            {
                temp[i*n+j]=scanner.nextDouble();
            }
        }
        
        DoubleMatrix result=new DoubleMatrix(temp, m, n);
        
        return result;
    }
}

public class DoubleMatrixTester {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        DoubleMatrix fm = null;

        double[] info = null;

        DecimalFormat format = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            String operation = scanner.next();

            switch (operation) {
                case "READ": {
                    int N = scanner.nextInt();
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    double[] f = new double[N];

                    for (int i = 0; i < f.length; i++)
                        f[i] = scanner.nextDouble();

                    try {
                        fm = new DoubleMatrix(f, R, C);
                        info = Arrays.copyOf(f, f.length);

                    } catch (InsufficientElementsException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }

                    break;
                }

                case "INPUT_TEST": {
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    StringBuilder sb = new StringBuilder();

                    sb.append(R + " " + C + "\n");

                    scanner.nextLine();

                    for (int i = 0; i < R; i++)
                        sb.append(scanner.nextLine() + "\n");

                    fm = MatrixReader.read(new ByteArrayInputStream(sb
                            .toString().getBytes()));

                    info = new double[R * C];
                    Scanner tempScanner = new Scanner(new ByteArrayInputStream(sb
                            .toString().getBytes()));
                    tempScanner.nextDouble();
                    tempScanner.nextDouble();
                    for (int z = 0; z < R * C; z++) {
                        info[z] = tempScanner.nextDouble();
                    }

                    tempScanner.close();

                    break;
                }

                case "PRINT": {
                    System.out.println(fm.toString());
                    break;
                }

                case "DIMENSION": {
                    System.out.println("Dimensions: " + fm.getDimensions());
                    break;
                }

                case "COUNT_ROWS": {
                    System.out.println("Rows: " + fm.rows());
                    break;
                }

                case "COUNT_COLUMNS": {
                    System.out.println("Columns: " + fm.columns());
                    break;
                }

                case "MAX_IN_ROW": {
                    int row = scanner.nextInt();
                    try {
                        System.out.println("Max in row: "
                                + format.format(fm.maxElementAtRow(row)));
                    } catch (InvalidRowNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "MAX_IN_COLUMN": {
                    int col = scanner.nextInt();
                    try {
                        System.out.println("Max in column: "
                                + format.format(fm.maxElementAtColumn(col)));
                    } catch (InvalidColumnNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "SUM": {
                    System.out.println("Sum: " + format.format(fm.sum()));
                    break;
                }

                case "CHECK_EQUALS": {
                    int val = scanner.nextInt();

                    int maxOps = val % 7;

                    for (int z = 0; z < maxOps; z++) {
                        double work[] = Arrays.copyOf(info, info.length);

                        int e1 = (31 * z + 7 * val + 3 * maxOps) % info.length;
                        int e2 = (17 * z + 3 * val + 7 * maxOps) % info.length;

                        if (e1 > e2) {
                            double temp = work[e1];
                            work[e1] = work[e2];
                            work[e2] = temp;
                        }

                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(work, fm.rows(),
                                fm.columns());
                        System.out
                                .println("Equals check 1: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode()&&f1
                                        .equals(f2)));
                    }

                    if (maxOps % 2 == 0) {
                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(new double[]{3.0, 5.0,
                                7.5}, 1, 1);

                        System.out
                                .println("Equals check 2: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode()&&f1
                                        .equals(f2)));
                    }

                    break;
                }

                case "SORTED_ARRAY": {
                    double[] arr = fm.toSortedArray();

                    String arrayString = "[";

                    if (arr.length > 0)
                        arrayString += format.format(arr[0]) + "";

                    for (int i = 1; i < arr.length; i++)
                        arrayString += ", " + format.format(arr[i]);

                    arrayString += "]";

                    System.out.println("Sorted array: " + arrayString);
                    break;
                }

            }

        }

        scanner.close();
    }
}
