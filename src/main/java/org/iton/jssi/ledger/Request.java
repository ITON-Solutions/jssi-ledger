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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Request<T> {

    private long reqId;
    private String identifier;
    private T operation;
    private int protocolVersion;
    private String signature;

    @JsonCreator
    public Request(
            @JsonProperty("identifier") String identifier,
            @JsonProperty("operation") T operation){

        this.reqId = System.nanoTime();
        this.identifier = identifier;
        this.operation = operation;
        this.protocolVersion = 2;
    }

    public static RequestBuilder builder(){
        return new RequestBuilder<>();
    }

    public static class RequestBuilder<T>{
        private String identifier;
        private T operation;
        private String signature;

        public RequestBuilder<T> identifier(String identifier){
            this.identifier = identifier;
            return this;
        }

        public RequestBuilder<T> operation(T operation){
            this.operation = operation;
            return this;
        }

        public RequestBuilder<T> signature(String signature){
            this.signature = signature;
            return this;
        }

        public String build() throws JsonProcessingException {
            ObjectMapper mapper = new ObjectMapper()
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            Request<T> request =  new Request(identifier, operation);
            request.signature = signature;
            return mapper.writeValueAsString(request);
        }
    }

    public long getReqId() {
        return reqId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public T getOperation() {
        return operation;
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public String getSignature() {
        return signature;
    }
}
