/*
 *
 *  The MIT License
 *
 *  Copyright 2019 ITON Solutions.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.iton.jssi.ledger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.iton.jssi.anoncreds.schema.AttributeNames;
import org.iton.jssi.anoncreds.schema.Schema;
import org.iton.jssi.anoncreds.schema.SchemaId;
import org.junit.jupiter.api.Test;

import static org.iton.jssi.ledger.LedgerConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class LedgerServiceTest {

    private static final String IDENTIFIER = "NcYxiDXkpYi6ov5FcYDi1e";
    private static final String DEST = "VsKV7grR1BUE29mG2Fm2kX";
    private static final String VERKEY = "CnEDk9HrMnmiHXEV1WFgbVCRteYnPqsJwrTdcZaNhFVW";

    private LedgerService service = new LedgerService();

    @Test
    void testBuildGetNymRequest() throws JsonProcessingException {
        String expected = String.format("{\"type\": \"%s\", \"dest\": \"%s\"}", GET_NYM, DEST);
        String request = service.buildGetNymRequest(IDENTIFIER, DEST);
        checkRequest(request, expected);
    }

    @Test
    void testBuildGetNymRequestOptionalField() throws JsonProcessingException {
        String expected = String.format("{\"type\": \"%s\", \"dest\": \"%s\"}", GET_NYM, DEST);
        String request = service.buildGetNymRequest(null, DEST);
        checkRequest(request, expected);
    }

    @Test
    void testBuildNymRequestRequiredFields() throws JsonProcessingException {
        String expected = String.format("{\"type\": \"%s\", \"dest\": \"%s\"}", NYM, DEST);
        String request = service.buildNymRequest(IDENTIFIER, DEST, null, null, "");
        checkRequest(request, expected);
    }

    @Test
    void testBuildNymRequestWithRole() throws JsonProcessingException {
        String expected = String.format("{\"type\": \"%s\", \"dest\": \"%s\", \"role\": \"%s\"}", NYM, DEST, NETWORK_MONITOR);
        String request = service.buildNymRequest(IDENTIFIER, DEST, null, null, "NETWORK_MONITOR");
        checkRequest(request, expected);
        request = service.buildNymRequest(IDENTIFIER, DEST, null, null, "201");
        checkRequest(request, expected);
    }

    @Test
    void testBuildNymRequestOptionalFields() throws JsonProcessingException {
        String expected = String.format("{\"type\": \"%s\", \"dest\": \"%s\",\"alias\": \"%s\", \"verkey\": \"%s\"}", NYM, DEST, "alias", VERKEY);
        String request = service.buildNymRequest(IDENTIFIER, DEST, VERKEY, "alias", "");
        checkRequest(request, expected);
    }

    @Test
    void testBuildGetDdoRequest() throws JsonProcessingException {
        String expected = String.format("{\"type\": \"%s\", \"dest\": \"%s\"}", GET_DDO, DEST);
        String request = service.buildGetDdoRequest(IDENTIFIER, DEST);
        checkRequest(request, expected);
    }

    @Test
    void testBuildAttributeRequestOptionalFields() throws JsonProcessingException {
        String expected = String.format("{\"type\": \"%s\", \"dest\": \"%s\",\"hash\": \"%s\"}", ATTRIB, DEST, "hash");
        String request = service.buildAttributeRequest(IDENTIFIER, DEST, "hash", null, null);
        checkRequest(request, expected);
    }

    @Test
    void testBuildGetAttributeRequesHash() throws JsonProcessingException {
        String expected = String.format("{\"type\": \"%s\", \"dest\": \"%s\",\"hash\": \"%s\"}", GET_ATTR, DEST, "hash");
        String request = service.buildGetAttributeRequest(IDENTIFIER, DEST, "hash", null, null);
        checkRequest(request, expected);
    }

    @Test
    void testBuildGetAttributeRequestRaw() throws JsonProcessingException {
        String expected = String.format("{\"type\": \"%s\", \"dest\": \"%s\",\"raw\": \"%s\"}", GET_ATTR, DEST, "raw");
        String request = service.buildGetAttributeRequest(IDENTIFIER, DEST, null, "raw", null);
        checkRequest(request, expected);
    }

    @Test
    void testBuildGetAttributeRequestEncode() throws JsonProcessingException {
        String expected = String.format("{\"type\": \"%s\", \"dest\": \"%s\",\"enc\": \"%s\"}", GET_ATTR, DEST, "enc");
        String request = service.buildGetAttributeRequest(IDENTIFIER, DEST, null, null, "enc");
        checkRequest(request, expected);
    }

    @Test
    void testBuildSchemaRequest() throws JsonProcessingException {
        String expected = String.format("{\"type\": \"%s\", \"data\": {\"name\": \"%s\", \"version\": \"%s\", \"attr_names\": [\"%s\"]}}", SCHEMA, "Alex", "1.0", "male");
        AttributeNames names = new AttributeNames();
        names.add("male");
        Schema schema = new Schema(new SchemaId(IDENTIFIER, "name", "1.0"), "Alex", "1.0", names, 0);
        String request = service.buildSchemaRequest(IDENTIFIER, schema);
        checkRequest(request, expected);
    }

    private void checkRequest(String request, String expected) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(request);
        JsonNode operation = node.get("operation");
        assertEquals(operation, mapper.readTree(expected));
    }
}