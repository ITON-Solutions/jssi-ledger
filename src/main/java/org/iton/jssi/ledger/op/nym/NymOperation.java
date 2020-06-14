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

package org.iton.jssi.ledger.op.nym;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import static org.iton.jssi.ledger.LedgerConstants.NYM;

public class NymOperation {

    private String type;
    private String dest;
    private String verkey;
    private String alias;
    private String role;

    @JsonCreator
    public NymOperation(
            @JsonProperty("dest") String dest,
            @JsonProperty("verkey") String verkey,
            @JsonProperty("alias") String alias,
            @JsonProperty("role") String role){

        type = NYM;
        this.dest = dest;
        this.verkey = verkey;
        this.alias = alias;
        this.role = role;
    }

    public String getType() {
        return type;
    }

    public String getDest() {
        return dest;
    }

    public String getVerkey() {
        return verkey;
    }

    public String getAlias() {
        return alias;
    }

    public String getRole() {
        return role;
    }
}
