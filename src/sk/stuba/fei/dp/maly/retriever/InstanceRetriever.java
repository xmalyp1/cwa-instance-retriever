package sk.stuba.fei.dp.maly.retriever;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

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
	private OWLReasoner reasoner;
	
	public InstanceRetriever(){
		this.ontologyManager = OWLManager.createOWLOntologyManager();
	}

	/*
	 public static void main(String[] args) throws Exception {
	        // Load an example ontology.
	        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	        File f = new File("pizza.owl");
	        if(!f.exists())
	        	f.createNewFile();
	        OWLOntology ontology = manager
	                .loadOntologyFromOntologyDocument(new File("pizza.owl"));
	        // We need a reasoner to do our query answering


	        // These two lines are the only relevant difference between this code and the original example
	        // This example uses HermiT: http://hermit-reasoner.com/
	        OWLReasoner reasoner = new Reasoner.ReasonerFactory().createReasoner(ontology);



	        ShortFormProvider shortFormProvider = new SimpleShortFormProvider();
	        // Create the DLQueryPrinter helper class. This will manage the
	        // parsing of input and printing of results
	        DLQueryPrinter dlQueryPrinter = new DLQueryPrinter(new DLQueryEngine(reasoner,
	                shortFormProvider), shortFormProvider);
	        // Enter the query loop. A user is expected to enter class
	        // expression on the command line.
	        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
	        while (true) {
	            System.out
	                    .println("Type a class expression in Manchester Syntax and press Enter (or press x to exit):");
	            String classExpression = br.readLine();
	            // Check for exit condition
	            if (classExpression == null || classExpression.equalsIgnoreCase("x")) {
	                break;
	            }
	            dlQueryPrinter.askQuery(classExpression.trim());
	            System.out.println();
	            }
	        }
	 
	 */
	 public void createOntology(File f) throws OWLException{
		 ontologyManager = OWLManager.createOWLOntologyManager();
		 ontology = ontologyManager
	                .loadOntologyFromOntologyDocument(f);
	 }
	 
	 public void initializeReasoner(){
		 this.reasoner = new Reasoner.ReasonerFactory().createReasoner(ontology);
	 }
	
	 public List<IndividualsDatatableModel> getIndividuals(String classExpression){
	        ShortFormProvider shortFormProvider = new SimpleShortFormProvider();
	        // Create the DLQueryPrinter helper class. This will manage the
	        // parsing of input and printing of results
	        DLQueryPrinter dlQueryPrinter = new DLQueryPrinter(new DLQueryEngine(reasoner,
	                shortFormProvider), shortFormProvider);	
	       return dlQueryPrinter.getIndividuals(classExpression.trim());
	 }

	public OWLOntology getOntology() {
		return ontology;
	}

	public void setOntology(OWLOntology ontology) {
		this.ontology = ontology;
	}

	public OWLReasoner getReasoner() {
		return reasoner;
	}

	public void setReasoner(OWLReasoner reasoner) {
		this.reasoner = reasoner;
	}
	 
	 
}
