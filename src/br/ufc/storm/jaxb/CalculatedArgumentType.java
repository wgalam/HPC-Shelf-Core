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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de calculated_argument_type complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="calculated_argument_type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="function" type="{http://storm.lia.ufc.br}calculated_function_type" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="cp_id" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
 *       &lt;attribute name="kind_id" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "calculated_argument_type", propOrder = {
    "function"
})
public class CalculatedArgumentType {

    protected CalculatedFunctionType function;
    @XmlAttribute(name = "cp_id")
    protected Integer cpId;
    @XmlAttribute(name = "value")
    protected Double value;
    @XmlAttribute(name = "kind_id")
    protected Integer kindId;

    /**
     * Obtém o valor da propriedade function.
     * 
     * @return
     *     possible object is
     *     {@link CalculatedFunctionType }
     *     
     */
    public CalculatedFunctionType getFunction() {
        return function;
    }

    /**
     * Define o valor da propriedade function.
     * 
     * @param value
     *     allowed object is
     *     {@link CalculatedFunctionType }
     *     
     */
    public void setFunction(CalculatedFunctionType value) {
        this.function = value;
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
     * Obtém o valor da propriedade value.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getValue() {
        return value;
    }

    /**
     * Define o valor da propriedade value.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setValue(Double value) {
        this.value = value;
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
