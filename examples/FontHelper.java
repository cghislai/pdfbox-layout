import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class FontHelper {
    public static PDType1Font getFont(Standard14Fonts.FontName name) {
        switch(name) {
            case HELVETICA:
                return new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            case TIMES_ROMAN:
                return new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN);
            case TIMES_BOLD:
                return new PDType1Font(Standard14Fonts.FontName.TIMES_BOLD);
            case COURIER:
                return new PDType1Font(Standard14Fonts.FontName.COURIER);
            case HELVETICA_BOLD_OBLIQUE:
                return new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD_OBLIQUE);
            default:
                return new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        }
    }
}
