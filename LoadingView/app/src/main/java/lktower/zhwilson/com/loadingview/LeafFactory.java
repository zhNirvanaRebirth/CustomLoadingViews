package lktower.zhwilson.com.loadingview;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zhwilson on 2016/11/16.
 * 用于产生一个或多个叶子
 */
public class LeafFactory {
    private static final int MAX_LEAFS = 6;
    private Random random = new Random();
    //若不设置这个时间，可能造成叶子抱团
    private int mAddTime;

    private Leaf generateLeaf() {
        Leaf leaf = new Leaf();
        //设置叶子飘动曲线的振幅
        switch (random.nextInt(3)) {
            case 1:
                leaf.amplitude = Amplitude.LITTLE;
                break;
            case 2:
                leaf.amplitude = Amplitude.LARGE;
                break;
            default:
                leaf.amplitude = Amplitude.MIDDLE;
                break;
        }
        //设置叶子起始的旋转角度
        leaf.rotateAngle = random.nextInt(360);
        //设置叶子的旋转方向
        leaf.rotateDir = random.nextInt(2);
        //设置绘制叶子的开始时间 要让叶子产生的时间间隔有一定的随机性，而不是间隔固定的时间就产生一片
        mAddTime += random.nextInt(2000);
        leaf.startTime = System.currentTimeMillis() + mAddTime;
        leaf.floatType = random.nextInt(2);
        return leaf;
    }

    public List<Leaf> generateLeafs(int leafSize) {
        List<Leaf> leafs = new ArrayList<>();
        for (int i = 0; i < leafSize; i++) {
            leafs.add(generateLeaf());
        }
        return leafs;
    }

    public List<Leaf> generateLeafs() {
        List<Leaf> leafs = new ArrayList<>();
        for (int i = 0; i < MAX_LEAFS; i++) {
            leafs.add(generateLeaf());
        }
        return leafs;
    }
}
