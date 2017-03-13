package sk.stuba.fei.dp.maly.exceptions;

/**
 * Výnimka, ktorá obsluhuje chybu spracovania vstupného konceptuálneho výrazu
 * @author Patrik Malý
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
