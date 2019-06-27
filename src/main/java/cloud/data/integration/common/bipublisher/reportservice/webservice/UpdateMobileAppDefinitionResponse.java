
package cloud.data.integration.common.bipublisher.reportservice.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="updateMobileAppDefinitionReturn" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    "updateMobileAppDefinitionReturn"
})
@XmlRootElement(name = "updateMobileAppDefinitionResponse")
public class UpdateMobileAppDefinitionResponse {

    protected boolean updateMobileAppDefinitionReturn;

    /**
     * Gets the value of the updateMobileAppDefinitionReturn property.
     * 
     */
    public boolean isUpdateMobileAppDefinitionReturn() {
        return updateMobileAppDefinitionReturn;
    }

    /**
     * Sets the value of the updateMobileAppDefinitionReturn property.
     * 
     */
    public void setUpdateMobileAppDefinitionReturn(boolean value) {
        this.updateMobileAppDefinitionReturn = value;
    }

}
