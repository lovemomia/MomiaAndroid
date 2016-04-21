package com.youxing.duola.mine;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lling.photopicker.PhotoPickerActivity;
import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.model.Child;
import com.youxing.common.model.UploadImageModel;
import com.youxing.common.services.account.AccountService;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.common.utils.Log;
import com.youxing.common.utils.UnitTools;
import com.youxing.common.views.CircleImageView;
import com.youxing.duola.R;
import com.youxing.duola.app.SGActivity;
import com.youxing.duola.model.AccountModel;
import com.youxing.duola.views.InputListItem;
import com.youxing.duola.views.SimpleListItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Jun Deng on 16/4/5.
 */
public class ChildInfoActivity extends SGActivity implements AdapterView.OnItemClickListener {

    private final static int REQUEST_CODE_PICK_PHOTO = 1;

    private Adapter adapter;

    private boolean isAdd;
    private Child child;
    private UploadImage avatarImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new Adapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        String cid = getIntent().getData().getQueryParameter("cid");
        if (!TextUtils.isEmpty(cid)) {
            findChild(cid);

        } else {
            isAdd = true;
            child = new Child();
        }

        setTitle(isAdd ? "添加宝宝" : "宝宝信息");

    }

    private void findChild(String cid) {
        List<Child> children = AccountService.instance().account().getChildren();
        for (Child child : children) {
            if (cid.equals(String.valueOf(child.getId()))) {
                this.child = child;
                return;
            }
        }
    }

    private void submit() {
        if (TextUtils.isEmpty(child.getName())) {
            showDialog(this, "姓名不能为空");
            return;
        }
        requestAddChild();
    }

    private void requestAddChild() {
        showLoadingDialog(this);

        List<Child> children = new ArrayList<Child>();
        children.add(child);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("children", JSON.toJSONString(children)));
        HttpService.post(Constants.domain() + "/user/child", params, AccountModel.class, addChildHandler);
    }

    private RequestHandler addChildHandler = new RequestHandler() {
        @Override
        public void onRequestFinish(Object response) {
            dismissDialog();

            AccountModel model = (AccountModel) response;
            AccountService.instance().dispatchAccountChanged(model.getData());
            adapter.notifyDataSetChanged();
            finish();
        }

        @Override
        public void onRequestFailed(BaseModel error) {
            dismissDialog();
            showDialog(ChildInfoActivity.this, error.getErrmsg());
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GroupStyleAdapter.IndexPath indexPath = adapter.getIndexForPosition(position);
        if (indexPath.row == 0) {
            pickPhoto();
        } else if (indexPath.row == 2) {
            showSexChooseDialog();

        } else if (indexPath.row == 3) {
            // 生日
            Calendar cal = Calendar.getInstance();
            if (!TextUtils.isEmpty(child.getBirthday())) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = dateFormat.parse(child.getBirthday());
                    cal.setTime(date);
                } catch (Exception e) {
                    Log.e("PersonInfoActivity", "parse birthday fail", e);
                }
            }
            new DatePickerDialog(ChildInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(year, monthOfYear, dayOfMonth);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String birthday = dateFormat.format(cal.getTime());
                    child.setBirthday(birthday);
                    adapter.notifyDataSetChanged();
                }
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

    private void pickPhoto() {
        Intent intent = new Intent(ChildInfoActivity.this, PhotoPickerActivity.class);
        intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
        intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_SINGLE);
        startActivityForResult(intent, REQUEST_CODE_PICK_PHOTO);
    }

    private void showSexChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChildInfoActivity.this);
        builder.setItems(new String[]{"男", "女"}, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        child.setSex("男");
                        break;

                    case 1:
                        child.setSex("女");
                        break;
                }
                adapter.notifyDataSetChanged();
            }
        });
        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_PHOTO) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
                if (result.size() > 0) {
                    avatarImage = new UploadImage();
                    avatarImage.filePath = result.get(0);
                    requestUploadImage();
                }
            }
        }
    }

    private void requestUploadImage() {
        avatarImage.status = 1;

        HttpService.uploadImage(new File(avatarImage.filePath), uploadImageHandler);
    }

    private RequestHandler uploadImageHandler = new RequestHandler() {
        @Override
        public void onRequestFinish(Object response) {
            UploadImageModel uploadImageModel = (UploadImageModel) response;
            avatarImage.url = uploadImageModel.getData().getPath();
            avatarImage.status = 2;
            child.setAvatar(Constants.domainImage() + avatarImage.url);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onRequestFailed(BaseModel error) {
            dismissDialog();
            showDialog(ChildInfoActivity.this, error.getErrmsg());
            avatarImage.status = -1;
        }
    };

    @Override
    protected void onDestroy() {
        HttpService.abort(addChildHandler);
        HttpService.abort(uploadImageHandler);
        super.onDestroy();
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter(Context context) {
            super(context);
        }

        @Override
        public int getSectionCount() {
            return 2;
        }

        @Override
        public int getCountInSection(int section) {
            if (section == 0)
                return 4;
            return 0;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            View cell = null;
            if (row == 0) {
                cell = LayoutInflater.from(ChildInfoActivity.this).inflate(R.layout.layout_personinfo_avatar_item, null);
                TextView title = (TextView) cell.findViewById(R.id.title);
                title.setText("头像");
                CircleImageView avatar = (CircleImageView) cell.findViewById(R.id.avatar);
                avatar.setDefaultImageResId(R.drawable.ic_default_avatar);
                avatar.setImageUrl(child.getAvatar());

            } else {
                if (row == 1) {
                    InputListItem item = InputListItem.create(ChildInfoActivity.this);
                    item.setTitle("姓名");
                    item.setInputHint("请输入孩子姓名");
                    item.setInputChangeListener(new InputListItem.InputChangeListener() {
                        @Override
                        public void onInputChanged(InputListItem inputListItem, String textNow) {
                            child.setName(textNow);
                        }
                    });
                    item.setInputText(child.getName());
                    cell = item;

                } else {
                    SimpleListItem item = SimpleListItem.create(ChildInfoActivity.this);
                    item.setShowArrow(true);
                    if (row == 2) {
                        item.setTitle("性别");
                        item.setSubTitle(child.getSex());
                    } else {
                        item.setTitle("生日");
                        item.setSubTitle(child.getBirthday());
                    }
                    cell = item;

                }
            }
            return cell;
        }

        @Override
        public View getViewForSection(View convertView, ViewGroup parent, int section) {
            if (section == 1) {
                LinearLayout ll = new LinearLayout(ChildInfoActivity.this);
                int padding = UnitTools.dip2px(ChildInfoActivity.this, 20);
                ll.setPadding(padding, padding, padding, padding);
                Button payBtn = new Button(ChildInfoActivity.this);
                payBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                payBtn.setText(isAdd ? "确认添加" : "确认修改");
                payBtn.setTextSize(18);
                payBtn.setTextColor(getResources().getColor(R.color.white));
                payBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submit();
                    }
                });
                padding = UnitTools.dip2px(ChildInfoActivity.this, 10);
                payBtn.setPadding(padding, padding, padding, padding);
                payBtn.setBackgroundResource(R.drawable.btn_shape_green);
                ll.addView(payBtn);
                return ll;
            }
            return super.getViewForSection(convertView, parent, section);
        }
    }

    class UploadImage {
        String filePath;
        int status; // -1.failed 0.pending 1.uploading 2.finish
        String url;
    }
}
