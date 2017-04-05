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

	private static final String TOP_CLASS = "Thing";
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
			List<String> inClasses = new ArrayList<>();
			Set<OWLClass> inClass = engine.getClassOfIndividual(classInstance);
			for(OWLClass cClass : inClass){
				inClasses.add(shortFormProvider.getShortForm(cClass));
			}
			
			if(onlyInTopClass(inClasses)){
				inClasses.clear();
				inClasses.add(TOP_CLASS);
			}
			
			result.add(new InstanceDTO(shortFormProvider.getShortForm(classInstance),inClasses));
		}
		return result;
	}
	
	private boolean onlyInTopClass(List<String> inClasses){
		if(inClasses.isEmpty() || inClasses.size() == 1){
			if(inClasses.size() == 1){
				String tmp = inClasses.get(0);
				return tmp.isEmpty();
			}
			return true;
		}else{
			return false;
		}
	}

    //Pizza that  not(hasTopping only MeatTopping)

}

