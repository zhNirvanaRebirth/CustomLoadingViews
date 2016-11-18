package lktower.zhwilson.com.loadingview;

/**
 * Created by zhwilson on 2016/11/16.
 * 叶子
 */
public class Leaf {
    //叶子的位置，因为叶子是图，canvas绘制Bitmap时是从左上角开始绘制
    float positionX;
    float positionY;
    //叶子的飘动曲线的振幅
    Amplitude amplitude;
    //叶子的旋转角度
    int rotateAngle;
    //叶子旋转方向 0：顺时针； 1：逆时针
    int rotateDir;
    //开始绘制时间
    long startTime;
    //漂浮曲线类型
    int floatType;
}
