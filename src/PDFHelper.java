

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class PDFHelper {
	
	private static PDFont headerFont = new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD);
	private static PDFont propertiesFont = new PDType1Font(Standard14Fonts.FontName.TIMES_BOLD);
	private static PDFont textBodyFont = new PDType1Font(Standard14Fonts.FontName.TIMES_ITALIC);
	private static float width = PDRectangle.LETTER.getWidth();
	private static float height = PDRectangle.LETTER.getHeight();
	private PDDocument doc;
	private PDPage page;
	private PDPageContentStream stream;
	private PDImageXObject bgImage;
	private MagicItem item;
	
	public PDFHelper(MagicItem item) throws IOException {
		if(item == null) {
			throw new IllegalArgumentException("Null MagicItem detected in PDFHelper constructor");
		}
		this.item = item;
		this.doc = new PDDocument();
		this.page = new PDPage();
		this.stream = new PDPageContentStream(this.doc, this.page);
		this.bgImage = PDImageXObject.createFromFile("src/assets/parchment.jpg", this.doc);
	}
	
	public void renderPDF() throws IOException {
		this.renderArt();
		this.renderHeader();
		this.renderDescription();
		this.renderLore();
		this.renderProperties();
		this.createPDF();
	}
	
	private void renderArt() throws IOException {
		PDImageXObject pixelArt = PDImageXObject.createFromFile("src/assets/pikachu.png", this.doc);
		stream.drawImage(bgImage, 0, 0, PDRectangle.LETTER.getWidth(), PDRectangle.LETTER.getHeight());
		stream.drawImage(pixelArt, width - 175, height - 175, 128, 128);
	}
	
	private void renderHeader() throws IOException {
		this.stream.beginText();
		this.stream.newLineAtOffset(50, height - 130);
		this.stream.setFont(headerFont, 30);
		this.stream.showText(item.getName());
		this.stream.endText();
		
		this.stream.beginText();
		this.stream.newLineAtOffset(50, height - 170);
		this.stream.setFont(headerFont, 20);
		this.stream.showText(item.getType().name());
		this.stream.endText();
		
		this.stream.moveTo(50, height - 200);
		this.stream.lineTo(width - 50, height - 200);
		this.stream.stroke();
	}
	
	private void renderDescription() throws IOException {
		this.stream.beginText();
		this.stream.newLineAtOffset(50, height - 240);
		this.stream.setFont(headerFont, 20);
		this.stream.showText("Description");
		this.stream.endText();
		
		this.stream.beginText();
		this.stream.newLineAtOffset(50, height - 270);
		this.stream.setLeading(20);
		this.stream.setFont(textBodyFont, 14);
		divideText(item.getDescription(), this.page, this.stream, textBodyFont, 14);
		this.stream.endText();
	}
	
	private void renderLore() throws IOException {
		this.stream.beginText();
		this.stream.newLineAtOffset(50, height - 400);
		this.stream.setFont(headerFont, 20);
		this.stream.showText("Lore");
		this.stream.endText();
		
		this.stream.beginText();
		this.stream.newLineAtOffset(50, height - 430);
		this.stream.setLeading(20);
		this.stream.setFont(textBodyFont, 14);
		String lore = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
		divideText(lore, this.page, this.stream, textBodyFont, 14);
		this.stream.endText();
	}
	
	private void renderProperties() throws IOException {
		this.stream.beginText();
		this.stream.newLineAtOffset(50, height - 600);
		this.stream.setFont(headerFont, 20);
		this.stream.showText("Traits");
		this.stream.endText();
		
		this.stream.beginText();
		this.stream.newLineAtOffset(50, height - 630);
		this.stream.setLeading(30);
		this.stream.setFont(propertiesFont, 14);
		for(String trait : item.getTraits()) {
			this.stream.showText(String.format("\u2022 %s", trait));
			this.stream.newLine();
		}
//		this.stream.showText("\u2022Focused");
//		this.stream.newLine();
//		this.stream.showText("\u2022Hardy");
//		this.stream.newLine();
//		this.stream.showText("\u2022Unwavering");
		this.stream.endText();
	}
	
	private void createPDF() throws IOException {
		this.stream.close();
		this.doc.addPage(page);
		try {
			this.doc.save("test.pdf");
			this.doc.close();
			System.out.println("PDF created");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void divideText(String text, PDPage page, PDPageContentStream contentStream, PDFont font, int fontSize) throws IOException {
	    List<String> lines = new ArrayList<String>();
	    String line = "";
	    String[] words = text.split("\\s+");
	    for(String word : words) {
	    	if(!line.isEmpty()) {
	    		line += " ";
	    	}
	    	int size = (int) (fontSize * font.getStringWidth(line + word) / 1000);
	    	if(size > 512) {
	    		lines.add(line);
	    		line = word;
	    	} else {
	    		line += word;
	    	}
	    }
	    lines.add(line);
	    for(String ln : lines) { 
	    	contentStream.showText(ln);
	    	contentStream.newLine();
	    }
	}
	
}
