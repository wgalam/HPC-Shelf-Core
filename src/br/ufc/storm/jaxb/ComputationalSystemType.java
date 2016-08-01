//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2016.08.01 às 03:00:29 PM BRT 
//


package br.ufc.storm.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de computational_system_type complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="computational_system_type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="context_contract" type="{http://storm.lia.ufc.br}context_contract"/>
 *         &lt;element name="port" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="files" type="{http://storm.lia.ufc.br}unit_file_type" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="session" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="network_address" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "computational_system_type", propOrder = {
    "contextContract",
    "port",
    "files"
})
public class ComputationalSystemType {

    @XmlElement(name = "context_contract", required = true)
    protected ContextContract contextContract;
    protected List<String> port;
    protected List<UnitFileType> files;
    @XmlAttribute(name = "session")
    protected Integer session;
    @XmlAttribute(name = "network_address")
    protected String networkAddress;

    /**
     * Obtém o valor da propriedade contextContract.
     * 
     * @return
     *     possible object is
     *     {@link ContextContract }
     *     
     */
    public ContextContract getContextContract() {
        return contextContract;
    }

    /**
     * Define o valor da propriedade contextContract.
     * 
     * @param value
     *     allowed object is
     *     {@link ContextContract }
     *     
     */
    public void setContextContract(ContextContract value) {
        this.contextContract = value;
    }

    /**
     * Gets the value of the port property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the port property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPort().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getPort() {
        if (port == null) {
            port = new ArrayList<String>();
        }
        return this.port;
    }

    /**
     * Gets the value of the files property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the files property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFiles().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UnitFileType }
     * 
     * 
     */
    public List<UnitFileType> getFiles() {
        if (files == null) {
            files = new ArrayList<UnitFileType>();
        }
        return this.files;
    }

    /**
     * Obtém o valor da propriedade session.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSession() {
        return session;
    }

    /**
     * Define o valor da propriedade session.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSession(Integer value) {
        this.session = value;
    }

    /**
     * Obtém o valor da propriedade networkAddress.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNetworkAddress() {
        return networkAddress;
    }

    /**
     * Define o valor da propriedade networkAddress.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNetworkAddress(String value) {
        this.networkAddress = value;
    }

}
