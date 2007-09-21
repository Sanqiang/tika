/**
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

package org.apache.tika.mime;

//JDK imports
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

//Tika imports
import org.apache.tika.metadata.TikaMimeKeys;
import org.apache.tika.utils.Configuration;

//Junit imports
import junit.framework.TestCase;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Test Suite for the {@link MimeTypes} repository.
 * </p>.
 */
public class TestMimeUtils extends TestCase implements TikaMimeKeys {

    private static final String tikaMimeFile = "org/apache/tika/mime/tika-mimetypes.xml";

    private Configuration conf;

    private static URL u;

    static {
        try {
            u = new URL("http://mydomain.com/x.pdf?x=y");
        } catch (MalformedURLException e) {
            fail(e.getMessage());
        }
    }

    private static final File f = new File("/a/b/c/x.pdf");

    private MimeUtils utils;

    public TestMimeUtils() {
        Configuration conf = new Configuration();
        conf.set(TIKA_MIME_FILE, tikaMimeFile);
        utils = new MimeUtils(conf);
        assertNotNull(utils);
    }

    public void testLoadMimeTypes() {
        assertNotNull(utils.getRepository().forName("application/octet-stream"));
        assertNotNull(utils.getRepository().forName("text/x-tex"));
    }

    public void testGuessMimeTypes() {

        assertEquals("application/pdf", utils.getRepository().getMimeType(
                "x.pdf").getName());
        assertEquals("application/pdf", utils.getRepository().getMimeType(u)
                .getName());
        assertEquals("application/pdf", utils.getRepository().getMimeType(f)
                .getName());
        assertEquals("text/plain", utils.getRepository().getMimeType("x.txt")
                .getName());
        assertEquals("text/html", utils.getRepository().getMimeType("x.htm")
                .getName());
        assertEquals("text/html", utils.getRepository().getMimeType("x.html")
                .getName());
        assertEquals("application/xhtml+xml", utils.getRepository()
                .getMimeType("x.xhtml").getName());
        assertEquals("application/xml", utils.getRepository().getMimeType(
                "x.xml").getName());
        assertEquals("application/msword", utils.getRepository().getMimeType(
                "x.doc").getName());
        assertEquals("application/vnd.ms-powerpoint", utils.getRepository()
                .getMimeType("x.ppt").getName());
        assertEquals("application/vnd.ms-excel", utils.getRepository()
                .getMimeType("x.xls").getName());
        assertEquals("application/zip", utils.getRepository().getMimeType(
                "x.zip").getName());
        assertEquals("application/vnd.oasis.opendocument.text", utils
                .getRepository().getMimeType("x.odt").getName());
        assertEquals("application/octet-stream", utils.getRepository()
                .getMimeType("x.xyz").getName());
    }

}