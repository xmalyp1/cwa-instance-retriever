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

/**
 * Objekt zodpovedný za spracovanie výstupov z instance retrieva do objektov, ktoré jednoduchšie reprezentujú OWL entity
 * ako napríklad {@link InstanceDTO}
 *
 * @see ShortFormProvider
 * @author Patrik Malý
 */
public class OWLIndividualConverter {

    private final ShortFormProvider shortFormProvider;

	/**
	 * Konštruktor objektu {@code OWLIndividualConverter}
	 * @param shortFormProvider objekt zodpovedný za spracovanie OWL entít
	 */
    public OWLIndividualConverter(ShortFormProvider shortFormProvider) {
        this.shortFormProvider = shortFormProvider;
        }

	/**
	 * Metóda, ktorá spracuváva vstupnú množinu entít {@link OWLIndividual} a následne ich transformuje
	 * na množinu objektov {@link InstanceDTO}
	 * @param individuals vstupná množina objektov
	 * @return kolekciu objektov reprezentujúcich inštancie - {@link InstanceDTO}
	 */
	public List<InstanceDTO> formatOWLIndividuals(Set<OWLIndividual> individuals,DLQueryEngine engine){
		List<InstanceDTO> result = new ArrayList<>();
		for(OWLIndividual classNonNamedInstance : individuals){
			OWLNamedIndividual classInstance = (OWLNamedIndividual) classNonNamedInstance;
			StringBuilder sb = new StringBuilder();
			Set<OWLClass> inClass = engine.getClassOfIndividual(classInstance);
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

