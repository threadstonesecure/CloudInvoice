
package cloud.data.integration.common.bipublisher.reportservice.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="uploadReportDataChunkInSessionReturn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "uploadReportDataChunkInSessionReturn"
})
@XmlRootElement(name = "uploadReportDataChunkInSessionResponse")
public class UploadReportDataChunkInSessionResponse {

    @XmlElement(required = true)
    protected String uploadReportDataChunkInSessionReturn;

    /**
     * Gets the value of the uploadReportDataChunkInSessionReturn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUploadReportDataChunkInSessionReturn() {
        return uploadReportDataChunkInSessionReturn;
    }

    /**
     * Sets the value of the uploadReportDataChunkInSessionReturn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUploadReportDataChunkInSessionReturn(String value) {
        this.uploadReportDataChunkInSessionReturn = value;
    }

}
