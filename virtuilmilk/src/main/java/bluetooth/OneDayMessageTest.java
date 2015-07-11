package bluetooth;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/5/30.
 */
public class OneDayMessageTest extends BaseSendMessages {
    private static final String TAG = "sendMessage";
    private byte[] bytes;
    private static final int MAXLEN = 10;
    private int flags =0;
    Context cnt;
    public OneDayMessageTest(Context cnt){
        super();
        bytes = new byte[MAXLEN];
        flags = 0;
        setTitle();
        this.cnt =cnt;

        floatToData(bytes,getstartT(),DataVaild);
        setByte(bytes,4,1);

        floatToData(bytes,getendT(),DataVaild);
        setByte(bytes,4,5);

        floatToData(bytes,getamount(),DataVaild);
        setByte(bytes,4,9);

        floatToData(bytes,gettimeDuration(),DataVaild);
        setByte(bytes,4,13);

        floatToData(bytes,gettimejiange(),DataVaild);
        setByte(bytes,4,17);

        floatToData(bytes,getadvise(),DataVaild);
        setByte(bytes,4,21);

        setCRC(25);

        setEnd(27);


    }
    public void setStartT(float t){
        floatToData(bytes,t,DataVaild);
        setByte(bytes,4,1);
    }
    public void setEndT(float t){
        floatToData(bytes,t,DataVaild);
        setByte(bytes,4,5);
    }
    public void setamount(float t){
        floatToData(bytes,t,DataVaild);
        setByte(bytes,4,9);
    }
    public void setTimeDuration(float t){
        floatToData(bytes,t,DataVaild);
        setByte(bytes,4,13);
    }
    public void setjiange(float t){
        floatToData(bytes,t,DataVaild);
        setByte(bytes,4,17);
    }
    public void setadvise(float t){
        floatToData(bytes,t,DataVaild);
        setByte(bytes,4,21);
    }
    public int message2Bytes(byte[] buffer){
        return getBytes(buffer);
    }
    private void setTitle(){
        bytes[0] = (byte)0x01;
        setByte(bytes,1,0);
    }


    private float getstartT(){
        float temp =(float)Math.random()*10+35;
        Toast.makeText(cnt,String.valueOf(temp),1000).show();
        Log.i(TAG,String.valueOf(temp));
        return temp;
    }
    private float getendT(){
        float temp =(float)Math.random()*10+25;
        Toast.makeText(cnt,String.valueOf(temp),1000).show();
        Log.i(TAG,String.valueOf(temp));
        return temp;
    }
    private float getamount(){
        float temp =(float)Math.random()*10+150;
        Toast.makeText(cnt,String.valueOf(temp),1000).show();
        Log.i(TAG,String.valueOf(temp));
        return temp;
    }
    private float getadvise(){
        float temp =(float)Math.random()*10+150;
        Toast.makeText(cnt,String.valueOf(temp),1000).show();
        Log.i(TAG,String.valueOf(temp));
        return temp;
    }
    private float gettimeDuration(){
        float temp =(float)Math.random()*10+10;
        Toast.makeText(cnt,String.valueOf(temp),1000).show();
        Log.i(TAG,String.valueOf(temp));
        return temp;
    }
    private float gettimejiange(){
        return 0;
    }
}
