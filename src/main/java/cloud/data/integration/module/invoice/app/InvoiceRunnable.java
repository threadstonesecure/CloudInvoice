package cloud.data.integration.module.invoice.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import cloud.data.integration.common.erpintegrationservice.webservice.DocumentDetails;
import cloud.data.integration.common.erpintegrationservice.webservice.ServiceException;
import cloud.data.integration.common.util.CommonMethods;
import cloud.data.integration.common.util.MailManager;
import cloud.data.integration.module.invoice.service.InvoiceService;
import cloud.data.integration.module.invoice.util.InvoiceUtil;



public class InvoiceRunnable implements Runnable{
	final static Logger logger = Logger.getLogger(InvoiceRunnable.class);
	private String customFile=null;
	public InvoiceRunnable(String fileName) {
		// TODO Auto-generated constructor stub
		customFile=fileName;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			logger.debug("Starting new thread to processInvoice for"+ customFile);
			processInvoice();
			logger.debug("End processing processInvoice");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.error("Error"+e.getMessage());
		}
	}
	
	public void processInvoice() throws Exception {
		
		String sourceFilePath = InvoiceUtil.getPropertyValue("INVOICE_SOURCE_FILE_PATH",customFile);
		String inboundHeaderFileName = InvoiceUtil.getPropertyValue("INVOICE_HEADER_FILE_NAME",customFile);
		String inboundLineFileName = InvoiceUtil.getPropertyValue("INVOICE_ITEM_FILE_NAME",customFile);
		String zipFileNameAndLocation = InvoiceUtil.getPropertyValue("INVOICE_ZIP_FILE_PATH_NAME",customFile);
		String zipFileLocation=InvoiceUtil.getPropertyValue("INVOICE_ZIP_DIRECTORY_NAME",customFile);
		//String remoteDirectory = InvoiceUtil.getPropertyValue("INVOICE_SFTP_SERVER_REMOTE_SOURCE_DIRECTORY").trim();
			
		String localLogDirectory = InvoiceUtil.getPropertyValue("INVOICE_LOG_DIRECTORY_NAME",customFile).trim();
		String tHeaderFileName=InvoiceUtil.getPropertyValue("HEADER_FILE_NAME",customFile);
		String tItemFileName=InvoiceUtil.getPropertyValue("ITEM_FILE_NAME",customFile);
		
		logger.info("URL "+InvoiceUtil.getPropertyValue("ERP_INTEGRATION_SERVICE_URL_WSDL"));
		try {
			//SFTPManager.downloadSourceFileFromSFTPServer(remoteDirectory, localDirectory);
			//SFTPManager.deleteSourceFilesFromSFTPServer(remoteDirectory);
			
            
			// TODO Auto-generated method stub
			File zipFolder = new File(zipFileLocation);
			//First Delete existing files under zip directory
			for(File file: zipFolder.listFiles()) 
		    if (!file.isDirectory()) {
		        file.delete();
		    }
			
			
			
			List<String[]> headerList=new ArrayList<String[]>();
			List<String[]> itemList=new ArrayList<String[]>();
			
			File downloadedSourceFiles = new File(sourceFilePath);
			File[] downloadedFilesList = downloadedSourceFiles.listFiles();
			if(downloadedFilesList.length>0) {
				
				
				if (customFile != null && customFile.equalsIgnoreCase("boa")) {
					// for BOA you need a create header file
					// and Item level file from one single master file
					
					for (File file : downloadedFilesList) {
						//System.out.println("file path"+file.getAbsolutePath());
						splitMasterFileToHeaderAndItemFiles(file.getAbsolutePath(), inboundHeaderFileName,
								inboundLineFileName);
					}
					
					
					
				}else {	
					// For em,toshiba,telecom 
					for (File file : downloadedFilesList) {
					//System.out.println("file path"+file.getAbsolutePath());
					    List<String[]> rList=CommonMethods.readCSVFileAsList(file.getAbsolutePath());
						 if(file.getName().toUpperCase().startsWith(tItemFileName.toUpperCase())){
					    	itemList.addAll(rList);
						 }else if(file.getName().toUpperCase().startsWith(tHeaderFileName.toUpperCase())) {
						    headerList.addAll(rList);
						 }
					}
					if(headerList.size() ==0 && itemList.size()==0 ) {
						
						MailManager.sendMail(InvoiceUtil.getPropertyValue("MAIL_USER_FROM_ADDRESS"),
								InvoiceUtil.getPropertyValue("MAIL_USER_TO_ADDRESS"),
								InvoiceUtil.getPropertyValue("MAIL_USER_CC_ADDRESS"),
								"File names not matching for " + customFile,
								"Please verify file names in folder for " + customFile);
						logger.error("File names not matching for "+customFile);
						throw new Exception("File names not matching for "+customFile);
					}
					writeListDataToHeaderFile(inboundHeaderFileName,headerList);
					writeListDataToLineFile(inboundLineFileName,itemList);
			}
			InvoiceService service = new InvoiceService();
			File[] zipFileList=zipFolder.listFiles();
			List<File> inboundFileList = new ArrayList<File>();
			inboundFileList.add(zipFileList[0]);
			inboundFileList.add(zipFileList[1]);
			File zipFileName=new File(zipFileNameAndLocation);
			CommonMethods.zipFiles(zipFileName, inboundFileList);
            logger.debug("Start making a call to uploadFileToFusion "+ new Date() );
			String documentId = service.uploadFileToFusion(zipFileNameAndLocation,customFile);
			logger.debug("End making a call to uploadFileToFusion "+ new Date() );
			logger.debug("uploadFileToFusion documentId"+ documentId);
			logger.debug("Start making a call to submitESSJobRequestForInterfaceLoader "+ new Date() );
			long jobRequestIdLI = service.submitESSJobRequestForInterfaceLoader(documentId);
			logger.debug("End making a call to submitESSJobRequestForInterfaceLoader "+ new Date() );
			logger.debug("ESSJob Submitted Request Id: " + jobRequestIdLI);
			
			logger.debug("Thread is started sleeping for 2 Sec " + new Date());
			Thread.sleep(2000);
			logger.debug("Thread is End sleeping " + new Date());
			// String essSubmittedJobStatusq = journalImportService.getESSJobStatus(jobRequestId);
			// logger.debug("essSubmittedJobStatus Status: " + essSubmittedJobStatusq);
			logger.debug("Start making a call to getJobStatus "+ new Date() );
			String essSubmittedJobStatusLI = service.getJobStatus(jobRequestIdLI);
			logger.debug("End making a call to getJobStatus "+ new Date() );
			logger.debug("getJobStatus ESSJob Status: [" + essSubmittedJobStatusLI+"]");
            logger.debug("End job status");
			String subject = null;
			String message = null;
				String fileListMessage = "Files processed in this run for " + customFile.toUpperCase() + " :";
				for (File file : downloadedFilesList) {
					fileListMessage = fileListMessage + "\n" + file.getAbsolutePath();
				}

			if (essSubmittedJobStatusLI.equalsIgnoreCase("ERROR")) {
					logger.debug("An error has occurred at Oracle Fusion Server while processing the submitted ESSJob");
					subject = InvoiceUtil.getPropertyValue("MAIL_MESSAGE_ERROR_SUBJECT_LINE_LOAD_INTERFACE") + " "
							+ customFile.toUpperCase();
					message = fileListMessage + "\n"
							+ InvoiceUtil.getPropertyValue("MAIL_MESSAGE_ERROR_TEXT_FOR_LOAD_INTERFACE");
					downLoadExecDetailsAndSendEmail(service, jobRequestIdLI, essSubmittedJobStatusLI, localLogDirectory,
							subject, message);
				} else if (essSubmittedJobStatusLI.equalsIgnoreCase("WARNING")) {
					subject = InvoiceUtil.getPropertyValue("MAIL_MESSAGE_WARNING_SUBJECT_LINE_LOAD_INTERFACE") + " "
							+ customFile.toUpperCase();
					message = fileListMessage + "\n"
							+ InvoiceUtil.getPropertyValue("MAIL_MESSAGE_WARNING_TEXT_FOR_LOAD_INTERFACE");
					downLoadExecDetailsAndSendEmail(service, jobRequestIdLI, essSubmittedJobStatusLI, localLogDirectory,
							subject, message);
				} else if (essSubmittedJobStatusLI.equalsIgnoreCase("RUNNING")) {
					subject = InvoiceUtil.getPropertyValue("MAIL_MESSAGE_RUNNING_SUBJECT_LINE_LOAD_INTERFACE") + " "
							+ customFile.toUpperCase();
					;
					message = fileListMessage + "\n"
							+ InvoiceUtil.getPropertyValue("MAIL_MESSAGE_RUNNING_TEXT_FOR_LOAD_INTERFACE");
					downLoadExecDetailsAndSendEmail(service, jobRequestIdLI, essSubmittedJobStatusLI, localLogDirectory,
							subject, message);
				} else if (essSubmittedJobStatusLI.equalsIgnoreCase("SUCCEEDED")) {
					subject = InvoiceUtil.getPropertyValue("MAIL_MESSAGE_SUCCESS_SUBJECT_LINE_LOAD_INTERFACE") + " "
							+ customFile.toUpperCase();
					message = fileListMessage + "\n"
							+ InvoiceUtil.getPropertyValue("MAIL_MESSAGE_SUCCESS_TEXT_FOR_LOAD_INTERFACE");
					downLoadExecDetailsAndSendEmail(service, jobRequestIdLI, essSubmittedJobStatusLI, localLogDirectory,
							subject, message);
				// Load is success full so send the email for load
				

				// START THE ESS JOB REQUEST TO IMPORT JOURNALS
					// File file = new File(sourceFilePath);
				Long journalImportrequestId = service.submitESSJobRequestForImportPayableInvoice(customFile);
				logger.debug("waiting thread for 2 Sec");
				logger.debug("Thread is started sleeping " + new Date());
				Thread.sleep(2000);
				logger.debug("Thread is End sleeping " + new Date());
				logger.debug("Start making a call to getJobStatus "+ new Date() );
				String invoiceImportESSJobStatus = service.getJobStatus(journalImportrequestId);
				logger.debug("End making a call to getJobStatus "+ new Date() );
				logger.debug(" getJobStatus journalImportESSJobStatus [" + invoiceImportESSJobStatus + "]");
				
				if (invoiceImportESSJobStatus.equalsIgnoreCase("ERROR")) {
					logger.debug(
							"An error has occurred at Oracle Fusion Server while processing the submitted ESSJob");
						subject = InvoiceUtil.getPropertyValue("MAIL_MESSAGE_ERROR_SUBJECT_LINE") + " "
								+ customFile.toUpperCase();
						message = fileListMessage + "\n"
								+ InvoiceUtil.getPropertyValue("MAIL_MESSAGE_ERROR_TEXT_FOR_INVOICE_IMPORT");
						downLoadExecDetailsAndSendEmail(service, journalImportrequestId, invoiceImportESSJobStatus,
								localLogDirectory, subject, message);
				} else if (invoiceImportESSJobStatus.equalsIgnoreCase("SUCCEEDED")) {
						subject = InvoiceUtil.getPropertyValue("MAIL_MESSAGE_SUCCESS_SUBJECT_LINE") + " "
								+ customFile.toUpperCase();
						message = fileListMessage + "\n"
								+ InvoiceUtil.getPropertyValue("MAIL_MESSAGE_SUCCESS_TEXT_FOR_INVOICE_IMPORT");
						downLoadExecDetailsAndSendEmail(service, journalImportrequestId, invoiceImportESSJobStatus,
								localLogDirectory, subject, message);
				
				} else if (invoiceImportESSJobStatus.equalsIgnoreCase("WARNING")) {
						subject = InvoiceUtil.getPropertyValue("MAIL_MESSAGE_WARNING_SUBJECT_LINE") + " "
								+ customFile.toUpperCase();
						message = fileListMessage + "\n"
								+ InvoiceUtil.getPropertyValue("MAIL_MESSAGE_WARNING_TEXT_FOR_INVOICE_IMPORT");
						downLoadExecDetailsAndSendEmail(service, journalImportrequestId, invoiceImportESSJobStatus,
								localLogDirectory, subject, message);
				} else if (invoiceImportESSJobStatus.equalsIgnoreCase("RUNNING")) {
						subject = InvoiceUtil.getPropertyValue("MAIL_MESSAGE_RUNNING_SUBJECT_LINE") + " "
								+ customFile.toUpperCase();
						message = fileListMessage + "\n"
								+ InvoiceUtil.getPropertyValue("MAIL_MESSAGE_RUNNING_TEXT_FOR_INVOICE_IMPORT");
						downLoadExecDetailsAndSendEmail(service, journalImportrequestId, invoiceImportESSJobStatus,
								localLogDirectory, subject, message);
				} 
				
				// find exact error in invoice report
			
				logger.debug("Thread is started sleeping 2 sec" + new Date());
				Thread.sleep(2000);
				logger.debug("Thread is End sleeping " + new Date());
				// for report 
				subject = InvoiceUtil.getPropertyValue("MAIL_MESSAGE_SUCCESS_SUBJECT_LINE_INVOICE_REPORT");
				message = InvoiceUtil.getPropertyValue("MAIL_MESSAGE_SUCCESS_TEXT_FOR_INVOICE_REPORT");
				
				//MailManager.sendMail(InvoiceUtil.getPropertyValue("MAIL_USER_FROM_ADDRESS"),
				//		InvoiceUtil.getPropertyValue("MAIL_USER_TO_ADDRESS"),InvoiceUtil.getPropertyValue("MAIL_USER_CC_ADDRESS"), subject, message
			//	);
			//	Long requestId=service.getBIImportInvoiceReportDetails(journalImportrequestId);
				Thread.sleep(2000);
				//gereratePDFandSendEmail(service, requestId,invoiceImportESSJobStatus, localLogDirectory, subject, message,unProceedRecordsData,sRecordsData,dRecordsData);
				
				logger.debug("deleting log files under log directectory");
				// delete files in log directory 
				File logDirectory=new File(localLogDirectory);
				File[] dataFilesDeletList = logDirectory.listFiles();
				for (int i = 0; i < dataFilesDeletList.length; i++) {
					logger.debug("dataFilesDeletList[i].getName()"+dataFilesDeletList[i].getName());
					CommonMethods.deleteFile(localLogDirectory+dataFilesDeletList[i].getName());
					logger.debug("End deleting file"+localLogDirectory+dataFilesDeletList[i].getName());
				}
				
				// create output file
				logger.debug("Moving the file to archive directory");
				String inFileDir=InvoiceUtil.getPropertyValue("INVOICE_SOURCE_FILE_PATH",customFile);
				String outFileArchive=InvoiceUtil.getPropertyValue("INVOICE_SOURCE_FILE_ARCHIVE",customFile);
				
				File sourceFiles = new File(inFileDir);
				File[] sourceFilesList = sourceFiles.listFiles();
				if(sourceFilesList.length>0) {
					CommonMethods.moveFiles(sourceFilesList, outFileArchive);
					logger.debug("Moved old files to "+outFileArchive);
				}
				
				//service.getBIPaymentReport();
				logger.debug("After calling output report");
			} 
				
			} else {
				logger.error("Files not existing in folder for "+customFile);
			}
			} catch (IOException e) {
			logger.error("IOException: " + e.getMessage());
			e.printStackTrace();
			} catch(NullPointerException e) {
			System.err.println("NullPointerException: " + e.getMessage());
			e.printStackTrace();
			}
		
	}

	private void splitMasterFileToHeaderAndItemFiles(String fileName, String headerFileName, String itemFileName)
			throws IOException {
		// TODO Auto-generated method stub
		 List<String[]> rList=CommonMethods.readCSVFileAsListWithoutHeader(fileName);
		 List<String[]> hList=new ArrayList<String[]>();
		 hList.add(rList.get(rList.size()-1));
		 
		 writeListDataToHeaderFile(headerFileName,hList);
		 
		 List<String[]> iTemList=new ArrayList<String[]>();
		rList.remove(rList.size() - 1);
		iTemList = rList;
		 writeListDataToLineFile(itemFileName,iTemList);
		 
		 // last record goes to header file
		 
	}

	private void writeListDataToLineFile(String inboundLineFileName, List<String[]> itemList) throws IOException {
		// TODO Auto-generated method stub
		CommonMethods.writeToCSVFileFromList(inboundLineFileName,itemList);
	}

	private void writeListDataToHeaderFile(String inboundHeaderFileName, List<String[]> headerList) throws IOException {
		// TODO Auto-generated method stub
		CommonMethods.writeToCSVFileFromList(inboundHeaderFileName,headerList);
	}

	private void sendErrorEmail(ArrayList<String> sRecordsData, ArrayList<String> dRecordsData) {
		// TODO Auto-generated method stub
		String unzipFolder=InvoiceUtil.getPropertyValue("INVOICE_UNZIP_DIRECTORY_NAME",customFile);
		createErrorFile(unzipFolder+"ErroredRecords.txt",sRecordsData,dRecordsData);
		String subject="Unprocessed record file";
		String message="Please see attached unprocessed records file";
		MailManager.sendMailWithAttachment(InvoiceUtil.getPropertyValue("MAIL_USER_FROM_ADDRESS"),
				InvoiceUtil.getPropertyValue("MAIL_USER_TO_ADDRESS"),
				InvoiceUtil.getPropertyValue("MAIL_USER_CC_ADDRESS"), subject, message,
				unzipFolder+"ErroredRecords.txt");
		// delete all files under unZip folder
		logger.debug("message send and deleting files under unzip folder");
		//Thread.sleep(300000);
		CommonMethods.deleteFile(unzipFolder+"ErroredRecords.txt");
		
	}

	private void downLoadExecDetailsAndSendEmail(InvoiceService service, Long jobRequestIdLI,
			String essSubmittedJobStatus, String localLogDirectory, String subject, String message)
			throws IOException, ServiceException {
		// TODO Auto-generated method stub
		
		logger.debug("jobRequestIdLI"+jobRequestIdLI);
		List<DocumentDetails> jobExecutionDetails = service.downloadESSJobExecutionDetails(jobRequestIdLI);
		byte[] fileContent;
		String fileName = null;
		
		logger.debug(jobExecutionDetails.size());
		if (jobExecutionDetails != null && !jobExecutionDetails.isEmpty()) {
			for (DocumentDetails jobExecDetail : jobExecutionDetails) {
				fileName = jobExecDetail.getDocumentName().getValue();
				logger.debug("Job Execution Detail: FileName: " + fileName);
				fileContent = jobExecDetail.getContent();
				// File logFile = new File(localLogDirectory + "/"+fileName);
				Path logFilePath = FileSystems.getDefault().getPath(localLogDirectory, fileName);
				Files.write(logFilePath, fileContent, StandardOpenOption.CREATE_NEW);
				//subject = InvoiceUtil.getPropertyValue("MAIL_MESSAGE_WARNING_SUBJECT_LINE");
			//	subject = essSubmittedJobStatus + " " + subject + " - " + fileName; //DK
				subject =  subject + " - " + fileName;
				subject=subject.substring(0, subject.lastIndexOf("."));
				logger.debug("Subject :" + subject);
				logger.debug("Message :" + message);
				MailManager.sendMailWithAttachment(InvoiceUtil.getPropertyValue("MAIL_USER_FROM_ADDRESS"),
						InvoiceUtil.getPropertyValue("MAIL_USER_TO_ADDRESS"),
						InvoiceUtil.getPropertyValue("MAIL_USER_CC_ADDRESS"), subject, message,
						localLogDirectory + "/" + fileName);
			}
		}
	}
	
/*	private void gereratePDFandSendEmail(InvoiceService service,Long jobRequestIdLI,String essSubmittedJobStatus,String localLogDirectory,String subject,String message,ArrayList<String> unProceedRecordsData,ArrayList<String> sRecordsData,ArrayList<String> dRecordsData) throws IOException, ServiceException,  oracle.xdo.XDOException, InterruptedException {
		// TODO Auto-generated method stub
		
		List<DocumentDetails> jobExecutionDetails = service
				.downloadESSJobExecutionDetails(jobRequestIdLI);
		byte[] fileContent;
		String fileName = null;
		
		logger.debug(jobExecutionDetails.size());
		
		if (jobExecutionDetails != null && !jobExecutionDetails.isEmpty()) {
			for (DocumentDetails jobExecDetail : jobExecutionDetails) {
				fileName = jobExecDetail.getDocumentName().getValue();
				logger.debug("fileName"+fileName);
				fileContent = jobExecDetail.getContent();
				Path logFilePath = FileSystems.getDefault().getPath(localLogDirectory, fileName);
				Files.write(logFilePath, fileContent, StandardOpenOption.CREATE);
				String unzipFolder=InvoiceUtil.getPropertyValue("INVOICE_UNZIP_DIRECTORY_NAME");
				logger.debug("logFilePath.toString()"+logFilePath.toString());
				logger.debug("unzipFolder"+unzipFolder);
				CommonMethods.unZipFiles(logFilePath.toString(), unzipFolder);
				String xmlFileName=getXMLFileName(unzipFolder);
				logger.debug("xmlFileName "+xmlFileName);
				Path path = Paths.get(unzipFolder+xmlFileName);
				byte[] xmlFileContent = Files.readAllBytes(path);


				if(xmlFileName.contains("xml")) {
					// this is for ESS_O_<NUMBER>_BIP.xml
					
					RTFProcessor rtfp = new RTFProcessor(InvoiceUtil.getPropertyValue("INVOICE_PAYABLES_REPORT_RTF")); //input RTF template
					ByteArrayOutputStream xslOutStream = new ByteArrayOutputStream();
					rtfp.setOutput(xslOutStream);
					ByteArrayOutputStream XLIFFOutStream = new ByteArrayOutputStream();
					rtfp.setXLIFFOutput(XLIFFOutStream); // XLIFF output file
					rtfp.process();
					FOProcessor p1 = new FOProcessor();
					ByteArrayInputStream XLIFFInStream = new ByteArrayInputStream(XLIFFOutStream.toByteArray());
					p1.setXLIFF(XLIFFInStream);// set xliff file, which is generated from RTFProcessor
					ByteArrayInputStream xmlInStream = new ByteArrayInputStream(xmlFileContent);
					p1.setData(xmlInStream); // set data file
					ByteArrayInputStream xslInStream = new ByteArrayInputStream(xslOutStream.toByteArray());
					p1.setTemplate(xslInStream);
				
					ByteArrayOutputStream pdfOutStream = new ByteArrayOutputStream();
					p1.setOutput(unzipFolder+"ESS_O_"+fileName.substring(0, fileName.lastIndexOf("."))+".pdf");
					p1.setOutputFormat(FOProcessor.FORMAT_PDF);
					p1.generate();
					Path logFilePathPDF = FileSystems.getDefault().getPath(localLogDirectory, fileName);
					Files.write(logFilePathPDF, pdfOutStream.toByteArray(), StandardOpenOption.CREATE);
					xslOutStream.close();
					xslInStream.close();
					XLIFFOutStream.close();
					XLIFFInStream.close();
					xmlInStream.close();
					pdfOutStream.close(); 
				}
				
				File zipFileName = new File(unzipFolder+fileName);
				List<File> inboundFileList = new ArrayList<File>();
				
				// create unprocessed records into file
				
			
				
				//Path unprocessedFilePath = FileSystems.getDefault().getPath(unzipFolder, "unprocessed.txt");
				//Files.write(logFilePath, fileContent, StandardOpenOption.CREATE);
				createAUnprocessedRecordsFile(unzipFolder+"unprocessed.txt",unProceedRecordsData);
				createErrorFile(unzipFolder+"ErroredRecords.txt",sRecordsData,dRecordsData);
				File pdfZipFilePath = new File(unzipFolder);
				File[] dataFilesList = pdfZipFilePath.listFiles();
				for (int i = 0; i < dataFilesList.length; i++) {
						inboundFileList.add(dataFilesList[i]);
				}
				
				CommonMethods.zipFiles(zipFileName, inboundFileList);
				
				logger.debug("Job Execution Detail: FileName: " + fileName);
				
				// File logFile = new File(localLogDirectory + "/"+fileName);
				//Path logFilePath = FileSystems.getDefault().getPath(localLogDirectory, fileName);
				//Files.write(logFilePath, fileContent, StandardOpenOption.APPEND);
				//subject = InvoiceUtil.getPropertyValue("MAIL_MESSAGE_WARNING_SUBJECT_LINE");
				subject=essSubmittedJobStatus+" "+subject+" - "+fileName;
				subject=subject.substring(0, subject.lastIndexOf("."));
				logger.debug("subject"+subject);
				MailManager.sendMailWithAttachment(InvoiceUtil.getPropertyValue("MAIL_USER_FROM_ADDRESS"),
						InvoiceUtil.getPropertyValue("MAIL_USER_TO_ADDRESS"), subject, message,
						unzipFolder+fileName);
				// delete all files under unZip folder
				logger.debug("message send and deleting files under unzip folder");
				//Thread.sleep(300000);
				File[] dataFilesDeletList = pdfZipFilePath.listFiles();
				for (int i = 0; i < dataFilesDeletList.length; i++) {
					logger.debug("dataFilesDeletList[i].getName()"+dataFilesDeletList[i].getName());
					CommonMethods.deleteFile(unzipFolder+dataFilesDeletList[i].getName());
					logger.debug("End deleting file"+unzipFolder+dataFilesDeletList[i].getName());
				}
				
			}
		}
	}
	*/

	private void createErrorFile(String fileName, ArrayList<String> sRecordsData, ArrayList<String> dRecordsData) {
		// TODO Auto-generated method stub
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
			if(sRecordsData.size()>0) {
				bw.write ("The supplier not exist for below records" + "\n");
				for (String line : sRecordsData) {
					bw.write (line + "\n");
				}
			}
			
			if(dRecordsData.size()>0) {
				bw.write ("The distribution set name is not found for below records" + "\n");
				for (String line : dRecordsData) {
					bw.write (line + "\n");
				}
			}
			
			bw.close ();
			
		} catch (IOException e) {
			e.printStackTrace ();
			logger.error("Error"+e.getMessage());
		}
	}

	private void createAUnprocessedRecordsFile(String fileName, ArrayList<String> unProcessedList) {
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
							for (String line : unProcessedList) {
								bw.write (line + "\n");
							}
							
							bw.close ();
							
						} catch (IOException e) {
							e.printStackTrace ();
							logger.error("Error"+e.getMessage());
						}

	}

	private String getXMLFileName(String unzipFolder) {
		// TODO Auto-generated method stub
		File dataFile = new File(unzipFolder);
		File[] dataFilesList = dataFile.listFiles();
		String xmlFileName=null;
		for (int i = 0; i < dataFilesList.length; i++) {
			if(dataFilesList[i].getName().contains("xml")) {
				xmlFileName=dataFilesList[i].getName();
			}
		}
		return xmlFileName;
	} 

}
