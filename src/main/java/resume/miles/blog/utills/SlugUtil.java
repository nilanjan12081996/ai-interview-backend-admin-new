package resume.miles.blog.utills;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

import resume.miles.customeAnnotation.Utills;

@Utills
public class SlugUtil {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public  String makeSlug(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        // 1. Remove whitespace and replace with hyphens
        String noSpace = WHITESPACE.matcher(input).replaceAll("-");

        // 2. Normalize (Convert 'é' to 'e', 'ñ' to 'n', etc.)
        String normalized = Normalizer.normalize(noSpace, Normalizer.Form.NFD);

        // 3. Remove all non-latin characters (except for hyphens)
        String slug = NONLATIN.matcher(normalized).replaceAll("");

        // 4. Convert to lowercase and string
        return slug.toLowerCase(Locale.ENGLISH);
    }
}
