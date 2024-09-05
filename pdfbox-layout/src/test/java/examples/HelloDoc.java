package examples;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import rst.pdfbox.layout.elements.Document;
import rst.pdfbox.layout.elements.Paragraph;

public class HelloDoc {

    public static void main(String[] args) throws Exception {
	Document document = new Document(40, 60, 40, 60);

	Paragraph paragraph = new Paragraph();
	paragraph.addText("Hello Document", 20,
		FontHelper.getFont(Standard14Fonts.FontName.HELVETICA));
	document.add(paragraph);

	final OutputStream outputStream = new FileOutputStream(args[0]+"hellodoc.pdf");
	document.save(outputStream);

    }
}
