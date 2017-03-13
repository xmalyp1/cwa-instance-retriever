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
 * @see DLQueryEngine
 * @author Patrik Malý
 */
public class QueryResultPrinter {

    private final DLQueryEngine dlQueryEngine;
    private final ShortFormProvider shortFormProvider;

	/**
	 * Konštruktor objektu {@code QueryResultPrinter}
	 * @param engine objekt {@link DLQueryEngine}
	 * @param shortFormProvider objekt zodpovedný za spracovanie OWL entít
	 */
    public QueryResultPrinter(DLQueryEngine engine, ShortFormProvider shortFormProvider) {
        this.shortFormProvider = shortFormProvider;
        dlQueryEngine = engine;
        }

	public DLQueryEngine getDlQueryEngine(){
		return dlQueryEngine;
	}

	/**
	 * Metóda, ktorá vráti množinu inštancií, ktoré vyhovujú konceptuálnemu výrazu
	 *
	 * @param classExpression vstupný konceptuálny výraz reprezentovaný reťazcom
	 * @return množinu inštancií, ktoré sú reprezentované triedou {@link InstanceDTO}
	 * @throws ManchesterSyntaxParseException v pripade, že sa nepodarí spracovať vstupný konceptuálny výraz
	 */
    public List<InstanceDTO> getIndividuals(String classExpression) throws ManchesterSyntaxParseException {
    	List<InstanceDTO> result = new ArrayList<InstanceDTO>();
		Set<OWLIndividual> individuals = dlQueryEngine.getInstances(
                classExpression);
		return formatOWLIndividuals(individuals);
	}

	/**
	 * Metóda, ktorá vráti množinu inštancií vyhovujúcim konceptuálnym výrazom. Inštancie sú reprezentované triedou {@link InstanceDTO}.
	 * @param classExpression konceptuálny výraz reprezentovaný objektom {@link OWLClassExpression}
	 * @return množina inštancií, ktoré vyhovujú konceptuálnemu výrazu
	 */
	public List<InstanceDTO> getIndividuals(OWLClassExpression classExpression){
		List<InstanceDTO> result = new ArrayList<InstanceDTO>();
		Set<OWLIndividual> individuals = dlQueryEngine.getInstances(
				classExpression);
		return formatOWLIndividuals(individuals);
	}


	/**
	 * Metóda, ktorá spracuváva vstupnú množinu entít {@link OWLIndividual} a následne ich transformuje
	 * na množinu objektov {@link InstanceDTO}
	 * @param individuals vstupná množina objektov
	 * @return kolekciu objektov reprezentujúcich inštancie - {@link InstanceDTO}
	 */
	private List<InstanceDTO> formatOWLIndividuals(Set<OWLIndividual> individuals){
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

