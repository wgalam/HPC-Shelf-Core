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
 * <p>Classe Java de context_parameter_type complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="context_parameter_type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="bound" type="{http://storm.lia.ufc.br}context_contract" minOccurs="0"/&gt;
 *         &lt;element name="bound_value" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;choice&gt;
 *           &lt;element name="context_variable_required" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;/choice&gt;
 *         &lt;element name="context_argument" type="{http://storm.lia.ufc.br}context_argument_type" minOccurs="0"/&gt;
 *         &lt;element name="context_variable_provided" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="context_variable_required_id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="numeric_domain" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="cp_unit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="cp_id" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="kind" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="variance" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "context_parameter_type", propOrder = {
    "bound",
    "boundValue",
    "contextVariableRequired",
    "contextArgument",
    "contextVariableProvided",
    "contextVariableRequiredId",
    "numericDomain",
    "cpUnit"
})
public class ContextParameterType {

    protected ContextContract bound;
    @XmlElement(name = "bound_value", required = true)
    protected String boundValue;
    @XmlElement(name = "context_variable_required")
    protected String contextVariableRequired;
    @XmlElement(name = "context_argument")
    protected ContextArgumentType contextArgument;
    @XmlElement(name = "context_variable_provided")
    protected String contextVariableProvided;
    @XmlElement(name = "context_variable_required_id")
    protected Integer contextVariableRequiredId;
    @XmlElement(name = "numeric_domain", required = true)
    protected String numericDomain;
    @XmlElement(name = "cp_unit")
    protected String cpUnit;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "cp_id")
    protected Integer cpId;
    @XmlAttribute(name = "kind")
    protected Integer kind;
    @XmlAttribute(name = "variance")
    protected Integer variance;

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
     * Obtém o valor da propriedade boundValue.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBoundValue() {
        return boundValue;
    }

    /**
     * Define o valor da propriedade boundValue.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBoundValue(String value) {
        this.boundValue = value;
    }

    /**
     * Obtém o valor da propriedade contextVariableRequired.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContextVariableRequired() {
        return contextVariableRequired;
    }

    /**
     * Define o valor da propriedade contextVariableRequired.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContextVariableRequired(String value) {
        this.contextVariableRequired = value;
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
     * Obtém o valor da propriedade contextVariableProvided.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContextVariableProvided() {
        return contextVariableProvided;
    }

    /**
     * Define o valor da propriedade contextVariableProvided.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContextVariableProvided(String value) {
        this.contextVariableProvided = value;
    }

    /**
     * Obtém o valor da propriedade contextVariableRequiredId.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getContextVariableRequiredId() {
        return contextVariableRequiredId;
    }

    /**
     * Define o valor da propriedade contextVariableRequiredId.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setContextVariableRequiredId(Integer value) {
        this.contextVariableRequiredId = value;
    }

    /**
     * Obtém o valor da propriedade numericDomain.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumericDomain() {
        return numericDomain;
    }

    /**
     * Define o valor da propriedade numericDomain.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumericDomain(String value) {
        this.numericDomain = value;
    }

    /**
     * Obtém o valor da propriedade cpUnit.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCpUnit() {
        return cpUnit;
    }

    /**
     * Define o valor da propriedade cpUnit.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCpUnit(String value) {
        this.cpUnit = value;
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

    /**
     * Obtém o valor da propriedade kind.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getKind() {
        return kind;
    }

    /**
     * Define o valor da propriedade kind.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setKind(Integer value) {
        this.kind = value;
    }

    /**
     * Obtém o valor da propriedade variance.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getVariance() {
        return variance;
    }

    /**
     * Define o valor da propriedade variance.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setVariance(Integer value) {
        this.variance = value;
    }

}
