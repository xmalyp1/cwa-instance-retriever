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
 * Created by Patrik on 03/01/2017.
 */

public class RetrieverConfiguration {

    private boolean cwaMode;
    private ReasonerImplementation reasoner;
    private OWLOntology ontology;

    public RetrieverConfiguration(ReasonerImplementation reasoner,OWLOntology ontology,boolean cwaMode){
        this.cwaMode = cwaMode;
        this.ontology = ontology;
        this.reasoner = reasoner;
    }

    public boolean isCwaMode() {
        return cwaMode;
    }

    public void setCwaMode(boolean cwaMode) {
        this.cwaMode = cwaMode;
    }

    public ReasonerImplementation getReasoner() {
        return reasoner;
    }

    public void setReasoner(ReasonerImplementation reasoner) {
        this.reasoner = reasoner;
    }

    public OWLOntology getOntology() {
        return ontology;
    }

    public void setOntology(OWLOntology ontology) {
        this.ontology = ontology;
    }

    public static RetrieverConfiguration buildDefault(OWLOntology ontology){
        return new RetrieverConfiguration(ReasonerImplementation.PELLET,ontology,false);
    }
}
