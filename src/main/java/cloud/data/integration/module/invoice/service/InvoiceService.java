/**
 * 
 */
package cloud.data.integration.module.invoice.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.opencsv.CSVWriter;

import cloud.data.integration.common.bipublisher.reportservice.webservice.ArrayOfParamNameValue;
import cloud.data.integration.common.bipublisher.reportservice.webservice.ArrayOfString;
import cloud.data.integration.common.bipublisher.reportservice.webservice.ParamNameValue;
import cloud.data.integration.common.bipublisher.reportservice.webservice.ParamNameValues;
import cloud.data.integration.common.bipublisher.reportservice.webservice.ReportRequest;
import cloud.data.integration.common.bipublisher.reportservice.webservice.ReportResponse;
import cloud.data.integration.common.bipublisher.reportservice.webservice.ReportService;
import cloud.data.integration.common.erpintegrationservice.webservice.DocumentDetails;
import cloud.data.integration.common.erpintegrationservice.webservice.ErpIntegrationService;
import cloud.data.integration.common.erpintegrationservice.webservice.ObjectFactory;
import cloud.data.integration.common.service.CloudDataIntegrationService;
import cloud.data.integration.module.invoice.model.InvoiceData;
import cloud.data.integration.module.invoice.util.InvoiceUtil;
import cloud.data.integration.module.invoice.vo.ItemInfo;
import cloud.data.integration.module.invoice.vo.SupplierInfo;


/**
 * @author SBalawatri
 *
 */
public class InvoiceService {

	final static Logger logger = Logger.getLogger(InvoiceService.class);
	public void processInvoiceDataFile(String dataFilePath, Map<String, ArrayList<ItemInfo>> itemLevelData,
			Map<String, InvoiceData> headerLevelData, LinkedHashSet<String> headerKeys, ArrayList<String> unProceedRecordsData,ArrayList<String> sRecordsData,ArrayList<String> dRecordsData) throws Exception {

		File dataFile = new File(dataFilePath);
		File[] dataFilesList = dataFile.listFiles();
		if (dataFilesList.length == 0) {

			logger.debug(
					"Files dosn't exists in source folder. Please keep the files in source folder and try again");
		}
		else {
			int invoiceId = Integer.parseInt(InvoiceUtil.getPropertyValue("invoiceid","invoice")); 
	        
			logger.debug("invoiceId ["+ invoiceId+"]");
			
			try (BufferedReader br = new BufferedReader(new FileReader(dataFilesList[0]))) {
				String line;
				while ((line = br.readLine()) != null) {
					logger.debug("Let us print the line: " + line);
					InvoiceData invoiceData = new InvoiceData();
					String providerId = line.substring(0, 10);
					String payeeId = line.substring(10, 20);
					String payeeName = line.substring(20, 50);
	
					
					
					String ifcFinancialInstName = line.substring(50, 82);
					String ifcDirectDepInd ="CHECK";// line.substring(82, 83);
					String ifcAccountNum = line.substring(83, 93);
					String ifcChildId = line.substring(93, 107);
					String ifcChildLastName = line.substring(107, 132);
					String ifcChildFirstName = line.substring(132, 147);
					String ifcChildMiddleName = line.substring(147, 148);
					String ifcChildSuffixName = line.substring(148, 151);
	
					String ifcMailFirstStreetAddr = line.substring(151, 191);
					String ifcMailSecondStreetAddr = line.substring(191, 221);
					String ifcMailInCareOfName = line.substring(221, 251);
	
					String ifcMailCityName = line.substring(251, 273);
					String ifcMailStateName = line.substring(273, 275);
					String ifcMailZipCode = line.substring(275, 280);
					String ifcMailZipCodePlusFour = line.substring(280, 284);
					String ifcMailForeignAddress = line.substring(284, 299);
					String ifcRemFirstStreetAddr = line.substring(299, 339);
	
					String ifcRemSecondStreetAddr = line.substring(339, 369);
					String ifcRemInCareOfName = line.substring(369, 399);
					String ifcRemCityName = line.substring(399, 421);
					String ifcRemStateName = line.substring(421, 423);
					String ifcRemZipCode = line.substring(423, 428);
					String ifcRemZipCodePlusFour = line.substring(428, 432);
					String ifcRemForeignAddress = line.substring(432, 447);
					String ifcPaymentType = line.substring(447, 449);
					logger.debug("ifcPaymentType"+ifcPaymentType);
					String ifcPaymentStartDate = line.substring(449, 459);
					String ifcNumberOfDays = line.substring(459, 462);
	
					String ifcSign = line.substring(462, 463);
					
					String ifcAmountString=line.substring(463, 470);
				    BigInteger ifcAmountBigInt = new BigInteger(ifcAmountString);
					BigDecimal ifcAmountBigDec = new BigDecimal(ifcAmountBigInt, 2);
					double ifcAmount =ifcAmountBigDec.doubleValue();
	
					String ifcVoucherNumber = line.substring(470, 475);
					String ifcVoucherTypeCode = line.substring(475, 477);
					String ifcVoucherDate = line.substring(477, 487);
					String ifcFund = line.substring(487, 489);
					String ifcDepartment = line.substring(489, 492);
					String ifcServiceDescription = line.substring(492, 532);
					String ifcCourtNumber = line.substring(532, 540);
					String ifcCountyCode = line.substring(540, 542);
					String ifcCountyPersonId = line.substring(542, 562);
					String ifcCountyCaseId = line.substring(562, 582);
					String ifcCountyPrvdId = line.substring(582, 602);
					String ifcRptCat = line.substring(602, 607);
					
					logger.debug("ifcRptCat"+ifcRptCat.trim());
					String ifcEpisodeId = line.substring(607, 621);
					String ifcPaymentId = line.substring(621, 635);
					String ifcCountyPayeeId = line.substring(635, 655).trim();
					
					String ifcCdSrvc = line.substring(655, 664);
					String ifcTargetPop = line.substring(664, 669);
					logger.debug("ifcTargetPop"+Integer.parseInt(ifcTargetPop));
					
					String inputCOA=String.format("%02d", Integer.parseInt(ifcRptCat.trim()))+"-"+ifcPaymentType.trim()+"-"+Integer.parseInt(ifcTargetPop);
					
					logger.debug("inputCOA "+inputCOA);
					
					
					String ifcSPC = line.substring(669, 674);
					String ifcPayeeName = line.substring(674, 704);
					String ifcReimbursmntCatgry = line.substring(704, 706);
					String ifcPaymentSubcode = line.substring(476, 477);
					String filler = line.substring(477, 484);
	
					logger.debug("ifcVoucherDate"+ifcVoucherDate);
	
					
					SupplierInfo supplierInfo = null;
					boolean gotError=false;
					try {
						logger.debug("ifcCountyPayeeId"+ifcCountyPayeeId);
						supplierInfo=getBIReportInvoiceSiteDetails(ifcCountyPayeeId);
					}catch(Exception e) {
						logger.error("Supplier not found");
						sRecordsData.add(line);
						gotError=true;
						logger.error("Error"+e.getMessage());
						
						
					}
					// validate supplier number
					
					
					String distributionSetName=null;
					try {
						distributionSetName=getBIReportDistributionSetName(inputCOA);
					}catch(Exception e) {
						gotError=true;
						logger.error("Error"+e.getMessage());
						dRecordsData.add(line);
					}
					
					if(gotError) {
						unProceedRecordsData.add(line);
					} else {
						
						int supplierNumber = supplierInfo.getSupplierNumber();
						if (supplierNumber>0) {
							payeeId=""+supplierNumber;
						}
						logger.debug("supplierNumber ["+ supplierNumber+"]" );
						logger.debug("distributionSetName ["+distributionSetName+"]");
						
						invoiceData.setIfcProviderId(providerId);
						invoiceData.setDistributionSetName(distributionSetName);
						invoiceData.setIfcPayeeId(payeeId);
						invoiceData.setIfcPayeeName(payeeName);
						invoiceData.setIfcFinancialInstName(ifcFinancialInstName);
						invoiceData.setIfcDirectDepositIndicator(ifcDirectDepInd);
						invoiceData.setIfcAccountNumber(ifcAccountNum);
						invoiceData.setIfcChildIdentifier(ifcChildId);
						invoiceData.setIfcChildLastName(ifcChildLastName);
						invoiceData.setIfcChildFirstName(ifcChildFirstName);
						invoiceData.setIfcChildMiddleName(ifcChildMiddleName);
						invoiceData.setIfcChildSuffixName(ifcChildSuffixName);
						invoiceData.setIfcMailFirstStreetAddr(ifcMailFirstStreetAddr);
						invoiceData.setIfcMailSecondStreetAddr(ifcMailSecondStreetAddr);
						invoiceData.setIfcMailInCareOfName(ifcMailInCareOfName);
						invoiceData.setIfcMailCityName(ifcMailCityName);
						invoiceData.setIfcMailStateName(ifcMailStateName);
						invoiceData.setIfcMailZipCode(ifcMailZipCode);
						invoiceData.setIfcMailZipCodePlusFour(ifcMailZipCodePlusFour);
						invoiceData.setIfcMailForeignAddress(ifcMailForeignAddress);
		
						invoiceData.setIfcRemFirstStreetAddr(ifcRemFirstStreetAddr);
						invoiceData.setIfcRemSecondStreetAddr(ifcRemSecondStreetAddr);
						invoiceData.setIfcRemInCareOfName(ifcRemInCareOfName);
						invoiceData.setIfcRemCityName(ifcRemCityName);
						invoiceData.setIfcRemStateName(ifcRemStateName);
						invoiceData.setIfcRemZipCode(ifcRemZipCode);
						invoiceData.setIfcRemZipCodePlusFour(ifcMailZipCodePlusFour);
						invoiceData.setIfcRemForeignAddress(ifcRemForeignAddress);
						invoiceData.setIfcPaymentType(ifcPaymentType);
						invoiceData.setIfcPaymentStartDate(ifcPaymentStartDate);
						invoiceData.setIfcNumberOfDays(ifcNumberOfDays);
						invoiceData.setIfcSign(ifcSign);
						invoiceData.setIfcAmount(ifcAmount);
						invoiceData.setInvoiceId(invoiceId);
						invoiceData.setSiteName(supplierInfo.getSupplierSite());
						invoiceData.setIfcVoucherNumber(ifcVoucherNumber);
						invoiceData.setIfcVoucherTypeCode(ifcVoucherTypeCode);
						invoiceData.setIfcVoucherDate(ifcVoucherDate);
						// invoiceDataList.add(invoiceData);
		
						String headerKey = supplierNumber + payeeName ;
						logger.debug("headerKey" + headerKey);
						if (!headerLevelData.containsKey(headerKey)) {
							headerLevelData.put(headerKey, invoiceData);
							invoiceId=invoiceId+1;
						}
						ItemInfo item = new ItemInfo();
						item.setAmount(invoiceData.getIfcAmount());
						ArrayList<ItemInfo> itemList = itemLevelData.get(headerKey);
						if (itemList != null) {
							itemList.add(item);
						} else {
							itemList = new ArrayList<ItemInfo>();
							itemList.add(item);
						}
						itemLevelData.put(headerKey, itemList);
						headerKeys.add(headerKey);
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
	}

	public void processInvoiceDataFileToCSVWithColumnMapping(Map<String, ArrayList<ItemInfo>> itemLevelData,
			Map<String, InvoiceData> headerLevelData, LinkedHashSet<String> headerKeyList) throws IOException, ParseException {
      

		String csvFileName = InvoiceUtil.getPropertyValue("INVOICE_INBOUND_CSV_COLUMN_MAPPED_HEADER_FILE_NAME");
		CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFileName));
		List<String[]> data = new ArrayList<String[]>();
		
		int inti = 1;
		int maxInvoiceId=1;
		for (String headerKey : headerKeyList) {
			double totalLineLevelAmout=0.00;
			for (ItemInfo itemInfo : itemLevelData.get(headerKey)) {
				totalLineLevelAmout=itemInfo.getAmount()+totalLineLevelAmout;
			}
				
				
					
			String description="";
			if(headerLevelData.get(headerKey).getIfcChildLastName()!=null) {
				description=description+headerLevelData.get(headerKey).getIfcChildLastName().trim();
			}
			if(headerLevelData.get(headerKey).getIfcChildFirstName()!=null) {
				description=description+" "+headerLevelData.get(headerKey).getIfcChildFirstName().trim();
			}
			if(headerLevelData.get(headerKey).getIfcChildMiddleName()!=null) {
				description=description+" "+headerLevelData.get(headerKey).getIfcChildMiddleName().trim();
			}
			if(headerLevelData.get(headerKey).getIfcChildSuffixName()!=null) {
				description=description+" "+headerLevelData.get(headerKey).getIfcChildSuffixName().trim();
			}			
			data=buildHeaderData(headerLevelData,headerKey,totalLineLevelAmout,description,inti,data);
			inti++;
			maxInvoiceId=headerLevelData.get(headerKey).getInvoiceId();
		}
       
		csvWriter.writeAll(data);
		csvWriter.close();

		String csvLineFileName = InvoiceUtil.getPropertyValue("INVOICE_INBOUND_CSV_COLUMN_MAPPED_ITEM_FILE_NAME");
		CSVWriter csvLineWriter = new CSVWriter(new FileWriter(csvLineFileName));
		List<String[]> dataLineItems = new ArrayList<String[]>();
		
		for (String headerKey : headerKeyList) {

			int intj = 1;
			for (ItemInfo itemInfo : itemLevelData.get(headerKey)) {
				dataLineItems=buildDataLineItems(headerLevelData,headerKey,itemInfo,
						intj,dataLineItems);
				intj++;
			}

		}

		csvLineWriter.writeAll(dataLineItems);
		csvLineWriter.close();
		logger.debug("maxInvoiceId"+maxInvoiceId);
		
		// increment by one before saving
		updateInvoiceIdInFile(maxInvoiceId+1);
	}

	

	

	private void updateInvoiceIdInFile(int maxInvoiceId) {
		
		try {
			InvoiceUtil.updateInvoiceIdInFile(maxInvoiceId);
		} catch (IOException e) {
			logger.error("Error"+e.getMessage());
			e.printStackTrace();
		}
	}

	public SupplierInfo getBIReportInvoiceSiteDetails(String payeeName) throws Exception {

		String reportAbsolutePath = InvoiceUtil.getPropertyValue("INVOICE_FUSION_BI_PUBLISHER_SITE_REPORT_PATH");
		ReportService reportService = CloudDataIntegrationService.getBIPublisherReportService();
		ReportRequest reportRequest = new ReportRequest();
		reportRequest.setReportAbsolutePath(reportAbsolutePath);
		ReportResponse reportResponse = null;
		SupplierInfo supplierInfo = null;
		ParamNameValues paramNameValues = new ParamNameValues();
		ArrayOfParamNameValue arrayOfParamNameValue = paramNameValues.getListOfParamNameValues();
		if (arrayOfParamNameValue == null) {
			arrayOfParamNameValue = new ArrayOfParamNameValue();
		}
		List<ParamNameValue> paramNameValueList = arrayOfParamNameValue.getItem();
		ParamNameValue paramNameValue = new ParamNameValue();
		paramNameValue.setName("party_name");
		paramNameValue.setLabel("party_name");
		ArrayOfString arrayOfValue = new ArrayOfString();
		arrayOfValue.getItem().add(payeeName);
		paramNameValue.setValues(arrayOfValue);
		paramNameValueList.add(paramNameValue);
		paramNameValues.setListOfParamNameValues(arrayOfParamNameValue);
		reportRequest.setParameterNameValues(paramNameValues);
		try {
			reportRequest.setAttributeFormat("xml");
			reportRequest.setAttributeLocale("en_US");
			reportRequest.setFlattenXML(false);
			reportRequest.setByPassCache(false);
			reportRequest.setSizeOfDataChunkDownload(-1);
			reportResponse = reportService.runReport(reportRequest,
					InvoiceUtil.getPropertyValue("FUSION_INTEGRATION_USER_ID"),
					InvoiceUtil.getPropertyValue("FUSION_INTEGRATION_USER_PASSWORD"));

			byte[] data = reportResponse.getReportBytes();
			logger.debug("data.length"+data.length);
			
			//Path path = FileSystems.getDefault().getPath("c:\\logs\\2.xml");
			//Files.write(path, data, StandardOpenOption.CREATE);
			
			JAXBContext jaxbContext = JAXBContext
					.newInstance(cloud.data.integration.module.invoice.report.jaxb.partyname.ObjectFactory.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			ByteArrayInputStream input = new ByteArrayInputStream(data);
			
			Object object = unmarshaller.unmarshal(input);
			cloud.data.integration.module.invoice.report.jaxb.partyname.DATADS datads = (cloud.data.integration.module.invoice.report.jaxb.partyname.DATADS) object;
			input.close();
			supplierInfo = new SupplierInfo();
			if(datads.getG1() ==null) {
				throw new Exception("The supplier does not exist");
			}
			supplierInfo.setSupplierId(datads.getG1().getSUPPLIERID());
			supplierInfo.setSupplierName(datads.getG1().getSUPPLIERNAME());
			supplierInfo.setSupplierNumber(datads.getG1().getSUPPLIERNUMBER());
			supplierInfo.setSupplierSite(datads.getG1().getSUPPLIERSITE());
			logger.debug("Supplier name" + datads.getG1().getSUPPLIERNAME());
			logger.debug("Supplier number" + datads.getG1().getSUPPLIERNUMBER());
			logger.debug("Supplier Id" + datads.getG1().getSUPPLIERID());
			logger.debug("Supplier Site" + datads.getG1().getSUPPLIERSITE());

			// journalLedgerId = String.valueOf(datads.getG1().getLEDGERID());
			// logger.debug("The Journal Category retrieved from Oracle Fusion is: " +
			// datads.getG1().getLEDGERID());

		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("Error"+e.getMessage());
			throw e;
		}

		return supplierInfo;
	}
	
	public void getBIPaymentReport() throws Exception {

		String reportAbsolutePath = InvoiceUtil.getPropertyValue("INVOICE_FUSION_BI_PUBLISHER_PAYMENT_REPORT_PATH");
		ReportService reportService = CloudDataIntegrationService.getBIPublisherReportService();
		ReportRequest reportRequest = new ReportRequest();
		reportRequest.setReportAbsolutePath(reportAbsolutePath);
		ReportResponse reportResponse = null;
		ParamNameValues paramNameValues = new ParamNameValues();
		ArrayOfParamNameValue arrayOfParamNameValue = paramNameValues.getListOfParamNameValues();
		if (arrayOfParamNameValue == null) {
			arrayOfParamNameValue = new ArrayOfParamNameValue();
		}
		List<ParamNameValue> paramNameValueList = arrayOfParamNameValue.getItem();
		ParamNameValue paramNameValue = new ParamNameValue();
		ArrayOfString arrayOfValue = new ArrayOfString();
		paramNameValue.setValues(arrayOfValue);
		paramNameValueList.add(paramNameValue);
		paramNameValues.setListOfParamNameValues(arrayOfParamNameValue);
		reportRequest.setParameterNameValues(paramNameValues);
		try {
			//reportRequest.setAttributeFormat("text");
			reportRequest.setAttributeLocale("en_US");
			reportRequest.setFlattenXML(false);
			reportRequest.setByPassCache(false);
			reportRequest.setSizeOfDataChunkDownload(-1);
			reportResponse = reportService.runReport(reportRequest,
					InvoiceUtil.getPropertyValue("FUSION_INTEGRATION_USER_ID"),
					InvoiceUtil.getPropertyValue("FUSION_INTEGRATION_USER_PASSWORD"));

			byte[] data = reportResponse.getReportBytes();

			logger.debug("getBIPaymentReport data.length"+data.length);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String dateToAppend = sdf.format(new Date());
			String outFileName=InvoiceUtil.getPropertyValue("INVOICE_PAYABLES_REPORT_OUT_FILE_NAME");
			
		/*	String outFileDir=InvoiceUtil.getPropertyValue("INVOICE_PAYABLES_REPORT_OUT_FILE_DIR");
			String outFileArchive=InvoiceUtil.getPropertyValue("INVOICE_PAYABLES_REPORT_OUT_FILE_ARCHIVE");
			
			File sourceFiles = new File(outFileDir);
			File[] sourceFilesList = sourceFiles.listFiles();
			if(sourceFilesList.length>0) {
				CommonMethods.moveFiles(sourceFilesList, outFileArchive);
				logger.debug("Moved old files to "+outFileArchive);
			}
			*/
			outFileName=outFileName+dateToAppend+".txt";
			Path path = FileSystems.getDefault().getPath(outFileName);
			Files.write(path, data, StandardOpenOption.CREATE);
		} 
		catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
	}
	
	public String getBIReportDistributionSetName(String inputCOA) throws Exception {

		String reportAbsolutePath = InvoiceUtil.getPropertyValue("INVOICE_FUSION_BI_PUBLISHER_EW_COA_MAPPING_PATH");
		ReportService reportService = CloudDataIntegrationService.getBIPublisherReportService();
		ReportRequest reportRequest = new ReportRequest();
		reportRequest.setReportAbsolutePath(reportAbsolutePath);
		ReportResponse reportResponse = null;
		String distributionSetName=null;
		ParamNameValues paramNameValues = new ParamNameValues();
		ArrayOfParamNameValue arrayOfParamNameValue = paramNameValues.getListOfParamNameValues();
		if (arrayOfParamNameValue == null) {
			arrayOfParamNameValue = new ArrayOfParamNameValue();
		}
		List<ParamNameValue> paramNameValueList = arrayOfParamNameValue.getItem();
		ParamNameValue paramNameValue = new ParamNameValue();
		paramNameValue.setName("p_input_coa");
		paramNameValue.setLabel("p_input_coa");
		ArrayOfString arrayOfValue = new ArrayOfString();
		arrayOfValue.getItem().add(inputCOA);
		paramNameValue.setValues(arrayOfValue);
		paramNameValueList.add(paramNameValue);
		paramNameValues.setListOfParamNameValues(arrayOfParamNameValue);
		reportRequest.setParameterNameValues(paramNameValues);
		try {
			reportRequest.setAttributeFormat("xml");
			reportRequest.setAttributeLocale("en_US");
			reportRequest.setFlattenXML(false);
			reportRequest.setByPassCache(false);
			reportRequest.setSizeOfDataChunkDownload(-1);
			reportResponse = reportService.runReport(reportRequest,
					InvoiceUtil.getPropertyValue("FUSION_INTEGRATION_USER_ID"),
					InvoiceUtil.getPropertyValue("FUSION_INTEGRATION_USER_PASSWORD"));

			byte[] data = reportResponse.getReportBytes();
			logger.debug("data.length"+data.length);
			//Path path = FileSystems.getDefault().getPath("c:\\logs\\1.xml");
			//File file = new File(InvoiceUtil.getPropertyValue("REPORT_LEDGER_DIRECTORY_PATH_NAME"));
			//file.mkdirs();
			//Files.write(path, data, StandardOpenOption.CREATE); 
			JAXBContext jaxbContext = JAXBContext
					.newInstance(cloud.data.integration.module.invoice.report.jaxb.coa.ObjectFactory.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			ByteArrayInputStream input = new ByteArrayInputStream(data);
			
			Object object = unmarshaller.unmarshal(input);
			cloud.data.integration.module.invoice.report.jaxb.coa.DATADS datads = (cloud.data.integration.module.invoice.report.jaxb.coa.DATADS) object;
			input.close();
			
			if(datads.getG1()== null) {
				throw new Exception("Distribution set name is not found");
			}
			distributionSetName=datads.getG1().getDISTRIBUTIONSETNAME();
			logger.debug("Distribution set name" + distributionSetName);

			logger.debug(distributionSetName);
			// journalLedgerId = String.valueOf(datads.getG1().getLEDGERID());
			// logger.debug("The Journal Category retrieved from Oracle Fusion is: " +
			// datads.getG1().getLEDGERID());

		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("Distribution set name is not found");
			throw e;
		}

		return distributionSetName;
	}
	

	
	public  String uploadFileToFusion(String fileName,String customFile)
			throws cloud.data.integration.common.erpintegrationservice.webservice.ServiceException, 
			IOException {

		ErpIntegrationService erpService = CloudDataIntegrationService.getErpIntegrationService();
		ObjectFactory objectFactory = new ObjectFactory();

		File zipFile = new File(fileName);

		// UploadFileToUcm uploadFile = objectFactory.createUploadFileToUcm();
		DocumentDetails documentDetails = objectFactory.createDocumentDetails();

		JAXBElement<String> documentTitle = objectFactory.createDocumentDetailDocumentTitle(zipFile.getName());
		documentDetails.setDocumentTitle(documentTitle);

		JAXBElement<String> documentAuthor = objectFactory.createDocumentDetailDocumentAuthor("Arun Mavoori");
		documentDetails.setDocumentAuthor(documentAuthor);

		JAXBElement<String> documentId = objectFactory.createDocumentDetailDocumentId("1000");
		documentDetails.setDocumentId(documentId);

		JAXBElement<String> documentContentType = objectFactory.createDocumentDetailContentType("zip");
		documentDetails.setContentType(documentContentType);

		JAXBElement<String> documentSecurityGroup = objectFactory
				.createDocumentDetailDocumentSecurityGroup("FAFusionImportExport");
		documentDetails.setDocumentSecurityGroup(documentSecurityGroup);

		JAXBElement<String> documentAccount = objectFactory
				.createDocumentDetailDocumentAccount("fin$/payables$/import$");
		documentDetails.setDocumentAccount(documentAccount);

		documentDetails.setFileName(zipFile.getName());

		// File fileToUpload = new File(fileName);
		String zipDirectoryPath = InvoiceUtil.getPropertyValue("INVOICE_ZIP_DIRECTORY_NAME",customFile);
		String zipFileName = InvoiceUtil.getPropertyValue("INVOICE_ZIP_FILE_NAME",customFile);
		Path filePath = FileSystems.getDefault().getPath(zipDirectoryPath, zipFileName);
		byte[] documentContent = null;

		documentContent = Files.readAllBytes(filePath);
		if (documentContent == null || documentContent.length == 0) {

			logger.debug("documentContent" + documentContent.length);
		}
		documentDetails.setContent(documentContent);

		String serverReturnedDocId = null;

		serverReturnedDocId = erpService.uploadFileToUcm(documentDetails);
		logger.debug("Server Returned Document Id: " + serverReturnedDocId);
		return serverReturnedDocId;
	}
	
	public long submitESSJobRequestForInterfaceLoader(String documentId) throws cloud.data.integration.common.erpintegrationservice.webservice.ServiceException {
		
		long essJobStatusId = 0;
		ErpIntegrationService erpService = CloudDataIntegrationService.getErpIntegrationService();
		String jobPackageName="/oracle/apps/ess/financials/commonModules/shared/common/interfaceLoader";
		String jobDefinitionName="InterfaceLoaderController";
		List<String> paramList = new ArrayList<String>();
		paramList.add("1");
		paramList.add(documentId);
		essJobStatusId = erpService.submitESSJobRequest(jobPackageName, jobDefinitionName, paramList);
		return essJobStatusId;
	}
	
	public String getESSJobStatus(long essJobSubmitId) 
			throws cloud.data.integration.common.erpintegrationservice.webservice.ServiceException {
	
		ErpIntegrationService erpService = CloudDataIntegrationService.getErpIntegrationService();
		String essJobStatus = erpService.getESSJobStatus(essJobSubmitId); 
		logger.debug("ESSJobStatus: " + essJobStatus);
		return essJobStatus;
	}
	
	public String getJobStatus(long requestId) {
		ErpIntegrationService erpService = CloudDataIntegrationService.getErpIntegrationService();
		String status = "";
			try {
				Thread.sleep(60000);
				status = erpService.getESSJobStatus(requestId);
			} catch (InterruptedException e1) {
				logger.error("Error"+e1.getMessage());
				e1.printStackTrace();
			} catch (cloud.data.integration.common.erpintegrationservice.webservice.ServiceException e) {
				logger.error("Error"+e.getMessage());
				e.printStackTrace();
			}			

		while ((status.equals("RUNNING")) || (status.equals("WAIT"))
				|| (status.equals("READY")) || (status.equals("PAUSED"))
				|| (status.equals("BLOCKED")) || (status.equals("COMPLETED"))) {
			try {
				//Thread.sleep(10000);
				Thread.sleep(20000);
				status = erpService.getESSJobStatus(requestId);
				
			} catch (InterruptedException e) {
				logger.error("Error"+e.getMessage());
				e.printStackTrace();
			} catch (cloud.data.integration.common.erpintegrationservice.webservice.ServiceException e) {
				logger.error("Error"+e.getMessage());
				e.printStackTrace();
			} 
			logger.debug("Waiting for ESS Job Status: " + status);
		}
		return status;
	}
	
public long submitESSJobRequestForImportPayableInvoice(String customFile) {
		
		long essJobStatusId = 0;
		
		ErpIntegrationService erpService = CloudDataIntegrationService.getErpIntegrationService();
		String jobPackageName="oracle/apps/ess/financials/payables/invoices/transactions";
		String jobDefinitionName="APXIIMPT";
		List<String> paramList = new ArrayList<String>();
	
		
		paramList.add(InvoiceUtil.getPropertyValue("IMPORT_PAYABLE_INVOICE_PARAM1",customFile).trim());
		paramList.add(InvoiceUtil.getPropertyValue("IMPORT_PAYABLE_INVOICE_PARAM2",customFile).trim());
		paramList.add(InvoiceUtil.getPropertyValue("IMPORT_PAYABLE_INVOICE_PARAM3",customFile).trim());
		paramList.add(InvoiceUtil.getPropertyValue("IMPORT_PAYABLE_INVOICE_PARAM4",customFile).trim());
		paramList.add(InvoiceUtil.getPropertyValue("IMPORT_PAYABLE_INVOICE_PARAM5",customFile).trim());
		paramList.add(InvoiceUtil.getPropertyValue("IMPORT_PAYABLE_INVOICE_PARAM6",customFile).trim());
		paramList.add(InvoiceUtil.getPropertyValue("IMPORT_PAYABLE_INVOICE_PARAM7",customFile).trim());
		paramList.add(InvoiceUtil.getPropertyValue("IMPORT_PAYABLE_INVOICE_PARAM8",customFile).trim());
		paramList.add(InvoiceUtil.getPropertyValue("IMPORT_PAYABLE_INVOICE_PARAM9",customFile).trim());
		paramList.add(InvoiceUtil.getPropertyValue("IMPORT_PAYABLE_INVOICE_PARAM10",customFile).trim());
		paramList.add(InvoiceUtil.getPropertyValue("IMPORT_PAYABLE_INVOICE_PARAM11",customFile).trim());
		paramList.add(InvoiceUtil.getPropertyValue("IMPORT_PAYABLE_INVOICE_PARAM12",customFile).trim());
		paramList.add(InvoiceUtil.getPropertyValue("IMPORT_PAYABLE_INVOICE_PARAM13",customFile).trim());
		paramList.add(InvoiceUtil.getPropertyValue("IMPORT_PAYABLE_INVOICE_PARAM14",customFile).trim());
		
		try {
			essJobStatusId = erpService.submitESSJobRequest(jobPackageName, jobDefinitionName, paramList);
		} catch (cloud.data.integration.common.erpintegrationservice.webservice.ServiceException e) {
			logger.error("Error"+e.getMessage());
			e.printStackTrace();
		}
		logger.debug("Service Type Name: " + essJobStatusId);
		return essJobStatusId;
	}

public List<DocumentDetails> downloadESSJobExecutionDetails(long essJobSubmitId) 
		throws cloud.data.integration.common.erpintegrationservice.webservice.ServiceException {

	ErpIntegrationService erpService = CloudDataIntegrationService.getErpIntegrationService();
	List<DocumentDetails> jobExecutionDetailsList = erpService.downloadESSJobExecutionDetails(String.valueOf(essJobSubmitId), null); 
	logger.debug("Job Execution Details List Size: " + jobExecutionDetailsList.size());
	return jobExecutionDetailsList;
} 

private List<String[]> buildHeaderData(Map<String, InvoiceData> headerLevelData, String headerKey,
		double totalLineLevelAmout,String description,int inti,List<String[]> data) throws ParseException {
	SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
	DateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
	Date date=inputFormat.parse(headerLevelData.get(headerKey).getIfcVoucherDate());
	String invoiceDate = format1.format(date);  
	logger.debug("Invoice date "+invoiceDate);
	
	DecimalFormat df = new DecimalFormat("#.00");
	
	data.add(new String[] { "" + headerLevelData.get(headerKey).getInvoiceId(),
			InvoiceUtil.getPropertyValue("INVOICE_AP_INVOICES_BUSINESSUNIT_HEADER"),
			InvoiceUtil.getPropertyValue("INVOICE_AP_INVOICES_SOURCE_HEADER"), 
			"EWINV"+headerLevelData.get(headerKey).getInvoiceId(),
			df.format(totalLineLevelAmout),
			invoiceDate,null,
			headerLevelData.get(headerKey).getIfcPayeeId(),
			headerLevelData.get(headerKey).getSiteName(),
			"USD",
			"USD",
			description,
			null,
			"STANDARD",
			null,null,null,null,null,
			"Immediate",
			null,null,null,
			invoiceDate,
			headerLevelData.get(headerKey).getIfcDirectDepositIndicator(),
			"Standard",
			null,null,null,null,null,null,null,null,null,null,
			null,null,null,null,null,null,null,null,null,null,
			null,null,null,null,null,null,null,null,null,null,
			null,null,null,null,null,null,null,null,null,null,
			null,null,null,null,null,null,null,null,
			headerLevelData.get(headerKey).getIfcProviderId(),headerLevelData.get(headerKey).getIfcChildIdentifier(),headerLevelData.get(headerKey).getIfcVoucherNumber(),
			headerLevelData.get(headerKey).getIfcVoucherTypeCode(),
			null,null,null,null,null,null,null,null,null,null,
			null,null,null,null,null,null,null,null,null,null,
			null,null,null,null,null,null,null,null,null,null,
			null,null,null,null,null,null,null,null,null,null,
			null,null,null,null,null,null,null,null,null,null,
			null,null,null
	});
	return data;
}

private List<String[]> buildDataLineItems(Map<String, InvoiceData> headerLevelData, String headerKey,
		ItemInfo itemInfo, int intj, List<String[]> dataLineItems) throws ParseException {
	
	SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
	DateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
	Date date=inputFormat.parse(headerLevelData.get(headerKey).getIfcVoucherDate());
	String invoiceDate = format1.format(date); 
	logger.debug("Line Item invoice date"+invoiceDate);
	String distributionCombination=calculateDistributionCombination();
	String distributionSetName=headerLevelData.get(headerKey).getDistributionSetName();
	
	logger.debug("Line Item level"+distributionSetName);
	dataLineItems.add(new String[] { 
			"" + headerLevelData.get(headerKey).getInvoiceId(), 
			String.valueOf(intj),
			itemInfo.getLineType(), 
			String.valueOf(itemInfo.getAmount()),
			null,null,null,null,null,null,null,null,null,null,
			null,null,null,null,null,null,null,null,distributionSetName,invoiceDate,
			null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
			null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
			null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
			null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
			null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
			null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
			null,null,null,null,null,null,null,null,null,null,null });
	return dataLineItems;
}

private String calculateDistributionCombination() {
	// TODO Auto-generated method stub
	return null;//"110-0000-000000-10010-00000-00000";
}

public Long submitESSJobRequestForImportPayableInvoiceReport(Long journalImportrequestId) {
	long essJobStatusId = 0;
	
	ErpIntegrationService erpService = CloudDataIntegrationService.getErpIntegrationService();
	String jobPackageName="oracle/apps/ess/financials/payables/invoices/transactions";
	String jobDefinitionName="APXIIMPT_BIP";
	List<String> paramList = new ArrayList<String>();
	paramList.add(journalImportrequestId.toString());
	
	try {
		essJobStatusId = erpService.submitESSJobRequest(jobPackageName, jobDefinitionName, paramList);
	} catch (cloud.data.integration.common.erpintegrationservice.webservice.ServiceException e) {
		logger.error("Error"+e.getMessage());
		e.printStackTrace();
	}
	logger.debug("Service Type Name: " + essJobStatusId);
	return essJobStatusId;
}

public Long getBIImportInvoiceReportDetails(Long parentId) throws Exception {

	String reportAbsolutePath = InvoiceUtil.getPropertyValue("INVOICE_FUSION_BI_PUBLISHER_INVOICE_REPORT_PATH");
	ReportService reportService = CloudDataIntegrationService.getBIPublisherReportService();
	ReportRequest reportRequest = new ReportRequest();
	reportRequest.setReportAbsolutePath(reportAbsolutePath);
	ReportResponse reportResponse = null;
	ParamNameValues paramNameValues = new ParamNameValues();
	ArrayOfParamNameValue arrayOfParamNameValue = paramNameValues.getListOfParamNameValues();
	if (arrayOfParamNameValue == null) {
		arrayOfParamNameValue = new ArrayOfParamNameValue();
	}
	List<ParamNameValue> paramNameValueList = arrayOfParamNameValue.getItem();
	ParamNameValue paramNameValue = new ParamNameValue();
	paramNameValue.setName("parentId");
	paramNameValue.setLabel("parentId");
	ArrayOfString arrayOfValue = new ArrayOfString();
	arrayOfValue.getItem().add(parentId.toString());
	paramNameValue.setValues(arrayOfValue);
	paramNameValueList.add(paramNameValue);
	paramNameValues.setListOfParamNameValues(arrayOfParamNameValue);
	reportRequest.setParameterNameValues(paramNameValues);
	Long requestId;
	try {
		reportRequest.setAttributeFormat("xml");
		reportRequest.setAttributeLocale("en_US");
		reportRequest.setFlattenXML(false);
		reportRequest.setByPassCache(false);
		reportRequest.setSizeOfDataChunkDownload(-1);
		reportResponse = reportService.runReport(reportRequest,
				InvoiceUtil.getPropertyValue("FUSION_INTEGRATION_USER_ID"),
				InvoiceUtil.getPropertyValue("FUSION_INTEGRATION_USER_PASSWORD"));

		byte[] data = reportResponse.getReportBytes();
	
		JAXBContext jaxbContext = JAXBContext
				.newInstance(cloud.data.integration.module.invoice.report.jaxb.parentreport.ObjectFactory.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		ByteArrayInputStream input = new ByteArrayInputStream(data);
		Object object = unmarshaller.unmarshal(input);
		cloud.data.integration.module.invoice.report.jaxb.parentreport.DATADS datads = (cloud.data.integration.module.invoice.report.jaxb.parentreport.DATADS) object;
		input.close();
		requestId= Long.valueOf(datads.getG1().getREQUESTID());
		logger.debug("child request id" + requestId);

		// journalLedgerId = String.valueOf(datads.getG1().getLEDGERID());
		// logger.debug("The Journal Category retrieved from Oracle Fusion is: " +
		// datads.getG1().getLEDGERID());
		
	} catch (Exception e) {
		logger.error("Error"+e.getMessage());
		e.printStackTrace();
		throw e;
	}

	return requestId;
}

}
