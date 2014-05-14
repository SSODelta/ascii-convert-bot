package com.ignatieff.converter;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Segmentation {
	
	private double[][] data;
	double min, max;
	private static double CONV_FACTOR = 0.77906976744;
	public int blocksX, blocksY;
	
	public Segmentation(BufferedImage img, int gridSize){
		blocksX = (int)Math.floor(img.getWidth()/gridSize)-1;
		blocksY = (int)(Math.floor(img.getHeight()/((double)gridSize)));
		
		min=0;
		max=0;
		
		data = new double[blocksX][blocksY];
		
		for(int x=0; x<blocksX; x++){
			for(int y=0; y<blocksY; y++){
				if((gridSize*(y+CONV_FACTOR))>=img.getHeight()){data[x][y]=-1;continue;}
				double d = getAverageColor(img.getSubimage(gridSize*x, gridSize*y, gridSize, (int) (gridSize)));
				if(d<min)min=d;
				if(d>max)max=d;
				data[x][y] = d;
			}
		}
	}
	
	public String[] getBestMatching(){
		String[] strings = new String[blocksY];
		for(int y=0; y<blocksY; y++){
			StringBuilder s = new StringBuilder();
			for(int x=0; x<blocksX; x++){
				if(data[x][y]==-1)continue;
				char q = CharSet.getBestChar((data[x][y]-min)/max);
				s.append(q);
			}
			strings[y]=s.toString();
		}
		return strings;
	}
	
	public static double getAverageColor(BufferedImage img){
		double buffer = 0;
		for(int x=0; x<img.getWidth(); x++){
			for(int y=0; y<img.getHeight(); y++){
				double d=getDensity(new Color(img.getRGB(x, y)));
				buffer+=d;
			}
		}
		return 1-buffer/(img.getWidth()*img.getHeight());
	}
	
	public static double invGammaSRGB(int color){
		double c = color/255.0;
	    if ( c <= 0.04045 )
	        return c/12.92;
	    else 
	        return Math.pow(((c+0.055)/(1.055)),2.4);
	}
	
	public static double getDensity(Color c){
		return	0.212655*invGammaSRGB(c.getRed()) +
				0.715158*invGammaSRGB(c.getGreen()) +
				0.072187*invGammaSRGB(c.getBlue());
	}
}
