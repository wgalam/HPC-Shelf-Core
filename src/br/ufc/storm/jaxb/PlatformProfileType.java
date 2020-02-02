//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.4.0-b180830.0438 
// Consulte <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2020.02.02 às 07:19:57 PM BRT 
//


package br.ufc.storm.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de platform_profile_type complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="platform_profile_type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="platform_contract" type="{http://storm.lia.ufc.br}context_contract"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="network_ip_address" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="port" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "platform_profile_type", propOrder = {
    "platformContract"
})
public class PlatformProfileType {

    @XmlElement(name = "platform_contract", required = true)
    protected ContextContract platformContract;
    @XmlAttribute(name = "network_ip_address")
    protected String networkIpAddress;
    @XmlAttribute(name = "port")
    protected String port;

    /**
     * Obtém o valor da propriedade platformContract.
     * 
     * @return
     *     possible object is
     *     {@link ContextContract }
     *     
     */
    public ContextContract getPlatformContract() {
        return platformContract;
    }

    /**
     * Define o valor da propriedade platformContract.
     * 
     * @param value
     *     allowed object is
     *     {@link ContextContract }
     *     
     */
    public void setPlatformContract(ContextContract value) {
        this.platformContract = value;
    }

    /**
     * Obtém o valor da propriedade networkIpAddress.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNetworkIpAddress() {
        return networkIpAddress;
    }

    /**
     * Define o valor da propriedade networkIpAddress.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNetworkIpAddress(String value) {
        this.networkIpAddress = value;
    }

    /**
     * Obtém o valor da propriedade port.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPort() {
        return port;
    }

    /**
     * Define o valor da propriedade port.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPort(String value) {
        this.port = value;
    }

}
