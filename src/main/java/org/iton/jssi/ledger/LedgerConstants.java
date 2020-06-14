/*
 * The MIT License
 *
 * Copyright 2019 ITON Solutions.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.iton.jssi.ledger;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author ITON Solutions
 */
public abstract class LedgerConstants {

    public static final String NODE = "0";
    public static final String NYM = "1";
    public static final String GET_TXN = "3";
    public static final String TXN_AUTHR_AGRMT = "4";
    public static final String GET_TXN_AUTHR_AGRMT = "6";
    public static final String GET_TXN_AUTHR_AGRMT_AML = "7";
    public static final String ATTRIB = "100";
    public static final String SCHEMA = "101";
    public static final String CRED_DEF = "102";
    public static final String GET_ATTR = "104";
    public static final String GET_NYM = "105";
    public static final String GET_SCHEMA = "107";
    public static final String GET_CRED_DEF = "108";
    public static final String POOL_UPGRADE = "109";
    public static final String POOL_RESTART = "118";
    public static final String POOL_CONFIG = "111";
    public static final String REVOC_REG_DEF = "113";
    public static final String REVOC_REG_ENTRY = "114";
    public static final String GET_REVOC_REG_DEF = "115";
    public static final String GET_REVOC_REG = "116";
    public static final String GET_REVOC_REG_DELTA = "117";
    public static final String GET_VALIDATOR_INFO = "119";
    public static final String AUTH_RULE = "120";
    public static final String GET_AUTH_RULE = "121";
    public static final String GET_DDO = "120";//TODO change number

    public static final List<String> REQUESTS =  Arrays.asList(new String[]{
        NODE, NYM, GET_TXN, ATTRIB, SCHEMA, CRED_DEF, GET_ATTR, GET_NYM,
        GET_SCHEMA, GET_CRED_DEF, POOL_UPGRADE, POOL_RESTART, POOL_CONFIG, REVOC_REG_DEF, REVOC_REG_ENTRY,
        GET_REVOC_REG_DEF, GET_REVOC_REG, GET_REVOC_REG_DELTA, GET_VALIDATOR_INFO, AUTH_RULE, GET_DDO
    });

    public static final String TRUSTEE = "0";
    public static final String STEWARD = "2";
    public static final String ENDORSER = "101";
    public static final String NETWORK_MONITOR = "201";
    public static final String ROLE_REMOVE = "";
    
    public static final List<String> ROLES =  Arrays.asList(new String[]{
            TRUSTEE, STEWARD, ENDORSER, NETWORK_MONITOR, ROLE_REMOVE
    });

    public static final List<String> REQUESTS_FOR_MULTI_STATE_PROOFS = Arrays.asList(new String[]{
            GET_REVOC_REG_DELTA
    });

    public static final List<String> REQUESTS_FOR_STATE_PROOFS =  Arrays.asList(new String[]{
        GET_NYM, GET_TXN_AUTHR_AGRMT, GET_TXN_AUTHR_AGRMT_AML, GET_SCHEMA, GET_CRED_DEF, GET_ATTR,
        GET_REVOC_REG, GET_REVOC_REG_DEF, GET_REVOC_REG_DELTA, GET_AUTH_RULE, GET_TXN
    });
}
