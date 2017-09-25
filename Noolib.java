import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import javax.imageio.ImageIO;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

/**
 *  _   _             _      _ _     	
 * | \ | |           | |    (_) |    	
 * |  \| | ___   ___ | |     _| |__  
 * | . ` |/ _ \ / _ \| |    | | '_ \ 	
 * | |\  | (_) | (_) | |____| | |_) |	
 * |_| \_|\___/ \___/|______|_|_.__/ 	
 * 										
 */
/** 
 * NOOLIB LIBRARY <p>
 * 
 * https://www.noolib.com
 *
 * @author Quentin DENIS contact@quentindenis.com 
 */

public class Noolib 
{
	/** Output Data */
	public static final int DEBUG = 0;
	public static final int RELEASE = 1;
	private ArrayList<String> commentsN;
	private ArrayList<double[][]> graphsN;
	private ArrayList<String[]> graphsLegendsN;
	private ArrayList<String[][]> tablesN;
	private ArrayList<String[]> tablesVerticalLegendsN;
	private ArrayList<String[]> tablesHorizontalLegendsN;
	private ArrayList<String> imagesN;
	private ArrayList<String> imagesNameN;
	private ArrayList<String> imagesExtensionN;
	private ArrayList<String> filesN;
	private ArrayList<String> filesNameN;
	private ArrayList<String> filesExtensionN;
	private ArrayList<String> resultsN;
	private ArrayList<String> resultsNameN;
	private ArrayList<String> resultsMinN;
	private ArrayList<String> resultsMaxN;
	private ArrayList<String> resultsThresholdMinN;
	private ArrayList<String> resultsThresholdMaxN;
	private ArrayList<String> errorsN;
	/** END Output Data */
	
	private PrintStream originalStream = System.out;
	private PrintStream muteStream = new PrintStream(new OutputStream(){public void write(int b) {}});
	private boolean logIsOn = true;
	private long startTime;
	
	
	/** 
	 * 	Create a Noolib object before ANY other instructions<br>
	 * 	@param 	ExecutionMode Noolib.DEBUG or Noolib.RELEASE <br><p>
	 * 	DEBUG => test mode<br>
	 * 	RELEASE => use this mode when you want to import your jar on noolib.com<br><p>
	 * 
	 *  The debug mode allow you to print whatever you want in the console. <br>
	 *  In order to ensure compatibility with all programming languages Noolib <br>
	 *  use the console to get the output of the program. That's why when the <br>
	 *  Release mode is enable all the output streams like System.out are disable. <br>
	 *  */
	public Noolib(int executionMode)
	{ 
		startTime = System.nanoTime();
		graphsN = new ArrayList<double[][]>();
		graphsLegendsN = new ArrayList<String[]>();
		tablesN = new ArrayList<String[][]>();
		tablesVerticalLegendsN = new ArrayList<String[]>();
		tablesHorizontalLegendsN = new ArrayList<String[]>();
		commentsN = new ArrayList<String>();
		imagesN = new ArrayList<String>();
		imagesNameN = new ArrayList<String>();
		imagesExtensionN = new ArrayList<String>();
		filesN = new ArrayList<String>();
		filesNameN = new ArrayList<String>();
		filesExtensionN = new ArrayList<String>();
		resultsN = new ArrayList<String>();
		resultsNameN = new ArrayList<String>();
		resultsMinN = new ArrayList<String>();
		resultsMaxN = new ArrayList<String>();
		resultsThresholdMinN = new ArrayList<String>();
		resultsThresholdMaxN = new ArrayList<String>();
		errorsN = new ArrayList<String>();
		
		if(executionMode == 1)
			logOff();
	}

	/**
     * Add a graph to the sending queue
     * This function add an output graph on Noolib
     *	
     * @param dataGraph array containing graph data<br>
     * @param graphLegends array containing column legends<br>
     */
	public void addGraph(double[][] dataGraph, String[] graphLegends)
	{
		graphsN.add(dataGraph);
		graphsLegendsN.add(graphLegends);
		
		if(graphLegends.length != dataGraph.length)
			error("Graph size and legends are different");
	}
	
	/**
     * Add a table to the sending queue
     * This function add an output table on Noolib
     *	
     * @param table array containing data<br>
     * @param horizontalLegends array containing column legends<br>
     * @param verticalLegends array containing row legends<br>
     */
	public void addTable(String[][] table, String[] horizontalLegends,String[] verticalLegends)
	{
		tablesN.add(table);
		tablesHorizontalLegendsN.add(horizontalLegends);
		tablesVerticalLegendsN.add(verticalLegends);
	}
	
	/**
     * Add a table to the sending queue
     * This function add an output table on Noolib
     *	
     * @param table array containing data<br>
     * @param horizontalLegends array containing column legends<br>
     * @param verticalLegends array containing row legends<br>
     */
	public void addTable(String[][] table, String[] horizontalLegends)
	{
		String[] verticalLegends = new String[horizontalLegends.length];
		Arrays.fill(verticalLegends, "-1");
		addTable(table, horizontalLegends, verticalLegends);
	}
	
	/**
     * Add a comment to the sending queue
     * This function add an output comment on Noolib
     *	
     * @param comments Informations about the result or/and the program<br>
     */
	public void addComment(String comments)
	{
		commentsN.add(comments);
	}
	
	/**
     * Add an image to the sending queue
     * This function add an output image on Noolib
     *	
     * @param image the image to send<br>
     * @param name the image name<br>
     * @param extension the image extension<br>
     */
	public void addImage(BufferedImage image, String name, String extension)
	{		
		try 
		{
			File imageFile = new File("image.jpg");
			ImageIO.write(image, extension, imageFile);
			imagesN.add(fileToBase64(imageFile));
			imagesNameN.add(name);
			imagesExtensionN.add(extension);
		} 
		catch (IOException e) {e.printStackTrace();}
	}
	
	/**
     * Add a result to the sending queue
     * This function add an output value on Noolib
     *	
     * @param name the result name<br>
     * @param value the result content/value to send<br>
     * @param min minimum of the value<br>
     * @param max maximum of the value<br>
     * @param thresholdmin minimum threshold of the value<br>
     * @param thresholdmax maximum threshold of the value<br>
     */
	public void addResult(String name, double value, double min, double max, double thresholdMin, double thresholdMax)
	{	
		resultsN.add(String.valueOf(value));
		resultsNameN.add(name);
		resultsMinN.add(""+min);
		resultsMaxN.add(""+max);
		resultsThresholdMinN.add(""+thresholdMin);
		resultsThresholdMaxN.add(""+thresholdMax);
	}
	
	/**
     * Add a result to the sending queue
     * This function add an output value on Noolib
     *	
     * @param name the result name<br>
     * @param value the result content/value to send<br>
     * @param min minimum of the value<br>
     * @param max maximum of the value<br>
     */
	public void addResult(String name, double value, double min, double max)
	{
		addResult(name, value, min, max, -1, -1);
	}
	
	/**
     * Add a result to the sending queue
     * This function add an output value on Noolib
     *	
     * @param name the result name<br>
     * @param value the result content/value to send<br>
     */
	public void addResult(String name, double value)
	{
		addResult(name, value, -1, -1, -1, -1);
	}
	
	/**
     * Add a result to the sending queue
     * This function add an output value on Noolib
     *	
     * @param name the result name<br>
     * @param value the result content/value to send<br>
     */
	public void addResult(String name, String value)
	{
		resultsN.add(value);
		resultsNameN.add(name);
		resultsMinN.add(""+(-1));
		resultsMaxN.add(""+(-1));
		resultsThresholdMinN.add(""+(-1));
		resultsThresholdMaxN.add(""+(-1));
	}
	
	/**
     * Add a <code>File</code> to the sending queue
     * This function add an output <code>File</code> on Noolib
     *	
     * @param file the <code>File</code> to send<br>
     * @param name the filename<br>
     * @param extension the file extension<br>
     */
	public void addFile(File file, String name, String extension)
	{
		if(file != null && name != null && name.length() > 0 && extension.length() > 0)
		{
			filesN.add(fileToBase64(file));
			filesNameN.add(name);
			filesExtensionN.add(extension);
		}
	}
	
	/**
     * Enable <code>System</code> streams.
     *	
     */
	private void logOn()
	{
		logIsOn = true;
		System.setOut(originalStream);
	}
	
	/**
     * Mute <code>System</code> streams.
     *	
     */
	private void logOff()
	{
		logIsOn = false;
		System.setOut(muteStream);
	}

	/**
     * Convert a csv <code>File</code> into a 2d <code>double</code> array.
     *	
     * @param file <code>File</code> to convert
     * @return the <code>double</code> array
     */
	public double[][] csvToDoubleArray(String fname, char separator)
	{
		try 
		{
			int col = 0, row = 0;
			@SuppressWarnings("resource")
			CSVReader reader = new CSVReader(new FileReader(fname), separator, '"' , 1);
			double doubleArray[][] = null;
			
			List<String[]> tmpList = reader.readAll();
			row = tmpList.size();
			
			if(row > 0)
			{ 
				col = tmpList.get(0).length;
				doubleArray = new double[row][col];
		        for(int i = 0; i < row; i++)
		        {
		        	for(int j = 0; j < col; j++)
		        	{
		        		DecimalFormat df = new DecimalFormat(); 
		        		DecimalFormatSymbols sfs = new DecimalFormatSymbols();
		        		sfs.setDecimalSeparator('.'); 
		        		df.setDecimalFormatSymbols(sfs);
		        		doubleArray[i][j] = df.parse(tmpList.get(i)[j]).doubleValue();
		        	}
		        }
				
			}
			return doubleArray;

		} 
		catch (IOException | ParseException e) {e.printStackTrace();}
		
		return null;		
	}
	
	/**
     * Convert a 2d <code>double</code> array into a csv <code>File</code>.
     *	
     * @param array 2d <code>double</code> array to convert
     * @return the csv <code>File</code>
     */
	public File doubleArrayToCsv(double[][] array)
	{
		try 
		{
			File myFile = null;
			
		    String csv = "data.csv";
		    
		    int lineLength = array[0].length;
		    String[] nextLine = new String[lineLength];
		    List<String[]> entries = new ArrayList<>();

		    for(int i = 0; i < array.length; i++)
		    {
			    for(int j = 0; j < array[0].length; j++)
			    {
			    	nextLine[j] = "" + array[i][j];
			    }
			    entries.add(nextLine);
		    } 
	        try (CSVWriter writer = new CSVWriter(new FileWriter(csv))) 
	        {
	            writer.writeAll(entries);
	        }
		    
		    myFile = new File("data.csv");
		
			return myFile;
		} 
		catch (IOException e) {e.printStackTrace();}
		
		return null;		
	}
	
	/**
     * Read a <code>File</code> and return it as a <code>String</code>.
     *	
     * @param file The <code>File</code> to read
     * @return the content of the <code>File</code>
     */
	public String readFile(File file)
	{
		BufferedReader br = null;
		String fileContent = "";
		
		try 
		{

			br = new BufferedReader(new FileReader(file));
			String nextLine;

			while ((nextLine = br.readLine()) != null) 
			{
				fileContent += nextLine;
			}

		} catch (IOException e) {e.printStackTrace();};
		
		return fileContent;
	}
	
	/**
     * Send all the data to Noolib (file, images, results, graph ...)
     * To save data use <code>addFile()</code>, <code>addResult()</code>,
     * <code>addGraph()</code>, <code>addComment()</code>, <code>addImage()</code> ...
     *
     */
	public void sendData()
	{

		String json = "{";
		
		if(errorsN.isEmpty())
		{
			if(!graphsN.isEmpty())
			{
				json += "\"graphs\":[";
				for(int h = 0; h < graphsN.size(); h++)
				{
					json += "{";
					json += "\"data\":[";
					for(int i = 0; i < graphsN.size(); i++)
					{
						json += "[";
						for(int j = 0; j < graphsN.get(h)[i].length; j++)
						{
							json += "" + graphsN.get(h)[i][j] + ",";
						}
						json = deleteLastChar(json);
						json +="],";
					}
					json = deleteLastChar(json);
					json +=	"],\"legend\":[";
					for(int i = 0; i <  graphsLegendsN.get(h).length; i++)
					{
						json += "\"" + graphsLegendsN.get(h)[i] + "\",";
					}
					json = deleteLastChar(json);
					json += "],\"sampleRate\":1";
					json += "},";		
				}
				json = deleteLastChar(json);
				json += "],";
			}
			
			if(!tablesN.isEmpty())
			{
				json += "\"tables\":[";
				for(int h = 0; h < graphsN.size(); h++)
				{
					json += "{";
					json += "\"data\":[";
					for(int i = 0; i < tablesN.size(); i++)
					{
						json += "[";
						for(int j = 0; j < tablesN.get(h)[i].length; j++)
						{
							json += "\"" + tablesN.get(h)[i][j] + "\",";
						}
						json = deleteLastChar(json);
						json +="],";
					}
					json = deleteLastChar(json);
					json +=	"],\"HorizontalLegend\":[";
					for(int i = 0; i <  tablesHorizontalLegendsN.get(h).length; i++)
					{
						json += "\"" + tablesHorizontalLegendsN.get(h)[i] + "\",";
					}
					json = deleteLastChar(json);
					json +=	"],\"VerticalLegend\":[";
					for(int i = 0; i <  tablesVerticalLegendsN.get(h).length; i++)
					{
						json += "\"" + tablesVerticalLegendsN.get(h)[i] + "\",";
					}
					json = deleteLastChar(json);
					json += "],\"sampleRate\":1";
					json += "},";		
				}
				json = deleteLastChar(json);
				json += "],";
				
			}
			
			if(!filesN.isEmpty())
			{
				json += "\"files\":[";
				for(int i = 0; i < filesN.size(); i++)
				{
					json += "{";
					json += "\"data\":\""+ filesN.get(i) +"\",";
					json += "\"name\":\""+ filesNameN.get(i) + "\",";
					json += "\"ext\":\""+ filesExtensionN.get(i) + "\"";
					json += "},";
				}
				json = deleteLastChar(json);
				json += "],";
			}
			
			if(!imagesN.isEmpty())
			{
				json += "\"images\":[";
				for(int i = 0; i < imagesN.size(); i++)
				{
					json += "{";
					json += "\"data\":\""+ imagesN.get(i) +"\",";
					json += "\"name\":\""+ imagesNameN.get(i) + "\",";
					json += "\"ext\":\""+ imagesExtensionN.get(i) + "\"";
					json += "},";
				}
				json = deleteLastChar(json);
				json += "],";
			}
			
			if(!resultsN.isEmpty())
			{
				json += "\"results\":[";
				for(int i = 0; i < resultsN.size(); i++)
				{
					json += "{";
					json += "\"name\":\""+ resultsNameN.get(i) +"\",";
					json += "\"value\":\""+ resultsN.get(i) + "\",";
					json += "\"min\":\""+ resultsMinN.get(i) + "\",";
					json += "\"max\":\""+ resultsMaxN.get(i) + "\",";
					json += "\"thresholdMin\":\""+ resultsThresholdMinN.get(i) + "\",";
					json += "\"thresholdMax\":\""+ resultsThresholdMaxN.get(i) + "\"";
					json += "},";
				}
				json = deleteLastChar(json);
				json += "],";
			}
			
			if(!commentsN.isEmpty())
			{
				json += "\"comments\":[";
				for(int i = 0; i < commentsN.size(); i++)
				{
					json += "\""+commentsN.get(i)+"\",";
				}
				json = deleteLastChar(json);
				json += "],";
			}
			
			if(json.length() != 1)
				json = deleteLastChar(json);
		}
		else
		{
			json += "\"errors\":[";
			for(int i = 0; i < errorsN.size(); i++)
			{
				json += "\""+errorsN.get(i)+"\",";
			}
			json = deleteLastChar(json);
			json += "]";
			
		}
		json += "}";

		if(!logIsOn)
		{
			logOn();
			System.out.println(json);
			logOff();
		}
	}
	
	/**
     * Delete the last char of a <code>String</code>.
     *	
     * @param o  The <code>String</code> to crop
     * @return The new <code>String</code>
     */
	private String deleteLastChar(String input)
	{
		return input.substring(0,input.length()-1);
	}
	
	/**
     * Converts a <code>File</code> to a base64 <code>String</code>.
     *	
     * @param o  The File to convert
     * @return The Base64 <code>String</code>
     */
	public String fileToBase64(File file)
	{
		return Base64.getEncoder().encodeToString(readFile(file).getBytes());
	}
	
    /**
     * Prints an error log and the execution time on noolib.
     *	
     * @param o  The message to print
     */
	public void error(String s)
	{
		errorsN.add(getExecutionTime()+" ms : " + s.toString());
	}
	
    /**
     * Get the execution time since the creation of the <code>Noolib</code> object.
     *
     * @return Execution time in miliseconds
     */
	public double getExecutionTime()
	{
		long endTime = System.nanoTime();
		return (endTime - startTime) / 100000;
	}
}
