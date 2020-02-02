//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.4.0-b180830.0438 
// Consulte <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2020.02.02 às 07:19:57 PM BRT 
//


package br.ufc.storm.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de concrete_component_type complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="concrete_component_type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="abstract_component" type="{http://storm.lia.ufc.br}abstract_component_type"/&gt;
 *         &lt;element name="context_contract" type="{http://storm.lia.ufc.br}context_contract"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "concrete_component_type", propOrder = {
    "abstractComponent",
    "contextContract"
})
public class ConcreteComponentType {

    @XmlElement(name = "abstract_component", required = true)
    protected AbstractComponentType abstractComponent;
    @XmlElement(name = "context_contract", required = true)
    protected ContextContract contextContract;

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

}
