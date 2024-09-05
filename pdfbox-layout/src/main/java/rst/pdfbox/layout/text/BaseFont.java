package rst.pdfbox.layout.text;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 * In order to easy handling with fonts, this enum bundles the
 * plain/italic/bold/bold-italic variants of the three standard font types
 * {@link Standard14Fonts.FontName#TIMES_ROMAN Times},{@link Standard14Fonts.FontName#COURIER Courier} and
 * {@link Standard14Fonts.FontName#HELVETICA Helveticy}.
 * 
 * @author Ralf
 *
 */
public enum BaseFont {

    Times(new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN),
            new PDType1Font(Standard14Fonts.FontName.TIMES_BOLD),
            new PDType1Font(Standard14Fonts.FontName.TIMES_ITALIC),
            new PDType1Font(Standard14Fonts.FontName.TIMES_BOLD_ITALIC)
    ), //
    Courier(new PDType1Font(Standard14Fonts.FontName.COURIER),
            new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD),
            new PDType1Font(Standard14Fonts.FontName.COURIER_OBLIQUE),
            new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD_OBLIQUE)
    ), //

    Helvetica(new PDType1Font(Standard14Fonts.FontName.HELVETICA),
            new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD),
            new PDType1Font(Standard14Fonts.FontName.HELVETICA_OBLIQUE),
            new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD_OBLIQUE)
    );

    private PDFont plainFont;
    private PDFont boldFont;
    private PDFont italicFont;
    private PDFont boldItalicFont;

    private BaseFont(PDFont plainFont, PDFont boldFont, PDFont italicFont,
	    PDFont boldItalicFont) {
	this.plainFont = plainFont;
	this.boldFont = boldFont;
	this.italicFont = italicFont;
	this.boldItalicFont = boldItalicFont;
    }

    public PDFont getPlainFont() {
	return plainFont;
    }

    public PDFont getBoldFont() {
	return boldFont;
    }

    public PDFont getItalicFont() {
	return italicFont;
    }

    public PDFont getBoldItalicFont() {
	return boldItalicFont;
    }

}
