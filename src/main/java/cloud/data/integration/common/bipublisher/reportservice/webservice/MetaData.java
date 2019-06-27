
package cloud.data.integration.common.bipublisher.reportservice.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MetaData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MetaData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="metaDataName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="metaDataValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MetaData", propOrder = {
    "metaDataName",
    "metaDataValue"
})
public class MetaData {

    @XmlElement(required = true, nillable = true)
    protected String metaDataName;
    @XmlElement(required = true, nillable = true)
    protected String metaDataValue;

    /**
     * Gets the value of the metaDataName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMetaDataName() {
        return metaDataName;
    }

    /**
     * Sets the value of the metaDataName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMetaDataName(String value) {
        this.metaDataName = value;
    }

    /**
     * Gets the value of the metaDataValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMetaDataValue() {
        return metaDataValue;
    }

    /**
     * Sets the value of the metaDataValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMetaDataValue(String value) {
        this.metaDataValue = value;
    }

}
