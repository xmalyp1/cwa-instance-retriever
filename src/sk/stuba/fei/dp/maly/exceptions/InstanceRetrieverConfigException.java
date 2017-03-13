package sk.stuba.fei.dp.maly.exceptions;

/**
 * Výnimka, ktorá obsluhuje chybnú konfiguráciu komponenty instance retriever
 * @author Patrik Malý
 */
public class InstanceRetrieverConfigException extends Exception {

    public InstanceRetrieverConfigException () {

    }

    public InstanceRetrieverConfigException (String message) {
        super (message);
    }

    public InstanceRetrieverConfigException (Throwable cause) {
        super (cause);
    }

    public InstanceRetrieverConfigException (String message, Throwable cause) {
        super (message, cause);
    }
}
