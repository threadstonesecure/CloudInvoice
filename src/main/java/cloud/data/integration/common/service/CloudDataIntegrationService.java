package cloud.data.integration.common.service;

import javax.xml.ws.BindingProvider;

import cloud.data.integration.common.bipublisher.reportservice.webservice.ReportService;
import cloud.data.integration.common.bipublisher.reportservice.webservice.ReportService_Service;
import cloud.data.integration.common.erpintegrationservice.webservice.ErpIntegrationService;
import cloud.data.integration.common.erpintegrationservice.webservice.ErpIntegrationService_Service;
import cloud.data.integration.module.invoice.util.InvoiceUtil;


public class CloudDataIntegrationService {

	public static ErpIntegrationService getErpIntegrationService() {
		ErpIntegrationService_Service service = new ErpIntegrationService_Service();
		ErpIntegrationService erpIntegrationService = service.getErpIntegrationServiceSoapHttpPort();
		BindingProvider bindingProvider = (BindingProvider)erpIntegrationService;
		bindingProvider.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, 
				InvoiceUtil.getPropertyValue("FUSION_INTEGRATION_USER_ID"));
		bindingProvider.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY,
				InvoiceUtil.getPropertyValue("FUSION_INTEGRATION_USER_PASSWORD"));
		
		bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, InvoiceUtil.getPropertyValue("ERP_INTEGRATION_SERVICE_URL_WSDL"));
		return erpIntegrationService;
	}
	
	public static ReportService getBIPublisherReportService() {
		
		ReportService_Service service = new ReportService_Service();
    	ReportService reportService = service.getReportService();
		BindingProvider bindingProvider = (BindingProvider)reportService;
		bindingProvider.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, 
				InvoiceUtil.getPropertyValue("FUSION_INTEGRATION_USER_ID"));
		bindingProvider.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY,
				InvoiceUtil.getPropertyValue("FUSION_INTEGRATION_USER_PASSWORD"));
		bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, InvoiceUtil.getPropertyValue("REPORT_SERVICE_URL_WSDL"));
		return reportService;
	}
	
	
	
}
