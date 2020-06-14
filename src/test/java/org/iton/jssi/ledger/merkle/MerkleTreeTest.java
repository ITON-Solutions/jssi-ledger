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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iton.jssi.ledger.merkle;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import org.bitcoinj.core.Base58;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Andrei
 */
public class MerkleTreeTest {
    
    public MerkleTreeTest() {
    }
    

    /**
     * Test of findHash method, of class MerkleTree.
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void testFindHash() throws NoSuchAlgorithmException {
        List<Leaf> leaves = new ArrayList<>();
        for(int i = 1; i < 10; i++){
            Leaf leaf = new Leaf(Integer.toString(i).getBytes());
            leaves.add(leaf);
        }
        
        MerkleTree tree = new MerkleTree(leaves).build();
        byte[] hash = new byte[]{
            (byte)0xe8, (byte)0xbc, (byte)0xd9, (byte)0x7e, (byte)0x34, (byte)0x96, (byte)0x93, (byte)0xdc,
            (byte)0xfe, (byte)0xc0, (byte)0x54, (byte)0xfe, (byte)0x21, (byte)0x9a, (byte)0xb3, (byte)0x57,
            (byte)0xb7, (byte)0x5d, (byte)0x3c, (byte)0x1c, (byte)0xd9, (byte)0xf8, (byte)0xbe, (byte)0x17,
            (byte)0x67, (byte)0xf6, (byte)0x09, (byte)0x0f, (byte)0x9c, (byte)0x86, (byte)0xf9, (byte)0xfd};
        
        Node result = tree.findHash(tree.root, hash);
        assertArrayEquals(result.hash, hash);
        
        hash = new byte[]{
            (byte)0x22, (byte)0x15, (byte)0xe8, (byte)0xac, (byte)0x4e, (byte)0x2b, (byte)0x87, (byte)0x1c,
            (byte)0x2a, (byte)0x48, (byte)0x18, (byte)0x9e, (byte)0x79, (byte)0x73, (byte)0x8c, (byte)0x95,
            (byte)0x6c, (byte)0x08, (byte)0x1e, (byte)0x23, (byte)0xac, (byte)0x2f, (byte)0x24, (byte)0x15,
            (byte)0xbf, (byte)0x77, (byte)0xda, (byte)0x19, (byte)0x9d, (byte)0xfd, (byte)0x92, (byte)0x0c};
        
        result = tree.findHash(tree.root, hash);
        assertArrayEquals(result.hash, hash);
        
        hash = new byte[]{
            (byte) 0x23, (byte) 0x15, (byte) 0xe8, (byte) 0xac, (byte) 0x4e, (byte) 0x2b, (byte) 0x87, (byte) 0x1c,
            (byte) 0x2a, (byte) 0x48, (byte) 0x18, (byte) 0x9e, (byte) 0x79, (byte) 0x73, (byte) 0x8c, (byte) 0x95,
            (byte) 0x6c, (byte) 0x08, (byte) 0x1e, (byte) 0x23, (byte) 0xac, (byte) 0x2f, (byte) 0x24, (byte) 0x15,
            (byte) 0xbf, (byte) 0x77, (byte) 0xda, (byte) 0x19, (byte) 0x9d, (byte) 0xfd, (byte) 0x92, (byte) 0x0d};

        result = tree.findHash(tree.root, hash);
        assertNull(result);
    }

    /**
     * Test of generateProof method, of class MerkleTree.
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void testGenerateProof() throws NoSuchAlgorithmException {
        List<Leaf> leaves = new ArrayList<>();
        for(int i = 0; i < 9; i++){
            Leaf leaf = new Leaf(Integer.toString(i).getBytes());
            leaves.add(leaf);
        } 
        
        MerkleTree tree = new MerkleTree(leaves).build();

        for(Leaf leaf : leaves){
            Proof proof = tree.generateProof(leaf.hash);
            boolean result = proof.validade(tree.root.hash);
            assertTrue(result);
        }
    }

    /**
     * Test of consistencyProof method, of class MerkleTree.
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void testConsistencyProof_2_4() throws NoSuchAlgorithmException {
        
        String[] VALUES = new String[]{
            "{\"data\":{\"alias\":\"Node1\",\"client_ip\":\"192.168.1.35\",\"client_port\":9702,\"node_ip\":\"192.168.1.35\",\"node_port\":9701,\"services\":[\"VALIDATOR\"]},\"dest\":\"Gw6pDLhcBcoQesN72qfotTgFa7cbuqZpkX3Xo6pLhPhv\",\"identifier\":\"FYmoFw55GeQH7SRFa37dkx1d2dZ3zUF8ckg7wmL7ofN4\",\"txnId\":\"fea82e10e894419fe2bea7d96296a6d46f50f93f9eeda954ec461b2ed2950b62\",\"type\":\"0\"}",
            "{\"data\":{\"alias\":\"Node2\",\"client_ip\":\"192.168.1.35\",\"client_port\":9704,\"node_ip\":\"192.168.1.35\",\"node_port\":9703,\"services\":[\"VALIDATOR\"]},\"dest\":\"8ECVSk179mjsjKRLWiQtssMLgp6EPhWXtaYyStWPSGAb\",\"identifier\":\"8QhFxKxyaFsJy4CyxeYX34dFH8oWqyBv1P4HLQCsoeLy\",\"txnId\":\"1ac8aece2a18ced660fef8694b61aac3af08ba875ce3026a160acbc3a3af35fc\",\"type\":\"0\"}"};
        
        MerkleTree tree = new MerkleTree();
        
        for(String txn : VALUES){
            tree.append(txn.getBytes());
        }
        
        List<byte[]> proofs = new ArrayList<>();
        proofs.add(new byte[]{
            (byte)0x26, (byte)0x06, (byte)0x53, (byte)0x99, (byte)0xf1, (byte)0xe9, (byte)0x0d, (byte)0xba,
            (byte)0x37, (byte)0xe1, (byte)0x86, (byte)0xd8, (byte)0x83, (byte)0x3c, (byte)0x07, (byte)0x21,
            (byte)0x26, (byte)0xe3, (byte)0xf4, (byte)0xdf, (byte)0xe6, (byte)0x03, (byte)0xe4, (byte)0x1b,
            (byte)0x41, (byte)0x27, (byte)0x1d, (byte)0x83, (byte)0x74, (byte)0x72, (byte)0x6f, (byte)0x74});
        
        proofs.add(new byte[]{
            (byte)0xf1, (byte)0xb0, (byte)0x51, (byte)0xa9, (byte)0x11, (byte)0x4b, (byte)0x69, (byte)0xa7,
            (byte)0x0f, (byte)0x82, (byte)0x91, (byte)0xe3, (byte)0x77, (byte)0xf0, (byte)0x78, (byte)0x1f,
            (byte)0x06, (byte)0x63, (byte)0xe6, (byte)0x5c, (byte)0x8b, (byte)0xbc, (byte)0x11, (byte)0xe9,
            (byte)0x00, (byte)0x74, (byte)0x8b, (byte)0xb7, (byte)0x55, (byte)0xf3, (byte)0xcd, (byte)0x6e});
        
        proofs.add(new byte[]{
            (byte)0x22, (byte)0x6c, (byte)0x66, (byte)0x53, (byte)0x08, (byte)0xe4, (byte)0xa8, (byte)0x5a,
            (byte)0x01, (byte)0x7d, (byte)0x52, (byte)0x24, (byte)0x24, (byte)0x17, (byte)0x91, (byte)0xdc,
            (byte)0xfa, (byte)0x9e, (byte)0x38, (byte)0x55, (byte)0x5a, (byte)0x38, (byte)0x7b, (byte)0x33,
            (byte)0x61, (byte)0x4d, (byte)0x7f, (byte)0x5a, (byte)0x68, (byte)0x72, (byte)0x60, (byte)0xd6});
        
        byte[] root = new byte[]{
            (byte)0x77, (byte)0xf1, (byte)0x5a, (byte)0x58, (byte)0x07, (byte)0xfd, (byte)0xaa, (byte)0x56,
            (byte)0x51, (byte)0x28, (byte)0xc5, (byte)0x8f, (byte)0x59, (byte)0x1f, (byte)0x4f, (byte)0x03,
            (byte)0x25, (byte)0x81, (byte)0xfe, (byte)0xe7, (byte)0xd8, (byte)0x61, (byte)0x99, (byte)0xae,
            (byte)0xf8, (byte)0xae, (byte)0xac, (byte)0x7b, (byte)0x05, (byte)0x80, (byte)0xbe, (byte)0x0a
        };

        int size = 4;
        
        assertTrue(tree.consistencyProof(root, size, proofs));
    }
    
    /**
     * Test of consistencyProof method, of class MerkleTree.
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void testConsistencyProof_4_8() throws NoSuchAlgorithmException {
        String[] VALUES = new String[]{
            "{\"data\":{\"alias\":\"Node1\",\"client_ip\":\"10.0.0.2\",\"client_port\":9702,\"node_ip\":\"10.0.0.2\",\"node_port\":9701,\"services\":[\"VALIDATOR\"]},\"dest\":\"Gw6pDLhcBcoQesN72qfotTgFa7cbuqZpkX3Xo6pLhPhv\",\"identifier\":\"FYmoFw55GeQH7SRFa37dkx1d2dZ3zUF8ckg7wmL7ofN4\",\"txnId\":\"fea82e10e894419fe2bea7d96296a6d46f50f93f9eeda954ec461b2ed2950b62\",\"type\":\"0\"}",
            "{\"data\":{\"alias\":\"Node2\",\"client_ip\":\"10.0.0.2\",\"client_port\":9704,\"node_ip\":\"10.0.0.2\",\"node_port\":9703,\"services\":[\"VALIDATOR\"]},\"dest\":\"8ECVSk179mjsjKRLWiQtssMLgp6EPhWXtaYyStWPSGAb\",\"identifier\":\"8QhFxKxyaFsJy4CyxeYX34dFH8oWqyBv1P4HLQCsoeLy\",\"txnId\":\"1ac8aece2a18ced660fef8694b61aac3af08ba875ce3026a160acbc3a3af35fc\",\"type\":\"0\"}",
            "{\"data\":{\"alias\":\"Node3\",\"client_ip\":\"10.0.0.2\",\"client_port\":9706,\"node_ip\":\"10.0.0.2\",\"node_port\":9705,\"services\":[\"VALIDATOR\"]},\"dest\":\"DKVxG2fXXTU8yT5N7hGEbXB3dfdAnYv1JczDUHpmDxya\",\"identifier\":\"2yAeV5ftuasWNgQwVYzeHeTuM7LwwNtPR3Zg9N4JiDgF\",\"txnId\":\"7e9f355dffa78ed24668f0e0e369fd8c224076571c51e2ea8be5f26479edebe4\",\"type\":\"0\"}",
            "{\"data\":{\"alias\":\"Node4\",\"client_ip\":\"10.0.0.2\",\"client_port\":9708,\"node_ip\":\"10.0.0.2\",\"node_port\":9707,\"services\":[\"VALIDATOR\"]},\"dest\":\"4PS3EDQ3dW1tci1Bp6543CfuuebjFrg36kLAUcskGfaA\",\"identifier\":\"FTE95CVthRtrBnK2PYCBbC9LghTcGwi9Zfi1Gz2dnyNx\",\"txnId\":\"aa5e817d7cc626170eca175822029339a444eb0ee8f0bd20d3b0b76e566fb008\",\"type\":\"0\"}",
            "{\"data\":{\"alias\":\"Node5\",\"client_ip\":\"10.0.0.2\",\"client_port\":9710,\"node_ip\":\"10.0.0.2\",\"node_port\":9709,\"services\":[\"VALIDATOR\"]},\"dest\":\"4SWokCJWJc69Tn74VvLS6t2G2ucvXqM9FDMsWJjmsUxe\",\"identifier\":\"5NekXKJvGrxHvfxbXThySmaG8PmpNarXHCf1CkwTLfrg\",\"txnId\":\"5abef8bc27d85d53753c5b6ed0cd2e197998c21513a379bfcf44d9c7a73c3a7e\",\"type\":\"0\"}",
            "{\"data\":{\"alias\":\"Node6\",\"client_ip\":\"10.0.0.2\",\"client_port\":9712,\"node_ip\":\"10.0.0.2\",\"node_port\":9711,\"services\":[\"VALIDATOR\"]},\"dest\":\"Cv1Ehj43DDM5ttNBmC6VPpEfwXWwfGktHwjDJsTV5Fz8\",\"identifier\":\"A2yZJTPHZyqJDELb8E1mhxUqWPEW5vgH2ePLTiTDQayp\",\"txnId\":\"a23059dc16aaf4513f97ca91f272235e809f8bda8c40f6688b88615a2c318ff8\",\"type\":\"0\"}",
            "{\"data\":{\"alias\":\"Node7\",\"client_ip\":\"10.0.0.2\",\"client_port\":9714,\"node_ip\":\"10.0.0.2\",\"node_port\":9713,\"services\":[\"VALIDATOR\"]},\"dest\":\"BM8dTooz5uykCbYSAAFwKNkYfT4koomBHsSWHTDtkjhW\",\"identifier\":\"6pYGZXnqXLxLAhrEBhVjyvuhnV2LUgM9iw1gHds8JDqT\",\"txnId\":\"e5f11aa7ec7091ca6c31a826eec885da7fcaa47611d03fdc3562b48247f179cf\",\"type\":\"0\"}",
            "{\"data\":{\"alias\":\"Node8\",\"client_ip\":\"10.0.0.2\",\"client_port\":9716,\"node_ip\":\"10.0.0.2\",\"node_port\":9715,\"services\":[\"VALIDATOR\"]},\"dest\":\"98VysG35LxrutKTNXvhaztPFHnx5u9kHtT7PnUGqDa8x\",\"identifier\":\"B4xQBURedpCS3r6v8YxTyz5RYh3Nh5Jt2MxsmtAUr1rH\",\"txnId\":\"2b01e69f89514be94ebf24bfa270abbe1c5abc72415801da3f0d58e71aaa33a2\",\"type\":\"0\"}"};
        
        List<Leaf> leaves = new ArrayList<>();
        for(String txn : VALUES){
            leaves.add(new Leaf(txn.getBytes()));
        }
        
        MerkleTree full = new MerkleTree(leaves).build();
        
        
        MerkleTree tree = new MerkleTree();
        for(int i = 0; i < 4; i++){
            tree.append(VALUES[i].getBytes());
        }
        
        String[] proofs_for_5 = new String[]{
            "9fVeiDkVJ4YrNB1cy9PEeRYXE5BhxapQsGu85WZ8MyiE",
            "8p6GotiwYFiWgjMvY7KYNYcbz6hCFBJhcD9Sjo1PQANU",
            "BqHByHYX9gAHye1SoKKiLXLFB7TDntyUoMtZQjMW2w7U",
            "BhXMcoxZ9eu3Cu85bzr4G4Msrw77BT3R6Mw6P6bM9wQe"};
        
        List<byte[]> proofs = new ArrayList<>();
        for(String proof : proofs_for_5){
            proofs.add(Base58.decode(proof));
        }
        
        tree.append(VALUES[4].getBytes());
        assertTrue(tree.consistencyProof(full.getHash(), 8, proofs));
        
        String[] proofs_for_6 = new String[]{
            "HhkWitSAXG12Ugn4KFtrUyhbZHi9XrP4jnbLuSthynSu",
            "BqHByHYX9gAHye1SoKKiLXLFB7TDntyUoMtZQjMW2w7U",
            "BhXMcoxZ9eu3Cu85bzr4G4Msrw77BT3R6Mw6P6bM9wQe"};
        
        proofs.clear();
        for(String proof : proofs_for_6){
            proofs.add(Base58.decode(proof));
        }
        
        tree.append(VALUES[5].getBytes());
        assertTrue(tree.consistencyProof(full.getHash(), 8, proofs));
        
        String[] proofs_for_7 = new String[]{
            "2D1aU5DeP8uPmaisGSpNoF2tNS35YhaRvfk2KPZzY2ue",
            "5cVBJRrdFraAtDzUhezeifS6W4Gsgo3TdPXs8847p95L",
            "HhkWitSAXG12Ugn4KFtrUyhbZHi9XrP4jnbLuSthynSu",
            "BhXMcoxZ9eu3Cu85bzr4G4Msrw77BT3R6Mw6P6bM9wQe"};
        
        proofs.clear();
        for(String proof : proofs_for_7){
            proofs.add(Base58.decode(proof));
        }
        
        tree.append(VALUES[6].getBytes());
        assertTrue(tree.consistencyProof(full.getHash(), 8, proofs));
        
        proofs.clear();
        tree.append(VALUES[7].getBytes());
        assertTrue(tree.consistencyProof(full.getHash(), 8, proofs));
    }

    /**
     * Test of serialize method, of class MerkleTree.
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void testSerialize() throws NoSuchAlgorithmException, IOException {
        List<Leaf> leaves = new ArrayList<>();
        for(int i = 1; i < 10; i++){
            Leaf leaf = new Leaf(Integer.toString(i).getBytes());
            leaves.add(leaf);
        }
        
        MerkleTree tree = new MerkleTree(leaves).build();
        String serialized = tree.serialize();
        
        MerkleTree result = tree.deserialize(serialized);
        assertArrayEquals(tree.root.hash, result.root.hash);
        assertArrayEquals(tree.leaves.toArray(new Leaf[tree.leaves.size()]), result.leaves.toArray(new Leaf[result.leaves.size()]));
        assertEquals(tree.height, result.height);
        assertEquals(tree.nodes_count, result.nodes_count);
    }
    
}
