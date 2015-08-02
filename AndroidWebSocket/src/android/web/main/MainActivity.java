package android.web.main;

import android.app.Activity;
import static android.content.ContentValues.TAG;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.web.socket.WebSocket;
import android.web.socket.WebSocketConnection;
import android.web.socket.WebSocketConnectionHandler;
import android.web.socket.WebSocketException;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {

    private final WebSocket mConnection = new WebSocketConnection();

    private static final String PREFS_NAME = "AutobahnAndroidEcho";

    static EditText mHostname;
    static EditText mPort;
    static TextView mStatusline;
    static Button mStart;

    static EditText mMessage;
    static Button mSendMessage;

    private SharedPreferences mSettings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mHostname = (EditText) findViewById(R.id.hostname);
        mPort = (EditText) findViewById(R.id.port);
        mStatusline = (TextView) findViewById(R.id.statusline);
        mStart = (Button) findViewById(R.id.start);
        mMessage = (EditText) findViewById(R.id.msg);
        mSendMessage = (Button) findViewById(R.id.sendMsg);

        mSettings = getSharedPreferences(PREFS_NAME, 0);
        loadPrefs();
        setButtonConnect();
        mSendMessage.setEnabled(false);
        mMessage.setEnabled(false);

        mSendMessage.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                
                try {
                    String toString = mMessage.getText().toString();
                    
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("user", "usuarioAndroid");
                    jsonObject.put("message", toString);
                    
                    
                    mConnection.sendTextMessage(jsonObject.toString());
                } catch (JSONException ex) {
                    Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void loadPrefs() {
        mHostname.setText(mSettings.getString("hostname", ""));
        mPort.setText(mSettings.getString("port", "9000"));
    }

    private void alert(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    private void setButtonConnect() {
        mHostname.setEnabled(true);
        mPort.setEnabled(true);
        mStart.setText("Connect");
        mStart.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                start();
            }
        });
    }

    private void setButtonDisconnect() {
        mHostname.setEnabled(false);
        mPort.setEnabled(false);
        mStart.setText("Disconnect");
        mStart.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                mConnection.disconnect();
            }
        });
    }

    private void savePrefs() {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("hostname", mHostname.getText().toString());
        editor.putString("port", mPort.getText().toString());
        editor.commit();
    }

    private void start() {
        //final String wsuri = "ws://" + mHostname.getText() + ":" + mPort.getText();

        final String wsuri = "ws://10.0.2.2:8080/chat/echo";

        mStatusline.setText("Status: Connecting to " + wsuri + " ..");
        setButtonDisconnect();
        try {
            mConnection.connect(wsuri, new WebSocketConnectionHandler() {

                @Override
                public void onOpen() {
                    mStatusline.setText("Status: Connected to " + wsuri);
                    savePrefs();
                    mSendMessage.setEnabled(true);
                    mMessage.setEnabled(true);
                }

                @Override
                public void onBinaryMessage(byte[] payload) {
                    alert("Got echo: " + payload);
                }

                @Override
                public void onTextMessage(String payload) {
                    alert("Got echo: " + payload);
                }

                @Override
                public void onRawTextMessage(byte[] payload) {
                    alert("TESTE");
                }

                @Override
                public void onClose(int code, String reason) {
                    alert("Connection lost.");
                    mStatusline.setText("Status: Ready.");
                    setButtonConnect();
                    mSendMessage.setEnabled(false);
                    mMessage.setEnabled(false);
                }
            });
        } catch (WebSocketException e) {
            Log.d(TAG, e.toString());
        }
    }
}
