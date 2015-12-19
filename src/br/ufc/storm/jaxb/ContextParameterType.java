//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2015.12.16 às 03:13:02 PM BRT 
//


package br.ufc.storm.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de context_parameter_type complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="context_parameter_type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bound" type="{http://storm.lia.ufc.br}context_contract" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element name="context_variable" type="{http://storm.lia.ufc.br}context_contract" minOccurs="0"/>
 *           &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;/choice>
 *         &lt;element name="context_argument" type="{http://storm.lia.ufc.br}context_argument_type" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="cp_id" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "context_parameter_type", propOrder = {
    "bound",
    "contextVariable",
    "value",
    "contextArgument"
})
public class ContextParameterType {

    protected ContextContract bound;
    @XmlElement(name = "context_variable")
    protected ContextContract contextVariable;
    protected String value;
    @XmlElement(name = "context_argument")
    protected ContextArgumentType contextArgument;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "cp_id")
    protected Integer cpId;

    /**
     * Obtém o valor da propriedade bound.
     * 
     * @return
     *     possible object is
     *     {@link ContextContract }
     *     
     */
    public ContextContract getBound() {
        return bound;
    }

    /**
     * Define o valor da propriedade bound.
     * 
     * @param value
     *     allowed object is
     *     {@link ContextContract }
     *     
     */
    public void setBound(ContextContract value) {
        this.bound = value;
    }

    /**
     * Obtém o valor da propriedade contextVariable.
     * 
     * @return
     *     possible object is
     *     {@link ContextContract }
     *     
     */
    public ContextContract getContextVariable() {
        return contextVariable;
    }

    /**
     * Define o valor da propriedade contextVariable.
     * 
     * @param value
     *     allowed object is
     *     {@link ContextContract }
     *     
     */
    public void setContextVariable(ContextContract value) {
        this.contextVariable = value;
    }

    /**
     * Obtém o valor da propriedade value.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Define o valor da propriedade value.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Obtém o valor da propriedade contextArgument.
     * 
     * @return
     *     possible object is
     *     {@link ContextArgumentType }
     *     
     */
    public ContextArgumentType getContextArgument() {
        return contextArgument;
    }

    /**
     * Define o valor da propriedade contextArgument.
     * 
     * @param value
     *     allowed object is
     *     {@link ContextArgumentType }
     *     
     */
    public void setContextArgument(ContextArgumentType value) {
        this.contextArgument = value;
    }

    /**
     * Obtém o valor da propriedade name.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Define o valor da propriedade name.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Obtém o valor da propriedade cpId.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCpId() {
        return cpId;
    }

    /**
     * Define o valor da propriedade cpId.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCpId(Integer value) {
        this.cpId = value;
    }

}
