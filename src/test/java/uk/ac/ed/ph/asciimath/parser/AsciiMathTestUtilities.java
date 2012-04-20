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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * Some helpers for the test suite
 *
 * @author David McKain
 */
public final class AsciiMathTestUtilities {

    public static String wrapInMathElement(final String mathMLContent) {
        return wrapInMathElement(mathMLContent, false);
    }

    public static String wrapInMathElement(final String mathMLContent, final boolean displayMode) {
        final StringBuilder b = new StringBuilder("<math xmlns='http://www.w3.org/1998/Math/MathML'");
        if (displayMode) {
            b.append(" display='block'");
        }
        b.append(">").append(mathMLContent).append("</math>");
        return b.toString();
    }

    public static void assertXMLEqual(final String expectedXML, final Document parserOutput) throws Throwable {
        final InputSource expectedSource = new InputSource(new StringReader(expectedXML));
        final Document expectedDocument = XmlUtilities.createNSAwareDocumentBuilder().parse(expectedSource);
        try {
            XMLUnit.setIgnoreWhitespace(true);
            XMLAssert.assertXMLEqual(expectedDocument, parserOutput);
        }
        catch (final Throwable e) {
            System.out.println("XML comparison failed: expected=" + expectedXML);
            System.out.println("XML comparison failed: actually=" + XmlUtilities.serializeMathmlDocument(parserOutput));
            throw e;
        }
    }

    /**
     * Reads in the given "single line" test file, assuming it is of the format
     * <pre>
     * single line input
     * 1 or more lines of output
     * ==== (divider token, at least 4 characters)
     * ...
     * </pre>
     *
     * Returns a List of [input,output] pairs.
     *
     * @throws Exception
     */
    public static Collection<String[]> readAndParseSingleLineInputTestResource(final String resourceName) throws Exception {
        String testData = ensureGetResource(resourceName);
        testData = testData.replaceAll("(?m)^#.*$(\\s+)(^|$)", "");
        final String[] testItems = testData.split("(?m)\\s*^={4,}\\s*");
        final Collection<String[]> result = new ArrayList<String[]>(testItems.length);
        for (final String testItem : testItems) {
            result.add(testItem.split("\n+", 2));
        }
        return result;
    }

    private static String ensureGetResource(final String resourceName) throws IOException {
        final InputStream resourceStream = AsciiMathTestUtilities.class.getClassLoader().getResourceAsStream(resourceName);
        if (resourceStream==null) {
            throw new RuntimeException("Could not load Resource '" + resourceName
                    + "' via ClassLoader - check the ClassPath!");
        }
        return readUnicodeStream(resourceStream);
    }

    //----------------------------------------------------------------------------
    // Reading methods

    /** Maximum size of characters we'll read from a text stream before complaining */
    public static final int MAX_TEXT_STREAM_SIZE = 1024 * 1024;

    /**
     * Same as {@link #readCharacterStream(Reader)} but assumes the
     * stream is encoded as UTF-8.
     *
     * @param in InputStream supplying character data
     * @return String representing the data read in
     * @throws IOException
     */
    public static String readUnicodeStream(final InputStream in) throws IOException {
        return readCharacterStream(new InputStreamReader(in, "UTF-8"));
    }

    /**
     * Reads all character data from the given Reader, returning a String
     * containing all of the data. The Reader will be buffered for efficiency and
     * will be closed once finished with.
     * Be careful reading in very large files - we will barf if MAX_FILE_SIZE is
     * passed as a safety precaution.
     *
     * @param reader source of string data
     * @return String representing the data read
     * @throws IOException
     */
    public static String readCharacterStream(final Reader reader) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(reader);
        String line;
        int size = 0;
        final StringBuilder result = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            size += line.length() + 1;
            if (size > MAX_TEXT_STREAM_SIZE) {
                throw new IOException("String data exceeds current maximum safe size ("
                        + MAX_TEXT_STREAM_SIZE + ")");
            }
            result.append(line).append("\n");
        }
        bufferedReader.close();
        return result.toString();
    }
}
