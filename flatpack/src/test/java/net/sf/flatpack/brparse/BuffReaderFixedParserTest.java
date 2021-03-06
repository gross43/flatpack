package net.sf.flatpack.brparse;

import java.io.StringReader;
import junit.framework.TestCase;
import net.sf.flatpack.DataSet;
import net.sf.flatpack.DefaultDataSet;
import net.sf.flatpack.DefaultParserFactory;
import net.sf.flatpack.Parser;
import net.sf.flatpack.structure.Row;

/**
 * Test misc methods in the BuffReaderFixedParser class
 *
 * @author Tim Zimmerman
 */
public class BuffReaderFixedParserTest extends TestCase {

    private static final String PZ_MAP_XML_STRING
            = "<?xml version=\"1.0\"?>\n"
            + "<!DOCTYPE PZMAP SYSTEM	\"flatpack.dtd\" >\n"
            + "<PZMAP>\n"
            + "	<COLUMN name=\"FIELD_ONE\" length=\"10\" />\n"
            + "	<COLUMN name=\"FIELD_TWO\" length=\"10\" />\n"
            + "	<COLUMN name=\"FIELD_THREE\" length=\"10\" />\n"
            + "</PZMAP> ";
    private static final String EXACT_LINE_STRING = "exactly   thirty    characters";
    private static final String SHORT_LINE_STRING = "shorter   than      thirty";
    private static final String LONG_LINE_STRING = "longer    than      thirty    characters";

    private Parser fixedWidthParser;

    public String parseRawData(String pzMapXML, String dataString) {
        String rawData = null;
        StringReader pzReader = new StringReader(pzMapXML);
        StringReader lineReader = new StringReader(dataString);
        try {
            Parser parser = BuffReaderParseFactory.getInstance().newFixedLengthParser(pzReader, lineReader);
            assertTrue("Parser is not an instance of " + BuffReaderFixedParser.class, parser instanceof BuffReaderFixedParser);

            BuffReaderFixedParser fixedWidthParser = (BuffReaderFixedParser) parser;
            fixedWidthParser.setIgnoreExtraColumns(true);
            fixedWidthParser.setHandlingShortLines(true);
            fixedWidthParser.setStoreRawDataToDataSet(true);

            DefaultDataSet dataset = (DefaultDataSet) fixedWidthParser.parse();
            Row parsedRow = fixedWidthParser.buildRow(dataset);
            rawData = parsedRow.getRawData();
        } finally {
            pzReader.close();
            lineReader.close();
        }
        return rawData;
    }

    public void testBuildExactRow() {
        String rawData = this.parseRawData(PZ_MAP_XML_STRING, EXACT_LINE_STRING);
        assertTrue("The raw data does not match the orginal line", rawData.equals(EXACT_LINE_STRING));
    }

    public void testBuildShortRow() {
        String rawData = this.parseRawData(PZ_MAP_XML_STRING, SHORT_LINE_STRING);
        assertTrue("The raw data does not match the orginal line", rawData.equals(SHORT_LINE_STRING));
    }

    public void testBuildLongRow() {
        String rawData = this.parseRawData(PZ_MAP_XML_STRING, LONG_LINE_STRING);
        assertTrue("The raw data does not match the orginal line", rawData.equals(LONG_LINE_STRING));
    }

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(BuffReaderFixedParserTest.class);
    }
}
