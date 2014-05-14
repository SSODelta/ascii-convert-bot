package com.ignatieff.converter;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

public class ASCIIConverter {
	
	String[] contents;
	
	Segmentation segment;
	
	public static double CONV_FACTOR = 0.77906976744;
	
	public ASCIIConverter(String path){
		this(readImage(path),4);
	}
	
	public static BufferedImage readImage(String path){
		try {
			return ImageIO.read(new File(path));
		} catch (IOException e) {
			return null;
		}
	}
	
	public ASCIIConverter(BufferedImage img, int gridScalar){
		RescaleOp rescaleOp = new RescaleOp(1.35f, 15, null);
		rescaleOp.filter(img, img);
		segment = new Segmentation(img, gridScalar);
		contents = segment.getBestMatching();
	}
	
	public void saveTextFile(String path){
		try {
			PrintWriter out = new PrintWriter(path);
			for(int i=0; i<contents.length; i++){
				out.println(contents[i]);
			}
			out.close();
		} catch (FileNotFoundException e) {
			
		}
		
	}
	
	public void saveImageFile(String path){
		try {
			ImageIO.write(this.toImage(8), "png", new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public BufferedImage toImage(int fontSize){
		Font f = new Font("Courier", Font.PLAIN, fontSize);
		Canvas c = new Canvas();
		FontMetrics fm = c.getFontMetrics(f);
		
		int h = fm.getHeight();
		int w = fm.stringWidth("M");
		
		BufferedImage img = new BufferedImage((int)(w*segment.blocksX/CONV_FACTOR), h*segment.blocksY, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		g.setColor(new Color(255,255,255));
		g.fillRect(0,0,(int)(w*segment.blocksX/CONV_FACTOR),h*segment.blocksY);
		g.setColor(new Color(0,0,0));
		g.setFont(f);
		
		for(int y=0; y<contents.length; y++){
			char[] q = contents[y].toCharArray();
			if(q.length==0)continue;
			for(int x=0; x<segment.blocksX; x++){
				g.drawString(""+q[x], (int)(x*w/CONV_FACTOR), y*h);
			}
		}
		
		return img;
	}
}
