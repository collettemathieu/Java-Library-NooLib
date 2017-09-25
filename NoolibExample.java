import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class NoolibExample 
{
	static public double[][] randomDArray = {{2, 3},{4, 5}};
	static public String[][] randomSArray = {{"r","ra", "ran"},{"rand","rando","random"}};
	static public String[] tableLegends = {"randomTitle", "randomTitle2"};
	static public String[] tableLegends2 = {"ranTitle1", "ranTitle2", "ranTitle3"};
	
	public static void main(String[] args) 
	{			
		/** 
		 * 	Create a Noolib object before ANY other instructions: 
		 * 	DEBUG = test mode (output stream like System.out are enable
		 *  RELEASE = use this mode when you want to import your jar on noolib.com 
		 */
		Noolib noo = new Noolib(Noolib.RELEASE); 

		/** The following lines are the outputs of your program **/
		
		/** You can print tables, with vertical and horizontal legend */
		noo.addTable(randomSArray, tableLegends);
		noo.addTable(randomSArray, tableLegends, tableLegends2);
		
		/** You can print tables */
		noo.addGraph(randomDArray, tableLegends);
		noo.addGraph(randomDArray, tableLegends);
		
		/** You can send images */
		BufferedImage image = new BufferedImage(1, 10, BufferedImage.TYPE_BYTE_BINARY);
		noo.addImage(image, "MyImage", "png");	
		noo.addImage(image, "MyImageCopy", "png");	
		
		/** You can send files */
		File csvFile = noo.doubleArrayToCsv(randomDArray);
		noo.addFile(csvFile, "MyFileCsv", "csv");	
		noo.addFile(csvFile, "MyFileCsvCopy", "csv");	
		
		/** You can send results */
		noo.addResult("This result is important", ((5+9)*3) );
		noo.addResult("Another result", "i'm a result");
		noo.addResult("A result with max min and thresholds", (3*10/5), 5, 55, 18.5, 25);
		noo.addResult("Execution time", noo.getExecutionTime());
		
		/** Write some comments */ 
		noo.addComment("This is a small information about my program");
		noo.addComment("Another information");
	
		/** Finally send all the data to noolib */
		noo.sendData();

	}
}
