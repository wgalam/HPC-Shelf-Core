//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2017.09.01 às 02:33:55 AM BRT 
//


package br.ufc.storm.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de slice_type complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="slice_type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="inner_unit_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="inner_component_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="slice_id" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="inner_component_id" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="inner_unit_id" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "slice_type", propOrder = {
    "innerUnitName",
    "innerComponentName"
})
public class SliceType {

    @XmlElement(name = "inner_unit_name")
    protected String innerUnitName;
    @XmlElement(name = "inner_component_name")
    protected String innerComponentName;
    @XmlAttribute(name = "slice_id")
    protected Integer sliceId;
    @XmlAttribute(name = "inner_component_id")
    protected Integer innerComponentId;
    @XmlAttribute(name = "inner_unit_id")
    protected Integer innerUnitId;

    /**
     * Obtém o valor da propriedade innerUnitName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInnerUnitName() {
        return innerUnitName;
    }

    /**
     * Define o valor da propriedade innerUnitName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInnerUnitName(String value) {
        this.innerUnitName = value;
    }

    /**
     * Obtém o valor da propriedade innerComponentName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInnerComponentName() {
        return innerComponentName;
    }

    /**
     * Define o valor da propriedade innerComponentName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInnerComponentName(String value) {
        this.innerComponentName = value;
    }

    /**
     * Obtém o valor da propriedade sliceId.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSliceId() {
        return sliceId;
    }

    /**
     * Define o valor da propriedade sliceId.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSliceId(Integer value) {
        this.sliceId = value;
    }

    /**
     * Obtém o valor da propriedade innerComponentId.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getInnerComponentId() {
        return innerComponentId;
    }

    /**
     * Define o valor da propriedade innerComponentId.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInnerComponentId(Integer value) {
        this.innerComponentId = value;
    }

    /**
     * Obtém o valor da propriedade innerUnitId.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getInnerUnitId() {
        return innerUnitId;
    }

    /**
     * Define o valor da propriedade innerUnitId.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInnerUnitId(Integer value) {
        this.innerUnitId = value;
    }

}
