package sk.stuba.fei.dp.maly.retriever;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dllearner.core.AbstractKnowledgeSource;
import org.dllearner.core.ComponentInitException;
import org.dllearner.core.KnowledgeSource;
import org.dllearner.kb.OWLAPIOntology;
import org.dllearner.kb.OWLOntologyKnowledgeSource;
import org.dllearner.reasoning.ClosedWorldReasoner;
import org.dllearner.reasoning.OWLAPIReasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import sk.stuba.fei.dp.maly.concept.expression.OWLIndividualConverter;
import sk.stuba.fei.dp.maly.exceptions.InstanceRetrieverConfigException;
import sk.stuba.fei.dp.maly.concept.expression.DLQueryEngine;
import sk.stuba.fei.dp.maly.exceptions.ManchesterSyntaxParseException;
import sk.stuba.fei.dp.maly.model.dto.InstanceDTO;

/**
 * Objekt {@code InstanceRetriever} poskytuje funkcionality komponenty pre získavanie inštancií.
 * Poskytuje metódy, ktoré sú nevyhnutné pre správne fungovanie mechanizmu na ziskávanie inštancií.
 *
 * @see OWLOntologyManager
 * @see ClosedWorldReasoner
 * @see RetrieverConfiguration
 *
 * @author Patrik Malý
 */
public class InstanceRetriever {

    private OWLOntologyManager ontologyManager;
    private ClosedWorldReasoner cwaReasoner;
    private RetrieverConfiguration config;

    /**
     * Bezparametrový konštruktor objektu Instance retriever, ktorej úloha je
     * inicializácia objektu {@link OWLOntologyManager}
     *
     * @author Patrik Malý
     */
    public InstanceRetriever() {
        this.ontologyManager = OWLManager.createOWLOntologyManager();
    }

    /**
     * Metóda pre načítanie ontológie zo vstupného súboru a vytvorenie objektu {@link OWLOntology}
     *
     * @author Patrik Malý
     * @param file predstavuje vstupný súbor, obsahujúci ontológiu v nešpecifikovanom formáte (RDF/XML , turtle , Manchester, OWL)
     * @return {@code OWLOntology} objekt reprezentujúci načítanú ontológiu
     * @throws OWLException ak sa nepodarí načítať súbor.
     */
    public OWLOntology createOntology(File file) throws OWLException {
        ontologyManager = OWLManager.createOWLOntologyManager();
        return ontologyManager.loadOntologyFromOntologyDocument(file);
    }

    /**
     * Metóda pre inicializáciu retrievera. Metóda načíta poskytnutú ontológiu , inicializuje reasoner a aplikuje príslušný mód (CWA / OWA)
     *
     * @author Patrik Malý
     * @param config {@link RetrieverConfiguration} predstavuje konfiguráciu komponenty instance retriever (CWA/OWA mód, typ reasonera, ontológia)
     * @throws ComponentInitException v prípade, že sa nepodarí inicializovať reasoner potrebný na získavanie inŠtancií.
     */
    public void initializeRetriever(RetrieverConfiguration config) throws ComponentInitException, InstanceRetrieverConfigException {
        if(config == null){
            throw new InstanceRetrieverConfigException("The retriever was not initialized.");
        }
        this.config = config;
        AbstractKnowledgeSource ks = new OWLAPIOntology(this.config.getOntology());
        ks.init();
        OWLAPIReasoner reasoner = new OWLAPIReasoner((KnowledgeSource) ks);
        reasoner.setReasonerImplementation(this.config.getReasoner());
        reasoner.setSources(((OWLOntologyKnowledgeSource) ks));

        // setup the reasoner
        reasoner.init();

        cwaReasoner = new ClosedWorldReasoner(reasoner);
        cwaReasoner.init();

    }

    /**
     * Metóda, ktorá získa inštancie, ktoré vyhovujú vstupnému parametru {@code classExpression}
     * @author Patrik Malý
     * @param classExpression vstupný konceptuálny výraz vo formáte Manchester
     * @return Kolekcia inštancií, ktoré vyhovujú vstupnému parametru {@code classExpression} v danej ontológii
     * @throws ComponentInitException v prípade, že sa nepodarí načítať vybraný reasoner
     * @throws ManchesterSyntaxParseException v prípade, že sa nepodarí rozparsovať syntax Manchester
     * @throws InstanceRetrieverConfigException v prípade, že konfigurácia nie je úplná
     */
    public List<InstanceDTO> getIndividuals(String classExpression) throws ComponentInitException, ManchesterSyntaxParseException, InstanceRetrieverConfigException {
        //initializeReasoner(config);
        if (cwaReasoner == null || cwaReasoner.getSources() == null ||
                cwaReasoner.getSources().isEmpty() || cwaReasoner.getReasonerComponent() == null ||
                cwaReasoner.getReasonerComponent().getOntology() == null){
            throw new InstanceRetrieverConfigException("The retriever was not initialized.");
        }

        ShortFormProvider shortFormProvider = new SimpleShortFormProvider();
        // Create the OWLIndividualConverter helper class. This will manage the
        // parsing of input and printing of results
        OWLIndividualConverter converter = new OWLIndividualConverter(shortFormProvider);
        DLQueryEngine engine = new DLQueryEngine(config.getMode() == RetrieverMode.CWA ? cwaReasoner : cwaReasoner.getReasonerComponent(), cwaReasoner.getReasonerComponent().getOntology(),
                shortFormProvider);
        return converter.formatOWLIndividuals(engine.getInstances(classExpression),engine);
    }

    /**
     * Metóda pre získavanie {@link OWLOntologyManager} použítý v danej komponente
     * @author Patrik Malý
     * @return objekt {@code OWLOntologyManager}, ktorý je použítý komponentom Instance Retriever
     */
    public OWLOntologyManager getOntologyManager() {
        return ontologyManager;
    }

    /**
     * Metóda pre ziskanie použítého reasonera
     * @author Patrik Malý
     * @return {@link ClosedWorldReasoner} ,ktorý je použitý v komponente
     */
    public ClosedWorldReasoner getCwaReasoner() {
        return cwaReasoner;
    }

    /**
     * Metóda na nastavenie reasonera, odporúča sa však použiť metódu
     * {@link sk.stuba.fei.dp.maly.retriever.InstanceRetriever#initializeRetriever}
     * @author Patrik Malý
     */
    public void setCwaReasoner(ClosedWorldReasoner cwaReasoner) {
        this.cwaReasoner = cwaReasoner;
    }


    /**
     * Metóda na získavanie inštancií v oboch módoch (CWA / OWA), ktoré vyhovujú vstupnému parametru {@code classExpression}
     *
     * @author Patrik Malý
     * @param classExpression vstupný konceptuálny výraz vo formáte Manchester
     * @return Mapa,ktorá obsahuje dvojice, kde kľučom je mód (CWA,OWA) a hodnotou je kolekcia inštancií
     * @throws InstanceRetrieverConfigException
     * @throws ManchesterSyntaxParseException
     */
    public Map<RetrieverMode, List<InstanceDTO>> getIndividualsInCompareMode(String classExpression) throws InstanceRetrieverConfigException, ManchesterSyntaxParseException {
        //initializeReasoner(config);
        if (cwaReasoner == null || cwaReasoner.getSources() == null ||
            cwaReasoner.getSources().isEmpty() || cwaReasoner.getReasonerComponent() == null ||
            cwaReasoner.getReasonerComponent().getOntology() == null){
                throw new InstanceRetrieverConfigException("The retriever was not initialized.");
        }
        Map<RetrieverMode, List<InstanceDTO>> result = new HashMap<RetrieverMode, List<InstanceDTO>>();

        ShortFormProvider shortFormProvider = new SimpleShortFormProvider();
        OWLIndividualConverter converter = new OWLIndividualConverter(shortFormProvider);
        DLQueryEngine owaEngine = new DLQueryEngine(cwaReasoner.getReasonerComponent(), cwaReasoner.getReasonerComponent().getOntology(),
                shortFormProvider);
        result.put(RetrieverMode.OWA,converter.formatOWLIndividuals(owaEngine.getInstances(classExpression),owaEngine));

        DLQueryEngine cwaEngine = new DLQueryEngine(cwaReasoner, cwaReasoner.getReasonerComponent().getOntology(),
                shortFormProvider);
        result.put(RetrieverMode.CWA,converter.formatOWLIndividuals(cwaEngine.getInstances(classExpression),cwaEngine));

        return result;
    }


}
