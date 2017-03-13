package sk.stuba.fei.dp.maly.retriever;

/**
 * Enumerát, ktorý reprezentuje možné formy dopytovania dát pomocou komponenty instance retriever.
 * Možné hodnoty sú :
 * <ul>
 *     <li><b>CWA</b> - Closed world assumption</li>
 *     <li><b>OWA</b> - Open world assumption</li>
 * </ul>
 * @author Patrik Malý
 */
public enum RetrieverMode {
    CWA,
    OWA;
}
