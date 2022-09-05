package DataStructure.Tree;

import java.io.*;
import java.util.*;

public class HuffmanTree {
    static int length = 10;
    static Map<Byte, String> result = new HashMap<>();

    public static void main(String[] args) {
        // HuffmanTree ht = new HuffmanTree();
        // int[] testA = {13,7,8,3,29,6,1};
        // HNode root = ht.creat(ArrayToNodeList(getArray()));
        // HNode root = ht.creat(ArrayToNodeList(testA));
        // preOrder(root);
        // String source = "i like like like java do you like a java";

        // List<HNode> nodes = StringToNodeList(source);
        // HNode root = creat(nodes);
        // preOrder(root);
        // StringBuilder sb = new StringBuilder();
        // getCode(root, "", sb);
        // result.forEach((k,v) -> System.out.println(k+" = "+v));
        // byte[] zip = zip(source.toCharArray(), result);
        // byte[] bytes = finalZip(source);
        // System.out.println(Arrays.toString(bytes));
        // System.out.println(unZip(result, bytes));

        String source = "C:\\Users\\zzp84\\Desktop\\test\\Python-2.pptx";
        String outFile = "C:\\Users\\zzp84\\Desktop\\test\\Python-2.zip";
        // zipFile(source, outFile);
        unZipFile(outFile, source);
    }

    public static void zipFile(String source, String outFile) {
        try (FileInputStream is = new FileInputStream(source);
            OutputStream os = new FileOutputStream(outFile);
            ObjectOutputStream oos = new ObjectOutputStream(os)
        ){
            byte[] bytes = new byte[is.available()];
            is.read(bytes);
            byte[] ok = finalZip(bytes);
            oos.writeObject(ok);
            oos.writeObject(result);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void unZipFile(String source, String outFile){
        try (FileInputStream is = new FileInputStream(source);
             ObjectInputStream ois = new ObjectInputStream(is);
             OutputStream os = new FileOutputStream(outFile);
        ){
            byte[] bytes = (byte[]) ois.readObject();
            Map<Byte, String> codeMap = (Map<Byte, String>) ois.readObject();
            byte[] s = unZip(codeMap, bytes);
            os.write(s);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static byte[] finalZip(String s){
        StringBuilder sb = new StringBuilder();
        List<HNode> nodes = StringToNodeList(s);
        HNode root = creat(nodes);
        getCode(root, "", sb);
        return zip(s.getBytes(), result);
    }

    public static byte[] finalZip(byte[] bytes){
        String s = new String(bytes);
        return finalZip(s);
    }

    public static byte[] unZip(Map<Byte, String> result, byte[] bytes){
        List<Byte> cache = new ArrayList<>();
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            boolean flag = (i != bytes.length - 1);
            s.append(byteToBitString(bytes[i],flag));
        }
        Map<String, Byte> reversResult = new HashMap<>();
        result.forEach((k,v) -> reversResult.put(v, k));

        for (int i = 0; i < s.length();) {
            int count = 1;
            boolean flag = true;
            while (flag) {
                String key = s.substring(i, i + count);
                Byte b = reversResult.get(key);
                if(b == null) {
                    count++;
                } else {
                    flag = false;
                    cache.add(b);
                    i += count;
                }
            }
        }
        byte[] b = new byte[cache.size()];
        for (int i = 0; i < cache.size(); i++) {
            b[i] = cache.get(i);
        }

        return b;
    }

    public static String byteToBitString(byte b, boolean flag){
        // 将b转为int
        int temp = b;
        // 判断是否需要补高位
        if(flag) temp |= 256;
        // 将temp转为二进制字符串
        String s = Integer.toBinaryString(temp);
        return flag ? s.substring(s.length() - 8) : s;
    }

    public static HNode creat(List<HNode> nodes){
        Collections.sort(nodes);
        while (nodes.size() > 1) {
            HNode left = nodes.remove(0);
            HNode right = nodes.remove(0);
            nodes.add(new HNode(left, right));
            // 由于可能会存在权值相同的情况，生成的树并不稳定相同，但是最后编码完毕后，长度一样
            Collections.sort(nodes);
        }
        return nodes.get(0);
    }

    public static byte[] zip(byte[] source, Map<Byte, String> result){
        // 根据编码表将原字符转位一串
        StringBuilder s = new StringBuilder();
        for (byte c : source) {
            s.append(result.get(c));
        }
        // 获取哈夫曼编码的Byte数组的长度
        int length = (s.length() + 7) / 8;
        // 创建压缩后的Byte数组
        byte[] huffCode = new byte[length];
        int index = 0;
        for (int i = 0; i < s.length(); i+=8) {
            String cache = i + 8 > s.length() ? s.substring(i) : s.substring(i, i+8);
            huffCode[index] = (byte) Integer.parseInt(cache, 2);
            index++;
        }

        return huffCode;
    }

    public static List<HNode> StringToNodeList(String s){
        List<HNode> nodes = new ArrayList<>();
        byte[] bytes = s.getBytes();
        Map<Byte, Integer> counts = new HashMap<>();
        for (Byte b : bytes) {
            Integer value = counts.get(b);
            var x = value == null ? counts.put(b, 1) : counts.put(b, ++value);
        }
        counts.forEach((k, v) -> nodes.add(new HNode(k,v)));
        return nodes;
    }

    public static void getCode(HNode node, String way, StringBuilder sb){
        StringBuilder s = new StringBuilder(sb);
        s.append(way);
        if(node != null) {
            if(node.aByte != null) {
                result.put(node.aByte, s.toString());
            } else {
                getCode(node.left, "0", s);
                getCode(node.right, "1", s);
            }
        }
    }

    public static void preOrder(HNode root){
        if(root == null) return;
        if(root.aByte != null) System.out.println(root);
        preOrder(root.left);
        preOrder(root.right);
    }

    public static List<HNode> ArrayToNodeList(int[] a){
        List<HNode> nodes = new ArrayList<>();
        for (int anInt : a) {
            HNode node = new HNode(anInt);
            nodes.add(node);
        }
        return nodes;
    }

    public static int[] getArray() {
        int[] arr = new int[length];
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            arr[i] = random.nextInt(length);
        }
        return arr;
    }
}

class HNode implements Comparable<HNode> {
    public int value;
    public Byte aByte;
    public HNode left;
    public HNode right;

    public HNode(int value) {
        this.value = value;
    }

    public HNode(Byte aByte, int value) {
        this.aByte = aByte;
        this.value = value;
    }

    public HNode(HNode left, HNode right) {
        this.value = left.value + right.value;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "HNode{" +
                "value=" + value +
                ", aByte=" + aByte +
                '}';
    }

    @Override
    public int compareTo(HNode o) {
        return this.value - o.value;
    }
}
