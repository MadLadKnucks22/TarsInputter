package neuralnet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Matrix implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5759163320214990544L;
	
	protected double [][]data;
	public int rows,cols;
	
	public Matrix(int rows,int cols){
		data= new double[rows][cols];
		this.rows=rows;
		this.cols=cols;
	}
	
	public void setMatrixValue(int row, int col, double value) {
		this.data[row][col] = value;
	}
	
	
	public static Matrix intilizeRandomMatrix(int rows, int cols) {
		
		Matrix m = new Matrix(rows, cols);
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				m.data[i][j] = Math.random()*2-1;
			}
		}
		return m;
	}
	
	public static Matrix intilizeOnesMatrix(int size) {
		Matrix m = new Matrix(size, size);
		
		for(int i = 0; i < size; i++) {
    		m.data[i][i] = 1;
    	}
		return m;
		
	}
	
	 public static double[][] genOnesArray(int size){
	    	double[][] data = new double[size][size];
	    	
	    	for(int i = 0; i < size; i++) {
	    		data[i][i] = 1;
	    	}
	    	return data;
	    	
	    }
	
	public void add(double value) {
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				data[i][j] += value;
			}
		}
	}
	
	public void add(Matrix m) {
		if(cols!=m.cols || rows!=m.rows) {
	        System.out.println("Shape Mismatch");
	        return;
	    }
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				data[i][j] += m.data[i][j];
			}
		}
	}
	
	public static Matrix subtract(Matrix a, Matrix b) {
        Matrix temp=new Matrix(a.rows,a.cols);
        for(int i=0;i<a.rows;i++)
        {
            for(int j=0;j<a.cols;j++)
            {
                temp.data[i][j]=a.data[i][j]-b.data[i][j];
            }
        }
        return temp;
    }
	
	public void subtract(Matrix m) {
		for(int i=0;i<m.rows;i++)
        {
            for(int j=0;j<m.cols;j++)
            {
                data[i][j]=data[i][j]-m.data[i][j];
            }
        }
	}
	
	public static Matrix transpose(Matrix a) {
        Matrix temp=new Matrix(a.cols,a.rows);
        for(int i=0;i<a.rows;i++)
        {
            for(int j=0;j<a.cols;j++)
            {
                temp.data[j][i]=a.data[i][j];
            }
        }
        return temp;
    }
	
	public static Matrix multiply(Matrix a, Matrix b) {
        Matrix temp=new Matrix(a.rows,b.cols);
        for(int i=0;i<temp.rows;i++)
        {
            for(int j=0;j<temp.cols;j++)
            {
                double sum=0;
                for(int k=0;k<a.cols;k++)
                {
                    sum+=a.data[i][k]*b.data[k][j];
                }
                temp.data[i][j]=sum;
            }
        }
        return temp;
    }
    
    public void multiply(Matrix a) {
        for(int i=0;i<a.rows;i++)
        {
            for(int j=0;j<a.cols;j++)
            {
                this.data[i][j]*=a.data[i][j];
            }
        }
        
    }
    
    public void multiply(double a) {
        for(int i=0;i<rows;i++)
        {
            for(int j=0;j<cols;j++)
            {
                this.data[i][j]*=a;
            }
        }
        
    }
    
    public void sigmoid() {
        for(int i=0;i<rows;i++)
        {
            for(int j=0;j<cols;j++)
                this.data[i][j] = 1/(1+Math.exp(-this.data[i][j])); 
        }
        
    }
    
    public Matrix dsigmoid() {
        Matrix temp=new Matrix(rows,cols);
        for(int i=0;i<rows;i++)
        {
            for(int j=0;j<cols;j++)
                temp.data[i][j] = this.data[i][j] * (1-this.data[i][j]);
        }
        return temp;
        
    }
    
    
    public static Matrix fromArray(double[]x)
    {
        Matrix temp = new Matrix(x.length,1);
        for(int i =0;i<x.length;i++)
            temp.data[i][0]=x[i];
        return temp;
        
    }
    
    public List<Double> toArray() {
        List<Double> temp= new ArrayList<Double>()  ;
        
        for(int i=0;i<rows;i++)
        {
            for(int j=0;j<cols;j++)
            {
                temp.add(data[i][j]);
            }
        }
        return temp;
   }
    
	
	public static void printMatrix(Matrix m) {
		for(int row = 0; row < m.rows; row++) {
			for(int col = 0; col < m.cols; col++) {
				System.out.print("[" + String.format("%.03f", m.data[row][col]) + "]");
			}
			System.out.println();
		}
	}
	
	public void printMatrix() {
		printMatrix(this);
	}
	
	

	
}
