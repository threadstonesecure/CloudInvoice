package cloud.data.integration.module.invoice.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import cloud.data.integration.module.invoice.util.InvoiceUtil;



/**
 * This is the main class for processing the Invoice Ledger Journal Import processing functionality.
 * @author SBalawatri
 *
 */
public class InvoiceApp {

	final static Logger logger = Logger.getLogger(InvoiceApp.class);
	public static void main(String[] args) {
		try {
			initializeLog4j(args[0]);
			logger.debug("Starting main thread file "+args[0] );
			InvoiceRunnable invRunnable = new InvoiceRunnable(args[0]);
			System.setProperty("org.xml.sax.driver", "com.sun.org.apache.xerces.internal.parsers.SAXParser");
			System.setProperty("javax.xml.parsers.DocumentBuilderFactory","com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
			System.setProperty("javax.xml.parsers.SAXParserFactory","com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
			Thread t = new Thread(invRunnable);
			t.start();
			logger.debug("End main thread"+ new Date());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Error"+e.getMessage());
		}
	 
	} 
	
	public static void initializeLog4j(String custFile) {
		final String LOG_FILE = InvoiceUtil.getPropertyValue("INVOICE_LOG_PROPERTY_FILE",custFile); 
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
