package com.ignatieff.converter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

public class GIFConverter {
	public static void convertGIF(String file, String output, int gridScalar) throws IOException{
		ImageReader reader = (ImageReader)ImageIO.getImageReadersByFormatName("gif").next();
        ImageInputStream ciis = ImageIO.createImageInputStream(new File(file));
        reader.setInput(ciis, false);
		ImageOutputStream outStream = new FileImageOutputStream(new File(output));
		GIFSequenceWriter gifWriter = null;
		int i=1;
		while(true){
			ImageFrame frame = ImageFrame.readGIF(reader, i);
			if(frame==null){break;}
			ASCIIConverter a = new ASCIIConverter(frame.getImage(), gridScalar);
			BufferedImage ascii = a.toImage(10);
			if(gifWriter == null){
				gifWriter = new GIFSequenceWriter(outStream, frame.getImage().getType(), frame.getDelay(), true);
			}
			gifWriter.writeToSequence(ascii);
			i++;
		}
		if(gifWriter == null){
			System.out.println("Error converting .GIF...");
		}
		gifWriter.close();
	}
	
}
