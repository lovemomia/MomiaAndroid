package com.youxing.duola.course;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.youxing.common.services.account.AccountChangeListener;
import com.youxing.common.services.account.AccountService;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.common.utils.Log;
import com.youxing.common.utils.UnitTools;
import com.youxing.common.views.CircleImageView;
import com.youxing.duola.R;
import com.youxing.duola.app.SGActivity;
import com.youxing.duola.course.views.BookConfirmChildListItem;
import com.youxing.duola.course.views.BookSkuListItem;
import com.youxing.duola.model.AccountModel;
import com.youxing.duola.model.CourseSkuListModel;
import com.youxing.duola.views.InputListItem;
import com.youxing.duola.views.SectionView;
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
 * Created by Jun Deng on 16/4/8.
 */
public class BookConfirmActivity extends SGActivity implements AdapterView.OnItemClickListener, AccountChangeListener {

    private final static int REQUEST_CODE_PICK_PHOTO = 1;
    private final static int REQUEST_CODE_CHOOSE_CHILD = 2;

    private CourseSkuListModel.CourseSku sku;
    private String pid;

    private Child child;
    private boolean hasChild;
    private UploadImage avatarImage;

    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        sku = getIntent().getParcelableExtra("sku");
        pid = getIntent().getData().getQueryParameter("pid");

        if (AccountService.instance().isLogin() && AccountService.instance().account().getChildren().size() > 0) {
            child = AccountService.instance().account().getChildren().get(0);
            hasChild = true;

        } else {
            child = new Child();
        }

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new Adapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        AccountService.instance().addListener(this);
    }

    @Override
    protected void onDestroy() {
        AccountService.instance().removeListener(this);
        super.onDestroy();
    }

    @Override
    public void onAccountChange(AccountService service) {
        if (AccountService.instance().isLogin() && AccountService.instance().account().getChildren().size() > 0) {
            child = AccountService.instance().account().getChildren().get(0);
            hasChild = true;

        } else {
            child = new Child();
            hasChild = false;
        }
        adapter.notifyDataSetChanged();
    }

    private void submit() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sid", String.valueOf(sku.getId())));
        params.add(new BasicNameValuePair("pid", pid));
        params.add(new BasicNameValuePair("cid", child.getId() > 0 ? String.valueOf(child.getId()) : ""));

        HttpService.post(Constants.domain() + "/course/booking", params, BaseModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();
                showDialog(BookConfirmActivity.this, "预约成功，您已被拉入该课群组，猛戳 “我的—我的群组” 就可以随意调戏我们的老师啦~", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("duola://mine"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                showDialog(BookConfirmActivity.this, error.getErrmsg());
            }
        });
    }

    private void requestUploadImage() {
        avatarImage.status = 1;

        HttpService.uploadImage(new File(avatarImage.filePath), new RequestHandler() {
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
                showDialog(BookConfirmActivity.this, error.getErrmsg());
                avatarImage.status = -1;
            }
        });
    }

    private void requestAddChild() {
        List<Child> children = new ArrayList<Child>();
        children.add(child);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("children", JSON.toJSONString(children)));
        HttpService.post(Constants.domain() + "/user/child", params, AccountModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                AccountModel model = (AccountModel) response;
                AccountService.instance().dispatchAccountChanged(model.getData());
                child = model.getData().getChildren().get(0);
                submit();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(BookConfirmActivity.this, error.getErrmsg());
            }
        });
    }



    private boolean check() {
        if (TextUtils.isEmpty(child.getName())) {
            showDialog(this, "孩子姓名不能为空！");
            return false;
        }
        if (TextUtils.isEmpty(child.getSex())) {
            showDialog(this, "孩子性别不能为空！");
            return false;
        }
        if (TextUtils.isEmpty(child.getBirthday())) {
            showDialog(this, "孩子生日不能为空！");
            return false;
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GroupStyleAdapter.IndexPath ip = adapter.getIndexForPosition(position);
        if (hasChild) {
            if (ip.section == 1 && ip.row == 0) {
                startActivityForResult("duola://childlist?select=true", REQUEST_CODE_CHOOSE_CHILD);
            }
        } else {
            if (ip.row == 0) {
                pickPhoto();
            } else if (ip.row == 2) {
                showSexChooseDialog();

            } else if (ip.row == 3) {
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
                new DatePickerDialog(BookConfirmActivity.this, new DatePickerDialog.OnDateSetListener() {
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
    }

    private void pickPhoto() {
        Intent intent = new Intent(BookConfirmActivity.this, PhotoPickerActivity.class);
        intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
        intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_SINGLE);
        startActivityForResult(intent, REQUEST_CODE_PICK_PHOTO);
    }

    private void showSexChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BookConfirmActivity.this);
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
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHOOSE_CHILD) {
                int childIndex = data.getIntExtra("childIndex", 0);
                child = AccountService.instance().account().getChildren().get(childIndex);
                adapter.notifyDataSetChanged();

            } else if (requestCode == REQUEST_CODE_PICK_PHOTO) {
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
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter(Context context) {
            super(context);
        }

        @Override
        public int getSectionCount() {
            return 3;
        }

        @Override
        public int getCountInSection(int section) {
            if (section == 0) {
                return 1;
            } else if (section == 1) {
                if (!hasChild) {
                    return 4;
                }
                return 1;
            }
            return 0;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            if (section == 0) {
                BookSkuListItem listItem = BookSkuListItem.create(BookConfirmActivity.this);
                listItem.setData(sku);
                return listItem;

            } else {
                if (!hasChild) {
                    View cell;
                    if (row == 0) {
                        cell = LayoutInflater.from(BookConfirmActivity.this).inflate(R.layout.layout_personinfo_avatar_item, null);
                        TextView title = (TextView) cell.findViewById(R.id.title);
                        title.setText("头像");
                        CircleImageView avatar = (CircleImageView) cell.findViewById(R.id.avatar);
                        avatar.setDefaultImageResId(R.drawable.ic_default_avatar);
                        avatar.setImageUrl(child.getAvatar());

                    } else {
                        if (row == 1) {
                            InputListItem item = InputListItem.create(BookConfirmActivity.this);
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
                            SimpleListItem item = SimpleListItem.create(BookConfirmActivity.this);
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

                } else {
                    BookConfirmChildListItem cell = BookConfirmChildListItem.create(BookConfirmActivity.this);
                    cell.setData(child);
                    return cell;
                }
            }
        }

        @Override
        public View getViewForSection(View convertView, ViewGroup parent, int section) {
            if (section == 0) {
                SectionView sectionView = SectionView.create(BookConfirmActivity.this);
                sectionView.setTitle("上课时间地点");
                return sectionView;


            } else if (section == 1) {
                SectionView sectionView = SectionView.create(BookConfirmActivity.this);
                sectionView.setTitle("出行宝宝");
                return sectionView;

            } else if (section == 2) {
                LinearLayout ll = new LinearLayout(BookConfirmActivity.this);
                int padding = UnitTools.dip2px(BookConfirmActivity.this, 20);
                ll.setPadding(padding, padding, padding, padding);
                Button payBtn = new Button(BookConfirmActivity.this);
                payBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                payBtn.setText("确认预约");
                payBtn.setTextSize(18);
                payBtn.setTextColor(getResources().getColor(R.color.white));
                payBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!hasChild) {
                            if (!check()) {
                                return;
                            }
                            showLoadingDialog(BookConfirmActivity.this);
                            requestAddChild();

                        } else {
                            showLoadingDialog(BookConfirmActivity.this);
                            submit();
                        }
                    }
                });
                padding = UnitTools.dip2px(BookConfirmActivity.this, 10);
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
