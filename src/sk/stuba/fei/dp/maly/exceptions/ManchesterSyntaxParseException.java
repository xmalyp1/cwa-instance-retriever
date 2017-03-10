package sk.stuba.fei.dp.maly.exceptions;

/**
 * Created by Patrik on 10/03/2017.
 */
public class ManchesterSyntaxParseException extends Exception {

    public ManchesterSyntaxParseException () {

    }

    public ManchesterSyntaxParseException (String message) {
        super (message);
    }

    public ManchesterSyntaxParseException (Throwable cause) {
        super (cause);
    }

    public ManchesterSyntaxParseException (String message, Throwable cause) {
        super (message, cause);
    }
}
