package sk.stuba.fei.dp.maly.concept.expression;

import java.util.Set;

import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntaxClassExpressionParser;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;
import org.semanticweb.owlapi.util.ShortFormProvider;
import sk.stuba.fei.dp.maly.exceptions.ManchesterSyntaxParseException;

public class ManchesterSyntaxQueryParser {
    private final OWLOntology rootOntology;
    private final BidirectionalShortFormProvider bidiShortFormProvider;

    public ManchesterSyntaxQueryParser(OWLOntology rootOntology, ShortFormProvider shortFormProvider) {
        this.rootOntology = rootOntology;
        OWLOntologyManager manager = rootOntology.getOWLOntologyManager();
        Set<OWLOntology> importsClosure = rootOntology.getImportsClosure();
        // Create a bidirectional short form provider to do the actual mapping.
        // It will generate names using the input
        // short form provider.
        bidiShortFormProvider = new BidirectionalShortFormProviderAdapter(manager,
                importsClosure, shortFormProvider);
    }

    public OWLClassExpression parseClassExpression(String classExpressionString) throws ManchesterSyntaxParseException {

        if (rootOntology == null || rootOntology.getOWLOntologyManager() == null) {
            throw new ManchesterSyntaxParseException("Ontology or ontology manager was not initialized.");
        }
        OWLDataFactory dataFactory = rootOntology.getOWLOntologyManager()
                .getOWLDataFactory();
        OWLEntityChecker entityChecker = new ShortFormEntityChecker(bidiShortFormProvider);
        try {
            ManchesterOWLSyntaxClassExpressionParser parser = new ManchesterOWLSyntaxClassExpressionParser(dataFactory, entityChecker);
            parser.setOWLEntityChecker(entityChecker);
            return parser.parse(classExpressionString);
        } catch (Exception e) {
            throw new ManchesterSyntaxParseException("Unable to parse query: " + classExpressionString);

        }
    }
}
