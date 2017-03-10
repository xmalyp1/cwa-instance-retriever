package sk.stuba.fei.dp.maly.retriever;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.dllearner.core.AbstractKnowledgeSource;
import org.dllearner.core.ComponentInitException;
import org.dllearner.core.KnowledgeSource;
import org.dllearner.kb.OWLAPIOntology;
import org.dllearner.kb.OWLOntologyKnowledgeSource;
import org.dllearner.reasoning.ClosedWorldReasoner;
import org.dllearner.reasoning.OWLAPIReasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import sk.stuba.fei.dp.maly.exceptions.InstanceRetrieverConfigException;
import sk.stuba.fei.dp.maly.concept.expression.DLQueryEngine;
import sk.stuba.fei.dp.maly.concept.expression.QueryResultPrinter;
import sk.stuba.fei.dp.maly.exceptions.ManchesterSyntaxParseException;
import sk.stuba.fei.dp.maly.model.dto.InstanceDTO;

public class InstanceRetriever {

    private OWLOntologyManager ontologyManager;
    private ClosedWorldReasoner cwaReasoner;

    public InstanceRetriever() {
        this.ontologyManager = OWLManager.createOWLOntologyManager();
    }

    public OWLOntology createOntology(File f) throws OWLException {
        ontologyManager = OWLManager.createOWLOntologyManager();
        return ontologyManager
                .loadOntologyFromOntologyDocument(f);
    }

    public void initializeReasoner(RetrieverConfiguration config) throws ComponentInitException {
        AbstractKnowledgeSource ks = new OWLAPIOntology(config.getOntology());
        ks.init();
        OWLAPIReasoner reasoner = new OWLAPIReasoner((KnowledgeSource) ks);
        reasoner.setReasonerImplementation(config.getReasoner());
        reasoner.setSources(((OWLOntologyKnowledgeSource) ks));

        // setup the reasoner
        reasoner.init();

        cwaReasoner = new ClosedWorldReasoner(reasoner);
        cwaReasoner.init();

    }

    public List<InstanceDTO> getIndividuals(String classExpression, RetrieverConfiguration config) throws ComponentInitException, ManchesterSyntaxParseException, InstanceRetrieverConfigException {
        //initializeReasoner(config);
        if (cwaReasoner == null || cwaReasoner.getSources() == null ||
                cwaReasoner.getSources().isEmpty() || cwaReasoner.getReasonerComponent() == null ||
                cwaReasoner.getReasonerComponent().getOntology() == null){
            throw new InstanceRetrieverConfigException("The retriever was not initialized.");
        }

        ShortFormProvider shortFormProvider = new SimpleShortFormProvider();
        // Create the QueryResultPrinter helper class. This will manage the
        // parsing of input and printing of results
        QueryResultPrinter queryResultPrinter = new QueryResultPrinter(new DLQueryEngine(config.getMode() == RetrieverMode.CWA ? cwaReasoner : cwaReasoner.getReasonerComponent(), cwaReasoner.getReasonerComponent().getOntology(),
                shortFormProvider), shortFormProvider);
        return queryResultPrinter.getIndividuals(classExpression.trim());
    }

    public OWLOntologyManager getOntologyManager() {
        return ontologyManager;
    }

    public void setOntologyManager(OWLOntologyManager ontologyManager) {
        this.ontologyManager = ontologyManager;
    }

    public ClosedWorldReasoner getCwaReasoner() {
        return cwaReasoner;
    }

    public void setCwaReasoner(ClosedWorldReasoner cwaReasoner) {
        this.cwaReasoner = cwaReasoner;
    }


    public Map<RetrieverMode, List<InstanceDTO>> getIndividualsInCompareMode(String classExpression, RetrieverConfiguration config) throws InstanceRetrieverConfigException, ManchesterSyntaxParseException {
        //initializeReasoner(config);
        if (cwaReasoner == null || cwaReasoner.getSources() == null ||
            cwaReasoner.getSources().isEmpty() || cwaReasoner.getReasonerComponent() == null ||
            cwaReasoner.getReasonerComponent().getOntology() == null){
                throw new InstanceRetrieverConfigException("The retriever was not initialized.");
        }
        Map<RetrieverMode, List<InstanceDTO>> result = new HashMap<RetrieverMode, List<InstanceDTO>>();

        ShortFormProvider shortFormProvider = new SimpleShortFormProvider();

        QueryResultPrinter owaResult = new QueryResultPrinter(new DLQueryEngine(cwaReasoner.getReasonerComponent(), cwaReasoner.getReasonerComponent().getOntology(),
                shortFormProvider), shortFormProvider);
        result.put(RetrieverMode.OWA,owaResult.getIndividuals(classExpression));

        QueryResultPrinter cwaResult = new QueryResultPrinter(new DLQueryEngine(cwaReasoner, cwaReasoner.getReasonerComponent().getOntology(),
                shortFormProvider), shortFormProvider);
        result.put(RetrieverMode.CWA,cwaResult.getIndividuals(classExpression));

        return result;
    }

}
