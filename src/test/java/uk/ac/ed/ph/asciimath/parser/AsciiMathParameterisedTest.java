/* Copyright (c) 2011-2012, The University of Edinburgh.
 * All Rights Reserved.
 *
 * This file is part of AsciiMathParser.
 *
 * AsciiMathParser is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AsciiMathParser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License (at http://www.gnu.org/licences/lgpl.html)
 * for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AsciiMathParser. If not, see <http://www.gnu.org/licenses/lgpl.html>.
 */
package uk.ac.ed.ph.asciimath.parser;

import static uk.ac.ed.ph.asciimath.parser.AsciiMathTestUtilities.wrapInMathElement;

import java.io.StringReader;
import java.util.Collection;

import org.custommonkey.xmlunit.XMLAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * Parameterised test that reads test data from {@link #TEST_RESOURCE_NAME}. Add extra test
 * cases here as and when required, following the (hopefully straightforward) conventions
 * listed therein.
 *
 * @author David McKain
 */
@RunWith(Parameterized.class)
public class AsciiMathParameterisedTest {

    public static final String TEST_RESOURCE_NAME = "parser-tests.txt";

    private final String asciiMathInput;
    private final String expectedMathML;

    @Parameters
    public static Collection<String[]> data() throws Exception {
        return AsciiMathTestUtilities.readAndParseSingleLineInputTestResource(TEST_RESOURCE_NAME);
    }

    public AsciiMathParameterisedTest(final String asciiMathInput, final String expectedMathML) {
        this.asciiMathInput = asciiMathInput;
        this.expectedMathML = expectedMathML;
    }

    @Test
    public void runTest() throws Throwable {
        final AsciiMathParser parser = new AsciiMathParser();
        final Document mathmlResult = parser.parseAsciiMath(asciiMathInput);

        final InputSource expectedSource = new InputSource(new StringReader(wrapInMathElement(expectedMathML)));
        final Document expectedDocument = XmlUtilities.createNSAwareDocumentBuilder().parse(expectedSource);

        XMLAssert.assertXMLEqual(expectedDocument, mathmlResult);
    }
}
