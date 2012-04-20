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

import static uk.ac.ed.ph.asciimath.parser.AsciiMathTestUtilities.assertXMLEqual;
import static uk.ac.ed.ph.asciimath.parser.AsciiMathTestUtilities.wrapInMathElement;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * Basic tests of the parser API
 *
 * @author David McKain
 */
public class AsciiMathBasicTest {

    private AsciiMathParser parser;

    @Before
    public void setupParser() {
        parser = new AsciiMathParser();
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNullInput() {
        parser.parseAsciiMath(null);
    }

    @Test
    public void testSuccessBasic() throws Throwable {
        assertXMLEqual(wrapInMathElement("<mn>1</mn>"), parser.parseAsciiMath("1"));
    }

    @Test
    public void testSuccessDisplayMode() throws Throwable {
        final AsciiMathParserOptions options = new AsciiMathParserOptions();
        options.setDisplayMode(true);

        final Document output = parser.parseAsciiMath("1", options);
        assertXMLEqual(wrapInMathElement("<mn>1</mn>", true), output);
    }

    @Test
    public void testSuccessSourceAnnotation() throws Throwable {
        final AsciiMathParserOptions options = new AsciiMathParserOptions();
        options.setAddSourceAnnotation(true);

        final Document output = parser.parseAsciiMath("1", options);
        assertXMLEqual(wrapInMathElement("<semantics><mn>1</mn><annotation encoding='ASCIIMathInput'>1</annotation></semantics>"),
                output);
    }

    @Test
    public void testEmptyInput() throws Throwable {
        /* (Note what ASCIIMath generates here!) */
        assertXMLEqual(wrapInMathElement("<mo/>"), parser.parseAsciiMath(""));
    }
}
