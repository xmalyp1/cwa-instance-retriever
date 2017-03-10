package sk.stuba.fei.dp.maly.model.dto;

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
