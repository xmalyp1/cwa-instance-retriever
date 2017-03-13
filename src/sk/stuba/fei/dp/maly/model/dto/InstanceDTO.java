package sk.stuba.fei.dp.maly.model.dto;

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
	private String individualClass;
	
	public InstanceDTO(String ind, String owlClass){
		this.namedIndividual = ind;
		this.individualClass = owlClass;
	}

	public String getNamedIndividual() {
		return namedIndividual;
	}

	public void setNamedIndividual(String namedIndividual) {
		this.namedIndividual = namedIndividual;
	}

	public String getIndividualClass() {
		return individualClass;
	}

	public void setIndividualClass(String individualClass) {
		this.individualClass = individualClass;
	}
	
	
	
}
