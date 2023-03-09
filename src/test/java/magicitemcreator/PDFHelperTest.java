package magicitemcreator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import helpers.PDFHelper;
import magicitem.ItemType;
import magicitem.MagicItem;

class PDFHelperTest {

	@Test
	void PDFHelper_renderPDF_ExportsPDF() {
		MagicItem item = new MagicItem("Test Name", ItemType.WEAPON, "Test description");
		item.addTrait("Hardy");
		item.addTrait("Focused");
		assertTrue(PDFHelper.renderPDF(item));
	}

}
