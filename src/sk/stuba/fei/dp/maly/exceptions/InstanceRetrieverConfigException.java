package sk.stuba.fei.dp.maly.exceptions;

/**
 * Created by Patrik on 10/03/2017.
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
