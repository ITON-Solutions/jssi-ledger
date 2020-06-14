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
import org.bouncycastle.util.Arrays;

/**
 *
 * @author ITON Solutions
 */
public class Leaf extends Node{
    public byte[] data;
    
    public Leaf(final byte[] data){
        this.data = data;
        this.hash = hash();
    }
    
    private byte[] hash(){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(new byte[]{(byte) 0x00});
            return digest.digest(data);
        } catch (NoSuchAlgorithmException e) {
            return new byte[0];
        }
    }
    
    public boolean equals(Object object){
        if(!(object instanceof Leaf)){
            return false;
        }
        Leaf leaf = (Leaf) object;
        return Arrays.areEqual(data, leaf.data);
    }
}
