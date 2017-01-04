package sk.stuba.fei.dp.maly.retriever;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

import org.dllearner.core.AbstractKnowledgeSource;
import org.dllearner.core.ComponentInitException;
import org.dllearner.core.KnowledgeSource;
import org.dllearner.kb.OWLAPIOntology;
import org.dllearner.kb.OWLOntologyKnowledgeSource;
import org.dllearner.reasoning.ClosedWorldReasoner;
import org.dllearner.reasoning.OWLAPIReasoner;
import org.dllearner.reasoning.ReasonerImplementation;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import sk.stuba.fei.dp.maly.queries.DLQueryEngine;
import sk.stuba.fei.dp.maly.queries.DLQueryPrinter;
import sk.stuba.fei.dp.maly.ui.models.IndividualsDatatableModel;

public class InstanceRetriever {
	
	private OWLOntologyManager ontologyManager;
	private OWLOntology ontology;
	private ClosedWorldReasoner cwaReasoner;
	
	public InstanceRetriever(){
		this.ontologyManager = OWLManager.createOWLOntologyManager();
	}

	public void createOntology(File f) throws OWLException{
		 ontologyManager = OWLManager.createOWLOntologyManager();
		 ontology = ontologyManager
	                .loadOntologyFromOntologyDocument(f);
	 }
	 
	 private void initializeReasoner(RetrieverConfiguration config) throws ComponentInitException{
			AbstractKnowledgeSource ks = new OWLAPIOntology(config.getOntology());
			ks.init();
			OWLAPIReasoner reasoner= new OWLAPIReasoner((KnowledgeSource)ks);
		 	reasoner.setReasonerImplementation(config.getReasoner());
		 	reasoner.setSources(((OWLOntologyKnowledgeSource) ks));
			
			// setup the reasoner
		 	reasoner.init();
			cwaReasoner = new ClosedWorldReasoner(reasoner);
			cwaReasoner.init();
	}
	
	 public List<IndividualsDatatableModel> getIndividuals(String classExpression,RetrieverConfiguration config) throws ComponentInitException {
	     	initializeReasoner(config);

		 	ShortFormProvider shortFormProvider = new SimpleShortFormProvider();
	        // Create the DLQueryPrinter helper class. This will manage the
	        // parsing of input and printing of results
	        DLQueryPrinter dlQueryPrinter = new DLQueryPrinter(new DLQueryEngine(config.isCwaMode() ? cwaReasoner : cwaReasoner.getReasonerComponent(),cwaReasoner.getReasonerComponent().getOntology(),
	                shortFormProvider), shortFormProvider);	
	       return dlQueryPrinter.getIndividuals(classExpression.trim());
	 }

	public OWLOntology getOntology() {
		return ontology;
	}

	public void setOntology(OWLOntology ontology) {
		this.ontology = ontology;
	}

	public ClosedWorldReasoner getCwaReasoner() {
		return cwaReasoner;
	}

	public void setCwaReasoner(ClosedWorldReasoner cwaReasoner) {
		this.cwaReasoner = cwaReasoner;
	}


	 
	 
}
