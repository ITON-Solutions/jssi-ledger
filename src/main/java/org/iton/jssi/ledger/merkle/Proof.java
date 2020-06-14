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

package org.iton.jssi.ledger.merkle;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 *
 * @author ITON Solutions
 */
public class Proof {
    
    // The hash of the root of the original `MerkleTree`
    byte[] root;
    // The first `Lemma` of the `Proof`
    Lemma lemma;
    // The value concerned by this `Proof`
    byte[] leaf;

    public Proof(byte[] root, byte[] leaf, Lemma lemma){
        this.root = root;
        this.lemma = lemma;
        this.leaf = leaf;
    }
    
    // Checks whether this inclusion proof is well-formed,
    // and whether its root hash matches the given `root`.
    public boolean validade(byte[] root) throws NoSuchAlgorithmException{
        
        if (!Arrays.equals(this.root, root) || !Arrays.equals(lemma.hash, root)) {
            return false;
        }
        
        return validate(lemma);
    }
    
    private boolean validate(Lemma lemma) throws NoSuchAlgorithmException{
        
        if(lemma.lemma == null){
            return lemma.sibling == null;
        }
        
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        
        switch(lemma.getPositioned()){
            case LEFT:{
                digest.update(new byte[]{(byte) 0x01});
                digest.update(lemma.sibling);
                digest.update(lemma.lemma.hash);
                break;
            }
            case RIGHT:{
                digest.update(new byte[]{(byte) 0x01});
                digest.update(lemma.lemma.hash);
                digest.update(lemma.sibling);
                break;
            }
        }
        return Arrays.equals(lemma.hash, digest.digest()) && validate(lemma.lemma);
    }
}
