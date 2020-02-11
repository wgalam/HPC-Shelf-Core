//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.4.0-b180830.0438 
// Consulte <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2020.02.11 às 01:54:38 PM BRT 
//


package br.ufc.storm.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de calculated_parameter_type complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="calculated_parameter_type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="calculated_argument" type="{http://storm.lia.ufc.br}calculated_argument_type" minOccurs="0"/&gt;
 *         &lt;element name="cp_unit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="calc_id" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="kind_id" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "calculated_parameter_type", propOrder = {
    "calculatedArgument",
    "cpUnit"
})
public class CalculatedParameterType {

    @XmlElement(name = "calculated_argument")
    protected CalculatedArgumentType calculatedArgument;
    @XmlElement(name = "cp_unit")
    protected String cpUnit;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "calc_id")
    protected Integer calcId;
    @XmlAttribute(name = "kind_id")
    protected Integer kindId;

    /**
     * Obtém o valor da propriedade calculatedArgument.
     * 
     * @return
     *     possible object is
     *     {@link CalculatedArgumentType }
     *     
     */
    public CalculatedArgumentType getCalculatedArgument() {
        return calculatedArgument;
    }

    /**
     * Define o valor da propriedade calculatedArgument.
     * 
     * @param value
     *     allowed object is
     *     {@link CalculatedArgumentType }
     *     
     */
    public void setCalculatedArgument(CalculatedArgumentType value) {
        this.calculatedArgument = value;
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
     * Obtém o valor da propriedade calcId.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCalcId() {
        return calcId;
    }

    /**
     * Define o valor da propriedade calcId.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCalcId(Integer value) {
        this.calcId = value;
    }

    /**
     * Obtém o valor da propriedade kindId.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getKindId() {
        return kindId;
    }

    /**
     * Define o valor da propriedade kindId.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setKindId(Integer value) {
        this.kindId = value;
    }

}
