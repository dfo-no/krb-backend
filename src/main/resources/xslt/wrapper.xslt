<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:j="http://www.w3.org/2005/xpath-functions"
                xmlns="http://www.w3.org/1999/xhtml"
                exclude-result-prefixes="xs j"
                version="3.0">

    <xsl:output indent="yes"/>

    <xsl:param name="json" as="xs:string"/>

    <xsl:variable name="parsed" select="json-to-xml($json)/j:map"/>

    <xsl:template match="/*">
        <html>
            <head>
                <title>Kravspesifikasjon</title>
            </head>
            <body>
                <h1>Kravspesifikasjon</h1>

                <p>Title: <xsl:value-of select="$parsed/j:string[@key='title']/normalize-space()"/></p>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>