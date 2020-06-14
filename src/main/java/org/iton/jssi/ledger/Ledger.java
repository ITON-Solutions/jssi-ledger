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
import io.reactivex.Observable;
import org.iton.jssi.anoncreds.schema.Schema;
import org.iton.jssi.crypto.CryptoException;
import org.iton.jssi.crypto.CryptoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class Ledger {

    private LedgerService ledgerService;
    private CryptoService cryptoService;

    public Ledger(LedgerService ledgerService, CryptoService cryptoService){
        this.ledgerService = ledgerService;
        this.cryptoService = cryptoService;
    }

    private static final Logger LOG = LoggerFactory.getLogger(Ledger.class);

    public Observable<String> buildGetNymRequest(String submitterDid, String targetDid){
        LOG.debug(String.format("Build get nym request submitter did: {}, target did: {}", submitterDid, targetDid));
        if(!cryptoService.validateDid(submitterDid) || !cryptoService.validateDid(targetDid)){
            return null;
        }

        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws JsonProcessingException {
                return ledgerService.buildGetNymRequest(submitterDid, targetDid);
            }
        });
    }

    public Observable<String> buildNymRequest(String submitterDid, String targetDid, String verkey, String alias, String role) {
        LOG.debug(String.format("Build nym request submitter did: {}, target did: {}", submitterDid, targetDid));
        if(!cryptoService.validateDid(submitterDid) || !cryptoService.validateDid(targetDid)){
            return null;
        }

        try {
            cryptoService.validateKey(verkey);
        } catch(CryptoException e){
            return null;
        }

        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws JsonProcessingException {
                return ledgerService.buildNymRequest(submitterDid, targetDid, verkey, alias, role);
            }
        });
    }

    public Observable<String> buildGetDdoRequest(String submitterDid, String targetDid){
        LOG.debug(String.format("Build get ddo request submitter did: {}, target did: {}", submitterDid, targetDid));

        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws JsonProcessingException {
                return ledgerService.buildGetDdoRequest(submitterDid, targetDid);
            }
        });
    }

    public Observable<String> buildAttributeRequest(String submitterDid, String targetDid, String hash, String raw, String enc) {
        LOG.debug(String.format("Build attribute request submitter did: {}, target did: {}", submitterDid, targetDid));
        if(!cryptoService.validateDid(submitterDid) || !cryptoService.validateDid(targetDid)){
            return null;
        }

        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws JsonProcessingException {
                return ledgerService.buildAttributeRequest(submitterDid, targetDid, hash, raw, enc);
            }
        });
    }

    public Observable<String> buildGetAttributeRequest(String submitterDid, String targetDid, String hash, String raw, String enc) {
        LOG.debug(String.format("Build attribute request submitter did: {}, target did: {}", submitterDid, targetDid));
        if(!cryptoService.validateDid(submitterDid) || !cryptoService.validateDid(targetDid)){
            return null;
        }

        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws JsonProcessingException {
                return ledgerService.buildGetAttributeRequest(submitterDid, targetDid, hash, raw, enc);
            }
        });
    }

    public Observable<String> buildSchemaRequest(String submitterDid, Schema schema) {
        LOG.debug(String.format("Build schema request submitter did: {}", submitterDid));
        if(!cryptoService.validateDid(submitterDid)){
            return null;
        }

        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws JsonProcessingException {
                return ledgerService.buildSchemaRequest(submitterDid, schema);
            }
        });
    }
}
