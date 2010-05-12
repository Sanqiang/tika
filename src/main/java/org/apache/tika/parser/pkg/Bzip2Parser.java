/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tika.parser.pkg;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.input.CloseShieldInputStream;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.pkg.bzip2.CBZip2InputStream;
import org.apache.tika.sax.XHTMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * Gzip parser.
 */
public class Bzip2Parser extends PackageParser {

    /**
     * Parses the given stream as a gzip file.
     */
    public void parse(
            InputStream stream, ContentHandler handler, Metadata metadata)
            throws IOException, SAXException, TikaException {
        // The CBZip2InputStream class does not want to see the magic BZ header
        if (stream.read() != 'B' || stream.read() != 'Z') {
            throw new TikaException("Invalid BZip2 magic header");
        }

        metadata.set(Metadata.CONTENT_TYPE, "application/x-bzip");

        XHTMLContentHandler xhtml = new XHTMLContentHandler(handler, metadata);
        xhtml.startDocument();

        // At the end we want to close the bzip2 stream to release any associated
        // resources, but the underlying document stream should not be closed
        InputStream gzip =
            new CBZip2InputStream(new CloseShieldInputStream(stream));
        try {
            Metadata entrydata = new Metadata();
            String name = metadata.get(Metadata.RESOURCE_NAME_KEY);
            if (name != null) {
                if (name.endsWith(".tbz")) {
                    name = name.substring(0, name.length() - 4) + ".tar";
                } else if (name.endsWith(".tbz2")) {
                    name = name.substring(0, name.length() - 5) + ".tar";
                } else if (name.endsWith(".bz")) {
                    name = name.substring(0, name.length() - 3);
                } else if (name.endsWith(".bz2")) {
                    name = name.substring(0, name.length() - 4);
                }
                entrydata.set(Metadata.RESOURCE_NAME_KEY, name);
            }
            parseEntry(gzip, xhtml, entrydata);
        } finally {
            gzip.close();
        }

        xhtml.endDocument();
    }

}