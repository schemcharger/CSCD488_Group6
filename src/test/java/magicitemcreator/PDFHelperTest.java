package magicitemcreator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import helpers.PDFHelper;
import magicitem.ItemType;
import magicitem.MagicItem;

class PDFHelperTest {

	@Test
	public void PDFHelper_renderPDF_ExportsPDF() {
		MagicItem item = new MagicItem("Test Name", ItemType.WEAPON, "Test description");
		int i = 0;
		item.addTrait("" + i++);
		item.addTrait("" + i++);
		item.addTrait("" + i++);
		item.addTrait("" + i++);
		item.addTrait("" + i++);
		item.addTrait("" + i++);
		item.addTrait("" + i++);
		item.addTrait("" + i++);
		item.addTrait("" + i++);
		item.addTrait("" + i++);
		item.addTrait("" + i++);
		item.addTrait("" + i++);
		item.addTrait("" + i++);
		item.addTrait("" + i++);
		item.addTrait("" + i++);
		item.addTrait("" + i++);
		item.addTrait("" + i++);
		item.addTrait("" + i++);
		item.addTrait("" + i++);
		item.addTrait("" + i++);
		item.addTrait("" + i++);
		assertTrue(PDFHelper.renderPDF(item));
	}

}
