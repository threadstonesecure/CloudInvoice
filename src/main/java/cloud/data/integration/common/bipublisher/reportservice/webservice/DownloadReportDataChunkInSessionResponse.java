
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
 *         &lt;element name="downloadReportDataChunkInSessionReturn" type="{http://xmlns.oracle.com/oxp/service/v2}ReportDataChunk"/>
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
    "downloadReportDataChunkInSessionReturn"
})
@XmlRootElement(name = "downloadReportDataChunkInSessionResponse")
public class DownloadReportDataChunkInSessionResponse {

    @XmlElement(required = true)
    protected ReportDataChunk downloadReportDataChunkInSessionReturn;

    /**
     * Gets the value of the downloadReportDataChunkInSessionReturn property.
     * 
     * @return
     *     possible object is
     *     {@link ReportDataChunk }
     *     
     */
    public ReportDataChunk getDownloadReportDataChunkInSessionReturn() {
        return downloadReportDataChunkInSessionReturn;
    }

    /**
     * Sets the value of the downloadReportDataChunkInSessionReturn property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReportDataChunk }
     *     
     */
    public void setDownloadReportDataChunkInSessionReturn(ReportDataChunk value) {
        this.downloadReportDataChunkInSessionReturn = value;
    }

}
