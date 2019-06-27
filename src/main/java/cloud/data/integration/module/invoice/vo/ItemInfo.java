/**
 * 
 */
package cloud.data.integration.module.invoice.vo;

/**
 * @author SBalawatri
 *
 */
public class ItemInfo {
	
	//Invoice ID,Line Number,Line Type,Amount,Invoice Quantity,Unit Price,UOM,Description,PO Number,PO Line Number,PO Schedule Number,PO Distribute Number,Item Description,PO Release Number,Purchasing Category,Receipt Number,Receipt Line Number,Consumption Advice Number,Consumption Advice Line Number,Packing Slip,Final Match,Distribution Combination,Distribution Set,Accounting Date,Overlay Account Segment,Overlay Primary Balancing Segment,Overlay Cost Center Segment,Tax Classification Code,Ship-to Location,Ship-from Location,Location of Final Discharge,Transaction Business Category,Product Fiscal Classification,Intended Use,User-Defined Fiscal Classification,Product Type,Assessable Value,Product Category,Tax Control Amount,Tax Regime Code,Tax,Tax Status Code,Tax Jurisdiction Code,Tax Rate Code,Tax Rate,Withholding Tax Group,Income Tax Type,Income Tax Region,Prorate Across All Item Lines,Line Group Number,Cost Factor Name,Statistical Quantity,Track as Asset,Asset Book Type Code,Asset Category ID,Serial Number,Manufacturer,Model Number,Warranty Number,Price Correction Line,Price Correction Invoice Number,Price Correction Invoice Line Number,Requester First Name,Requester Last Name,Requester Employee Number,Attribute Category,Attribute 1,Attribute 2,Attribute 3,Attribute 4,Attribute 5,Attribute 6,Attribute 7,Attribute 8,Attribute 9,Attribute 10,Attribute 11,Attribute 12,Attribute 13,Attribute 14,Attribute 15,Attribute Number 1,Attribute Number 2,Attribute Number 3,Attribute Number 4,Attribute Number 5,Attribute Date 1,Attribute Date 2,Attribute Date 3,Attribute Date 4,Attribute Date 5,Global Attribute Category,Global Attribute 1,Global Attribute 2,Global Attribute 3,Global Attribute 4,Global Attribute 5,Global Attribute 6,Global Attribute 7,Global Attribute 8,Global Attribute 9,Global Attribute 10,Global Attribute 11,Global Attribute 12,Global Attribute 13,Global Attribute 14,Global Attribute 15,Global Attribute 16,Global Attribute 17,Global Attribute 18,Global Attribute 19,Global Attribute 20,Global Attribute Number 1,Global Attribute Number 2,Global Attribute Number 3,Global Attribute Number 4,Global Attribute Number 5,Global Attribute Date 1,Global Attribute Date 2,Global Attribute Date 3,Global Attribute Date 4,Global Attribute Date 5,Project ID,Task ID,Expenditure Type ID,Expenditure Item Date,Ependiture Organization ID,Project Number,Task Number,Expenditure Type,Expenditure Organization,Funding Source ID,PJC Reserved Attribute 2,PJC Reserved Attribute 3,PJC Reserved Attribute 4,PJC Reserved Attribute 5,PJC Reserved Attribute 6,PJC Reserved Attribute 7,PJC Reserved Attribute 8,PJC Reserved Attribute 9,PJC Reserved Attribute 10,PJC User Defined Attribute 1,PJC User Defined Attribute 2,PJC User Defined Attribute 3,PJC User Defined Attribute 4,PJC User Defined Attribute 5,PJC User Defined Attribute 6,PJC User Defined Attribute 7,PJC User Defined Attribute 8,PJC User Defined Attribute 9,PJC User Defined Attribute 10,Fiscal Charge Type,Multiperiod Accounting Start Date,Multiperiod Accounting End Date,Multiperiod Accounting Accrual Account
	
	private String lineType="ITEM";
	private double amount;
	/**
	 * @return the lineType
	 */
	public String getLineType() {
		return lineType;
	}
	/**
	 * @param lineType the lineType to set
	 */
	public void setLineType(String lineType) {
		this.lineType = lineType;
	}
	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	

}
