package com.antonicapart.triumph.support;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Str {

    /**
     * Transform the character string to <b>"studly case"</b>
     * Example of "studly case" : StudlyCase
     *
     * @param str The string to be processed
     * @return The processed string
     */
    public static String toStudly(final String str) {
        final Collection<String> collector = Arrays.asList(new Str(str).normalizedString.split("_"));

        return collector.stream().map(Str::upperCaseFirst).collect(Collectors.joining(""));
    }

    /**
     * Transform the character string to <b>"camel case"</b>
     * Example of "camel case" : camelCase
     *
     * @param str The string to be processed
     * @return The processed string
     */
    public static String toCamel(final String str) {
        return Str.lowerCaseFirst(Str.toStudly(str));
    }

    /**
     * Transform the character string to <b>"kebab case"</b>
     * Example of "kebab case" : kebab-case
     *
     * @param str The string to be processed
     * @return The processed string
     */
    public static String toKebab(final String str) {
        return new Str(str).normalizedString.replace('_', '-');
    }

    /**
     * Transform the character string to <b>"snake case"</b>
     * Example of "snake case" : snake_case
     *
     * @param str The string to be processed
     * @return The processed string
     */
    public static String toSnake(final String str) {
        return new Str(str).normalizedString;
    }

    /**
     * Enclose the string in quotation marks
     * Example of a string between quotes : 'string'
     *
     * @param str The string to be processed
     * @return The processed string
     */
    public static String addQuotationMarks(final String str) {
        return new StringBuilder("'")
                .append(str)
                .append("'")
                .toString();
    }

    /**
     * Capitalize the first letter of the string
     * Example of the first letter in upper case : String
     *
     * @param str The string to be processed
     * @return The processed string
     */
    public static String upperCaseFirst(final String str) {
        return new StringBuilder(str.substring(0, 1).toUpperCase())
                .append(str.substring(1))
                .toString();
    }

    /**
     * Put in lower case the first letter of the string
     * Example of the first letter in lower case : string
     *
     * @param str The string to be processed
     * @return The processed string
     */
    public static String lowerCaseFirst(final String str) {
        return new StringBuilder(str.substring(0, 1).toLowerCase())
                .append(str.substring(1))
                .toString();
    }

    /**
     * Normalized string in "snake case"
     */
    private final String normalizedString;

    // Case patterns
    private static final String camelCasePattern = "^[a-zA-Z][a-zA-Z0-9]+$";
    private static final String kebabCasePattern = "^([a-z][a-z0-9]*)(-[a-z0-9]+)*$";

    /**
     * Hide the constructor to make the class static
     */
    private Str(String str) {

        str = str.trim();

        if (Pattern.compile(camelCasePattern).matcher(str).find()) {
            this.normalizedString = Arrays.stream(str.split("(?=[A-Z])")).map(String::toLowerCase).collect(Collectors.joining("_"));
        } else if (Pattern.compile(kebabCasePattern).matcher(str).find()) {
            this.normalizedString = str.replace('-', '_');
        } else {
            this.normalizedString = str;
        }
    }
}
