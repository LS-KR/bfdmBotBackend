package io.github.elihuso.bfdmBotBackend.logic;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.BitSet;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BloomFilter {
    private final BitSet hash;
    private final int hashSize;
    private final MessageDigest[] digests;

    public BloomFilter(int hashSize, MessageDigest... digests) {
        this.hash = new BitSet(hashSize);
        this.hashSize = hashSize;
        this.digests = digests;
    }

    public void add(byte[] data) throws IOException {
        for (MessageDigest digest : digests) {
            byte[] digest1 = digest.digest(data);
            int val = new DataInputStream(new ByteArrayInputStream(digest1)).readInt();
            int k = (val % hashSize > 0) ? val % hashSize : (((-val % hashSize + 2147483647 % hashSize + 1) < hashSize) ? (-val % hashSize + 2147483647 % hashSize + 1) : (-val % hashSize + 2147483647 % hashSize + 1 - hashSize));
            hash.set(k);
        }
    }

    public void del(byte[] data) throws IOException {
        for (MessageDigest digest : digests) {
            byte[] digest1 = digest.digest(data);
            int val = new DataInputStream(new ByteArrayInputStream(digest1)).readInt();
            int k = (val % hashSize > 0) ? val % hashSize : (((-val % hashSize + 2147483647 % hashSize + 1) < hashSize) ? (-val % hashSize + 2147483647 % hashSize + 1) : (-val % hashSize + 2147483647 % hashSize + 1 - hashSize));
            hash.set(k, false);
        }
    }

    public boolean mightHas(byte[] data) throws IOException {
        for (MessageDigest digest : digests) {
            byte[] digest1 = digest.digest(data);
            int val = new DataInputStream(new ByteArrayInputStream(digest1)).readInt();
            int k = (val % hashSize > 0) ? val % hashSize : (((-val % hashSize + 2147483647 % hashSize + 1) < hashSize) ? (-val % hashSize + 2147483647 % hashSize + 1) : (-val % hashSize + 2147483647 % hashSize + 1 - hashSize));
            if (!hash.get(k)) {
                return false;
            }
        }

        return true;
    }
    public BitSet getHash() {
        return hash;
    }

    public MessageDigest[] getDigests() {
        return digests;
    }

    public int getHashSize() {
        return hashSize;
    }

    public Integer[] getIntegerArray() {
        IntStream stream = hash.stream();
        Stream<Integer> boxed = stream.boxed();
        return boxed.toArray(Integer[]::new);
    }

    public void writeIntoFile(File file) throws IOException {
        Integer[] rawData = getIntegerArray();
        int[] data = new int[rawData.length];
        for (int i = 0; i < rawData.length; ++i) {
            data[i] = rawData[i].intValue();
        }
        ByteBuffer buffer = ByteBuffer.allocate(data.length * 4);
        IntBuffer intBuffer = buffer.asIntBuffer();
        intBuffer.put(data);
        byte[] rawFile = buffer.array();
        Files.write(file.toPath(), rawFile);
    }

    public void writeIntoFile(String path) throws IOException {
        writeIntoFile(new File(path));
    }

    public void writeIntoFile(Path path) throws IOException {
        writeIntoFile(path.toAbsolutePath().toString());
    }

    public void load(int[] data) {
        this.hash.clear();
        for (int i = 0; i < data.length; ++i) {
            hash.set(data[i]);
        }
    }

    public void loadFromFile(File file) throws IOException {
        byte[] rawFile = Files.readAllBytes(file.toPath());
        int[] data = new int[rawFile.length / 4];
        for (int i = 0; i < rawFile.length / 4; ++i) {
            byte[] b = new byte[4];
            for (int j = 0; j < 4; ++j) {
                b[j] = rawFile[i * 4 + j];
            }
            data[i] = ByteBuffer.wrap(b).getInt();
        }
        load(data);
    }

    public void loadFromFile(String path) throws IOException {
        loadFromFile(new File(path));
    }

    public void loadFromFile(Path path) throws IOException {
        loadFromFile(path.toAbsolutePath().toString());
    }
}