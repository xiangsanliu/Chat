package com.xiang.chat.mvp.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.xiang.chat.R;
import com.xiang.chat.mvp.presenter.ChatPresenter;
import com.xiang.chat.mvp.view.ChatView;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, ChatView {

    EditText editText;
    ChatPresenter presenter;
    RecyclerView recyclerView;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        findViewById(R.id.send).setOnClickListener(this);
        editText = (EditText) findViewById(R.id.send_content);
        presenter = new ChatPresenter();
        presenter.attachView(this);
        presenter.onCreate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                presenter.send(editText.getText().toString());
                recyclerView.smoothScrollToPosition(presenter.adapter.getItemCount());
                break;
        }
    }

    @Override
    public void showProgress() {
        dialog.show();
    }

    @Override
    public void hideProgress() {
        dialog.dismiss();
    }

    @Override
    public void refreshEditText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                editText.setText("");
            }
        });
    }

    @Override
    public void showToast(final String content) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ChatActivity.this, content, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void initRecycerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(presenter.adapter);
            }
        });
    }

    @Override
    public void notifyRecyclerView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                presenter.adapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(presenter.adapter.getItemCount());
            }
        });
    }

    @Override
    public void showNameInputer() {
        createLoginInputer().create().show();
    }

    @Override
    public void showIpInputer() {
        createIpInputer().create().show();
    }

    @Override
    public void initProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("连接中...");
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        dialog.create();
    }

    private AlertDialog.Builder createLoginInputer() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_login_inputer, null);
        final EditText accountEditor = (EditText) view.findViewById(R.id.account);
        final EditText passwordEditor = (EditText) view.findViewById(R.id.password);
        return new AlertDialog.Builder(this)
                .setTitle("请输入账号和密码")
                .setView(view)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.setName(accountEditor.getText().toString());
                        presenter.send(accountEditor.getText().toString()
                                +":"+passwordEditor.getText().toString());
                    }
                })
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
    }

    private AlertDialog.Builder createIpInputer() {
        final View view = LayoutInflater.from(this).inflate(R.layout.dialog_ip_inputer, null);
        final EditText ipEditor = (EditText) view.findViewById(R.id.ip_edit);
        return new AlertDialog.Builder(this)
                .setTitle("请输入主机ip")
                .setView(view)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.getOutputStream(ipEditor.getText().toString());
                    }
                })
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
    }
}
