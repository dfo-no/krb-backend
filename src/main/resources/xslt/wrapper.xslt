<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:j="http://www.w3.org/2005/xpath-functions"
                xmlns="http://www.w3.org/1999/xhtml"
                exclude-result-prefixes="xs j"
                version="3.0">

    <xsl:output indent="yes"/>

    <xsl:param name="json" as="xs:string"/>
    <xsl:param name="krb_link" as="xs:string" select="'https://anskaffelser.no/kravbank/'"/>

    <xsl:variable name="parsed" select="json-to-xml($json)/j:map"/>

    <xsl:template match="/*">
        <html>
            <head>
                <title>Kravspesifikasjon</title>

                <style>
                    body {
                        font-size: 1.1em;
                        font-family: sans-serif;
                    }

                    #instructions {
                        font-size: 1.3em;
                    }

                    dl {
                        padding-left: 170pt;
                    }
                    dt {
                        margin-left: -170pt;
                        float: left;
                        font-style: italic;
                    }
                    dd {
                        margin-bottom: 15pt;
                        margin-inline-start: 0;
                    }

                    small {
                        color: #777;
                    }

                    a {
                        color: #005B91;
                    }
                </style>
            </head>
            <body>
                <div id="frontpage">
                    <h1>Kravspesifikasjon</h1>

                    <dl>
                        <dt>Tittel</dt>
                        <dd>
                            <xsl:value-of select="$parsed/j:string[@key='title']/normalize-space()"/>
                            <xsl:if test="$parsed/j:string[@key='description']/normalize-space()">
                                <br/>
                                <small><xsl:value-of select="$parsed/j:string[@key='description']/normalize-space()"/></small>
                            </xsl:if>
                        </dd>

                        <dt>Kravsett</dt>
                        <dd>
                            <xsl:value-of select="$parsed/j:map[@key='bank']/j:string[@key='title']/normalize-space()"/>
                            <xsl:if test="$parsed/j:map[@key='bank']/j:string[@key='description']/normalize-space()">
                                <br/>
                                <small><xsl:value-of select="$parsed/j:map[@key='bank']/j:string[@key='description']/normalize-space()"/></small>
                            </xsl:if>
                        </dd>

                        <dt>Oppdragsgiver</dt>
                        <dd>
                            <xsl:value-of select="$parsed/j:string[@key='organization']/normalize-space()"/><br/>
                            <small><xsl:value-of select="$parsed/j:string[@key='organizationNumber']/normalize-space()"/></small>
                        </dd>

                        <xsl:if test="$parsed/j:string[@key='caseNumber']/normalize-space()">
                            <dt>Saksnummer</dt>
                            <dd><xsl:value-of select="$parsed/j:string[@key='caseNumber']/normalize-space()"/></dd>
                        </xsl:if>
                    </dl>

                    <div id="instructions" style="background-color: #eee; border-radius: 15pt; margin: 30pt; padding: 20pt;">
                        <h2>Kravbank</h2>

                        <p>Denne filen inneholder strukturert informasjon som er laget ved bruk av Kravbank, en tjeneste levert av Direktoratet for forvaltning og økonomistyring (DFØ).</p>

                        <p>For å redigere eller besvare innholdet i denne filen lastes den opp i DFØ sin tjeneste:</p>

                        <p><a href="{$krb_link}"><xsl:value-of select="$krb_link"/></a></p>

                        <p><small>This file contains structured content used by the Kravbank service. Please visit the link above and upload this file to get access to change or respond to the content of this file.</small></p>
                    </div>
                </div>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>