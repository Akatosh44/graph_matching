package graph_matching;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.ejml.simple.SimpleMatrix;

public class ImportGraph {

	public static SimpleMatrix importAdjFromCSV(String fileName){
		String csvFile = fileName+".csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ";";
		SimpleMatrix matResult=null;
		try {
			br = new BufferedReader(new FileReader(csvFile));
			line = br.readLine();
			String[] matval = line.split(cvsSplitBy);
			int width=Integer.parseInt(matval[0]);
			int height=Integer.parseInt(matval[1]);
			matResult= new SimpleMatrix(width,height);
			int row=0;
			int col=0;
			while ((line = br.readLine()) != null) {
				matval = line.split(cvsSplitBy);
				for(col=0;col<width;col++){
					matResult.set(row, col, Double.parseDouble(matval[col]));
				}
				row++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		double max=0;
		for(int i=0;i<matResult.getMatrix().getNumElements();i++){
			if(matResult.get(i)>max){
				max=matResult.get(i);
			}
		}
		for(int i=0;i<matResult.getMatrix().getNumElements();i++){
				matResult.set(i,matResult.get(i)/max);
		}
		return matResult;
	  }
	public static void normalize(SimpleMatrix matM, SimpleMatrix matS){
		double max=0;
		for(int i=0;i<matM.getMatrix().getNumElements();i++){
			if(matM.get(i)>max){
				max=matM.get(i);
			}
		}
		for(int i=0;i<matS.getMatrix().getNumElements();i++){
			matS.set(i,matS.get(i)/max);
		}
		
		for(int i=0;i<matM.getMatrix().getNumElements();i++){
			matM.set(i,matM.get(i)/max);
		}
		
		
	}
	public static SimpleMatrix importGraphFromCSV(String fileName){
		String csvFile = fileName+".csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ";";
		String[] value;
		ArrayList<Point2D> content=new ArrayList<Point2D>();
		try {
			br = new BufferedReader(new FileReader(csvFile));
			br.readLine();
			while ((line = br.readLine()) != null) {
				value = line.split(cvsSplitBy);
				Point2D result=new Point2D(Double.parseDouble(value[0]),Double.parseDouble(value[1]));
				content.add(result);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return parseDataFromImport(content);
	}
	public static void exportMatrixToCSV(String fileName, SimpleMatrix matrix){
		String csvFile = fileName+".csv";
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(csvFile));
			bw.write("Source;Target;Type");
			bw.newLine();
			for(int i=0;i<matrix.getMatrix().getNumRows();i++){
				for(int j=0;j<i;j++){
					if(matrix.get(i,j)==1){
						bw.write((i+1)+";"+(j+1)+";undirected");
						bw.newLine();
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	private static SimpleMatrix parseDataFromImport(ArrayList<Point2D> content){
		double sizeMax=0;
		
		for(int i=0;i<content.size();i++){
			if(content.get(i).getCordx()>sizeMax){
				sizeMax=content.get(i).getCordx();
			}else if(content.get(i).getCordy()>sizeMax){
				sizeMax=content.get(i).getCordy();
			}
		}
		double sizeMin=sizeMax;
		for(int i=0;i<content.size();i++){
			if(content.get(i).getCordx()<sizeMin){
				sizeMin=content.get(i).getCordx();
			}else if(content.get(i).getCordy()<sizeMin){
				sizeMin=content.get(i).getCordy();
			}
		}
		System.out.println(sizeMax+",,,"+sizeMin);
		double size=sizeMax-sizeMin+1;
		System.out.println(content);
		SimpleMatrix result=new SimpleMatrix((int)size,(int)size);
		for(int i=0;i<content.size();i++){
			result.set((int)content.get(i).getCordy()-(int)sizeMin, (int)content.get(i).getCordx()-(int)sizeMin, 1);
			result.set((int)content.get(i).getCordx()-(int)sizeMin, (int)content.get(i).getCordy()-(int)sizeMin, 1);
		}
		return result;
	}
	
	public static SimpleMatrix removeZeros(SimpleMatrix M){
		
		ArrayList<ArrayList<Double>> values=new ArrayList<ArrayList<Double>>();
		ArrayList<Integer> rowsZero=new ArrayList<Integer>();
		
		boolean noZero=false;
		int nRows=0;
		int nCols=0;
		for(int j=0;j<M.getMatrix().getNumRows();j++){
			noZero=false;
			for(int i=0;i<M.getMatrix().getNumCols();i++){
				if(M.get(j, i)>0){
					noZero=true;
				}
			}
			if(noZero){
				rowsZero.add(j);
			}
		}
		System.out.println(rowsZero);
		for(int j=0;j<rowsZero.size();j++){
			ArrayList<Double> rowValues=new ArrayList<Double>();
			for(int i=0;i<rowsZero.size();i++){
				rowValues.add(M.get(rowsZero.get(j), rowsZero.get(i)));
			}
			values.add(rowValues);

		}
		
		SimpleMatrix result=new SimpleMatrix(values.get(0).size(),values.get(0).size());
		for(int j=0;j<result.getMatrix().getNumRows();j++){
			for(int i=0;i<result.getMatrix().getNumCols();i++){
				result.set(j, i, values.get(j).get(i));
			}
		}
		System.out.println(values);
		return result;
			
			
	}
	public static void exportArray(ArrayList<Double> values){
		String csvFile = "data/result/BT.csv";
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(csvFile));
			for(int i=0;i<values.size();i++){
				bw.write(values.get(i).toString()+";");
			}
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static SimpleMatrix toBinary(SimpleMatrix mat){
		SimpleMatrix result=new SimpleMatrix(mat.getMatrix().getNumRows(),mat.getMatrix().getNumCols());
		for(int i=0;i<mat.getNumElements();i++){
			if(mat.get(i)>0){
				result.set(i,1);
			}else{
				result.set(i,0);
			}
		}
		return result;
	}
}

