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
import org.iton.jssi.anoncreds.schema.Schema;
import org.iton.jssi.ledger.op.attrib.AttributeOperation;
import org.iton.jssi.ledger.op.attrib.GetAttributeOperation;
import org.iton.jssi.ledger.op.ddo.GetDdoOperation;
import org.iton.jssi.ledger.op.nym.GetNymOperation;
import org.iton.jssi.ledger.op.nym.NymOperation;
import org.iton.jssi.ledger.op.schema.SchemaOperation;
import org.iton.jssi.ledger.op.schema.SchemaOperationData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.iton.jssi.ledger.LedgerConstants.*;

public class LedgerService {

    private static final Logger LOG = LoggerFactory.getLogger(LedgerService.class);

    public String buildGetNymRequest(String identifier, String dest) throws JsonProcessingException {
        GetNymOperation operation = new GetNymOperation(dest);
        return Request.builder()
                .identifier(identifier)
                .operation(operation)
                .build();
    }

    public String buildNymRequest(String identifier, String dest, String verkey, String alias, String role) throws JsonProcessingException {

        switch(role){
             case ROLE_REMOVE:
                 role = null;
                 break;
             case "STEWARD":
                 role = STEWARD;
                 break;
             case "TRUSTEE":
                 role = TRUSTEE;
                 break;
             case "TRUST_ANCHOR":
             case "ENDORSER":
                 role = ENDORSER;
                 break;
             case  "NETWORK_MONITOR":
                 role = NETWORK_MONITOR;
                 break;
             default:{
                 if(!ROLES.contains(role)){
                     return null;
                 }
             }
         }

        NymOperation operation = new NymOperation(dest, verkey, alias, role);
        return Request.builder()
                .identifier(identifier)
                .operation(operation)
                .build();
    }

    public String buildGetDdoRequest(String identifier, String dest) throws JsonProcessingException {
        GetDdoOperation operation = new GetDdoOperation(dest);
        return Request.builder()
                .identifier(identifier)
                .operation(operation)
                .build();
    }

    public String buildAttributeRequest(String identifier, String dest, String hash, String raw, String enc) throws JsonProcessingException {
        AttributeOperation operation = new AttributeOperation(dest, hash, raw, enc);
        return Request.builder()
                .identifier(identifier)
                .operation(operation)
                .build();
    }

    public String buildGetAttributeRequest(String identifier, String dest, String hash, String raw, String enc) throws JsonProcessingException {
        GetAttributeOperation operation = new GetAttributeOperation(dest, hash, raw, enc);
        Request.RequestBuilder<GetAttributeOperation> builder = Request.builder()
                .identifier(identifier)
                .operation(operation);
        return builder.build();
    }

    public String buildSchemaRequest(String identifier, Schema schema) throws JsonProcessingException {
        SchemaOperationData data = new SchemaOperationData(schema.getName(), schema.getVersion(), schema.getAttrNames());
        SchemaOperation operation = new SchemaOperation(data);
        return Request.builder()
                .identifier(identifier)
                .operation(operation)
                .build();
    }
}
