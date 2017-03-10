package sk.stuba.fei.dp.maly.concept.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.util.ShortFormProvider;

import sk.stuba.fei.dp.maly.exceptions.ManchesterSyntaxParseException;
import sk.stuba.fei.dp.maly.model.dto.InstanceDTO;


public class QueryResultPrinter {
    private final DLQueryEngine dlQueryEngine;
    private final ShortFormProvider shortFormProvider;

    public QueryResultPrinter(DLQueryEngine engine, ShortFormProvider shortFormProvider) {
        this.shortFormProvider = shortFormProvider;
        dlQueryEngine = engine;
        }

	public DLQueryEngine getDlQueryEngine(){
		return dlQueryEngine;
	}

    public List<InstanceDTO> getIndividuals(String classExpression) throws ManchesterSyntaxParseException {
    	List<InstanceDTO> result = new ArrayList<InstanceDTO>();
		Set<OWLIndividual> individuals = dlQueryEngine.getInstances(
                classExpression);
		return formatOWLIndividual(individuals);
	}

	public List<InstanceDTO> getIndividuals(OWLClassExpression classExpression) throws ManchesterSyntaxParseException {
		List<InstanceDTO> result = new ArrayList<InstanceDTO>();
		Set<OWLIndividual> individuals = dlQueryEngine.getInstances(
				classExpression);
		return formatOWLIndividual(individuals);
	}


	private List<InstanceDTO> formatOWLIndividual(Set<OWLIndividual> individuals){
		List<InstanceDTO> result = new ArrayList<>();
		for(OWLIndividual classNonNamedInstance : individuals){
			OWLNamedIndividual classInstance = (OWLNamedIndividual) classNonNamedInstance;
			StringBuilder sb = new StringBuilder();
			Set<OWLClass> inClass = dlQueryEngine.getClassOfIndividual(classInstance);
			for(OWLClass cClass : inClass){
				sb.append(shortFormProvider.getShortForm(cClass));
				sb.append(",");
			}
			if(sb.length() > 0)
				sb.deleteCharAt(sb.length()-1);
			result.add(new InstanceDTO(shortFormProvider.getShortForm(classInstance),sb.toString()));
		}
		return result;
	}

    //Pizza that  not(hasTopping only MeatTopping)

}

