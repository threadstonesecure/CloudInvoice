
package cloud.data.integration.common.bipublisher.reportservice.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MetaDataList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MetaDataList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="metaDataList" type="{http://xmlns.oracle.com/oxp/service/v2}ArrayOfMetaData"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MetaDataList", propOrder = {
    "metaDataList"
})
public class MetaDataList {

    @XmlElement(required = true, nillable = true)
    protected ArrayOfMetaData metaDataList;

    /**
     * Gets the value of the metaDataList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfMetaData }
     *     
     */
    public ArrayOfMetaData getMetaDataList() {
        return metaDataList;
    }

    /**
     * Sets the value of the metaDataList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfMetaData }
     *     
     */
    public void setMetaDataList(ArrayOfMetaData value) {
        this.metaDataList = value;
    }

}
