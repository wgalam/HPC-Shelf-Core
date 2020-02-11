//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.4.0-b180830.0438 
// Consulte <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2020.02.11 às 01:54:38 PM BRT 
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
 * <p>Classe Java de abstract_component_type complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="abstract_component_type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="supertype" type="{http://storm.lia.ufc.br}abstract_component_type" minOccurs="0"/&gt;
 *         &lt;element name="context_parameter" type="{http://storm.lia.ufc.br}context_parameter_type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="inner_components" type="{http://storm.lia.ufc.br}abstract_component_type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="abstract_unit" type="{http://storm.lia.ufc.br}abstract_unit_type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="quality_parameters" type="{http://storm.lia.ufc.br}calculated_parameter_type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="cost_parameters" type="{http://storm.lia.ufc.br}calculated_parameter_type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="ranking_parameters" type="{http://storm.lia.ufc.br}calculated_parameter_type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="slices" type="{http://storm.lia.ufc.br}slice_type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="calculated_parameters" type="{http://storm.lia.ufc.br}calculated_parameter_type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="kind" type="{http://storm.lia.ufc.br}kind_type" /&gt;
 *       &lt;attribute name="id_ac" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="path" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstract_component_type", propOrder = {
    "supertype",
    "contextParameter",
    "innerComponents",
    "abstractUnit",
    "qualityParameters",
    "costParameters",
    "rankingParameters",
    "slices",
    "calculatedParameters"
})
public class AbstractComponentType {

    protected AbstractComponentType supertype;
    @XmlElement(name = "context_parameter")
    protected List<ContextParameterType> contextParameter;
    @XmlElement(name = "inner_components")
    protected List<AbstractComponentType> innerComponents;
    @XmlElement(name = "abstract_unit")
    protected List<AbstractUnitType> abstractUnit;
    @XmlElement(name = "quality_parameters")
    protected List<CalculatedParameterType> qualityParameters;
    @XmlElement(name = "cost_parameters")
    protected List<CalculatedParameterType> costParameters;
    @XmlElement(name = "ranking_parameters")
    protected List<CalculatedParameterType> rankingParameters;
    protected List<SliceType> slices;
    @XmlElement(name = "calculated_parameters")
    protected List<CalculatedParameterType> calculatedParameters;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "kind")
    protected Integer kind;
    @XmlAttribute(name = "id_ac")
    protected Integer idAc;
    @XmlAttribute(name = "path")
    protected String path;

    /**
     * Obtém o valor da propriedade supertype.
     * 
     * @return
     *     possible object is
     *     {@link AbstractComponentType }
     *     
     */
    public AbstractComponentType getSupertype() {
        return supertype;
    }

    /**
     * Define o valor da propriedade supertype.
     * 
     * @param value
     *     allowed object is
     *     {@link AbstractComponentType }
     *     
     */
    public void setSupertype(AbstractComponentType value) {
        this.supertype = value;
    }

    /**
     * Gets the value of the contextParameter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contextParameter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContextParameter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ContextParameterType }
     * 
     * 
     */
    public List<ContextParameterType> getContextParameter() {
        if (contextParameter == null) {
            contextParameter = new ArrayList<ContextParameterType>();
        }
        return this.contextParameter;
    }

    /**
     * Gets the value of the innerComponents property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the innerComponents property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInnerComponents().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AbstractComponentType }
     * 
     * 
     */
    public List<AbstractComponentType> getInnerComponents() {
        if (innerComponents == null) {
            innerComponents = new ArrayList<AbstractComponentType>();
        }
        return this.innerComponents;
    }

    /**
     * Gets the value of the abstractUnit property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the abstractUnit property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAbstractUnit().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AbstractUnitType }
     * 
     * 
     */
    public List<AbstractUnitType> getAbstractUnit() {
        if (abstractUnit == null) {
            abstractUnit = new ArrayList<AbstractUnitType>();
        }
        return this.abstractUnit;
    }

    /**
     * Gets the value of the qualityParameters property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the qualityParameters property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getQualityParameters().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CalculatedParameterType }
     * 
     * 
     */
    public List<CalculatedParameterType> getQualityParameters() {
        if (qualityParameters == null) {
            qualityParameters = new ArrayList<CalculatedParameterType>();
        }
        return this.qualityParameters;
    }

    /**
     * Gets the value of the costParameters property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the costParameters property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCostParameters().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CalculatedParameterType }
     * 
     * 
     */
    public List<CalculatedParameterType> getCostParameters() {
        if (costParameters == null) {
            costParameters = new ArrayList<CalculatedParameterType>();
        }
        return this.costParameters;
    }

    /**
     * Gets the value of the rankingParameters property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rankingParameters property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRankingParameters().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CalculatedParameterType }
     * 
     * 
     */
    public List<CalculatedParameterType> getRankingParameters() {
        if (rankingParameters == null) {
            rankingParameters = new ArrayList<CalculatedParameterType>();
        }
        return this.rankingParameters;
    }

    /**
     * Gets the value of the slices property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the slices property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSlices().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SliceType }
     * 
     * 
     */
    public List<SliceType> getSlices() {
        if (slices == null) {
            slices = new ArrayList<SliceType>();
        }
        return this.slices;
    }

    /**
     * Gets the value of the calculatedParameters property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the calculatedParameters property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCalculatedParameters().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CalculatedParameterType }
     * 
     * 
     */
    public List<CalculatedParameterType> getCalculatedParameters() {
        if (calculatedParameters == null) {
            calculatedParameters = new ArrayList<CalculatedParameterType>();
        }
        return this.calculatedParameters;
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
     * Obtém o valor da propriedade idAc.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIdAc() {
        return idAc;
    }

    /**
     * Define o valor da propriedade idAc.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIdAc(Integer value) {
        this.idAc = value;
    }

    /**
     * Obtém o valor da propriedade path.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPath() {
        return path;
    }

    /**
     * Define o valor da propriedade path.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPath(String value) {
        this.path = value;
    }

}
