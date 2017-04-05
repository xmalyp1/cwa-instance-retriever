package sk.stuba.fei.dp.maly.model.dto;

import java.util.List;

/**
 * Objekt {@code InstanceDTO} predstavuje model, výsledkov dopytovania pomocou komponenty instance retriever
 * Po dopytovaní inštancií pomocou objektu {@link sk.stuba.fei.dp.maly.retriever.InstanceRetriever} sa nám vráti zoznam inštancií, ktoré sú reprezentované týmto objektom.
 *
 * Objekt uchováva nasledujúce informácie:
 *
 * <ul>
 *     <li>Meno inštancie</li>
 *     <li>Triedu inštancie</li>
 * </ul>
 *
 *
 * @author Patrik Malý
 */
public class InstanceDTO {

	private String namedIndividual;
	private List<String> individualClasses;
	
	public InstanceDTO(String ind, List<String> owlClasses){
		this.namedIndividual = ind;
		this.individualClasses = owlClasses;
	}

	public String getNamedIndividual() {
		return namedIndividual;
	}

	public void setNamedIndividual(String namedIndividual) {
		this.namedIndividual = namedIndividual;
	}

	public List<String> getIndividualClasses() {
		return individualClasses;
	}

	public void setIndividualClasses(List<String> individualClasses) {
		this.individualClasses = individualClasses;
	}
	
	
	
}
