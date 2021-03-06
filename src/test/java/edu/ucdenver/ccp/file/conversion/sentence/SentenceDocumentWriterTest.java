package edu.ucdenver.ccp.file.conversion.sentence;

/*-
 * #%L
 * Colorado Computational Pharmacology's file conversion
 * 						project
 * %%
 * Copyright (C) 2019 Regents of the University of Colorado
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Regents of the University of Colorado nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileReaderUtil;
import edu.ucdenver.ccp.file.conversion.TextDocument;
import edu.ucdenver.ccp.nlp.core.annotation.TextAnnotationFactory;
import edu.ucdenver.ccp.nlp.core.mention.impl.DefaultClassMention;

public class SentenceDocumentWriterTest {

	@Test
	public void testSentenceDocumentWriter() throws IOException {
		CharacterEncoding encoding = CharacterEncoding.UTF_8;
		TextAnnotationFactory factory = TextAnnotationFactory.createFactoryWithDefaults();

		String documentText = "Intraocular pressure in genetically distinct mice: an update and strain survey\n\nAbstract\n\nBackground\n\nLittle is known about genetic factors affecting intraocular pressure (IOP) in mice and other mammals.";

		TextDocument td = new TextDocument("12345", "PMC", documentText);

		td.addAnnotation(factory.createAnnotation(0, 78,
				"Intraocular pressure in genetically distinct mice: an update and strain survey",
				new DefaultClassMention("sentence")));
		td.addAnnotation(factory.createAnnotation(80, 88, "Abstract", new DefaultClassMention("sentence")));
		td.addAnnotation(factory.createAnnotation(90, 100, "Background", new DefaultClassMention("sentence")));
		td.addAnnotation(factory.createAnnotation(102, 203,
				"Little is known about genetic factors affecting intraocular pressure (IOP) in mice and other mammals.",
				new DefaultClassMention("sentence")));

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		new SentenceDocumentWriter().serialize(td, outputStream, encoding);
		String serializedSentences = outputStream.toString(encoding.getCharacterSetName());

		List<String> expectedSentences = CollectionsUtil.createList(
				"Intraocular pressure in genetically distinct mice: an update and strain survey", "Abstract",
				"Background",
				"Little is known about genetic factors affecting intraocular pressure (IOP) in mice and other mammals.");

		List<String> observedSentences = FileReaderUtil
				.loadLinesFromFile(new ByteArrayInputStream(serializedSentences.getBytes()), encoding);

		assertEquals(expectedSentences, observedSentences);

	}

}
