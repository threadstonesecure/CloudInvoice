
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
 *         &lt;element name="updateMobileAppDefinitionInSessionReturn" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    "updateMobileAppDefinitionInSessionReturn"
})
@XmlRootElement(name = "updateMobileAppDefinitionInSessionResponse")
public class UpdateMobileAppDefinitionInSessionResponse {

    protected boolean updateMobileAppDefinitionInSessionReturn;

    /**
     * Gets the value of the updateMobileAppDefinitionInSessionReturn property.
     * 
     */
    public boolean isUpdateMobileAppDefinitionInSessionReturn() {
        return updateMobileAppDefinitionInSessionReturn;
    }

    /**
     * Sets the value of the updateMobileAppDefinitionInSessionReturn property.
     * 
     */
    public void setUpdateMobileAppDefinitionInSessionReturn(boolean value) {
        this.updateMobileAppDefinitionInSessionReturn = value;
    }

}
