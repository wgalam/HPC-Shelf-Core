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
 * <p>Classe Java de context_contract complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="context_contract"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="context_arguments" type="{http://storm.lia.ufc.br}context_argument_type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="abstract_component" type="{http://storm.lia.ufc.br}abstract_component_type" minOccurs="0"/&gt;
 *         &lt;element name="platform" type="{http://storm.lia.ufc.br}platform_profile_type" minOccurs="0"/&gt;
 *         &lt;element name="quality_arguments" type="{http://storm.lia.ufc.br}calculated_argument_type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="ranking_arguments" type="{http://storm.lia.ufc.br}calculated_argument_type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="cost_arguments" type="{http://storm.lia.ufc.br}calculated_argument_type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;choice&gt;
 *           &lt;element name="inner_components_resolved" type="{http://storm.lia.ufc.br}candidate_list_type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *           &lt;element name="inner_components" type="{http://storm.lia.ufc.br}context_contract" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;/choice&gt;
 *         &lt;element name="concrete_units" type="{http://storm.lia.ufc.br}concrete_unit_type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="cc_name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="cc_id" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="owner_id" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="kind_id" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "context_contract", propOrder = {
    "contextArguments",
    "abstractComponent",
    "platform",
    "qualityArguments",
    "rankingArguments",
    "costArguments",
    "innerComponentsResolved",
    "innerComponents",
    "concreteUnits"
})
public class ContextContract {

    @XmlElement(name = "context_arguments")
    protected List<ContextArgumentType> contextArguments;
    @XmlElement(name = "abstract_component")
    protected AbstractComponentType abstractComponent;
    protected PlatformProfileType platform;
    @XmlElement(name = "quality_arguments")
    protected List<CalculatedArgumentType> qualityArguments;
    @XmlElement(name = "ranking_arguments")
    protected List<CalculatedArgumentType> rankingArguments;
    @XmlElement(name = "cost_arguments")
    protected List<CalculatedArgumentType> costArguments;
    @XmlElement(name = "inner_components_resolved")
    protected List<CandidateListType> innerComponentsResolved;
    @XmlElement(name = "inner_components")
    protected List<ContextContract> innerComponents;
    @XmlElement(name = "concrete_units")
    protected List<ConcreteUnitType> concreteUnits;
    @XmlAttribute(name = "cc_name")
    protected String ccName;
    @XmlAttribute(name = "cc_id")
    protected Integer ccId;
    @XmlAttribute(name = "owner_id")
    protected Integer ownerId;
    @XmlAttribute(name = "kind_id")
    protected Integer kindId;

    /**
     * Gets the value of the contextArguments property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contextArguments property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContextArguments().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ContextArgumentType }
     * 
     * 
     */
    public List<ContextArgumentType> getContextArguments() {
        if (contextArguments == null) {
            contextArguments = new ArrayList<ContextArgumentType>();
        }
        return this.contextArguments;
    }

    /**
     * Obtém o valor da propriedade abstractComponent.
     * 
     * @return
     *     possible object is
     *     {@link AbstractComponentType }
     *     
     */
    public AbstractComponentType getAbstractComponent() {
        return abstractComponent;
    }

    /**
     * Define o valor da propriedade abstractComponent.
     * 
     * @param value
     *     allowed object is
     *     {@link AbstractComponentType }
     *     
     */
    public void setAbstractComponent(AbstractComponentType value) {
        this.abstractComponent = value;
    }

    /**
     * Obtém o valor da propriedade platform.
     * 
     * @return
     *     possible object is
     *     {@link PlatformProfileType }
     *     
     */
    public PlatformProfileType getPlatform() {
        return platform;
    }

    /**
     * Define o valor da propriedade platform.
     * 
     * @param value
     *     allowed object is
     *     {@link PlatformProfileType }
     *     
     */
    public void setPlatform(PlatformProfileType value) {
        this.platform = value;
    }

    /**
     * Gets the value of the qualityArguments property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the qualityArguments property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getQualityArguments().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CalculatedArgumentType }
     * 
     * 
     */
    public List<CalculatedArgumentType> getQualityArguments() {
        if (qualityArguments == null) {
            qualityArguments = new ArrayList<CalculatedArgumentType>();
        }
        return this.qualityArguments;
    }

    /**
     * Gets the value of the rankingArguments property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rankingArguments property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRankingArguments().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CalculatedArgumentType }
     * 
     * 
     */
    public List<CalculatedArgumentType> getRankingArguments() {
        if (rankingArguments == null) {
            rankingArguments = new ArrayList<CalculatedArgumentType>();
        }
        return this.rankingArguments;
    }

    /**
     * Gets the value of the costArguments property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the costArguments property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCostArguments().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CalculatedArgumentType }
     * 
     * 
     */
    public List<CalculatedArgumentType> getCostArguments() {
        if (costArguments == null) {
            costArguments = new ArrayList<CalculatedArgumentType>();
        }
        return this.costArguments;
    }

    /**
     * Gets the value of the innerComponentsResolved property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the innerComponentsResolved property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInnerComponentsResolved().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CandidateListType }
     * 
     * 
     */
    public List<CandidateListType> getInnerComponentsResolved() {
        if (innerComponentsResolved == null) {
            innerComponentsResolved = new ArrayList<CandidateListType>();
        }
        return this.innerComponentsResolved;
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
     * {@link ContextContract }
     * 
     * 
     */
    public List<ContextContract> getInnerComponents() {
        if (innerComponents == null) {
            innerComponents = new ArrayList<ContextContract>();
        }
        return this.innerComponents;
    }

    /**
     * Gets the value of the concreteUnits property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the concreteUnits property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConcreteUnits().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConcreteUnitType }
     * 
     * 
     */
    public List<ConcreteUnitType> getConcreteUnits() {
        if (concreteUnits == null) {
            concreteUnits = new ArrayList<ConcreteUnitType>();
        }
        return this.concreteUnits;
    }

    /**
     * Obtém o valor da propriedade ccName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCcName() {
        return ccName;
    }

    /**
     * Define o valor da propriedade ccName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCcName(String value) {
        this.ccName = value;
    }

    /**
     * Obtém o valor da propriedade ccId.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCcId() {
        return ccId;
    }

    /**
     * Define o valor da propriedade ccId.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCcId(Integer value) {
        this.ccId = value;
    }

    /**
     * Obtém o valor da propriedade ownerId.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOwnerId() {
        return ownerId;
    }

    /**
     * Define o valor da propriedade ownerId.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOwnerId(Integer value) {
        this.ownerId = value;
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
