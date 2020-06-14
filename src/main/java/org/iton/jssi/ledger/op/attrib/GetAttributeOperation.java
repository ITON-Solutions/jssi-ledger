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

package org.iton.jssi.ledger.op.attrib;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import static org.iton.jssi.ledger.LedgerConstants.GET_ATTR;

public class GetAttributeOperation {

    private String type;
    private String dest;
    private String hash;
    private String raw;
    private String enc;

    @JsonCreator
    public GetAttributeOperation(
            @JsonProperty("dest") String dest,
            @JsonProperty("hash") String hash,
            @JsonProperty("raw") String raw,
            @JsonProperty("enc") String enc){

        type = GET_ATTR;
        this.dest = dest;
        this.hash = hash;
        this.raw = raw;
        this.enc = enc;
    }

    public String getType() {
        return type;
    }

    public String getDest() {
        return dest;
    }

    public String getHash() {
        return hash;
    }

    public String getRaw() {
        return raw;
    }

    public String getEnc() {
        return enc;
    }
}
