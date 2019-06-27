
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
 *         &lt;element name="getMobileAppDefinitionInSessionReturn" type="{http://xmlns.oracle.com/oxp/service/v2}MobileAppDefinition"/>
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
    "getMobileAppDefinitionInSessionReturn"
})
@XmlRootElement(name = "getMobileAppDefinitionInSessionResponse")
public class GetMobileAppDefinitionInSessionResponse {

    @XmlElement(required = true)
    protected MobileAppDefinition getMobileAppDefinitionInSessionReturn;

    /**
     * Gets the value of the getMobileAppDefinitionInSessionReturn property.
     * 
     * @return
     *     possible object is
     *     {@link MobileAppDefinition }
     *     
     */
    public MobileAppDefinition getGetMobileAppDefinitionInSessionReturn() {
        return getMobileAppDefinitionInSessionReturn;
    }

    /**
     * Sets the value of the getMobileAppDefinitionInSessionReturn property.
     * 
     * @param value
     *     allowed object is
     *     {@link MobileAppDefinition }
     *     
     */
    public void setGetMobileAppDefinitionInSessionReturn(MobileAppDefinition value) {
        this.getMobileAppDefinitionInSessionReturn = value;
    }

}
