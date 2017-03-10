package sk.stuba.fei.dp.maly.concept.expression;

import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;

import org.dllearner.core.Reasoner;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.util.ShortFormProvider;
import sk.stuba.fei.dp.maly.exceptions.ManchesterSyntaxParseException;


public class DLQueryEngine {

    private OWLClassExpression concept;
    private Reasoner reasoner;
    private final ManchesterSyntaxQueryParser parser;

    public DLQueryEngine(Reasoner reasoner, OWLOntology ontology, ShortFormProvider shortFormProvider) {
        this.reasoner = reasoner;
        parser = new ManchesterSyntaxQueryParser(ontology, shortFormProvider);
    }

    public OWLClassExpression getParsedConcept(){
        return concept;
    }

    public void setReasoner(Reasoner reasoner){
        this.reasoner = reasoner;
    }
    public Set<OWLClassExpression> getSuperClasses(String classExpressionString) throws ManchesterSyntaxParseException {
        if (classExpressionString.trim().length() == 0) {
            return Collections.emptySet();
        }
        concept = parser
                .parseClassExpression(classExpressionString);
        SortedSet<OWLClassExpression> superClasses = reasoner
                .getSuperClasses(concept);
        return superClasses;
    }


    public Set<OWLClassExpression> getSubClasses(String classExpressionString) throws ManchesterSyntaxParseException {
        if (classExpressionString.trim().length() == 0) {
            return Collections.emptySet();
        }
        concept = parser
                .parseClassExpression(classExpressionString);
        SortedSet<OWLClassExpression> subClasses = reasoner.getSubClasses(concept);
        return subClasses;
    }

    public Set<OWLIndividual> getInstances(String classExpressionString) throws ManchesterSyntaxParseException {
        if (classExpressionString.trim().length() == 0) {
            return Collections.emptySet();
        }
        concept = parser
                .parseClassExpression(classExpressionString);
        SortedSet<OWLIndividual> individuals = reasoner.getIndividuals(concept);
        return individuals;
    }

    public Set<OWLIndividual> getInstances(OWLClassExpression manchesterExpression) {
        SortedSet<OWLIndividual> individuals = reasoner.getIndividuals(manchesterExpression);
        return individuals;
    }

    public Set<OWLClass> getClassOfIndividual(OWLIndividual individual) {
        return reasoner.getTypes(individual);
    }
}
