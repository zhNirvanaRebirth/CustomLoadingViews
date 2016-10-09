import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhwilson on 2016/10/9.
 * 模仿别人的炫酷的进度条
 *
 * obtainStyledAttributes 四个参数的相关解释：http://www.cnblogs.com/angeldevil/p/3479431.html
 */
public class NirvanaLoadingView extends View{

    public NirvanaLoadingView(Context context) {
        this(context, null);
    }

    public NirvanaLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NirvanaLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
