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
import java.util.Arrays;

/**
 *
 * @author ITON Solutions
 
 
 A `Lemma` holds the hash of a hash, the hash of its sibling hash,
 and a sub lemma, whose `node_hash`, when combined with this `sibling_hash`
 must be equal to this `node_hash`.
 */
public class Lemma implements IPositioned {
    
    byte[] hash;
    byte[] sibling;
    Lemma lemma;
    private Positioned positioned = Positioned.NONE;

    @Override
    public Positioned getPositioned() {
        return positioned;
    }
    
    /**
     *
     * @param node
     * @param needle
     * 
     * Attempts to generate a proof that the a value with hash `needle` is a member of the given `tree`.
     */
    public Lemma (Node node, byte[] needle){
        if(node == null){
            return;
        }
        if(node.left == null && node.right == null){
            buildLemma(node.hash, needle);
            return;
        }
        buildLemma(node.hash, needle, node.left, node.right);
    }
    
    // reached leaf
    private void buildLemma(byte[] hash, byte[] needle){
        if(Arrays.equals(hash, needle)){
            this.hash = hash;
        }
    }

    // hash
    private void buildLemma(byte[] hash, byte[] needle, Node left, Node right){
        
        lemma = new Lemma(left, needle);
        if(lemma.hash != null){
            this.sibling = right.hash;
            this.positioned = Positioned.RIGHT;
            this.hash = hash;
            return;
        }
        
        lemma = new Lemma(right, needle);
        if(lemma.hash != null){
            sibling = left.hash;
            positioned = Positioned.LEFT;
            this.hash = hash;
        }
    }
}
