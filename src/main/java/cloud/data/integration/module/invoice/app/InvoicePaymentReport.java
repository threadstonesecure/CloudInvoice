package cloud.data.integration.module.invoice.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import cloud.data.integration.module.invoice.service.InvoiceService;
import cloud.data.integration.module.invoice.util.InvoiceUtil;


public class InvoicePaymentReport {
	final static Logger logger = Logger.getLogger(InvoicePaymentReport.class);
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		initializeLog4j();
		System.setProperty("org.xml.sax.driver", "com.sun.org.apache.xerces.internal.parsers.SAXParser");
		System.setProperty("javax.xml.parsers.DocumentBuilderFactory","com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
		System.setProperty("javax.xml.parsers.SAXParserFactory","com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
		InvoiceService inServ=new InvoiceService();
        try {
        	logger.debug("Calling BI Payment Report");
			inServ.getBIPaymentReport();
			logger.debug("End BI Payment Report");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Error "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void initializeLog4j() {
		BasicConfigurator.configure();
		final String LOG_FILE = InvoiceUtil.getPropertyValue("JOURNAL_LOG_PROPERTY_FILE"); 
	      Properties logProp = new Properties();      
	      try {      
	    	  logProp.load(new FileInputStream (LOG_FILE));  
	          PropertyConfigurator.configure(logProp);      
	          logger.info("Logging enabled");
	      }catch(IOException e) {       
	    	  logger.error("Log4j logging not enabled..."); 
	    	  logger.error(e.getMessage());
	      }  
	}

}
