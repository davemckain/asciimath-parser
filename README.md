# asciimath-parser

Trivial Java wrapper for https://github.com/davemckain/AsciiMathParser.js,
providing simple AsciiMath parsing capabilities in Java.

# Usage

Requires Xerces & Rhino JARs at Runtime.

Code artefacts can be obtained from:
https://mvnrepository.com/artifact/uk.ac.ed.ph.asciimath/asciimath-parser

The main `AsciiMathParser` class has a simple main method that accepts
AsciiMath input, converts to MathML, and outputs the result to `System.out`.

## Example code

```
final String asciiMathInput = "sinx";
final AsciiMathParser parser = new AsciiMathParser();
final Document result = parser.parseAsciiMath(asciiMathInput);
System.out.println(XmlUtilities.serializeMathmlDocument(result));
```

will print out:

```
<math xmlns="http://www.w3.org/1998/Math/MathML">
  <mrow>
    <mo>sin</mo>
    <mi>x</mi>
  </mrow>
</math>
```