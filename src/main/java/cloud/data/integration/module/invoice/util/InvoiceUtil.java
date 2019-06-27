package cloud.data.integration.module.invoice.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class InvoiceUtil {
	// load the properties only once
		private static Properties properties;
		private static Properties customProperties;
		final static Logger logger = Logger.getLogger(InvoiceUtil.class);
	public static String getPropertyValue (String stringKey){
		// reading property file from outside location
		    //logger.debug("getting property for key"+stringKey);
	    	return InvoiceUtil.getProperties().getProperty(stringKey);
	}
	
	 public static synchronized Properties getProperties(String fileName) {
	        if (customProperties != null) { return customProperties; }
	        try {
	        	customProperties= new Properties();
		        File jarPath=new File(InvoiceUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		        String propertiesPath=jarPath.getParentFile().getAbsolutePath();
		        logger.debug("loading file for "+fileName);
		        customProperties.load(new FileInputStream(propertiesPath+"/"+fileName+".properties"));
		        return customProperties;
		    } catch (IOException e1) {
		        e1.printStackTrace();
		        logger.error("unable to find property file in running jar location");
		        return null;
		    }
	    }
	    public static String getPropertyValue (String stringKey,String fileName){
			// reading property file from outside location
			    //logger.debug("getting property for key"+stringKey);
		    	return InvoiceUtil.getProperties(fileName).getProperty(stringKey);
		}

    public static synchronized Properties getProperties() {
        if (properties != null) { return properties; }
        try {
        	properties= new Properties();
	        File jarPath=new File(InvoiceUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	        String propertiesPath=jarPath.getParentFile().getAbsolutePath();
	        logger.debug(" propertiesPath-"+propertiesPath);
	        logger.debug("loading file ApplicationResources_invoice.properties");
	        properties.load(new FileInputStream(propertiesPath+"/ApplicationResources_invoice.properties"));
	        return properties;
	    } catch (IOException e1) {
	        e1.printStackTrace();
	        logger.error("unable to find property file in running jar location");
	        return null;
	    }
    }

	public static void updateInvoiceIdInFile(int maxInvoiceId) throws IOException {
		
		File jarPath=new File(InvoiceUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String propertiesPath=jarPath.getParentFile().getAbsolutePath();
		FileInputStream in = new FileInputStream(propertiesPath+"/invoice.properties");
		Properties props = new Properties();
		props.load(in);
		in.close();

		FileOutputStream out = new FileOutputStream(propertiesPath+"/invoice.properties");
		props.setProperty("invoiceid", ""+maxInvoiceId);
		props.store(out, null);
		out.close();
		
		
	}
}
