package sk.stuba.fei.dp.maly.queries;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.util.ShortFormProvider;

import sk.stuba.fei.dp.maly.ui.models.IndividualsDatatableModel;

import org.semanticweb.owlapi.expression.ParserException;


public class DLQueryPrinter {
    private final DLQueryEngine dlQueryEngine;
    private final ShortFormProvider shortFormProvider;

    public DLQueryPrinter(DLQueryEngine engine, ShortFormProvider shortFormProvider) {
        this.shortFormProvider = shortFormProvider;
        dlQueryEngine = engine;
        }

    public void askQuery(String classExpression) {
        if (classExpression.length() == 0) {
            System.out.println("No class expression specified");
        } else {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("\\nQUERY:   ").append(classExpression).append("\\n\\n");
                Set<OWLClass> superClasses = dlQueryEngine.getSuperClasses(
                        classExpression, true);
                printEntities("SuperClasses", superClasses, sb);
                Set<OWLClass> equivalentClasses = dlQueryEngine
                        .getEquivalentClasses(classExpression);
                printEntities("EquivalentClasses", equivalentClasses, sb);
                Set<OWLClass> subClasses = dlQueryEngine.getSubClasses(classExpression,
                        true);
                printEntities("SubClasses", subClasses, sb);
                Set<OWLNamedIndividual> individuals = dlQueryEngine.getInstances(
                        classExpression, true);
                printEntities("Instances", individuals, sb);
                System.out.println(sb.toString());
            } catch (ParserException e) {
                System.out.println(e.getMessage());
            }
            }
        }

    private void printEntities(String name, Set<? extends OWLEntity> entities,
            StringBuilder sb) {
        sb.append(name);
        int length = 50 - name.length();
        for (int i = 0; i < length; i++) {
            sb.append(".");
        }
        sb.append("\\n\\n");
        if (!entities.isEmpty()) {
            for (OWLEntity entity : entities) {
                sb.append("\\t").append(shortFormProvider.getShortForm(entity))
                        .append("\\n");
            }
        } else {
            sb.append("\\t[NONE]\\n");
            }
        sb.append("\\n");
        }

    public List<IndividualsDatatableModel> getIndividuals(String classExpression){
		System.out.println(classExpression);
    	List<IndividualsDatatableModel> result = new ArrayList<IndividualsDatatableModel>();
		Set<OWLNamedIndividual> individuals = dlQueryEngine.getInstances(
                classExpression, true);
		for(OWLNamedIndividual classInstance : individuals){
			StringBuilder sb = new StringBuilder();
			Set<OWLClass> inClass = dlQueryEngine.getClassOfIndividual(classInstance);
			for(OWLClass cClass : inClass){
				sb.append(shortFormProvider.getShortForm(cClass));
				sb.append(",");
			}
			sb.deleteCharAt(sb.length()-1);
			System.out.println(sb.toString());
			result.add(new IndividualsDatatableModel(shortFormProvider.getShortForm(classInstance),sb.toString()));
		}
		//askQuery(classExpression);
		
		return result;
	}
    
    //Pizza that  not(hasTopping only MeatTopping)


}

