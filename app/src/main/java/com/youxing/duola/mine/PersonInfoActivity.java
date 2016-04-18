package com.youxing.duola.mine;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.common.app.Constants;
import com.youxing.common.model.Account;
import com.youxing.common.model.BaseModel;
import com.youxing.common.model.Child;
import com.youxing.common.model.UploadImageModel;
import com.youxing.common.services.account.AccountChangeListener;
import com.youxing.common.services.account.AccountService;
import com.youxing.common.services.http.CacheType;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.common.utils.UnitTools;
import com.youxing.common.views.CircleImageView;
import com.youxing.duola.R;
import com.youxing.duola.app.SGActivity;
import com.youxing.duola.model.AccountModel;
import com.youxing.duola.utils.PhotoPicker;
import com.youxing.duola.views.SimpleListItem;
import com.youxing.duola.views.StepperView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 15/8/24.
 */
public class PersonInfoActivity extends SGActivity implements StepperView.OnNumberChangedListener,
        AdapterView.OnItemClickListener, AccountChangeListener {

    private ListView listView;
    private Adapter adapter;

    private Account account;
    private PhotoPicker photoPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        account = AccountService.instance().account();

        listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        adapter = new Adapter();
        listView.setAdapter(adapter);

        requestData();

        AccountService.instance().addListener(this);
    }

    @Override
    protected void onDestroy() {
        AccountService.instance().removeListener(this);
        super.onDestroy();
    }

    public void requestData() {
        showLoadingDialog(this);

        HttpService.get(Constants.domain() + "/user", null, CacheType.DISABLE, AccountModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();

                AccountModel model = (AccountModel) response;
                PersonInfoActivity.this.account = model.getData();
                AccountService.instance().dispatchAccountChanged(model.getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(PersonInfoActivity.this, error.getErrmsg(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GroupStyleAdapter.IndexPath indexPath = adapter.getIndexForPosition(position);
        int section = indexPath.section;
        int row = indexPath.row;
        if (section == 0) {
            if (row == 0) {
                pickPhoto();
            } else if (row == 1) {
                // 昵称
                showInputDialog("请输入昵称", new OnInputDoneListener() {
                    @Override
                    public void onInputDone(String text) {
                        requestUpdateNickname(text);
                    }
                });
            }
        } else if (section == 1) {
            if (row == 0) {
                // 性别
                showSexChooseDialog(new OnInputDoneListener() {
                    @Override
                    public void onInputDone(String text) {
                        requestUpdateSex(text);
                    }
                });
            } else {
                // 常住地
                showInputDialog("请输入常住地", new OnInputDoneListener() {
                    @Override
                    public void onInputDone(String text) {
                        requestUpdateAddress(text);
                    }
                });
            }
        } else {
            startActivity("duola://childlist");
        }
    }

    private void pickPhoto() {
        if (photoPicker == null) {
            photoPicker = new PhotoPicker(PersonInfoActivity.this);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonInfoActivity.this);
        builder.setItems(new String[]{"拍照", "从手机相册里选择"}, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // take a new photo
                        photoPicker.doTakePhoto();
                        break;

                    case 1:
                        // pick a photo from gallery
                        photoPicker.doPickPhotoFromGallery();
                        break;
                }
            }
        });
        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String strImgPath;
            switch (requestCode) {
                case PhotoPicker.REQUEST_CODE_CAMERA:
                    strImgPath = photoPicker.strImgPath();
                    if (!TextUtils.isEmpty(strImgPath)) {
                        File f = new File(strImgPath);
                        if (!f.exists()) {
                            strImgPath = photoPicker.parseImgPath(data);
                        }

                    } else {
                        strImgPath = photoPicker.parseImgPath(data);
                    }
                    if (!TextUtils.isEmpty(strImgPath)) {
                        photoPicker.doCropPhoto();
                    }
                    break;
                case PhotoPicker.REQUEST_CODE_PHOTO_PICKED:
                    if (Build.VERSION.SDK_INT < 19) {
                        strImgPath = photoPicker.parseImgPath(data);
                        if (!TextUtils.isEmpty(strImgPath)) {
                            requestUploadImage(new File(strImgPath));
                        }
                    } else {
                        if (data != null) {
                            strImgPath = photoPicker.getPath(PersonInfoActivity.this, data.getData());
                            requestUploadImage(new File(strImgPath));
                        }
                    }
                    break;
            }
        }
    }

    private void requestUploadImage(File file) {
        showLoadingDialog(this);

        HttpService.uploadImage(file, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                UploadImageModel model = (UploadImageModel) response;
                requestUpdateAvatar(model.getData().getPath());
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(PersonInfoActivity.this, error.getErrmsg());
            }
        });
    }

    private void requestUpdateAvatar(String url) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("avatar", url));
        HttpService.post(Constants.domain() + "/user/avatar", params, AccountModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();

                AccountModel model = (AccountModel) response;
                PersonInfoActivity.this.account = model.getData();
                AccountService.instance().dispatchAccountChanged(model.getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(PersonInfoActivity.this, error.getErrmsg());
            }
        });
    }

    private void requestUpdateNickname(String nickname) {
        showLoadingDialog(this);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("nickname", nickname));
        HttpService.post(Constants.domain() + "/user/nickname", params, AccountModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();

                AccountModel model = (AccountModel) response;
                PersonInfoActivity.this.account = model.getData();
                AccountService.instance().dispatchAccountChanged(model.getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(PersonInfoActivity.this, error.getErrmsg());
            }
        });
    }

    private void requestUpdateSex(String sex) {
        showLoadingDialog(this);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sex", sex));
        HttpService.post(Constants.domain() + "/user/sex", params, AccountModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();

                AccountModel model = (AccountModel) response;
                PersonInfoActivity.this.account = model.getData();
                AccountService.instance().dispatchAccountChanged(model.getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(PersonInfoActivity.this, error.getErrmsg());
            }
        });
    }

    private void requestUpdateAddress(String address) {
        showLoadingDialog(this);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("address", address));
        HttpService.post(Constants.domain() + "/user/address", params, AccountModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();

                AccountModel model = (AccountModel) response;
                PersonInfoActivity.this.account = model.getData();
                AccountService.instance().dispatchAccountChanged(model.getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(PersonInfoActivity.this, error.getErrmsg());
            }
        });
    }

    private void requestAddChild() {
        showLoadingDialog(this);

        Child child = new Child();
        child.setName(AccountService.instance().account().getNickName() + "的宝宝");
        child.setSex("男");
        child.setBirthday("2015-07-01");
        List<Child> children = new ArrayList<Child>();
        children.add(child);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("children", JSON.toJSONString(children)));
        HttpService.post(Constants.domain() + "/user/child", params, AccountModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();

                AccountModel model = (AccountModel) response;
                PersonInfoActivity.this.account = model.getData();
                AccountService.instance().dispatchAccountChanged(model.getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(PersonInfoActivity.this, error.getErrmsg());
            }
        });
    }

    private void requestDelChild() {
        showLoadingDialog(this);

        Child child = account.getChildren().get(account.getChildren().size() - 1);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("cid", String.valueOf(child.getId())));
        HttpService.post(Constants.domain() + "/user/child/delete", params, AccountModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();

                AccountModel model = (AccountModel) response;
                PersonInfoActivity.this.account = model.getData();
                AccountService.instance().dispatchAccountChanged(model.getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(PersonInfoActivity.this, error.getErrmsg());
            }
        });
    }

    private void showInputDialog(String title, final OnInputDoneListener listener) {
        final EditText input = new EditText(this);
        new AlertDialog.Builder(this).setTitle(title)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(input).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onInputDone(input.getText().toString());
            }
        }).setNegativeButton("取消", null).show();
    }

    private void showSexChooseDialog(final OnInputDoneListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonInfoActivity.this);
        builder.setItems(new String[]{"男", "女"}, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        listener.onInputDone("男");
                        break;

                    case 1:
                        listener.onInputDone("女");
                        break;
                }
            }
        });
        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @Override
    public void onNumberChanged(StepperView stepper) {
        int number = stepper.getNumber();
        if (number < account.getChildren().size()) {
            requestDelChild();
        } else if (number > account.getChildren().size()) {
            requestAddChild();
        }
    }

    @Override
    public void onAccountChange(AccountService service) {
        account = AccountService.instance().account();
        if (account == null) {
            finish();
            return;
        }
        adapter.notifyDataSetChanged();
    }

    interface OnInputDoneListener {
        void onInputDone(String text);
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(PersonInfoActivity.this);
        }

        @Override
        public int getSectionCount() {
            return 4;
        }

        @Override
        public int getCountInSection(int section) {
            if (section == 0) {
                return 3;
            } else if (section == 1) {
                return 2;
            } else if (section == getSectionCount() - 1) {
                return 0;
            }
            return 1;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            View view = null;
            if (section == 0 && row == 0) {
                view = LayoutInflater.from(PersonInfoActivity.this).inflate(R.layout.layout_personinfo_avatar_item, null);
                TextView title = (TextView) view.findViewById(R.id.title);
                title.setText("头像");
                CircleImageView avatar = (CircleImageView) view.findViewById(R.id.avatar);
                avatar.setDefaultImageResId(R.drawable.ic_default_avatar);
                avatar.setImageUrl(account == null ? "" : account.getAvatar());

            } else {
                SimpleListItem item = SimpleListItem.create(PersonInfoActivity.this);
                item.setShowArrow(true);
                if (section == 0) {
                    if (row == 1) {
                        item.setTitle("昵称");
                        item.setSubTitle(account == null ? "" : account.getNickName());

                    } else if (row == 2) {
                        item.setTitle("手机号");
                        item.setSubTitle(account == null ? "" : account.getMobile());
                        item.setShowArrow(false);
                    }

                } else if (section == 1) {
                    if (row == 0) {
                        item.setTitle("性别");
                        item.setSubTitle(account == null ? "" : account.getSex());
                    } else {
                        item.setTitle("常住地");
                        item.setSubTitle(account == null ? "" : account.getAddress());
                    }

                } else {
                    item.setTitle("出行宝宝");
                    item.setSubTitle(account == null ? "" : account.getChildren().size() + "个");
                }
                view = item;
            }
            return view;
        }

        @Override
        public View getViewForSection(View convertView, ViewGroup parent, int section) {
            if (section == getSectionCount() - 1) {
                LinearLayout ll = new LinearLayout(PersonInfoActivity.this);
                int padding = UnitTools.dip2px(PersonInfoActivity.this, 20);
                ll.setPadding(padding, padding, padding, padding);
                Button payBtn = new Button(PersonInfoActivity.this);
                payBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                payBtn.setText("退出登录");
                payBtn.setTextSize(18);
                payBtn.setTextColor(getResources().getColor(R.color.white));
                payBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(PersonInfoActivity.this, null, "确认退出已登录账号？", "确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AccountService.instance().dispatchAccountChanged(null);
                                finish();
                            }
                        }, "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                    }
                });
                padding = UnitTools.dip2px(PersonInfoActivity.this, 10);
                payBtn.setPadding(padding, padding, padding, padding);
                payBtn.setBackgroundResource(R.drawable.btn_shape_green);
                ll.addView(payBtn);
                return ll;
            }
            return super.getViewForSection(convertView, parent, section);
        }
    }

}
