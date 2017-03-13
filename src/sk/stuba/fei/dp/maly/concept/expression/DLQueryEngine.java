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


/**
 * Predstavuje objekt, ktorý obsahuje celkovú logiku funkcionality instance retrievera.
 * @author Patrik Malý
 */
public class DLQueryEngine {

    private OWLClassExpression concept;
    private Reasoner reasoner;
    private final ManchesterSyntaxQueryParser parser;

    /**
     * Konštruktor daného objektu.
     *
     * @see Reasoner
     * @see OWLOntology
     * @see ShortFormProvider
     *
     * @param reasoner použity reasoner
     * @param ontology načítaná ontológia
     * @param shortFormProvider objekt, ktorý zodpovedá za zobrazovanie OWL entít
     *
     */
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

    /**
     * Metóda pre získanie tried, ktoré vyhovujú vstupnému konceptuálnemu výrazu.
     * @param classExpressionString vstupný konceptuálny výraz
     * @return Množinu tried, ktorú reprezentujú objekty {@link OWLClassExpression}
     * @throws ManchesterSyntaxParseException v prípade, že sa nepodarilo rozparsovat vstupný reťazec
     */
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


    /**
     * Metóda, ktorá vráti množinu podtried, ktoré vyhovujú vstupnému konceptuálnemu výrazu
     * @param classExpressionString vstupný konceptuálny výraz
     * @return Množinu tried, ktorú reprezentujú objekty {@link OWLClassExpression}
     * @throws ManchesterSyntaxParseException v prípade, že sa nepodarilo rozparsovat vstupný reťazec
     */
    public Set<OWLClassExpression> getSubClasses(String classExpressionString) throws ManchesterSyntaxParseException {
        if (classExpressionString.trim().length() == 0) {
            return Collections.emptySet();
        }
        concept = parser
                .parseClassExpression(classExpressionString);
        SortedSet<OWLClassExpression> subClasses = reasoner.getSubClasses(concept);
        return subClasses;
    }

    /**
     * Metóda, ktorá vráti množinu inštancií, ktoré vyhovujú konceptuálnemu výrazu
     *
     * @param classExpressionString vstupný konceptuálny výraz
     * @return Množinu inštancií, ktoré sú reprezentované triedou {@link OWLIndividual}
     * @throws ManchesterSyntaxParseException v prípade, že sa nepodarí rozparsovať vstupný konceptuálny výraz
     */
    public Set<OWLIndividual> getInstances(String classExpressionString) throws ManchesterSyntaxParseException {
        if (classExpressionString.trim().length() == 0) {
            return Collections.emptySet();
        }
        concept = parser
                .parseClassExpression(classExpressionString);
        SortedSet<OWLIndividual> individuals = reasoner.getIndividuals(concept);
        return individuals;
    }

    /**
     * Metóda, ktorá vráti množinu inštancií, ktoré vyhovujú konceptuálnemu výrazu
     *
     * @param manchesterExpression vstupný konceptuálny výraz, reprezentovaný entitou {@link OWLClassExpression}
     * @return Množinu inštancií, ktoré sú reprezentované triedou {@link OWLIndividual}
     */
    public Set<OWLIndividual> getInstances(OWLClassExpression manchesterExpression) {
        SortedSet<OWLIndividual> individuals = reasoner.getIndividuals(manchesterExpression);
        return individuals;
    }

    /**
     * Metóda, ktorá zistí do akých tried patrí vstupná inštancia {@link OWLIndividual}
     *
     * @param individual ištancia, ktorej triedu potrebujeme {@link OWLIndividual}
     * @return Množina tried, ktoré do ktorej vstupná inštancia patrí
     */
    public Set<OWLClass> getClassOfIndividual(OWLIndividual individual) {
        return reasoner.getTypes(individual);
    }
}
