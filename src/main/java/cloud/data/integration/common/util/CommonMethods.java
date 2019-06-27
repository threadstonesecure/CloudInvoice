package cloud.data.integration.common.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

/**
 * 
 * @author SBalawatri
 *
 */
public class CommonMethods {
	final static Logger logger = Logger.getLogger(CommonMethods.class);
	public static String getPropertyValue (String stringKey){
			
			ResourceBundle bundle = ResourceBundle.getBundle("cloud.data.integration.common.resource.ApplicationResources", Locale.getDefault());
			if (bundle.getString(stringKey) != null){
				//logger.debug("CommonMethods:Property Value: " + bundle.getString(stringKey));
			} else {
				logger.debug("Error: Property "+ stringKey +" Not Found!");
			}
			
			return bundle.getString(stringKey);
	}
	
	public static File [] checkFilePresence(String fileLocation) {
	
		File folder = new File(fileLocation);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				logger.debug("Files Available in folder: " + listOfFiles[i].getName());
		    }
		}
		if ((listOfFiles.length == 0) || listOfFiles[0].getName().isEmpty()) {
			logger.debug("File(s) not available in Source folder to Upload.");
		}
		
		return listOfFiles;
	}
	
	/**
	 * This method moves all of the files in the sourceFilePath directory into the destinationFilePath
	 * directory.
	 * @param sourceFilePath
	 * @param processedFilePath
	 * @throws IOException 
	 */
	public static void moveFiles(String sourceFilePath, String destinationFilePath) throws IOException {
		File csvFilePath = new File(sourceFilePath);
		File [] files = csvFilePath.listFiles();
		
		for(File file : files) {
			Path filePath = file.toPath();
			Path destination = new File(destinationFilePath, file.getName()).toPath();
			Files.move(filePath, destination, StandardCopyOption.REPLACE_EXISTING);
		}
	}
	
	/**
	 * This method moves all of the files in the sourceFilePath directory into the destinationFilePath
	 * directory.
	 * @param sourceFilePath
	 * @param processedFilePath
	 * @throws IOException 
	 */
	public static void moveFiles(File [] movefiles, String destinationFilePath) throws IOException {
		File [] files = movefiles;
		for(File file : files) {
			Path filePath = file.toPath();
			Path destination = new File(destinationFilePath, file.getName()).toPath();
			Files.move(filePath, destination, StandardCopyOption.REPLACE_EXISTING);
		}
	}
	public static void deleteFile(String fileName) {
		if(fileName == null) {
			throw new NullPointerException("The file path is null of the file you want to delete.");
		}
		File inboundFile = new File(fileName);
		inboundFile.delete();
	}
	
public static void processCSVFile(String sourceFilePath, String inboundFileName) throws IOException {
		
		//File sourceCSVFile = new File(CommonMethods.getPropertyValue("SOURCE_FILE_PATH"));
	/*
		File sourceCSVFile = new File(sourceFilePath);
    	File [] sourceFilesList = sourceCSVFile.listFiles();
    	if(sourceFilesList.length == 0) {
    		//logger.debug("Journal Source File Path Does Not Exist.");
    		throw new JournalImportException("Source File does not exist in the Source folder. ");
    	}
    */	    
    //	for(File sourceCSV : sourceFilesList) {
    		
    	 	CSVReader reader = new CSVReaderBuilder(new FileReader(sourceFilePath))
        			.withSkipLines(1)
        			//.withCSVParser(parser)
        			.build();
        	
        	List<String[]> readLines = reader.readAll();
        	//Writer writer2 = new FileWriter(CommonMethods.getPropertyValue("INBOUND_CSV_FILE_NAME_WITHOUT_HEADER"));
        	Writer writer2 = new FileWriter(inboundFileName);
        	CSVWriter csvWriter = new CSVWriter(writer2);
        	
        	csvWriter.writeAll(readLines);
        	csvWriter.flush();
        	csvWriter.close();
        	reader.close();
    //	}
    	
    	//sourceCSVFile.
		
	}

	public static List<String[]> readCSVFileAsList(String sourceFilePath) throws IOException {
		
		 	CSVReader reader = new CSVReaderBuilder(new FileReader(sourceFilePath))
	    			.withSkipLines(1)
	    			//.withCSVParser(parser)
	    			.build();
	    	List<String[]> readLines = reader.readAll();
	    	reader.close();
	    	return readLines;
	}
	
	public static List<String[]> readCSVFileAsListWithoutHeader(String sourceFilePath) throws IOException {
		
	 	CSVReader reader = new CSVReaderBuilder(new FileReader(sourceFilePath))
    			.build();
    	List<String[]> readLines = reader.readAll();
    	reader.close();
    	return readLines;
}
	
	public static void writeToCSVFileFromList(String sourceFilePath, List<String[]> listData) throws IOException {
		Writer writer2 = new FileWriter(sourceFilePath);
    	CSVWriter csvWriter = new CSVWriter(writer2);
    	csvWriter.writeAll(listData);
    	csvWriter.flush();
    	csvWriter.close();
	}
	

	public static void zipFiles(File zipFileName, List<File> sources) throws IOException {
		Packager.packZip(zipFileName, sources);
	}


	public static List<String[]> processCSVFileToCollections(String sourceFilePath) throws IOException {
				
		CSVReader reader = new CSVReaderBuilder(new FileReader(sourceFilePath))
	    			.withSkipLines(1)
	    			//.withCSVParser(parser)
	    			.build();
	    	
	    List<String[]> readLines = reader.readAll();
		return readLines;
	}
	public static void unZipFiles(String zipFileName, String destinationPath) throws IOException {
		Packager.unPackZip(zipFileName, destinationPath);
	}

	
	public static void moveFile(String valueSetFileNameAndLocation, String destinationFile) throws IOException {
		// TODO Auto-generated method stub
		logger.debug(valueSetFileNameAndLocation);
		logger.debug(destinationFile);
		
		Path temp = Files.move
		        (Paths.get(valueSetFileNameAndLocation), 
		        Paths.get(destinationFile));
		if(temp != null)
        {
            logger.debug("File renamed and moved successfully");
        }
        else
        {
            logger.error("Failed to move the file");
        }
	}

}
