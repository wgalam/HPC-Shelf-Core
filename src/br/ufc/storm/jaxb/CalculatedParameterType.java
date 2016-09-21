//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2016.09.17 às 02:29:59 PM BRT 
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
 * &lt;complexType name="calculated_parameter_type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="calculated_argument" type="{http://storm.lia.ufc.br}calculated_argument_type" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="calc_id" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="kind_id" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "calculated_parameter_type", propOrder = {
    "calculatedArgument"
})
public class CalculatedParameterType {

    @XmlElement(name = "calculated_argument")
    protected CalculatedArgumentType calculatedArgument;
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
