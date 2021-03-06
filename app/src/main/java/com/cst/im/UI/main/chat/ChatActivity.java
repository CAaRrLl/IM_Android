package com.cst.im.UI.main.chat;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cst.im.R;
import com.cst.im.presenter.ChatPresenter;
import com.cst.im.presenter.IChatPresenter;
import com.cst.im.view.IChatView;

/**
 * @author way
 */
public class ChatActivity extends Activity implements View.OnClickListener ,IChatView {
    private Button mBtnBack;// 返回btn
    private EditText mEditTextContent;//输入消息的栏
    private ListView mListView;//消息列表
    private ChatMsgViewAdapter mAdapter;// 消息视图的Adapter
    //抽象出聊天的业务逻辑
    private IChatPresenter chatPresenter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initView();// 初始化view

        //初始化数据（MVP）
        chatPresenter=new ChatPresenter(this);
        mAdapter = new ChatMsgViewAdapter(this, chatPresenter.LoadHisMsg());
        mListView.setAdapter(mAdapter);
        //消息列表选择到最后一行
        mListView.setSelection(mAdapter.getCount() - 1);

        /**
         * 监听EditText的回车事件
         * 发送消息
         */

        mEditTextContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    //发送消息
                    chatPresenter.SendMsg(mEditTextContent.getText().toString());
                }
                return false;
            }
        });
    }
    @Override
    public void onSendMsg(){
        mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变
        mEditTextContent.setText("");// 清空编辑框数据
        mListView.setSelection(mListView.getCount() - 1);// 发送一条消息时，ListView显示选择最后一项

    }

    /**
     * 初始化view
     */
    public void initView() {
        mListView = (ListView) findViewById(R.id.listview);
        mBtnBack = (Button) findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(this);
        mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回按钮点击事件
                finish();// 结束,实际开发中，可以返回主界面
                break;
        }
    }

}