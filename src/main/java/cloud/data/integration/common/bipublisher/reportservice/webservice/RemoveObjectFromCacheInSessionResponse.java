
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
 *         &lt;element name="removeObjectFromCacheInSessionReturn" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    "removeObjectFromCacheInSessionReturn"
})
@XmlRootElement(name = "removeObjectFromCacheInSessionResponse")
public class RemoveObjectFromCacheInSessionResponse {

    protected boolean removeObjectFromCacheInSessionReturn;

    /**
     * Gets the value of the removeObjectFromCacheInSessionReturn property.
     * 
     */
    public boolean isRemoveObjectFromCacheInSessionReturn() {
        return removeObjectFromCacheInSessionReturn;
    }

    /**
     * Sets the value of the removeObjectFromCacheInSessionReturn property.
     * 
     */
    public void setRemoveObjectFromCacheInSessionReturn(boolean value) {
        this.removeObjectFromCacheInSessionReturn = value;
    }

}
