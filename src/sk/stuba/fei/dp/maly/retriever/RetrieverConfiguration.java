package sk.stuba.fei.dp.maly.retriever;

import org.apache.jena.ontology.Ontology;
import org.dllearner.core.AbstractKnowledgeSource;
import org.dllearner.core.KnowledgeSource;
import org.dllearner.kb.OWLAPIOntology;
import org.dllearner.kb.OWLOntologyKnowledgeSource;
import org.dllearner.reasoning.OWLAPIReasoner;
import org.dllearner.reasoning.ReasonerImplementation;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Objekt reprezentujúci možné konfigurácie komponenty instance retrievera. Pri používaní instance retrievera je možné konfigurovať:
 * <ul>
 *     <li>{@link RetrieverMode} - CWA / OWA</li>
 *     <li>{@link ReasonerImplementation} - Reasoner, ktorý sa má použiť pri dopytovaní dát</li>
 *     <li>{@link OWLOntology} - ontológia,ktorá sa má použiť</li>
 * </ul>
 *
 * @see RetrieverMode
 * @see ReasonerImplementation
 * @see OWLOntology
 *
 * @author Patrik Malý
 */
public class RetrieverConfiguration {

    private RetrieverMode mode;
    private ReasonerImplementation reasoner;
    private OWLOntology ontology;

    /**
     * Konštruktor pre konfiguráciu komponenty instance retrievera
     *
     * @param reasoner  Reasoner, ktorý sa má použiť pri dopytovaní dát
     * @param ontology ontológia,ktorá sa má použiť
     * @param mode  CWA / OWA
     *
     * @author Patrik Malý
     */
    public RetrieverConfiguration(ReasonerImplementation reasoner,OWLOntology ontology,RetrieverMode mode){
        this.mode = mode;
        this.ontology = ontology;
        this.reasoner = reasoner;
    }

    /**
     * Konštruktor, ktorého vstupným parametrom je ontológia, ktorá sa má načítať. Ostatné možnosti konfigurácie sú nastavené na základné hodnoty (defaults) :
     *
     * <ul>
     *     <li>{@link ReasonerImplementation} - <b>PELLET</b> </li>
     *     <li>{@link RetrieverMode} - <b>OWA</b></li>
     * </ul>
     *
     * @param ontology ontológia,ktorá sa má použiť

     * @author Patrik Malý
     */
    public RetrieverConfiguration(OWLOntology ontology){
        this.ontology=ontology;
        reasoner = ReasonerImplementation.PELLET;
        mode=RetrieverMode.OWA;
    }

    /**
     * Getter pre príslušný mód komponenty instance retriever
     * @return {@link RetrieverMode} enum
     * @author Patrik Malý
     */
    public RetrieverMode getMode() {
        return mode;
    }

    /**
     * Setter pre príslušný mód komponenty instance retriever
     * @author Patrik Malý
     */
    public void setMode(RetrieverMode mode) {
        this.mode = mode;
    }

    /**
     * Getter pre príslušnú implementáciu reasonera v komponente instance retriever
     * @return {@link ReasonerImplementation} enum
     * @author Patrik Malý
     */
    public ReasonerImplementation getReasoner() {
        return reasoner;
    }

    /**
     * Setter pre príslušný mód komponenty instance retriever
     * @author Patrik Malý
     */
    public void setReasoner(ReasonerImplementation reasoner) {
        this.reasoner = reasoner;
    }

    /**
     * Getter pre ontológiu použitú v komponente instance retriever
     * @return {@link OWLOntology}
     * @author Patrik Malý
     */
    public OWLOntology getOntology() {
        return ontology;
    }

    /**
     * Setter pre ontológiu použitú v komponente instance retriever
     * @author Patrik Malý
     */
    public void setOntology(OWLOntology ontology) {
        this.ontology = ontology;
    }

    /**
     * Statická metóda pre vytvorenie prednastavenej konfigurácie s parametrami :
     *
     *<ul>
     *     <li>{@link ReasonerImplementation} - <b>PELLET</b> </li>
     *     <li>{@link RetrieverMode} - <b>OWA</b></li>
     * </ul>
     * @param ontology {@link OWLOntology} , ontológia ktorá sa má použiť
     * @return {@link RetrieverConfiguration} základná konfigurácia instance retrievera
     * @author Patrik Malý
     */
    public static RetrieverConfiguration buildDefault(OWLOntology ontology){
        return new RetrieverConfiguration(ReasonerImplementation.PELLET,ontology,RetrieverMode.OWA);
    }
}
