package sk.stuba.fei.dp.maly.queries;

import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.jena.ontology.Ontology;
import org.dllearner.core.Reasoner;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.ShortFormProvider;


public class DLQueryEngine {
    private final Reasoner reasoner;
    private final DLQueryParser parser;

    public DLQueryEngine(Reasoner reasoner, OWLOntology ontology ,ShortFormProvider shortFormProvider) {
        this.reasoner = reasoner;
        parser = new DLQueryParser(ontology, shortFormProvider);
    }

    public Set<OWLClassExpression> getSuperClasses(String classExpressionString) {
        if (classExpressionString.trim().length() == 0) {
            return Collections.emptySet();
        }
        OWLClassExpression classExpression = parser
                .parseClassExpression(classExpressionString);
        SortedSet<OWLClassExpression> superClasses = reasoner
                .getSuperClasses(classExpression);
        return superClasses;
    }


    public Set<OWLClassExpression> getSubClasses(String classExpressionString) {
        if (classExpressionString.trim().length() == 0) {
            return Collections.emptySet();
        }
        OWLClassExpression classExpression = parser
                .parseClassExpression(classExpressionString);
        SortedSet<OWLClassExpression> subClasses = reasoner.getSubClasses(classExpression);
        return subClasses;
        }

    public Set<OWLIndividual> getInstances(String classExpressionString) {
        if (classExpressionString.trim().length() == 0) {
            return Collections.emptySet();
        }
        OWLClassExpression classExpression = parser
                .parseClassExpression(classExpressionString);
        SortedSet<OWLIndividual> individuals = reasoner.getIndividuals(classExpression);
        return individuals;
        }
    
    public Set<OWLClass> getClassOfIndividual(OWLIndividual individual){
    	return reasoner.getTypes(individual);
    }
    }
