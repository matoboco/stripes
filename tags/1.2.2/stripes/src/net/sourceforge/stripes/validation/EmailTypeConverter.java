package net.sourceforge.stripes.validation;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Collection;
import java.util.Locale;

/**
 * <p>A faux TypeConverter that validates that the String supplied is a valid email address.
 * Relies on javax.mail.internet.InternetAddress for the bulk of the work (note that this means
 * in order to use this type converter you must have JavaMail available in your classpath).</p>
 *
 * <p>If the String cannot be parsed, or it represents a "local" address (one with no @domain) a
 * single error message will be generated.  The error message is a scoped message with a default
 * scope of <tt>converter.email</tt> and name <tt>invalidEmail</tt>.  As a result error messages
 * will be looked for in the following order:</p>
 *
 * <ul>
 *   <li>actionPath.fieldName.invalidEmail</li>
 *   <li>actionPath.invalidEmail</li>
 *   <li>converter.email.invalidEmail</li>
 * </ul>
 *
 * @author Tim Fennell
 * @since Stripes 1.2
 */
public class EmailTypeConverter implements TypeConverter<String> {
    /** Accepts the Locale provide, but does nothing with it since emails are Locale-less. */
    public void setLocale(Locale locale) { /** Doesn't matter for email. */}

    /**
     * Validates the user input to ensure that it is a valid email address.
     *
     * @param input the String input, always a non-null non-empty String
     * @param targetType realistically always String since java.lang.String is final
     * @param errors a non-null collection of errors to populate in case of error
     * @return the parsed address, or null if there are no errors. Note that the parsed address
     *         may be different from the input if extraneous characters were removed.
     */
    public String convert(String input,
                          Class<? extends String> targetType,
                          Collection<ValidationError> errors) {

        String result = null;

        try {
            InternetAddress address = new InternetAddress(input);
            result = address.getAddress();
            if (!result.contains("@")) {
                result = null;
                throw new AddressException();
            }
        }
        catch (AddressException ae) {
            errors.add( new ScopedLocalizableError("converter.email", "invalidEmail") );
        }

        return result;
    }
}