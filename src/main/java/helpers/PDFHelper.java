package helpers;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import magicitem.MagicItem;

public class PDFHelper {
	
	private static PDFont headerFont = PDType1Font.COURIER_BOLD;
	private static PDFont propertiesFont = PDType1Font.TIMES_BOLD;
	private static PDFont textBodyFont = PDType1Font.TIMES_ITALIC;
	private static float width = PDRectangle.LETTER.getWidth();
	private static float height = PDRectangle.LETTER.getHeight();
	private static PDDocument doc;
	private static PDPage page;
	private static PDPageContentStream stream;
	private static PDImageXObject bgImage;
	private static MagicItem item;
	
	public static boolean renderPDF(MagicItem item) {
		if(item == null) {
			throw new IllegalArgumentException("Null MagicItem detected in PDFHelper constructor");
		}
		PDFHelper.item = item;
		doc = new PDDocument();
		page = new PDPage();
		try {
			stream = new PDPageContentStream(doc, page);
			bgImage = PDImageXObject.createFromFile("src/main/assets/parchment.jpg", doc);
		} catch (IOException e) {
			System.out.println("Content stream error in renderPDF method of PDFHelper");
			e.printStackTrace();
		}
		renderArt();
		renderHeader();
		renderDescription();
//		renderLore();
		renderProperties();
		return createPDF();
	}
	
	private static void renderArt() {
		PDImageXObject pixelArt;
		try {
			pixelArt = PDImageXObject.createFromFile("src/main/assets/pikachu.png", doc);
			stream.drawImage(bgImage, 0, 0, PDRectangle.LETTER.getWidth(), PDRectangle.LETTER.getHeight());
			stream.drawImage(pixelArt, width - 175, height - 175, 128, 128);
		} catch (IOException e) {
			System.out.println("Content stream error in renderArt method of PDFHelper");
			e.printStackTrace();
		}
	}
	
	private static void renderHeader() {
		try {
			stream.beginText();
			stream.newLineAtOffset(50, height - 130);
			stream.setFont(headerFont, 30);
			stream.showText(item.getName());
			stream.endText();
			
			stream.beginText();
			stream.newLineAtOffset(50, height - 170);
			stream.setFont(headerFont, 20);
			stream.showText(item.getType().name());
			stream.endText();
			
			stream.moveTo(50, height - 200);
			stream.lineTo(width - 50, height - 200);
			stream.stroke();
		} catch (IOException e) {
			System.out.println("Content stream error in renderHeader method of PDFHelper");
			e.printStackTrace();
		}
	}
	
	private static void renderDescription() {
		try {
			stream.beginText();
			stream.newLineAtOffset(50, height - 240);
			stream.setFont(headerFont, 20);
			stream.showText("Description");
			stream.endText();
			
			stream.beginText();
			stream.newLineAtOffset(50, height - 270);
			stream.setLeading(20);
			stream.setFont(textBodyFont, 14);
			divideText(item.getDescription(), page, stream, textBodyFont, 14);
			stream.endText();
		} catch (IOException e) {
			System.out.println("Content stream error in renderDescription method of PDFHelper");
			e.printStackTrace();
		}
	}
	
//	private static void renderLore() {
//		try {
//			stream.beginText();
//			stream.newLineAtOffset(50, height - 400);
//			stream.setFont(headerFont, 20);
//			stream.showText("Lore");
//			stream.endText();
//			
//			stream.beginText();
//			stream.newLineAtOffset(50, height - 430);
//			stream.setLeading(20);
//			stream.setFont(textBodyFont, 14);
//			divideText(lore, page, stream, textBodyFont, 14);
//			stream.endText();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	private static void renderProperties() {
		try {
			stream.beginText();
			stream.newLineAtOffset(50, height - 600);
			stream.setFont(headerFont, 20);
			stream.showText("Traits");
			stream.endText();
			
			stream.beginText();
			stream.newLineAtOffset(50, height - 630);
			stream.setLeading(30);
			stream.setFont(propertiesFont, 14);
			for(String trait : item.getTraits()) {
				stream.showText(String.format("\u2022 %s", trait));
				stream.newLine();
			}
			stream.endText();
		} catch (IOException e) {
			System.out.println("Content stream error in renderProperties method of PDFHelper");
			e.printStackTrace();
		}
	}
	
	private static boolean createPDF() {
		String home = System.getProperty("user.home");
		String dir = String.format("%s%sDocuments%sMagic Item Creator", home, File.separator, File.separator);
		final File pdf = new File(dir, "Item Exports");
		pdf.mkdirs();
		try {
			stream.close();
			doc.addPage(page);
			doc.save(new File(pdf, item.getName() + ".pdf"));
			doc.close();
			System.out.println("PDF created");
			return true;
		} catch (IOException e) {
			System.out.println("I/O error in createPDF method of PDFHelper");
			e.printStackTrace();
			return false;
		}
	}
	
	private static void divideText(String text, PDPage page, PDPageContentStream contentStream, PDFont font, int fontSize) throws IOException {
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
