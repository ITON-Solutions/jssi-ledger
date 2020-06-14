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

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.iton.jssi.ledger.util.Utils;

/**
 *
 * @author ITON Solutions
 */
public class MerkleTree {
    // The root of the inner binary tree
    Node root = new Node();
    // The height of the tree
    int height;
    // The number of leaf nodes in the tree
    int count;
    // The number of nodes in the tree
    int nodes_count;
    // List of leaves in the tree
    List<Leaf> leaves = new ArrayList<>();
    
    public MerkleTree() {
    }
    
    public MerkleTree(List<Leaf> leaves){
        this.leaves = leaves;
    }
    
    public final MerkleTree build() throws NoSuchAlgorithmException{
        
        if(leaves.isEmpty()){
            root.hash   = MessageDigest.getInstance("SHA-256").digest(new byte[]{});
            height      = 0;
            count       = 0;
            nodes_count = 0;
            return this;
        }
        
        height      = 0;
        count       = leaves.size();
        nodes_count = 0;
        
        List<Node> current = new ArrayList<>(leaves);
        while(current.size() > 1){
            List<Node> next = new ArrayList<>();
            while(!current.isEmpty()){
                if(current.size() == 1){
                    next.add(current.remove(0));
                } else {
                    Node left  = current.remove(0);
                    Node right = current.remove(0);

                    Node node  = new Node();
                    node.left  = left;
                    node.right = right;
                    node.hash  = hash(left.hash, right.hash);
                    next.add(node);
                    nodes_count++;
                }
            }
            height++;
            current = next;
        }
        
        root = current.remove(0);
        return this;
    }
    
    /**
     * append leaf to MerkleTree
     * @param data
     * @throws NoSuchAlgorithmException
     */
    public void append(byte[] data) throws NoSuchAlgorithmException{
        if(count == 0){
            Leaf leaf = new Leaf(data);
            root = leaf;
            leaves.add(leaf);
            count++;
        } else if(Integer.bitCount(count) == 1){
            // add to right subtree, if number of leaves is power of 2
            Leaf leaf = new Leaf(data);
            List<Leaf> list = new ArrayList<>();
            list.add(leaf);
            MerkleTree tree = new MerkleTree(list).build();
            
            root = new Node(root, tree.root, hash(root.hash, tree.root.hash));
            leaves.add(leaf);
            nodes_count++;
            count++;
        } else {
            // nearest power of 2
            int nearest = Integer.highestOneBit(count);
            List<Leaf> remain = new ArrayList<>(count - nearest);
            for (int i = nearest; i < count; i++) {
                remain.add(leaves.get(i));
            }
            Leaf leaf = new Leaf(data);
            remain.add(leaf);
            MerkleTree tree = new MerkleTree(remain).build();
            root = new Node(root.left, tree.root, hash(root.left.hash, tree.root.hash));
            leaves.add(leaf);
            nodes_count++;
            count++;
        }
    }
    
    public Node findHash(Node node, byte[] hash){
        if(node == null){
            return null;
        } else {
            if(Arrays.equals(node.hash, hash)){
                return node;
            }
            Node right = findHash(node.right, hash);
            if(right != null){
                return right;
            } else {
                Node left = findHash(node.left, hash);
                if(left != null){
                    return left;
                } else {
                    return null;
                }
            }
        }
    }
    
    /**
     *
     * @param node initial node
     * @return list of leaves from Node node
     */
    public List<Leaf> getLeaves(Node node){

        List<Leaf> next = new ArrayList<>();
        if(node.left != null){
            next.addAll(getLeaves(node.left));
        }
        if(node.right != null){
            next.addAll(getLeaves(node.right));
        }
        
        if(node.left == null && node.right == null){
            next.add((Leaf)node);
        }
        return next;
                 
    }
    
    public Proof generateProof(byte[] leaf){
        Lemma lemma = new Lemma(root, leaf);
        return new Proof(root.hash, leaf, lemma);
    }
    
    public boolean consistencyProof(byte[] new_root_hash, int new_count, List<byte[]> proofs) throws NoSuchAlgorithmException{
        
        if (count == 0 || (count == new_count && root.hash == new_root_hash)){
            return true;
        }
        
        if (count > new_count){
            return false;
        }
        
        int old_node = count - 1;
        int new_node = new_count - 1;

        // is even?
        while (old_node % 2 != 0) {
            old_node /= 2;
            new_node /= 2;
        }
        
        byte[] old_hash;
        byte[] new_hash;
        
        Iterator<byte[]> proof = proofs.listIterator();
        
        if (old_node != 0) {
            if(proof.hasNext()){
                new_hash = proof.next();
            } else {
                return false;
            }
            old_hash = new_hash;
        } else {
            new_hash = root.hash;
            old_hash = new_hash;
        }
        
        while (old_node != 0) {
            
            if (old_node % 2 != 0) {

                if (proof.hasNext()) {
                    byte[] next_proof = proof.next();
                    old_hash = hash(next_proof, old_hash);
                    new_hash = hash(next_proof, new_hash);
                } else {
                    return false;
                }
                
            } else if (old_node < new_node) {
                if (proof.hasNext()) {
                    new_hash = hash(new_hash, proof.next());
                } else {
                    return false;
                }
            }
            
            old_node /= 2;
            new_node /= 2;
        }
            
        while (new_node != 0) {
            if (proof.hasNext()) {
                new_hash = hash(new_hash, proof.next());
            } else {
                return false;
            }
            new_node /= 2;
        }

        return Arrays.equals(old_hash, root.hash) && Arrays.equals(new_hash, new_root_hash);
    }
    
    public MerkleTree deserialize(String data) throws NoSuchAlgorithmException, IOException {
        MerkleTree tree = new MerkleTree();
        ObjectMapper mapper = new ObjectMapper();
        tree.root = tree.deserialize(mapper.readTree(data));
        tree.height = 0x20 - Integer.numberOfLeadingZeros(leaves.size());
        return tree;
    }
    
    private Node deserialize(JsonNode data){

        Node node = new Node();
        nodes_count++;
        node.hash = Utils.hexToBytes(data.get("hash").asText());
        if(data.has("left")){
            JsonNode left = data.get("left");
            node.left = deserialize(left);
        }
        if(data.has("right")){
            JsonNode right = data.get("right");
            node.right = deserialize(right);
        }
        if(!data.has("right") && !data.has("left")){
            count++;
            nodes_count--;
            Leaf leaf = new Leaf(data.get("data").asText().getBytes());
            leaves.add(leaf);
            return leaf;
        }
        return node;
    }
    
    public String serialize(){
        return serialize(root).toString();
    }
    
    private ObjectNode serialize(Node node){
        ObjectNode item = JsonNodeFactory.instance.objectNode();
        item.put("hash", Utils.bytesToHex(node.hash));
        if(node.left != null){
            item.set("left", serialize(node.left));
        }
        if(node.left != null){
            item.set("right", serialize(node.right));
        }
        if(node.left == null && node.left == null){
            item.put("data", new String(((Leaf)node).data));
        }
        return item;
    }
    
    private byte[] hash(byte[] left, byte[] right) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(new byte[]{(byte) 0x01});
        digest.update(left);
        digest.update(right);
        return digest.digest();
    }
   
    
    public byte[] getHash(){
        return root.hash;
    }
    
    public int getHeight() {
        return height;
    }

    public int getCount() {
        return count;
    }
    
    public boolean isEmpty(){
        return count == 0;
    }

    public List<Leaf> getLeaves() {
        return leaves;
    }
}
