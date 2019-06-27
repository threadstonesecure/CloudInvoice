package cloud.data.integration.common.util;

import java.io.IOException;

public class UtilTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			CommonMethods.processCSVFile("/toshiba/ap/in/Toshiba_AP_Invoice_Lines_Interface_20190204.csv", "/toshiba/ap/in/1.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
